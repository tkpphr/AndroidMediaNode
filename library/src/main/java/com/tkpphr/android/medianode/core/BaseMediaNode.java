/*
   Copyright 2018 tkpphr

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.tkpphr.android.medianode.core;

import android.content.Context;
import android.text.TextUtils;

import com.tkpphr.android.medianode.R;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseMediaNode<T extends MediaNode<T>> implements MediaNode<T>,Serializable{
    private String nodeName;
    private T parent;
    private List<T> children;
    private String imagePath;
    private String soundPath;
    private static final long serialVersionUID = 6985608118246672383L;

    public BaseMediaNode(String nodeName) {
        this.nodeName = nodeName;
        parent = null;
        children = new ArrayList<>();
    }
    
    public BaseMediaNode(T parent, String nodeName) {
        this.nodeName = nodeName;
        this.parent = parent;
        children = new ArrayList<>();
    }
    
    public BaseMediaNode(String fullPath,T parent) {
        String[] nodeName = fullPath.split("\\\\");
        this.nodeName = nodeName[nodeName.length-1];
        this.parent = parent;
        children = new ArrayList<>();
    }

    @Override
    public String getNodeName() {
        return nodeName;
    }

    @Override
    public void setNodeName(String nodeName) {
        this.nodeName=nodeName;
    }

    @Override
    public String getFullPath() {
        return createFullPath(getParent(),nodeName);
    }

    @Override
    public T getParent() {
        return parent;
    }

    @Override
    public int getChildCount(){
        return children.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getRoot(){
        if(parent ==null){
            return (T)this;
        }else {
            return parent.getRoot();
        }
    }

    @Override
    public T getFirstChild(){
        if(getChildCount()<1){
            return null;
        }
        return children.get(0);
    }

    @Override
    public T getLastChild(){
        if(getChildCount()<1){
            return null;
        }
        return children.get(getChildCount()-1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T find(String path) {
        if (getFullPath().equals(path)) {
            return (T)this;
        } else {
            if (children.size() > 0) {
                for (T node : children) {
                    if (path.startsWith(node.getFullPath())) {
                        if (path.equals(node.getFullPath())) {
                            return node;
                        } else {
                            T result;
                            result = node.find(path);
                            if (result != null) {
                                return result;
                            }
                        }
                    }
                }
                return null;
            } else {
                return null;
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll(MediaNodeFilter<T> filter){
        List<T> foundList=new ArrayList<>();
        findAllRecursive((T)this,foundList, filter);
        return foundList;
    }

    @Override
    public T getChild(int index){
        return children.get(index);
    }

    @Override
    public List<T> getChildren(){ return Collections.unmodifiableList(children); }

    @Override
    public void addChild(T node) {
        children.add(node);
    }

    @Override
    public void addChild(int index, T node){
        children.add(index,node);
    }

    @Override
    public void removeChild(T deleteNode) {
        children.remove(deleteNode);
    }

    @Override
    public void clearChildren() {
        children.clear();
    }

    @Override
    public int indexOf(T node) {
        if (node == null)
        {
            return -1;
        }
        return children.indexOf(node);
    }

    @Override
    public String getNodeInfo(Context context) {
        int ellipseLen=15;
        return getFullPathInfo(context,ellipseLen)+"\n"+
                getImagePathInfo(context,ellipseLen)+"\n"+
                getSoundPathInfo(context,ellipseLen);
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String getSoundPath() {
        return soundPath;
    }

    @Override
    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }

    protected String getFullPathInfo(Context context, int ellipseLen){
        String fullPath=getFullPath();
        if(fullPath.length()>ellipseLen){
            fullPath="..."+fullPath.substring(fullPath.length()-ellipseLen,fullPath.length());
        }
        return context.getString(R.string.mdnd_path)+" : "+fullPath;
    }

    protected String getImagePathInfo(Context context,int ellipseLen){
        String imagePath=getImagePath();
        File imageFile=new File(imagePath);
        if(imageFile.exists()){
            imagePath= imageFile.getName();
        }else if(TextUtils.isEmpty(imagePath) || TextUtils.equals(getFullPath(),imagePath)){
            imagePath=context.getString(R.string.mdnd_none);
        }
        if(imagePath.length()>ellipseLen){
            imagePath="..."+imagePath.substring(imagePath.length()-ellipseLen,imagePath.length());
        }
        return context.getString(R.string.mdnd_image)+" : "+imagePath;
    }

    protected String getSoundPathInfo(Context context,int ellipseLen){
        String soundPath=getSoundPath();
        File soundFile=new File(soundPath);
        if(soundFile.exists()){
            soundPath= soundFile.getName();
        }else if(TextUtils.isEmpty(soundPath) || TextUtils.equals(getFullPath(),soundPath)){
            soundPath=context.getString(R.string.mdnd_none);
        }
        if(soundPath.length()>ellipseLen){
            soundPath="..."+soundPath.substring(soundPath.length()-ellipseLen,soundPath.length());
        }
        return context.getString(R.string.mdnd_sound)+" : "+soundPath;
    }

    private String createFullPath(T parent, String path){
        if(parent==null){
            return path;
        }else {
            return createFullPath(parent.getParent(),parent.getNodeName()+"\\"+path);
        }
    }

    private void findAllRecursive(T parent,List<T> foundList,MediaNodeFilter<T> filter){
        if(filter.apply(parent)) {
            foundList.add(parent);
        }
        for(T child : parent.getChildren()) {
            findAllRecursive(child, foundList, filter);
        }
    }
}
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
package com.tkpphr.android.medianode.util;

import android.text.TextUtils;

import com.tkpphr.android.medianode.core.MediaNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MediaNodeSelector{
    private MediaNode<?> rootNode;
    private LinkedList<MediaNode<?>> selectedNodeStack;
    private List<Callback> callbacks;

    public MediaNodeSelector(MediaNode<?> node) {
        this(new ArrayList<MediaNode<?>>(Arrays.asList(node)));
    }

    public MediaNodeSelector(List<MediaNode<?>> selectedNodes) {
        if(selectedNodes.size()==0){
            throw new IllegalArgumentException("selectedNodes size is 0");
        }
        this.selectedNodeStack=new LinkedList<>(selectedNodes);
        this.rootNode=this.selectedNodeStack.peek().getRoot();
        this.callbacks =new ArrayList<>();
    }

    public MediaNode<?> getRootNode() {
        return rootNode;
    }

    public MediaNode<?> getCurrentNode(){
        return selectedNodeStack.size()==0 ? null :selectedNodeStack.peek();
    }

    public List<MediaNode<?>> getSelectedNodes(){
        return Collections.unmodifiableList(selectedNodeStack);
    }

    public int getCount(){
        return selectedNodeStack.size();
    }

    public void addCallback(Callback callback){
        callbacks.add(callback);
    }

    public void removeCallback(Callback callback){
        callbacks.remove(callback);
    }

    @SuppressWarnings("unchecked")
    public void pushNode(MediaNode<?> node){
        if(TextUtils.equals(node.getFullPath(),getCurrentNode().getFullPath())) {
            return;
        }
        selectedNodeStack.push(node);
        onCurrentNodeChanged();
    }

    public void moveToAncestorPath(int depth){
        String[] pathNames=getCurrentNode().getFullPath().split("\\\\");
        if(depth >= pathNames.length){
            return;
        }
        StringBuilder ancestorPath= new StringBuilder();
        ancestorPath.append(pathNames[0]);
        for(int i=1;i<depth+1;i++){
            ancestorPath.append("\\");
            ancestorPath.append(pathNames[i]);
        }
        MediaNode<?> ancestorNode=rootNode.find(new String(ancestorPath));
        if(ancestorNode!=null){
            pushNode(ancestorNode);
        }
    }

    @SuppressWarnings("unchecked")
    public boolean back(){
        Iterator<MediaNode<?>> iterator=selectedNodeStack.iterator();
        while (iterator.hasNext()){
            if(rootNode.find(iterator.next().getFullPath())==null){
                iterator.remove();
            }
        }
        MediaNode<?> previousNode=selectedNodeStack.pop();
        while (getCount()>1 && previousNode==getCurrentNode()) {
            previousNode=selectedNodeStack.pop();
        }
        if(selectedNodeStack.size()>=1) {
            onCurrentNodeChanged();
            return true;
        }else {
            selectedNodeStack.push(previousNode);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public void shiftNodeUp(MediaNode<?> node){
        MediaNode<?> dstNode;
        if(node.getChildCount()<=0 || node.getParent().getChildCount()>=2){
            dstNode=node.getParent();
        }else {
            dstNode=node.getParent().getParent();
        }

        if(dstNode==null){
            dstNode=node.getRoot();
        }
        pushNode(dstNode);
    }

    @SuppressWarnings("unchecked")
    public void shiftNodeDown(MediaNode<?> node){
        MediaNode<?> dstNode;
        if(node.getChildCount()<2){
            if(node.getFirstChild().getChildCount()==0){
                dstNode=node;
            }else {
                dstNode=node.getFirstChild();
            }
        }else {
            dstNode=node;
        }

        if(!TextUtils.equals(dstNode.getFullPath(),getCurrentNode().getFullPath())) {
            pushNode(dstNode);
        }
    }

    private void onCurrentNodeChanged(){
        for(Callback callback : callbacks){
            callback.onCurrentNodeChanged(getCurrentNode());
        }
    }

    public interface Callback {
        void onCurrentNodeChanged(MediaNode<?> currentNode);
    }
}

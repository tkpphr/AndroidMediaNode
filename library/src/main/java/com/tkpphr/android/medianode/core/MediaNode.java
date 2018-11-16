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
import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;


public interface MediaNode<T extends MediaNode<T>> extends Serializable {
    String getNodeName();
    void setNodeName(String nodeName);
    String getFullPath();
    T getParent();
    int getChildCount();
    T getRoot();
    T getFirstChild();
    T getLastChild();
    T find(String path);
    List<T> findAll(MediaNodeFilter<T> filter);
    T getChild(int index);
    List<T> getChildren();
    void addChild(T node);
    void addChild(int index, T node);
    void removeChild(T deleteNode);
    void clearChildren();
    int indexOf(T node);
    String getNodeInfo(Context context);
    String getImagePath();
    void setImagePath(String imagePath);
    String getSoundPath();
    void setSoundPath(String soundPath);
    Bitmap getImage();
    MediaNodeSound getSound();

}
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
package com.tkpphr.android.medianode.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.tkpphr.android.medianode.core.BaseMediaNode;
import com.tkpphr.android.medianode.core.MediaNode;
import com.tkpphr.android.medianode.core.MediaNodeFilter;
import com.tkpphr.android.medianode.core.MediaNodeSound;

public class SelectableMediaNodeImpl extends BaseMediaNode<SelectableMediaNodeImpl> implements SelectableMediaNode<SelectableMediaNodeImpl> {
    private MediaNode<?> mediaNode;
    private boolean selectable;

    public SelectableMediaNodeImpl(MediaNode<?> mediaNode, boolean selectable) {
        super(mediaNode.getNodeName());
        this.mediaNode = mediaNode;
        this.selectable = selectable;
    }

    public SelectableMediaNodeImpl(SelectableMediaNodeImpl parent, MediaNode<?> mediaNode, boolean selectable) {
        super(parent, mediaNode.getNodeName());
        this.mediaNode = mediaNode;
        this.selectable = selectable;
    }

    @Override
    public String getNodeName() {
        return mediaNode.getNodeName();
    }

    @Override
    public String getNodeInfo(Context context) {
        return mediaNode.getNodeInfo(context);
    }

    @Override
    public Bitmap getImage() {
        return mediaNode.getImage();
    }

    @Override
    public MediaNodeSound getSound() {
        return mediaNode.getSound();
    }

    public MediaNode<?> getMediaNode() {
        return mediaNode;
    }

    public boolean isSelectable() {
        return selectable;
    }

    @SuppressWarnings("unchecked")
    public static SelectableMediaNodeImpl createSelectorNode(MediaNode<?> node, MediaNodeFilter filter){
        SelectableMediaNodeImpl selectorNode =new SelectableMediaNodeImpl(node,filter.apply(node));
        createSelectorNodeRecursive(selectorNode,filter);
        return selectorNode;
    }

    @SuppressWarnings("unchecked")
    private static void createSelectorNodeRecursive(SelectableMediaNodeImpl node, MediaNodeFilter filter){
        MediaNode<?> mediaNode=node.getMediaNode();
        for (MediaNode<?> childNode : mediaNode.getChildren()){
            SelectableMediaNodeImpl childSelectorNode =new SelectableMediaNodeImpl(node,childNode,filter.apply(childNode));
            node.addChild(childSelectorNode);
            createSelectorNodeRecursive(childSelectorNode,filter);
        }
    }

}
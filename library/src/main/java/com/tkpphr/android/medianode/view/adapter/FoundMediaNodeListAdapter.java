package com.tkpphr.android.medianode.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tkpphr.android.medianode.core.MediaNode;
import com.tkpphr.android.medianode.core.MediaNodeFilter;
import com.tkpphr.android.medianode.util.MediaNodeSelector;
import com.tkpphr.android.medianode.view.customview.FoundMediaNodeInfoView;
import com.tkpphr.android.medianode.view.customview.OnJumpMediaNodeListener;
import com.tkpphr.android.medianode.view.customview.OnMediaNodeSelectedListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FoundMediaNodeListAdapter extends BaseAdapter{
    private Context context;
    private String searchString;
    private MediaNodeSelector mediaNodeSelector;
    private List<MediaNode<?>> foundList;
    private OnMediaNodeSelectedListener onMediaNodeSelectedListener;
    private OnJumpMediaNodeListener onJumpMediaNodeListener;

    public FoundMediaNodeListAdapter(Context context) {
        this.context = context;
        this.foundList=new ArrayList<>();
        this.searchString="";
    }

    @Override
    public int getCount() {
        if(foundList.size() < 1){
            return 0;
        }else {
            return foundList.size();
        }
    }

    @Override
    public MediaNode<?> getItem(int i) {
        return foundList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void setOnMediaNodeSelectedListener(OnMediaNodeSelectedListener onMediaNodeThumbnailClickListener) {
        this.onMediaNodeSelectedListener = onMediaNodeThumbnailClickListener;
        notifyDataSetChanged();
    }

    public void setOnJumpMediaNodeListener(OnJumpMediaNodeListener onJumpMediaNodeListener) {
        this.onJumpMediaNodeListener = onJumpMediaNodeListener;
    }

    public void refresh(){
        Iterator<MediaNode<?>> iterator=foundList.iterator();
        while (iterator.hasNext()){
            MediaNode mediaNode=iterator.next();
            if(!mediaNode.getNodeName().toLowerCase().contains(searchString.toLowerCase())){
                iterator.remove();
            }
        }
        notifyDataSetChanged();
    }

    @SuppressWarnings("unchecked")
    public void reset(MediaNodeSelector mediaNodeSelector,final String searchString){
        this.foundList.clear();
        if(!TextUtils.isEmpty(searchString)) {
            List<MediaNode<?>> foundList=mediaNodeSelector.getCurrentNode().getRoot().findAll(new MediaNodeFilter() {
                @Override
                public boolean apply(MediaNode mediaNode) {
                    return mediaNode.getNodeName().toLowerCase().contains(searchString.toLowerCase());
                }
            });
            this.foundList.addAll(foundList);
            this.mediaNodeSelector = mediaNodeSelector;
            this.searchString = searchString;
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if(convertView==null){
            convertView=new FoundMediaNodeInfoView(context, null);
        }
        ((FoundMediaNodeInfoView)convertView).setMediaNode(mediaNodeSelector,getItem(position),searchString);
        ((FoundMediaNodeInfoView)convertView).setOnMediaNodeSelectedListener(onMediaNodeSelectedListener);
        ((FoundMediaNodeInfoView)convertView).setOnJumpMediaNodeListener(onJumpMediaNodeListener);
        return convertView;
    }
}

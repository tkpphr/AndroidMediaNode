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
package com.tkpphr.android.medianode.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tkpphr.android.medianode.core.MediaNode;
import com.tkpphr.android.medianode.util.MediaNodeSelector;
import com.tkpphr.android.medianode.view.customview.ChildMediaNodeInfoView;
import com.tkpphr.android.medianode.view.customview.OnMediaNodeSelectedListener;

public class ChildSelectorNodeListAdapter extends BaseAdapter{
    private MediaNode<?> parentNode;
    private MediaNodeSelector mediaNodeSelector;
    private OnMediaNodeSelectedListener onMediaNodeSelectedListener;

    public ChildSelectorNodeListAdapter() {

    }

    public MediaNodeSelector getMediaNodeSelector() {
        return mediaNodeSelector;
    }

    public int indexOf(MediaNode<?> mediaNode){
        if(parentNode==null){
            return -1;
        }else {
            return parentNode.getChildren().indexOf(mediaNode);
        }
    }

    public void reset(MediaNodeSelector mediaNodeSelector,MediaNode<?> parentNode){
        this.parentNode=parentNode;
        this.mediaNodeSelector = mediaNodeSelector;
        notifyDataSetChanged();
    }

    public void setOnMediaNodeSelectedListener(OnMediaNodeSelectedListener onMediaNodeSelectedListener) {
        this.onMediaNodeSelectedListener = onMediaNodeSelectedListener;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(mediaNodeSelector==null || parentNode==null){
            return 0;
        }else {
            return parentNode.getChildCount();
        }
    }

    @Override
    public MediaNode<?> getItem(int i) {
        return parentNode.getChild(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if(convertView==null){
            convertView=new ChildMediaNodeInfoView(container.getContext(),null);
        }
        ((ChildMediaNodeInfoView)convertView).setMediaNode(mediaNodeSelector,getItem(position));
        ((ChildMediaNodeInfoView)convertView).setOnMediaNodeSelectedListener(onMediaNodeSelectedListener);
        return convertView;
    }
}

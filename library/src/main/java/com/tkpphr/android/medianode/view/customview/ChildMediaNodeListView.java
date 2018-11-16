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
package com.tkpphr.android.medianode.view.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ListView;

import com.tkpphr.android.medianode.core.MediaNode;
import com.tkpphr.android.medianode.util.MediaNodeSelector;
import com.tkpphr.android.medianode.view.adapter.ChildSelectorNodeListAdapter;

public class ChildMediaNodeListView extends ListView{
    private ChildSelectorNodeListAdapter adapter;

    public ChildMediaNodeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        adapter = new ChildSelectorNodeListAdapter();
        setAdapter(adapter);
    }

    public void refresh(){
        adapter.notifyDataSetChanged();
    }

    public void scrollTo(final MediaNode<?> mediaNode){
        post(new Runnable() {
            @Override
            public void run() {
                int index=adapter.indexOf(mediaNode);
                if(index>=0){
                    setSelection(index);
                }
            }
        });
    }

    public void setMediaNode(@NonNull MediaNodeSelector mediaNodeSelector, @NonNull MediaNode<?> parentMediaNode){
        adapter.reset(mediaNodeSelector,parentMediaNode);
    }

    public void setOnMediaNodeSelectedListener(OnMediaNodeSelectedListener onMediaNodeSelectedListener){
        adapter.setOnMediaNodeSelectedListener(onMediaNodeSelectedListener);
    }


}

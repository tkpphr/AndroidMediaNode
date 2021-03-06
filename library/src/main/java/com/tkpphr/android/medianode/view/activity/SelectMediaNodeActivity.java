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
package com.tkpphr.android.medianode.view.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tkpphr.android.medianode.R;
import com.tkpphr.android.medianode.core.MediaNode;
import com.tkpphr.android.medianode.core.MediaNodeFilter;
import com.tkpphr.android.medianode.util.MediaNodeSelector;
import com.tkpphr.android.medianode.util.SelectableMediaNodeImpl;
import com.tkpphr.android.medianode.view.customview.MediaNodeExplorerView;
import com.tkpphr.android.medianode.view.customview.MediaNodePickerView;
import com.tkpphr.android.medianode.view.customview.OnMediaNodeSelectedListener;
import com.tkpphr.android.medianode.view.dialog.ConfirmMediaNodeSelectDialog;

public class SelectMediaNodeActivity extends AppCompatActivity implements MediaNodePickerView.OnMediaNodePickedListener {
    private Toolbar toolbar;
    private MediaNodePickerView mediaNodePickerView;

    public static Intent createIntent(Context context, String title, MediaNode<?> startNode, MediaNodeFilter<MediaNode<?>> filter){
        Intent intent=new Intent(context,SelectMediaNodeActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("node", SelectableMediaNodeImpl.createSelectorNode(startNode.getRoot(),filter));
        intent.putExtra("start_path",startNode.getFullPath());
        return intent;
    }

    public static Intent createIntent(Context context, String title, MediaNode<?> startNode){
        return createIntent(context, title, startNode, new MediaNodeFilter<MediaNode<?>>(){
            @Override
            public boolean apply(MediaNode<?> mediaNode) {
                return true;
            }
        });
    }

    public static MediaNode<?> getSelectedMediaNode(Intent resultData){
        return (MediaNode<?>)resultData.getSerializableExtra("selected_media_node");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mdnd_activity_select_media_node);
        toolbar=findViewById(R.id.mdnd_toolbar);
        if(getActionBar()==null && getSupportActionBar()==null) {
            setSupportActionBar(toolbar);
        }else {
            toolbar.setVisibility(View.GONE);
        }
        setTitle(getIntent().getStringExtra("title"));
        mediaNodePickerView=findViewById(R.id.mdnd_media_node_picker_view);
        mediaNodePickerView.setOnMediaNodePickedListener(this);
        if(savedInstanceState==null) {
            SelectableMediaNodeImpl rootNode = (SelectableMediaNodeImpl) getIntent().getSerializableExtra("node");
            String startPath=getIntent().getStringExtra("start_path");
            SelectableMediaNodeImpl startNode=rootNode.find(startPath);
            MediaNodeSelector mediaNodeSelector=new MediaNodeSelector(startNode);
            mediaNodePickerView.initialize(mediaNodeSelector);
        }
        setResult(RESULT_CANCELED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mdnd_menu_select_node,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.mdnd_menu_cancel){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMediaNodePicked(MediaNode<?> mediaNode) {
        Intent intent = new Intent();
        intent.putExtra("selected_media_node",((SelectableMediaNodeImpl)mediaNode).getMediaNode());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}

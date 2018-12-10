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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tkpphr.android.medianode.R;
import com.tkpphr.android.medianode.core.MediaNode;
import com.tkpphr.android.medianode.util.MediaNodeSelector;

import java.util.ArrayList;
import java.util.List;


public class MediaNodeExplorerView extends LinearLayout{
    private MediaNodePathBar mediaNodePathBar;
    private Button backButton;
    private ImageView searchButton;
    private SearchView searchView;
    private MediaNodeSelectorView mediaNodeSelectorView;
    private FoundMediaNodeListView foundMediaNodeListView;
    private MediaNodeSelector mediaNodeSelector;
    private boolean isSearchEnabled;

    public MediaNodeExplorerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.mdnd_view_media_node_explorer,this,true);
        if(isInEditMode()){
            return;
        }
        isSearchEnabled=false;
        mediaNodePathBar=findViewById(R.id.mdnd_media_node_path_bar);
        mediaNodePathBar.setOnClickPathListener(new MediaNodePathBar.OnClickPathListener() {
            @Override
            public void onClickPath(int depth) {
                if(mediaNodeSelector!=null){
                    mediaNodeSelector.moveToAncestorPath(depth);
                }
            }
        });
        mediaNodeSelectorView=findViewById(R.id.mdnd_media_node_selector_view);
        foundMediaNodeListView=findViewById(R.id.mdnd_found_media_node_list_view);
        foundMediaNodeListView.setOnJumpMediaNodeListener(new OnJumpMediaNodeListener() {
            @Override
            public void OnJumpMediaNode(MediaNode<?> mediaNode) {
                setSearchEnabled(false);
            }
        });
        searchView=findViewById(R.id.mdnd_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(mediaNodeSelector!=null) {
                    foundMediaNodeListView.reset(mediaNodeSelector,newText);
                }
                return false;
            }
        });
        backButton=findViewById(R.id.mdnd_back_button);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        searchButton=findViewById(R.id.mdnd_search_button);
        searchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setSearchEnabled(true);
            }
        });
        Drawable searchIcon=((ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_button)).getDrawable();
        searchButton.setImageDrawable(searchIcon);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle outState=new Bundle();
        outState.putParcelable("super_state",super.onSaveInstanceState());
        outState.putString("search_string",searchView.getQuery().toString());
        outState.putSerializable("selected_nodes",new ArrayList<>(mediaNodeSelector.getSelectedNodes()));
        outState.putSerializable("current_node",mediaNodeSelector.getCurrentNode());
        outState.putBoolean("search_enabled",isSearchEnabled);
        return outState;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Bundle savedState=(Bundle)state;
            state=savedState.getParcelable("super_state");
            List<MediaNode<?>> selectedNodes=(List<MediaNode<?>>) savedState.getSerializable("selected_nodes");
            setMediaNodeSelector(new MediaNodeSelector(selectedNodes));
            searchView.setQuery(savedState.getString("search_string"),false);
            setSearchEnabled(savedState.getBoolean("search_enabled"));
        }
        super.onRestoreInstanceState(state);
    }

    public void refresh(){
        if(mediaNodeSelector==null){
            return;
        }
        backButton.setEnabled(isSearchEnabled || mediaNodeSelector.getCount() > 1);
        mediaNodePathBar.setNodePath(mediaNodeSelector.getCurrentNode().getFullPath());
        mediaNodeSelectorView.refresh();
        foundMediaNodeListView.refresh();
    }

    public boolean back(){
        if(isSearchEnabled){
            setSearchEnabled(false);
            return true;
        }else {
            boolean isBacked=mediaNodeSelector.back();
            backButton.setEnabled(mediaNodeSelector.getCount()>1);
            return isBacked;
        }

    }

    public MediaNode<?> getCurrentNode(){
        return mediaNodeSelector.getCurrentNode();
    }

    public void moveTo(MediaNode<?> mediaNode){
        isSearchEnabled=false;
        if(mediaNodeSelector.getCurrentNode()==mediaNode) {
            refresh();
        }else {
            mediaNodeSelector.pushNode(mediaNode);
        }
    }

    public void moveTo(MediaNode<?> parentMediaNode,MediaNode<?> childMediaNode){
        moveTo(parentMediaNode);
        mediaNodeSelectorView.scrollToChild(childMediaNode);
    }

    public MediaNodeSelector getMediaNodeSelector() {
        return mediaNodeSelector;
    }

    public void setMediaNodeSelector(final MediaNodeSelector mediaNodeSelector){
        this.mediaNodeSelector=mediaNodeSelector;
        mediaNodePathBar.setNodePath(mediaNodeSelector.getCurrentNode().getFullPath());
        this.mediaNodeSelector.addCallback(new MediaNodeSelector.Callback() {
            @Override
            public void onCurrentNodeChanged(MediaNode<?> currentNode) {
                mediaNodePathBar.setNodePath(currentNode.getFullPath());
                mediaNodeSelectorView.setMediaNode(mediaNodeSelector,currentNode);
                setSearchEnabled(false);
            }
        });
        mediaNodeSelectorView.setMediaNode(mediaNodeSelector,mediaNodeSelector.getCurrentNode());
        backButton.setEnabled(false);
    }

    public void setOnMediaNodeSelectedListener(OnMediaNodeSelectedListener onMediaNodeSelectedListener){
        mediaNodeSelectorView.setOnMediaNodeSelectedListener(onMediaNodeSelectedListener);
        foundMediaNodeListView.setOnMediaNodeSelectedListener(onMediaNodeSelectedListener);
    }

    public void setSearchEnabled(boolean isSearchEnabled){
        this.isSearchEnabled=isSearchEnabled;
        if(isSearchEnabled){
            backButton.setEnabled(true);
            searchButton.setVisibility(INVISIBLE);
            mediaNodeSelectorView.setVisibility(INVISIBLE);
            mediaNodePathBar.setVisibility(INVISIBLE);
            searchView.setVisibility(VISIBLE);
            foundMediaNodeListView.setVisibility(VISIBLE);
            foundMediaNodeListView.refresh();
        }else {
            backButton.setEnabled(mediaNodeSelector.getCount()>1);
            searchButton.setVisibility(VISIBLE);
            mediaNodeSelectorView.setVisibility(VISIBLE);
            mediaNodePathBar.setVisibility(VISIBLE);
            searchView.setVisibility(INVISIBLE);
            foundMediaNodeListView.setVisibility(INVISIBLE);
            mediaNodeSelectorView.refresh();
            if(searchView.isFocused()){
                InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(inputMethodManager!=null){
                    inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(),0);
                }
            }
        }

    }

}

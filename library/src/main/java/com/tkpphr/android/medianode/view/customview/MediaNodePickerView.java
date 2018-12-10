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
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tkpphr.android.medianode.R;
import com.tkpphr.android.medianode.core.MediaNode;
import com.tkpphr.android.medianode.util.MediaNodeSelector;

public class MediaNodePickerView extends FrameLayout {
	private MediaNodeExplorerView mediaNodeExplorerView;
	private SelectedMediaNodeView selectedMediaNodeView;
	private ConstraintLayout selectedMediaNodeContainer;
	private Button selectButton;
	private Button cancelSelectButton;
	private boolean isMediaNodeSelected;
	private OnMediaNodePickedListener onMediaNodePickedListener;

	public MediaNodePickerView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.mdnd_view_media_node_picker,this,true);
		if(isInEditMode()){
			return;
		}
		mediaNodeExplorerView=findViewById(R.id.mdnd_media_node_explorer_view);
		mediaNodeExplorerView.setOnMediaNodeSelectedListener(new OnMediaNodeSelectedListener() {
			@Override
			public void onMediaNodeSelected(MediaNode<?> mediaNode) {
				selectedMediaNodeView.setMediaNode(mediaNode);
				setMediaNodeSelected(true);
			}
		});
		selectedMediaNodeView=findViewById(R.id.mdnd_selected_media_node_view);
		selectedMediaNodeContainer=findViewById(R.id.mdnd_selected_media_node_container);
		selectButton=findViewById(R.id.mdnd_select_button);
		selectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(onMediaNodePickedListener!=null && selectedMediaNodeView.getMediaNode()!=null){
					onMediaNodePickedListener.onMediaNodePicked(selectedMediaNodeView.getMediaNode());
				}
			}
		});
		cancelSelectButton =findViewById(R.id.mdnd_cancel_select_button);
		cancelSelectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setMediaNodeSelected(false);
			}
		});
	}

	@Nullable
	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle outState=new Bundle();
		outState.putParcelable("super_state",super.onSaveInstanceState());
		outState.putBoolean("is_media_node_selected", isMediaNodeSelected);
		return outState;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		if(state instanceof Bundle){
			Bundle savedState=(Bundle)state;
			state=savedState.getParcelable("super_state");
			setMediaNodeSelected(savedState.getBoolean("is_media_node_selected"));
		}
		super.onRestoreInstanceState(state);
	}

	public void initialize(MediaNodeSelector mediaNodeSelector){
		mediaNodeExplorerView.setMediaNodeSelector(mediaNodeSelector);
		setMediaNodeSelected(false);
	}

	public boolean back(){
		if(isMediaNodeSelected){
			setMediaNodeSelected(false);
			return true;
		}else {
			return mediaNodeExplorerView.back();
		}
	}

	public void setOnMediaNodePickedListener(OnMediaNodePickedListener onMediaNodePickedListener) {
		this.onMediaNodePickedListener = onMediaNodePickedListener;
	}

	private void setMediaNodeSelected(boolean isSelected){
		this.isMediaNodeSelected =isSelected;
		if(this.isMediaNodeSelected){
			mediaNodeExplorerView.setVisibility(INVISIBLE);
			selectedMediaNodeContainer.setVisibility(VISIBLE);
		}else {
			mediaNodeExplorerView.setVisibility(VISIBLE);
			selectedMediaNodeContainer.setVisibility(INVISIBLE);
		}
	}

	public interface OnMediaNodePickedListener{
		void onMediaNodePicked(MediaNode<?> mediaNode);
	}

}

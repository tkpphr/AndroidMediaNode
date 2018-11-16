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
import android.graphics.PorterDuff;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpphr.android.medianode.R;
import com.tkpphr.android.medianode.core.MediaNode;
import com.tkpphr.android.medianode.core.MediaNodeSound;

public class SelectedMediaNodeView extends LinearLayout{
	private TextView nodeName;
	private TextView nodeInfo;
	private ImageView nodeImage;
	private Button playButton;
	private Button stopButton;
	private MediaNodeSound sound;

	public SelectedMediaNodeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.mdnd_view_selected_media_node,this,true);
		if(isInEditMode()){
			return;
		}
		nodeName=findViewById(R.id.mdnd_node_name);
		nodeInfo=findViewById(R.id.mdnd_node_info);
		nodeImage=findViewById(R.id.mdnd_node_image);
		playButton=findViewById(R.id.mdnd_play_button);
		playButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				sound.stop();
				sound.play();
			}
		});
		stopButton=findViewById(R.id.mdnd_stop_button);
		stopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				sound.stop();
			}
		});
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		nodeImage.setImageBitmap(null);
		releaseSound();
	}

	public void setMediaNode(MediaNode<?> mediaNode){
		nodeName.setText(mediaNode.getNodeName());
		nodeInfo.setText(mediaNode.getNodeInfo(getContext()));
		nodeImage.setImageBitmap(null);
		releaseSound();
		nodeImage.setImageBitmap(mediaNode.getImage());
		sound = mediaNode.getSound();
		playButton.setEnabled(sound!=null);
		stopButton.setEnabled(playButton.isEnabled());

	}

	public void stopSound(){
		if(sound!=null){
			sound.stop();
		}
	}

	private void releaseSound(){
		if(sound!=null){
			sound.stop();
			sound.release();
			sound=null;
		}
	}
}

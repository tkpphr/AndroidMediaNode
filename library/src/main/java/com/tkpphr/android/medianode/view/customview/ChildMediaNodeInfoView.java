package com.tkpphr.android.medianode.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpphr.android.medianode.R;
import com.tkpphr.android.medianode.core.MediaNode;
import com.tkpphr.android.medianode.util.MediaNodeSelector;
import com.tkpphr.android.medianode.util.SelectableMediaNode;

public class ChildMediaNodeInfoView extends LinearLayout{
	private TextView nodeName;
	private TextView nodeInfo;
	private Button shiftButton;
	private MediaNode<?> mediaNode;
	private MediaNodeSelector mediaNodeSelector;
	private OnMediaNodeSelectedListener onMediaNodeSelectedListener;

	public ChildMediaNodeInfoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.mdnd_media_node_info,this,true);
		if(isInEditMode()){
			return;
		}
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(onMediaNodeSelectedListener !=null){
					onMediaNodeSelectedListener.onMediaNodeSelected(mediaNode);
				}
			}
		});
		nodeName=findViewById(R.id.mdnd_node_name);
		nodeInfo=findViewById(R.id.mdnd_node_info);
		shiftButton=findViewById(R.id.mdnd_shift_button);
		shiftButton.setText("↓");
		shiftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(mediaNodeSelector!=null){
					mediaNodeSelector.shiftNodeDown(mediaNode);
				}
			}
		});
	}

	public void refresh(){
		if(mediaNodeSelector==null || mediaNode==null) {
			return;
		}
		if(mediaNode instanceof SelectableMediaNode) {
			boolean selectable=((SelectableMediaNode<?>)mediaNode).isSelectable();
			setEnabled(selectable);
			nodeName.setAlpha(selectable ?  1.0f : 0.5f);
			nodeInfo.setAlpha(nodeName.getAlpha());
		}else {
			setEnabled(true);
		}
		shiftButton.setVisibility((mediaNode.getChildCount() > 0 ? VISIBLE : INVISIBLE));
		nodeName.setText(mediaNode.getNodeName());
		nodeInfo.setText(mediaNode.getNodeInfo(getContext()));
	}

	public void setMediaNode(MediaNodeSelector mediaNodeSelector, MediaNode<?> mediaNode){
		this.mediaNodeSelector=mediaNodeSelector;
		if(this.mediaNode==mediaNode){
			refresh();
		}else {
			this.mediaNode=mediaNode;
			refresh();
		}
	}

	public void setOnMediaNodeSelectedListener(OnMediaNodeSelectedListener onMediaNodeSelectedListener) {
		this.onMediaNodeSelectedListener = onMediaNodeSelectedListener;
	}
}

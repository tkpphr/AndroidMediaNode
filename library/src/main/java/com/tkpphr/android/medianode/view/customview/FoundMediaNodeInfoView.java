package com.tkpphr.android.medianode.view.customview;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
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

public class FoundMediaNodeInfoView extends LinearLayout{
	private TextView nodeName;
	private TextView nodeInfo;
	private Button shiftButton;
	private MediaNode<?> mediaNode;
	private MediaNodeSelector mediaNodeSelector;
	private String searchString;
	private OnMediaNodeSelectedListener onMediaNodeSelectedListener;
	private OnJumpMediaNodeListener onJumpMediaNodeListener;

	public FoundMediaNodeInfoView(Context context, AttributeSet attrs) {
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
		shiftButton.setText("→");
		shiftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(mediaNodeSelector==null){
					return;
				}
				mediaNodeSelector.pushNode(mediaNode);
				if(onJumpMediaNodeListener !=null){
					onJumpMediaNodeListener.OnJumpMediaNode(mediaNode);
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
			setAlpha(selectable ?  1.0f : 0.5f);
		}else {
			setEnabled(true);
		}
		nodeName.setText(getBackgroundSpanNodeName());
		nodeInfo.setText(mediaNode.getNodeInfo(getContext()));
	}

	public void setMediaNode(MediaNodeSelector mediaNodeSelector, MediaNode<?> mediaNode,String searchString){
		this.mediaNodeSelector=mediaNodeSelector;
		this.searchString=searchString;
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

	public void setOnJumpMediaNodeListener(OnJumpMediaNodeListener onJumpMediaNodeListener) {
		this.onJumpMediaNodeListener = onJumpMediaNodeListener;
	}

	private SpannableStringBuilder getBackgroundSpanNodeName(){
		SpannableStringBuilder srcBuilder=new SpannableStringBuilder();
		srcBuilder.append(mediaNode.getNodeName());
		if(TextUtils.isEmpty(searchString)){
			return srcBuilder;
		}
		String srcLowerCase=mediaNode.getNodeName().toLowerCase();
		String searchStringLowerCase=searchString.toLowerCase();
		int index = srcLowerCase.indexOf(searchStringLowerCase);
		while (index >= 0) {
			srcBuilder.setSpan(new BackgroundColorSpan(Color.rgb(255,140,0)), index, index + searchStringLowerCase.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			index = srcLowerCase.indexOf(searchStringLowerCase, index + searchString.length());
		}
		return srcBuilder;
	}


}

package com.tkpphr.android.medianode.view.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tkpphr.android.medianode.R;
import com.tkpphr.android.medianode.core.MediaNode;
import com.tkpphr.android.medianode.util.MediaNodeSelector;

public class MediaNodeSelectorView extends LinearLayout{
	private ParentMediaNodeInfoView parentMediaNodeInfoView;
	private ChildMediaNodeListView childMediaNodeListView;

	public MediaNodeSelectorView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.mdnd_view_media_node_selector,this,true);;
		if(isInEditMode()){
			return;
		}
		parentMediaNodeInfoView=findViewById(R.id.mdnd_parent_info_view);
		childMediaNodeListView=findViewById(R.id.mdnd_child_list_view);
	}

	public void refresh(){
		parentMediaNodeInfoView.refresh();
		childMediaNodeListView.refresh();
	}

	public void setMediaNode(MediaNodeSelector mediaNodeSelector, MediaNode<?> mediaNode){
		parentMediaNodeInfoView.setMediaNode(mediaNodeSelector,mediaNode);
		childMediaNodeListView.setMediaNode(mediaNodeSelector,mediaNode);
	}

	public void scrollToChild(MediaNode<?> childMediaNode){
		childMediaNodeListView.scrollTo(childMediaNode);
	}

	public void setOnMediaNodeSelectedListener(OnMediaNodeSelectedListener onMediaNodeSelectedListener){
		parentMediaNodeInfoView.setOnMediaNodeSelectedListener(onMediaNodeSelectedListener);
		childMediaNodeListView.setOnMediaNodeSelectedListener(onMediaNodeSelectedListener);
	}

}

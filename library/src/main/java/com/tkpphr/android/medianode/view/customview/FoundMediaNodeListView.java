package com.tkpphr.android.medianode.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.tkpphr.android.medianode.util.MediaNodeSelector;
import com.tkpphr.android.medianode.view.adapter.FoundMediaNodeListAdapter;

public class FoundMediaNodeListView extends ListView{
	private FoundMediaNodeListAdapter adapter;

	public FoundMediaNodeListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		adapter=new FoundMediaNodeListAdapter(getContext());
		setAdapter(adapter);
	}

	public void refresh(){
		adapter.refresh();
	}

	public void reset(MediaNodeSelector mediaNodeSelector,String searchString){
		adapter.reset(mediaNodeSelector,searchString);
	}

	public void setOnMediaNodeSelectedListener(OnMediaNodeSelectedListener onMediaNodeSelectedListener){
		adapter.setOnMediaNodeSelectedListener(onMediaNodeSelectedListener);
	}

	public void setOnJumpMediaNodeListener(OnJumpMediaNodeListener onMediaNodeJumpedListener) {
		adapter.setOnJumpMediaNodeListener(onMediaNodeJumpedListener);
	}
}

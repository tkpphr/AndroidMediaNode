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
package com.tkpphr.android.medianode.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.KeyEvent;
import android.view.View;

import com.tkpphr.android.medianode.R;
import com.tkpphr.android.medianode.core.MediaNode;
import com.tkpphr.android.medianode.core.MediaNodeFilter;
import com.tkpphr.android.medianode.util.MediaNodeSelector;
import com.tkpphr.android.medianode.util.SelectableMediaNodeImpl;
import com.tkpphr.android.medianode.view.customview.MediaNodeExplorerView;
import com.tkpphr.android.medianode.view.customview.OnMediaNodeSelectedListener;

public class SelectMediaNodeDialog extends AppCompatDialogFragment implements ConfirmMediaNodeSelectDialog.OnConfirmedListener{
	private OnMediaNodeDialogListener onMediaNodeSelectedListener;

	public static SelectMediaNodeDialog newInstance(String title, MediaNode<?> startNode,MediaNodeFilter<MediaNode<?>> filter) {
		Bundle args = new Bundle();
		args.putString("title",title);
		args.putString("start_path",startNode.getFullPath());
		args.putSerializable("node", SelectableMediaNodeImpl.createSelectorNode(startNode.getRoot(),filter));
		SelectMediaNodeDialog fragment = new SelectMediaNodeDialog();
		fragment.setArguments(args);
		return fragment;
	}

	public static SelectMediaNodeDialog newInstance(String title, MediaNode<?> startNode){
		return newInstance(title, startNode, new MediaNodeFilter<MediaNode<?>>() {
			@Override
			public boolean apply(MediaNode<?> mediaNode) {
				return true;
			}
		});
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if(context instanceof OnMediaNodeDialogListener){
			onMediaNodeSelectedListener =(OnMediaNodeDialogListener) context;
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
			if (activity instanceof OnMediaNodeDialogListener) {
				onMediaNodeSelectedListener =(OnMediaNodeDialogListener) activity;
			}
		}
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		if(getTargetFragment() instanceof OnMediaNodeDialogListener){
			onMediaNodeSelectedListener =(OnMediaNodeDialogListener)getTargetFragment();
		}else if(getParentFragment() instanceof OnMediaNodeDialogListener){
			onMediaNodeSelectedListener =(OnMediaNodeDialogListener)getParentFragment();
		}
		AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
		View view= View.inflate(getContext(),R.layout.mdnd_dialog_select_media_node,null);
		final MediaNodeExplorerView mediaNodeExplorerView=view.findViewById(R.id.mdnd_media_node_explorer_view);
		mediaNodeExplorerView.setFocusable(true);
		mediaNodeExplorerView.setFocusableInTouchMode(true);
		mediaNodeExplorerView.requestFocus();
		mediaNodeExplorerView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View view, int i, KeyEvent keyEvent) {
				if(keyEvent.getKeyCode()==KeyEvent.KEYCODE_BACK && keyEvent.getAction()==KeyEvent.ACTION_DOWN) {
					return mediaNodeExplorerView.back();
				}
				return false;
			}
		});
		mediaNodeExplorerView.setOnMediaNodeSelectedListener(new OnMediaNodeSelectedListener() {
			@Override
			public void onMediaNodeSelected(MediaNode<?> mediaNode) {
				ConfirmMediaNodeSelectDialog.newInstance(mediaNode).show(getChildFragmentManager(),null);
			}
		});
		if(savedInstanceState==null){
			Bundle args=getArguments();
			SelectableMediaNodeImpl rootNode = (SelectableMediaNodeImpl) args.getSerializable("node");
			String startPath=args.getString("start_path");
			SelectableMediaNodeImpl startNode=rootNode.find(startPath);
			MediaNodeSelector mediaNodeSelector=new MediaNodeSelector(startNode);
			mediaNodeExplorerView.setMediaNodeSelector(mediaNodeSelector);
		}
		return dialog.setTitle(getArguments().getString("title"))
				.setView(view)
				.create();
	}

	@Override
	public void onConfirmed(MediaNode<?> mediaNode) {
		if(onMediaNodeSelectedListener !=null){
			onMediaNodeSelectedListener.onMediaNodeSelected(mediaNode);
		}
		onDismiss(getDialog());
	}

	public interface OnMediaNodeDialogListener {
		void onMediaNodeSelected(MediaNode<?> mediaNode);
	}
}

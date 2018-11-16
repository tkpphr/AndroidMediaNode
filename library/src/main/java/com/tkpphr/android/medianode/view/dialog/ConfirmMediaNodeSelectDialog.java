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
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tkpphr.android.medianode.R;
import com.tkpphr.android.medianode.core.MediaNode;
import com.tkpphr.android.medianode.view.customview.SelectedMediaNodeView;

public class ConfirmMediaNodeSelectDialog extends AppCompatDialogFragment{
    private MediaNode<?> mediaNode;
    private SelectedMediaNodeView selectedMediaNodeView;
    private OnConfirmedListener onConfirmAcceptedListener;

    public static ConfirmMediaNodeSelectDialog newInstance(MediaNode<?> mediaNode){
        ConfirmMediaNodeSelectDialog dialog=new ConfirmMediaNodeSelectDialog();
        Bundle args=new Bundle();
        args.putSerializable("node",mediaNode);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnConfirmedListener){
            onConfirmAcceptedListener =(OnConfirmedListener)context;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (activity instanceof OnConfirmedListener) {
                onConfirmAcceptedListener = (OnConfirmedListener) activity;
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(getTargetFragment() instanceof OnConfirmedListener){
            onConfirmAcceptedListener =(OnConfirmedListener)getTargetFragment();
        }else if(getParentFragment() instanceof OnConfirmedListener){
            onConfirmAcceptedListener=(OnConfirmedListener)getParentFragment();
        }
        AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
        mediaNode=(MediaNode<?>) getArguments().getSerializable("node");
        View view=View.inflate(getContext(),R.layout.mdnd_dialog_confirm_media_node_select,null);
        selectedMediaNodeView=(SelectedMediaNodeView)view;
        selectedMediaNodeView.setMediaNode(mediaNode);
        return dialog.setView(view)
                .setPositiveButton(R.string.mdnd_select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(onConfirmAcceptedListener !=null){
                            onConfirmAcceptedListener.onConfirmed(mediaNode);
                        }
                    }
                })
                .setNegativeButton(R.string.mdnd_cancel,null)
                .create();
    }

    @Override
    public void onPause() {
        super.onPause();
        selectedMediaNodeView.stopSound();
    }

    public interface OnConfirmedListener {
        void onConfirmed(MediaNode<?> mediaNode);
    }


}

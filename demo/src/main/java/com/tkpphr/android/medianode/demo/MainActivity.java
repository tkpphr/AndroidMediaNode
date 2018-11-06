package com.tkpphr.android.medianode.demo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.media.AudioTrack;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;


import com.tkpphr.android.medianode.core.MediaNode;
import com.tkpphr.android.medianode.core.MediaNodeSound;

import com.tkpphr.android.medianode.demo.databinding.ActivityMainBinding;
import com.tkpphr.android.medianode.view.activity.SelectMediaNodeActivity;
import com.tkpphr.android.medianode.view.dialog.SelectMediaNodeDialog;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements SelectMediaNodeDialog.OnMediaNodeDialogListener {
	private ActivityMainBinding binding;
	private SampleMediaNode root;
	private SampleMediaNode selectedNode;
	private MediaNodeSound nodeSound;
	private static final int REQUEST_CODE_SELECTED_NODE=0;
	private static final int REQUEST_CODE_FILE_IMAGE=1;
	private static final int REQUEST_CODE_FILE_SOUND=2;
	private static final int REQUEST_CODE_PERMISSION=3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		copyAssets();
		root=createRoot();
		selectedNode=root;
		binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
		binding.buttonSelectNode.setOnClickListener(v->
				startActivityForResult(SelectMediaNodeActivity.createIntent(this, "Select Node",selectedNode), REQUEST_CODE_SELECTED_NODE));
		binding.buttonSelectFromDialog.setOnClickListener(view ->{
				SampleMediaNode startNode=selectedNode.getChildCount() > 0 ? selectedNode : selectedNode.getParent();
				SelectMediaNodeDialog.newInstance("Select Node",startNode,mediaNode -> !TextUtils.equals(mediaNode.getFullPath(),selectedNode.getFullPath()))
				.show(getSupportFragmentManager(),null);
		});
		binding.buttonSelectImage.setOnClickListener(v -> {
			Intent intent;
			intent=new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent,REQUEST_CODE_FILE_IMAGE);
		});
		binding.buttonSelectSound.setOnClickListener(v->{
			Intent intent;
			intent=new Intent();
			intent.setType("audio/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent,REQUEST_CODE_FILE_SOUND);
		});
		binding.buttonPlaySound.setOnClickListener(v -> {
			nodeSound.play();
			binding.buttonPlaySound.setEnabled(false);
			binding.buttonStopSound.setEnabled(true);
		});
		binding.buttonStopSound.setOnClickListener(v -> {
			nodeSound.stop();
			binding.buttonPlaySound.setEnabled(true);
			binding.buttonStopSound.setEnabled(false);
		});
		reset();
		ActivityCompat.requestPermissions(this,new String[]{"android.permission.READ_EXTERNAL_STORAGE"},REQUEST_CODE_PERMISSION);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(nodeSound!=null){
			binding.buttonPlaySound.setEnabled(true);
			binding.buttonStopSound.setEnabled(false);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(nodeSound!=null){
			nodeSound.stop();
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(nodeSound!=null){
			nodeSound.release();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_CANCELED){
			return;
		}
		switch (requestCode){
			case REQUEST_CODE_SELECTED_NODE:
				selectedNode=root.find(SelectMediaNodeActivity.getSelectedMediaNode(data).getFullPath());
				break;
			case REQUEST_CODE_FILE_IMAGE:
				selectedNode.setImagePath(UriResolver.getFilePath(getApplicationContext(),data.getData()));
				break;
			case REQUEST_CODE_FILE_SOUND:
				selectedNode.setSoundPath(UriResolver.getFilePath(getApplicationContext(),data.getData()));
				break;
		}
		reset();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode==REQUEST_CODE_PERMISSION){
			for(String permission : permissions) {
				if(ActivityCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
					finish();
					break;
				}
			}
		}
	}

	@Override
	public void onMediaNodeSelected(MediaNode<?> mediaNode) {
		selectedNode=root.find(mediaNode.getFullPath());
		reset();
	}

	private void copyAssets(){
		FileOutputStream fileOutputStream=null;
		InputStream inputStream=null;
		try {
			String[] files=getAssets().list("");
			if(getFilesDir().list().length==0) {
				for (String file : files) {
					if (!(file.contains(".wav") || file.contains("png"))) {
						continue;
					}
					fileOutputStream = openFileOutput(file, MODE_PRIVATE);
					inputStream = getAssets().open(file);
					byte[] data = new byte[inputStream.available()];
					inputStream.read(data, 0, data.length);
					fileOutputStream.write(data, 0, data.length);
					fileOutputStream.close();
					inputStream.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(fileOutputStream!=null){
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private SampleMediaNode createRoot(){
		String filesDirPath=getFilesDir().getAbsolutePath();
		SampleMediaNode root=new SampleMediaNode(null,"root",filesDirPath+"/parent_root.png",filesDirPath+"/sound_1.wav");
		root.addChild(new SampleMediaNode(root,root.getFullPath()+"\\child1",filesDirPath+"/child_1.png",filesDirPath+"/sound_2.wav"));
		root.addChild(new SampleMediaNode(root,root.getFullPath()+"\\child2",filesDirPath+"/child_2.png",filesDirPath+"/sound_1.wav"));
		root.addChild(new SampleMediaNode(root,root.getFullPath()+"\\child3",filesDirPath+"/child_3.png",filesDirPath+"/sound_3.wav"));
		root.addChild(new SampleMediaNode(root,root.getFullPath()+"\\child4",filesDirPath+"/child_4.png",filesDirPath+"/sound_1.wav"));
		SampleMediaNode child1=root.getChild(0);
		child1.addChild(new SampleMediaNode(child1,child1.getFullPath()+"\\grandchild1",filesDirPath+"/grand_child_1.png",filesDirPath+"/sound_1.wav"));
		child1.addChild(new SampleMediaNode(child1,child1.getFullPath()+"\\grandchild2",filesDirPath+"/grand_child_2.png",filesDirPath+"/sound_3.wav"));
		SampleMediaNode child2=root.getChild(1);
		child2.addChild(new SampleMediaNode(child2,child2.getFullPath()+"\\grandchild3",filesDirPath+"/grand_child_3.png",filesDirPath+"/sound_2.wav"));
		return root;
	}

	private void reset(){
		binding.textViewNodeName.setText(selectedNode.getNodeName());
		binding.imageViewNode.setImageBitmap(selectedNode.getImage());
		MediaNodeSound nodeSound=selectedNode.getSound();
		if(nodeSound instanceof MediaNodeSoundImpl){
			this.nodeSound=nodeSound;
			((MediaNodeSoundImpl)nodeSound).setOnCompletionListener(mediaPlayer->{
				binding.buttonPlaySound.setEnabled(true);
				binding.buttonStopSound.setEnabled(false);
			});
			binding.buttonPlaySound.setEnabled(true);
			binding.buttonStopSound.setEnabled(false);
		}else {
			binding.buttonPlaySound.setEnabled(false);
			binding.buttonStopSound.setEnabled(false);
		}
	}
}

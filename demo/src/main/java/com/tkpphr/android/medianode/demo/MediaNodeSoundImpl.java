package com.tkpphr.android.medianode.demo;

import android.media.MediaPlayer;


import com.tkpphr.android.medianode.core.MediaNodeSound;

import java.io.IOException;

public class MediaNodeSoundImpl implements MediaNodeSound{
	private MediaPlayer mediaPlayer;
	private String filePath;
	private MediaPlayer.OnCompletionListener onCompletionListener;

	public MediaNodeSoundImpl(String filePath){
		this.filePath =filePath;
	}


	@Override
	public void release(){
		if(mediaPlayer!=null){
			mediaPlayer.setOnCompletionListener(null);
			mediaPlayer.stop();
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer=null;
		}

	}

	@Override
	public void play(){
		if(mediaPlayer!=null){
			return;
		}
		mediaPlayer=new MediaPlayer();

		mediaPlayer.setOnCompletionListener(mediaPlayer->{
			release();
			if(onCompletionListener !=null){
					onCompletionListener.onCompletion(mediaPlayer);
			}
		});

		try {
			mediaPlayer.setDataSource(filePath);
			mediaPlayer.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mediaPlayer.start();
	}

	@Override
	public void stop() {
		release();
	}

	public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
		this.onCompletionListener = onCompletionListener;
	}
}

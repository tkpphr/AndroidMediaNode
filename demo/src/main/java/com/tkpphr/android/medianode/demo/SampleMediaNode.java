package com.tkpphr.android.medianode.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tkpphr.android.medianode.core.BaseMediaNode;
import com.tkpphr.android.medianode.core.MediaNodeSound;

import java.io.File;

public class SampleMediaNode extends BaseMediaNode<SampleMediaNode>{
    private String imagePath;
    private String soundPath;

    public SampleMediaNode(SampleMediaNode parent, String fullPath, String imagePath, String soundPath) {
        super(fullPath,parent);
        this.imagePath=imagePath;
        this.soundPath=soundPath;
    }

    @Override
    public Bitmap getImage() {
        if(new File(imagePath).exists()) {
            return BitmapFactory.decodeFile(imagePath);
        }else {
            return null;
        }
    }

    @Override
    public MediaNodeSound getSound() {
        if(new File(soundPath).exists()) {
            return new MediaNodeSoundImpl(soundPath);
        }else {
            return null;
        }
    }

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public String getSoundPath() {
        return soundPath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }


}

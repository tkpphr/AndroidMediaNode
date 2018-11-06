package com.tkpphr.android.medianode.view.customview;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.tkpphr.android.medianode.core.MediaNode;
import com.tkpphr.android.medianode.util.MediaNodeSelector;

public class MediaNodePathBar extends TabLayout{
    private String oldFullPath;
    private OnClickPathListener onClickPathListener;

    public MediaNodePathBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllTabs();
    }

    public void setNodePath(String fullPath){
        if(TextUtils.equals(this.oldFullPath,fullPath)){
            return;
        }
        this.oldFullPath = fullPath;
        removeAllTabs();
        removeOnTabSelectedListener(onTabSelectedListener);
        String[] pathNames=fullPath.split("\\\\");
        for(int i=0;i<pathNames.length;i++){
            if(TextUtils.isEmpty(pathNames[i])){
                continue;
            }
            Tab tab=newTab();
            if(i==pathNames.length-1){
                tab.setText(pathNames[i]);
            }else {
                tab.setText(String.format("%s >",pathNames[i]));
            }
            addTab(tab);
        }
        post(new Runnable() {
            @Override
            public void run() {
                Tab tab=getTabAt(getTabCount()-1);
                if(tab!=null && !tab.isSelected()){
                    tab.select();
                }
                addOnTabSelectedListener(onTabSelectedListener);
            }
        });
    }

    public void setOnClickPathListener(OnClickPathListener onClickPathListener) {
        this.onClickPathListener = onClickPathListener;
    }

    private final OnTabSelectedListener onTabSelectedListener=new OnTabSelectedListener() {
        @Override
        public void onTabSelected(Tab tab) {
            if(onClickPathListener!=null){
                onClickPathListener.onClickPath(tab.getPosition());
            }
        }

        @Override
        public void onTabUnselected(Tab tab) {

        }

        @Override
        public void onTabReselected(Tab tab) {

        }
    };

    public interface OnClickPathListener{
        void onClickPath(int depth);
    }
}

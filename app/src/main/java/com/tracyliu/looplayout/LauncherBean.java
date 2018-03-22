package com.tracyliu.looplayout;

/**
 * Created by tracyliu on 2017/6/3.
 */

public class LauncherBean {

    private int mAnim;
    private int mFronts;
    private int mImages;
    private String mIconName;

    public LauncherBean(int anim, int fronts, int images, String iconName) {
        mAnim = anim;
        mFronts = fronts;
        mImages = images;
        mIconName = iconName;
    }

    public int getAnim() {
        return mAnim;
    }

    public void setAnim(int anim) {
        mAnim = anim;
    }

    public int getFronts() {
        return mFronts;
    }

    public void setFronts(int fronts) {
        mFronts = fronts;
    }

    public int getImages() {
        return mImages;
    }

    public void setImages(int images) {
        mImages = images;
    }

    public String getIconName() {
        return mIconName;
    }

    public void setIconName(String iconName) {
        mIconName = iconName;
    }
}

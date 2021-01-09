package com.example.myapplication;

import android.content.res.AssetManager;

public class GLYiYiLib {
    static {
        System.loadLibrary("yiyi_lib");
    }
    public static native void init(int width,int height);
    public static native void step();
    public static native void nativeSetAssetManager(AssetManager am);
}

package com.example.myapplication;

import android.opengl.Matrix;

public class MatrixState {
    private static float[] mVMatrix = new float[16];//摄像机位置朝向
    private static float[] mProjMatrix = new float[16];//投影矩阵
    private static float[] mMVPMatrix ;//总变换矩阵
    private static float[] mCurMatrix;//当前变换矩阵
    static float[][] mstack = new float[10][16];
    static int stackTop = 1;
    public static void setCamera(float cx,float cy,float cz,float tx,float ty,float tz,float upx,float upy,float upz){
        Matrix.setLookAtM(mVMatrix,0, cx, cy,cz,tx,ty,tz,upx,upy,upz);
    }
    //正交投影
    public static void setProjectOrtho(float left,float right,float bottom,float up,float near,float far){
        Matrix.orthoM(mProjMatrix,0,left,right,bottom,up,near,far);
    }
    //透视投影
    public static void setProjectFrustum(float left,float right,float bottom,float up,float near,float far){
        Matrix.frustumM(mProjMatrix,0,left,right,bottom,up,near,far);
    }
    public static float[] getFinalMatrix(float[] spec){
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix,0,mVMatrix,0,spec,0);
        Matrix.multiplyMM(mMVPMatrix,0,mProjMatrix,0,mMVPMatrix,0);
        return mMVPMatrix;
    }

    public static float[] getFinalMatrix(){
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix,0,mVMatrix,0,mCurMatrix,0);
        Matrix.multiplyMM(mMVPMatrix,0,mProjMatrix,0,mMVPMatrix,0);
        return mMVPMatrix;
    }

    public static void setInitStack(){
        mCurMatrix = new float[16];
        Matrix.setRotateM(mCurMatrix,0,0,1,0,0);
    }

    public static void pushMatrix(){
        ++stackTop;
        for(int i = 0 ; i < 16;++i){
            mstack[stackTop][i] = mCurMatrix[i];
        }
    }
    public static void popMatrix(){
        for(int i = 0 ; i < 16;++i){
            mCurMatrix[i] = mstack[stackTop][i];
        }
        --stackTop;
    }
    //平移
    public static void translate(){

    }
}

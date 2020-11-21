package com.example.myapplication;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyTdView extends GLSurfaceView {
    SceneRederer mRenderer;
    final float ANGLE_SPAN = 0.375f;
    RotateThread rThread;
    public MyTdView(Context context){
        super(context);
        this.setEGLContextClientVersion(3);
        mRenderer = new SceneRederer();
        this.setRenderer(mRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
    private class SceneRederer implements GLSurfaceView.Renderer{
        Triangle triangle;
        public void onSurfaceCreated(GL10 gl, EGLConfig config){
            GLES30.glClearColor(0,0,0,1.0f);
            triangle = new Triangle(MyTdView.this);
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            rThread = new RotateThread();
            rThread.start();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height){
            GLES30.glViewport(0,0,width,height);
            float radio = (float)width / height;
            Matrix.frustumM(Triangle.mProMatrix,0,-radio,radio,-1,1,1,10);
            Matrix.setLookAtM(Triangle.mVMatrix,0,0,0,3,0,0,0,0,1,0);
        }

        public void onDrawFrame(GL10 gl){
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
            triangle.drawSelf();
        }
    }

    public class RotateThread extends Thread{
        public boolean flag = true;
        @Override public void run(){
            while (flag){
                mRenderer.triangle.addAngle(ANGLE_SPAN);
                try {
                    Thread.sleep(20);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}

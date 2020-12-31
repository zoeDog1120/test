package com.example.myapplication;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MySurfaceView extends GLSurfaceView {
    SceneRederer mRenderer;
    float mPreviousX ;
    float mPreviousY ;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    float[][] color=new float[][]{
            {1, 0, 0.1f},//红
            {0.98f, 0.49f, 0.04f},//橙
            {1f, 1f, 0.04f},//黄
            {0.67f, 1, 0},//绿
            {0.27f, 0.41f, 1f},//蓝
            {0.88f,0.43f,0.92f}};//紫
    public MySurfaceView(Context context){
        super(context);
        this.setEGLContextClientVersion(3);
        mRenderer = new SceneRederer();
        this.setRenderer(mRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public boolean onTouchEvent(MotionEvent e){
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()){
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;
                float dx = x - mPreviousX;
                for(SixPointedStar star : mRenderer.ha){
                    star.yAngle = dx *TOUCH_SCALE_FACTOR;
                    star.xAngle = dy *TOUCH_SCALE_FACTOR;
                }
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    private class SceneRederer implements GLSurfaceView.Renderer{
        SixPointedStar[] ha = new SixPointedStar[6];
//        Circle circle;
        public void onSurfaceCreated(GL10 gl, EGLConfig config){
            GLES30.glClearColor(0,0,0,1.0f);
            for(int i = 0; i < ha.length; ++i){
                ha[i] = new SixPointedStar(MySurfaceView.this,0.2f,0.5f,-0.5f*i,color[i]);
            }
//            circle = new Circle(MySurfaceView.this);
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        }

        public void onSurfaceChanged(GL10 gl, int width, int height){
            GLES30.glViewport(0,0,width,height);
            float radio = (float)width / height;
            MatrixState.setProjectFrustum(-radio*0.4f,radio*0.4f,-1*0.4f,1*0.4f,1,50);
            MatrixState.setCamera(0,0,3,0,0,0,0,1,0);
            MatrixState.setInitStack();
        }

        public void onDrawFrame(GL10 gl){
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
            for(int i = 0; i < ha.length; ++i){
                ha[i].drawSelf();
            }


//            circle.drawSelf(0,24);

//            MatrixState.pushMatrix();
//            MatrixState.pushMatrix();
//            MatrixState.translate(-0.2f,0,0);
//            circle.drawSelf(0,30);
//            MatrixState.popMatrix();
//            MatrixState.pushMatrix();
//            MatrixState.translate(0.2f,0,0);
//            circle.drawSelf(15,30);
//            MatrixState.popMatrix();
//            MatrixState.popMatrix();
        }
    }
}

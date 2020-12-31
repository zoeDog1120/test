package com.example.myapplication;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MySurfaceCubeView extends GLSurfaceView {
    SceneRederer mRenderer;
    float mPreviousX ;
    float mPreviousY ;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    public MySurfaceCubeView(Context context){
        super(context);
        this.setEGLContextClientVersion(3);
        mRenderer = new SceneRederer();
        this.setRenderer(mRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private class SceneRederer implements GLSurfaceView.Renderer{
        Cube cube;
        public void onSurfaceCreated(GL10 gl, EGLConfig config){
            GLES30.glClearColor(0,0,0,1.0f);
            cube = new Cube(MySurfaceCubeView.this);
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            GLES30.glEnable(GLES30.GL_CULL_FACE);//打开背面裁剪
        }

        public void onSurfaceChanged(GL10 gl, int width, int height){
            GLES30.glViewport(0,0,width,height);
            Constant.ratio = (float)width / height;
            MatrixState.setProjectFrustum(-Constant.ratio*0.8f,Constant.ratio*1.2f,-1,1,20,100);
            MatrixState.setCamera(-16f,8f,45f,0,0,0,0,1,0);
            MatrixState.setInitStack();
        }

        public void onDrawFrame(GL10 gl){
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
            MatrixState.pushMatrix();
            cube.drawSelf();
            MatrixState.popMatrix();
            MatrixState.pushMatrix();
            MatrixState.translate(1.0f,0,0);
            MatrixState.rotate(30,0,0,1);
            MatrixState.scale(0.4f,2.0f,0.6f);
            cube.drawSelf();
            MatrixState.popMatrix();
        }
    }
}

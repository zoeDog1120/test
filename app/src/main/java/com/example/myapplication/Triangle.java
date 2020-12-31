package com.example.myapplication;

import android.opengl.GLES30;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {
    public static float[] mProMatrix = new float[16];//投影矩阵
    public static float[] mVMatrix = new float[16];//摄像机位置朝向的参数矩阵
    public static float[] mMvpMatrix;//总变换矩阵
    static float[] mMMatrix = new float[16];
    FloatBuffer mVertexBuffer;//顶点坐标数据缓冲
    FloatBuffer mColorBuffer;//顶点颜色数据缓冲
    String mVertexShader;
    String mFragmentShader;
    int maPositionHandle;
    int maColorHandle;
    int muMvpMatrixHandle;
    int mProgram;
    float xAngle = 0;
    int vCount;//顶点数量
    public Triangle(MyTdView mv){
        initVertexData();
        intShader(mv);
    }
    public void drawSelf(){
        GLES30.glUseProgram(mProgram);
        Matrix.setRotateM(mMMatrix,0,0,1,0,0);
        Matrix.translateM(mMMatrix,0,0,0,1);
        Matrix.rotateM(mMMatrix,0,xAngle,1,0,0);
        GLES30.glUniformMatrix4fv(muMvpMatrixHandle,1,false,Triangle.getFinalMatrix(mMMatrix),0);
        GLES30.glVertexAttribPointer(maPositionHandle,3,GLES30.GL_FLOAT,false,3*4,mVertexBuffer);
        GLES30.glVertexAttribPointer(maColorHandle,4,GLES30.GL_FLOAT,false,4*4,mColorBuffer);
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glEnableVertexAttribArray(maColorHandle);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,0,vCount);
    }
    public void initVertexData() {
        vCount = 3;
        final float UNIT_SIZE = 0.2f;
        float vertices[] = new float[]{-4 * UNIT_SIZE , 0,0,
                                        0,-4 * UNIT_SIZE,0,
                                        4*UNIT_SIZE , 0 , 0};
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        float colors[] = new float[]{1,1, 0,0,
                0,0,1,0,
                0,1, 0 , 0};
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
    }
    public void intShader(MyTdView mv){
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh",mv.getResources());
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.sh",mv.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader,mFragmentShader);
        maPositionHandle = GLES30.glGetAttribLocation(mProgram,"aPosition");
        maColorHandle = GLES30.glGetAttribLocation(mProgram,"aColor");
        muMvpMatrixHandle = GLES30.glGetUniformLocation(mProgram,"uMVPMatrix");

    }

    public static float[] getFinalMatrix(float spec[]){
        mMvpMatrix = new float[16];
        Matrix.multiplyMM(mMvpMatrix,0,mVMatrix,0,spec,0);
        Matrix.multiplyMM(mMvpMatrix,0,mProMatrix,0,mMvpMatrix,0);
        return mMvpMatrix;
    }
    public void setAngle(float angle){
        xAngle = angle;
    }
    public void addAngle(float angle){
        xAngle += angle;
    }
}

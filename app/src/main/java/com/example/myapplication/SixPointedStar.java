package com.example.myapplication;

import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class SixPointedStar {
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
    float color[] = new float[3];
    float xAngle = 0,yAngle = 0;
    final float UNIT_SIZE = 1.0f;
    int vCount;//顶点数量
    public SixPointedStar(MySurfaceView mv,float r,float R,float z,float[] color){
        initVertexData(r,R,z);
        initShader(mv);
        this.color = color;
    }
    public void initVertexData(float R,float r,float z){
        List<Float> flist = new ArrayList<Float>();
        float tempAngle = 360 / 6;
        for (float angle = 0 ; angle < 360; angle += tempAngle){
            flist.add(0f);
            flist.add(0f);
            flist.add(z);//中心点

            flist.add((float)(UNIT_SIZE * R * Math.cos(Math.toRadians(angle))));
            flist.add((float)(UNIT_SIZE * R * Math.sin(Math.toRadians(angle))));
            flist.add(z);//第二个点

            flist.add((float)(UNIT_SIZE * r * Math.cos(Math.toRadians(angle + tempAngle * 0.5))));
            flist.add((float)(UNIT_SIZE * r * Math.sin(Math.toRadians(angle + tempAngle * 0.5))));
            flist.add(z);//第三个点

            flist.add(0f);
            flist.add(0f);
            flist.add(z);//中心点

            flist.add((float)(UNIT_SIZE * r * Math.cos(Math.toRadians(angle + tempAngle * 0.5))));
            flist.add((float)(UNIT_SIZE * r * Math.sin(Math.toRadians(angle + tempAngle * 0.5))));
            flist.add(z);//第二个点

            flist.add((float)(UNIT_SIZE * R * Math.cos(Math.toRadians(angle + tempAngle))));
            flist.add((float)(UNIT_SIZE * R * Math.sin(Math.toRadians(angle + tempAngle))));
            flist.add(z);//第三个点
        }
        vCount = flist.size() / 3;
        float[] vertexArray = new float[flist.size()];
        for (int i = 0 ; i < flist.size(); ++i){
            vertexArray[i] = flist.get(i);
        }
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertexArray);
        mVertexBuffer.position(0);

//        float[] colorArray = new float[vCount*4];
//        Log.e("yyy",String.valueOf(vCount));
//        for(int i = 0 ; i < vCount; ++i){
//            if(i%3 == 0){
//                colorArray[i*4] =1;
//                colorArray[i*4+1] =1;
//                colorArray[i*4+2] =1;
//                colorArray[i*4+3] =0;
//            }else{
//                colorArray[i*4] =0.45f;
//                colorArray[i*4+1] =0.75f;
//                colorArray[i*4+2] =0.75f;
//                colorArray[i*4+3] =0;
//            }
//        }
//        ByteBuffer cbb = ByteBuffer.allocateDirect(colorArray.length*4);
//        cbb.order(ByteOrder.nativeOrder());
//        mColorBuffer = cbb.asFloatBuffer();
//        mColorBuffer.put(colorArray);
//        mColorBuffer.position(0);
    }
    public void initShader(MySurfaceView mv){
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh",mv.getResources());
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.sh",mv.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader,mFragmentShader);
        maPositionHandle = GLES30.glGetAttribLocation(mProgram,"aPosition");
        maColorHandle = GLES30.glGetAttribLocation(mProgram,"aColor");
        muMvpMatrixHandle = GLES30.glGetUniformLocation(mProgram,"uMVPMatrix");
    }

    public void drawSelf(){
        GLES30.glUseProgram(mProgram);
        Matrix.setRotateM(mMMatrix,0,0,0,1,0);
        Matrix.translateM(mMMatrix,0,0,0,1);
        Matrix.setRotateM(mMMatrix,0,xAngle,1,0,0);
        Matrix.setRotateM(mMMatrix,0,yAngle,0,1,0);
        GLES30.glUniformMatrix4fv(muMvpMatrixHandle,1,false,MatrixState.getFinalMatrix(mMMatrix),0);
        GLES30.glVertexAttribPointer(maPositionHandle,3,GLES30.GL_FLOAT,false,3*4,mVertexBuffer);
        GLES30.glVertexAttrib4f(maColorHandle,color[0],color[1],color[2],1.0f);
//        GLES30.glVertexAttribPointer(maColorHandle,4,GLES30.GL_FLOAT,false,4*4,mColorBuffer);
        GLES30.glEnableVertexAttribArray(maPositionHandle);
//        GLES30.glEnableVertexAttribArray(maColorHandle);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES,0,vCount);
    }
}

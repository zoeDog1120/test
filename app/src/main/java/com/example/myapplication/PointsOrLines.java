package com.example.myapplication;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class PointsOrLines {
    int mProgram;
    int maColorHandle;
    int maPositionHandle;
    int muMVPMatrixHandle;
    String mVertexShader;
    String mFragmentShader;
    FloatBuffer mColorBuffer;
    FloatBuffer mVertexBuffer;
    int vCount;
    public PointsOrLines(MySurfaceView mv){
        initVertexData();
        initShader(mv);
    }
    public void initVertexData(){
        vCount = 5;
        float[] vertices = {
                0,0,0,
                Constant.UNIT_SIZE,Constant.UNIT_SIZE,0,
                -Constant.UNIT_SIZE,Constant.UNIT_SIZE,0,
                -Constant.UNIT_SIZE,-Constant.UNIT_SIZE,0,
                Constant.UNIT_SIZE,-Constant.UNIT_SIZE,0,
        };
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        float[] colors = {
                1,1,0,0,
                1,1,1,0,
                0,1,0,0,
                1,1,1,0,
                1,1,0,0,
        };
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
    }

    public void initShader(MySurfaceView mv){
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        maColorHandle= GLES30.glGetAttribLocation(mProgram, "aColor");
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void drawSelf(){
        GLES30.glUseProgram(mProgram);
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
        GLES30.glVertexAttribPointer
                (
                        maPositionHandle,
                        3,
                        GLES30.GL_FLOAT,
                        false,
                        3*4,
                        mVertexBuffer
                );
        GLES30.glVertexAttribPointer
                (
                        maColorHandle,
                        4,
                        GLES30.GL_FLOAT,
                        false,
                        4*4,
                        mColorBuffer
                );
        GLES30.glEnableVertexAttribArray(maPositionHandle);
        GLES30.glEnableVertexAttribArray(maColorHandle);
        GLES30.glLineWidth(10);
        switch (Constant.cur_draw_mode){
            case 1:
                GLES30.glDrawArrays(GLES30.GL_POINTS,0, vCount);
                break;
            case 2:
                GLES30.glDrawArrays(GLES30.GL_LINE_LOOP,0, vCount);
                break;
            case 3:
                GLES30.glDrawArrays(GLES30.GL_LINE_STRIP,0, vCount);
                break;
            case 4:
                GLES30.glDrawArrays(GLES30.GL_LINES,0, vCount);
                break;
        }
    }
}

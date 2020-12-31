package com.triangle;

public class Points {
    int vCount = 0;
    public void Points(MySurfaceView mv){
        initVertexData();
        initShader(mv);
    }
    public void initVertexData(){
        vCount = 9;
        float vertices[] = new float[vCount*3];
        for(int i = 0 ; i < vCount;++i){
            vertices[i*3] = (float)Math.random();
            vertices[i*3+1] = (float)Math.random();
            vertices[i*3+2] = 0;
        }
    }
    public void initShader(MySurfaceView mv){

    }

    public void drawSelf(){

    }

}

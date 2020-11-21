package com.example.myapplication;

import android.content.res.Resources;
import android.opengl.GLES30;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ShaderUtil {
    public static int loadShader(int shaderType,String source){
        int shader = GLES30.glCreateShader(shaderType);
        if (shader != 0 ){
            GLES30.glShaderSource(shader,source);
            GLES30.glCompileShader(shader);
            int[] compiled = new int[1];
            GLES30.glGetShaderiv(shader,GLES30.GL_COMPILE_STATUS,compiled,0);
            if (compiled[0] == 0 ){
                GLES30.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }
    public static int createProgram(String vertexSource,String fragmentSource){
        int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER,vertexSource);
        if (vertexShader == 0)return 0;
        int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER,fragmentSource);
        if(fragmentShader == 0)return 0;
        int programId = GLES30.glCreateProgram();
        if(programId != 0){
            GLES30.glAttachShader(programId,vertexShader);
            checkGlError("glAttachShader");
            GLES30.glAttachShader(programId,fragmentShader);
            checkGlError("glAttachShader");
            GLES30.glLinkProgram(programId);
            int[] link = new int[1];
            GLES30.glGetProgramiv(programId,GLES30.GL_LINK_STATUS,link,0);
            if(link[0] != GLES30.GL_TRUE ){
                GLES30.glDeleteProgram(programId);
                programId = 0 ;
            }
        }
        return programId;
    }

    public static void checkGlError(String op){
        int error;
        while((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR){

            throw new RuntimeException(op + "glError " + error);
        }
    }

    public static String loadFromAssetFile(String fname, Resources r){
        String result = null;
        try {
            InputStream in = r.getAssets().open(fname);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int ch = 0;
            while((ch = in.read()) != -1){
                baos.write(ch);
            }
            byte[] buff = baos.toByteArray();
            result = new String(buff,"UTF-8");
            result = result.replaceAll("\\r\\n","\n");
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}

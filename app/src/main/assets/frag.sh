#version 300 es
precision mediump float;
in vec4 vColor;
in vec4 vPosition;
out vec4 fragColor;
void main(){
    fragColor = vColor;
}
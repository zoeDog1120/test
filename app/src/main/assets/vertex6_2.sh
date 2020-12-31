#version 300 es
uniform mat4 uMVPMatrix; 						//总变换矩阵
uniform mat4 uMMatrix; 							//变换矩阵(包括平移、旋转、缩放)
uniform vec3 uLightLocation;						//光源位置
uniform vec3 aCamera;
uniform vec3 uLightDirection;
in vec3 aPosition;  						//顶点位置
in vec3 aNormal;    						//顶点法向量
out vec3 vPosition;							//用于传递给片元着色器的顶点位置
out vec4 vDiffuse;							//用于传递给片元着色器的散射光分量
out vec4 vSpecular;                         //镜面光最终强度
out vec4 vAmbient;
void pointLight (								//定位光照计算的方法
  in vec3 normal,								//法向量
  inout vec4 ambient,                           //环境光计算结果
  inout vec4 diffuse,                           //散射光计算结果
  inout vec4 specular,								//镜面光计算结果
  in vec3 lightLocation,							//光源位置
  in vec4 lightSpecular,							//镜面光强度
  in vec4 lightDiffuse,                             //散射光强度
  in vec4 lightAmbient                             //环境光强度
){
    vec3 normalTarget = normal + aPosition;
    vec3 newNormal = (uMMatrix * vec4(normalTarget,1)).xyz - (uMMatrix * vec4(aPosition,1)).xyz;
    newNormal = normalize(newNormal);
    vec3 eye = normalize(aCamera - (uMMatrix * vec4(aPosition,1)).xyz);
    //vec3 vp = normalize(lightLocation - (uMMatrix * vec4(aPosition,1)).xyz);
    vec3 vp = normalize(lightLocation)
    vec3 halfVector = normalize(eye + vp);
    float shininess = 50.0f;
    float nDotViewHalfVector = dot(newNormal,halfVector);
    float powerFactor = max(0.0f,pow(nDotViewHalfVector,shininess));
    specular = lightSpecular * powerFactor;
    ambient = lightAmbient;
    float nDotViewPosition = max(0.0f,dot(newNormal,vp));
    diffuse = lightDiffuse * nDotViewPosition;
}
void main(){
   gl_Position = uMVPMatrix * vec4(aPosition,1); 	//根据总变换矩阵计算此次绘制此顶点的位置 
   vec4 diffuseTemp=vec4(0.0,0.0,0.0,0.0);
   vec4 specularTemp=vec4(0.0,0.0,0.0,0.0);
   vec4 ambientTemp = vec4(0.0,0.0,0.0,0.0);
   pointLight(normalize(aNormal), ambientTemp,diffuseTemp,specularTemp, uLightLocation, vec4(0.7,0.7,0.7,1.0),vec4(0.8,0.8,0.8,1.0),vec4(0.15,0.15,0.15,1.0));
   vDiffuse=diffuseTemp;					//将散射光最终强度传给片元着色器
   vSpecular = specularTemp;
   vAmbient = ambientTemp;
   vPosition = aPosition; 					//将顶点的位置传给片元着色器
}


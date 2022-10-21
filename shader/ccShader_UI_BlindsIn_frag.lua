--[[
Author: yuyang
Date: 2021-08-05 15:50:58
Description: 图片渐隐效果
--]]

local frag_str = [[

#ifdef GL_ES
varying lowp vec4 v_fragmentColor;
varying mediump vec2 v_texCoord;
precision mediump float;
#else
varying vec4 v_fragmentColor;
varying vec2 v_texCoord;
#endif

uniform float u_direction;
uniform float u_blinds_num;
uniform float u_percent;

void main()
{
    vec4 c = texture2D(CC_Texture0, v_texCoord);

    //从左向右
    if (u_direction < 2.0) {
        float base = 1.0 / u_blinds_num;
        float index = floor(v_texCoord.x / base);
        float temp = v_texCoord.x - index * base;
        float temp2 = temp / base;
        if (temp2 >= u_percent ) {
            c = c * 0.0;
        }
    }

    //从右向左
    if (u_direction >= 2.0 && u_direction < 3.0) {
        float base = 1.0 / u_blinds_num;
        float index = floor(v_texCoord.x / base);
        float temp = v_texCoord.x - index * base;
        float temp2 = temp / base;
        if (temp2 <= (1.0 - u_percent)) {
            c = c * 0.0;
        }
    }

    //从上向下
    if (u_direction >= 3.0 && u_direction < 4.0) {
        float base = 1.0 / u_blinds_num;
        float index = floor(v_texCoord.y / base);
        float temp = v_texCoord.y - index * base;
        float temp2 = temp / base;
        if (temp2 >= u_percent ) {
            c = c * 0.0;
        }
    }

    //从下向上
    if (u_direction >= 4.0 && u_direction < 5.0) {
        float base = 1.0 / u_blinds_num;
        float index = floor(v_texCoord.y / base);
        float temp = v_texCoord.y - index * base;
        float temp2 = temp / base;
        if (temp2 <= (1.0 - u_percent)) {
            c = c * 0.0;
        }
    }

    gl_FragColor = v_fragmentColor * c;
}
    
]]

return frag_str

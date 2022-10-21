--[[
Author: yuyang
Date: 2021-08-05 15:50:58
Description: 图片渐显效果
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
uniform float u_fade_radius;
uniform float u_percent;

void main()
{
    vec4 c = texture2D(CC_Texture0, v_texCoord);

    //从左向右
    if (u_direction < 2.0) {
        float temp = v_texCoord.x - u_percent;
        if (temp >= 0.0) {
            float temp2 = abs(temp);
            if (temp2 <= u_fade_radius) {
                c = c * (1.0 - temp2/u_fade_radius);
            } else {
                c = c * 0.0;
            }
        }
    }

    //从右向左
    if (u_direction >= 2.0 && u_direction < 3.0) {
        float temp = v_texCoord.x - (1.0 - u_percent);
        if (temp <= 0.0) {
            float temp2 = abs(temp);
            if (temp2 <= u_fade_radius) {
                c = c * (1.0 - temp2/u_fade_radius);
            } else {
                c = c * 0.0;
            }
        }
    }

    //从上向下
    if (u_direction >= 3.0 && u_direction < 4.0) {
        float temp = v_texCoord.y - u_percent;
        if (temp >= 0.0) {
            float temp2 = abs(temp);
            if (temp2 <= u_fade_radius) {
                c = c * (1.0 - temp2/u_fade_radius);
            } else {
                c = c * 0.0;
            }
        }
    }

    //从下向上
    if (u_direction >= 4.0 && u_direction < 5.0) {
        float temp = v_texCoord.y - (1.0 - u_percent);
        if (temp <= 0.0) {
            float temp2 = abs(temp);
            if (temp2 <= u_fade_radius) {
                c = c * (1.0 - temp2/u_fade_radius);
            } else {
                c = c * 0.0;
            }
        }
    }

    gl_FragColor = v_fragmentColor * c;
}
    
]]

return frag_str

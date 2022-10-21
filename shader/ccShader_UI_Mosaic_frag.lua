--[[
Author: yuyang
Date: 2021-08-06 15:41:35
Description: 图片马赛克处理
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

uniform float u_cell_width;
uniform float u_cell_height;

void main(void)
{
    vec2 size = textureSize(CC_Texture0, 0);

    float block_w = u_cell_width / size.x;
    float block_x_idx = floor(v_texCoord.x / block_w);

    float block_h = u_cell_height / size.y;
    float block_y_idx = floor(v_texCoord.y / block_h);

    vec2 realPos = vec2(block_w * (block_x_idx + 0.5), block_h * (block_y_idx + 0.5));
    vec4 c = texture2D(CC_Texture0, realPos);
    gl_FragColor = v_fragmentColor * c;
}
]]

return frag_str


local frag_str = [[


#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_fragmentColor;
varying vec2 v_texCoord;
uniform float gray_val;

void main(void)
{
    vec4 c = texture2D(CC_Texture0, v_texCoord);
    c = v_fragmentColor * c;
    gl_FragColor.xyz = vec3(0.3*c.r + 0.59*c.g*gray_val + 0.11*c.b);
    gl_FragColor.w = c.w;
}
]]

return frag_str

local vert_str = [[
    
attribute vec4 a_position;
attribute vec2 a_texCoord;
attribute vec4 a_color;

varying vec2 v_texCoord;

void main()
{
    gl_Position = CC_PMatrix * a_position;
    v_texCoord = a_texCoord;
}

]]

return vert_str
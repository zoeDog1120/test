local frag_str = [[

varying vec2 v_texCoord;
uniform mat4 u_hue;
uniform float u_alpha;

void main()
{
    vec4 pixColor = texture2D(CC_Texture0, v_texCoord);
    vec4 rgbColor ;

    if(u_hue[0][0]!=0.0)
    {
        rgbColor = u_hue * pixColor.rgba;
    }
    else
    {
        rgbColor = pixColor.rgba;
    }
  
    if(u_alpha==0.0)
    {
        gl_FragColor = vec4(0,0,0,0);
    }
    else
    {
        gl_FragColor = vec4(rgbColor.r,rgbColor.g,rgbColor.b, pixColor.a*u_alpha );
    }
}
    
]]

return frag_str

    -- gl_FragColor = vec4(rgbColor.r,rgbColor.g,rgbColor.b, pixColor.a*u_alpha );

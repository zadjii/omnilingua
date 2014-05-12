#ifdef GL_ES
precision mediump float;
#endif

attribute vec3 a_position;
attribute vec2 a_texCoord0;
attribute vec4 a_color;
attribute vec3 a_normal;

uniform mat4 proj;
uniform mat4 view;
uniform mat4 modl;

uniform mat4 mv;
uniform mat3 norm;
uniform mat4 mvp;

uniform int usingTexture;
uniform sampler2D texture0;
uniform sampler2D texture1;



//varying outs to the frag shader
varying vec4 v_world;
varying vec4 v_eye;
varying vec4 v_final;

varying vec3 v_norm;

varying vec4 v_color;
varying vec2 v_tc0;

void main()
{
  //This was in the last GUI Shader. Maybe useful in the future.
  //color = mix(vertex_color, colorTint.rgb, colorTint.a);

  v_final = modl * vec4(a_position, 1);
  gl_Position = v_final;
  v_color = a_color;
  v_tc0 = a_texCoord0;
}
#ifdef GL_ES
precision mediump float;
#endif

uniform mat4 proj;
uniform mat4 view;
uniform mat4 modl;

uniform mat4 mv;
uniform mat3 norm;
uniform mat4 mvp;

// --- varying ins from the vert shader --- //
varying vec4 v_world;
varying vec4 v_eye;
varying vec4 v_final;

varying vec4 v_norm;

varying vec4 v_color;

varying vec2 v_tc0;

uniform int usingTexture;
uniform sampler2D texture0;
uniform sampler2D texture1;

void main()
{
  vec4 final;
  if(usingTexture != 0){
    final = texture2D(texture0, v_tc0);
    if(final.a == 0.0) {
      discard;
    }
  }
  else{
    final = v_color;
  }
  
  gl_FragColor = final;
}
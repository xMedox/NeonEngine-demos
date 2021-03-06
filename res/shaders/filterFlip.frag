#version 330

in vec2 texCoord0;

uniform sampler2D R_filterTexture;

layout(location = 0) out vec4 outputFS;

void main(){
	outputFS = texture(R_filterTexture, vec2(texCoord0.s, 1.0 - texCoord0.t));
}
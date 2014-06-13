#version 330 core

uniform sampler2D texture0; //binded texture

varying vec2 vTexCoord;

layout(location = 0) out vec4 out_colour;//only works because theres one output


void main(void)
{
   	out_colour = texture(texture0,vTexCoord);
}
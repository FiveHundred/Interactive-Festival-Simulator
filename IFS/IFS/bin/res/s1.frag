#version 330 core

uniform sampler2D texture0;

in vec2 vTexCoord;
flat in vec3[4] spriteColours;

layout(location = 0) out vec4 out_colour;//only works because theres one output

void main(void)
{
	vec4 texColor = texture(texture0,vTexCoord.xy);
 	if(texColor.a == 0.0)//stop transparent fragments being written to depth buffer
		discard;
   	
   	int index = (255 - int(texColor.a * 255.0)) - 1;// 255 alpha has index of -1;
   	
   	vec3 blendColour = (index < 0) ? vec3(1.0) : spriteColours[index];   

   	out_colour = vec4(texColor.rgb*blendColour,1.0);
   	//out_colour = vec4(vTexCoord.xxx,1.0);
}

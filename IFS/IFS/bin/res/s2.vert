#version 330 core

//layout(location = 0) in vec3 pos;
//layout(location = 1) in vec2 texCoord;

uniform int screenWidth;
uniform int screenHeight;

uniform vec2 texPos;
uniform vec2 texSize;

out vec2 vTexCoord;

const vec2[4] verts = vec2[](vec2(0,0),vec2(0,1),vec2(1,0),vec2(1,1));

void main(void)
{
	vTexCoord = verts[gl_VertexID];
	
	vec2 pos = verts[gl_VertexID]*texSize + texPos;
	
	//convert screen coords to ndc (all components between -1 and 1)
	pos.x = ((pos.x * 2.0)/screenWidth) - 1.0;
	pos.y = ((pos.y * 2.0)/screenHeight) - 1.0;
	
	gl_Position = vec4(pos,0,1);
}
#version 330 core

//inputs
layout(location = 0) in vec2 inVertPos;
layout(location = 1) in vec3 inSpritePos;
layout(location = 2) in int  inFrame;
layout(location = 3) in uint  inColours;//four colours in the 256 colour palette

//outputs
out vec2 vTexCoord;
flat out vec3[4] spriteColours;

//varying vec3 vPosition; //the real fragment position before conversion to ndc

//constants
//todo: remove the constants below and have the program transform the vertPos in future to allow this shader to become a general purpose sprite sheet handling vertex shader

const int tileSize = 48;
const float root3 = 1.73205080757;
const mat4 transform = mat4(
					tileSize /2.0, 	-tileSize/4.0, 	-(tileSize/4.0)*root3,	0.0,
					-tileSize/2.0,	-tileSize/4.0, 	-(tileSize/4.0)*root3,	0.0,
					0.0,			 tileSize/2.0, 	-(tileSize/4.0)/root3, 	0.0,
					0.0, 			 0.0, 			0.0,					1.0);

//variables
uniform int screenWidth;
uniform int screenHeight;

uniform float scale = 1.0;//default values here
uniform vec2 cameraOffset = vec2(0.0,0.0);

//sprite sheet related variables
uniform int horizontalFrames;
uniform float frameWidth;
uniform float frameHeight;

//others
uniform isamplerBuffer paletteBuffer;

//declarations
vec3 lookupColour(int index);

void main(void)
{
	//calculate tex coords
	int framex = inFrame%horizontalFrames;
	int framey = inFrame/horizontalFrames;
	
	vTexCoord.x = frameWidth*float(framex);
	vTexCoord.y = frameHeight*float(framey);
	
	vTexCoord.x += frameWidth * float(gl_VertexID >= 2);
	vTexCoord.y += frameHeight * float(gl_VertexID == 1 || gl_VertexID == 3);
	
	//transform the vertex positions into scene coords (positions on screen) from a world position //remove this in future and do it on cpu
	vec3 vertPos = vec3(inVertPos,0);
	vertPos += (transform * vec4(inSpritePos,1)).xyz;
	
	//vertPos.x += 10.0 * float(gl_VertexID==3);
	//vertPos.x += inFrame*10;
	
	//transform the screen coords based on camera position and zoom.
	vertPos.xy -= cameraOffset.xy;
	vertPos.xy *= scale;
	
	//convert scene coords to ndc (all components between -1 and 1)
	vertPos.x = (vertPos.x * 2.0)/screenWidth;
	vertPos.y = (vertPos.y * 2.0)/screenHeight;
	//use a non linear function to map z from -infinity -> +infinity to -1 to 1
	vertPos.z = 2.0*atan(vertPos.z)/3.14159265358979323846264;
	
	//now populate the colours array:
	uint inColoursCpy = inColours;
	for(int i = 0; i < 4; i++)
	{
		uint index = inColoursCpy & 0x000000FF;
		spriteColours[i] = lookupColour(int(index));
		inColoursCpy >>= 8;
	}
	
	gl_Position = vec4(vertPos,1);
}
vec3 lookupColour(int index)
{
	ivec4 tvec = texelFetch(paletteBuffer,index);
	float r = float(tvec.r & 0x000000FF);
   	float g = float(tvec.g & 0x000000FF);
   	float b = float(tvec.b & 0x000000FF);
   	return (vec3(r,g,b)/255.0);
}
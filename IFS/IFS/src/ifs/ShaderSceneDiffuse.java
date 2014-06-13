package ifs;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;

public class ShaderSceneDiffuse extends Shader {	
	//scene shader specifics:
	int scaleLoc;
	int cameraOffsetLoc;
	
	int horizontalFramesLoc;
	int frameWidthLoc;
	int frameHeightLoc;
	
	//256 colour palette tbo
	int tbo;
	int tbo_tex;
	
	//inst id texture
	int instid;
	
	public ShaderSceneDiffuse(String vert, String frag, int width, int height)
	{
		super(vert,frag);
		
		//specify the vertex arrays that this shader uses.
		glBindVertexArray(vertexArray);
		glEnableVertexAttribArray(0);//vert pos
		glEnableVertexAttribArray(1);//sprite pos (instance data)
		glEnableVertexAttribArray(2);//frame (instance data)
		glEnableVertexAttribArray(3);//colours (instance data)

		//set up the uniforms that this shader uses.
		ARBShaderObjects.glUseProgramObjectARB(program);
		glUniform1i(glGetUniformLocation(program,"screenWidth"),width);
		glUniform1i(glGetUniformLocation(program,"screenHeight"),height);
		
		scaleLoc = glGetUniformLocation(program, "scale");
		cameraOffsetLoc = glGetUniformLocation(program, "cameraOffset");
		
		horizontalFramesLoc = glGetUniformLocation(program, "horizontalFrames");
		frameWidthLoc = glGetUniformLocation(program, "frameWidth");
		frameHeightLoc = glGetUniformLocation(program, "frameHeight");
		
		//set up tbo:
		ByteBuffer bb = ByteBuffer.allocateDirect(paletteData.length);
		bb/*.asFloatBuffer()*/.put(paletteData);
		bb.flip();
		
		//buffer
		tbo = glGenBuffers();
		glBindBuffer(GL_TEXTURE_BUFFER,tbo);
		glBufferData(GL_TEXTURE_BUFFER,bb, GL_STATIC_DRAW);
		
		//texture wrapper of buffer
		tbo_tex = glGenTextures();
		glActiveTexture(GL_TEXTURE0+1); // specify tiu 1
		glBindTexture(GL_TEXTURE_BUFFER, tbo_tex); // specify tbo_tex in tiu 1
		glTexBuffer(GL_TEXTURE_BUFFER, GL_RGBA8I, tbo); //set it to contain the data put in the tbo
		
		glUniform1i(glGetUniformLocation(program,"paletteBuffer"),1); // set the sampler to point to tiu 1 which should contain the tbo data
		
		glActiveTexture(GL_TEXTURE0);
		
		validate();
	}

	public void use(float zoomScale, Vector2f cameraOffset)
	{
		super.use();
		glUniform1f(scaleLoc,zoomScale);
		glUniform2f(cameraOffsetLoc,cameraOffset.x,cameraOffset.y);
	}
	
	byte[] paletteData = {
		    0, 0, 0, 0, //index: 0
		    -51, 0, 0, 0, //index: 1
		    0, -51, 0, 0, //index: 2
		    -51, -51, 0, 0, //index: 3
		    0, 0, -18, 0, //index: 4
		    -51, 0, -51, 0, //index: 5
		    0, -51, -51, 0, //index: 6
		    -27, -27, -27, 0, //index: 7
		    127, 127, 127, 0, //index: 8
		    -1, 0, 0, 0, //index: 9
		    0, -1, 0, 0, //index: 10
		    -1, -1, 0, 0, //index: 11
		    92, 92, -1, 0, //index: 12
		    -1, 0, -1, 0, //index: 13
		    0, -1, -1, 0, //index: 14
		    -1, -1, -1, 0, //index: 15
		    0, 0, 0, 0, //index: 16
		    0, 0, 95, 0, //index: 17
		    0, 0, -121, 0, //index: 18
		    0, 0, -81, 0, //index: 19
		    0, 0, -41, 0, //index: 20
		    0, 0, -1, 0, //index: 21
		    0, 95, 0, 0, //index: 22
		    0, 95, 95, 0, //index: 23
		    0, 95, -121, 0, //index: 24
		    0, 95, -81, 0, //index: 25
		    0, 95, -41, 0, //index: 26
		    0, 95, -1, 0, //index: 27
		    0, -121, 0, 0, //index: 28
		    0, -121, 95, 0, //index: 29
		    0, -121, -121, 0, //index: 30
		    0, -121, -81, 0, //index: 31
		    0, -121, -41, 0, //index: 32
		    0, -121, -1, 0, //index: 33
		    0, -81, 0, 0, //index: 34
		    0, -81, 95, 0, //index: 35
		    0, -81, -121, 0, //index: 36
		    0, -81, -81, 0, //index: 37
		    0, -81, -41, 0, //index: 38
		    0, -81, -1, 0, //index: 39
		    0, -41, 0, 0, //index: 40
		    0, -41, 95, 0, //index: 41
		    0, -41, -121, 0, //index: 42
		    0, -41, -81, 0, //index: 43
		    0, -41, -41, 0, //index: 44
		    0, -41, -1, 0, //index: 45
		    0, -1, 0, 0, //index: 46
		    0, -1, 95, 0, //index: 47
		    0, -1, -121, 0, //index: 48
		    0, -1, -81, 0, //index: 49
		    0, -1, -41, 0, //index: 50
		    0, -1, -1, 0, //index: 51
		    95, 0, 0, 0, //index: 52
		    95, 0, 95, 0, //index: 53
		    95, 0, -121, 0, //index: 54
		    95, 0, -81, 0, //index: 55
		    95, 0, -41, 0, //index: 56
		    95, 0, -1, 0, //index: 57
		    95, 95, 0, 0, //index: 58
		    95, 95, 95, 0, //index: 59
		    95, 95, -121, 0, //index: 60
		    95, 95, -81, 0, //index: 61
		    95, 95, -41, 0, //index: 62
		    95, 95, -1, 0, //index: 63
		    95, -121, 0, 0, //index: 64
		    95, -121, 95, 0, //index: 65
		    95, -121, -121, 0, //index: 66
		    95, -121, -81, 0, //index: 67
		    95, -121, -41, 0, //index: 68
		    95, -121, -1, 0, //index: 69
		    95, -81, 0, 0, //index: 70
		    95, -81, 95, 0, //index: 71
		    95, -81, -121, 0, //index: 72
		    95, -81, -81, 0, //index: 73
		    95, -81, -41, 0, //index: 74
		    95, -81, -1, 0, //index: 75
		    95, -41, 0, 0, //index: 76
		    95, -41, 95, 0, //index: 77
		    95, -41, -121, 0, //index: 78
		    95, -41, -81, 0, //index: 79
		    95, -41, -41, 0, //index: 80
		    95, -41, -1, 0, //index: 81
		    95, -1, 0, 0, //index: 82
		    95, -1, 95, 0, //index: 83
		    95, -1, -121, 0, //index: 84
		    95, -1, -81, 0, //index: 85
		    95, -1, -41, 0, //index: 86
		    95, -1, -1, 0, //index: 87
		    -121, 0, 0, 0, //index: 88
		    -121, 0, 95, 0, //index: 89
		    -121, 0, -121, 0, //index: 90
		    -121, 0, -81, 0, //index: 91
		    -121, 0, -41, 0, //index: 92
		    -121, 0, -1, 0, //index: 93
		    -121, 95, 0, 0, //index: 94
		    -121, 95, 95, 0, //index: 95
		    -121, 95, -121, 0, //index: 96
		    -121, 95, -81, 0, //index: 97
		    -121, 95, -41, 0, //index: 98
		    -121, 95, -1, 0, //index: 99
		    -121, -121, 0, 0, //index: 100
		    -121, -121, 95, 0, //index: 101
		    -121, -121, -121, 0, //index: 102
		    -121, -121, -81, 0, //index: 103
		    -121, -121, -41, 0, //index: 104
		    -121, -121, -1, 0, //index: 105
		    -121, -81, 0, 0, //index: 106
		    -121, -81, 95, 0, //index: 107
		    -121, -81, -121, 0, //index: 108
		    -121, -81, -81, 0, //index: 109
		    -121, -81, -41, 0, //index: 110
		    -121, -81, -1, 0, //index: 111
		    -121, -41, 0, 0, //index: 112
		    -121, -41, 95, 0, //index: 113
		    -121, -41, -121, 0, //index: 114
		    -121, -41, -81, 0, //index: 115
		    -121, -41, -41, 0, //index: 116
		    -121, -41, -1, 0, //index: 117
		    -121, -1, 0, 0, //index: 118
		    -121, -1, 95, 0, //index: 119
		    -121, -1, -121, 0, //index: 120
		    -121, -1, -81, 0, //index: 121
		    -121, -1, -41, 0, //index: 122
		    -121, -1, -1, 0, //index: 123
		    -81, 0, 0, 0, //index: 124
		    -81, 0, 95, 0, //index: 125
		    -81, 0, -121, 0, //index: 126
		    -81, 0, -81, 0, //index: 127
		    -81, 0, -41, 0, //index: 128
		    -81, 0, -1, 0, //index: 129
		    -81, 95, 0, 0, //index: 130
		    -81, 95, 95, 0, //index: 131
		    -81, 95, -121, 0, //index: 132
		    -81, 95, -81, 0, //index: 133
		    -81, 95, -41, 0, //index: 134
		    -81, 95, -1, 0, //index: 135
		    -81, -121, 0, 0, //index: 136
		    -81, -121, 95, 0, //index: 137
		    -81, -121, -121, 0, //index: 138
		    -81, -121, -81, 0, //index: 139
		    -81, -121, -41, 0, //index: 140
		    -81, -121, -1, 0, //index: 141
		    -81, -81, 0, 0, //index: 142
		    -81, -81, 95, 0, //index: 143
		    -81, -81, -121, 0, //index: 144
		    -81, -81, -81, 0, //index: 145
		    -81, -81, -41, 0, //index: 146
		    -81, -81, -1, 0, //index: 147
		    -81, -41, 0, 0, //index: 148
		    -81, -41, 95, 0, //index: 149
		    -81, -41, -121, 0, //index: 150
		    -81, -41, -81, 0, //index: 151
		    -81, -41, -41, 0, //index: 152
		    -81, -41, -1, 0, //index: 153
		    -81, -1, 0, 0, //index: 154
		    -81, -1, 95, 0, //index: 155
		    -81, -1, -121, 0, //index: 156
		    -81, -1, -81, 0, //index: 157
		    -81, -1, -41, 0, //index: 158
		    -81, -1, -1, 0, //index: 159
		    -41, 0, 0, 0, //index: 160
		    -41, 0, 95, 0, //index: 161
		    -41, 0, -121, 0, //index: 162
		    -41, 0, -81, 0, //index: 163
		    -41, 0, -41, 0, //index: 164
		    -41, 0, -1, 0, //index: 165
		    -41, 95, 0, 0, //index: 166
		    -41, 95, 95, 0, //index: 167
		    -41, 95, -121, 0, //index: 168
		    -41, 95, -81, 0, //index: 169
		    -41, 95, -41, 0, //index: 170
		    -41, 95, -1, 0, //index: 171
		    -41, -121, 0, 0, //index: 172
		    -41, -121, 95, 0, //index: 173
		    -41, -121, -121, 0, //index: 174
		    -41, -121, -81, 0, //index: 175
		    -41, -121, -41, 0, //index: 176
		    -41, -121, -1, 0, //index: 177
		    -41, -81, 0, 0, //index: 178
		    -41, -81, 95, 0, //index: 179
		    -41, -81, -121, 0, //index: 180
		    -41, -81, -81, 0, //index: 181
		    -41, -81, -41, 0, //index: 182
		    -41, -81, -1, 0, //index: 183
		    -41, -41, 0, 0, //index: 184
		    -41, -41, 95, 0, //index: 185
		    -41, -41, -121, 0, //index: 186
		    -41, -41, -81, 0, //index: 187
		    -41, -41, -41, 0, //index: 188
		    -41, -41, -1, 0, //index: 189
		    -41, -1, 0, 0, //index: 190
		    -41, -1, 95, 0, //index: 191
		    -41, -1, -121, 0, //index: 192
		    -41, -1, -81, 0, //index: 193
		    -41, -1, -41, 0, //index: 194
		    -41, -1, -1, 0, //index: 195
		    -1, 0, 0, 0, //index: 196
		    -1, 0, 95, 0, //index: 197
		    -1, 0, -121, 0, //index: 198
		    -1, 0, -81, 0, //index: 199
		    -1, 0, -41, 0, //index: 200
		    -1, 0, -1, 0, //index: 201
		    -1, 95, 0, 0, //index: 202
		    -1, 95, 95, 0, //index: 203
		    -1, 95, -121, 0, //index: 204
		    -1, 95, -81, 0, //index: 205
		    -1, 95, -41, 0, //index: 206
		    -1, 95, -1, 0, //index: 207
		    -1, -121, 0, 0, //index: 208
		    -1, -121, 95, 0, //index: 209
		    -1, -121, -121, 0, //index: 210
		    -1, -121, -81, 0, //index: 211
		    -1, -121, -41, 0, //index: 212
		    -1, -121, -1, 0, //index: 213
		    -1, -81, 0, 0, //index: 214
		    -1, -81, 95, 0, //index: 215
		    -1, -81, -121, 0, //index: 216
		    -1, -81, -81, 0, //index: 217
		    -1, -81, -41, 0, //index: 218
		    -1, -81, -1, 0, //index: 219
		    -1, -41, 0, 0, //index: 220
		    -1, -41, 95, 0, //index: 221
		    -1, -41, -121, 0, //index: 222
		    -1, -41, -81, 0, //index: 223
		    -1, -41, -41, 0, //index: 224
		    -1, -41, -1, 0, //index: 225
		    -1, -1, 0, 0, //index: 226
		    -1, -1, 95, 0, //index: 227
		    -1, -1, -121, 0, //index: 228
		    -1, -1, -81, 0, //index: 229
		    -1, -1, -41, 0, //index: 230
		    -1, -1, -1, 0, //index: 231
		    8, 8, 8, 0, //index: 232
		    18, 18, 18, 0, //index: 233
		    28, 28, 28, 0, //index: 234
		    38, 38, 38, 0, //index: 235
		    48, 48, 48, 0, //index: 236
		    58, 58, 58, 0, //index: 237
		    68, 68, 68, 0, //index: 238
		    78, 78, 78, 0, //index: 239
		    88, 88, 88, 0, //index: 240
		    98, 98, 98, 0, //index: 241
		    108, 108, 108, 0, //index: 242
		    118, 118, 118, 0, //index: 243
		    -128, -128, -128, 0, //index: 244
		    -118, -118, -118, 0, //index: 245
		    -108, -108, -108, 0, //index: 246
		    -98, -98, -98, 0, //index: 247
		    -88, -88, -88, 0, //index: 248
		    -78, -78, -78, 0, //index: 249
		    -68, -68, -68, 0, //index: 250
		    -58, -58, -58, 0, //index: 251
		    -48, -48, -48, 0, //index: 252
		    -38, -38, -38, 0, //index: 253
		    -28, -28, -28, 0, //index: 254
		    -18, -18, -18, 0 //index: 255
	};
}

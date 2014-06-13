package ifs;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL33.*;

public class PainterSpriteSheet { //handles the rending of sprite sheets
	
	//int sheetWidth;
	//int sheetHeight;
	int horizontalFrames;
	int verticalFrames;
	
	float frameWidth;
	float frameHeight;
	
	float[] vertexPos;
	
	int spritesToDraw = 0;
	
	//int shaderProgramToUse = 0;
	
	//position buffer must be provided as it is dependant on the type of entity this sprite sheet is for (blocks have a different origin to characters)
	public PainterSpriteSheet(Texture spriteSheet, int elementWidth, int elementHeight, float[] vertexPositions)
	{
		int sheetWidth = spriteSheet.iwidth;
		int sheetHeight = spriteSheet.iheight;
		horizontalFrames = sheetWidth/elementWidth;
		verticalFrames = sheetHeight/elementHeight;
		
		//these values are in texture coords
		frameWidth = 1f/(float)(horizontalFrames);
		frameHeight = 1f/(float)(verticalFrames);
		
		this.vertexPos = vertexPositions;
	}
	
	public void begin() {
		GraphicsResources.gpVBOBuffer.clear();
		spritesToDraw = 0;
		
		for (int i = 0; i < vertexPos.length; i++)
		GraphicsResources.gpVBOBuffer.putFloat(vertexPos[i]);
	}
	
	public void draw(Vector3f pos, int framex, int framey, byte[] colours) {
		GraphicsResources.gpVBOBuffer.putFloat(pos.x);
		GraphicsResources.gpVBOBuffer.putFloat(pos.y);
		GraphicsResources.gpVBOBuffer.putFloat(pos.z);
		GraphicsResources.gpVBOBuffer.putInt(framex + framey * horizontalFrames);
		
		GraphicsResources.gpVBOBuffer.put(colours);//should only be four bytes
		//total of 20 bytes of data.
		
		spritesToDraw++;
	}
	
	public void end(Texture t) {
		//put all the data from the posBuffer byte buffer into the actual spritevbo
		GraphicsResources.gpVBOBuffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER,GraphicsResources.gpVBO);
		glBufferSubData(GL_ARRAY_BUFFER,0,GraphicsResources.gpVBOBuffer);
		
		//byte[] dbg = new byte[GraphicsResources.spriteSheetVBOBuffer.limit()];
		//GraphicsResources.spriteSheetVBOBuffer.get(dbg);
		
		//dbg shit
		byte[] memes = new byte[GraphicsResources.gpVBOBuffer.remaining()];
		GraphicsResources.gpVBOBuffer.get(memes);
		
		//vert pos
		glVertexAttribPointer(
				0,//index (0 is VertPos)
				2,//components per attrib
				GL_FLOAT,//type
				false,//normalized?
				0, //stride
				0//starting element offset
				);
		glVertexAttribDivisor(0,0);
		
		//sprite pos (instance data)
		glVertexAttribPointer(
				1,//index (1 is SpritePos)
				3,//components per attrib
				GL_FLOAT,//type
				false,//normalized?
				20, //stride
				8*4//starting element offset
				);
		glVertexAttribDivisor(1,1);
		
		//frame (instance data)
		glVertexAttribIPointer(
				2,//index (2 is frame)
				1,//components per attrib
				GL_INT,//type
				20, //stride
				(8+3)*4//starting element offset
				);
		glVertexAttribDivisor(2,1);
		
		//colours (instance data)
		glVertexAttribIPointer(
				3,//index (3 is colours)
				1,//components per attrib
				GL_UNSIGNED_INT,//type
				20, //stride
				(8+4)*4//starting element offset
				);
		glVertexAttribDivisor(3,1);
		
		//put sprite sheet info into shader:
		glUniform1i(GraphicsResources.sceneShader.horizontalFramesLoc,horizontalFrames);
		glUniform1f(GraphicsResources.sceneShader.frameWidthLoc,frameWidth);
		glUniform1f(GraphicsResources.sceneShader.frameHeightLoc,frameHeight);
		
		//GraphicsResources.sceneShader.debug();
		t.bind();
		//glUniform1i(glGetUniformLocation(GraphicsResources.sceneShader.program,"texture1"),1);
		
		glDrawArraysInstanced(GL_TRIANGLE_STRIP,0,4,spritesToDraw);
		//glBindBuffer(GL_ARRAY_BUFFER, posVBO);
	}
}

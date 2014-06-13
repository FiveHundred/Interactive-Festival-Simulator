package ifs;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import org.lwjgl.util.vector.Vector3f;

public class PainterBlock extends PainterSpriteSheet{

	static final int blockWidth = 48;
	static final int blockHeight = 49;
	
	static final float[] vertexPos = {
			0f,							(+3f/4f)*ifs.IFS._tile_diagonal_size+1f, //top left (the plus 1 is because the height is actually 49 pixels for proper outlines)
			0f,							(-1f/4f)*ifs.IFS._tile_diagonal_size,//bottom left
			ifs.IFS._tile_diagonal_size,(+3f/4f)*ifs.IFS._tile_diagonal_size+1f, //top right (the plus 1 is because the height is actually 49 pixels for proper outlines)
			ifs.IFS._tile_diagonal_size,(-1f/4f)*ifs.IFS._tile_diagonal_size //bottom right
		};
	
	/// spriteSheet is just an example sprite sheet used to set up stuff.
	// it is not used or remembered in any way.
	public PainterBlock(Texture spriteSheet)
	{
		super(spriteSheet,blockWidth,blockHeight,vertexPos);//in the superclass
	}

}

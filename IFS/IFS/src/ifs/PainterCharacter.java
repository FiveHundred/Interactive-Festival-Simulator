package ifs;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

public class PainterCharacter extends PainterSpriteSheet{

	static final int characterWidth = 50;
	static final int characterHeight = 60;
	
	static final float[] vertexPos = {
			-(characterWidth/2), characterHeight, //top left
			-(characterWidth/2), 0f,//bottom left
			+(characterWidth/2), characterHeight, //top right
			+(characterWidth/2), 0f //bottom right
			};
		
	/// spriteSheet is just an example sprite sheet used to set up stuff.
	// it is not used or remembered in any way.
	public PainterCharacter(Texture spriteSheet)
	{
		super(spriteSheet,characterWidth,characterHeight,vertexPos);//in the superclass
	}
}
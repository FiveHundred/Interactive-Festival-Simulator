package ifs;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class ShaderTexture extends Shader {	
	
	public int texPosLoc;
	public int texSizeLoc;
	
	public ShaderTexture(String vert, String frag, int width, int height)
	{
		super(vert,frag);

		//stuff below this is specific to a "texture shader"
		ARBShaderObjects.glUseProgramObjectARB(program);
		glUniform1i(glGetUniformLocation(program,"screenWidth"),width);
		glUniform1i(glGetUniformLocation(program,"screenHeight"),height);
		
		texPosLoc = glGetUniformLocation(program, "texPos");
		texSizeLoc = glGetUniformLocation(program, "texSize");
	}
}

package ifs;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;

public class Shader {
	int program;
	int vertexArray;
	public Shader(String vert, String frag)
	{
		int shaderv = ARBShaderObjects.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
		int shaderf = ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);

		ARBShaderObjects.glShaderSourceARB(shaderv, vert);
		ARBShaderObjects.glShaderSourceARB(shaderf, frag);
		
		ARBShaderObjects.glCompileShaderARB(shaderv);
		if (ARBShaderObjects.glGetObjectParameteriARB(shaderv, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE)
			throw new RuntimeException("Failed to compile vertex shader: " + getLogInfo(shaderv));
		
		ARBShaderObjects.glCompileShaderARB(shaderf);
		if (ARBShaderObjects.glGetObjectParameteriARB(shaderf, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL_FALSE)
			throw new RuntimeException("Failed to compile fragment shader" + getLogInfo(shaderf));
		
		program = ARBShaderObjects.glCreateProgramObjectARB();
		ARBShaderObjects.glAttachObjectARB(program, shaderf);
		ARBShaderObjects.glAttachObjectARB(program, shaderv);
		
		ARBShaderObjects.glLinkProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL_FALSE)
			throw new RuntimeException("Failed to link shader");
		
		vertexArray = glGenVertexArrays();
	}
	public void validate()
	{
		ARBShaderObjects.glValidateProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL_FALSE)
			throw new RuntimeException("Failed to validate shader: " + getLogInfo(program));
	}
	public void use()
	{
		ARBShaderObjects.glUseProgramObjectARB(program);
		glBindVertexArray(vertexArray);
	}
	public void unuse()// just put this here to make code using shaders look neater
	{	
		glBindVertexArray(0);
		ARBShaderObjects.glUseProgramObjectARB(0);
	}
	private static String getLogInfo(int obj) {
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
		}
}

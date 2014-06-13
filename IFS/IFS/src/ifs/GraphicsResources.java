package ifs;

import ifs.IFS;

import static ifs.Utility.IsPowerOfTwo;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

//"global" (static) class containing a lot of shit used by everything else for drawing
public class GraphicsResources {
	
	//sprite sheet stuff
	
	static final int gpVBOSize = 1024*1024;// 1 mb = 65,536 total sprites.
	public static int gpVBO;//used to specify position and frame data for drawing a bunch of sprites from sprite sheets using instanced rendering
									// format is pos.x pos.y pos.z frame number. each component is 4 bytes for a total of 16 bytes per specified sprite.
	public static ByteBuffer gpVBOBuffer =  ByteBuffer.allocateDirect(gpVBOSize);
	
	//shaders
	public static ShaderSceneDiffuse sceneShader;
	public static ShaderTexture textureShader;
	
	//painters
	public static PainterCharacter characterPainter;
	public static PainterBlock blockPainter;
	
	//frame buffer shit:
	public static int framebuffer;
	public static int fbTex0;

	public static void init() throws Exception
	{
		TextureHandler.loadAll();//load textures
		
		//load shaders
		sceneShader = new ShaderSceneDiffuse(Utility.loadTextFile("/res/s1.vert"),Utility.loadTextFile("/res/s1.frag"),IFS._width,IFS._height);
		textureShader = new ShaderTexture(Utility.loadTextFile("/res/s2.vert"),Utility.loadTextFile("/res/s2.frag"),IFS._width,IFS._height);
		
		//set up buffers
		//the big general purpose vbo containing
		gpVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER,gpVBO);
		glBufferData(GL_ARRAY_BUFFER,gpVBOSize,GL_DYNAMIC_DRAW);
		gpVBOBuffer.order(ByteOrder.LITTLE_ENDIAN);
		
		//set up painter classes
		characterPainter = new PainterCharacter(TextureHandler.getTexture("lad1"));
		blockPainter = new PainterBlock(TextureHandler.getTexture("tiles"));
		
		//set up framebuffer:
		
		//gen framebuffer
		framebuffer = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
		
		//gen texutre
		fbTex0 = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, fbTex0);
		glTexImage2D(GL_TEXTURE_2D, 0,GL_RGB, IFS._width, IFS._height, 0,GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer)null);//create empty texture
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);//turn filtering off
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);// this shouldn't need to be here, but apparently my gfx card and driver doesn't follow the spec
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        
        //setup depth buffer
        int depthbuffer;
        depthbuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, depthbuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, 1024, 768);

        
        //config the framebuffer
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthbuffer);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, fbTex0, 0);
        glDrawBuffers(GL_COLOR_ATTACHMENT0);
        
        if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
			throw new RuntimeException("Failed to create framebuffer.");
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}

}

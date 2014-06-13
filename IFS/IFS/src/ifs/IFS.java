package ifs;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.*;
import org.lwjgl.util.vector.*;
import org.lwjgl.openal.AL;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.OggData;
import org.newdawn.slick.openal.OggDecoder;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import static ifs.Utility.*;

public class IFS {
	
	public static final int _width = 1024;
	public static final int _height = 768;
	static final float[] _clearcolour = {0.94f,0.94f,0.94f,1f};
	static final byte[] _linecolour = {(byte) 0xD0, (byte) 0xD0, (byte) 0xD0};
	public static final int _tile_diagonal_size = 48;//if this changes, remember to change it in the vertex scene shader
	public static final int _grid_size = 15;
	
	//public static final Vector2f grid_top_left = new Vector2f(_width/2,100);
	
	int mousex = 0;
	int mousey = 0;
	
	int mousedx = 0;
	int mousedy = 0;
	
	int mousedw = 0; //delta wheel
	
	public static int zoomLevel = 1;
	public static float zoomScale = 1;
	float[] zoomScales = {0.5f,1f,2f};
	
	public static Vector2f cameraPos = new Vector2f(0,0);//new Vector2f(_width/2,150);
	
	public static GameGrid currentGrid;
	
	//timing shit
	volatile long frameStart;
	volatile long frameEnd;
	public static double deltaTime = 0f;
	
	//sync shit
	public static volatile boolean loaded = false;
	public static volatile boolean closing = false;
	
	//font
	public static TrueTypeFont font;
	
	//debug shit
	List<EntityCharacter> characters = new ArrayList<EntityCharacter>();
	
	//shader shit
	//int shaderv;
	//int shaderf;
	//int program;
	
	//music shit
	public DJ djThread;
	public static volatile List<DJMusicPoint> triggeredMusicPoints = new ArrayList<DJMusicPoint>();
	
	//init game here.
	void start() throws Exception
	{
		try
		{
			Display.setDisplayMode(new org.lwjgl.opengl.DisplayMode(_width,_height));
			Display.setTitle("Interactive Festival Simulator 2013");
			Display.create();
			AL.create();
			
			//debug shit:
			System.out.println("GL_MAX_TEXTURE_SIZE: " + glGetInteger(GL_MAX_TEXTURE_SIZE));
			System.out.println("GL_MAX_ARRAY_TEXTURE_LAYERS: " + glGetInteger(GL_MAX_ARRAY_TEXTURE_LAYERS));
			System.out.println("GL_VERSION: " + glGetString(GL_VERSION));
			
			//SETUP GL SHIT HERE
	
			glClearColor(_clearcolour[0], _clearcolour[1],_clearcolour[2],_clearcolour[3]);//set clear colour
			glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);//specify how to blend shit
			glEnable(GL_BLEND);//turn blending on
			//glEnable(GL_TEXTURE_2D);//turn on 2d textures for drawing stuff
	
			//DRAW LOADING MESSAGE AS SOON AS GL SHIT IS SETUP
			
			//TODO: draw loading message here
			glClear(GL_COLOR_BUFFER_BIT);
			Display.update();
			
			//LOAD TEXTURES AND SHIT HERE;
			GraphicsResources.init();
			
			//LOAD UP MUSIC AND MUSIC POINTS
			djThread = new DJ();
			djThread.thisThread = new Thread(djThread);
			djThread.thisThread.setDaemon(true);
			//dj thread setup:
			djThread.playlist.add("/res/m1");
			djThread.playlist.add("/res/m2");
			
			djThread.thisThread.start();
			
			//SET UP GAME SHIT HERE
			
			currentGrid = new GameGrid(16,16,10);
			
			//E
			currentGrid.addBlock(0, 0, 0, new Block(0));
			currentGrid.addBlock(0, 0, 1, new Block(0));
			currentGrid.addBlock(0, 0, 2, new Block(0));
			currentGrid.addBlock(0, 0, 3, new Block(0));
			currentGrid.addBlock(0, 0, 4, new Block(0));
			currentGrid.addBlock(1, 0, 4, new Block(0));
			currentGrid.addBlock(2, 0, 4, new Block(0));
			currentGrid.addBlock(1, 0, 2, new Block(0));
			currentGrid.addBlock(2, 0, 2, new Block(0));
			currentGrid.addBlock(1, 0, 0, new Block(0));
			currentGrid.addBlock(2, 0, 0, new Block(0));
			
			//S
			currentGrid.addBlock(5, 0, 4, new Block(0));
			currentGrid.addBlock(6, 0, 4, new Block(0));
			currentGrid.addBlock(4, 0, 3, new Block(0));
			currentGrid.addBlock(5, 0, 2, new Block(0));
			currentGrid.addBlock(6, 0, 1, new Block(0));
			currentGrid.addBlock(4, 0, 0, new Block(0));
			currentGrid.addBlock(5, 0, 0, new Block(0));
			
			//H
			currentGrid.addBlock(8, 0, 0, new Block(0));
			currentGrid.addBlock(8, 0, 1, new Block(0));
			currentGrid.addBlock(8, 0, 2, new Block(0));
			currentGrid.addBlock(8, 0, 3, new Block(0));
			currentGrid.addBlock(8, 0, 4, new Block(0));
			currentGrid.addBlock(10, 0, 0, new Block(0));
			currentGrid.addBlock(10, 0, 1, new Block(0));
			currentGrid.addBlock(10, 0, 2, new Block(0));
			currentGrid.addBlock(10, 0, 3, new Block(0));
			currentGrid.addBlock(10, 0, 4, new Block(0));
			currentGrid.addBlock(9, 0, 2, new Block(0));
			
			/*for (int i = 0; i < 15; i++)
			{
				if( i < 6 || i > 7)
					currentGrid.addBlock(i, 0, 0, new Block(textures.get(1)));
			}
			//*/
			
			//characters.add(new EntityCharacter(15,15,TextureHandler.getTexture("lad1")));
			//((EntityCharacter) characters.get(0)).WalkTo(new Vector2f(7,-1));
			
			Random r = new Random();
			for (int i = 0; i < 100; i++)
			{
				characters.add(new EntityCharacter(r.nextFloat() * 15,r.nextFloat()* 13 + 2,TextureHandler.getTexture("lad1")));
			}//*/
	        
	        loaded = true;
	        
	        //wait for dj thread if its behind this one
			while(true)// wait till main thread is loaded if its behind
			{
				if(djThread.loaded) break;// leave this loop
				
				glClear(GL_COLOR_BUFFER_BIT);
				Display.update();
				//draw another (animated possibly?) loading screen here
			}

	        frameStart = System.nanoTime();//start recording time from here
			while (!Display.isCloseRequested())
			{
				//timing shit
				frameEnd = System.nanoTime();
				deltaTime = (frameEnd - frameStart)/1000000000f;
				frameStart = frameEnd;
				
				//print fps
				//System.out.println((int)(1.0f/deltaTime));
				
				input();
				update();
				draw();
				
				//debug shit
				//System.out.println(alGetSourcef(source, AL_SAMPLE_OFFSET));
				
				//input apparently goes here for some reason?? it seems stupid because then it'll be a frame behind so i moved it
				
				Display.update();
				Display.sync(60);
			}
		}
		finally
		{
			//TODO make this code request an exit on the DJ thread, then wait till it actually does exit
			// this should prevent it throwing an exception every time the app is closed
			
			closing = true;// notify other threads to finish.
			
			//here we wait for other threads to have finished before continuing (this way there's no exceptions thrown when the game exits)
			if(djThread != null) {while(djThread.thisThread.isAlive()) Thread.sleep(1);}
			
			Display.destroy();
			AL.destroy();
		}
	}
	
	//shitty polling method variables
	boolean leftMouseLast = false;
	boolean leftMouseClick = false;
	
	boolean rightMouseUpClick = false;
	boolean rightMouseLast = false;
	boolean rightMouseNotMoved = false;
	void input()
	{
		//TODO: fix this shit up so that all mouse events occur within the loop below.
		while(Mouse.next())
		{
			mousex = Mouse.getEventX();
			mousey = _height - Mouse.getEventY() - 1;
		}
		mousedx = Mouse.getDX();
		mousedy = Mouse.getDY();
		mousedw = Mouse.getDWheel();
		/*
		mousex =  Mouse.getX();
		mousey = _height - Mouse.getY() - 1; // dunno why you need to fix it up like that (its a known issue)
		//*/
		
		//shitty polling method variables
		boolean leftmousedown = Mouse.isButtonDown(0);
		if(!leftMouseLast && leftmousedown) leftMouseClick = true;
		else leftMouseClick = false;
		leftMouseLast = leftmousedown;
		
		
		//'complex' algorithm to only trigger right mouse clicks if the mouse does not drag, should work
		boolean rightmousedown = Mouse.isButtonDown(1);
		
		if(mousedx != 0 || mousedy != 0)//if the mouse moves
			rightMouseNotMoved = false;
		
		if(rightmousedown && !rightMouseLast)//if mouse just went down
			rightMouseNotMoved = true;
		else if (!rightmousedown && rightMouseLast && rightMouseNotMoved)// if mouse just went up and didn't move
		{
			rightMouseUpClick = true;
			rightMouseNotMoved = false;
		}
		else
			rightMouseUpClick = false;

		rightMouseLast = rightmousedown;
		
	}
	
	
	void update()
	{
		if(Mouse.isButtonDown(1))
		{
			cameraPos.x -= mousedx/zoomScale;
			cameraPos.y -= mousedy/zoomScale;
		}
		
		//zooming code here
		int wheelMotion = mousedw/120;
		if(wheelMotion != 0)// if mouse input
		{
			int lastZoom = zoomLevel;
			
			if(wheelMotion > 0) wheelMotion = 1;
			else wheelMotion = -1;
			//the above code clams the zoom level so it only changes by one
			//this is because the following code fails if the zoom level skips a step
			//possibly something to fix in the future
			
			zoomLevel += wheelMotion;
			if(zoomLevel > 2) zoomLevel = 2;//clamp zoom level to 0 -> 2
			else if(zoomLevel < 0) zoomLevel = 0;
			
			if(lastZoom != zoomLevel)
			{
				zoomScale = zoomScales[zoomLevel];//set the zoom scale
			}
		}
		
		if(rightMouseUpClick)
		{//remove block
			Vector2f vec = screenToGrid(new Vector2f(mousex,mousey));
			
			currentGrid.removeBlock((int)Math.floor(vec.x), (int)Math.floor(vec.y), 0);
		}
		if(leftMouseClick)
		{
			Vector2f vec = screenToGrid(new Vector2f(mousex,mousey));
			
			currentGrid.addBlock((int)Math.floor(vec.x), (int)Math.floor(vec.y), 0,  new Block(0));
		}
		//System.out.println(zoomLevel);
		
		//	 characters.get(0).WalkTo(screenToGrid(new Vector2f(mousex,mousey)));
		
		//Check triggered music points
		//run trigger
		
		Iterator<DJMusicPoint> it = triggeredMusicPoints.iterator();
		while (it.hasNext()) {
		   DJMusicPoint mp = it.next(); // must be called before you can call i.remove()
			switch (mp.type)
			{
			case 0://stop hakk
				for(int i = 0; i < characters.size(); i++)
					characters.get(i).UpdateHakk(false, 1);
				break;
			case 1://start hakk
				for(int i = 0; i < characters.size(); i++)
					characters.get(i).UpdateHakk(true, mp.bpm);
				break;
			}
			it.remove();//handled so remove it
		}

		//update characters
		for(int i = 0; i < characters.size(); i++)
		{
			characters.get(i).Update();
		}
		//Collections.sort(entities,Entity.depthComp); //debug
	}
	
	//String dbgstr = "";
	void draw()
	{
		/*TODO: implement this drawing system (for use with deferred lighting)
		 * 1. draw diffuse texture
		 * 2. draw normal texture (possibly in same pass as 1)
		 * 3. draw instance id texture (possibly in same pass as 1)
		 * --now using different geometry (cube for blocks, vertical planes for characters)--
		 * 4. draw a position texture, only drawing when instance id for current object in fragment matches the value in the instance id texture
		 * --now using different geometry (cube for blocks, cylinders for characters)--
		 * 5. draw shadow maps for lights
		 * 6. final pass, calculate lighting
		*/
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);//clear screen
		
		glBindFramebuffer(GL_FRAMEBUFFER, GraphicsResources.framebuffer);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);//clear framebuffer
		GraphicsResources.sceneShader.use(zoomScale,cameraPos);
		
		glEnable(GL_DEPTH_TEST);		

		GraphicsResources.blockPainter.begin();
		currentGrid.drawMap();
		GraphicsResources.blockPainter.end(TextureHandler.getTexture("tiles"));

		GraphicsResources.characterPainter.begin();
		for(int i = 0; i < characters.size(); i++)
		{
			characters.get(i).Draw();
		}
		//GraphicsResources.characterPainter.draw(null, new Vector3f( ), 1, 1);
		GraphicsResources.characterPainter.end(TextureHandler.getTexture("lad1"));
		
		glDisable(GL_DEPTH_TEST);

		GraphicsResources.sceneShader.unuse();
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
		GraphicsResources.textureShader.use();
		glBindTexture(GL_TEXTURE_2D, GraphicsResources.fbTex0);
		glUniform2f(GraphicsResources.textureShader.texPosLoc,0f,0f);
		glUniform2f(GraphicsResources.textureShader.texSizeLoc,IFS._width,IFS._height);
		glDrawArrays(GL_TRIANGLE_STRIP,0,4);
		GraphicsResources.textureShader.unuse();
	}
}

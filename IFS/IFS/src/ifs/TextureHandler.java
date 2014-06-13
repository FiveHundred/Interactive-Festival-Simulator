package ifs;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//import org.newdawn.slick.opengl.Texture;
//import org.newdawn.slick.opengl.TextureLoader;

public class TextureHandler {
	
	static Map<String, Texture> textureMap = new HashMap<String, Texture>();
	
	static String[][] toLoad = new String[][]{
			//{"tile","/res/tile.png"},
			{"lad1","/res/p1.png"},
			{"tiles","/res/tiles.png"},
			{"speakerIcon","/res/i1.png"}
	};
	
	public static void loadAll() throws IOException
	{
		for(int i = 0; i < toLoad.length; i++)
		{
			Texture t = new Texture(IFS.class.getResourceAsStream(toLoad[i][1]));//TextureLoader.getTexture("PNG", IFS.class.getResourceAsStream(toLoad[i][1]));
			t.bind();//make it not use bilinear filtering to draw it zoomed (looks too blurry)
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			
			textureMap.put(toLoad[i][0], t);
		}
	}
	
	public static Texture getTexture(String s)
	{
		return textureMap.get(s);
	}

}

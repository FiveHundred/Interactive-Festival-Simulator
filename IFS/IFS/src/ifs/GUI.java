package ifs;

import static ifs.Utility.*;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

public class GUI {
	
	
	public static void draw()
	{
		Texture speaker = TextureHandler.getTexture("speakerIcon");
		
		//drawSprite(new Vector2f(IFS._width - 20 - speaker.getImageWidth(),IFS._height - 20 - speaker.getImageHeight()), speaker, Color.white);
		
		String s = "Interactive Festival Simulator 2013";
		IFS.font.drawString(IFS._width/2-IFS.font.getWidth(s)/2 + 2,2, s,Color.black);
		IFS.font.drawString(IFS._width/2-IFS.font.getWidth(s)/2,0, s,Color.lightGray);
	}
}

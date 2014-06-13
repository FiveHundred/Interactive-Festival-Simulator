package ifs;

import static ifs.Utility.gridToView;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class Utility {
	
	public static Vector2f gridToView(Vector3f v)//converts grid coords to model view coords
	
	{
		Vector2f output = new Vector2f();//IFS.grid_top_left); // top 'left' of isometric grid on screen, must be a deep copy
		//move to x coordinate
		output.x += v.x * IFS._tile_diagonal_size / 2;
		output.y += v.x * IFS._tile_diagonal_size / 4;
		
		//move to y coordinate
		output.x -= v.y * IFS._tile_diagonal_size / 2;
		output.y += v.y * IFS._tile_diagonal_size / 4;
		
		//move to z coordinate
		output.y -= v.z * IFS._tile_diagonal_size / 2;
		
		return output;
	}
	public static Vector2f screenToGrid(Vector2f v)//converts screen coords to 2d grid coords (note down the screen is y+ in this method)
	{	
		//System.out.println("lel: " + IFS.cameraPos.x + ", " + IFS.cameraPos.y);
		
		//position from 0,0 in screen coords.
		Vector2f relativeV = new Vector2f((v.x - 0.5f*IFS._width)/IFS.zoomScale + IFS.cameraPos.x,(v.y - 0.5f*IFS._height)/IFS.zoomScale - IFS.cameraPos.y); //new Vector2f(v.x - IFS.cameraPos.x*IFS.zoomScale,v.y - IFS.cameraPos.y*IFS.zoomScale);
		
		
		float xplusy = relativeV.y / (IFS._tile_diagonal_size) * 4;
		float xminusy = relativeV.x / (IFS._tile_diagonal_size) * 2;
		
		float x = (xplusy + xminusy)/2;
		return new Vector2f(x, xplusy - x);
	}
	public static void drawSprite(Vector2f v,Texture t, Color c)//draws a sprite (only works for sprites with no depth)
	{	
		t.bind();
		glColor3f(c.r,c.g,c.b);
		
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0); //top left
		glVertex2f(v.x, v.y);
		
		glTexCoord2f(0, t.getHeight()); //bottom left
		glVertex2f(v.x, v.y + t.getImageHeight());
		
		glTexCoord2f(t.getWidth(), t.getHeight()); //bottom right
		glVertex2f(v.x + t.getImageWidth(), v.y  + t.getImageHeight());
		
		glTexCoord2f(t.getWidth(), 0);//top right
		glVertex2f(v.x + t.getImageWidth(), v.y);
		glEnd();
	}
	public static String loadTextFile(String path) throws IOException
	{
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IFS.class.getResourceAsStream(path)));
		StringBuilder inputStringBuilder = new StringBuilder();
        
		String line = bufferedReader.readLine();
        while(line != null){
            inputStringBuilder.append(line);inputStringBuilder.append("\r\n");
            line = bufferedReader.readLine();
        }
		return inputStringBuilder.toString();
	}
	public static Boolean IsPowerOfTwo(int x)
	{return (x != 0) && ((x & (x - 1)) == 0);}
	
	public static <T extends Comparable<T>> T clamp(T x, T min, T max)
	{
		if(x.compareTo(max) == 1) return max;//if(x>max)
		else if (x.compareTo(min) == -1) return min;//if(x<min)
		return x;
	}
}

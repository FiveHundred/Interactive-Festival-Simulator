package ifs;

import static ifs.IFS._height;
import static ifs.Utility.gridToView;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class Block extends Entity{

	int framex;
	int framey;
	
	int x;
	int y;
	int z;
	
	byte[] colours = new byte[4];
	
	public Block(int type)//type is equivalent to frame now
	{	
		// only one block type now
		framex = 1;
		framey = 0;
	}
	public void setXYZ(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		position = new Vector2f(x,y);
		this.calcDepth(new Vector3f(x,y+1,z));//depth measured from bottom left sides
		//this.depth = 1-gridToView(new Vector3f(x,y+1,0)).y/_height;
	}
	public void Draw()
	{
											// y value is plus one since origin for drawing is at the left corner of the block
		GraphicsResources.blockPainter.draw(new Vector3f(x,y+1,z), framex, framey,colours);
	}
}

package ifs;

import static ifs.IFS._height;
import static ifs.Utility.gridToView;

import java.util.Comparator;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Entity {
	
	static Comparator<Entity> depthComp = new Comparator<Entity>() //used so this class can be sorted by Arrays.sort
			{
				public int compare(Entity e1, Entity e2)
				{//greatest depth is last
					if(e1.depth > e2.depth) return +1;
					if(e1.depth < e2.depth) return -1;
					return 0;
				}
			};
	
	Vector2f position;
	public float depth;
	
	void calcDepth(Vector3f point)
	{
		//for each x value the depth gets smaller by width/4 * root(3)
		//for each y value the depth gets smaller by width/4 * root(3)
		//for each z value the depth gets smaller by (width/4)/ root (3)
		depth = 1;
		
		depth -= (point.x + point.y) * (IFS._tile_diagonal_size/4) * 1.73205080757 + point.z * (IFS._tile_diagonal_size/4)/1.73205080757;
		depth = (float) (2*Math.atan(depth)/Math.PI);//non linearly map depth for all values to -1 -> +1
		
		//gridToView(new Vector3f(point.x,point.y,0)).y
		//this.depth = 1-gridToView(new Vector3f(point.x,point.y,0)).y/_height; //smaller is closer
		//this.depth -= 1.0f/(float)_height/10f * point.z;//(1/_height/10)*point.z;
	}
	
	public void Update()
	{
	}
	public void Draw()
	{
	}
}
//		this.depth = gridToView(new Vector2f(position.x,position.y)).y/_height;
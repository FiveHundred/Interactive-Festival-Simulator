package ifs;

public class GameGrid {
	
	private Block[][][] blocks;
	int xsize;
	int ysize;
	int zsize;
	
	public GameGrid(int x, int y, int z)
	{
		xsize = x;
		ysize = y;
		zsize = z;
		
		blocks = new Block[x][y][z];
	}
	
	public void addBlock(int x, int y, int z, Block block)
	{
		if(x < xsize && x >= 0)
			if(y < ysize && y >= 0)
				if(z < ysize && z >= 0)
				{
					block.setXYZ(x, y, z);
					blocks[x][y][z] = block;
				}
	}
	public void removeBlock(int x, int y, int z)
	{
		if(x < xsize && x >= 0)
			if(y < ysize && y >= 0)
				if(z < ysize && z >= 0)
					blocks[x][y][z] = null;
	}
	
	public void drawMap()
	{//this method will have thousands of iterations
		// TODO make a better one 
		
		for(int ix = 0; ix < xsize; ix ++)
			for(int iy = 0; iy < ysize; iy ++)
				for(int iz = 0; iz < zsize; iz ++)
					if(blocks[ix][iy][iz] != null) blocks[ix][iy][iz].Draw();
	}

}

package shooter_v0;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import shooter_v0.objects.Model;
import shooter_v0.objects.Obj;

public class Map implements Serializable{
	public static final int BLOCK_COUNT = 50;
	public static final int MAP_SECTOR_SIZE = 5;
	public static final int MAP_SIZE = 200;
	public static final int RAND_BLOCK_SIZE = 5;
	public static final int BLOCK_LEVEL = 10;
	public static final int MIN_BLOCK_SIZE = 1;
	
	public ArrayList<Obj> blocks=new ArrayList<Obj>();
	private Engine parentEngine;
	

	public void generate()
	{
		Random rand=new Random();
		int x;
		int y;
		int width;
		int height;

		for (int blockIndex=0; blockIndex<Map.BLOCK_COUNT; blockIndex++)
		{
			if (blockIndex<4)//края карты
			{
				x=(int) (Map.MAP_SIZE/2*Math.cos(Math.PI/2*blockIndex));
				y=(int) (Map.MAP_SIZE/2*Math.sin(Math.PI/2*blockIndex));
				width= blockIndex%2*Map.MAP_SIZE+Map.MAP_SECTOR_SIZE;
				height= (blockIndex+1)%2*Map.MAP_SIZE+Map.MAP_SECTOR_SIZE;
			}
			else
			{
			x=rand.nextInt(Map.MAP_SIZE/Map.MAP_SECTOR_SIZE)*Map.MAP_SECTOR_SIZE-Map.MAP_SIZE/2;
			y=rand.nextInt(Map.MAP_SIZE/Map.MAP_SECTOR_SIZE)*Map.MAP_SECTOR_SIZE-Map.MAP_SIZE/2;
			width=(rand.nextInt(Map.RAND_BLOCK_SIZE)+Map.MIN_BLOCK_SIZE)*Map.MAP_SECTOR_SIZE;
			height=(rand.nextInt(Map.RAND_BLOCK_SIZE)+Map.MIN_BLOCK_SIZE)*Map.MAP_SECTOR_SIZE;
		    
			}
			Obj objBuf=new Obj(x,y,0,width,height,Map.BLOCK_LEVEL);
		    this.blocks.add(objBuf);
		    
		}
		Obj objBuf = new Obj();
		objBuf.type=Obj.CUBE;
		objBuf.modelName="squere";
		objBuf.scaleX=MAP_SIZE;
		objBuf.scaleY=MAP_SIZE;
		objBuf.scaleZ=1;
	    this.blocks.add(objBuf);
	}


	public void parse(HashMap<String, Model> models) {
		for (int i=0; i<blocks.size(); i++)
		{
			blocks.set(i,Obj.parseObj(blocks.get(i), models));
		}
	}
	

	

	
	
	
	

}

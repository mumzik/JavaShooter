package shooter_v0;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import org.eclipse.swt.graphics.Color;

import shooter_v0.objects.fisCube;
import shooter_v0.objects.Point3d;
import shooter_v0.objects.Polygon;

public class Map implements Serializable{
	public static final int BLOCK_COUNT = 20;
	public static final int MAP_SECTOR_SIZE = 10;
	public static final int MAP_SIZE = 200;
	public static final int RAND_BLOCK_SIZE = 3;
	public static final int BLOCK_LEVEL = 10;
	public static final int MIN_BLOCK_SIZE = 1;
	
	public ArrayList<fisCube> blocks=new ArrayList<fisCube>();
	
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
			fisCube objBuf=new fisCube(x,y,0,width,height,Map.BLOCK_LEVEL);
		    this.blocks.add(objBuf);
		    
		}
		fisCube objBuf = new fisCube();
		objBuf.modelName="squere";
		objBuf.scaleX=MAP_SIZE;
		objBuf.scaleY=MAP_SIZE;
		objBuf.scaleZ=1;
	    this.blocks.add(objBuf);
	}
	
	boolean crossBlockPoint(fisCube block, double x, double y)
	{
		boolean b;
		b=(x<block.x+block.width/2);
		b=(x>block.x-block.width/2)&&b;
		b=(y<block.y+block.height/2)&&b;
		b=(y>block.y-block.height/2)&&b;
		return b;
	}
	
	public boolean crossPoint(double x, double y)
	{
		for (int i=0; i<blocks.size(); i++)
		{
			if (crossBlockPoint(blocks.get(i), x, y))
				return true;
		}
		return false;
	}
	
	double dist(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
	}
	
	private boolean crossBlockCircle(fisCube block, double x, double y, double r)
	{
		boolean inOutCross=false;
		boolean b=false;
		if ((x<(block.x+block.width/2))&&(x>block.x-block.width/2)) //в полосе по х
		{
			inOutCross=true;
			if ((y-r<block.y+block.height/2)&&(y+r>block.y-block.height/2))
			{
				return true;
			}
		}
		if ((y<(block.y+block.height/2))&&(y>block.y-block.height/2))//в полосе по y
		{
			inOutCross=true;
			if ((x-r<block.x+block.width/2)&&(x+r>block.x-block.width/2))
			{
				return true;
			}
		}
		if (!inOutCross)//круг находится с углов
		{
			b=b||(dist(x, y, block.x-block.width, block.y-block.height)<r);
			b=b||(dist(x, y, block.x+block.width, block.y-block.height)<r);
			b=b||(dist(x, y, block.x+block.width, block.y+block.height)<r);
			b=b||(dist(x, y, block.x-block.width, block.y+block.height)<r);
			return b;
		}
		return false;
	}
	
	public boolean crossCircle(double x, double y,double r)
	{
		for (int i=0; i<blocks.size(); i++)
		{
			if (crossBlockCircle(blocks.get(i), x, y, r))
				return true;
		}
		return false;
	}
}

package shooter_v0.objects;

import java.util.ArrayList;

public class Map {
	public static final int BLOCK_COUNT = 280;
	public static final int MAP_SECTOR_SIZE = 1;
	public static final int MAP_SIZE = 200;
	public static final int RAND_BLOCK_SIZE = 1;
	public static final int BLOCK_LEVEL = 20;
	public static final int MIN_BLOCK_SIZE = 1;
	
	public ArrayList<Obj> blocks=new ArrayList<Obj>();
	
	boolean crossBlockPoint(Obj block, double x, double y)
	{
		boolean b;
		b=(x<block.x+block.width);
		b=(x>block.x)&&b;
		b=(y<block.y+block.height)&&b;
		b=(y>block.y)&&b;
		return b;
	}
	
	boolean crossPoint(double x, double y)
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
	
	boolean crossBlockCircle(Obj block, double x, double y, int r)
	{
		boolean inOutCross=false;
		boolean b=false;
		if ((x<(block.x+block.width))&&(x>block.x)) //в полосе по х
		{
			inOutCross=true;
			if ((y-r<block.y+block.height)&&(y+r>block.y))
			{
				//System.out.println("x line");
				return true;
			}
		}
		if ((y<(block.y+block.height))&&(y>block.y))//в полосе по y
		{
			inOutCross=true;
			if ((x-r<block.x+block.width)&&(x+r>block.x))
			{
				//System.out.println("y line");
				return true;
			}
		}
		//System.out.println("px: "+(int) center.x+"   py: "+(int) center.y+"   bx: "+(int) block.x+"   b.y: "+(int) block.y+"   b.w: "+block.width+"   b.h: "+block.height);
		
		if (!inOutCross)//круг находится с углов
		{
			b=b||(dist(x, y, block.x, block.y)<r);
			b=b||(dist(x, y, block.x+block.width, block.y)<r);
			b=b||(dist(x, y, block.x, block.y+block.height)<r);
			b=b||(dist(x, y, block.x+block.width, block.y+block.height)<r);
			return b;
		}
		return false;
	}
	
	boolean crossCircle(double x, double y,int r)
	{
		for (int i=0; i<blocks.size(); i++)
		{
			if (crossBlockCircle(blocks.get(i), x, y, r))
				return true;
		}
		return false;
	}
}

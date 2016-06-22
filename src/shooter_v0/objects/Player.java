package shooter_v0.objects;

import java.io.Serializable;

import org.eclipse.swt.graphics.Color;

public class Player extends Obj implements Serializable {
	public static final int PLAYER_SIZE=2;
	
	protected float x=0;
	protected float y=-5;
	protected double orientation=0;
	int hp;
	public String name;
	
	public Player()
	{
		this.color=new Color(null,255,00,0);
		Polygon plgbuf=new Polygon();
		plgbuf.v.add(new Point3d(-1,-1,100));
		plgbuf.v.add(new Point3d(1,-1,100));
		plgbuf.v.add(new Point3d(1,1,0));
		plgbuf.v.add(new Point3d(-1,1,0));
		plgbuf.norm=new Point3d(0,0,-1);
		this.polygons.add(plgbuf);
	}
	
	public String getName()
	{
		return name;
	};
	public void setName(String newName)
	{
		name=newName;
	};
	
	public void setX(int x)
	{
		this.x=x;
	};
	public float getX()
	{
		return x;
	};
	
	public void setY(int y)
	{
		this.y=y;
	};
	public float getY()
	{
		return y;
	};
	
	public void setHP(int hp)
	{
		this.hp=hp;
	};
	public int getHP()
	{
		return hp;
	};
	
	public void setOrientation(double angle)
	{
		this.orientation=angle;
	};
	public double getOrientation()
	{
		return orientation;
	};
	
}

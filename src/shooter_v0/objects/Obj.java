package shooter_v0.objects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import shooter_v0.World;
import shooter_v0.helper_parent.DebugClass;

public class Obj extends DebugClass implements Cloneable,Serializable{
	public double x;
	public double y;
	public double z;
	public double scaleX=1;
	public double scaleY=1;
	public double scaleZ=1;
	public Model model;
	public String modelName;
	public double orientation;
	public double width;
	public double height;
	public String type;
	public double r;
	public int id;
	public static final String CUBE="CUBE";
	public static final String SPHERE="SPHERE";
	private static final double PLAYER_CAMERA_HEIGHT =7.5;
	private static final double CAMERA_DIST = 10;
	private static final double CAMERA_DIST_ACCURCY = 0.1;
	public void bulletCollision() 
	{
	}
	
	public Obj(double x, double y, double z, double width, double height, double top)
	{
		this.x=x;
		this.y=y;
		this.z=z;
		this.width=width;
		this.height=height;
		scaleX=width;
		scaleY=height;
		scaleZ=top;
		this.type=CUBE;
		this.modelName="cube";
		Random rand=new Random();
		id=rand.nextInt();
	}
	
	public Obj(double x, double y, double z, double r)
	{
		this.x=x;
		this.y=y;
		this.z=z;
		this.r=r;
		this.type=SPHERE;
		Random rand=new Random();
		id=rand.nextInt();
	}
	
	public Obj()
	{
		Random rand=new Random();
		id=rand.nextInt();
	}
	
	public boolean collision(Obj obj)
	{
		if ((this.type.equals(CUBE))&&(obj.type.equals(SPHERE)))
		{
			return crossCubeSphere(this, obj.x, obj.y,obj.r);
		}
		if ((this.type.equals(SPHERE))&&(obj.type.equals(SPHERE)))
		{
			return (dist(this.x,this.y,this.z,obj.x,obj.y,obj.z)<(this.r+obj.r));
		}
		if ((this.type.equals(SPHERE))&&(obj.type.equals(CUBE)))
		{
			return crossCubeSphere(obj, this.x, this.y,this.r);
		}
		System.out.println("no type");
		return true;
	}
	
	public Camera getCamera(World world) {	
		Camera camera=new Camera();
		double dist=Player.PLAYER_SIZE;
		camera.z=PLAYER_CAMERA_HEIGHT;
		camera.orientation=orientation;
		do
		{
		camera.x=x-dist*Math.sin(orientation/180*Math.PI);
		camera.y=y-dist*Math.cos(orientation/180*Math.PI);		
		dist+=CAMERA_DIST_ACCURCY;
		}while ((!world.isCollide(camera))&&(dist<CAMERA_DIST));
		return  camera;
	}
	
	private static double dist(double x1, double y1, double z1, double x2, double y2, double z2) {
		return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2)+(z1-z2)*(z1-z2));
	}

	public static double dist(double x1, double y1, double x2, double y2)
	{
		return dist(x1,y1,0,x2,y2,0);
	}
	
	private boolean crossCubePoint(Obj cube, double x, double y)
	{
		boolean b;
		b=(x<cube.x+cube.width/2);
		b=(x>cube.x-cube.width/2)&&b;
		b=(y<cube.y+cube.height/2)&&b;
		b=(y>cube.y-cube.height/2)&&b;
		return b;
	}
	
	private boolean crossCubeSphere(Obj cube, double x, double y, double r)
	{
		boolean inOutCross=false;
		boolean b=false;
		if ((x<(cube.x+cube.width/2))&&(x>cube.x-cube.width/2)) //в полосе по х
		{
			inOutCross=true;
			if ((y-r<cube.y+cube.height/2)&&(y+r>cube.y-cube.height/2))
			{
				return true;
			}
		}
		if ((y<(cube.y+cube.height/2))&&(y>cube.y-cube.height/2))//в полосе по y
		{
			inOutCross=true;
			if ((x-r<cube.x+cube.width/2)&&(x+r>cube.x-cube.width/2))
			{
				return true;
			}
		}
		if (!inOutCross)//круг находится с углов
		{
			b=b||(dist(x, y, cube.x-cube.width/2, cube.y-cube.height/2)<r);
			b=b||(dist(x, y, cube.x+cube.width/2, cube.y-cube.height/2)<r);
			b=b||(dist(x, y, cube.x+cube.width/2, cube.y+cube.height/2)<r);
			b=b||(dist(x, y, cube.x-cube.width/2, cube.y+cube.height/2)<r);
			return b;
		}
		return false;
	}
	
	public static Obj parseObj(Obj arg, HashMap<String, Model> models) {
		Obj buf;
		try {
			if (arg!=null)
			{	
				buf = (Obj) arg.clone();
				buf.model=models.get(arg.modelName);
			return buf;
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}

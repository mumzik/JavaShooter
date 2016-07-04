package shooter_v0.objects;

import java.io.Serializable;

import org.eclipse.swt.graphics.Color;


public class Player extends Model {
	public static final double PLAYER_SIZE=2.5;


	private static final double PLAYER_CAMERA_HEIGHT = 8;


	private static final double CAMERA_DIST = 10;

	int hp;
	public String login;
	
	public Player()
	{
		this.color=new Color(null,255,00,0);
	}
	
	public String getName()
	{
		return login;
	};
	public void setName(String newName)
	{
		login=newName;
	};
	
	public void setX(int x)
	{
		this.pos.x=x;
	};
	public double getX()
	{
		return pos.x;
	};
	
	public void setY(int y)
	{
		this.pos.y=y;
	};
	public double getY()
	{
		return pos.y;
	};
	
	public void setHP(int hp)
	{
		this.hp=hp;
	};
	public int getHP()
	{
		return hp;
	};
	
	public Camera getCamera() {	
		Point3d bufPoint=new Point3d();
		bufPoint.x=pos.x-CAMERA_DIST*Math.sin(orientation/180*Math.PI);
		bufPoint.y=pos.y-CAMERA_DIST*Math.cos(orientation/180*Math.PI);
		bufPoint.z=PLAYER_CAMERA_HEIGHT;
		return new Camera(bufPoint,orientation);
	}
	
}

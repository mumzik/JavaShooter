package shooter_v0.objects;

import java.awt.Color;
import java.io.Serializable;

import shooter_v0.Engine;



public class Player extends Obj implements Serializable,Cloneable{
	public static final double PLAYER_SIZE=2.5;
	private static final double PLAYER_CAMERA_HEIGHT = 8;
	private static final double CAMERA_DIST = 10;

	public int hp;
	public String login;
	
	public Player()
	{
		modelName="player";
	}
	public String getName()
	{
		return login;
	};
	public void setName(String newName)
	{
		login=newName;
	};
	public Camera getCamera() {	
		Point3d bufPoint=new Point3d();
		bufPoint.x=x-CAMERA_DIST*Math.sin(orientation/180*Math.PI);
		bufPoint.y=y-CAMERA_DIST*Math.cos(orientation/180*Math.PI);
		bufPoint.z=PLAYER_CAMERA_HEIGHT;
		return new Camera(bufPoint,orientation);
	}
	
}

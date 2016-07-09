package shooter_v0.objects;

import java.io.Serializable;

import shooter_v0.Engine;
import shooter_v0.Map;

public class Bullet extends Obj implements Serializable{
	public static final double MAX_SPEED = 5;
	public Point3d speed;
	public boolean isDestoyed(Map map){
				if (!map.crossPoint(x,y))
				{
					x+=speed.x;
					y+=speed.y;
					return false;
				}
					return true;
	}
	
	public Bullet(Point3d pos, Point3d speed)
	{
		this.speed=speed;
		x=pos.x;
		y=pos.y;
		z=pos.z;
		modelName="bullet";
	}
}

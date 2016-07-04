package shooter_v0.objects;

import org.eclipse.swt.graphics.Color;

import shooter_v0.Map;

public class Bullet extends Model{
	public static final double MAX_SPEED = 5;
	public Point3d speed;
	public boolean isDestoyed(Map map){
				if (!map.crossPoint(pos.x,pos.y))
				{
					pos.x+=speed.x;
					pos.y+=speed.y;
					return false;
				}
					return true;
	}
	
	public Bullet(Point3d pos, Point3d speed)
	{
		this.speed=speed;
		this.pos=pos;
		this.color=new Color(null,250,200,100);
		this.name="bullet";
		loadModel("models/bullet.obj");
	}
}

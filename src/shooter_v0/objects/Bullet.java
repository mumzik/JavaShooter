package shooter_v0.objects;

import org.eclipse.swt.graphics.Color;

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
		Polygon plgbuf=new Polygon();
		plgbuf.v.add(new Point3d(-1,-1,0));
		plgbuf.v.add(new Point3d(1,-1,0));
		plgbuf.v.add(new Point3d(1,1,0));
		plgbuf.v.add(new Point3d(-1,1,0));
		plgbuf.norm=new Point3d(0,0,-1);
		this.polygons.add(plgbuf);
	}
}

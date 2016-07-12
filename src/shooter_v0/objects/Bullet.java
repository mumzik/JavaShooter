package shooter_v0.objects;

import java.io.Serializable;
import java.util.ArrayList;

import shooter_v0.Engine;
import shooter_v0.Map;
import shooter_v0.World;

public class Bullet extends Obj implements Serializable, Cloneable {
	public static final double MAX_SPEED = 10;
	private static final double BULLET_DESTROY_ACCURACY = 0.1;
	private static final double SIZE=0.5;
	public Point3d speed;

	public boolean isDestoyed(World world) {
			for (double i = 0; i < MAX_SPEED; i += BULLET_DESTROY_ACCURACY) {
				Obj onFire=world.collisionWhith(this);
				if (onFire!=null)
				{
					onFire.bulletCollision();
					return true;
				}

			}
			x+=speed.x;
			y+=speed.y;
			return false;
	}

	public Bullet clone() {
		try {
			return (Bullet) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			System.out.println("error cloning bullet");
		}
		return null;
	}

	public Bullet(Point3d pos, Point3d speed) {
		this.speed = speed;
		x = pos.x;
		y = pos.y;
		z = pos.z;
		r=SIZE;
		modelName = "bullet";
		type=SPHERE;
	}
}

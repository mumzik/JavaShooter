package shooter_v0.objects;

public class Camera extends Point3d{
	public double orientation;
	public Camera(double x, double y, double z, double orientation)
	{
		this.x=x;
		this.y=y;
		this.z=z;
		this.orientation=orientation;
	}
	public Camera(Point3d bufPoint, double orientation) {
		this.x=bufPoint.x;
		this.y=bufPoint.y;
		this.z=bufPoint.z;
		this.orientation=orientation;
	}
}

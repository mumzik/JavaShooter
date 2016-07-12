package shooter_v0.objects;

public class Camera extends Obj{
	public static final double SIZE = 1.5;
	public double orientation;
	public Camera(double x, double y, double z, double orientation)
	{
		super(x,y,z,SIZE);
		this.orientation=orientation;
	}
	public Camera() {
		this.type=SPHERE;
		this.r=SIZE;
	}
}

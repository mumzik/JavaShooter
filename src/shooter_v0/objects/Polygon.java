package shooter_v0.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class Polygon implements Serializable{
	public ArrayList<Point3d> v = new ArrayList<Point3d>();
	public Point3d norm=new Point3d(0,0,0); 
}

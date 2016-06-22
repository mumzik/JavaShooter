package shooter_v0.objects;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;

abstract public class Model {
	public List<Polygon> polygons =new ArrayList<Polygon>();	
	public Color color;
	public Point3d pos=new Point3d(0,0,0);
	public String name="default";
}

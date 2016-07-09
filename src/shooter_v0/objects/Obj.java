package shooter_v0.objects;

import java.io.Serializable;
import java.util.HashMap;

public class Obj implements Cloneable,Serializable{
	public double x;
	public double y;
	public double z;
	public double scaleX=1;
	public double scaleY=1;
	public double scaleZ=1;
	public Model model;
	public String modelName;
	public double orientation;
	public static Obj parseObj(Obj arg, HashMap<String, Model> models) {
		Obj buf;
		try {
			buf = (Obj) arg.clone();
			buf.model=models.get(arg.modelName);
			return buf;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}

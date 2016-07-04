package shooter_v0.objects;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;

abstract public class Model {
	public List<Polygon> polygons = new ArrayList<Polygon>();
	public Color color;
	public Point3d pos = new Point3d(0, 0, 0);
	protected double orientation;
	public String name = "default";

	public void loadModel(String path) {
		name = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
		System.out.println("model " + name + " loading");
		ArrayList<Point3d> normals = new ArrayList<Point3d>();
		ArrayList<Point3d> vertexes = new ArrayList<Point3d>();
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(path));
			String buf;
			Point3d bufPoint = new Point3d();
			while ((buf = readLine(fileReader)) != null) // нормали
			{

				if (buf.indexOf("v ") == 0) // строка с вершиной
				{
					vertexes.add(readPoint(buf));
					continue;
				}
				if (buf.indexOf("vn ") == 0) // строка с нормалью
				{
					normals.add(readPoint(buf));
					continue;
				}
				if (buf.indexOf("f ") == 0) // строка с полигоном
				{
					polygons.add(createPolygon(buf, vertexes, normals));
					continue;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("файл модели \"" + path + "\" не найден");
			e.printStackTrace();
		}
	}

	private Polygon createPolygon(String objPolygon, ArrayList<Point3d> vertexes, ArrayList<Point3d> normals) {
		String buf;
		Polygon bufPolygon = new Polygon();
		boolean b=true;
		while (b) {
			objPolygon = objPolygon.substring(objPolygon.indexOf(" ") + 1, objPolygon.length());
			if (objPolygon.indexOf(" ")!=-1)
			buf = objPolygon.substring(0, objPolygon.indexOf(" "));
			else
			{
			buf = objPolygon.substring(0, objPolygon.length());
			b=false;
			}
			bufPolygon.norm = normals.get(Integer.parseInt(buf.substring(buf.indexOf("//") + 2, buf.length()))-1);
			bufPolygon.v.add(vertexes.get(Integer.parseInt(buf.substring(0, buf.indexOf("//")))-1));
		}
		return bufPolygon;
	}

	private Point3d readPoint(String buf) {
		Point3d bufPoint = new Point3d();
		buf = buf.substring(buf.indexOf(" ") + 1, buf.length());
		bufPoint.x = Double.parseDouble(buf.substring(0, buf.indexOf(" ")));
		buf = buf.substring(buf.indexOf(" ") + 1, buf.length());
		bufPoint.z = Double.parseDouble(buf.substring(0, buf.indexOf(" ")));
		buf = buf.substring(buf.indexOf(" ") + 1, buf.length());
		bufPoint.y = Double.parseDouble(buf.substring(0, buf.length()));
		return bufPoint;
	}

	private String readLine(BufferedReader fileReader) {
		try {
			return fileReader.readLine();
		} catch (IOException e) {
			System.out.println("ошибка чтения файла модели \"" + name + "\"");
			e.printStackTrace();
		}
		return null;
	}
	public void setOrientation(double angle)
	{
		this.orientation=angle;
	};
	public double getOrientation()
	{
		return orientation;
	}

}

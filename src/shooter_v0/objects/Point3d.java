package shooter_v0.objects;

import java.io.Serializable;

public class Point3d implements Serializable{
		public double x;
		public double y;
		public double z;
		
		public Point3d(double x, double y, double z){
			this.x=x;
			this.y=y;
			this.z=z;
		}

		public Point3d() {
		}
}

package shooter_v0.objects;

import java.io.Serializable;

public class fisCube extends Obj implements Serializable{
	public int width;
	public int height;
	
	public fisCube(int x, int y, int z, int width, int height, int top)
	{
		this.x=x;
		this.y=y;
		this.z=z;
		this.width=width;
		this.height=height;
		scaleX=width;
		scaleY=height;
		scaleZ=top;
		this.modelName="cube";
	}

	public fisCube() {
		this.x=0;
		this.y=0;
		this.width=0;
		this.height=0;
	}
}

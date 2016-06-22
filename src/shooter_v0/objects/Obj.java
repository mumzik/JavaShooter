package shooter_v0.objects;

public class Obj extends Model{
	public int x;
	public int y;
	public int width;
	public int height;
	
	public Obj(int x, int y, int width, int height)
	{
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}

	public Obj() {
		this.x=0;
		this.y=0;
		this.width=0;
		this.height=0;
	}
}

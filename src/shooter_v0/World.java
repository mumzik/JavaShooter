package shooter_v0;

import java.util.ArrayList;

import shooter_v0.objects.Obj;

public class World extends ArrayList<Obj> {

	public World() {
	}
	public boolean isCollide(Obj obj)
	{
		for (int i=0; i<this.size(); i++)
		{
			if (obj.id==this.get(i).id)
				continue;
			if (obj.collision(this.get(i)))
			{
				return true;
			}
		}
		return false;	
	}
	
	public Obj collisionWhith(Obj obj)
	{
		for (int i=0; i<this.size(); i++)
		{
			if (obj.id==this.get(i).id)
				continue;
			if (obj.collision(this.get(i)))
				return this.get(i);
		}
		return null;	
	}

}

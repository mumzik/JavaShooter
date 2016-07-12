package shooter_v0.objects;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

import shooter_v0.Engine;
import shooter_v0.Map;
import shooter_v0.World;



public class Player extends Obj implements Serializable,Cloneable{
	public static final double PLAYER_SIZE=2.5;

	public int hp;
	public String login;
	
	public Player()
	{	
		type=SPHERE;
		r=PLAYER_SIZE;
		modelName="player";
	}
	public String getName()
	{
		return login;
	};
	public void setName(String newName)
	{
		login=newName;
	};
	
}

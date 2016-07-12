package shooter_v0.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import shooter_v0.Engine;
import shooter_v0.Map;
import shooter_v0.World;


public class Actor extends Player implements Serializable,Cloneable {
	private static final double MOUSE_SENSIVITY = 0.2;
	private static final double MOVE_SPEED = 1.2;
	private static final int MAX_SLIDE_ANGLE = 90;//максимальный угол в градусах относительно плоскости стены когда игрок, упершись в нее движется вдоль
	private static final double STEP_ACCURACY = 0.1;
	private static final double BULLET_DIST_KOEF = 2;

	public Actor()
	{
		super();
		modelName="actor";
	}
	public Actor clone()
	{
		try {
			return (Actor) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			System.out.println("error cloning actor");
		}
		return null;
		
	}
	private void tryMove(double or, World world)
	{
		double pathDist=0;
		while (pathDist<MOVE_SPEED)
		{
			pathDist+=STEP_ACCURACY;
			int angle=0;
			boolean moved=false;
			while (Math.abs(angle)<MAX_SLIDE_ANGLE*2){
				double speedKoef=Math.cos(Math.PI/180*angle/2);//множитель замедляющий движение вдоль стены
				speedKoef*=speedKoef;
				double redirectAngle=Math.PI/180*(angle/2)*(1 - (angle%2)*2);//корректирующий угод движения вдоль стены
				double stepx=-speedKoef*Math.sin(or+ redirectAngle)*STEP_ACCURACY;
				double stepy=-speedKoef*Math.cos(or+redirectAngle)*STEP_ACCURACY;
				x+=stepx;
				y+=stepy;
				if (!world.isCollide(this))
				{
					moved=true;
					break;//куда то прошел
				}
				else
				{
					x-=stepx;
					y-=stepy;
				}
				angle++;
			}
			if (!moved) //некуда идти
				break;
		}
	}
	public void rotate(double value)
	{
		orientation=(orientation+value*MOUSE_SENSIVITY)%360;
		//System.out.println("orientation:  "+orientation);
	}
	public void create(World world)
	{
		Random rand=new Random();
		do
		{
			x=rand.nextInt((int) (Map.MAP_SIZE-PLAYER_SIZE*2)-1)-Map.MAP_SIZE/2;
			y=rand.nextInt((int) (Map.MAP_SIZE-PLAYER_SIZE*2)-1)-Map.MAP_SIZE/2;
		} while (world.isCollide(this));
	}
	public boolean move(Boolean[] movePad, World world) {
		double ornt = orientation/180*Math.PI;
		for (int i=0; i<4; i++){
			if (movePad[i]){
				tryMove(ornt-i*Math.PI/2,world);
				return true;
			}
		}
		return false;
	}
	public Bullet fire()
	{
		Point3d speed=new Point3d(Bullet.MAX_SPEED*Math.sin(orientation*Math.PI/180),Bullet.MAX_SPEED*Math.cos(orientation*Math.PI/180),0);
		double bulletX=x+(PLAYER_SIZE+MOVE_SPEED+BULLET_DIST_KOEF)*Math.sin(orientation*Math.PI/180);
		double bulletY=y+(PLAYER_SIZE+MOVE_SPEED+BULLET_DIST_KOEF)*Math.cos(orientation*Math.PI/180);
		Point3d posBuf=new Point3d(bulletX,bulletY,PLAYER_SIZE*2/3);
		Bullet bullet=new Bullet(posBuf,speed);
		return bullet;
	}

}

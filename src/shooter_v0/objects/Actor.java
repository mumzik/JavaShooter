package shooter_v0.objects;

import java.util.Random;


public class Actor extends Player {
	private static final double MOUSE_SENSIVITY = 0.2;
	private static final double MOVE_SPEED = 1.2;
	private static final int MAX_SLIDE_ANGLE = 90;//максимальный угол в градусах относительно плоскости стены когда игрок, упершись в нее движется вдоль
	private static final double STEP_ACCURACY = 0.1;

	Point2d tryMove(double or, Map map)
	{
		Point2d buf=new Point2d(x,y);
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
				if (!map.crossCircle(buf.x+stepx, buf.y+stepy,PLAYER_SIZE))
				{
					buf.x+=stepx;
					buf.y+=stepy;
					moved=true;
					break;//куда то прошел
				}
				angle++;
			}
			if (!moved) //некуда идти
				break;
		}
		return buf;
	}
	
	public void rotate(double value)
	{
		orientation=(orientation+value*MOUSE_SENSIVITY)%360;
		//System.out.println("orientation:  "+orientation);
	}

	public void create(Map map)
	{
		System.out.println("actor location");
		Random rand=new Random();
		do
		{
			x=rand.nextInt(Map.MAP_SIZE-PLAYER_SIZE*2)-Map.MAP_SIZE/2;
			y=rand.nextInt(Map.MAP_SIZE-PLAYER_SIZE*2)-Map.MAP_SIZE/2;
		} while (map.crossCircle(x, y, PLAYER_SIZE));
	}
	
	public boolean move(Boolean[] movePad, Map map) {
		double ornt = -orientation/180*Math.PI;
		//System.out.println("x: "+x+"   y: "+y);
		for (int i=0; i<4; i++){
			if (movePad[i]){
				Point2d step=tryMove(ornt-i*Math.PI/2,map);
				//System.out.println(x-step.x);
				x=(float) step.x;
				y=(float) step.y;
				return true;
			}
		}
		return false;
	}

	public Bullet fire()
	{
		Point3d speed=new Point3d(-Bullet.MAX_SPEED*Math.sin(-orientation*Math.PI/180),-Bullet.MAX_SPEED*Math.cos(-orientation*Math.PI/180),0);
		Point3d pos=new Point3d(x,y,PLAYER_SIZE/3);
		Bullet bullet=new Bullet(pos,speed);
		return bullet;
	}
}

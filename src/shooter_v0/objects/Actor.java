package shooter_v0.objects;

import java.util.Random;

import org.eclipse.swt.graphics.Color;

import shooter_v0.Map;


public class Actor extends Player {
	private static final double MOUSE_SENSIVITY = 0.2;
	private static final double MOVE_SPEED = 1.2;
	private static final int MAX_SLIDE_ANGLE = 90;//������������ ���� � �������� ������������ ��������� ����� ����� �����, �������� � ��� �������� �����
	private static final double STEP_ACCURACY = 0.1;

	public Actor()
	{
		color=new Color(null,0,0,255);
	}
	
	Point2d tryMove(double or, Map map)
	{
		Point2d buf=new Point2d(pos.x,pos.y);
		double pathDist=0;
		while (pathDist<MOVE_SPEED)
		{
			pathDist+=STEP_ACCURACY;
			int angle=0;
			boolean moved=false;
			while (Math.abs(angle)<MAX_SLIDE_ANGLE*2){
				double speedKoef=Math.cos(Math.PI/180*angle/2);//��������� ����������� �������� ����� �����
				speedKoef*=speedKoef;
				double redirectAngle=Math.PI/180*(angle/2)*(1 - (angle%2)*2);//�������������� ���� �������� ����� �����
				double stepx=-speedKoef*Math.sin(or+ redirectAngle)*STEP_ACCURACY;
				double stepy=-speedKoef*Math.cos(or+redirectAngle)*STEP_ACCURACY;
				if (!map.crossCircle(buf.x+stepx, buf.y+stepy,PLAYER_SIZE))
				{
					buf.x+=stepx;
					buf.y+=stepy;
					moved=true;
					break;//���� �� ������
				}
				angle++;
			}
			if (!moved) //������ ����
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
		loadModel("models/player.obj");
		Random rand=new Random();
		do
		{
			pos.x=rand.nextInt((int) (Map.MAP_SIZE-PLAYER_SIZE*2)-1)-Map.MAP_SIZE/2;
			pos.y=rand.nextInt((int) (Map.MAP_SIZE-PLAYER_SIZE*2)-1)-Map.MAP_SIZE/2;
		} while (map.crossCircle(pos.x, pos.y, PLAYER_SIZE));
	}
	
	public boolean move(Boolean[] movePad, Map map) {
		double ornt = orientation/180*Math.PI;
		//System.out.println("x: "+x+"   y: "+y);
		for (int i=0; i<4; i++){
			if (movePad[i]){
				Point2d step=tryMove(ornt-i*Math.PI/2,map);
				//System.out.println(x-step.x);
				pos.x= step.x;
				pos.y= step.y;
				return true;
			}
		}
		return false;
	}

	public Bullet fire()
	{
		Point3d speed=new Point3d(Bullet.MAX_SPEED*Math.sin(orientation*Math.PI/180),Bullet.MAX_SPEED*Math.cos(orientation*Math.PI/180),0);
		Point3d posBuf=new Point3d(pos.x,pos.y,PLAYER_SIZE*2/3);
		Bullet bullet=new Bullet(posBuf,speed);
		return bullet;
	}

}

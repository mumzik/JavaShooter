package shooter_v0;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import shooter_v0.helper_parent.Room;
import shooter_v0.objects.Actor;
import shooter_v0.objects.Bullet;
import shooter_v0.objects.Camera;
import shooter_v0.objects.Model;
import shooter_v0.objects.Player;
import shooter_v0.objects.Point3d;
import shooter_v0.objects.Polygon;
import shooter_v0.objects.Obj;

class Control{	
	Boolean[] movePad;
	boolean mouseIsDown;
	public Control(){
		movePad=new Boolean[4];
		mouseIsDown=false;
		
		for (int i=0; i<4; i++){
			movePad[i]=false;
		}
	}
}

public class Game extends Room{
		
	protected static final int FORWARD = 119;
	protected static final int RIGHT = 100;
	protected static final int BACK = 115;
	protected static final int LEFT = 97;
	
	public  Control control;
	private Map map;
	public boolean time;
	
	private ArrayList<Model> world;
	private ArrayList<Player> players;
	private ArrayList<Bullet> bullets;
	private Actor actor;
	private GLDraw glDraw;
	private GLCanvas canvas;
	public String login;

	public Game(Engine parentEngine){
		super(parentEngine);
		control= new Control();
		glDraw=new GLDraw();
		map=new Map();
		actor=new Actor();
		players=new ArrayList<Player>();
		bullets = new ArrayList<Bullet>();
		world =new ArrayList<Model>();	    
	}
	//СОБЫТИЯ
	private void setListeners()
	{
		canvas.addListener(SWT.MouseMove, new Listener(){
			public void handleEvent(Event e) {
				int center=composite.getParent().getBounds().x+composite.getParent().getClientArea().width/2;
				double rotateValue=composite.getDisplay().getCursorLocation().x-center;
				actor.rotate(rotateValue);
				composite.getDisplay().setCursorLocation(center,composite.getDisplay().getCursorLocation().y);
			}			
		});		
		composite.addListener(SWT.KeyDown, new Listener(){
			public void handleEvent(Event e) {
				//System.out.println(e.keyCode);
				if (e.keyCode==SWT.ESC){
					//написать отдельгым потоком вероятно
					MessageBox exitDialog = 
							 new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
							 exitDialog.setMessage("выйти в главное меню?");
							int answer = exitDialog.open();
							if (answer==SWT.OK)
							{
								time=false;
								composite.setEnabled(false);
								composite.setVisible(false);
								glDraw.dispose();
								parentEngine.mainMenu.open();
							}
				}
				
				if (e.keyCode==BACK){
					control.movePad[0]=true;
				}
				if (e.keyCode==RIGHT){
					control.movePad[1]=true;
				}
				if (e.keyCode==FORWARD){
					control.movePad[2]=true;
				}
				if (e.keyCode==LEFT){
					control.movePad[3]=true;
				}						
			}
			
		});		
		composite.addListener(SWT.KeyUp, new Listener(){
			public void handleEvent(Event e) {
				for (int i=0; i<4; i++){
					control.movePad[i]=false;
				}
			}
		});
		canvas.addListener(SWT.MouseDown, new Listener(){
			public void handleEvent(Event arg0) {
				control.mouseIsDown=true;
			}			
		});
		canvas.addListener(SWT.MouseUp,new Listener(){
			public void handleEvent(Event arg0) {
				control.mouseIsDown=false;				
			}
			
		});
	}
	//ТАЙМЕР
	private void inTime(){
		Camera cam=actor.getCamera();	
		world.remove(actor);
		actor.move(control.movePad, map);
		world.add(actor);
		glDraw.draw(world, cam );
		if (control.mouseIsDown){
			Bullet newBullet=actor.fire();
			bullets.add(newBullet);
			world.add(newBullet);
		}
		for (int i=0; i<bullets.size(); i++)
		{
			if (bullets.get(i).isDestoyed(map))
			{
				world.remove(bullets.get(i));
				bullets.remove(i);
				
			}
		}
	}
	//ГЕНЕРАЦИЯ КАРТЫ
	private ArrayList<Obj> mapGen()
	{
		Random rand=new Random();
		int x;
		int y;
		int width;
		int height;
		ArrayList<Obj> newMap=new ArrayList<Obj>();
		
		for (int blockIndex=0; blockIndex<Map.BLOCK_COUNT; blockIndex++)
		{
			if (blockIndex<4)//края карты
			{
				x=Map.MAP_SIZE/2;
				y=-x;
				if (blockIndex==0) x=-x;
				if (blockIndex==1) x=-x;
				if (blockIndex==2) {x=-x; y=-y;};
				width= (blockIndex+1)%2*Map.MAP_SIZE+Map.MAP_SECTOR_SIZE;
				height= blockIndex%2*Map.MAP_SIZE+Map.MAP_SECTOR_SIZE;
				//System.out.println("bx: "+(int)x+"   b.y: "+(int)y+"   b.w: "+width+"   b.h: "+height);
			}
			else
			{
			x=rand.nextInt(Map.MAP_SIZE/Map.MAP_SECTOR_SIZE)*Map.MAP_SECTOR_SIZE-Map.MAP_SIZE/2;
			y=rand.nextInt(Map.MAP_SIZE/Map.MAP_SECTOR_SIZE)*Map.MAP_SECTOR_SIZE-Map.MAP_SIZE/2;
			width=(rand.nextInt(Map.RAND_BLOCK_SIZE)+Map.MIN_BLOCK_SIZE)*Map.MAP_SECTOR_SIZE;
			height=(rand.nextInt(Map.RAND_BLOCK_SIZE)+Map.MIN_BLOCK_SIZE)*Map.MAP_SECTOR_SIZE;
		    
			}
			Obj objBuf=new Obj(x,y,width,height);
			objBuf.color=new Color(null,170,100,100);
		    Polygon plgBuf=new Polygon();
		    plgBuf.v.add(new Point3d(x,y,0));
		    plgBuf.v.add(new Point3d(x+width,y,0));
		    plgBuf.v.add(new Point3d(x+width,y,Map.BLOCK_LEVEL));
		    plgBuf.v.add(new Point3d(x,y,Map.BLOCK_LEVEL));
		    plgBuf.norm=new Point3d(0,-1,0);
		    objBuf.polygons.add(plgBuf);
		    
		    plgBuf=new Polygon();
		    plgBuf.v.add(new Point3d(x,y+height,0));
		    plgBuf.v.add(new Point3d(x+width,y+height,0));
		    plgBuf.v.add(new Point3d(x+width,y+height,Map.BLOCK_LEVEL));
		    plgBuf.v.add(new Point3d(x,y+height,Map.BLOCK_LEVEL));
		    plgBuf.norm=new Point3d(0,-1,0);
		    objBuf.polygons.add(plgBuf);
		    
		    plgBuf=new Polygon();
		    plgBuf.v.add(new Point3d(x+width,y,0));
		    plgBuf.v.add(new Point3d(x+width,y+height,0));
		    plgBuf.v.add(new Point3d(x+width,y+height,Map.BLOCK_LEVEL));
		    plgBuf.v.add(new Point3d(x+width,y,Map.BLOCK_LEVEL));
		    plgBuf.norm=new Point3d(1,0,0);
		    objBuf.polygons.add(plgBuf);
		    
		    plgBuf=new Polygon();
		    plgBuf.v.add(new Point3d(x,y,0));
		    plgBuf.v.add(new Point3d(x,y+height,0));
		    plgBuf.v.add(new Point3d(x,y+height,Map.BLOCK_LEVEL));
		    plgBuf.v.add(new Point3d(x,y,Map.BLOCK_LEVEL));
		    plgBuf.norm=new Point3d(1,0,0);
		    objBuf.polygons.add(plgBuf); 	    
		    newMap.add(objBuf);
		}
		
		Obj objBuf=new Obj();
		Polygon plgBuf=new Polygon();
		plgBuf.norm=new Point3d(0,0,-1);
	    objBuf.polygons.add(plgBuf);
	    objBuf.color=new Color(null,50,125,50);
	    plgBuf.v.add(new Point3d(-500,-500,0));
	    plgBuf.v.add(new Point3d(500,-500,0));
	    plgBuf.v.add(new Point3d(500,500,0));
	    plgBuf.v.add(new Point3d(-500,500,0));	    
	    newMap.add(objBuf);
	    
		return newMap;		
	}
	//НОВАЯ ИГРА
	public void newGame(){
		open();
	    map.blocks=mapGen();
		players.clear();
		bullets.clear();	
		actor.create(map);
		players.add(actor);
		world.addAll(map.blocks);
		canvas=glDraw.init(composite, players.get(0));
		time=true;
		setListeners();
	    Runnable timer = new Runnable() {
	        public void run() {
	        	if (time){
	        	inTime();	
	        	composite.getDisplay().timerExec(40, this);
	        	}
	        }
	      };
	      composite.getDisplay().timerExec(0, timer);
		
	}
}

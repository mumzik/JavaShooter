package shooter_v0;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import shooter_v0.helper_parent.DebugClass;
import shooter_v0.helper_parent.NetInteraction;
import shooter_v0.helper_parent.Room;
import shooter_v0.objects.Actor;
import shooter_v0.objects.Bullet;
import shooter_v0.objects.Camera;
import shooter_v0.objects.Model;
import shooter_v0.objects.Obj;
import shooter_v0.objects.Player;

class Control {
	Boolean[] movePad;
	boolean mouseIsDown;

	public Control() {
		movePad = new Boolean[4];
		mouseIsDown = false;

		for (int i = 0; i < 4; i++) {
			movePad[i] = false;
		}
	}
}

public class Game extends DebugClass {

	private static final int FORWARD = 119;
	private static final int RIGHT = 100;
	private static final int BACK = 115;
	private static final int LEFT = 97;
	private static final int FPS = 50;
	public Map map = new Map();
	public World world;
	private Actor actor;
	public ArrayList<Player> players = new ArrayList<Player>();
	public ArrayList<Bullet> bullets;
	private Engine parentEngine;
	private Composite openGLComposite;
	private GLDraw glDraw;
	private GLCanvas canvas;
	public Timer mainTimer;
	public Control control = new Control();
	public boolean online;
	public HashMap<String, Model> models = new HashMap<String, Model>();

	public Game(Engine parentEngine) {
		this.parentEngine = parentEngine;
		glDraw = new GLDraw(parentEngine);
		online=false;
		openGLComposite = new Composite(parentEngine.getShell(), SWT.NONE);
		openGLComposite.setSize(parentEngine.getShell().getClientArea().width,
				parentEngine.getShell().getClientArea().height);
		openGLComposite.setEnabled(false);
		openGLComposite.setVisible(false);
		bullets = new ArrayList<Bullet>();
		world = new World();
	}

	private void setListeners() {
		canvas.addListener(SWT.MouseMove, new Listener() {
			public void handleEvent(Event e) {
				int center = openGLComposite.getParent().getBounds().x
						+ openGLComposite.getParent().getClientArea().width / 2;
				double rotateValue = openGLComposite.getDisplay().getCursorLocation().x - center;
				actor.rotate(rotateValue);
				openGLComposite.getDisplay().setCursorLocation(center,
						openGLComposite.getDisplay().getCursorLocation().y);
			}
		});
		openGLComposite.addListener(SWT.KeyDown, new Listener() {
			public void handleEvent(Event e) {
				// System.out.println(e.keyCode);
				if (e.keyCode == SWT.ESC) {
					MessageBox exitDialog = new MessageBox(parentEngine.getShell(),
							SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
					exitDialog.setMessage("выйти в главное меню?");
					int answer = exitDialog.open();
					if (answer == SWT.OK) 
						exitToMainMenu();
				}

				if (e.keyCode == BACK) {
					control.movePad[0] = true;
				}
				if (e.keyCode == RIGHT) {
					control.movePad[1] = true;
				}
				if (e.keyCode == FORWARD) {
					control.movePad[2] = true;
				}
				if (e.keyCode == LEFT) {
					control.movePad[3] = true;
				}
			}

		});
		openGLComposite.addListener(SWT.KeyUp, new Listener() {
			public void handleEvent(Event e) {
				for (int i = 0; i < 4; i++) {
					control.movePad[i] = false;
				}
			}
		});
		canvas.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event arg0) {
				control.mouseIsDown = true;
			}
		});
		canvas.addListener(SWT.MouseUp, new Listener() {
			public void handleEvent(Event arg0) {
				control.mouseIsDown = false;
			}

		});
	}

	private void exitToMainMenu() {

		if (online){
			if (parentEngine.netType==NetInteraction.SERVER)
				parentEngine.server.shutdown();
			else
			{
				parentEngine.client.disconnectFromServer();
				parentEngine.client.disconnect();
			}
		}
			online=false;
			mainTimer.stop();
			exit();
			glDraw.dispose();
			parentEngine.mainMenu.open();		
	}

	private void inTime() {
		long GameTimeBuf=System.currentTimeMillis();
		world.clear();
		if (online)
		{
			onlineInTime();
		}
		else {
			world.add(actor);
		}
		
		world.addAll(map.blocks);
		world.addAll(bullets);
		world.addAll(players);
		
		actor.move(control.movePad, world);
		
		Camera cam = getCamera(world,actor);
		if (cam==null)//не успел добавить актора через сеть
			cam=actor.getCamera(world);
		long DrawTimeBuf=System.currentTimeMillis();
		glDraw.draw(world, cam);
		if (control.mouseIsDown) {
			Bullet newBullet = actor.fire();
			if (online)
			{
				parentEngine.client.sendMessage(NetInteraction.ADD_BULLET);
				parentEngine.client.sendObject(newBullet.clone());
			}
			else
			{
				bullets.add(newBullet);
			}
		}
		for (int i = 0; i < bullets.size(); i++) {
			if (bullets.get(i).isDestoyed(world)) {
				world.remove(bullets.get(i));
				bullets.remove(i);
			}
		}
		//print("game fps: "+(System.currentTimeMillis()-GameTimeBuf));
	}

	private Camera getCamera(World world, Actor actor) {
		for (int i=0; i<world.size();i++)
		{
			if (actor.id==world.get(i).id)
			{
				return world.get(i).getCamera(world);
			}
		}
		return null;
	}

	private void onlineInTime() {
		double timeBuf=System.currentTimeMillis();
		parentEngine.client.sendMessage(NetInteraction.REFRESH_ME);
		parentEngine.client.sendObject(actor.clone());
		//print("net fps: "+(System.currentTimeMillis()-timeBuf));
	}

	public void newGame(boolean isOnline) {
		print("new game");
		open();
		LoadModels();
		if (!online)
			map.generate();
		world.addAll(map.blocks);
		online = isOnline;
		bullets.clear();
		actor = new Actor();
		actor.create(world);
		print("init GL");
		canvas = glDraw.init(openGLComposite);
		setListeners();
		mainTimer = new Timer(parentEngine.getDisplay(), FPS, new Runnable() {
			public void run() {
				inTime();
			}
		});
		mainTimer.start();
	}

	private void LoadModels() {
		print("load models");
		Model modelBuf = new Model();
		modelBuf.color = new Color(null, 255, 0, 0);
		modelBuf.loadModel("player");
		models.put("player", modelBuf);
		modelBuf = new Model();
		modelBuf.color = new Color(null, 0, 0, 250);
		modelBuf.loadModel("player");
		models.put("actor", modelBuf);
		modelBuf = new Model();
		modelBuf.color = new Color(null, 250, 250, 250);
		modelBuf.loadModel("bullet");
		models.put("bullet", modelBuf);
		modelBuf = new Model();
		modelBuf.color = new Color(null, 250, 200, 100);
		modelBuf.loadModel("cube");
		models.put("cube", modelBuf);
		modelBuf = new Model();
		modelBuf.color = new Color(null, 50, 125, 50);
		modelBuf.loadModel("squere");
		models.put("squere", modelBuf);
	}

	private void exit() {
		openGLComposite.setEnabled(false);
		openGLComposite.setVisible(false);
	}

	private void open() {
		openGLComposite.setEnabled(true);
		openGLComposite.setVisible(true);
		openGLComposite.setFocus();
	}

	public void refreshPlayers(ArrayList<Player> newPlayers) {
		players.clear();
		players.addAll(newPlayers);
	}
}

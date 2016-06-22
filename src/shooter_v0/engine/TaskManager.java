package shooter_v0.engine;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import shooter_v0.engine.rooms.CreateGameMenu;
import shooter_v0.engine.rooms.JoinGameMenu;
import shooter_v0.engine.rooms.MainMenu;
import shooter_v0.engine.rooms.SelectServerMenu;
import shooter_v0.engine.rooms.SettingsMenu;
import shooter_v0.net.ServerClass;

public class TaskManager {
	public MainMenu menu;
	public Game game;
	public CreateGameMenu createGameMenu;
	private Display display;
	private Shell shell;
	public JoinGameMenu joinGame;
	public ServerClass server=null;
	public SelectServerMenu selectServer;
	public SettingsMenu settins;
	public String login="default";
	
	public Shell getShell()
	{
		return shell;
	}
	public Display getDisplay()
	{
		return display;
	}

	public void run()
	{
		  display = new Display();
	      shell = new Shell( display );
	      shell.setSize( 640, 480 );
		  shell.open();
		  menu=new MainMenu(this);
	      game=new Game(this);
	      selectServer=new SelectServerMenu(this);
	      joinGame=new JoinGameMenu(this);
	      settins=new SettingsMenu(this);
	      menu.open();
		  
	      while( !shell.isDisposed() ) {
	    	  if(!display.readAndDispatch() )
	                display.sleep();
	      }
	      display.dispose();
	}
	public ServerClass CreateServer()
	{
		return server=new ServerClass(this);
	}
}


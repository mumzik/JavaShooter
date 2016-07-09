package shooter_v0;



import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import shooter_v0.helper_parent.NetInteraction;
import shooter_v0.net.Client;
import shooter_v0.net.Server;
import shooter_v0.rooms.CreateGameMenu;
import shooter_v0.rooms.JoinGameMenu;
import shooter_v0.rooms.MainMenu;
import shooter_v0.rooms.OnlineGameMenu;
import shooter_v0.rooms.SelectServerMenu;
import shooter_v0.rooms.SettingsMenu;

public class Engine {
	
	public Game game;	
	private Display display;
	private Shell shell;	
	public Server server;
	public Client client;
	public MainMenu mainMenu;
	public OnlineGameMenu onlineGameMenu;
	public JoinGameMenu joinGameMenu;
	public CreateGameMenu createGameMenu;
	public SelectServerMenu selectServerMenu;
	public SettingsMenu settings;
	public String login="default";
	public String netType=NetInteraction.OFFLINE;

	public String getLogin() {
		return login;
	}
	public Shell getShell()
	{
		return shell;
	}
	public Display getDisplay()
	{
		return display;
	}
	public void showMessage(String text)
	{
		MessageBox informWindow = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK);
		informWindow.setMessage(text);
		informWindow.open();
	}
	public void run()
	{
		  display = new Display();
	      shell = new Shell( display );
	      shell.setSize( 640, 480 );
		  shell.open();
		  server=new Server(this);
		  client=new Client(this);
		  game=new Game(this);
		  mainMenu=new MainMenu(this);
	      settings=new SettingsMenu(this, mainMenu);
	      onlineGameMenu=new OnlineGameMenu(this, mainMenu);
	      joinGameMenu=new JoinGameMenu(this, onlineGameMenu);
	      createGameMenu=new CreateGameMenu(this, onlineGameMenu);
	      selectServerMenu=new SelectServerMenu(this, onlineGameMenu);	      
	      mainMenu.open();
		  
	      while( !shell.isDisposed() ) {
	    	  if(!display.readAndDispatch() )
	                display.sleep();
	      }
	      display.dispose();
	}
	public Server CreateServer()
	{
		return server=new Server(this);
	}
	
	public void exit() {
		display.dispose();;		
	}
	public Server getServer() {
		return server;
	}
	public Client getClient() {
		return client;
	}

	public String getNetType() {
		return netType;
	}
	
	public void setNetType(String netType) {
		this.netType=netType;		
	}

}


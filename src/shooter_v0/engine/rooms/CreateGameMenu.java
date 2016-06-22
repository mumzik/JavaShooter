package shooter_v0.engine.rooms;

import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Handler;

import javax.naming.Context;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

import shooter_v0.engine.TaskManager;
import shooter_v0.helper_parent.InsertedMenu;
import shooter_v0.net.ClientConnection;
import shooter_v0.net.ServerClass;

public class CreateGameMenu extends InsertedMenu{
	protected static final int REFRESH_TIME = 1000;
	private Button startGameButton;
	private Label serverIp;
	public ServerClass server;
	private List connectedClients;
	public Socket selfSocket;
	
	public void setListeners()
	{
		startGameButton.addListener(SWT.MouseUp, new Listener(){
			public void handleEvent(Event e) {
				composite.setEnabled(false);
				composite.setVisible(false);
				//server.isWaiting=false;
				//server.addSelf();
				parent.game.time=true;
				//server.game();
				for (int i=0; i<parent.server.clients.size(); i++)
				{
					parent.game.time=true;
					parent.game.login= server.clients.get(i).login;
					parent.server.clients.get(i).toClient.println("start");
					parent.game.newGame(server.CsocketOfServer);
					parent.game.open();
				}
				
				
				
				
								
			}			
		});
	}
	public CreateGameMenu(TaskManager parent) 
	{
		create(parent,true);
		setBackground();
		startGameButton=new Button(composite,SWT.PUSH);
		startGameButton.setText("start");
		startGameButton.setBounds(shell.getClientArea().width-COLUMN_WIDHT, shell.getClientArea().height-BUTTONS_HEIGHT, COLUMN_WIDHT, BUTTONS_HEIGHT);
		parent.server=parent.CreateServer();
		server=parent.server;
		server.waitClients();
		serverIp=new Label(composite,SWT.NONE);
		serverIp.setText(ServerClass.getLocalIp());
		serverIp.setBounds(shell.getClientArea().width/2-COLUMN_WIDHT/2, 0, COLUMN_WIDHT, BUTTONS_HEIGHT);
		connectedClients=new List(composite, SWT.NONE);
		connectedClients.setBounds(shell.getClientArea().width/2-COLUMN_WIDHT/2, BUTTONS_HEIGHT, COLUMN_WIDHT, BUTTONS_HEIGHT);
		setListeners();
		Runnable timer = new Runnable() {
	        public void run() {
	        	if (composite.isEnabled()){
	        	refresh();	
	        	composite.getDisplay().timerExec(REFRESH_TIME, this);
	        	}
	        }
	      };
	      composite.getDisplay().timerExec(0, timer);
	}

	
	public void refresh() {
		connectedClients.removeAll();
		for (int i=0; i<parent.server.clients.size(); i++)
		connectedClients.add(parent.server.clients.get(i).login+"  team: "+parent.server.clients.get(i).team);
	}


}

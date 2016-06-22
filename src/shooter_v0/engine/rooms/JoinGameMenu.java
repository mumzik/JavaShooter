package shooter_v0.engine.rooms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;

import shooter_v0.engine.TaskManager;
import shooter_v0.helper_parent.InsertedMenu;
import shooter_v0.helper_parent.Menu;
import shooter_v0.net.Client;

public class JoinGameMenu extends Menu {
	private static final int MAX_TEAM_COUNT = 5;
	public Socket socket;
	//private List connectedClients;
	private List teamSelector;
	private  Button selectConfirmButton;
	private Button backButton;
	public boolean waiting=true;
	
	
	protected void setListeners()
	{
		selectConfirmButton.addListener(SWT.MouseUp, new Listener(){
			public void handleEvent(Event e) {
				try {
					PrintWriter selectTeamWriter = new PrintWriter(socket.getOutputStream(),true);
					String[] teams=teamSelector.getSelection();
					selectTeamWriter.println(teams[0]);		
					MessageBox informWindow = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK);
					informWindow.setMessage("team selected");
					informWindow.open();
					selectConfirmButton.dispose();
				
				} catch (IOException e1) {
					System.out.println("ошибка отправки команды");
					e1.printStackTrace();
				}
			}			
		});
		
		backButton.addListener(SWT.MouseUp, new Listener(){
			public void handleEvent(Event e) {
				composite.setEnabled(false);
				composite.setVisible(false);
				try {
					PrintWriter selectTeamWriter = new PrintWriter(socket.getOutputStream(),true);
					selectTeamWriter.println(-1);		
					selectConfirmButton.dispose();
				
				} catch (IOException e1) {
					System.out.println("ошибка ухода из комнаты");
					e1.printStackTrace();
				}
				parent.menu.open();
			}			
		});
	}
	public JoinGameMenu(TaskManager parent) {
		create(parent);
		setBackground();
		teamSelector=new List(composite, SWT.NONE);
		teamSelector.setBounds(shell.getClientArea().width-COLUMN_WIDHT, BUTTONS_HEIGHT, COLUMN_WIDHT, BUTTONS_HEIGHT);
		for (int i=1; i<=MAX_TEAM_COUNT; i++)
			teamSelector.add(String.valueOf(i));
		teamSelector.setSelection(0);
		selectConfirmButton=new Button(composite,SWT.PUSH);
		selectConfirmButton.setText("select");
		selectConfirmButton.setBounds(shell.getClientArea().width-COLUMN_WIDHT, shell.getClientArea().height-BUTTONS_HEIGHT, COLUMN_WIDHT, BUTTONS_HEIGHT);
		//connectedClients=new List(composite, SWT.NONE);
		//connectedClients.setBounds(shell.getClientArea().width/2-COLUMN_WIDHT/2, BUTTONS_HEIGHT, COLUMN_WIDHT, BUTTONS_HEIGHT);
		backButton=new Button(composite,SWT.PUSH);
		backButton.setText(InsertedMenu.BACK_BUTTON_TEXT);
		backButton.setBounds(0, shell.getClientArea().height-BUTTONS_HEIGHT, COLUMN_WIDHT, BUTTONS_HEIGHT);
		setListeners();
	}

	public void connect(String serverIp) {
		Client client=new Client(this);
		socket=client.connect(parent, serverIp);
		Runnable waitgame = new Runnable() {
	        public void run() {
	        	if (waiting)
	        	{	
	        	composite.getDisplay().timerExec(40, this);
	        	}
	        	//else
	        		//if (!(socket==null))
	        			//parent.game.newGame(socket, );
	        }
	      };
	      composite.getDisplay().timerExec(40, waitgame);
	}
	

}

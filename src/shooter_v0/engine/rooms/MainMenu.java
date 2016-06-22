package shooter_v0.engine.rooms;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import shooter_v0.engine.TaskManager;
import shooter_v0.helper_parent.Menu;

public class MainMenu extends Menu {
	
	private Button createGameButton;
	private Button joinGameButton;
	private Button settingsButton;
	private Button exitButton;
	
	
	private void setListeners()
	{
		setCreateBL();
		setJoinBL();
		setExitBl();
		setSettingsBL();
	}
	private void setSettingsBL() {
		settingsButton.addListener(SWT.MouseUp, new Listener(){
			public void handleEvent(Event e) {
				composite.setEnabled(false);
				composite.setVisible(false);
				parent.settins.open();
			}			
		});
	}
	private void setJoinBL() {
		joinGameButton.addListener(SWT.MouseUp, new Listener(){
			public void handleEvent(Event e) {
				composite.setEnabled(false);
				composite.setVisible(false);
				parent.selectServer.open();
			}			
		});
	}	
	private void setCreateBL()
	{
		createGameButton.addListener(SWT.MouseUp, new Listener(){
			public void handleEvent(Event e) {
				composite.setEnabled(false);
				composite.setVisible(false);
				if (parent.createGameMenu==null)
				{
				      parent.createGameMenu=new CreateGameMenu(parent);
				}
				parent.createGameMenu.open();
			}			
		});
	}
	private void setExitBl() {
		exitButton.addListener(SWT.MouseUp, new Listener(){
			public void handleEvent(Event e) {
				if (!(parent.server==null)&&!parent.server.mainSocket.isClosed())
					try {
						parent.server.mainSocket.close();
					} catch (IOException exeption) {
						exeption.printStackTrace();
					}
				if (!parent.server.newClient.isClosed())
					try {
						parent.server.newClient.close();
					} catch (IOException exeption) {
						exeption.printStackTrace();
					}
				if (!parent.server.connectionSocket.isClosed())
					try {
						parent.server.connectionSocket.close();
					} catch (IOException exeption) {
						exeption.printStackTrace();
					}
				shell.dispose();
				display.dispose();
			}			
		});
	}
	public MainMenu(TaskManager parent)
	{
		create(parent);
		setBackground();
		createGameButton=new Button(composite,SWT.PUSH);
		createGameButton.setText("create game");
		createGameButton.setBounds(shell.getClientArea().width/2-COLUMN_WIDHT/2 , 0, COLUMN_WIDHT, BUTTONS_HEIGHT);
		joinGameButton=new Button(composite,SWT.PUSH);
		joinGameButton.setText("join game");
		joinGameButton.setBounds(shell.getClientArea().width/2-COLUMN_WIDHT/2 , BUTTONS_HEIGHT, COLUMN_WIDHT, BUTTONS_HEIGHT);
		settingsButton=new Button(composite,SWT.PUSH);
		settingsButton.setText("settings");
		settingsButton.setBounds(shell.getClientArea().width/2-COLUMN_WIDHT/2 , BUTTONS_HEIGHT*2, COLUMN_WIDHT, BUTTONS_HEIGHT);
		exitButton=new Button(composite,SWT.PUSH);
		exitButton.setText("exit");
		exitButton.setBounds(shell.getClientArea().width/2-COLUMN_WIDHT/2 , BUTTONS_HEIGHT*3, COLUMN_WIDHT, BUTTONS_HEIGHT);
		setListeners();
	}

}

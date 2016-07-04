package shooter_v0.rooms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import shooter_v0.Engine;
import shooter_v0.helper_parent.Menu;

public class MainMenu extends Menu{
	private Engine parentEngine;
	private Button onlineGameButton;
	private Button offlineGameButton;
	private Button settingsButton;
	private Button exitButton;
	private Menu self=this;
	
	public MainMenu(Engine parentEngine)
	{
		super(parentEngine);
		name="главное меню";
		this.parentEngine=parentEngine;
		int leftBorder=composite.getClientArea().width/2-COLUMN_WIDHT/2;
		onlineGameButton=new Button(composite, SWT.PUSH);
		onlineGameButton.setText("игра по сети");
		onlineGameButton.setBounds(leftBorder, 0, COLUMN_WIDHT, BUTTONS_HEIGHT);
		offlineGameButton=new Button(composite, SWT.PUSH);
		offlineGameButton.setText("одиночная игра");
		offlineGameButton.setBounds(leftBorder, BUTTONS_HEIGHT, COLUMN_WIDHT, BUTTONS_HEIGHT);
		settingsButton=new Button(composite, SWT.PUSH);
		settingsButton.setText("настройки");
		settingsButton.setBounds(leftBorder, BUTTONS_HEIGHT*2, COLUMN_WIDHT, BUTTONS_HEIGHT);
		exitButton=new Button(composite, SWT.PUSH);
		exitButton.setText("выход");
		exitButton.setBounds(leftBorder, BUTTONS_HEIGHT*3, COLUMN_WIDHT, BUTTONS_HEIGHT);
		setListeners();
	}

	private void setListeners() {
		setExitButLst();
		setSettingsButLst();
		setOffflineGameButLst();
		setOnlineGameButLst();
	}

	private void setOnlineGameButLst() {
		onlineGameButton.addListener(SWT.MouseDown, new Listener(){
			public void handleEvent(Event arg0) {
				composite.setEnabled(false);
				composite.setVisible(false);
				parentEngine.onlineGameMenu.open();
			}			
		});
	}

	private void setOffflineGameButLst() {
		offlineGameButton.addListener(SWT.MouseDown, new Listener(){
			public void handleEvent(Event arg0) {
				composite.setEnabled(false);
				composite.setVisible(false);
				parentEngine.game.newGame();
			}			
		});
	}

	private void setSettingsButLst() {
		settingsButton.addListener(SWT.MouseDown, new Listener(){
			public void handleEvent(Event arg0) {
				composite.setEnabled(false);
				composite.setVisible(false);
				parentEngine.settings.open();
			}

		
		});		
	}

	private void setExitButLst() {
		exitButton.addListener(SWT.MouseDown, new Listener(){
			public void handleEvent(Event arg0) {
				parentEngine.exit();					
			}			
		});		
	}

}

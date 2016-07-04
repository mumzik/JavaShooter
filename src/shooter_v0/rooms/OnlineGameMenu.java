package shooter_v0.rooms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import shooter_v0.Engine;
import shooter_v0.helper_parent.InsertedMenu;
import shooter_v0.helper_parent.Menu;

public class OnlineGameMenu extends InsertedMenu {

	private Button createGameButton;
	private Button joinGameButton;

	public OnlineGameMenu(Engine parentEngine, Menu backMenu) {
		super(parentEngine, backMenu);
		name="меню онлайн игры";
		int leftBorder=composite.getClientArea().width/2-COLUMN_WIDHT/2;
		createGameButton=new Button(composite, SWT.PUSH);
		createGameButton.setText("создать игру");
		createGameButton.setBounds(leftBorder, 0, COLUMN_WIDHT, BUTTONS_HEIGHT);
		joinGameButton=new Button(composite, SWT.PUSH);
		joinGameButton.setText("присоеденитьс€");
		joinGameButton.setBounds(leftBorder, BUTTONS_HEIGHT, COLUMN_WIDHT, BUTTONS_HEIGHT);
		setListeners();
	}
	private void setListeners() {
		setJoinButLst();
		setCreateButLst();
	}
	private void setJoinButLst() {
		joinGameButton.addListener(SWT.MouseDown, new Listener(){
			public void handleEvent(Event arg0) {
				composite.setEnabled(false);
				composite.setVisible(false);
				parentEngine.selectServerMenu.open();
			}			
		});
	}
	private void setCreateButLst() {
		createGameButton.addListener(SWT.MouseDown, new Listener(){
			public void handleEvent(Event arg0) {
				composite.setEnabled(false);
				composite.setVisible(false);
				parentEngine.createGameMenu.open();
			}			
		});
	}
	@Override
	protected void back() {
		// TODO Auto-generated method stub
		
	}

}

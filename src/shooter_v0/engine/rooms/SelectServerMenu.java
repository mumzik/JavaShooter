package shooter_v0.engine.rooms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import shooter_v0.engine.TaskManager;
import shooter_v0.helper_parent.InsertedMenu;

public class SelectServerMenu extends InsertedMenu{
	private Text serverIp;
	private Button joinGameButton;
	
	private void setListeners()
	{
		joinGameButton.addListener(SWT.MouseUp, new Listener(){
			public void handleEvent(Event e) {
				composite.setEnabled(false);
				composite.setVisible(false);
				parent.joinGame.connect(serverIp.getText());
				parent.joinGame.open();
			}			
		});
	}
	
	public SelectServerMenu(TaskManager parent) {
		create(parent,true);
		setBackground();
		serverIp=new Text(composite,SWT.NONE);
		serverIp.setText("192.168.1.66");
		serverIp.setBounds(shell.getClientArea().width/2-COLUMN_WIDHT/2, 0, COLUMN_WIDHT, BUTTONS_HEIGHT);
		joinGameButton=new Button(composite,SWT.PUSH);
		joinGameButton.setText("join");
		joinGameButton.setBounds(shell.getClientArea().width-COLUMN_WIDHT, shell.getClientArea().height-BUTTONS_HEIGHT, COLUMN_WIDHT, BUTTONS_HEIGHT);
		setListeners();
	}

}

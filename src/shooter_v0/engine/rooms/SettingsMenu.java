package shooter_v0.engine.rooms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import shooter_v0.engine.TaskManager;
import shooter_v0.helper_parent.InsertedMenu;

public class SettingsMenu extends InsertedMenu{

	private static final String LOCALE_LOGIN = "your login:";
	private static final String LOCALE_CONFIRM = "confirm";
	private Text login;
	private Label loginLabel;
	private Button confirmButton;
	
	private void setListeners()
	{
		confirmButton.addListener(SWT.MouseUp, new Listener(){
			public void handleEvent(Event e) {
				parent.login=login.getText();
				MessageBox informWindow = 
						 new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK);
						 informWindow.setMessage("configuration saved");
						 informWindow.open();
			}			
		});
	}
	public SettingsMenu(TaskManager parent) {
		create(parent,true);
		setBackground();
		login=new Text(composite,SWT.NONE);
		login.setText("default");
		login.setBounds(shell.getClientArea().width/2, 0, COLUMN_WIDHT, BUTTONS_HEIGHT);
		loginLabel=new Label(composite,SWT.NONE);
		loginLabel.setText(LOCALE_LOGIN);
		loginLabel.setBounds(shell.getClientArea().width/2-COLUMN_WIDHT, 0, COLUMN_WIDHT, BUTTONS_HEIGHT);
		confirmButton=new Button(composite,SWT.PUSH);
		confirmButton.setText(LOCALE_CONFIRM);
		confirmButton.setBounds(shell.getClientArea().width-COLUMN_WIDHT, shell.getClientArea().height-BUTTONS_HEIGHT, COLUMN_WIDHT, BUTTONS_HEIGHT);
		setListeners();
	}
	
}

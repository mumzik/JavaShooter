package shooter_v0.helper_parent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import shooter_v0.engine.TaskManager;

abstract public class InsertedMenu extends Menu {
	public static final String BACK_BUTTON_TEXT = "в главное меню";
	protected Button backButton;
	
	protected void setListener()
	{
		backButton.addListener(SWT.MouseUp, new Listener(){
			public void handleEvent(Event e) {
				composite.setEnabled(false);
				composite.setVisible(false);
				parent.menu.open();
			}			
		});
	}
	
	 public void create(TaskManager parent, boolean isInserted)
	{
		create(parent);
		backButton=new Button(composite,SWT.PUSH);
		backButton.setText(BACK_BUTTON_TEXT);
		backButton.setBounds(0, shell.getClientArea().height-BUTTONS_HEIGHT, COLUMN_WIDHT, BUTTONS_HEIGHT);
		setListener();
	}
}

package shooter_v0.helper_parent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import shooter_v0.Engine;

abstract public class InsertedMenu extends Menu {
	public InsertedMenu(Engine parentEngine, Menu backMenu) {
		super(parentEngine);
		this.backMenu=backMenu;
		backButton=new Button(composite,SWT.PUSH);
		backButton.setText("â "+backMenu.name);
		backButton.setBounds(0, shell.getClientArea().height-BUTTONS_HEIGHT, COLUMN_WIDHT, BUTTONS_HEIGHT);
		setListener();
	}

	protected Button backButton;
	protected Menu backMenu;
	protected abstract void back();
	
	protected void setListener()
	{
		backButton.addListener(SWT.MouseUp, new Listener(){
			public void handleEvent(Event e) {
				composite.setEnabled(false);
				composite.setVisible(false);
				back();
				backMenu.open();
			}			
		});
	}
	
}

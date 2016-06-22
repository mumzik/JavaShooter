package shooter_v0.helper_parent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import shooter_v0.engine.TaskManager;

public abstract class Menu extends Room {
	public static final int COLUMN_WIDHT = 200;
	public static final int BUTTONS_HEIGHT = 50;
	public static final Color MENU_COLOR = new Color(null,100,100,100);
	protected void setBackground(){
		composite.setBackground(MENU_COLOR);
	}
	}

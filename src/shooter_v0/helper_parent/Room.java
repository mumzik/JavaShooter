package shooter_v0.helper_parent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import shooter_v0.engine.TaskManager;

abstract public class Room {
	protected Shell shell;
	protected Display display;
	protected Composite composite;
	protected TaskManager parent;
	
	public void create(TaskManager parent)
	{
		this.parent=parent;
		this.shell=parent.getShell();
		this.display=parent.getDisplay();
		composite=new Composite(shell,SWT.NONE);
		composite.setSize(shell.getClientArea().width , shell.getClientArea().height);
		composite.setEnabled(false);
		composite.setVisible(false);
	}
	public void open()
	{
		composite.setEnabled(true);
		composite.setVisible(true);
		composite.setFocus();
	}
}

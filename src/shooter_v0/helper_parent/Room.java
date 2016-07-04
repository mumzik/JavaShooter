package shooter_v0.helper_parent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import shooter_v0.Engine;

abstract public class Room {
	protected Shell shell;
	protected Composite composite;
	protected Engine parentEngine;
	public String name="default";
	
	public Room(Engine parentEngine)
	{
		this.parentEngine=parentEngine;
		this.shell=this.parentEngine.getShell();
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

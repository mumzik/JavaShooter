package shooter_v0;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import shooter_v0.net.Client;
import shooter_v0.net.Server;

public interface EngineInterface {
	
	public Display getDisplay();
	public Shell getShell();
	public String getNetType();
	public void setNetType(String netType);
	public Server getServer();
	public Client getClient();
	public void showMessage(String text);
}

package shooter_v0.rooms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

import shooter_v0.Engine;
import shooter_v0.Map;
import shooter_v0.helper_parent.InsertedMenu;
import shooter_v0.helper_parent.Menu;
import shooter_v0.helper_parent.NetInteraction;

public class CreateGameMenu extends Menu{

	private Label IpLabel;
	private Button backButton;
	private Button startGameButton;

	public CreateGameMenu(Engine parentEngine, Menu backMenu) {
		super(parentEngine);
		name="создание игры";
		int leftBorder=composite.getClientArea().width/2-COLUMN_WIDHT/2;
		IpLabel=new Label(composite, SWT.PUSH);
		IpLabel.setBounds(leftBorder, 0, COLUMN_WIDHT, BUTTONS_HEIGHT);
		backButton=new Button(composite,SWT.PUSH);
		backButton.setText("выключить сервер и вернуться в меню онлайн игры");
		backButton.setBounds(0, shell.getClientArea().height-BUTTONS_HEIGHT, COLUMN_WIDHT*2, BUTTONS_HEIGHT);
		startGameButton=new Button(composite,SWT.PUSH);
		startGameButton.setText("начать игру");
		startGameButton.setBounds(shell.getClientArea().width-COLUMN_WIDHT, shell.getClientArea().height-BUTTONS_HEIGHT, COLUMN_WIDHT, BUTTONS_HEIGHT);
		setListener();
	}
	
	protected void setListener()
	{
		backButton.addListener(SWT.MouseUp, new Listener(){
			public void handleEvent(Event e) {
				composite.setEnabled(false);
				composite.setVisible(false);
				parentEngine.server.shutdown();
				parentEngine.onlineGameMenu.open();
			}			
		});
		startGameButton.addListener(SWT.MouseUp, new Listener(){
			public void handleEvent(Event e) {
				Map mapBuf =new Map();
				mapBuf.generate();
				parentEngine.server.sendToAll(NetInteraction.START_GAME);
				parentEngine.server.sendObjectToAll(mapBuf);
			}			
		});
	}
	public void open(){
		parentEngine.setNetType(NetInteraction.SERVER);
		parentEngine.server.run();
		String ip=parentEngine.server.localIp.toString();
		ip=ip.substring(1, ip.length());
		IpLabel.setText(ip);
		composite.setEnabled(true);
		composite.setVisible(true);
		composite.setFocus();
	}

}

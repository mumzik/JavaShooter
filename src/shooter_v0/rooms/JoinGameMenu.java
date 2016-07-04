package shooter_v0.rooms;

import shooter_v0.Engine;
import shooter_v0.helper_parent.InsertedMenu;
import shooter_v0.helper_parent.Menu;

public class JoinGameMenu extends InsertedMenu{

	public JoinGameMenu(Engine parentEngine, Menu backMenu) {
		super(parentEngine, backMenu);
		name="присоединения к игре";
	}

	protected void back() {
		parentEngine.client.disconnectFromServer();
		parentEngine.client.disconnect();		
	}

}

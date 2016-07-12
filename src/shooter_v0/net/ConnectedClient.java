package shooter_v0.net;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import shooter_v0.Engine;
import shooter_v0.helper_parent.NetInteraction;
import shooter_v0.objects.Actor;
import shooter_v0.objects.Bullet;
import shooter_v0.objects.Model;
import shooter_v0.objects.Player;

public class ConnectedClient extends NetInteraction {
	Player playerBuf;
	public String login;

	public ConnectedClient(Engine parentEngine, Socket socket, Socket objSocket) {
		localNetType = "server";
		this.parentEngine = parentEngine;
		this.socket = socket;
		this.objSocket = objSocket;
		initTextStreams();
		initObjStreams();
		try {
			login = reader.readLine();
			parentEngine.getDisplay().syncExec(new Runnable() {
				public void run() {
					parentEngine.createGameMenu.addClients(login);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			showInfo("ошибка получения логина игрока");
		}
		listen();
		listenObj();
		setType(SERVER);
		print("socket after add"+socket.isClosed());
	}

	protected void gotMessage(String message) {
		if (message.equals(DISCONNECT_QUERY)) {
			if (parentEngine.game.online) {
				if (!parentEngine.server.currentPlayersState.remove(playerBuf))
					showInfo("ошибка удаления игрока");
			} else {
				parentEngine.getDisplay().syncExec(new Runnable() {
					public void run() {
						parentEngine.createGameMenu.removeClient(login);
					}
				});
			}
			disconnect();
			parentEngine.server.connectedClients.remove(this);
		}
	}

	public int getPort() {
		return socket.getLocalPort();
	}

	@Override
	protected void gotObject(Object obj) {
		if (obj.getClass() == Actor.class)
		{
			refreshPlayers((Player) obj);
			return;
		}
		if (obj.getClass() == Bullet.class)
		{
			refreshBullets((Bullet) obj);
			return;
		}
		showInfo("полученный объект не опознан");
	}

	private void refreshBullets(Bullet obj) {
			parentEngine.server.sendObjectToAll(obj);
	}

	private void refreshPlayers(Player obj) {
		if ((playerBuf != null) && (!parentEngine.server.currentPlayersState.remove(playerBuf)))
			showInfo("ошибка обновления игрока");
		else {
			playerBuf = obj;
			playerBuf.modelName = "actor";
			parentEngine.server.currentPlayersState.add(playerBuf);
			@SuppressWarnings("unchecked")
			ArrayList<Player> playersBuf = (ArrayList<Player>) parentEngine.server.currentPlayersState.clone();
			sendObject(playersBuf);
			playerBuf.modelName = "player";
		}
	}

}

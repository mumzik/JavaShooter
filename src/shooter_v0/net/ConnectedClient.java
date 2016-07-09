package shooter_v0.net;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import shooter_v0.Engine;
import shooter_v0.helper_parent.NetInteraction;
import shooter_v0.objects.Model;
import shooter_v0.objects.Player;

public class ConnectedClient extends NetInteraction {
	Player playerBuf;

	public ConnectedClient(Engine parentEngine, Socket socket, Socket objSocket) {
		localNetType = "server";
		this.parentEngine = parentEngine;
		this.socket = socket;
		this.objSocket = objSocket;
		
		initTextStreams();
		initObjStreams();
		listen();
		setType("server");
		DEBUG_LEVEL=0;
	}

	protected void gotMessage(String message) {
		if (message.equals(DISCONNECT_QUERY)) {
			waitTextMessages.interrupt();
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				showInfo("ошибка при закрытии потока входящих сообщений");
			}
			writer.close();
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				showInfo("ошибка при закрытии сокета");
			}
			parentEngine.server.connectedClients.remove(this);
		}
		if (message.equals(REFRESH_ME)) {
			try {
				if (playerBuf != null) {
					if (!parentEngine.server.currentPlayersState.remove(playerBuf))
						showInfo("ошибка обновления игрока");
				}
				playerBuf = (Player) objectReader.readObject();
				parentEngine.server.currentPlayersState.add(playerBuf);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				showInfo("неверный тип объекта");
			} catch (IOException e) {
				e.printStackTrace();
				showInfo("ошибка чтения объекта");
			}
			sendMessage(PLAYERS_REFRESH);
			ArrayList<Player> playersBuf = (ArrayList<Player>) parentEngine.server.currentPlayersState.clone();
			sendObject(playersBuf);
			print("players has sended", 5);
		}
	}

	public int getPort() {
		return socket.getLocalPort();
	}

}

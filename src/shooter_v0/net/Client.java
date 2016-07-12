package shooter_v0.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import shooter_v0.Engine;
import shooter_v0.Map;
import shooter_v0.helper_parent.NetInteraction;
import shooter_v0.objects.Actor;
import shooter_v0.objects.Bullet;
import shooter_v0.objects.Model;
import shooter_v0.objects.Player;

public class Client extends NetInteraction {

	private static final int CONNECT_TIMEOUT = 3000;
	private String serverIp;
	private Runnable waitPlayersState;
	private Runnable waitBulletState;

	public Client(Engine parentEngine) {
		this.parentEngine = parentEngine;
		localNetType = "client";		
	}

	public void connect(String serverIp) {
		print("starting");
		this.serverIp = serverIp;
		Thread waitingPort = new Thread(new Runnable() {
			public void run() {
				try {
					socket = new Socket();
					socket.connect(new InetSocketAddress(serverIp, Server.DEFAULT_PORT), CONNECT_TIMEOUT);
					initTextStreams();
					int port = gotPort();
					if (port != 0) {
						int objPort = gotPort();
						socket = connectOnNewPort(port);
						objSocket = connectOnNewPort(objPort);
						initTextStreams();
						writer.println(parentEngine.login);
						initObjStreams();
						listen();
						listenObj();
					}
				} catch (SocketTimeoutException e) {
					showInfo("истекло время подключения к серверу");
				} catch (IOException e) {
					showInfo("ошибка подключения к серверу");
					e.printStackTrace();
				}
			}
		}, "getting free port");
		waitingPort.start();

	}

	private int gotPort() {
		try {
			socket.setSoTimeout(CONNECT_TIMEOUT);
		} catch (SocketException e) {
			e.printStackTrace();
			showInfo("ошибка установки таймаута приема выделенного порта");
		}
		try {
			String message = reader.readLine();
			if (message.equals(GAME_ALREADY_STARTED)) {
				parentEngine.getDisplay().syncExec(new Runnable() {
					public void run() {
						parentEngine.joinGameMenu.exit();
						parentEngine.selectServerMenu.open();
					}
				});
				showInfo("нельзя подключиться к уже начавшейся игре");
				return 0;
			} else {
				int port = Integer.parseInt(message);
				return port;
			}
		} catch (NumberFormatException e) {
			showInfo("неверный формат полученного порта");
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			showInfo("истекло время ожидания порта");
		} catch (IOException e) {
			showInfo("ошибка получения порта");
			e.printStackTrace();
		}
		return 0;
	}

	private Socket connectOnNewPort(int port) {
		// подключение к выделенному порту
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(serverIp, port), CONNECT_TIMEOUT);
			return socket;
		} catch (SocketTimeoutException e) {
			showInfo("истекло время подключения к серверу по выделенному порту");
		} catch (IOException e) {
			e.printStackTrace();
			showInfo("ошибка подключения к серверу по выделенному порту");
		}
		return null;
	}

	protected void gotMessage(String message) {
		if (message.equals(DISCONNECT_COMMAND)) {
			if (parentEngine.getNetType().equals(CLIENT))
				showInfo("сервер был выключен");
			writer.close();
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				showInfo("ошибка закрытия потока входящих сообщений");
			}
			disconnect();
			if (parentEngine.game.mainTimer != null)
				parentEngine.game.mainTimer.stop();

		}
		if (message.equals(START_GAME)) {
			parentEngine.game.online=true;
			parentEngine.getDisplay().syncExec(new Runnable() {
				public void run() {
					parentEngine.createGameMenu.exit();
					parentEngine.game.newGame(true);
				}
			});
		}
	}

	public void disconnectFromServer() {
		writer.println(DISCONNECT_QUERY);
	}

	@Override
	protected void gotObject(Object obj) {
		if (obj.getClass() == ArrayList.class)
		{
			ArrayList<?> buf=(ArrayList<?>) obj;
			if ((buf.get(0).getClass()==Player.class)|(buf.get(0).getClass()==Actor.class))
			{
				parentEngine.game.refreshPlayers((ArrayList<Player>) obj);
			return;
			}
			showInfo("полученный ArrayList не опознан."+buf.get(0).getClass());
			return;
		}
		if (obj.getClass()==Map.class)
		{
			parentEngine.game.map=(Map) obj;
			return;
		}
		if (obj.getClass() == Bullet.class)
		{
			parentEngine.game.bullets.add((Bullet) obj);	
			return;
		}
		showInfo("полученный объект не опознан."+obj.getClass());		
	}


}

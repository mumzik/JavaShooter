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
import shooter_v0.objects.Model;
import shooter_v0.objects.Player;

public class Client extends NetInteraction {

	private static final int CONNECT_TIMEOUT = 3000;
	private String serverIp;
	private Runnable waitPlayersState;

	public Client(Engine parentEngine) {
		this.parentEngine = parentEngine;
		DEBUG_LEVEL=0;
		localNetType="client";
		waitPlayersState=new Thread(new Runnable(){
			public void run() {
				ArrayList<Player> playersBuf;
				try {
					playersBuf = (ArrayList<Player>) objectReader.readObject();
					parentEngine.game.refreshPlayers(playersBuf);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					showInfo("�������� ��� �������");
				} catch (IOException e) {
					e.printStackTrace();
					showInfo("������ ������ �������");
				}
			}
			
		},"wait object thread");
	}

	public void connect(String serverIp) {
		print("starting",1);
		this.serverIp = serverIp;
		Thread waitingPort = new Thread(new Runnable() {
			public void run() {
				try {
					socket = new Socket();
					print("connecting to server(default port)",3);
					socket.connect(new InetSocketAddress(serverIp, Server.DEFAULT_PORT), CONNECT_TIMEOUT);
					initTextStreams();
					int port=gotPort();
					int objPort=gotPort();
					socket=connectOnNewPort(port);
					objSocket=connectOnNewPort(objPort);
					initTextStreams();
					initObjStreams();
					listen();
				} catch (SocketTimeoutException e) {
					showInfo("������� ����� ����������� � �������");
				} catch (IOException e) {
					showInfo("������ ����������� � �������");
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
			showInfo("������ ��������� �������� ������ ����������� �����");
		}
		try {
			print("waiting port",4);
			String message=reader.readLine();
			print(message,5);
			int port=Integer.parseInt(message);
			return port;
		} catch (NumberFormatException e) {
			showInfo("�������� ������ ����������� �����");
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			showInfo("������� ����� �������� �����");
		} catch (IOException e) {
			showInfo("������ ��������� �����");
			e.printStackTrace();
		}
		return 0;
	}

	private Socket connectOnNewPort(int port) {
		// ����������� � ����������� �����
		try {
			print("connecting to server on ports:" + port,3);
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(serverIp, port), CONNECT_TIMEOUT);
			return socket;
		} catch (SocketTimeoutException e) {
			showInfo("������� ����� ����������� � ������� �� ����������� �����");
		} catch (IOException e) {
			e.printStackTrace();
			showInfo("������ ����������� � ������� �� ����������� �����");
		}
		return null;
	}

	protected void gotMessage(String message) {
		if (message.equals(DISCONNECT_COMMAND))
		{	
			if (parentEngine.getNetType().equals(CLIENT))
			showInfo("������ ��� ��������");
			writer.close();
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				showInfo("������ �������� ������ �������� ���������");
			}
			disconnect();
			if (parentEngine.game.mainTimer!=null)
				parentEngine.game.mainTimer.stop();

		}
		if (message.equals(START_GAME))
		{
			try {
				parentEngine.game.map=(Map) objectReader.readObject();
			} catch (ClassNotFoundException e) {
				showInfo("�������� ������ ���������� �����");
				e.printStackTrace();
			} catch (IOException e) {
				showInfo("������ �������� �����");
				e.printStackTrace();
			}
			parentEngine.getDisplay().syncExec(new Runnable() {
				public void run() {
					parentEngine.createGameMenu.exit();
					parentEngine.game.newGame(true);
				}
			});
		}
		if (message.equals(PLAYERS_REFRESH))
		{
			Thread waitPlayersStateThread=new Thread(waitPlayersState,"waiting players state");
			waitPlayersStateThread.run();
		}
	}

	public void disconnectFromServer() {
		writer.println(DISCONNECT_QUERY);		
	}

}

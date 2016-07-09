package shooter_v0.net;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import shooter_v0.Engine;
import shooter_v0.Map;
import shooter_v0.helper_parent.NetInteraction;
import shooter_v0.objects.Model;
import shooter_v0.objects.Player;

public class Server extends NetInteraction {
	public static final int DEFAULT_PORT = 8095;
	private static final int SECONDARY_CONNECTION_ACCEPT_TIMEOUT = 3000;
	public ArrayList<ConnectedClient> connectedClients = new ArrayList<ConnectedClient>();
	private Engine parentEngine;
	private ServerSocket mainServerSocket;
	public InetAddress localIp;
	private Thread waitNewClientsThread;
	public ArrayList<Player> currentPlayersState=new ArrayList<Player>();

	public Server(Engine parentEngine) {
		this.parentEngine = parentEngine;
		setType(SERVER);
		DEBUG_LEVEL=0;
	}

	private InetAddress getLocalIp() {
		InetAddress address;
		try {
			address = InetAddress.getLocalHost();
			return InetAddress.getByName(address.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}

	}

	public void run() {
		print("starting", 1);
		localIp = getLocalIp();
		waitClients();
	}

	private void waitClients() {
		Runnable waiting = new Runnable() {
			public void run() {
				try {
					print("open server socket", 4);
					mainServerSocket = new ServerSocket(DEFAULT_PORT);
					String localIpStr = localIp.toString();
					localIpStr = localIpStr.substring(1, localIpStr.length());
					parentEngine.getClient().connect(localIpStr);
					while (!Thread.currentThread().isInterrupted()) {
						print("waiting new client...", 3);
						Socket socket = mainServerSocket.accept();
						connectNewClient(socket);
					}
				} catch (BindException e) {
					parentEngine.getDisplay().syncExec(new Runnable() {
						public void run() {
							parentEngine.showMessage(
									"ошибка инициализации сервера, порт " + DEFAULT_PORT + " уже исользуется");
							shutdown();
							parentEngine.createGameMenu.exit();
							parentEngine.onlineGameMenu.open();
						}
					});
				} catch (IOException e) {
					if (!Thread.currentThread().isInterrupted()) {
						e.printStackTrace();
						showInfo("ошибка запуска сервера на порту:" + DEFAULT_PORT + " " + e.getMessage());
					}
				} finally {
					print("close main server Socket", 4);
					if (mainServerSocket != null && !mainServerSocket.isClosed())
						try {
							mainServerSocket.close();
						} catch (IOException e) {
							e.printStackTrace();
							showInfo("ошибка при закрытии серверного сокета первичного подключения");

						}
				}
			}
		};

		waitNewClientsThread = new Thread(waiting, "waiting new clients");
		waitNewClientsThread.start();
	}

	private void connectNewClient(Socket socket) {
		if (!waitNewClientsThread.isInterrupted())
			print("new client connected", 2);
		Thread clientAddThread = new Thread(new Runnable() {
			public void run() {
				ServerSocket waitForTextSocket = null;
				ServerSocket waitForObjectSocket = null;
				try {
					print("finding port", 5);
					waitForTextSocket = new ServerSocket(0);
					waitForObjectSocket = new ServerSocket(0);
					sendFreePortToClient(socket, waitForTextSocket, waitForObjectSocket);
					waitSecondaryConnection(waitForTextSocket,waitForObjectSocket);
				} catch (IOException e) {
					e.printStackTrace();
					showInfo("ошибка выделения портов");
				}

			}
		}, "adding new client");
		clientAddThread.start();

	}

	private void sendFreePortToClient(Socket socket, ServerSocket textServerSocket, ServerSocket objectServerSocket) {
		try {
			PrintWriter newClientWriter = new PrintWriter(socket.getOutputStream());
			print("send new port to client", 4);
			print("new client text port is: " + textServerSocket.getLocalPort() + " object: "
					+ objectServerSocket.getLocalPort(), 4);
			newClientWriter.println(textServerSocket.getLocalPort());
			newClientWriter.flush();
			newClientWriter.println(objectServerSocket.getLocalPort());
			newClientWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			showInfo("ошибка создания исходящего потока для отправки порта");
			try {
				textServerSocket.close();
				objectServerSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				showInfo("ошибка при закрытии серверных сокетов вторичного подключения");
			}
		} finally {
			if (!socket.isClosed())
				print("close temorary socket on port: " + textServerSocket.getLocalPort(), 4);
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				showInfo("ошибка при закрытии серверного сокета вторичного подключения");

			}
		}
	}

	private void waitSecondaryConnection(ServerSocket textServerSocket, ServerSocket objectServerSocket ) {
		print("waiting secondary connecting from client", 4);
		try {
			textServerSocket.setSoTimeout(SECONDARY_CONNECTION_ACCEPT_TIMEOUT);
			objectServerSocket.setSoTimeout(SECONDARY_CONNECTION_ACCEPT_TIMEOUT);
			Socket socket=textServerSocket.accept();
			Socket objSocket=objectServerSocket.accept();
			connectedClients.add(new ConnectedClient(parentEngine,socket,objSocket));
			print("client added", 2);
		} catch (SocketTimeoutException e) {
			showInfo("истекло время ожидания вторичного подключения клиента");
		} catch (IOException e) {
			e.printStackTrace();
			showInfo("ошибка при ожидании вторичного подключения клиента к серверу ");
		} finally {
			try {
				textServerSocket.close();
				objectServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
				showInfo("ошибка при закрытии серверных сокетов вторичного подключения");
			}
		}
	}

	public void shutdown() {
		print("shutdown", 1);
		waitNewClientsThread.interrupt();
		print("close main server Socket", 4);
		if (mainServerSocket != null && !mainServerSocket.isClosed())
			try {
				mainServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
				showInfo("ошибка при закрытии серверного сокета первичного подключения");

			}
		for (int i = 0; i < this.connectedClients.size(); i++) {
			print("disconnecting client with port: " + this.connectedClients.get(i).getPort(), 3);
			this.connectedClients.get(i).sendMessage(NetInteraction.DISCONNECT_COMMAND);
			this.connectedClients.get(i).disconnect();
		}
	}

	protected void gotMessage(String message) {

	}

	public void sendToAll(String text) {
		for (int i=0; i<connectedClients.size(); i++)
			connectedClients.get(i).sendMessage(text);
	}

	public void sendObjectToAll(Serializable object) {
		for (int i=0; i<connectedClients.size(); i++)
			connectedClients.get(i).sendObject(object);
		
	}

}

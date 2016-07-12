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
	public static final int DEFAULT_PORT = 8098;
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
		print("starting");
		localIp = getLocalIp();
		waitClients();
	}

	private void waitClients() {
		Runnable waiting = new Runnable() {
			public void run() {
				try {
					mainServerSocket = new ServerSocket(DEFAULT_PORT);
					String localIpStr = localIp.toString();
					localIpStr = localIpStr.substring(1, localIpStr.length());
					parentEngine.getClient().connect(localIpStr);
					while (!Thread.currentThread().isInterrupted()) {
						Socket socket = mainServerSocket.accept();
						if (!parentEngine.game.online)
							connectNewClient(socket);
						else
						{
							try
							{
							PrintWriter newClientWriter = new PrintWriter(socket.getOutputStream());
							newClientWriter.println(GAME_ALREADY_STARTED);
							newClientWriter.flush();
							}
							catch(IOException e){
								e.printStackTrace();
								showInfo("ошибка при информировании клиента");
							}
						}
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
		{
		Thread clientAddThread = new Thread(new Runnable() {
			public void run() {
				ServerSocket waitForTextSocket = null;
				ServerSocket waitForObjectSocket = null;
				try {
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

	}

	private void sendFreePortToClient(Socket socket, ServerSocket textServerSocket, ServerSocket objectServerSocket) {
		try {
			PrintWriter newClientWriter = new PrintWriter(socket.getOutputStream());
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
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				showInfo("ошибка при закрытии серверного сокета вторичного подключения");

			}
		}
	}

	private void waitSecondaryConnection(ServerSocket textServerSocket, ServerSocket objectServerSocket ) {
		try {
			textServerSocket.setSoTimeout(SECONDARY_CONNECTION_ACCEPT_TIMEOUT);
			objectServerSocket.setSoTimeout(SECONDARY_CONNECTION_ACCEPT_TIMEOUT);
			Socket socket=textServerSocket.accept();
			Socket objSocket=objectServerSocket.accept();
			connectedClients.add(new ConnectedClient(parentEngine,socket,objSocket));
			System.out.println("clients count:"+connectedClients.size());
			print("client added");
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
		print("shutdown");
		waitNewClientsThread.interrupt();
		if (mainServerSocket != null && !mainServerSocket.isClosed())
			try {
				mainServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
				showInfo("ошибка при закрытии серверного сокета первичного подключения");

			}
		for (int i = 0; i < this.connectedClients.size(); i++) {
			this.connectedClients.get(i).sendMessage(NetInteraction.DISCONNECT_COMMAND);
			this.connectedClients.get(i).disconnect();
			this.connectedClients.remove(i);
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
		{
			connectedClients.get(i).sendObject(object);
		}
		
	}

	protected void gotObject(Object obj) {
	}

}

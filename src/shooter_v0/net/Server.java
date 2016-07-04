package shooter_v0.net;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import shooter_v0.Engine;
import shooter_v0.EngineInterface;
import shooter_v0.helper_parent.NetInteraction;

public class Server extends NetInteraction{
	public static final int DEFAULT_PORT = 8089;
	private static final int SECONDARY_CONNECTION_ACCEPT_TIMEOUT = 3000;
	public ArrayList<ConnectedClient> connectedClients = new ArrayList<ConnectedClient>();
	private Engine parentEngine;
	private ServerSocket mainServerSocket;
	public InetAddress localIp;
	private Thread waitNewClientsThread;
	private Server self=this;

	public Server(Engine parentEngine) {
		this.parentEngine = parentEngine;
		type="server";
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
		print("starting",self,1);
		localIp = getLocalIp();	
		waitClients();
	}

	private void waitClients() {
		Runnable waiting = new Runnable() {
			public void run() {
					try {
						print("open server socket",self,2);
						mainServerSocket = new ServerSocket(DEFAULT_PORT);
						String localIpStr=localIp.toString();
						localIpStr=localIpStr.substring(1,localIpStr.length());
						parentEngine.getClient().connect(localIpStr);
						while (!Thread.currentThread().isInterrupted()) {
						print("waiting new client...",self,1);
						Socket socket = mainServerSocket.accept();
						connectNewClient(socket);
						}
					}catch (BindException e){
						parentEngine.getDisplay().syncExec(new Runnable() {
							public void run() {
								parentEngine.showMessage("ошибка инициализации сервера, порт "+DEFAULT_PORT+" уже исользуется");
								shutdown();
								parentEngine.createGameMenu.exit();
								parentEngine.onlineGameMenu.open();
							}
						});
					} catch (IOException e) {
						if (!Thread.currentThread().isInterrupted())
						{
						e.printStackTrace();
						showInfo("ошибка запуска сервера на порту:" + DEFAULT_PORT + " " + e.getMessage());
						}						
					} finally {
						print("close main server Socket",self,2);
						if (mainServerSocket!=null&&!mainServerSocket.isClosed())
							try {
								mainServerSocket.close();
							} catch (IOException e) {
								e.printStackTrace();
								showInfo("ошибка при закрытии серверного сокета первичного подключения");
								
							}
				}
			}
		};

		waitNewClientsThread = new Thread(waiting,"waiting new clients");
		waitNewClientsThread.start();
	}

	private void connectNewClient(Socket socket) {
			if (!waitNewClientsThread.isInterrupted())
			print("new client connected",self,1);
			Thread clientAddThread=new Thread(new Runnable(){
				public void run()
				{	ServerSocket waitConnectionOnNewPort=null;	
					try {
						print("finding port",self,2);
						waitConnectionOnNewPort=new ServerSocket(0);
						print("new client port is: "+waitConnectionOnNewPort.getLocalPort(),this,2);
						sendFreePortToClient(socket, waitConnectionOnNewPort);
						waitSecondaryConnection(waitConnectionOnNewPort);		
					} catch (IOException e) {
						e.printStackTrace();
						showInfo("ошибка выделения порта");			
					}
					
				}
			},"adding new client");
			clientAddThread.start();

	}


	private void sendFreePortToClient(Socket socket, ServerSocket serverSocket) {
		try {
			PrintWriter newClientWriter = new PrintWriter(socket.getOutputStream(), true);
			print("send new port to client",this,2);
			newClientWriter.println(serverSocket.getLocalPort());
			newClientWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			showInfo("ошибка создания исходящего потока для отправки порта");
			try {
				serverSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				showInfo("ошибка при закрытии серверного сокета вторичного подключения");
			}
		} finally
		{
			if (!socket.isClosed())
				print("close temorary socket on port: "+serverSocket.getLocalPort(),this,2);
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
					showInfo("ошибка при закрытии серверного сокета вторичного подключения" );
					
				}
		}
	}

	private void waitSecondaryConnection(ServerSocket serverSocket) {
		print("waiting secondary connecting from client",this,2);
		try {	
			serverSocket.setSoTimeout(SECONDARY_CONNECTION_ACCEPT_TIMEOUT);
			connectedClients.add(new ConnectedClient(this, parentEngine, serverSocket.accept()));
			print("client added",this,1);
		}catch (SocketTimeoutException e)
		{
			showInfo("истекло время ожидания вторичного подключения клиента");
		} 
		catch (IOException e) {
			e.printStackTrace();
			showInfo("ошибка при ожидании вторичного подключения клиента к серверу ");
		}finally{
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
				showInfo("ошибка при закрытии серверного сокета вторичного подключения");
			}
		}
	}

	public void shutdown() {
		print("shutdown",this,1);
		waitNewClientsThread.interrupt();
		print("close main server Socket",this,2);
		if (mainServerSocket!=null&&!mainServerSocket.isClosed())
			try {
				mainServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
				showInfo("ошибка при закрытии серверного сокета первичного подключения");
				
			}
		for (int i = 0; i < this.connectedClients.size(); i++) {
			print("disconnecting client with port: "+this.connectedClients.get(i).getPort(),this,1);
			this.connectedClients.get(i).sendMessage(NetInteraction.DISCONNECT_COMMAND);
			this.connectedClients.get(i).disconnect();
		}
	}
	

	protected void gotMessage(String message) {
				
	}

}

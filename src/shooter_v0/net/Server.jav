package shooter_v0.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Handler;

import shooter_v0.engine.TaskManager;
import shooter_v0.objects.Player;

public class ServerClass {
	public static final int DEFAUL_PORT = 8119;
	public ArrayList<ClientConnection> clients=new ArrayList<ClientConnection>();
	public boolean shutDown;
	public TaskManager parent;
	public ServerSocket mainSocket=null;
	public Socket newClient=null;
	public Socket socketOfServer;
	public ServerSocket connectionSocket=null;
	public ArrayList<Player> players=new ArrayList<Player>();
	ServerClass self;
	public Socket CsocketOfServer;
	public InetAddress localIp;
	
	public ServerClass(TaskManager parent)
	{
		self = this;
		this.parent=parent;		
		localIp=getLocalIp();
	}
	
	public InetAddress getLocalIp()
	{
		InetAddress address;
		shutDown=false;
		try {
			address = InetAddress.getLocalHost();
			return InetAddress.getByName(address.getHostAddress()) ;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public void waitClients()
	{		

		 Runnable waiting = new Runnable() {
			 public void run()
			 {
				 try {
					 mainSocket = new ServerSocket(DEFAUL_PORT);
					 while (!shutDown)
					 {
						 System.out.println("waiting...");
						 newClient = mainSocket.accept();
						 System.out.println("new client connected");
						 
						 /** авторизация
						 BufferedReader newClientReader = new BufferedReader(new InputStreamReader(newClient.getInputStream()));
						 String login=newClientReader.readLine();
						 **/
						 
						 PrintWriter newClientWriter = new PrintWriter(newClient.getOutputStream(),true);			
						 connectionSocket=new ServerSocket(0);	
						 newClientWriter.println(connectionSocket.getLocalPort());
						 Socket socket=connectionSocket.accept();
						 clients.add(new ClientConnection(self, socket ,null));
						 System.out.println("client added");
					 }	
					 connectionSocket.close();
					 newClient.close();
					 mainSocket.close();
				 } catch (UnknownHostException e) {
					 e.printStackTrace();
				 } catch (IOException e) {
					 System.out.println("ошибка выдачи порта");
					 e.printStackTrace();
				 } 
		 	}
		 };			

			Thread serverWaitConnections = new Thread(waiting);
			serverWaitConnections.start();			 
	}

	
	

}

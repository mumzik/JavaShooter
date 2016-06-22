package shooter_v0.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import shooter_v0.objects.Player;
import shooter_v0.objects.SerList;

public class ClientConnection {
	public Socket socket;
	public PrintWriter toClient;
	public BufferedReader fromClient;
	public String login;
	public int team=1;
	public ServerClass parent;
	ClientConnection self;
	public Thread serverWaitSelTeam;
	
	public ClientConnection(ServerClass parent, Socket socket,String login) throws IOException
	{
		this.parent=parent;
		this.socket=socket;
		fromClient=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		toClient=new PrintWriter(socket.getOutputStream(),true);
		this.login=login;
		self = this;
		Runnable waitingSelTeam = new Runnable() {
		public void run(){
		try {
			System.out.println(fromClient.readLine());
			team=Integer.parseInt(fromClient.readLine());
			if (team==-1)
			{
				parent.clients.remove(self);
				self.socket.close();
				self=null;
			}
			if (Integer.parseInt(fromClient.readLine())==-1)
			{
				parent.clients.remove(self);
				self.socket.close();
				self.socket=null;
				self=null;
			}
				
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("ошибка получения клманды");
			e.printStackTrace();
		}
			 }
		};
		serverWaitSelTeam = new Thread(waitingSelTeam);	
		serverWaitSelTeam.start();	
	}
	public void init()
	{
		Player plBuf=new Player();
		plBuf.setName(login);
		parent.players.add(plBuf);
		 Runnable waiting = new Runnable() {
			 public void run()
			 {
				 Player playerBuf=new Player();
				 System.out.println("IN INIT"+parent.parent.game.time);
				 while (parent.parent.game.time)
				 	{
					  ObjectInputStream playerReader;
					try {
						
						
							playerReader = new ObjectInputStream(socket.getInputStream());
							playerBuf=(Player) playerReader.readObject();
						
						
						
						/**for (int i=0; i<parent.players.size(); i++)
						{
							if (parent.players.get(i).name.equals(playerBuf.name)){
								parent.players.remove(i);
								parent.players.add(playerBuf);
							}
							System.out.println("from server size:   "+parent.players.size());					
						}**/
						
						
						
							ObjectOutputStream playerWriter1 = new ObjectOutputStream(socket.getOutputStream());
							playerWriter1.writeObject((Player) playerBuf);
							
							//playerWriter.writeObject((Player)(parent.players.get(0)));														
							//for (int i=0; i<parent.players.size(); i++)
							//{
							//System.out.println("send players to client,   i:"+i);
							//playerBuf=(Player) parent.players.get(0);
							//System.out.println("class of player"+playerBuf.getClass());
							//playerWriter.writeObject((Player) parent.players.get(0));
							//}
						} catch (IOException | ClassNotFoundException e) {
							System.out.println("ошибка отправки мира");
							e.printStackTrace();
						}
				 	}
		 	}
		 };			

			Thread serverWaitConnections = new Thread(waiting);
			serverWaitConnections.start();
	}
}

package shooter_v0.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import shooter_v0.engine.TaskManager;
import shooter_v0.engine.rooms.JoinGameMenu;

public class Client {
	 Socket socket;
	 JoinGameMenu parentMenu;
	 public Client(JoinGameMenu parent) {
		this.parentMenu=parent;
	}
	public Socket connect(TaskManager parent,String serverIp)
	{
	System.out.println("client opened");
	try {
		socket=new Socket(serverIp,ServerClass.DEFAUL_PORT);
		PrintWriter getPortSocketWriter = new PrintWriter(socket.getOutputStream(),true);
		getPortSocketWriter.println(parent.login);
		BufferedReader getPortSocketReader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
		int port=Integer.parseInt(getPortSocketReader.readLine());
		System.out.println("client: my port is "+port);
		socket.close();
		socket=new Socket(serverIp,port);
		//need rewrite
		getPortSocketWriter = new PrintWriter(socket.getOutputStream(),true);
		Runnable waitingStart = new Runnable() {
			 public void run(){
		BufferedReader waitStart;
		try {
			waitStart = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message=waitStart.readLine();
			if (message.equals("start"))
			{
			parentMenu.socket=socket;
			parentMenu.waiting=false;
			}
		} catch (IOException e) {
			System.out.println("ошибка получения сигнала старта игры");
		}
		
			 }
		};
		Thread swaitingStartThread = new Thread(waitingStart);
		swaitingStartThread.start();
		
		getPortSocketWriter.println("its i, on new port");
		return socket;
	} catch (UnknownHostException e) {
		e.printStackTrace();
	} catch (IOException e) {
		System.out.println("ошибка в переопределении порта со стороны клиента");
	}
	return null;
	
	}
}

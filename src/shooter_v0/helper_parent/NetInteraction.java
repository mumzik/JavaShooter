package shooter_v0.helper_parent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import org.eclipse.swt.widgets.Shell;

import shooter_v0.EngineInterface;

public abstract class NetInteraction extends DebugClass {
	private static final int DEBUGING_LEVEL = 1;
	public static final String DISCONNECT_COMMAND = "DISCONNECT";
	public static final String DISCONNECT_QUERY="DISCONNECT ME";
	protected Socket socket;
	protected EngineInterface parentEngine;
	protected String type;
	protected Thread waitTextMessages;
	protected PrintWriter writer;
	protected BufferedReader reader;
	private NetInteraction self=this;

	protected void listen() {
		print("listening text",self,2);
		Runnable waitTextMessage = new Runnable() {
			public void run() {
				while (!Thread.interrupted()) {
					try {
						String message = reader.readLine();
						if (!Thread.currentThread().isInterrupted())
						{
							print("got message: " + message,self,1);
							gotMessage(message);
						}
					} catch (IOException e) {
						if (!Thread.currentThread().isInterrupted()) {
							e.printStackTrace();
							showInfo("ошибка чтения входящего сообщения");
						}
					}
				}
			}
		};
		waitTextMessages = new Thread(waitTextMessage, "waiting messages");
		waitTextMessages.start();
	}

	protected abstract void gotMessage(String message);

	public void disconnect() {
		print("disconnecting, port on server: " + socket.getPort(),self,2);
		parentEngine.setNetType("offline");
		waitTextMessages.interrupt();
		try {
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			showInfo("ошибка закрытия потока входящих сообщений");
		}
		writer.close();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			showInfo("ошибка закрытия сокета");
		}

	}


	protected void showInfo(String text) {
		parentEngine.getDisplay().syncExec(new Runnable() {
			public void run() {
				parentEngine.showMessage(type + ": " + text);
			}
		});
	}

	public void sendMessage(String text)
	{
		print("send message "+text,self,1);
		writer.println(text);
	}

	public void setType(String type) {
		this.type=type;
		
	}

	public void initStreams()
	{
		print("init streams",self,3);
		try {
			writer=new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
			showInfo("ошибка создания исходящего потока  сообщений");
		}
		try {
			reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			showInfo("ошибка создания входящего потока  сообщений");
		}
	}
}

package shooter_v0.helper_parent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

import shooter_v0.Engine;
import shooter_v0.objects.Obj;

public abstract class NetInteraction extends DebugClass {
	public static final String DISCONNECT_COMMAND = "DISCONNECT";
	public static final String DISCONNECT_QUERY="DISCONNECT ME";
	public static final String OFFLINE="offline";
	public static final String SERVER="server";
	public static final String CLIENT="client";
	public static final String PLAYERS_REFRESH="get players states";	
	public static final String REFRESH_ME="refresh this player";
	public static final String START_GAME = "game start";
	public static final String GAME_ALREADY_STARTED="game already started";
	public static final String ADD_BULLET = "add new bullet";
	protected Socket socket;
	protected Socket objSocket;
	protected Engine parentEngine;
	protected String localNetType;
	protected Thread waitTextMessages;
	protected PrintWriter writer;
	protected BufferedReader reader;
	protected ObjectInputStream objectReader;
	protected ObjectOutputStream objectWriter;
	protected Thread waitObjectThread;
	protected void listen() {
		Runnable waitTextMessage = new Runnable() {
			public void run() {
				while (!Thread.interrupted()) {
					try {
						String message = reader.readLine();
						if (!Thread.currentThread().isInterrupted())
						{
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

	
	protected void listenObj() {
		print("listening object");
		Runnable waitObject = new Runnable() {
			public void run() {
				while (!Thread.interrupted()) {
					try {
						System.out.println(this.getClass());
						Object obj = objectReader.readObject();
						if (!Thread.currentThread().isInterrupted())
						{
							gotObject(obj);
						}
					} catch (IOException e) {
						if (!Thread.currentThread().isInterrupted()) {
							e.printStackTrace();
							showInfo("ошибка чтения полученного объекта");
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						showInfo("не найден класс полученного объекта");
					}
				}
			}
		};
		waitObjectThread = new Thread(waitObject, "waiting messages");
		waitObjectThread.start();
	}

	protected abstract void gotMessage(String message);
	protected abstract void gotObject(Object obj);
	public void disconnect() {
		print("disconnect");
		parentEngine.setNetType(OFFLINE);
		waitTextMessages.interrupt();
		waitObjectThread.interrupt();
		try {
			objectReader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			showInfo("ошибка закрытия потока принимаемых объектов");
		}
		try {
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			showInfo("ошибка закрытия потока входящих сообщений");
		}
		try {
			objectWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			showInfo("ошибка закрытия потока отправляемых объектов");
		}
		writer.close();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			showInfo("ошибка закрытия сокета");
		}
		closeTextStreams();

	}
	protected void showInfo(String text) {
		parentEngine.getDisplay().syncExec(new Runnable() {
			public void run() {
				parentEngine.showMessage(localNetType + ": " + text);
			}
		});
	}
	public void sendMessage(String text)
	{
		writer.println(text);
	}
	public void setType(String type) {
		this.localNetType=type;
		
	}
	public void initTextStreams()
	{
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
	public void initObjStreams()
	{
		try {
			objectWriter=new ObjectOutputStream(objSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			showInfo("ошибка при создании потока отправляемых объектов");
		}
		try {
			objectReader=new ObjectInputStream(objSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			showInfo("ошибка при создании потока принимаемых объектов");
		}
	}
	public void closeTextStreams()
	{
		writer.close();
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			showInfo("ошибка при закрытии текстовых потоков");
		}
	}
	public void sendObject(Serializable object)
	{
		try {
			objectWriter.reset();
			objectWriter.writeObject(object);
			objectWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(object.getClass());
			showInfo("ошибка отправки объекта");
		}
	}
}

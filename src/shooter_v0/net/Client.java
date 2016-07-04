package shooter_v0.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import shooter_v0.EngineInterface;
import shooter_v0.helper_parent.NetInteraction;

public class Client extends NetInteraction {

	private static final int CONNECT_TIMEOUT = 3000;
	private String serverIp;
	private int port;

	public Client(EngineInterface parentEngine) {
		this.parentEngine = parentEngine;
		setType("client");
	}

	public void connect(String serverIp) {
		print("starting",this,1);
		this.serverIp = serverIp;
		Thread waitingPort = new Thread(new Runnable() {
			public void run() {
				try {
					socket = new Socket();
					print("connecting to server(default port)",this,2);
					socket.connect(new InetSocketAddress(serverIp, Server.DEFAULT_PORT), CONNECT_TIMEOUT);
					gotPort();
					connectOnNewPort();
					initStreams();
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

	private void gotPort() {
		try {
			socket.setSoTimeout(CONNECT_TIMEOUT);
		} catch (SocketException e) {
			e.printStackTrace();
			showInfo("������ ��������� �������� ������ ����������� �����");
		}
		try {
			print("waiting port",this,2);
			initStreams();
			port = Integer.parseInt(reader.readLine());
			print("got port " + port,this,2);
		} catch (NumberFormatException e) {
			showInfo("�������� ������ ����������� �����");
			e.printStackTrace();
		} catch (SocketTimeoutException e) {
			showInfo("������� ����� �������� �����");
		} catch (IOException e) {
			showInfo("������ ��������� �����");
			e.printStackTrace();
		}

	}

	private void connectOnNewPort() {
		// ����������� � ����������� �����
		try {
			print("connecting to server on port:" + port,this,2);
			socket = new Socket();
			socket.connect(new InetSocketAddress(serverIp, port), CONNECT_TIMEOUT);
		} catch (SocketTimeoutException e) {
			showInfo("������� ����� ����������� � ������� �� ����������� �����");
		} catch (IOException e) {
			e.printStackTrace();
			showInfo("������ ����������� � ������� �� ����������� �����");
		}
	}

	protected void gotMessage(String message) {
		if (message.equals(DISCONNECT_COMMAND))
		{
			writer.close();
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				showInfo("������ �������� ������ �������� ���������");
			}
			disconnectFromServer();
			disconnect();
			if (parentEngine.getServer()==null)
			showInfo("������ ��� ��������");
		}
	}

	public void disconnectFromServer() {
		writer.println(DISCONNECT_QUERY);		
	}

}

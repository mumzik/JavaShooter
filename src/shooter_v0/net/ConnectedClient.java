package shooter_v0.net;

import java.io.IOException;
import java.net.Socket;

import shooter_v0.EngineInterface;
import shooter_v0.helper_parent.NetInteraction;

public class ConnectedClient extends NetInteraction{
	private Server parentServer;
	
	
	public ConnectedClient(Server parentServer, EngineInterface parentEngine, Socket socket) 
	{
		type="server";
		this.parentEngine=parentEngine;
		this.parentServer=parentServer;		
		this.socket=socket;
		initStreams();
		listen();
		setType("server");
	}

	protected void gotMessage(String message) {	
		if (message.equals(DISCONNECT_QUERY))
		{
			waitTextMessages.interrupt();
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				showInfo("ошибка при закрытии потока входящих сообщений");
			}
			writer.close();
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
				showInfo("ошибка при закрытии сокета");
			}
			parentServer.connectedClients.remove(this);
			print("client has disconnected",this,1);
		}
	}

	public int getPort() {
		return socket.getLocalPort();
	}
	
}


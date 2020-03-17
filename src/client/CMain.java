package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class CMain {

	public static void main(String[] args) throws Exception {
		Socket withServer = null;
		withServer = new Socket("10.0.0.101",9999);
		new ClientChat(withServer);
		
		
		
	}

}

package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SMain {

	public static void main(String[] args) throws Exception {
		ServerSocket serverS = null;
		Socket withClient = null;
		serverS = new ServerSocket();
		serverS.bind(new InetSocketAddress("10.0.0.101", 9999));
		ServerCenter sc = new ServerCenter();
		
		while(true){
			withClient = serverS.accept();
			ServerChat s = new ServerChat(withClient, sc);
			sc.addServerChat(s);
			s.start();
			System.out.println("");
		}
	}

}

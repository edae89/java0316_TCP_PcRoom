package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientChat {
	private InputStream receiveMsg = null;
	private OutputStream sendMsg = null;
	private Socket withServer = null;
	private String id = null;
	private String pw = null;
	private Scanner in = new Scanner(System.in);
	
	
	public ClientChat(Socket withServer) {
		this.withServer = withServer;
		login();
		receive();
		send();
		
	}
	
	
	
	private void login() {
		try {
			System.out.println("[��ǰ �ֹ� �α���]");
			System.out.println("Id�� �Է��ϼ��� >");
			id = in.nextLine();
			System.out.println("Pw�� �Է��ϼ��� >");
			pw  = in.nextLine();
			String clientObject = id+"/"+pw;
			sendMsg = withServer.getOutputStream();
			byte[] sendBuffer = new byte[100];
			sendBuffer = clientObject.getBytes();
			sendMsg.write(sendBuffer);
			
			receiveMsg = withServer.getInputStream();
			byte[] receiveBuffer = new byte[100];
			receiveMsg.read(receiveBuffer);
			String msg = new String(receiveBuffer);
			msg = msg.trim();
			System.out.println(msg);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	//�޴� ��� �ޱ�
	private void receive() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					try {
						receiveMsg = withServer.getInputStream();
						byte[] receiveBuffer = new byte[100];
						receiveMsg.read(receiveBuffer);
						String msg = new String(receiveBuffer);
						msg = msg.trim();
						System.out.println(msg);
						
						
					} catch (IOException e) {
						e.printStackTrace();
						
					}
					
					
					
				}
				
			}
		}).start();
		
		
	}

	//�ֹ� ��ǰ ������
	private void send() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					try {
						System.out.println("�ֹ��� �޴��� �̸��� �����ּ���.");
						String msg = in.nextLine();
						String totalMsg = msg+"/"+id;
						sendMsg = withServer.getOutputStream();
						byte[] sendBuffer = new byte[100];
						sendBuffer = totalMsg.getBytes();
						sendMsg.write(sendBuffer);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();
		
	}

	protected void endChat() {
		try {
			withServer.close();
			receiveMsg.close();
			sendMsg.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	

}

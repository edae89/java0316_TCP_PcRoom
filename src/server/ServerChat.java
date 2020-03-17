package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ServerChat extends Thread{
	private InputStream receiveMsg = null;
	private OutputStream sendMsg = null;
	private Socket withClient = null;
	private ServerCenter sc = null;
	private String loginObject = null;
	private ArrayList<String> cList = null;
	private ArrayList<String> mList = null;
	
	

	public ServerChat(Socket withClient, ServerCenter sc) {
		this.withClient = withClient;
		this.sc = sc;
		this.cList = sc.getCList();
		this.mList = sc.getMList();
	}

	@Override
	public void run() {

		idChk();// idChk(��Ƽ������ x)
		receive();// receive(��Ƽ������ o)
//		send();// send(��Ƽ������ x)
		
	}

	
	// ID/PW�� ClientChat���κ��� InputStream�Ͽ� DB�� �� üũ�Ѵ�.-> ����� ClinetChat�� OutputStream�Ѵ�.
	private void idChk() {
		try {
			//ClientChat���� ���̵� �޾� String���� �ٲ�
			receiveMsg = withClient.getInputStream();
			byte[] receiveBuffer = new byte[100];
			receiveMsg.read(receiveBuffer);	// id/pw
			loginObject = new String(receiveBuffer);
			loginObject = loginObject.trim();
			
			System.out.println(loginObject);
			StringTokenizer tokenLO = new StringTokenizer(loginObject, "/");
			String loginId = tokenLO.nextToken();
			String loginPw = tokenLO.nextToken();
			
			//cList�� ��ȸ�ϸ�, ���� ���̵� ��Ͽ� �ִ��� Ȯ��
			for(int i = 0; i < cList.size(); i++) {
				String cListObject = cList.get(i);
				StringTokenizer tokenCO = new StringTokenizer(cListObject, "/");
				String cListId = tokenCO.nextToken();
				String cListPw = tokenCO.nextToken();
				if(loginId.equals(cListId) && loginPw.equals(cListPw)) {
					System.out.println(cListId+"�� �α��� ����!");
					
					//�α��� �Ϸ� �˸� �����ֱ�
					String msg = "�α��� �Ϸ�!";
					sendMsg = withClient.getOutputStream();
					byte[] sendBuffer = new byte[100];
					sendMsg.write(sendBuffer);
					
					//�޴���� �����ֱ�
					send();
					break;
				}else {
					String msg = "���̵� ���ų� ��й�ȣ�� �߸��Ǿ����ϴ�!";
					sendMsg = withClient.getOutputStream();
					byte[] sendBuffer = new byte[100];
					sendMsg.write(sendBuffer);
					
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	//�׽� �ֹ� �ޱ� (��Ƽ������)
	private void receive() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("receive ����!");
				while(true) {
					try {
						receiveMsg = withClient.getInputStream();
						byte[] receiveBuffer = new byte[100];
						receiveMsg.read(receiveBuffer);
						String OrderObject = new String(receiveBuffer);
						OrderObject = OrderObject.trim();
						StringTokenizer tokenOO = new StringTokenizer(OrderObject, "/");
						String OrderMenu = tokenOO.nextToken();
						String OrderId = tokenOO.nextToken();
						
						
						for(int i = 0; i < mList.size(); i++) {
							String mListObject = mList.get(i);
							StringTokenizer tokenMO = new StringTokenizer(mListObject, "/");
							String mListMenu = tokenMO.nextToken();
							
							if(OrderMenu.equals(mListMenu)) {
								System.out.println(OrderId+"���� "+mListMenu+"<< ��ǰ�� �ֹ��ϼ̽��ϴ�.");
								
								String msg = mListMenu+" ��ǰ �ֹ� �Ϸ�!";
								sendMsg = withClient.getOutputStream();
								byte[] sendBuffer = new byte[100];
								sendBuffer = msg.getBytes();
								sendMsg.write(sendBuffer);
								break;
							}
							
						}
						
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		}).start();
	}
	
	
	//�α��� �Ŀ� ������
	//�޴� ��� �����ֱ�(��Ƽ������ o)
	private void send() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					for(int i = 0; i < mList.size(); i++) {
						String msg = mList.get(i);
						sendMsg = withClient.getOutputStream();
						sendMsg.write(msg.getBytes());
						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}

	

	protected void endChat() {
		try {
			withClient.close();
			receiveMsg.close();
			sendMsg.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
}

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

		idChk();// idChk(멀티스레드 x)
		receive();// receive(멀티스레드 o)
//		send();// send(멀티스레드 x)
		
	}

	
	// ID/PW를 ClientChat으로부터 InputStream하여 DB와 비교 체크한다.-> 결과를 ClinetChat에 OutputStream한다.
	private void idChk() {
		try {
			//ClientChat에서 아이디를 받아 String으로 바꿈
			receiveMsg = withClient.getInputStream();
			byte[] receiveBuffer = new byte[100];
			receiveMsg.read(receiveBuffer);	// id/pw
			loginObject = new String(receiveBuffer);
			loginObject = loginObject.trim();
			
			System.out.println(loginObject);
			StringTokenizer tokenLO = new StringTokenizer(loginObject, "/");
			String loginId = tokenLO.nextToken();
			String loginPw = tokenLO.nextToken();
			
			//cList를 순회하며, 받은 아이디가 목록에 있는지 확인
			for(int i = 0; i < cList.size(); i++) {
				String cListObject = cList.get(i);
				StringTokenizer tokenCO = new StringTokenizer(cListObject, "/");
				String cListId = tokenCO.nextToken();
				String cListPw = tokenCO.nextToken();
				if(loginId.equals(cListId) && loginPw.equals(cListPw)) {
					System.out.println(cListId+"님 로그인 성공!");
					
					//로그인 완료 알림 보내주기
					String msg = "로그인 완료!";
					sendMsg = withClient.getOutputStream();
					byte[] sendBuffer = new byte[100];
					sendMsg.write(sendBuffer);
					
					//메뉴목록 보내주기
					send();
					break;
				}else {
					String msg = "아이디가 없거나 비밀번호가 잘못되었습니다!";
					sendMsg = withClient.getOutputStream();
					byte[] sendBuffer = new byte[100];
					sendMsg.write(sendBuffer);
					
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	//항시 주문 받기 (멀티스레드)
	private void receive() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				System.out.println("receive 시작!");
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
								System.out.println(OrderId+"님이 "+mListMenu+"<< 상품을 주문하셨습니다.");
								
								String msg = mListMenu+" 상품 주문 완료!";
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
	
	
	//로그인 후에 보내기
	//메뉴 목록 보내주기(멀티스레드 o)
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

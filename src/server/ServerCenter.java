package server;


import java.util.ArrayList;

public class ServerCenter {

	private ArrayList<ServerChat> sList = new ArrayList<>(); //ServerChat��ü ����Ʈ
	private ArrayList<String> cList = new ArrayList<>(); //ȸ�� ����Ʈ
	private ArrayList<String> mList = new ArrayList<>(); //�޴� ����Ʈ

	ServerCenter(){
		clientSet();
		menuSet();
	}
	
	
	public void addServerChat(ServerChat s) {
		sList.add(s);
	}
	
	
	public ArrayList<String> getCList() {
		return cList;
	}
	
	
	public ArrayList<String> getMList() {
		return mList;
	}
	

	private void clientSet() {
		cList.add("aaa/1111");
		cList.add("bbb/1111");
		cList.add("ccc/1111");
		
	}


	private void menuSet() {
		mList.add("drink/1000��  ");
		mList.add("noodle/1000��  ");
		mList.add("snack/1000��  ");
		
	}


	
	
	
}

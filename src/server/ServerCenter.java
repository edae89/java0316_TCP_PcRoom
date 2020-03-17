package server;


import java.util.ArrayList;

public class ServerCenter {

	private ArrayList<ServerChat> sList = new ArrayList<>(); //ServerChat객체 리스트
	private ArrayList<String> cList = new ArrayList<>(); //회원 리스트
	private ArrayList<String> mList = new ArrayList<>(); //메뉴 리스트

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
		mList.add("drink/1000원  ");
		mList.add("noodle/1000원  ");
		mList.add("snack/1000원  ");
		
	}


	
	
	
}

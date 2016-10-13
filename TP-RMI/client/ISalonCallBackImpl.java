package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ISalonCallBackImpl extends UnicastRemoteObject implements ISalonCallBack {
	
	private static final long serialVersionUID = 1L;
	private Client currentCl;

	protected ISalonCallBackImpl() throws RemoteException {
		super();
	}
	
	public ISalonCallBackImpl(Client currentCl) throws RemoteException {
		super();
		this.currentCl = currentCl;
	}
	
	public Client getCurrentCl() {
		return currentCl;
	}

	public void setCurrentCl(Client currentCl) {
		this.currentCl = currentCl;
	}
	
	@Override
	public void receiveMessage(String msg) throws RemoteException {
		System.out.println(msg);
	}
}

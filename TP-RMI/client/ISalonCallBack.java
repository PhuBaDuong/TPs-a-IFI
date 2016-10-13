package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISalonCallBack extends Remote {
	public void receiveMessage(String msg) throws RemoteException;
}

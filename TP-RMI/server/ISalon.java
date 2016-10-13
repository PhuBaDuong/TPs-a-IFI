package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISalon extends Remote {
	public void postMessage(String msg) throws RemoteException;
	public void leave() throws RemoteException;
}

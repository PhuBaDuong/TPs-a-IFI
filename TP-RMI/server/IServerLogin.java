package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServerLogin extends Remote {
	public IServer login(String user, String pass) throws RemoteException;
}

package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.ISalonCallBack;

public interface IServer extends Remote {
	public ISalon join(ISalonCallBack callback) throws RemoteException;
	
}

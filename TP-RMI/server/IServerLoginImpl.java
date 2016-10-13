package server;

import java.rmi.RemoteException;

import java.rmi.server.UnicastRemoteObject;

public class IServerLoginImpl extends UnicastRemoteObject implements IServerLogin {
	
	private static final long serialVersionUID = 1L;
	private IServerImpl serverImpl;

	protected IServerLoginImpl() throws RemoteException {
		super();
		this.serverImpl = new IServerImpl();
	}
	
	public IServerLoginImpl(IServerImpl serverImpl) throws RemoteException {
		super();
		this.serverImpl = serverImpl;
	}
	
	public IServerImpl getServerImpl() {
		return serverImpl;
	}
	
	public void setServerImpl(IServerImpl serverImpl) {
		this.serverImpl = serverImpl;
	}
	
	@Override
	public IServer login(String user, String pass) throws RemoteException {
		Account account = new Account(user, pass);
		Account accountFound = getServerImpl().findUser(account);
		if (accountFound != null) {
			System.out.println(user + " logined!\n");
			return getServerImpl();
		}
		else {			
			System.out.println("This account do not exist!");
		}
		return null;
	}
}

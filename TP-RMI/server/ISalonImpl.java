package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ISalonImpl extends UnicastRemoteObject implements ISalon {

	private static final long serialVersionUID = 1L;
	private IServerImpl serverImpl;
	private String receivedMsg;

	protected ISalonImpl() throws RemoteException {
		super();
	}

	public ISalonImpl(IServerImpl serverImpl) throws RemoteException {
		super();
		this.serverImpl = serverImpl;
	}

	public IServerImpl getServerImpl() {
		return serverImpl;
	}

	public void setServerImpl(IServerImpl serverImpl) {
		this.serverImpl = serverImpl;
	}

	public String getReceivedMsg() {
		return receivedMsg;
	}

	public void setReceivedMsg(String receivedMsg) {
		this.receivedMsg = receivedMsg;
	}

	@Override
	public void postMessage(String msg) throws RemoteException {
		// TODO Auto-generated method stub
		if (msg.substring(msg.indexOf(':') + 1).equalsIgnoreCase("quit")) {
			this.setReceivedMsg(msg);
			leave();
		} else {
			System.out.println("Message received : " + msg.substring(msg.indexOf(':') + 1) + " from "
					+ msg.substring(0, msg.indexOf(":")));
			this.getServerImpl().sendMsgToAllClients(msg);
		}
	}

	@Override
	public void leave() throws RemoteException {
		this.getServerImpl().removeClient(this.getReceivedMsg().substring(0, this.getReceivedMsg().indexOf(':')));
	}
}

package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import client.ISalonCallBack;

public class IServerImpl extends UnicastRemoteObject implements IServer {
	private static final long serialVersionUID = 1L;

	private ArrayList<Account> accounts;
	private Account currentAccount;
	private Map<String, ISalonCallBack> clientName_SalonCallBack;

	protected IServerImpl() throws RemoteException {
		super();
		this.accounts = new ArrayList<Account>();
		this.clientName_SalonCallBack = new HashMap<>();
		initAccounts();
	}

	public IServerImpl(ISalon salon) throws RemoteException {
		super();
		this.accounts = new ArrayList<Account>();
		this.clientName_SalonCallBack = new HashMap<>();
		initAccounts();
	}

	public void initAccounts() {
		Account accountD = new Account("Duong", "111");
		accounts.add(accountD);
		Account accountDer = new Account("ThaiDuong", "222");
		accounts.add(accountDer);
		Account accountKhai = new Account("Thanh", "333");
		accounts.add(accountKhai);
	}

	public Account findUser(Account account) {
		for (int i = 0; i < accounts.size(); i++) {
			if (accounts.get(i).verifyAcc(account)) {
				currentAccount = accounts.get(i);
				return accounts.get(i);
			}
		}
		return null;
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}

	@Override
	public ISalon join(ISalonCallBack callback) throws RemoteException {
		this.getClientName_SalonCallBack().put(currentAccount.getName(), callback);
		ISalonImpl salonImpl = new ISalonImpl(this);
		return salonImpl;
	}

	public Map<String, ISalonCallBack> getClientName_SalonCallBack() {
		return clientName_SalonCallBack;
	}

	public void setClientName_SalonCallBack(Map<String, ISalonCallBack> clientName_SalonCallBack) {
		this.clientName_SalonCallBack = clientName_SalonCallBack;
	}

	public void sendMsgToAllClients(String msg) throws RemoteException {
		Iterator<String> keys = this.getClientName_SalonCallBack().keySet().iterator();
		while (keys.hasNext()) {
			ISalonCallBack salonCallBack = (ISalonCallBack) this.getClientName_SalonCallBack().get(keys.next());
			salonCallBack.receiveMessage(msg);
		}
	}

	public void removeClient(String clientName) {
		this.getClientName_SalonCallBack().remove(clientName);
	}
}

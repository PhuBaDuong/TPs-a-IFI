package server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import setting.Parameters;

public class ServerChat {
	
	private IServerLoginImpl serverLoginImpl;
	
	public ServerChat() {
		try {
			serverLoginImpl = new IServerLoginImpl();
		} catch (Exception ex) {
			System.out.println("Cannot create IServerLogin " + ex);
		}
		try {
			LocateRegistry.createRegistry(1099);
			System.setProperty( "java.rmi.server.hostname", Parameters.IP_SERVER_RMI);
			Naming.rebind("rmi://" + Parameters.IP_SERVER_RMI + ":1099/ServerLogin", serverLoginImpl);
			System.out.println("Server chat is registered!\n");
		} catch (Exception ex) {
			System.out.println("Cann't rebind " + ex);
		}
	}
	
	public static void main(String[] args) {
		new ServerChat();
	}
}


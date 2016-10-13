package client;

import java.net.MalformedURLException;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import server.Account;
import server.ISalon;
import server.IServer;
import server.IServerLogin;
import setting.Parameters;

public class Client {
	private Account account;
	private static Scanner reader;

	public Client() {
		super();
		this.account = new Account();
	}

	public Client(Account account) {
		super();
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public static void main(String args[]) {
		String msg = null;
		reader = new Scanner(System.in);
		Client cl = new Client();
		System.out.println("Enter your name: ");
		cl.getAccount().setName(reader.nextLine());
		System.out.println("Enter your pass: ");
		cl.getAccount().setPwd(reader.nextLine());
		try {
			IServerLogin serverlogin = (IServerLogin) Naming.lookup("rmi://" + Parameters.IP_SERVER_RMI +":1099/ServerLogin");
			IServer server = (IServer) serverlogin.login(cl.getAccount().getName(), cl.getAccount().getPwd());
			if (server != null) {
				System.out.println("You logined! Do you want to join the chat salon? Y/N");
				String response = reader.nextLine();
				if (response.equals("Y")) {
					ISalonCallBack salonCallBack = new ISalonCallBackImpl();
					ISalon salon = (ISalon) server.join(salonCallBack);
					if (salon == null) {
						System.out.println("Salon does not exist!");
						System.exit(1);
					}
					while (true) {
						System.out.println("Do you want to write somethings? (Type 'quit' to leave)");
						msg = reader.nextLine();
						salon.postMessage(cl.getAccount().getName() + ":" + msg);
						if(msg.equalsIgnoreCase("quit"))
							System.exit(0);
					}
				} else {
					System.out.println("You did not want to join the chat!");
					return;
				}
			} else {
				System.out.println("Your account does not exist! :(");
			}

		} catch (MalformedURLException murle) {
			System.out.println("MalformedURLException");
			System.out.println(murle);
		} catch (RemoteException re) {
			System.out.println("RemoteException");
			System.out.println(re);
		} catch (NotBoundException nbe) {
			System.out.println("NotBoundException");
			System.out.println(nbe);
		} catch (java.lang.ArithmeticException ae) {
			System.out.println("java.lang.ArithmeticException");
			System.out.println(ae);
		}
	}
}

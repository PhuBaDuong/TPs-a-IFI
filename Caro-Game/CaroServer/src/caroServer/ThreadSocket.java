package caroServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ThreadSocket extends Thread {

	private Socket socket;
	private int port;
	private DatabaseConnection dbConnection;
	private Address address;

	public ThreadSocket(Socket socket, int port) {
		this.socket = socket;
		this.port = port;
		dbConnection = new DatabaseConnection();
	}

	@Override
	public void run() {
		try {
			System.out.println("The current port : " + port);
			DataInputStream inFromServer = new DataInputStream(System.in);
			// create input Stream from client
			DataInputStream inFromClient = new DataInputStream(
					socket.getInputStream());
			// create output Stream to client
			DataOutputStream outToClient = new DataOutputStream(
					socket.getOutputStream());

			while (true) {
				String strInfosFromClient = inFromClient.readLine();
				System.out.println(strInfosFromClient);
				while (strInfosFromClient == null)
					;
				String[] ss = strInfosFromClient.split("-");

				// check login
				if (ss[0].equals("1")) {

					System.out.println("Username and password :" + ss[1] + ","
							+ ss[2]);
					if (dbConnection.checkLogin(ss[1], ss[2])) {
						outToClient.writeBytes("1\n");
					} else {
						outToClient.writeBytes("0\n");
					}
				}

				// register member
				if (ss[0].equals("2")) {
					String infos;
					String account;
					account = "INSERT INTO tp1.players (username, password) VALUES (?,?)";
					dbConnection.setPstmt(dbConnection.getCon()
							.prepareStatement(account));
					dbConnection.getPstmt().setString(1, ss[1]);
					dbConnection.getPstmt().setString(2, ss[2]);
					dbConnection.getPstmt().executeUpdate();
					outToClient.writeBytes("1\n");
				}

				// create a server
				if (ss[0].equals("3")) {
					int port1 = port + 1;
					address.port1.add(port);
					address.port2.add(port1);
					address.ip.add(ss[2]);
					address.name.add(ss[1]);
					String strSocketServer = ss[2] + "-" + port + "-" + port1;
					// send address to client
					outToClient.writeBytes(strSocketServer + "\n");
				}

				// create client
				if (ss[0].equals("4")) {
					String strPort1 = ""; // address.port1.get(0).toString();
					String strPort2 = ""; // address.port2.get(0).toString();
					String strIp = ""; // address.ip.get(0);
					String strName = ""; // address.name.get(0);

					for (int i = 0; i < address.port1.size(); i++) {
						strPort1 = strPort1 + address.port1.get(i) + "-";
						strPort2 = strPort2 + address.port2.get(i) + "-";
						strIp = strIp + address.ip.get(i) + "-";
						strName = strName + address.name.get(i) + "-";
					}

					outToClient.writeBytes(strPort1 + "\n");
					outToClient.writeBytes(strPort2 + "\n");
					outToClient.writeBytes(strIp + "\n");
					outToClient.writeBytes(strName + "\n");
					System.out.println(strPort1);
					System.out.println(strPort2);
					System.out.println(strIp);
					System.out.println(strName);
					String[] sss = strName.split("-");
					System.out.println(sss.length);
					for (int i = 0; i < sss.length; i++) {
						System.out.println(sss[i]);
					}
				}
				// Remove address who participed the game
				if (ss[0].equals("5")) {
					int j = 0;
					for (j = 0; j < address.name.size(); j++) {
						if (ss[1].equals(address.name.get(j)))
							break;
					}
					address.port1.remove(j);
					address.port2.remove(j);
					address.ip.remove(j);
					address.name.remove(j);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
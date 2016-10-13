package caroClient;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SuccessLogin extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private DataInputStream inFromServer;
	private DataOutputStream outToServer;
	private String username;
	private Address address;

	private JLabel notificationSuccessLogin;
	private JComboBox gameServers;
	private JButton createGameServer;
	private JButton loadListOfGameServers;
	private JButton exit;
	
	private static final int SUCCESS_LOGIN_HEIGHT = 150;
	private static final int SUCCESS_LOGIN_WIDTH = 420;

	public SuccessLogin() {
		initComponents();
	}

	public SuccessLogin(DataInputStream inFromServer,
			DataOutputStream outToServer, String username) {
		initComponents();
		this.inFromServer = inFromServer;
		this.outToServer = outToServer;
		this.username = username;
	}

	public void initComponents() {

		notificationSuccessLogin = new JLabel("You have successfully logged in");
		createGameServer = new JButton("Create a game server");
		loadListOfGameServers = new JButton("Load list of game servers");
		gameServers = new JComboBox<String>();
		exit = new JButton("Exit");
		
		setLayout(new GridLayout(6, 1));
		
		JPanel gameFunctions = new JPanel();
		gameFunctions.setLayout(new GridLayout(1, 2));
		gameFunctions.add(loadListOfGameServers);
		gameFunctions.add(gameServers);
		
		add(notificationSuccessLogin);
		add(createGameServer);
		add(new JPanel());
		add(gameFunctions);
		add(new JPanel());
		add(exit);
		setSize(SUCCESS_LOGIN_WIDTH, SUCCESS_LOGIN_HEIGHT);
		setLocation(
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth())
						/ 2 - SUCCESS_LOGIN_WIDTH / 2, (int) (Toolkit
						.getDefaultToolkit().getScreenSize().getHeight())
						/ 2
						- SUCCESS_LOGIN_HEIGHT / 2);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		createGameServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createServerActionPerformed(e);
			}
		});

		loadListOfGameServers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadServerActionPerformed(e);
			}
		});

		gameServers.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				comServerItemStateChanged(e);
			}
		});

		gameServers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comServerActionPerformed(e);
			}
		});

		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	public void createServerActionPerformed(ActionEvent e) {
		// TODO add your handling code here:
		String createServer = "3-" + username;
		try {
			Enumeration<NetworkInterface> n = NetworkInterface
					.getNetworkInterfaces();
			while (n.hasMoreElements()) {
				NetworkInterface el = n.nextElement();
				Enumeration<InetAddress> a = el.getInetAddresses();
				while (a.hasMoreElements()) {
					InetAddress addr = a.nextElement();
					if(addr.getHostAddress().contains("."))
					{
						createServer = createServer + "-" + addr.getHostAddress();
						break;
					}
				}
				break;
			}
			
			// send data to server
			outToServer.writeBytes(createServer + "\n");
			// get data sent from server
			String serverInfos = inFromServer.readLine();
			String[] newServer = serverInfos.split("-");
			int port1 = Integer.parseInt(newServer[1]);
			int port2 = Integer.parseInt(newServer[2]);
			ServerCaroChess serverCaroChess = new ServerCaroChess(port1, port2,username);
		} catch (IOException ex) {
			Logger.getLogger(SuccessLogin.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void comServerItemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			int i = 0;
			for (i = 0; i < address.name.size(); i++) {
				if (e.getItem() == address.name.get(i))
					break;
			}
			ClientCaroChess clientCaro = new ClientCaroChess(address.ip.get(i),
					address.port1.get(i), address.port2.get(i), username);

			// remove game server chose
			String playGame = "5-" + e.getItem();
			try {
				// send to server
				outToServer.writeBytes(playGame + "\n");
			} catch (IOException ex) {
				Logger.getLogger(SuccessLogin.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
	}

	public void comServerActionPerformed(ActionEvent e) {

	}

	public void loadServerActionPerformed(ActionEvent e) {
		String playGame = "4-" + username;
		try {
			// send request to server
			outToServer.writeBytes(playGame + "\n");
			// get data from server
			String strPorts = inFromServer.readLine();
			String[] ports = strPorts.split("-");

			for (int i = 0; i < ports.length; i++)
				address.port1.add(Integer.parseInt(ports[i]));

			String _strPort2 = inFromServer.readLine();
			String[] _cutPort2 = _strPort2.split("-");

			for (int i = 0; i < _cutPort2.length; i++)
				address.port2.add(Integer.parseInt(_cutPort2[i]));

			String _strIp = inFromServer.readLine();
			String[] _cutIp = _strIp.split("-");

			for (int i = 0; i < _cutIp.length; i++)
				address.ip.add(_cutIp[i]);

			String strNames = inFromServer.readLine();
			String[] names = strNames.split("-");

			for (int i = 0; i < names.length; i++)
				address.name.add(names[i]);

		} catch (IOException ex) {
			Logger.getLogger(SuccessLogin.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		DefaultComboBoxModel model = new DefaultComboBoxModel();
		model.addElement("Choose enemy >>");
		for (int i = 0; i < address.name.size(); i++) {
			model.addElement(address.name.get(i));
		}
		gameServers.setModel(model);
	}
}

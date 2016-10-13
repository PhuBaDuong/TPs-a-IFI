package caroClient;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Client extends JFrame {

	private static final int CLIENT_HEIGHT = 100;
	private static final int CLIENT_WIDTH = 300;

	private JLabel serverIp;
	private JTextField serverIpText;
	private JLabel serverPort;
	private JTextField serverPortText;
	private JButton connect;
	private JButton cancel;

	public Client() {
		initComponents();
	}

	public void initComponents() {

		serverIp = new JLabel("Server IP");
		serverIpText = new JTextField();
		serverPort = new JLabel("Server Port");
		serverPortText = new JTextField();
		connect = new JButton("Connect");
		cancel = new JButton("Cancel");

		setLayout(new GridLayout(3, 2));
		add(serverIp);
		add(serverIpText);
		add(serverPort);
		add(serverPortText);
		add(connect);
		add(cancel);

		setTitle("Connect to server");
		setSize(CLIENT_WIDTH, CLIENT_HEIGHT);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth())
						/ 2 - CLIENT_WIDTH / 2, (int) (Toolkit
						.getDefaultToolkit().getScreenSize().getHeight())
						/ 2
						- CLIENT_HEIGHT / 2);
		setVisible(true);

		connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectToServer(e);
			}
		});

		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	public void connectToServer(ActionEvent e) {
		String serverIp = serverIpText.getText();
		int serverPort = Integer.parseInt(serverPortText.getText());
		Socket clientSocket;
		try {
			clientSocket = new Socket(serverIp, serverPort);
			System.out.println("Connect successfully!");
			System.out
					.println("InetAddress : " + clientSocket.getInetAddress());
			System.out.println("LocalSocketAddress : "
					+ clientSocket.getLocalSocketAddress());
			System.out.println("LocalPort : " + clientSocket.getLocalPort());
			System.out.println("LocalAddress : "
					+ clientSocket.getLocalAddress());
			// Create inputstream from keyboard

			DataInputStream inFromUser = new DataInputStream(System.in);
			// Create inputstream from server

			DataInputStream inFromServer = new DataInputStream(
					clientSocket.getInputStream());
			// Create inputstream to server

			DataOutputStream outToServer = new DataOutputStream(
					clientSocket.getOutputStream());

			// Login at server
			setVisible(false);
			Login login = new Login(inFromServer, outToServer);

		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
	}
}

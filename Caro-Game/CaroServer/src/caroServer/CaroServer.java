package caroServer;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class CaroServer extends JFrame {
	public static int port = 1100;

	private static final int SERVER_HEIGHT = 80;
	private static final int SERVER_WIDTH = 300;

	private JLabel serverPort;
	private JTextField serverPortText;
	private JButton create;
	private JButton cancel;

	public CaroServer() {
		initComponents();
	}

	public void initComponents() {
		serverPort = new JLabel("Server Port ");
		serverPortText = new JTextField();
		create = new JButton("Create");
		cancel = new JButton("Cancel");

		setLayout(new GridLayout(2, 2));
		add(serverPort);
		add(serverPortText);
		add(create);
		add(cancel);
		
		setTitle("Create Server");
		setSize(SERVER_WIDTH, SERVER_HEIGHT);
		setLocation(
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth())
						/ 2 - SERVER_WIDTH / 2, (int) (Toolkit
						.getDefaultToolkit().getScreenSize().getHeight())
						/ 2
						- SERVER_HEIGHT / 2);
		setVisible(true);

		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createServer(e);
			}
		});

		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

	}

	public void createServer(ActionEvent e) {
		int portServer = Integer.parseInt(serverPortText.getText());
		try {
			ServerSocket serverSocket = new ServerSocket(portServer);
			System.out.println("Starting server is successful");
			setVisible(false);
			while (true) {
				new ThreadSocket(serverSocket.accept(), port).start();
				System.out.println("Have a new connection");
				port = port + 2;
			}
		} catch (IOException ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
	}

	public static void main(String[] args) {
		CaroServer caroServer = new CaroServer();
	}
}

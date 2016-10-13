package caroClient;


import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private DataInputStream inFromServer;
	private DataOutputStream outToServer;
	private JLabel labelUsername;
	private JLabel labelPassword;
	private JTextField textFieldUsername;
	private JTextField textFieldPassword;
	private JButton login;
	private JButton register;
	private JButton exit;

	private static final int LOGIN_HEIGHT = 100;
	private static final int LOGIN_WIDTH = 300;

	public Login() {
		initComponents();
	}

	public Login(DataInputStream inFromServer, DataOutputStream outToServer) {
		initComponents();
		this.inFromServer = inFromServer;
		this.outToServer = outToServer;
	}

	public void initComponents() {

		labelUsername = new JLabel("Username");
		textFieldUsername = new JTextField();
		labelPassword = new JLabel("Password");
		textFieldPassword = new JTextField();

		login = new JButton("Login");
		register = new JButton("Register");
		exit = new JButton("Exit");

		GridLayout layout = new GridLayout(4, 1);
		setLayout(layout);
		JPanel loginNameInfos = new JPanel();
		JPanel loginPwdInfos = new JPanel();
		loginNameInfos.setLayout(new GridLayout(1, 2));
		loginPwdInfos.setLayout(new GridLayout(1, 2));

		loginNameInfos.add(labelUsername);
		loginNameInfos.add(textFieldUsername);
		loginPwdInfos.add(labelPassword);
		loginPwdInfos.add(textFieldPassword);

		JPanel functionButtons = new JPanel();
		functionButtons.setLayout(new GridLayout(1, 3));
		functionButtons.add(login);
		functionButtons.add(register);
		functionButtons.add(exit);

		add(loginNameInfos);
		add(loginPwdInfos);
		add(new JPanel());
		add(functionButtons);

		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginActionPerformed(e);
			}
		});

		register.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				registerActionPerformed(e);
			}
		});

		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		setTitle("Login");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth())
						/ 2 - LOGIN_WIDTH / 2, (int) (Toolkit
						.getDefaultToolkit().getScreenSize().getHeight())
						/ 2
						- LOGIN_HEIGHT / 2);
		setSize(LOGIN_WIDTH, LOGIN_HEIGHT);
		setVisible(true);
	}
	
	public void loginActionPerformed(ActionEvent e) {
		String username = textFieldUsername.getText();
		String password = textFieldPassword.getText();

		// Send message to server
		String loginInfos = "1-" + username + "-" + password;
		try {
			outToServer.writeBytes(loginInfos + "\n");
			// Read data responded from server
			String loginResult = inFromServer.readLine();

			if (loginResult.equals("1")) {
				SuccessLogin loginsuccess = new SuccessLogin(inFromServer,
						outToServer, textFieldUsername.getText());
				setVisible(false);
			} else {
				if (loginResult.equals("0")) {
					FailLogin loginfail = new FailLogin(inFromServer,
							outToServer);
					setVisible(false);
				}
			}
		} catch (IOException ex) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void registerActionPerformed(ActionEvent e) {
		Register register = new Register(inFromServer, outToServer);
		setVisible(false);
	}
}

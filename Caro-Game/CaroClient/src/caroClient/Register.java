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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Register extends JFrame {
	private DataInputStream inFromServer;
	private DataOutputStream outToServer;

	private JLabel labelUsername;
	private JLabel labelPassword;

	private JTextField textFieldUsername;
	private JTextField textFieldPwd;

	private JButton register;
	private JButton cancel;

	private static final int REGISTER_HEIGHT = 100;
	private static final int REGISTER_WIDTH = 300;

	public Register() {
		initComponents();
	}

	public Register(DataInputStream inFromServer, DataOutputStream outToServer) {
		initComponents();
		this.inFromServer = inFromServer;
		this.outToServer = outToServer;
	}

	private void initComponents() {

		labelUsername = new JLabel("Username");
		labelPassword = new JLabel("Password");

		textFieldUsername = new JTextField();
		textFieldPwd = new JTextField();

		register = new JButton("Register");
		cancel = new JButton("Cancel");

		GridLayout layout = new GridLayout(4, 1);
		setLayout(layout);

		JPanel nameInfos = new JPanel();
		JPanel pwdInfos = new JPanel();
		JPanel functionButtons = new JPanel();
		
		nameInfos.setLayout(new GridLayout(1, 2));
		pwdInfos.setLayout(new GridLayout(1, 2));
		functionButtons.setLayout(new GridLayout(1, 2));
		
		nameInfos.add(labelUsername);
		nameInfos.add(textFieldUsername);
		
		pwdInfos.add(labelPassword);
		pwdInfos.add(textFieldPwd);
		
		functionButtons.add(register);
		functionButtons.add(cancel);
		
		add(nameInfos);
		add(pwdInfos);
		add(new JPanel());
		add(functionButtons);

		register.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				verifyActionPerformed(e);
			}
		});

		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Register");
		setLocation(
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth())
						/ 2 - REGISTER_WIDTH / 2, (int) (Toolkit
						.getDefaultToolkit().getScreenSize().getHeight())
						/ 2
						- REGISTER_HEIGHT / 2);
		setSize(REGISTER_WIDTH, REGISTER_HEIGHT);
		setVisible(true);
	}

	public void verifyActionPerformed(ActionEvent e) {
		String resSend = "2-" + textFieldUsername.getText() + "-"
				+ textFieldPwd.getText();
		try {
			outToServer.writeBytes(resSend + "\n");
		} catch (IOException ex) {
			Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null,
					ex);
		}

		String result;
		try {
			result = inFromServer.readLine();
			if (result.equals("1")) {
				// a pop-up box
				JOptionPane.showMessageDialog(null,
						"You have registered successfully", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				setVisible(false);
				Login login = new Login(inFromServer, outToServer);
			} else {
				JOptionPane.showMessageDialog(null, "Register failed!",
						"Failed!!", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		} catch (IOException ex) {
			Logger.getLogger(Register.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}
}

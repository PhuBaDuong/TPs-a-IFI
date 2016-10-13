package caroClient;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FailLogin extends JFrame {
	private DataInputStream inFromServer;
	private DataOutputStream outToServer;

	private JLabel failLogin;
	private JLabel createNewAccOrNo;
	private JButton yesButton;
	private JButton noButton;
	
	private static final int FAIL_LOGIN_HEIGHT = 100;
	private static final int FAIL_LOGIN_WIDTH = 300;
	
	public FailLogin() {
		initComponents();
	}

	public FailLogin(DataInputStream inFromServer, DataOutputStream outToServer) {
		initComponents();
		this.inFromServer = inFromServer;
		this.outToServer = outToServer;
	}

	public void initComponents() {
		failLogin = new JLabel("Your login fail. This account don't exist.");
		createNewAccOrNo = new JLabel("Do you want to create a new acc?");

		yesButton = new JButton("Yes");
		noButton = new JButton("No");

		yesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				yesActionPerformed(e);
			}
		});

		noButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});

		setLayout(new GridLayout(3, 1));
		JPanel functionButtons = new JPanel();
		functionButtons.setLayout(new GridLayout(1, 2));
		functionButtons.add(yesButton);
		functionButtons.add(noButton);
		
		add(failLogin);
		add(createNewAccOrNo);
		add(functionButtons);
		
		setSize(FAIL_LOGIN_WIDTH, FAIL_LOGIN_HEIGHT);
		setLocation(
				(int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth())
						/ 2 - FAIL_LOGIN_WIDTH / 2, (int) (Toolkit
						.getDefaultToolkit().getScreenSize().getHeight())
						/ 2
						- FAIL_LOGIN_HEIGHT / 2);
		setVisible(true);
	}

	public void yesActionPerformed(ActionEvent e) {
		Register register = new Register(inFromServer, outToServer);
		setVisible(false);
	}

}

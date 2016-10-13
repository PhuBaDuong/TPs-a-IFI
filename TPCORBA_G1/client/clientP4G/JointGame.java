package clientP4G;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.omg.CORBA.ORB;

import serverP4G.Game;
import serverP4G.GameHelper;
import setting.Parameters;

public class JointGame extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel jButContainer = new JPanel(new GridLayout(1, 2));
	private JPanel jTextContainer = new JPanel(new GridLayout(1, 1));
	private JPanel jErrContainer = new JPanel(new GridLayout(1, 2));

	private JLabel jLabelJoiner = new JLabel("Paticipant name");
	private JTextField jTextJoiner = new JTextField(50);

	private JButton jButJoint = new JButton("Joint Game");
	private JButton jButCancel = new JButton("Cancel");

	private Game game;
	private String listGames[];

	JLabel jLabelError = new JLabel();

	public JointGame(Game game) {
		setTitle("Joint game");
		setVisible(true);
		setSize(Parameters.GAME_CREATION_WIDTH, Parameters.GAME_CREATION_HEIGHT);
		setLayout(new GridLayout(3, 1));
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		jTextContainer.add(jLabelJoiner);
		jTextContainer.add(jTextJoiner);

		jErrContainer.add(jLabelError);

		jButContainer.add(jButJoint);
		jButContainer.add(jButCancel);

		add(jTextContainer);
		add(jErrContainer);
		add(jButContainer);

		this.game = game;
		this.addActions();
	}

	public void addActions() {
		jButJoint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String joiner = jTextJoiner.getText();
				if (joiner == null || joiner.equals("")) {
					jLabelError.setText("Error : Player name is empty!");
				} else {
					game.connectGame(joiner);
					GameBoard gameBoard = new GameBoard(game, game.getPlayers().player2);
					setVisible(false);
				}
			}
		});

		jButCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setVisible(false);
			}
		});
	}
}

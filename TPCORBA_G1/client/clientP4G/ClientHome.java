package clientP4G;

import java.awt.BorderLayout;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import serverP4G.Game;
import serverP4G.GameServer;

public class ClientHome extends JFrame {

	private static final long serialVersionUID = 1L;

	public JPanel jButtonsContainerPanel = new JPanel();
	public JPanel jGameContainerPanel = new JPanel();
	public JLabel jGamesContainerLabel = new JLabel(new ImageIcon("images/puissance-4.png"));

	public JButton jCreationGameButton = new JButton("Create a new game");
	public JButton jJointGameButton = new JButton("Joint to the game");
	public JButton jListAvaiGameButton = new JButton("Available games");
	public JButton jListAllGameButton = new JButton("All games");
	public JButton jExit = new JButton("Quit");

	JComboBox<String> jComboBoxAvaiGames = new JComboBox<String>();
	JComboBox<String> jComboBoxAllGames = new JComboBox<String>();

	private GameServer gameServer;
	private Game game;
	private String listGames[];
	private String listOpenGames[];

	private String gameIdSelected;

	public ClientHome(GameServer gameServer) {
		setTitle("Client Home");
		setSize(setting.Parameters.HOME_FRAME_WIDTH, setting.Parameters.HOME_FRAME_HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addActions();
		setLayout(new BorderLayout());

		jButtonsContainerPanel.setLayout(new GridLayout(5, 1));
		jButtonsContainerPanel.add(jCreationGameButton);
		jButtonsContainerPanel.add(jJointGameButton);
		jJointGameButton.setEnabled(false);
		jButtonsContainerPanel.add(jListAvaiGameButton);
		jButtonsContainerPanel.add(jListAllGameButton);
		jButtonsContainerPanel.add(jExit);

		jGameContainerPanel.add(jGamesContainerLabel);
		add(jButtonsContainerPanel, BorderLayout.LINE_START);
		add(jGameContainerPanel, BorderLayout.CENTER);

		this.gameServer = gameServer;
		this.jComboBoxAvaiGames.addItem("");
		this.jComboBoxAllGames.addItem("");

	}

	public void addActions() {
		jCreationGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GameCreation gameCreation = new GameCreation(gameServer);
			}
		});

		jJointGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Client Home : Get game by using gameId = " + gameIdSelected);
				game = gameServer.getGame(gameIdSelected);
				JointGame jointGame = new JointGame(game);
				jJointGameButton.setEnabled(false);
			}
		});

		jListAvaiGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jJointGameButton.setEnabled(false);
				listOpenGames = gameServer.listOpenGames();
				jComboBoxAvaiGames.removeAllItems();
				jComboBoxAvaiGames.addItem("");
				for (int i = 0; i < listOpenGames.length; i++) {
					if (!listOpenGames[i].equals("")) {
						if (((DefaultComboBoxModel<String>) jComboBoxAvaiGames.getModel())
								.getIndexOf(listOpenGames[i]) == -1) {
							jComboBoxAvaiGames.addItem(listOpenGames[i]);
						}
					}
				}
				jGameContainerPanel.remove(jGamesContainerLabel);
				jGameContainerPanel.remove(jComboBoxAvaiGames);
				jGameContainerPanel.remove(jComboBoxAllGames);
				jGameContainerPanel.add(jComboBoxAvaiGames);
				setVisible(false);
				setVisible(true);
			}
		});

		jListAllGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("ClientHome : invokes listAllGame method");
				listGames = gameServer.listAllGames();
				jComboBoxAllGames.removeAllItems();
				jComboBoxAllGames.addItem("");
				for (int i = 0; i < listGames.length; i++) {
					if (!listGames[i].equals("")) {
						System.out.println(listGames[i]);
						if (((DefaultComboBoxModel<String>) jComboBoxAllGames.getModel())
								.getIndexOf(listGames[i]) == -1) {
							jComboBoxAllGames.addItem(listGames[i]);
						}
					}
				}
				jGameContainerPanel.remove(jGamesContainerLabel);
				jGameContainerPanel.remove(jComboBoxAvaiGames);
				jGameContainerPanel.remove(jComboBoxAllGames);
				jGameContainerPanel.add(jComboBoxAllGames);
				setVisible(false);
				setVisible(true);
			}
		});

		jExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		jComboBoxAvaiGames.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				System.out.println("ClientHome : Let choose a game id!");
				gameIdSelected = (String) jComboBoxAvaiGames.getSelectedItem();
				if (gameIdSelected != null && !gameIdSelected.equals("")) {
					jJointGameButton.setEnabled(true);
				}
			}
		});
	}
}

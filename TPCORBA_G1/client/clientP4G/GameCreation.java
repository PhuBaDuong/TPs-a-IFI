package clientP4G;

import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import serverP4G.Game;
import serverP4G.GameHelper;
import serverP4G.GameServer;
import setting.Parameters;

public class GameCreation extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel jButContainer = new JPanel(new GridLayout(1, 2));
	private JPanel jTextContainer = new JPanel(new GridLayout(1, 1));
	private JPanel jErrContainer = new JPanel(new GridLayout(1, 2));

	private JLabel jLabelCreator = new JLabel("User name");
	private JTextField jTextCreator = new JTextField(50);

	private JButton jButCreator = new JButton("Create Game");
	private JButton jButCancel = new JButton("Cancel");

	private GameServer gameServer;
	private Game game;
	private String listGames[];

	JLabel jLabelError = new JLabel();

	public GameCreation(GameServer gameServer) {
		setTitle("Create game");
		setVisible(true);
		setSize(Parameters.GAME_CREATION_WIDTH, Parameters.GAME_CREATION_HEIGHT);
		setLayout(new GridLayout(3, 1));
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		jTextContainer.add(jLabelCreator);
		jTextContainer.add(jTextCreator);

		jErrContainer.add(jLabelError);

		jButContainer.add(jButCreator);
		jButContainer.add(jButCancel);

		add(jTextContainer);
		add(jErrContainer);
		add(jButContainer);

		this.gameServer = gameServer;
		this.addActions();
	}

	public void addActions() {
		jButCreator.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String creator = jTextCreator.getText();
				if (creator == null || creator.equals("")) {
					jLabelError.setText("Error : Creator of game is empty!");
				} else {
					System.out.println("GameCreation : Player '" + creator + "' invoke createGame method on GameServer");
					gameServer.createGame(creator);
					listGames = gameServer.listAllGames();
					int index = 0;
//					System.out.println("GameCreation : List games available ");
//					while (index < listGames.length && !listGames[index].equals("")) {
//						System.out.print(listGames[index] + " ");
//						index++;
//					}
//					System.out.println();

					try {
						Properties props = new Properties();
						props.put("org.omg.CORBA.ORBInitialHost", Parameters.IP_HOST);
						props.put("org.omg.CORBA.ORBInitialPort", Parameters.PORT);
						props.put("com.sun.CORBA.giop.ORBGIOPVersion", "1.0");
						ORB orb = ORB.init(listGames, props);

						// get the root naming context
						org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
						// Use NamingContextExt instead of NamingContext. This
						// is
						// part of the Interoperable naming Service.
						NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

						// resolve the Object Reference in Naming
						String name = listGames[index == 0 ? index : index - 1];
						game = GameHelper.narrow(ncRef.resolve_str(name));

						// if this fails, a BAD_PARAM will be thrown
						if (game == null) {
							System.out.println("Game Creation Err : Can not create new game");
							setVisible(false);
						} else {
							// if this fails, a BAD_PARAM will be thrown
							GameBoard gameBoard = new GameBoard(game, game.getPlayers().player1);
							// close the game creation
							setVisible(false);
						}
					} catch (Exception ex) {
						System.out.println("ERROR Game Creation : " + ex.getMessage());
					}
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

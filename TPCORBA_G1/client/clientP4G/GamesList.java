package clientP4G;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import setting.Parameters;

public class GamesList extends JFrame {

	private JPanel openingGamesContainer;
	private JPanel allGamesContainer;

	private JButton jB_ListOpeningGames;
	private JButton jB_ListAllGames;

	public GamesList() {
		setTitle("Game List");
		setVisible(true);
		setSize(Parameters.L_FRAME_WIDTH, Parameters.L_FRAME_HEIGHT);
		setLayout(new BorderLayout());

		JPanel panelsContainer = new JPanel();
		JPanel buttonsContainer = new JPanel();

		panelsContainer.setSize(Parameters.L_FRAME_WIDTH, 400);
		buttonsContainer.setSize(Parameters.L_FRAME_WIDTH, 100);

		openingGamesContainer = new JPanel();
		openingGamesContainer.setBackground(Color.WHITE);
		openingGamesContainer.setSize(240, 400);
		allGamesContainer = new JPanel();
		allGamesContainer.setBackground(Color.WHITE);
		allGamesContainer.setSize(240, 400);
		
		jB_ListOpeningGames = new JButton("Opening Games");
		jB_ListAllGames = new JButton("All Games");

		panelsContainer.setLayout(new BorderLayout());
		panelsContainer.add(openingGamesContainer, BorderLayout.WEST);
		JPanel temp = new JPanel();
		temp.setSize(20, 400);
		panelsContainer.add(temp, BorderLayout.CENTER);
		panelsContainer.add(allGamesContainer, BorderLayout.EAST);

		buttonsContainer.add(jB_ListOpeningGames);
		buttonsContainer.add(jB_ListAllGames);

		add(panelsContainer, BorderLayout.CENTER);
		add(buttonsContainer, BorderLayout.SOUTH);
	}

	public static void main(String args[]) {
		GamesList gamesList = new GamesList();
	}
}

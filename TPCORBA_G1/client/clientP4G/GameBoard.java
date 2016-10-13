package clientP4G;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import serverP4G.Column;
import serverP4G.Game;
import serverP4G.GameState;
import serverP4G.ReturnCode;
import setting.Parameters;

public class GameBoard extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JFrame containner;
	private BufferedImage image;

	private Game game;
	private String currentPlayer;
	private ReturnCode returnCode;

	private JMenuBar jMenuBar;
	private JMenu fileMenu;
	private JMenuItem quitAction;

	private ActionListener taskPerformer;

	// public GameBoard(GameData gameData) {
	public GameBoard(Game game, String player) {
		this.setBackground(Color.blue);
		this.addActions();
		this.containner = new JFrame("Game Board " + player);
		this.containner.setSize(Parameters.FRAME_WIDTH, Parameters.FRAME_HEIGHT);
		this.containner.add(this);
		this.containner.setVisible(true);
		this.game = game;
		this.currentPlayer = player;

		this.jMenuBar = new JMenuBar();
		this.fileMenu = new JMenu("File");
		this.quitAction = new JMenuItem("quit");

		this.quitAction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane.showConfirmDialog(null, "Do you really want to quit game?", "Message",
						JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (response == JOptionPane.OK_OPTION) {
					System.out.println("GameBoard : Player " + currentPlayer + " want to quit the game!");
					boolean result = game.quitPlayer(currentPlayer);
					if (result == true) {
						response = JOptionPane.showConfirmDialog(null, "You quited", "Message",
								JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
						if (response == JOptionPane.OK_OPTION) {
							containner.setVisible(false);
							containner.dispose();
						}
					}
				}
			}
		});

		fileMenu.add(quitAction);
		jMenuBar.add(fileMenu);

		this.containner.setJMenuBar(jMenuBar);

		taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (game.getGameData().state == GameState.RUNNING || game.getGameData().state == GameState.TERMINATED) {
					repaint();
					if (returnCode == ReturnCode.WIN) {
						if (!game.ismyTurn(currentPlayer)) {
							int response = JOptionPane.showConfirmDialog(null, currentPlayer + " : You win!!!",
									"Message", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
							if (response == JOptionPane.OK_OPTION) {
								returnCode = ReturnCode.SUCCESS;
								game.quitPlayer(currentPlayer);
								containner.setVisible(false);
								containner.dispose();
							}
						}

					}
					if (returnCode == ReturnCode.SUCCESS && game.getGameData().state == GameState.TERMINATED) {
						if (game.ismyTurn(currentPlayer)) {
							int response = JOptionPane.showConfirmDialog(null, currentPlayer + " : You lose!!!",
									"Message", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
							if (response == JOptionPane.OK_OPTION) {
								game.quitPlayer(currentPlayer);
								containner.setVisible(false);
								containner.dispose();
							}
						}
						returnCode = ReturnCode.XNULL;
					}
				}
			}
		};

		new Timer(500, taskPerformer).start();
	}

	void drawLines(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		for (int x = 0; x <= Parameters.NUMBER_ROWS; x++) {
			g2d.drawLine(0, x * Parameters.CELL_WIDTH, Parameters.FRAME_WIDTH, x * Parameters.CELL_WIDTH);
		}
		for (int y = 0; y <= Parameters.NUMBER_COLUMNS; y++) {
			g2d.drawLine(y * Parameters.CELL_WIDTH, 0, y * Parameters.CELL_WIDTH, Parameters.FRAME_HEIGHT);
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		drawLines(g);
		for (int y = 0; y < Parameters.NUMBER_ROWS; y++) {
			for (int x = 0; x < Parameters.NUMBER_COLUMNS; x++) {
				try {
					image = ImageIO.read(new File("images/b_circle.png"));
				} catch (IOException ex) {
					System.out.println("ERROR " + ex.getMessage());
				}
				g.drawImage(image, x * Parameters.CELL_WIDTH, y * Parameters.CELL_WIDTH, null);
			}
		}
		for (int y = 0; y < Parameters.NUMBER_ROWS; y++) {
			for (int x = 0; x < Parameters.NUMBER_COLUMNS; x++) {
				try {
					image = ImageIO.read(new File(
							"images/" + game.getGameData().matrix[y * Parameters.NUMBER_COLUMNS + x] + "_circle.png"));
				} catch (IOException ex) {
					System.out.println("ERROR " + ex.getMessage());
				}
				g.drawImage(image, x * Parameters.CELL_WIDTH, y * Parameters.CELL_WIDTH, null);
			}
		}
	}

	public void addActions() {
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("GameBoard : check is " + currentPlayer + "'s turn");
				if (game.ismyTurn(currentPlayer)) {
					System.out.println("Gameboard : It is turn of " + currentPlayer);
					System.out.println("Gameboard : " + currentPlayer + " play at column "
							+ Column.from_int(e.getX() / Parameters.CELL_WIDTH));
					returnCode = game.play(currentPlayer, Column.from_int(e.getX() / Parameters.CELL_WIDTH));
					if (returnCode == ReturnCode.G_OPEN) {
						JOptionPane.showMessageDialog(null, "Not enough player!");
					}
					// repaint();
					else if (returnCode == ReturnCode.G_OVER) {
						int response = JOptionPane.showConfirmDialog(null, "Game over!!!", "Message",
								JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
						if (response == JOptionPane.OK_OPTION) {
							System.exit(0);
						}

					} else if (returnCode == ReturnCode.XNULL) {
						JOptionPane.showMessageDialog(null, "No space in this column for playing!");
					}
				} else {
					JOptionPane.showMessageDialog(null, "This is not your turn!");
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}

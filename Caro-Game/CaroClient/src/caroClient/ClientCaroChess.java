package caroClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

public class ClientCaroChess extends JFrame {
	private static final long serialVersionUID = 1L;
	private String host;
	private JLabel labelYou;
	private JLabel labelEnemy;
	private Vector<Point> checked = new Vector<Point>();

	private PlayingUser enemy;
	private PlayingUser you;

	private Socket socket = null;
	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	private Socket socketGame = null;
	private ObjectInputStream inGame = null;
	private ObjectOutputStream outGame = null;
	private boolean isPause = true;
	private boolean startUser = true;
	private boolean user = false;

	private JButton backButton;
	private JPanel boardPanel;
	private JEditorPane chatEditorPane;
	private JMenuItem exitMenuItem;
	private JMenu fileMenu;
	private JLabel labelOfYou;
	private JLabel labelOfEnemy;
	private JMenu menu;
	private JScrollPane jScrollPane;
	private JMenuBar menuBar;
	private ImagePanel panelOfYou;
	private JButton pauseButton;
	private JButton sendButton;
	private JTextField typingTextField;
	private ImagePanel panelOfEnemy;

	private int X0 = 20;
	private int Y0 = 20;
	private int Width = 32;
	private int Size = 15;
	private int currentRow = -1;
	private int currentColumn = -1;
	private Point currentPoint = new Point();

	public ClientCaroChess() {
		setVisible(true);
	}

	public ClientCaroChess(String ip, final Integer port1, final Integer port2,
			String name) {
		initComponents();
		setTitle(name + " (Client)");
		setVisible(true);
		host = ip;
		you = new PlayingUser(panelOfYou);
		enemy = new PlayingUser(panelOfEnemy);
		you.start();
		enemy.start();
		you.suspend();
		class Listen extends Thread {
			public Listen() {
				start();
			}

			@Override
			public void run() {
				listenSocket(port1);
			}
		}
		new Listen();
		class ListenGame extends Thread {
			public ListenGame() {
				start();
			}

			@Override
			public void run() {
				listenSocketGame(port2);
			}
		}
		new ListenGame();
	}

	public void initComponents() {
		backButton = new JButton();
		labelOfYou = new JLabel("Your turn");
		labelOfEnemy = new JLabel("Enemy's turn");
		panelOfYou = new ImagePanel(new ImageIcon(this.getClass().getResource(
				"/images/left.png")).getImage());
		panelOfEnemy = new ImagePanel(new ImageIcon(this.getClass()
				.getResource("/images/right.png")).getImage());
		typingTextField = new JTextField();
		sendButton = new JButton();
		pauseButton = new JButton();
		jScrollPane = new JScrollPane();
		chatEditorPane = new JEditorPane();
		menuBar = new JMenuBar();
		fileMenu = new JMenu();
		exitMenuItem = new JMenuItem();
		menu = new JMenu();

		boardPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				this.setOpaque(false);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				// Draw board chess
				g.setColor(Color.GRAY);
				for (int r = 0; r <= Size; r++) {
					g.drawLine(X0, Y0 + r * Width, X0 + Size * Width, Y0 + r
							* Width);
				}
				for (int c = 0; c <= Size; c++) {
					g.drawLine(X0 + c * Width, Y0, X0 + c * Width, Y0 + Size
							* Width);
				}

				// Draw cell when mouse point
				if (!isPause)
					if (currentColumn < Size && currentColumn >= 0
							&& currentRow < Size && currentRow >= 0) {
						g.setColor(new Color(255, 204, 204, 150));
						g2.fillOval(X0 + currentColumn * Width + Width / 6 + 1,
								Y0 + currentRow * Width + Width / 6 + 1,
								2 * Width / 3, 2 * Width / 3);
					}
				// Draw the position used
				if (checked.size() == 0)
					return;
				for (int p = 0; p < checked.size(); p++) {
					if (startUser) {
						if (p % 2 == 0)
							g2.setColor(Color.YELLOW);
						else
							g2.setColor(Color.GREEN);
					} else {
						if (p % 2 != 0)
							g2.setColor(Color.YELLOW);
						else
							g2.setColor(Color.GREEN);
					}
					g2.fillOval(X0 + checked.get(p).x * Width + Width / 6 + 1,
							Y0 + checked.get(p).y * Width + Width / 6 + 1,
							2 * Width / 3, 2 * Width / 3);
				}
				g.setColor(Color.RED);
				g.drawRect(checked.get(checked.size() - 1).x * Width + X0,
						checked.get(checked.size() - 1).y * Width + Y0, Width,
						Width);
				super.paintComponent(g);
			}
		};

		boardPanel
				.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
		boardPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				boardPanelMouseClicked(evt);
			}
		});

		boardPanel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(java.awt.event.MouseEvent evt) {
				boardPanelMouseMoved(evt);
			}
		});

		GroupLayout boardPanelLayout = new GroupLayout(boardPanel);
		boardPanel.setLayout(boardPanelLayout);
		boardPanelLayout.setHorizontalGroup(boardPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 533, Short.MAX_VALUE));
		boardPanelLayout.setVerticalGroup(boardPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 520,
				Short.MAX_VALUE));

		backButton.setText("Try againt");
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				backButtonActionPerformed(e);

			}
		});

		panelOfYou.setBackground(new Color(153, 255, 153));
		panelOfYou
				.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
		panelOfYou.setPreferredSize(new Dimension(150, 150));
		panelOfEnemy.setPreferredSize(new Dimension(150, 150));

		typingTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typingTextFieldActionPerformed(e);
			}
		});

		sendButton.setText("Send");
		sendButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sendButtonActionPerformed(evt);
			}
		});

		pauseButton.setText("Pause");
		pauseButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				pauseButtonActionPerformed(evt);
			}
		});
		jScrollPane.setViewportView(chatEditorPane);

		fileMenu.setText("File");
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_X,
				java.awt.event.InputEvent.ALT_MASK));
		exitMenuItem.setText("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitMenuItemActionPerformed(e);
			}
		});
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
		JPanel functionGames = new JPanel();
		JPanel functionButtons = new JPanel();
		functionGames.setLayout(new BorderLayout(10, 10));
		functionButtons.setLayout(new GridLayout(1, 2));
		functionGames.add(boardPanel, BorderLayout.NORTH);
		functionGames.add(jScrollPane, BorderLayout.CENTER);
		functionButtons.add(typingTextField);
		functionButtons.add(sendButton);
		functionButtons.add(backButton);
		functionGames.add(functionButtons, BorderLayout.SOUTH);

		panelOfYou.setMaximumSize(new Dimension(100, 100));
		panelOfYou.setMinimumSize(new Dimension(100, 100));
		add(panelOfYou, BorderLayout.WEST);
		add(functionGames, BorderLayout.CENTER);
		add(panelOfEnemy, BorderLayout.EAST);

		setSize(840, 680);

	}

	private void boardPanelMouseClicked(MouseEvent evt) {
		if (isPause) {
			return;
		}
		Point p = new Point();
		if ((currentColumn < Size && currentColumn >= 0 && currentRow < Size && currentRow >= 0)) {
			p = new Point(currentColumn, currentRow);
		} else {
			return;
		}
		if (!checked.contains(p)) {
			checked.add(p);
			currentPoint = new Point(p);
			currentColumn = -1;
			currentRow = -1;
			boardPanel.repaint();
			try {
				outGame.writeObject(currentPoint);// transmit information
			} catch (IOException ex) {
				Logger.getLogger(ClientCaroChess.class.getName()).log(
						Level.SEVERE, null, ex);
			}
			if (isWin(false)) {
				JOptionPane.showMessageDialog(this, "You win");
				startUser = true;
				checked.removeAllElements();
			}
			user = true;
			enemy.resume();
			you.suspend();
			panelOfYou.setBorder(new LineBorder(Color.YELLOW));
		}
		isPause = true;
	}

	private void boardPanelMouseMoved(MouseEvent evt) {
		int CX = evt.getX();
		int CY = evt.getY();
		if (CY - Y0 < 0) {
			currentColumn = (CX - X0) / Width;
			currentRow = -1 + (CY - Y0) / Width;
			return;
		}
		if (CX - X0 < 0) {
			currentColumn = -1 + (CX - X0) / Width;
			currentRow = (CY - Y0) / Width;
			return;
		}
		currentColumn = (CX - X0) / Width;
		currentRow = (CY - Y0) / Width;
		Point p = new Point(currentColumn, currentRow);
		if (checked.contains(p)) {
			currentColumn = -1;
			currentRow = -1;
		}
		boardPanel.repaint();
		boardPanel.validate();
	}

	private void exitMenuItemActionPerformed(ActionEvent evt) {
		this.dispose();
	}

	private void backButtonActionPerformed(ActionEvent e) {
		if (checked.size() == 0) {
			return;
		}
		if (JOptionPane.showConfirmDialog(this, "Do you want try again?",
				"Verify", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			user = !user;
			checked.remove(checked.size() - 1);
			boardPanel.repaint();
		}
	}

	private void sendButtonActionPerformed(ActionEvent evt) {
		String s = typingTextField.getText();
		if (s.length() == 0) {
			return;
		}
		try {
			Vector d = new Vector();
			d.add(s);
			out.writeObject(d);
		} catch (IOException ex) {
			Logger.getLogger(ClientCaroChess.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		chatEditorPane.setText(chatEditorPane.getText() + "Me : " + s + "\n");
		typingTextField.setText("");
	}

	private void pauseButtonActionPerformed(ActionEvent evt) {
		isPause = !isPause;
	}

	private void typingTextFieldActionPerformed(ActionEvent evt) {
		sendButtonActionPerformed(null);
	}

	public boolean isWin(boolean user) {
		int n = 6;
		int capacityOfWinner = 0;
		int numberDirectionBlocked = 0;
		int u;// u=0 is user 1; u=1 is user 2
		if (startUser) {
			u = user ? 0 : 1;
		} else {
			u = user ? 1 : 0;
		}
		// check horizontal
		for (int i = 1; i < n; i++) {
			Point p = new Point(currentPoint.x + i, currentPoint.y);
			if (!(p.x < Size)) {
				break;
			}
			if (checked.contains(p) && checked.indexOf(p) % 2 == u) {
				capacityOfWinner++;
			}
			if ((checked.contains(p) && checked.indexOf(p) % 2 != u)
					|| !checked.contains(p)) {
				if (checked.contains(p) && checked.indexOf(p) % 2 != u) {
					numberDirectionBlocked++;
				}
				break;
			}
		}
		for (int i = 1; i < n; i++) {
			Point p = new Point(currentPoint.x - i, currentPoint.y);
			if (!(p.x >= 0)) {
				break;
			}
			if (checked.contains(p) && checked.indexOf(p) % 2 == u) {
				capacityOfWinner++;
			}
			if ((checked.contains(p) && checked.indexOf(p) % 2 != u)
					|| !checked.contains(p)) {
				if (checked.contains(p) && checked.indexOf(p) % 2 != u) {
					numberDirectionBlocked++;
				}
				break;
			}
		}
		if (capacityOfWinner == 4 && numberDirectionBlocked != 2) {
			return true;
		}
		// check vertical
		capacityOfWinner = 0;
		numberDirectionBlocked = 0;
		for (int i = 1; i < n; i++) {
			Point p = new Point(currentPoint.x, currentPoint.y + i);
			if (!(p.y < Size)) {
				break;
			}
			if (checked.contains(p) && checked.indexOf(p) % 2 == u) {
				capacityOfWinner++;
			}
			if ((checked.contains(p) && checked.indexOf(p) % 2 != u)
					|| !checked.contains(p)) {
				if (checked.contains(p) && checked.indexOf(p) % 2 != u) {
					numberDirectionBlocked++;
				}
				break;
			}
		}
		for (int i = 1; i < n; i++) {
			Point p = new Point(currentPoint.x, currentPoint.y - i);
			if (!(p.y >= 0)) {
				break;
			}
			if (checked.contains(p) && checked.indexOf(p) % 2 == u) {
				capacityOfWinner++;
			}
			if ((checked.contains(p) && checked.indexOf(p) % 2 != u)
					|| !checked.contains(p)) {
				if (checked.contains(p) && checked.indexOf(p) % 2 != u) {
					numberDirectionBlocked++;
				}
				break;
			}
		}
		if (capacityOfWinner == 4 && numberDirectionBlocked != 2) {
			return true;
		}
		// check diagonal
		capacityOfWinner = 0;
		numberDirectionBlocked = 0;
		for (int i = 1; i < n; i++) {
			Point p = new Point(currentPoint.x + i, currentPoint.y + i);
			if (!(p.x >= 0 && p.x < Size && p.y >= 0 && p.y < Size)) {
				break;
			}
			if (checked.contains(p) && checked.indexOf(p) % 2 == u) {
				capacityOfWinner++;
			}
			if ((checked.contains(p) && checked.indexOf(p) % 2 != u)
					|| !checked.contains(p)) {
				if (checked.contains(p) && checked.indexOf(p) % 2 != u) {
					numberDirectionBlocked++;
				}
				break;
			}
		}
		for (int i = 1; i < n; i++) {
			Point p = new Point(currentPoint.x - i, currentPoint.y - i);
			if (!(p.x >= 0 && p.x < Size && p.y >= 0 && p.y < Size)) {
				break;
			}
			if (checked.contains(p) && checked.indexOf(p) % 2 == u) {
				capacityOfWinner++;
			}
			if ((checked.contains(p) && checked.indexOf(p) % 2 != u)
					|| !checked.contains(p)) {
				if (checked.contains(p) && checked.indexOf(p) % 2 != u) {
					numberDirectionBlocked++;
				}
				break;
			}
		}
		if (capacityOfWinner == 4 && numberDirectionBlocked != 2) {
			return true;
		}

		capacityOfWinner = 0;
		numberDirectionBlocked = 0;
		for (int i = 1; i < n; i++) {
			Point p = new Point(currentPoint.x + i, currentPoint.y - i);
			if (!(p.x >= 0 && p.x < Size && p.y >= 0 && p.y < Size)) {
				break;
			}
			if (checked.contains(p) && checked.indexOf(p) % 2 == u) {
				capacityOfWinner++;
			}
			if ((checked.contains(p) && checked.indexOf(p) % 2 != u)
					|| !checked.contains(p)) {
				if (checked.contains(p) && checked.indexOf(p) % 2 != u) {
					numberDirectionBlocked++;
				}
				break;
			}
		}
		for (int i = 1; i < n; i++) {
			Point p = new Point(currentPoint.x - i, currentPoint.y + i);
			if (!(p.x >= 0 && p.x < Size && p.y >= 0 && p.y < Size)) {
				break;
			}
			if (checked.contains(p) && checked.indexOf(p) % 2 == u) {
				capacityOfWinner++;
			}
			if ((checked.contains(p) && checked.indexOf(p) % 2 != u)
					|| !checked.contains(p)) {
				if (checked.contains(p) && checked.indexOf(p) % 2 != u) {
					numberDirectionBlocked++;
				}
				break;
			}
		}
		if (capacityOfWinner == 4 && numberDirectionBlocked != 2) {
			return true;
		}
		return false;
	}

	public void listenSocket(Integer port1) {
		// Create socket connection
		try {
			socket = new Socket(host, port1);
			OutputStream o = socket.getOutputStream();
			out = new ObjectOutputStream(o);
			InputStream i = socket.getInputStream();
			in = new ObjectInputStream(i);
		} catch (UnknownHostException e) {
			System.err.println("Server does not exist");
		} catch (IOException e) {
			System.err.println("Server does not open");
		}
		while (true) {
			try {
				Vector s = null;
				try {
					s = (Vector) in.readObject();
				} catch (ClassNotFoundException ex) {
					Logger.getLogger(ClientCaroChess.class.getName()).log(
							Level.SEVERE, null, ex);
				}
				this.toFront();
				chatEditorPane.setText(chatEditorPane.getText() + "Enemy : "
						+ s.get(0).toString() + "\n");
			} catch (IOException ex) {
			}
		}
	}

	public void listenSocketGame(Integer port2) {
		// Create socket connection
		try {
			socketGame = new Socket(host, port2);
			OutputStream o = socketGame.getOutputStream();
			outGame = new ObjectOutputStream(o);
			InputStream i = socketGame.getInputStream();
			inGame = new ObjectInputStream(i);
		} catch (UnknownHostException e) {
			System.err.println("Server doesn't exist");
		} catch (IOException e) {
			System.err.println("Server doesn't open");
		}
		while (true) {
			try {
				Point s = null;
				try {
					s = (Point) inGame.readObject();
					checked.add(s);
					currentPoint = s;
				} catch (ClassNotFoundException ex) {
					Logger.getLogger(ClientCaroChess.class.getName()).log(
							Level.SEVERE, null, ex);
				}
				this.toFront();
				boardPanel.repaint();
				if (isWin(true)) {
					JOptionPane.showMessageDialog(this, "You lose");
					startUser = false;
					checked.removeAllElements();
					boardPanel.repaint();
				}
				user = false;
				isPause = false;
				you.resume();
				enemy.suspend();
				panelOfEnemy.setBorder(new LineBorder(Color.YELLOW));
			} catch (IOException ex) {
			}
		}
	}
}

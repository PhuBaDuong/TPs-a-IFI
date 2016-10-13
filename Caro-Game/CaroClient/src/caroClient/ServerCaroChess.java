package caroClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

class AddClient implements Runnable {
	private JTextArea chatTextArea;
	private Socket client;

	public AddClient(Socket client, JTextArea chatTextArea) {
		this.client = client;
		this.chatTextArea = chatTextArea;
	}

	public void run() {
		String line;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);

		} catch (IOException e) {
			System.out.println("Accept failed!");
			System.exit(-1);
		}
		while (true) {
			try {
				line = in.readLine();
				chatTextArea.append("Client : " + line + "\n");
			} catch (IOException ex) {
			}
		}
	}
}

public class ServerCaroChess extends JFrame {

	private int portServerChat;
	private int portServerGame;
	private JButton backButton;
	private JPanel boardPanel;
	private JEditorPane chatEditorPane;
	private JMenuItem exitMenuItem;
	private JMenu fileMenu;
	private JLabel labelOfYou;
	private JLabel labelEnemy;
	private JMenu menu;
	private JScrollPane jScrollPane;
	private JMenuBar menuBar;
	private JButton sendButton;
	private JTextField typingTextField;
	private ImagePanel panelOfYou;
	private ImagePanel panelOfEnemy;

	private int X0 = 20;
	private int Y0 = 20;
	private int Width = 32;
	private int Size = 15;
	private int currentRow = -1;
	private int currentColumn = -1;
	private Point currentPoint = new Point();
	private boolean user = true;
	private Vector<Point> checked = new Vector<Point>();
	private PlayingUser enemy;
	private PlayingUser you;
	private ServerSocket server = null;
	private ServerSocket serverGame = null;

	private ObjectInputStream in = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream inGame = null;
	private ObjectOutputStream outGame = null;

	boolean isPause = false;
	boolean startUser = true;

	public ServerCaroChess(int portChatServer, int portGameServer, String name) {
		initComponents();
		setVisible(true);
		setTitle(name + " (Server)");
		this.portServerChat = portChatServer; // Port create server chat
		this.portServerGame = portGameServer; // Port create server game

		you = new PlayingUser(panelOfYou); // hightlight turn
		enemy = new PlayingUser(panelOfEnemy); // hightlight turn
		you.start();
		enemy.start();
		enemy.suspend();
		class Listen extends Thread {
			public Listen() {
				start();
			}

			@Override
			public void run() {
				listenSocket();
			}
		}
		new Listen();
		class ListenGame extends Thread {
			public ListenGame() {
				start();
			}

			@Override
			public void run() {
				listenSocketGame();
			}
		}
		new ListenGame();
	}

	private void initComponents() {
		boardPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				this.setOpaque(false);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				// draw the chess
				g.setColor(Color.GRAY);
				for (int r = 0; r <= Size; r++) {
					g.drawLine(X0, Y0 + r * Width, X0 + Size * Width, Y0 + r
							* Width);
				}
				for (int c = 0; c <= Size; c++) {
					g.drawLine(X0 + c * Width, Y0, X0 + c * Width, Y0 + Size
							* Width);
				}

				// mouse hover
				if (!isPause)
					if (currentColumn < Size && currentColumn >= 0
							&& currentRow < Size && currentRow >= 0) {
						g.setColor(new Color(0, 0, 0, 80));
						g2.fillOval(X0 + currentColumn * Width + Width / 6 + 1,
								Y0 + currentRow * Width + Width / 6 + 1,
								2 * Width / 3, 2 * Width / 3);
					}

				// The positions used
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

				// Mark a new cell
				g.setColor(Color.RED);
				g.drawRect(checked.get(checked.size() - 1).x * Width + X0,
						checked.get(checked.size() - 1).y * Width + Y0, Width,
						Width);
				super.paintComponent(g);
			}
		};
		backButton = new JButton("Try againt");
		labelOfYou = new JLabel("Your turn");
		labelEnemy = new JLabel("Enemy's turn");
		panelOfYou = new ImagePanel(new ImageIcon(this.getClass().getResource(
				"/images/left.png")).getImage());
		panelOfEnemy = new ImagePanel(new ImageIcon(this.getClass()
				.getResource("/images/right.png")).getImage());
		typingTextField = new JTextField();
		sendButton = new JButton("Send");
		jScrollPane = new JScrollPane();
		chatEditorPane = new JEditorPane();
		menuBar = new JMenuBar();
		fileMenu = new JMenu();
		exitMenuItem = new JMenuItem();
		menu = new JMenu();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		boardPanel
				.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
		boardPanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				boardPanelMouseClicked(e);
			}
		});
		boardPanel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent evt) {
				boardPanelMouseMoved(evt);
			}
		});

		GroupLayout boardPanelLayout = new GroupLayout(boardPanel);
		boardPanel.setLayout(boardPanelLayout);
		boardPanelLayout.setHorizontalGroup(boardPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0,
						521, Short.MAX_VALUE));
		boardPanelLayout.setVerticalGroup(boardPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGap(0, 523, Short.MAX_VALUE));

		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				backButtonActionPerformed(e);
			}
		});

		labelOfYou.setFont(new Font("Tahoma", 3, 14));
		panelOfYou.setBackground(new Color(204, 255, 153));
		panelOfYou
				.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
		panelOfYou.setPreferredSize(new Dimension(150, 150));
		panelOfEnemy.setPreferredSize(new Dimension(150, 150));

		typingTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				typingTextFieldActionPerformed(evt);
			}
		});

		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sendButtonActionPerformed(evt);
			}
		});

		chatEditorPane.setFont(new Font("Tahoma", 3, 11));
		jScrollPane.setViewportView(chatEditorPane);

		fileMenu.setText("File");
		exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				java.awt.event.InputEvent.ALT_MASK));
		exitMenuItem.setText("Exit");
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});

		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);

		setLayout(new BorderLayout(10, 10));
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
				outGame.writeObject(currentPoint); // transmit information
			} catch (IOException ex) {
				Logger.getLogger(ServerCaroChess.class.getName()).log(
						Level.SEVERE, null, ex);
			}
			if (isWin(true)) {
				JOptionPane.showMessageDialog(this, "You win!");
				checked.removeAllElements();
				startUser = false;
			}
			user = false;
			you.suspend();
			enemy.resume();

			panelOfYou.setBorder(new LineBorder(Color.YELLOW));
		}
		isPause = true;
		boardPanel.repaint();
	}

	private void boardPanelMouseMoved(MouseEvent evt) {

		int currentX = evt.getX();
		int currentY = evt.getY();
		if (currentY - Y0 < 0) {
			currentColumn = (currentX - X0) / Width;
			currentRow = -1 + (currentY - Y0) / Width;
			return;
		}
		if (currentX - X0 < 0) {
			currentColumn = -1 + (currentX - X0) / Width;
			currentRow = (currentY - Y0) / Width;
			return;
		}
		currentColumn = (currentX - X0) / Width;
		currentRow = (currentY - Y0) / Width;
		Point p = new Point(currentColumn, currentRow);
		if (checked.contains(p)) {
			currentColumn = -1;
			currentRow = -1;
		}
		boardPanel.repaint();
		boardPanel.validate();
	}

	private void exitMenuItemActionPerformed(ActionEvent e) {
		this.dispose();
	}

	private void backButtonActionPerformed(ActionEvent e) {
		if (checked.size() == 0) {
			return;
		}
		if (JOptionPane.showConfirmDialog(this, "You want to try againt?",
				"Verify", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			user = !user;
			checked.remove(checked.size() - 1);
			boardPanel.repaint();
		}
	}

	private void sendButtonActionPerformed(ActionEvent evt) {
		try {
			String s = typingTextField.getText();
			if (s.length() == 0) {
				return;
			}
			Vector<String> d = new Vector<String>();
			d.add(s);

			out.writeObject(d);
			chatEditorPane.setText(chatEditorPane.getText() + "Me : " + s
					+ "\n");

			typingTextField.setText("");
		} catch (IOException ex) {
			Logger.getLogger(ServerCaroChess.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	private void typingTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
		sendButtonActionPerformed(null);
	}

	public boolean isWin(boolean user) {
		int n = 6;
		int capacityOfWinner = 0;
		int numberDirectionBlocked = 0;
		int u;
		if (startUser) {
			u = user ? 0 : 1;
		} else {
			u = user ? 1 : 0;
		}
		// Check vertical
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

		// Check horizontal
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

	public void listenSocket() {

		try {
			server = new ServerSocket(portServerChat);
			Socket socket = server.accept();
			OutputStream o = socket.getOutputStream();
			out = new ObjectOutputStream(o);
			InputStream i = socket.getInputStream();
			in = new ObjectInputStream(i);
		} catch (IOException e) {
			System.out.println("Could not listen on port " + portServerChat);
			System.exit(-1);
		}

		while (true) {
			try {
				Vector s = null;
				try {
					s = (Vector) in.readObject();
				} catch (ClassNotFoundException ex) {
					Logger.getLogger(ServerCaroChess.class.getName()).log(
							Level.SEVERE, null, ex);
				}
				this.toFront();
				chatEditorPane.setText(chatEditorPane.getText() + "Enemy : "
						+ s.get(0).toString() + "\n");
			} catch (IOException ex) {
			}
		}
	}

	public void listenSocketGame() {
		try {
			serverGame = new ServerSocket(portServerGame);
			Socket socket = serverGame.accept();
			OutputStream o = socket.getOutputStream();
			outGame = new ObjectOutputStream(o);
			InputStream i = socket.getInputStream();
			inGame = new ObjectInputStream(i);
		} catch (IOException e) {
			System.out.println("Could not listen on port " + portServerGame);
			System.exit(-1);
		}

		while (true) {
			try {
				Point s = null;
				try {
					s = (Point) inGame.readObject();
					checked.add(s);
					currentPoint = s;
				} catch (ClassNotFoundException ex) {
					Logger.getLogger(ServerCaroChess.class.getName()).log(
							Level.SEVERE, null, ex);
				}
				this.toFront();
				boardPanel.repaint();
				if (isWin(false)) {
					JOptionPane.showMessageDialog(this, "You lose!");
					startUser = true;
					checked.removeAllElements();
					boardPanel.repaint();
				}
				user = true;
				isPause = false;
				enemy.suspend();
				you.resume();
				panelOfEnemy.setBorder(new LineBorder(Color.YELLOW));
			} catch (IOException ex) {
			}
		}
	}
}

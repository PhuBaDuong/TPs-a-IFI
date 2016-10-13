package environment;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import utils.Paramaters;

public class Board extends JPanel implements ActionListener, Paramaters {

	private static final long serialVersionUID = 1L;
	private Image backgroundImage;
	private UserAircraft userAircraft;
	private List<EnemyAircraft> enemyAircrafts;
	private Timer timer;

	public Board(UserAircraft userAircraft, List<EnemyAircraft> enemyAircrafts) {
		addKeyListener(new TAdapter());
		setFocusable(true);
		ImageIcon imageIconBG = new ImageIcon(IMAGE_PATH + BACK_GROUND_IMAGE
				+ EXTENSION);
		backgroundImage = imageIconBG.getImage();
		this.userAircraft = userAircraft;
		this.enemyAircrafts = new ArrayList<EnemyAircraft>();
		for (int i = 0; i < enemyAircrafts.size(); i++)
			this.enemyAircrafts.add(enemyAircrafts.get(i));
		timer = new Timer(INTERVAL_TIME, this);
		timer.start();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.drawImage(backgroundImage, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, this);
		if (userAircraft.isVisible())
			g2D.drawImage(userAircraft.getImage(), userAircraft.getX(),
					userAircraft.getY(), AIRCRAFT_WIDTH, AIRCRAFT_HEIGHT, this);

		for (int j = 0; j < LIST_OF_ENEMY_AIRCRAFTS.length; j++) {
			EnemyAircraft eA = enemyAircrafts.get(j);
			if (eA.isVisible())
				g2D.drawImage(eA.getImageEnemyAircraft(), eA.getX(), eA.getY(),
						AIRCRAFT_WIDTH, AIRCRAFT_HEIGHT, this);
		}

		ArrayList<Missile> ms = userAircraft.getMissiles();
		for (int i = 0; i < ms.size(); i++) {
			Missile m = (Missile) ms.get(i);
			g2D.drawImage(m.getImage(), m.getX(), m.getY(), MISSILE_WIDTH,
					MISSILE_HEIGHT, this);
		}
		for (int j = 0; j < enemyAircrafts.size(); j++) {
			ArrayList<Missile> ems = enemyAircrafts.get(j).getMissiles();
			for (int i = 0; i < ems.size(); i++) {
				Missile em = (Missile) ems.get(i);
				g2D.drawImage(em.getImage(), em.getX(), em.getY(),
						MISSILE_WIDTH, MISSILE_HEIGHT, this);
			}
		}

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	private class TAdapter extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			userAircraft.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {
			userAircraft.keyPressed(e);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList<Missile> ms = userAircraft.getMissiles();

		// changer missile d'enemy avion
		for (int j = 0; j < enemyAircrafts.size(); j++) {
			ArrayList<Missile> ems = enemyAircrafts.get(j).getMissiles();
			for (int i = 0; i < ems.size(); i++) {
				Missile em = (Missile) ems.get(i);
				if (em.isVisible())
					em.move();
				else
					ems.remove(i);
			}
		}
		// changer missile d'avion d'utilisateur
		for (int i = 0; i < ms.size(); i++) {
			Missile m = (Missile) ms.get(i);
			if (m.isVisible())
				m.move();
			else
				ms.remove(i);
		}

		checkCollisions();
		if (userAircraft.canMove())
			userAircraft.move();
		repaint();
	}

	public void checkCollisions() {
		ArrayList<Missile> ms = userAircraft.getMissiles();
		// check missile of userAircraft to enemyAircraft
		for (Missile missile : ms) {
			Rectangle rM = missile.getBounds();
			for (EnemyAircraft enemyAircraft : enemyAircrafts) {
				Rectangle eA = enemyAircraft.getBounds();
				if (missile.isVisible() && enemyAircraft.isVisible()
						&& rM.intersects(eA)) {
					enemyAircraft.setVisible(false);
					missile.setVisible(false);
				}
			}
		}
		// check missile of enemyAircraft to userAircraft
		for (int j = 0; j < enemyAircrafts.size(); j++) {
			ArrayList<Missile> ems = enemyAircrafts.get(j).getMissiles();
			for (Missile missile : ems) {
				Rectangle rM = missile.getBounds();
				Rectangle uA = userAircraft.getBounds();
				if (missile.isVisible() && userAircraft.isVisible()
						&& rM.intersects(uA)) {
					userAircraft.setVisible(false);
					missile.setVisible(false);
				}
			}
		}
	}
}

package environment;

import jade.core.Agent;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import utils.Paramaters;

public class Missile implements Paramaters {

	private static final long serialVersionUID = 1L;

	private int positionX;
	private int positionY;

	private Image imageMissile;
	private String direction;
	private boolean visible;

	public Missile(int x, int y, String direction) {
		ImageIcon ii = new ImageIcon(IMAGE_PATH + MISSILE_NAME + EXTENSION);
		imageMissile = ii.getImage();
		this.positionX = x;
		this.positionY = y;
		this.direction = direction;
		this.visible = true;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Image getImage() {
		return imageMissile;
	}

	public int getX() {
		return positionX;
	}

	public int getY() {
		return positionY;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void move() {
		if (direction.equals(DIRECTION_RIGHT))
			positionX += MISSILE_SPEED;
		else if (direction.equals(DIRECTION_LEFT))
			positionX -= MISSILE_SPEED;
		else if (direction.equals(DIRECTION_DOWN))
			positionY += MISSILE_SPEED;
		else if (direction.equals(DIRECTION_UP))
			positionY -= MISSILE_SPEED;
		if (positionX > FRAME_WIDTH || positionX < 0
				|| positionY > FRAME_HEIGHT || positionY < 0)
			visible = false;
	}

	public Rectangle getBounds() {
		return new Rectangle(this.positionX, this.positionY, MISSILE_WIDTH,
				MISSILE_HEIGHT);
	}
}


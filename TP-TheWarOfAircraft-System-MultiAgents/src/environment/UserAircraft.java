package environment;

import jade.core.AID;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import utils.Paramaters;

public class UserAircraft extends Agent implements Paramaters {

	private static final long serialVersionUID = 1L;

	private int positionX;
	private int positionY;
	private int dx;
	private int dy;
	private String aircraftName;
	private String direction;
	private boolean visible;

	protected ArrayList<Missile> missiles;
	private Image imageAircraft;

	public UserAircraft() {
		aircraftName = USER_AIRCRAFT_NAME;
		direction = DEFAUT_USER_DIRECTION;
		positionX = INITIAL_POSITION_X_USER;
		positionY = INITIAL_POSITION_Y_USER;
		this.missiles = new ArrayList<Missile>();
		this.visible = true;
		ImageIcon imageIcon = new ImageIcon(IMAGE_PATH + getAircraftName()
				+ getDirection() + EXTENSION);
		imageAircraft = imageIcon.getImage();
	}

	public void setup() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Utilisateur");
		sd.setName("JADE-sending");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		addBehaviour(new TickerBehaviour(this, 100) {
			@Override
			protected void onTick() {
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("Enemy");
				template.addServices(sd);
				DFAgentDescription[] result;
				try {
					result = DFService.search(myAgent, template);
					ACLMessage cfp = new ACLMessage(ACLMessage.REQUEST);
					for(int i =0 ; i < result.length; i++) {
//					if (result.length >= 0) {
						AID receiverName = (AID) result[i].getName();
						cfp.addReceiver(receiverName);
						try {
							cfp.setContentObject(new CurrentPosition(getX(),
									getY(), getDirection(), isVisible()));
							cfp.setConversationId("request");
							cfp.setReplyWith("cfp" + System.currentTimeMillis());
							myAgent.send(cfp);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (FIPAException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public boolean canMove() {
		if (getDirection() == null)
			return true;
		else if (getX() < 0 && getDirection().equals(DIRECTION_LEFT))
			return false;
		else if (getX() > FRAME_WIDTH - AIRCRAFT_WIDTH
				&& getDirection().equals(DIRECTION_RIGHT))
			return false;
		else if (getY() < 0 && getDirection().equals(DIRECTION_UP))
			return false;
		else if (getY() > FRAME_HEIGHT - AIRCRAFT_HEIGHT
				&& getDirection().equals(DIRECTION_DOWN))
			return false;
		return true;
	}

	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_1) {
			setVisible(true);
		}

		if (key == KeyEvent.VK_LEFT) {
			setDirection("_left");
			setImage(getAircraftName(), getDirection());
			setDx(-1);
		}

		if (key == KeyEvent.VK_RIGHT) {
			setDirection("_right");
			setImage(getAircraftName(), getDirection());
			setDx(1);
		}

		if (key == KeyEvent.VK_UP) {
			setDirection("_up");
			setImage(getAircraftName(), getDirection());
			setDy(-1);
		}

		if (key == KeyEvent.VK_DOWN) {
			setDirection("_down");
			setImage(getAircraftName(), getDirection());
			setDy(1);
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_SPACE) {
			if(isVisible())
				fire();
		}

		if (key == KeyEvent.VK_LEFT) {
			setDx(0);
		}

		if (key == KeyEvent.VK_RIGHT) {
			setDx(0);
		}

		if (key == KeyEvent.VK_UP) {
			setDy(0);
		}

		if (key == KeyEvent.VK_DOWN) {
			setDy(0);
		}
	}

	public void fire() {
		if (getDirection().equals(DIRECTION_RIGHT))
			missiles.add(new Missile(getX() + AIRCRAFT_WIDTH, getY()
					+ AIRCRAFT_HEIGHT / 2 - MISSILE_HEIGHT / 2, DIRECTION_RIGHT));
		if (getDirection().equals(DIRECTION_LEFT))
			missiles.add(new Missile(getX(), getY() + AIRCRAFT_HEIGHT / 2
					- MISSILE_HEIGHT / 2, DIRECTION_LEFT));
		if (getDirection().equals(DIRECTION_DOWN))
			missiles.add(new Missile(getX() + AIRCRAFT_WIDTH / 2
					- MISSILE_WIDTH / 2, getY() + AIRCRAFT_HEIGHT,
					DIRECTION_DOWN));
		if (getDirection().equals(DIRECTION_UP))
			missiles.add(new Missile(getX() + AIRCRAFT_WIDTH / 2
					- MISSILE_WIDTH / 2, getY(), DIRECTION_UP));
	}

	public void setImage(String airCraftName, String direction) {
		ImageIcon imageIcon = new ImageIcon(IMAGE_PATH + airCraftName
				+ direction + EXTENSION);
		this.imageAircraft = imageIcon.getImage();
	}

	public Image getImage() {
		return imageAircraft;
	}

	public ArrayList<Missile> getMissiles() {
		return missiles;
	}

	public void setMissiles(ArrayList<Missile> missiles) {
		this.missiles = missiles;
	}

	public String getAircraftName() {
		return aircraftName;
	}

	public void setAircraftName(String aircraftName) {
		this.aircraftName = aircraftName;
	}

	public int getX() {
		return positionX;
	}

	public void setX(int x) {
		this.positionX = x;
	}

	public int getY() {
		return positionY;
	}

	public void setY(int y) {
		this.positionY = y;
	}

	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void move() {
		if(canMove()){
			positionX += dx;
			positionY += dy;
		}
	}

	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), AIRCRAFT_WIDTH, AIRCRAFT_HEIGHT);
	}
}

class CurrentPosition implements Serializable {
	int positionX;
	int positionY;
	String direction;
	boolean isVisible;
	
	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public CurrentPosition(int positionX, int positionY, String direction, boolean visible) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.direction = direction;
		this.isVisible = visible;

	}
}


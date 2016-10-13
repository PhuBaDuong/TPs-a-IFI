package environment;


import java.util.List;

import javax.swing.JFrame;

import utils.Paramaters;

public class Space extends JFrame implements Paramaters {

	private static final long serialVersionUID = 1L;
		
	public Space(UserAircraft userAircraft, List<EnemyAircraft> enemyAircrafts) {
		add(new Board(userAircraft, enemyAircrafts));
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setVisible(true);
	}
}


package environment;

import java.util.ArrayList;
import java.util.List;

import utils.Paramaters;
import jade.core.Agent;

public class Application extends Agent implements Paramaters {

	private static final long serialVersionUID = 1L;

	private UserAircraft userAircraft;
	private List<EnemyAircraft> enemyAircrafts;
	
	public void setup() {
		userAircraft = new UserAircraft();
		enemyAircrafts = new ArrayList<EnemyAircraft>();
		for (int i = 0; i < LIST_OF_ENEMY_AIRCRAFTS.length; i++)
			enemyAircrafts.add(new EnemyAircraft(LIST_OF_ENEMY_AIRCRAFTS[i][0],
					LIST_OF_ENEMY_AIRCRAFTS[i][1]));

		try {
			getContainerController().acceptNewAgent("userAircraft", userAircraft).start();
			for (int i = 0; i < LIST_OF_ENEMY_AIRCRAFTS.length; i++) {
				getContainerController().acceptNewAgent("enemy" + i,
						enemyAircrafts.get(i)).start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Space space = new Space(userAircraft, enemyAircrafts);
	}
}


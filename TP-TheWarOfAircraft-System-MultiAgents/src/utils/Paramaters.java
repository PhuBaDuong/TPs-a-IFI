package utils;

public interface Paramaters {

	public static final int INTERVAL_TIME_CHANGING_DIRECTION = 100;	
	public static final int EFFECT_RADIUS = 200;	
	
	public static final int FRAME_WIDTH = 1000;
	public static final int FRAME_HEIGHT = 562;

	public static final int INTERVAL_TIME = 5;
	public static final int MISSILE_SPEED = 2;

	public static final int AIRCRAFT_WIDTH = 40;
	public static final int AIRCRAFT_HEIGHT = 40;

	public static final int MISSILE_WIDTH = 5;
	public static final int MISSILE_HEIGHT = 5;

	public static final String IMAGE_PATH = "images/";
	public static final String EXTENSION = ".png";
	
	public static final String BACK_GROUND_IMAGE = "space";

	public static final String DIRECTION_LEFT = "_left";
	public static final String DIRECTION_RIGHT = "_right";
	public static final String DIRECTION_UP = "_up";
	public static final String DIRECTION_DOWN = "_down";
	
	public static final String USER_AIRCRAFT_NAME = "userAircraft";
	public static final String DEFAUT_USER_DIRECTION = "_up";
	public static final int INITIAL_POSITION_X_USER = FRAME_WIDTH/2 - AIRCRAFT_WIDTH/2;
	public static final int INITIAL_POSITION_Y_USER = FRAME_HEIGHT - AIRCRAFT_HEIGHT;
	
	public static final String ENEMY_AIRCRAFT_NAME = "enemyAircraft";
	public static final String DEFAUT_ENEMY_DIRECTION = "_down";
	public static final int[][] LIST_OF_ENEMY_AIRCRAFTS = { { 0, 0 },
		{ FRAME_WIDTH - AIRCRAFT_WIDTH, 0 },
		{ FRAME_WIDTH / 2 - AIRCRAFT_WIDTH / 2, 0 } };
	
	public static final String MISSILE_NAME = "missile";
}


package setting;

public interface Parameters {
//	public static final String IP_HOST = "192.168.1.115";
	public static final String IP_HOST = "127.0.0.1";
	public static final String PORT = "1050";

	// Client Home
	public static final int HOME_FRAME_HEIGHT = 200;
	public static final int HOME_FRAME_WIDTH = 380;

	// GameBoard
	public static final int FRAME_HEIGHT = 320;
	public static final int FRAME_WIDTH = 350;
	public static final int NUMBER_COLUMNS = 7;
	public static final int NUMBER_ROWS = 6;
	public static final int CELL_WIDTH = FRAME_WIDTH / NUMBER_COLUMNS;

	// Game list
	public static final int L_FRAME_HEIGHT = 500;
	public static final int L_FRAME_WIDTH = 500;

	public static final int NUMBER_OF_CELLS = 42;
	public static final int MAX_GAME_NUMBER = 10;

	// Game creation
	public static final int GAME_CREATION_HEIGHT = 100;
	public static final int GAME_CREATION_WIDTH = 380;
}

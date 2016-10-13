package server;

import serverP4G.Column;
import serverP4G.GameData;
import serverP4G.GamePOA;
import serverP4G.GameState;
import serverP4G.Players;
import serverP4G.ReturnCode;
import setting.Parameters;

public class GameImpl extends GamePOA {

	private String gameId;
	private GameData gameData;
	private Players players;
	private ReturnCode returnCode;
	private boolean isCreatorQuit;

	private int turn;

	public GameImpl(String player1) {
		char matrix[] = new char[Parameters.NUMBER_OF_CELLS];
		for (int i = 0; i < Parameters.NUMBER_OF_CELLS; i++)
			matrix[i] = 'b'; // black_circle

		this.gameId = String.valueOf(System.currentTimeMillis());
		this.gameData = new GameData(GameState.CREATED, matrix);
		this.players = new Players(player1, "");
		this.setReturnCode(ReturnCode.G_OPEN);
		turn = 0;
		isCreatorQuit = false;
		System.out.println("GameImpl : " + this.players.player1 + this.gameId);

	}

	public ReturnCode getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(ReturnCode returnCode) {
		this.returnCode = returnCode;
	}

	public int getTurn() {
		return turn;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public void setGameData(GameData gameData) {
		this.gameData = gameData;
	}

	public void setPlayers(Players players) {
		this.players = players;
	}

	public boolean isWinner(String player, int row, int col) {
		// check vertical win
		if (row <= 2) {
			StringBuilder verticalResult = new StringBuilder("");
			for (int i = 0; i < 4; i++) {
				verticalResult.append(this.getGameData().matrix[(row + i) * Parameters.NUMBER_COLUMNS + col]);
			}
			if (verticalResult.toString().contains("yyyy") || verticalResult.toString().contains("rrrr")) {
				return true;
			}
		}
		// check horizontal win
		StringBuilder horizontalResult = new StringBuilder("");
		for (int i = 0; i < Parameters.NUMBER_COLUMNS; i++) {
			horizontalResult.append(this.getGameData().matrix[row * Parameters.NUMBER_COLUMNS + i]);
		}
		if (horizontalResult.toString().contains("yyyy") || horizontalResult.toString().contains("rrrr")) {
			return true;
		}

		// Check diagonal win from left to right
		StringBuilder diagonalStr = new StringBuilder("");
		diagonalStr.append(this.gameData.matrix[row * Parameters.NUMBER_COLUMNS + col]);
		int row_temp = row;
		int col_temp = col;
		while (--row_temp >= 0 && --col_temp >= 0) {
			diagonalStr.append(this.gameData.matrix[row_temp * Parameters.NUMBER_COLUMNS + col_temp]);
		}
		diagonalStr.reverse();
		row_temp = row;
		col_temp = col;
		while (++row_temp < Parameters.NUMBER_ROWS && ++col_temp < Parameters.NUMBER_COLUMNS) {
			diagonalStr.append(this.gameData.matrix[row_temp * Parameters.NUMBER_COLUMNS + col_temp]);
		}
		String diagonalResult = diagonalStr.toString();
		System.out.println("Check diagonal win from left to right : " + diagonalResult);
		if (diagonalResult.contains("rrrr") || diagonalResult.contains("yyyy")) {
			return true;
		}

		// Check diagonal win from right to left
		diagonalStr = new StringBuilder("");
		diagonalStr.append(this.gameData.matrix[row * Parameters.NUMBER_COLUMNS + col]);
		row_temp = row;
		col_temp = col;
		while (++row_temp < Parameters.NUMBER_ROWS && --col_temp >= 0) {
			diagonalStr.append(this.gameData.matrix[row_temp * Parameters.NUMBER_COLUMNS + col_temp]);
		}
		diagonalStr.reverse();
		row_temp = row;
		col_temp = col;
		while (--row_temp >= 0 && ++col_temp < Parameters.NUMBER_COLUMNS) {
			diagonalStr.append(this.gameData.matrix[row_temp * Parameters.NUMBER_COLUMNS + col_temp]);
		}
		diagonalResult = diagonalStr.toString();
		System.out.println("Check diagonal win from right to left : " + diagonalResult);
		if (diagonalResult.contains("rrrr") || diagonalResult
				.contains("yyyy")) { /* contain rrrr or yyyy return true; */
			return true;
		}
		return false;
	}

	@Override
	public GameData getGameData() {
		// TODO Auto-generated method stub
		return gameData;
	}

	@Override
	public String getGameId() {
		// TODO Auto-generated method stub
		return this.gameId;
	}

	@Override
	public Players getPlayers() {
		// TODO Auto-generated method stub
		return this.players;
	}

	@Override
	public ReturnCode connectGame(String player) {
		// TODO Auto-generated method stub
		System.out.println("GameImpl : connectGame method is invoked by " + player);
		if (this.players.player2 == null || this.players.player2.equals("")) {
			this.players.player2 = player;
			if (!this.getPlayers().player1.equals("") && !this.getPlayers().player2.equals("")) {
				this.getGameData().state = GameState.RUNNING;
				return ReturnCode.SUCCESS;
			}
			return ReturnCode.XNULL;
		} else {
			return ReturnCode.XNULL;
		}
	}

	@Override
	public ReturnCode play(String player, Column col) {
		// TODO Auto-generated method stub
		// Check opening game
		System.out.println("GameImpl : play methode is invoked by " + player + "at column + " + col);
		if (this.getPlayers().player1 == null || this.getPlayers().player1.equals("")
				|| this.getPlayers().player2 == null || this.getPlayers().player2.equals("")) {
			return ReturnCode.G_OPEN;
		}
		// player turn
		this.turn = this.turn % 2;
		int rowIndex;
		for (rowIndex = Parameters.NUMBER_ROWS - 1; rowIndex >= 0; rowIndex--) {
			if (this.gameData.matrix[rowIndex * Parameters.NUMBER_COLUMNS + col.value()] == 'b') {
				if (player.equals(this.players.player1) && this.turn == 0) {
					this.gameData.matrix[rowIndex * Parameters.NUMBER_COLUMNS + col.value()] = 'y';
					turn++;
					if (isWinner(player, rowIndex, col.value())) {
						this.getGameData().state = GameState.TERMINATED;
						return this.returnCode.WIN;
					}
				}
				if (player.equals(this.players.player2) && this.turn == 1) {
					this.gameData.matrix[rowIndex * Parameters.NUMBER_COLUMNS + col.value()] = 'r';
					turn++;
					if (isWinner(player, rowIndex, col.value())) {
						this.getGameData().state = GameState.TERMINATED;
						return this.returnCode.WIN;
					}
				}
				return this.returnCode.SUCCESS;
			}
		}

		// No longer space for playing
		if (rowIndex < 0) {
			return this.returnCode.XNULL;
		}
		// Check game over
		int index;
		for (index = 0; index < Parameters.NUMBER_COLUMNS * Parameters.NUMBER_ROWS; index++) {
			if (this.gameData.matrix[index] == 'b') {
				break;
			}
		}
		if (index == Parameters.NUMBER_COLUMNS * Parameters.NUMBER_ROWS
				|| this.getGameData().state == GameState.ABANDONED) {
			return this.returnCode.G_OVER;
		}
		return this.returnCode.XNULL;
	}

	@Override
	public boolean quitPlayer(String player) {
		// TODO Auto-generated method stub
		System.out.println("GameImpl: quitPlayer method is invoked by " + player);
		if (player.equals(this.getPlayers().player1)) {
			this.getPlayers().player1 = "";
			this.getGameData().state = GameState.TERMINATED;
			this.setReturnCode(ReturnCode.P_INV);
			return true;
		} else if (player.equals(this.getPlayers().player2)) {
			this.getPlayers().player2 = "";
			this.getGameData().state = GameState.ABANDONED;
			this.setReturnCode(ReturnCode.G_OPEN);
			if (this.getPlayers().player1 == null || this.getPlayers().player1.equals("")) {
				this.getGameData().state = GameState.TERMINATED;
				this.setReturnCode(ReturnCode.P_INV);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean ismyTurn(String player) {
		if (turn % 2 == 0 && player.equals(getPlayers().player1)) {
			return true;
		} else if (turn % 2 == 1 && player.equals(getPlayers().player2)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean _is_a(String repositoryIdentifier) {
		// TODO Auto-generated method stub
		return false;
	}
}

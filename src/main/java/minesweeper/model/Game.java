package minesweeper.model;

public class Game {

	private Board board;
	private int difficulty;
	private char gameState = 'i';
	private int time = 0;

	public Game(int difficulty, Board board) {
		if (!isValidDifficulty(difficulty)) {
			throw new IllegalArgumentException("Difficulty is invalid!");
		}
		this.difficulty = difficulty;
		if (!isValidBoard(difficulty, board)) {
			throw new IllegalArgumentException("Board is invalid with current difficulty!");
		}
		this.board = board;
	}
	
	public Game(int difficulty) {
		if (!isValidDifficulty(difficulty)) {
			throw new IllegalArgumentException("Difficulty is invalid!");
		}
		this.difficulty = difficulty;
		switch(difficulty) {
		case 0:
			board = new Board(8, 10);
			break;
		case 1:
			board = new Board(14, 18);
			break;
		case 2:
			board = new Board(20, 24);
			break;
		}
	}
	
	public void leftClickCell(int x, int y) {
		if (!(board.getCell(x, y).isRevealed())) {
			board.getCell(x, y).setRevealed();
		}
		if (board.getCell(x, y).isMine()) {
			gameState = 'f';
		}
	}
	
	public void rightClickCell(int x, int y) {
		if (!(board.getCell(x, y).isRevealed())
				&& !(board.getCell(x, y).isFlagged())) {
			board.getCell(x, y).setFlagged(true);
		} else {
			board.getCell(x, y).setFlagged(false);
		}
	}
	
	private boolean isValidDifficulty(int difficulty) {
		return (difficulty == 0 || difficulty == 1 || difficulty == 2);
	}
	
	private boolean isValidBoard(int difficulty, Board board) {
		switch(difficulty) {
		case 0:
			if (board.getHeight() == 8 && board.getWidth() == 10) {
				return true;
			}
			break;
		case 1:
			if (board.getHeight() == 14 && board.getWidth() == 18) {
				return true;
			}
			break;
		case 2:
			if (board.getHeight() == 20 && board.getWidth() == 24) {
				return true;
			}
			break;
		}
		return false;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public char getGameState() {
		return gameState;
	}
	
	public void setGameState(char gameState) {
		if (!isValidGameState(gameState)) {
			throw new IllegalArgumentException("Game state not supported!");
		} else {
			this.gameState = gameState;
		}
	}
	
	private boolean isValidGameState(char gameState) {
		return (gameState == 'f' || gameState == 'v' || gameState == '0' || gameState == '1' || gameState == '2');
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public boolean isVictory() {
		for (int y = 0; y < getBoard().getHeight(); y++) {
			for (int x = 0; x < getBoard().getWidth(); x++) {
				if (!getBoard().getCell(x, y).isMine()
						&& !getBoard().getCell(x, y).isRevealed()) {
					return false;
				}
			}
		}
		setGameState('v');
		return true;
	}
	
	public void setTime(int time) {
		if (!isValidTime(time)) {
			throw new IllegalArgumentException("Invalid time!");
		}
		this.time = time;
	}
	
	public int getTime() {
		return time;
	}
	
	private boolean isValidTime(int time) {
		return time >= 0;
	}
	
	@Override
	public String toString() {
		String boardString = "";
		for (int y = 0; y < getBoard().getHeight(); y++) {
			for (int x = 0; x < getBoard().getWidth(); x++) {
				if (getBoard().getCell(x, y).isMine()) {
					boardString += "*";
				} else if (getBoard().getCell(x, y).getAdjacentMines() == 0) {
					boardString += ".";
				} else {
					boardString += String.valueOf(getBoard().getCell(x, y).getAdjacentMines());
				}
			}
			boardString += "\n";
		}
		return boardString;
	}
	
	public static void main(String[] args) {
		Game game = new Game(0);
		System.out.println(game);
	}
	
}

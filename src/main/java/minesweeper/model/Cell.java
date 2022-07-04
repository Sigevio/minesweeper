package minesweeper.model;

public class Cell {

	private boolean mine = false;
	private boolean revealed = false;
	private boolean flagged = false;
	private int adjacentMines = 0;
	
	public boolean isMine() {
		return mine;
	}
	public boolean isRevealed() {
		return revealed;
	}
	public boolean isFlagged() {
		return flagged;
	}
	public int getAdjacentMines() {
		return adjacentMines;
	}

	public void setMine(boolean mine) {
		this.mine = mine;
	}
	public void setRevealed() {
		revealed = true;
	}
	public void setFlagged(boolean flagged) {
		this.flagged = flagged;
	}
	public void setAdjacentMines(int adjacentMines) {
		if (!isValidAdjacentMines(adjacentMines)) {
			throw new IllegalArgumentException("Illegal amount of adjacent mines!");
		}
		this.adjacentMines = adjacentMines;
	}
	
	private boolean isValidAdjacentMines(int adjacentMines) {
		return (adjacentMines >= 0 && adjacentMines <= 8);
	}
	
}

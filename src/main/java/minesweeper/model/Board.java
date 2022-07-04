package minesweeper.model;

import java.util.Random;

public class Board {

	private Cell container[][];
	private int width;
	private int height;
	
	public Board(int height, int width) {
		this(height, width, randomizer(height, width), null, null);
	}
	
	public Board(int height, int width, String mines, String flags, String revealed) {
		setHeight(height);
		setWidth(width);
		if (!isValidSizeCombination(getHeight(), getWidth())) {
			throw new IllegalStateException("Illegal combination of height and width!");
		}
		container = new Cell[getHeight()][getWidth()];
		
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				getCells()[y][x] = new Cell();
				if (mines.charAt(y*getWidth()+x) == 'm') {
					getCell(x, y).setMine(true);
				}
				if ((flags != null)
						&& flags.charAt(y*getWidth()+x) == 'f') {
					getCell(x, y).setFlagged(true);
				}
				if ((revealed != null)
						&& revealed.charAt(y*getWidth()+x) == 'r') {
					getCell(x, y).setRevealed();
				}
			}
		}
		
		setAllAdjacentMines();
	}
	
	private static String randomizer(int height, int width) {
		String randomized = "";
		Random rng = new Random();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (rng.nextDouble() < 0.15) {
					randomized += "m";
				} else {
					randomized += "#";
				}
			}
		}
		return randomized;
	}
	
	private int countAdjacentMines(int x, int y) {
		int count = 0;
		for (int j = -1; j <= 1; j++) {
			for (int i = -1; i <= 1; i++) {
				if (isValidCell(x+i, y+j)
						&& getCell(x+i, y+j).isMine()) {
					count++;
				}
			}
		}
		return count;
	}
	
	private void setAllAdjacentMines() {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (!(getCell(x, y).isMine())) {
					getCell(x, y).setAdjacentMines(countAdjacentMines(x, y));
				}
			}
		}
	}
	
	private void setHeight(int height) {
		if (!isValidHeight(height)) {
			throw new IllegalArgumentException("Invalid board height!");
		}
		this.height = height;
	}
	private void setWidth(int width) {
		if (!isValidWidth(width)) {
			throw new IllegalArgumentException("Invalid board width!");
		}
		this.width = width;
	}
	
	private boolean isValidHeight(int height) {
		return (height == 8 || height == 14 || height == 20);
	}
	private boolean isValidWidth(int width) {
		return (width == 10 || width == 18 || width == 24);
	}
	private boolean isValidSizeCombination(int height, int width) {
		return ((height == 8 && width == 10)
				|| (height == 14 && width == 18)
				|| (height == 20 && width == 24));
	}
	
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	
	public boolean isValidCell(int x, int y) {
		return (x >= 0
				&& y >= 0
				&& x < getWidth()
				&& y < getHeight());
	}
	
	public Cell getCell(int x, int y) {
		if (!isValidCell(x, y)) {
			throw new IllegalArgumentException("Cell does not excist!");
		} else {
			return getCells()[y][x];
		}
	}
	
	public Cell[][] getCells() {
		return container;
	}
	
}

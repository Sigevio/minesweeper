package minesweeper.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class BoardTest {

	private Board board0;
	private Board board1;
	private Board board2;
	
	@BeforeEach
	public void setup() {
		board0 = new Board(8, 10);
		board1 = new Board(14, 18);
		board2 = new Board(20, 24);
	}
	
	@Test
	@DisplayName("Test constructor with valid values.")
	public void testValidConstructor() {
		assertEquals(8, board0.getHeight());
		assertEquals(10, board0.getWidth());
		assertEquals(14, board1.getHeight());
		assertEquals(18, board1.getWidth());
		assertEquals(20, board2.getHeight());
		assertEquals(24, board2.getWidth());
	}
	
	@Test
	@DisplayName("Test constructor with invalid values.")
	public void testInvalidConstructor() {
		assertThrows(IllegalArgumentException.class, () -> new Board(7, 10));
		assertThrows(IllegalArgumentException.class, () -> new Board(9, 10));
		assertThrows(IllegalArgumentException.class, () -> new Board(8, 9));
		assertThrows(IllegalArgumentException.class, () -> new Board(8, 11));
		assertThrows(IllegalArgumentException.class, () -> new Board(13, 18));
		assertThrows(IllegalArgumentException.class, () -> new Board(15, 18));
		assertThrows(IllegalArgumentException.class, () -> new Board(14, 17));
		assertThrows(IllegalArgumentException.class, () -> new Board(14, 19));
		assertThrows(IllegalArgumentException.class, () -> new Board(19, 24));
		assertThrows(IllegalArgumentException.class, () -> new Board(21, 24));
		assertThrows(IllegalArgumentException.class, () -> new Board(20, 23));
		assertThrows(IllegalArgumentException.class, () -> new Board(20, 25));
		assertThrows(IllegalArgumentException.class, () -> new Board(10, 8));
		assertThrows(IllegalArgumentException.class, () -> new Board(18, 14));
		assertThrows(IllegalArgumentException.class, () -> new Board(24, 20));
		assertThrows(IllegalStateException.class, () -> new Board(8, 18));
		assertThrows(IllegalStateException.class, () -> new Board(8, 24));
		assertThrows(IllegalStateException.class, () -> new Board(14, 10));
		assertThrows(IllegalStateException.class, () -> new Board(14, 24));
		assertThrows(IllegalStateException.class, () -> new Board(20, 10));
		assertThrows(IllegalStateException.class, () -> new Board(20, 18));
	}
	
	@Test
	@DisplayName("Test getter for cell with valid coordinates.")
	public void testGetValidCell() {
		assertEquals(board0.getCells()[0][0], board0.getCell(0, 0));
		assertEquals(board0.getCells()[7][9], board0.getCell(9, 7));
		assertEquals(board1.getCells()[13][17], board1.getCell(17, 13));
		assertEquals(board2.getCells()[19][23], board2.getCell(23, 19));
	}
	
	@Test
	@DisplayName("Test getter for cell with invalid coordinates.")
	public void testGetInvalidCell() {
		assertThrows(IllegalArgumentException.class, () -> board0.getCell(-1, 0));
		assertThrows(IllegalArgumentException.class, () -> board0.getCell(0, -1));
		assertThrows(IllegalArgumentException.class, () -> board0.getCell(9, 8));
		assertThrows(IllegalArgumentException.class, () -> board0.getCell(10, 7));
		assertThrows(IllegalArgumentException.class, () -> board1.getCell(17, 14));
		assertThrows(IllegalArgumentException.class, () -> board1.getCell(18, 13));
		assertThrows(IllegalArgumentException.class, () -> board2.getCell(23, 20));
		assertThrows(IllegalArgumentException.class, () -> board2.getCell(24, 19));
		assertThrows(IllegalArgumentException.class, () -> board0.getCell(-999999, 0));
		assertThrows(IllegalArgumentException.class, () -> board0.getCell(999999, 0));
		assertThrows(IllegalArgumentException.class, () -> board0.getCell(0, -999999));
		assertThrows(IllegalArgumentException.class, () -> board0.getCell(0, 999999));
	}
	
}

package minesweeper.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class CellTest {

	private Cell cell;
	
	@BeforeEach
	public void setup() {
		cell = new Cell();
	}
	
	@Test
	@DisplayName("Test setting mine.")
	public void testSetMine() {
		assertFalse(cell.isMine());
		cell.setMine(true);
		assertTrue(cell.isMine());
		cell.setMine(false);
		assertFalse(cell.isMine());
	}
	
	@Test
	@DisplayName("Test setting revealed.")
	public void testSetRevealed() {
		assertFalse(cell.isRevealed());
		cell.setRevealed();
		assertTrue(cell.isRevealed());
	}
	
	@Test
	@DisplayName("Test setting flag.")
	public void testSetFlagged() {
		assertFalse(cell.isFlagged());
		cell.setFlagged(true);
		assertTrue(cell.isFlagged());
		cell.setFlagged(false);
		assertFalse(cell.isFlagged());
	}
	
	@Test
	@DisplayName("Test setting adjacent mines with valid values.")
	public void testSetValidAdjacentMines() {
		assertEquals(0, cell.getAdjacentMines());
		cell.setAdjacentMines(8);
		assertEquals(8, cell.getAdjacentMines());
		cell.setAdjacentMines(0);
		assertEquals(0, cell.getAdjacentMines());
	}
	
	@Test
	@DisplayName("Test setting adjacent mines with invalid values.")
	public void testSetInvalidAdjacentMines() {
		assertThrows(IllegalArgumentException.class, () -> cell.setAdjacentMines(-1));
		assertThrows(IllegalArgumentException.class, () -> cell.setAdjacentMines(9));
		assertThrows(IllegalArgumentException.class, () -> cell.setAdjacentMines(-999999));
		assertThrows(IllegalArgumentException.class, () -> cell.setAdjacentMines(999999));
	}
	
}

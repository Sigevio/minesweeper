package minesweeper.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class GameTest {

	private Game game0;
	private Game game1;
	private Game game2;
	private Game gameAlmostWon;
	private Game gameWon;
	
	private String mines = "####mm####"
			 			 + "m###mm###m"
			 			 + "##mmmmmm##"
			 			 + "mmm#mm#mmm"
			 			 + "mmmmmm####"
			 			 + "mmmmmmmmmm"
			 			 + "####mmmmmm"
			 			 + "###mmmm###";
	private String revealedAlmostWon = "rrrr##rrrr"
									 + "#rrr##rrr#"
									 + "rr######rr"
									 + "###r##r###"
									 + "######rrrr"
									 + "##########"
									 + "rrrr######"
									 + "rrr####rr#";
	private String revealedWon = "rrrr##rrrr"
		 		   			   + "#rrr##rrr#"
		 		   			   + "rr######rr"
		 		   			   + "###r##r###"
		 		   			   + "######rrrr"
		 		   			   + "##########"
		 		   			   + "rrrr######"
		 		   			   + "rrr####rrr";
	private String emptyFlags = "##########"
							  + "##########"
							  + "##########"
							  + "##########"
							  + "##########"
							  + "##########"
							  + "##########"
							  + "##########";
	
	@BeforeEach
	public void setup() {
		game0 = new Game(0);
		game1 = new Game(1);
		game2 = new Game(2);
		gameAlmostWon = new Game(0, new Board(8, 10, mines, emptyFlags, revealedAlmostWon));
		gameWon = new Game(0, new Board(8, 10, mines, emptyFlags, revealedWon));
	}
	
	@Test
	@DisplayName("Test constructor with valid values.")
	public void testValidConstructor() {
		assertEquals(0, game0.getDifficulty());
		assertEquals(1, game1.getDifficulty());
		assertEquals(2, game2.getDifficulty());
		assertEquals(8, game0.getBoard().getHeight());
		assertEquals(10, game0.getBoard().getWidth());
		assertEquals(14, game1.getBoard().getHeight());
		assertEquals(18, game1.getBoard().getWidth());
		assertEquals(20, game2.getBoard().getHeight());
		assertEquals(24, game2.getBoard().getWidth());
	}
	
	@Test
	@DisplayName("Test constructor with invalid values.")
	public void testInvalidConstructor() {
		assertThrows(IllegalArgumentException.class, () -> new Game(-1));
		assertThrows(IllegalArgumentException.class, () -> new Game(3));
		assertThrows(IllegalArgumentException.class, () -> new Game(-999999));
		assertThrows(IllegalArgumentException.class, () -> new Game(999999));
	}
	
	@Test
	@DisplayName("Test left clicking a cell.")
	public void testLeftClickCell() {
		assertEquals('i', game0.getGameState());
		assertFalse(game0.getBoard().getCell(0, 0).isRevealed());
		game0.getBoard().getCell(0, 0).setMine(true);
		game0.leftClickCell(0, 0);
		assertEquals('f', game0.getGameState());
		assertTrue(game0.getBoard().getCell(0, 0).isRevealed());
	}
	
	@Test
	@DisplayName("Test right clicking a cell")
	public void testRightClickCell() {
		assertFalse(game0.getBoard().getCell(0, 0).isFlagged());
		game0.rightClickCell(0, 0);
		assertTrue(game0.getBoard().getCell(0, 0).isFlagged());
		game0.rightClickCell(0, 0);
		assertFalse(game0.getBoard().getCell(0, 0).isFlagged());
	}
	
	@Test
	@DisplayName("Test setting valid game state.")
	public void testSetValidGameState() {
		game0.setGameState('0');
		game1.setGameState('1');
		game2.setGameState('2');
		assertEquals('0', game0.getGameState());
		assertEquals('1', game1.getGameState());
		assertEquals('2', game2.getGameState());
		game0.setGameState('f');
		game1.setGameState('v');
		assertEquals('f', game0.getGameState());
		assertEquals('v', game1.getGameState());
	}
	
	@Test
	@DisplayName("Test setting invalid game state.")
	public void testSetInvalidGameState() {
		assertThrows(IllegalArgumentException.class, () -> game0.setGameState('g'));
		assertThrows(IllegalArgumentException.class, () -> game1.setGameState('m'));
		assertThrows(IllegalArgumentException.class, () -> game2.setGameState('3'));
	}
	
	@Test
	@DisplayName("Test checking if game is won.")
	public void testIsVictory() {
		assertFalse(game0.isVictory());
		assertFalse(game1.isVictory());
		assertFalse(game2.isVictory());
		assertFalse(gameAlmostWon.isVictory());
		assertTrue(gameWon.isVictory());
	}
	
	@Test
	@DisplayName("Test setting time with valid values.")
	public void testSetValidTime() {
		game0.setTime(0);
		game1.setTime(10);
		game2.setTime(999999);
		assertEquals(0, game0.getTime());
		assertEquals(10, game1.getTime());
		assertEquals(999999, game2.getTime());
	}
	
	@Test
	@DisplayName("Test setting time with invalid values.")
	public void testSetInvalidTime() {
		assertThrows(IllegalArgumentException.class, () -> game0.setTime(-1));
		assertThrows(IllegalArgumentException.class, () -> game0.setTime(-999999));
	}
	
}

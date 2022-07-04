package minesweeper.fxui;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import minesweeper.model.Game;

public class FileHandlerTest {

	private Game game;
	private FileHandler fileHandler = new FileHandler();
	
	@BeforeEach
	public void setup() {
		game = new Game(0);
	}
	
	@Test
	@DisplayName("Test loading a file that does not exist.")
	public void testInvalidFileLoad() {
		File invalidFile = FileHandler.getSavesPath("invalid-file").toFile();
		invalidFile.delete();
		assertThrows(IOException.class,
				() -> game = fileHandler.loadProgress("invalid-file"),
				"An exception should be thrown when an invalid file is loaded!");
	}

	@Test
	@DisplayName("Test saving and loading file with string.")
	public void testStringSaveLoad() {
		try {
			fileHandler.saveProgress("test-file", game);
		} catch (IOException e) {
			fail("Saving failed!");
		}
		Game newGame;
		try {
			newGame = fileHandler.loadProgress("test-file");
			assertNotNull(newGame);
			assertEquals(newGame.toString(), game.toString());
		} catch (IOException e) {
			fail("Loading failed!");
		}
	}
	
	@AfterAll
	public static void teardown() {
		File file = FileHandler.getSavesPath("test-file").toFile();
		file.delete();
	}
	
}

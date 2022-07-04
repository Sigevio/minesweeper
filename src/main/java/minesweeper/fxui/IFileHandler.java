package minesweeper.fxui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import minesweeper.model.Game;

public interface IFileHandler {

	void saveHighscores(int highscoreEasy, int highscoreMedium, int highscoreHard) throws IOException;
	
	void saveHighscores(int highscoreEasy, int highscoreMedium, int highscoreHard, OutputStream os);
	
	int[] loadHighscores() throws IOException;
	
	int[] loadHighscores(InputStream is);
	
	void saveProgress(String filename, Game game) throws IOException;
	
	void saveProgress(Game game, OutputStream os);
	
	Game loadProgress(String filename) throws IOException;
	
	Game loadProgress(InputStream is);
	
}

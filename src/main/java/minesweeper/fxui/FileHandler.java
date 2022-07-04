package minesweeper.fxui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import minesweeper.model.Board;
import minesweeper.model.Game;

public class FileHandler implements IFileHandler {

	public final static String FILE_EXTENSION = "txt";
	
	private static Path getMinesweeperFolderPath() {
		return Path.of(System.getProperty("user.home"), "minesweeper");
	}
	
	private void ensureSavesFolder() {
		try {
			Files.createDirectories(getMinesweeperFolderPath().resolve("saves"));
			ensureHighscoresFile();
		} catch (IOException e) {
			System.out.println("Failed to create saves directory in FileHandler!");
		}
	}
	
	private void ensureHighscoresFile() {
		if (!getHighscoresPath().toFile().exists()) {
			try (PrintWriter pw = new PrintWriter(new FileOutputStream(getHighscoresPath().toFile()))) {
				pw.println("999\n999\n999");
			} catch (IOException e) {
				System.out.println("Failed to create default highscores file in FileHandler!");
			}
		}
	}
	
	public static Path getSavesPath(String name) {
		return getMinesweeperFolderPath().resolve("saves/" + name + "." + FILE_EXTENSION);
	}
	
	private Path getHighscoresPath() {
		return getMinesweeperFolderPath().resolve("highscores." + FILE_EXTENSION);
	}
	
	public void saveHighscores(int highscoreEasy, int highscoreMedium, int highscoreHard) throws IOException {
		try (OutputStream os = new FileOutputStream(getHighscoresPath().toFile())) {
			saveHighscores(highscoreEasy, highscoreMedium, highscoreHard, os);
		}
	}
	
	public void saveHighscores(int highscoreEasy, int highscoreMedium, int highscoreHard, OutputStream os) {
		try (PrintWriter pw = new PrintWriter(os)) {
			pw.println(highscoreEasy);
			pw.println(highscoreMedium);
			pw.println(highscoreHard);
		}
	}
	
	public int[] loadHighscores() throws IOException {
		ensureSavesFolder();
		try (InputStream is = new FileInputStream(getHighscoresPath().toFile())) {
			return loadHighscores(is);
		}
	}
	
	public int[] loadHighscores(InputStream is) {
		try (Scanner scanner = new Scanner(is)) {
			int highscoreEasy = Integer.parseInt(scanner.nextLine());
			int highscoreMedium = Integer.parseInt(scanner.nextLine());
			int highscoreHard = Integer.parseInt(scanner.nextLine());
			int[] highscores = {highscoreEasy, highscoreMedium, highscoreHard};
			return highscores;
		}
	}
	
	
	public void saveProgress(String filename, Game game) throws IOException {
		if (filename.isEmpty()) {
			filename = "filename";
		}
		try (OutputStream os = new FileOutputStream(getSavesPath(filename).toFile())) {
			saveProgress(game, os);
		}
	}
	
	public void saveProgress(Game game, OutputStream os) {
		try (PrintWriter pw = new PrintWriter(os)) {
			pw.println(game.getDifficulty());
			pw.println(game.getBoard().getHeight());
			pw.println(game.getBoard().getWidth());
			pw.println(game.getTime());
			
			for (int y = 0; y < game.getBoard().getHeight(); y++) {
				String stateOfLine = "";
				for (int x = 0; x < game.getBoard().getWidth(); x++) {
					String stateOfCell = "";
					if (game.getBoard().getCell(x, y).isMine()) {
						stateOfCell += "m";
					} else {
						stateOfCell += "#";
					}
					if (game.getBoard().getCell(x, y).isFlagged()) {
						stateOfCell += "f";
					} else {
						stateOfCell += "#";
					}
					if (game.getBoard().getCell(x, y).isRevealed()) {
						stateOfCell += "r";
					} else {
						stateOfCell += "#";
					}
					stateOfLine += stateOfCell;
				}
				pw.println(stateOfLine);
			}
		}
	}
	
	public Game loadProgress(String filename) throws IOException {
		if (filename.isEmpty()) {
			filename = "filename";
		}
		try (InputStream is = new FileInputStream(getSavesPath(filename).toFile())) {
			return loadProgress(is);
		}
	}
	
	public Game loadProgress(InputStream is) {
		try (Scanner scanner = new Scanner(is)) {
			int difficulty = Integer.parseInt(scanner.nextLine());
			int height = Integer.parseInt(scanner.nextLine());
			int width = Integer.parseInt(scanner.nextLine());
			int time = Integer.parseInt(scanner.nextLine());
			String mines = "";
			String flags = "";
			String revealed = "";
			
			for (int y = 0; y < height; y++) {
				String loadedLine = scanner.nextLine();
				for (int x = 0; x < width; x++) {
					if (loadedLine.charAt(x*3) == 'm') {
						mines += "m";
					} else {
						mines += "#";
					}
					if (loadedLine.charAt((x*3)+1) == 'f') {
						flags += "f";
					} else {
						flags += "#";
					}
					if (loadedLine.charAt((x*3)+2) == 'r') {
						revealed += "r";
					} else {
						revealed += "#";
					}
				}
			}
			
			Board board = new Board(height, width, mines, flags, revealed);
			Game game = new Game(difficulty, board);
			game.setTime(time);
			return game;
		}
	}
	
}

package minesweeper.fxui;

import java.io.IOException;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import minesweeper.model.Game;

public class SelectionScreenController {

	private static Game game;
	private static int difficulty;
	private int loadingTextCountdown;
	
	private FileHandler fileHandler = new FileHandler();
	
	@FXML
	private AnchorPane ap;
	
	@FXML
	private StackPane sp;
	
	@FXML
	private Button easyButton, mediumButton, hardButton, loadButton;
	
	@FXML
	private Text highscoreEasy, highscoreMedium, highscoreHard, highscoresText, defaultFilename, loadingFailText;
	
	@FXML
	private TextField filename;
	
	private Timeline timeline = new Timeline(new KeyFrame(
			Duration.millis(1000),
			event -> {
				if (loadingTextCountdown-- < 0) {
					loadingFailText.setVisible(false);
				}
			}
			));
	
	@FXML
	private void initialize() {
		try {
			Game game = MinesweeperController.getGame();
			switch (game.getGameState()) {
			case 'i':
				drawIntroScreen();
				break;
			case 'f':
				drawFailureScreen();
				break;
			case 'v':
				drawVictoryScreen();
				break;
			case '0':
				drawVictoryScreen();
				highscoresText.setText("NEW HIGHSCORE!!");
				highscoreEasy.setFill(Color.RED);
				break;
			case '1':
				drawVictoryScreen();
				highscoresText.setText("NEW HIGHSCORE!!");
				highscoreMedium.setFill(Color.RED);
				break;
			case '2':
				drawVictoryScreen();
				highscoresText.setText("NEW HIGHSCORE!!");
				highscoreHard.setFill(Color.RED);
				break;
			}
		} catch (NullPointerException e) {
			drawIntroScreen();
		} finally {
			filename.setOnKeyTyped(event -> {
				if (!filename.getText().isEmpty()) {
					defaultFilename.setVisible(false);
				} else {
					defaultFilename.setVisible(true);
				}
			});
			
			timeline.setCycleCount(Animation.INDEFINITE);
			timeline.play();
			
			loadHighscores();
		}
	}
	
	private void drawIntroScreen() {
		Text text = new Text("NEW GAME:");
		text.setTranslateY(-130);
		text.setStyle("-fx-font: 20 helvetica;");
		sp.getChildren().add(text);
	}
	
	private void drawFailureScreen() {
		Text text1 = new Text(String.valueOf("YOU LOST!"));
		Text text2 = new Text(String.valueOf("Retry:"));
		text1.setTranslateY(-140);
		text1.setStyle("-fx-font: 20 helvetica; -fx-fill: red;");
		text2.setTranslateY(-120);
		sp.getChildren().add(text1);
		sp.getChildren().add(text2);
	}
	
	private void drawVictoryScreen() {
		Text text1 = new Text(String.valueOf("YOU WON!"));
		Text text2 = new Text(String.valueOf("Play again:"));
		text1.setTranslateY(-140);
		text1.setStyle("-fx-font: 20 helvetica;");
		text2.setTranslateY(-120);
		sp.getChildren().add(text1);
		sp.getChildren().add(text2);
	}
	
	private void setupStage(Game game) {
		try {
			Parent gameParent = FXMLLoader.load(getClass().getResource("Minesweeper.fxml"));
			Scene gameScene = new Scene(gameParent);
			Stage primaryStage = (Stage)easyButton.getScene().getWindow();
			primaryStage.setScene(gameScene);
			primaryStage.setWidth(game.getBoard().getWidth()*30);
			primaryStage.setHeight(game.getBoard().getHeight()*30+200);
			primaryStage.centerOnScreen();
		} catch (IOException e) {
			System.out.println("Change of scene failed in SelectionScreenController!");
		}
	}
	
	@FXML
	private void handleEasyButton() {
		difficulty = 0;
		game = new Game(0);
		setupStage(game);
	}
	
	@FXML
	private void handleMediumButton() {
		difficulty = 1;
		game = new Game(1);
		setupStage(game);
	}
	
	@FXML
	private void handleHardButton() {
		difficulty = 2;
		game = new Game(2);
		setupStage(game);
	}
	
	public static Game getGame() {
		return game;
	}
	
	public static int getDifficulty() {
		return difficulty;
	}
	
	private void loadHighscores() {
		try {
			int[] highscores = fileHandler.loadHighscores();
			highscoreEasy.setText(String.valueOf(highscores[0]));
			highscoreMedium.setText(String.valueOf(highscores[1]));
			highscoreHard.setText(String.valueOf(highscores[2]));
		} catch (IOException e) {
			System.out.println("Failed to load highscores in SelectionScreenController!");
		}
	}
	
	@FXML
	private void loadProgress() {
		try {
			game = fileHandler.loadProgress(filename.getText());
			difficulty = game.getDifficulty();
			setupStage(game);
		} catch (IOException e) {
			loadingFailText.setVisible(true);
			loadingTextCountdown = 2;
		}
		
	}
	
}

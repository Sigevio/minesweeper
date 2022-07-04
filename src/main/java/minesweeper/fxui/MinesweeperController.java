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
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import minesweeper.model.Game;

public class MinesweeperController {

	private static Game game;
	private int difficulty;
	private long startTime = System.currentTimeMillis();
	private int loadedTime;
	private int textCountdown;
	
	private int[] highscores;
	
	private FileHandler fileHandler = new FileHandler();
	
	private String hiddenColorLight = "-fx-background-color: lightgreen;";
	private String hiddenColorDark = "-fx-background-color: #5BD84E;";
	private String revealedColorLight = "-fx-background-color: #EBFFBD;";
	private String revealedColorDark = "-fx-background-color: #D6F099;";
	private String focusColor = "-fx-background-color: green;";
	private String borderStem = "; -fx-border-color: #DCC062; -fx-border-width: ";
	
	@FXML
	private AnchorPane ap;
	
	@FXML
	private StackPane sp;
	
	@FXML
	private Pane board;
	
	@FXML
	private Text elapsedTime, elapsedTimeText, highscoreTime, highscoreTimeText, defaultFilename, loadingFailText, loadingSuccessText, savingSuccessText;
	
	@FXML
	private TextField filename;
	
	@FXML
	private Button saveButton, loadButton, menuButton;
	
	@FXML
	private Line separation;
	
	private Timeline timeline = new Timeline(new KeyFrame(
			Duration.millis(1000),
			event -> {
				game.setTime(((int)(System.currentTimeMillis() - startTime)/1000)+loadedTime);
				elapsedTime.setText(String.valueOf(game.getTime()));
				if (textCountdown-- < 0) {
					loadingFailText.setVisible(false);
					loadingSuccessText.setVisible(false);
					savingSuccessText.setVisible(false);
				}
			}
			));

	@FXML
	private void initialize() {
		game = SelectionScreenController.getGame();
		difficulty = SelectionScreenController.getDifficulty();
		loadedTime = game.getTime();
		
		elapsedTime.setText(String.valueOf(loadedTime));
		
		ap.setPrefSize(game.getBoard().getWidth()*30, game.getBoard().getHeight()*30+175);
		ap.setMinSize(game.getBoard().getWidth()*30, game.getBoard().getHeight()*30+175);
		ap.setMaxSize(game.getBoard().getWidth()*30, game.getBoard().getHeight()*30+175);
		
		separation.setStartX(0);
		separation.setStartY(game.getBoard().getHeight()*30+1);
		separation.setEndX(game.getBoard().getWidth()*30);
		separation.setEndY(game.getBoard().getHeight()*30+1);
		separation.setStrokeWidth(2);
		
		loadHighscores();
		highscoreTime.setText(String.valueOf(highscores[difficulty]));
		
		moveTextsAndButtons();
		
		createBoard();
		
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		
		filename.setOnKeyTyped(event -> {
			if (!filename.getText().isEmpty()) {
				defaultFilename.setVisible(false);
			} else {
				defaultFilename.setVisible(true);
			}
		});
		
	}
	
	private void saveHighscore() {
		try {
			switch (difficulty) {
			case 0:
				fileHandler.saveHighscores(game.getTime(), highscores[1], highscores[2]);
				break;
			case 1:
				fileHandler.saveHighscores(highscores[0], game.getTime(), highscores[2]);
				break;
			case 2:
				fileHandler.saveHighscores(highscores[0], highscores[1], game.getTime());
				break;
			}
		} catch (IOException e) {
			System.out.println("Failed to save highscores in MinesweeperController!");
		}
	}
	
	private void loadHighscores() {
		try {
			highscores = fileHandler.loadHighscores();
		} catch (IOException e) {
			System.out.println("Failed to load highscores in MinesweeperController!");
		}
	}
	
	@FXML
	private void backToMenu() {
		try {
			Parent introParent = FXMLLoader.load(getClass().getResource("SelectionScreen.fxml"));
			Scene introScene = new Scene(introParent);
			Stage primaryStage = (Stage)ap.getScene().getWindow();
			primaryStage.setScene(introScene);
			primaryStage.setWidth(300);
			primaryStage.setHeight(363);
			primaryStage.centerOnScreen();
		} catch(IOException e) {
			System.out.println("Change of scene failed in MinesweeperController!");
		}
	}
	
	@FXML
	private void saveProgress() {
		try {
			fileHandler.saveProgress(filename.getText(), game);
			loadingFailText.setVisible(false);
			loadingSuccessText.setVisible(false);
			savingSuccessText.setVisible(true);
			textCountdown = 2;
		} catch (IOException e) {
			System.out.println("Failed to save progress in MinesweeperController!");
		}
		
	}
	
	@FXML
	private void loadProgress() {
		try {
			game = fileHandler.loadProgress(filename.getText());
			savingSuccessText.setVisible(false);
			loadingFailText.setVisible(false);
			loadingSuccessText.setVisible(true);
			difficulty = game.getDifficulty();
			loadedTime = game.getTime();
			
			elapsedTime.setText(String.valueOf(loadedTime));
			startTime = System.currentTimeMillis();
			
			createBoard();
			moveTextsAndButtons();
			
			ap.getScene().getWindow().setWidth(game.getBoard().getWidth()*30);
			ap.getScene().getWindow().setHeight(game.getBoard().getHeight()*30+200);
			ap.getScene().getWindow().centerOnScreen();
			
			ap.setPrefSize(game.getBoard().getWidth()*30, game.getBoard().getHeight()*30+200);
			ap.setMinSize(game.getBoard().getWidth()*30, game.getBoard().getHeight()*30+200);
			ap.setMaxSize(game.getBoard().getWidth()*30, game.getBoard().getHeight()*30+200);
			
			separation.setStartX(0);
			separation.setStartY(game.getBoard().getHeight()*30+1);
			separation.setEndX(game.getBoard().getWidth()*30);
			separation.setEndY(game.getBoard().getHeight()*30+1);
		} catch (IOException e) {
			savingSuccessText.setVisible(false);
			loadingSuccessText.setVisible(false);
			loadingFailText.setVisible(true);
		} finally {
			textCountdown = 2;
		}
	}
	
	private void moveTextsAndButtons() {
		elapsedTime.setTranslateX(-70);
		elapsedTime.setTranslateY(85+difficulty*90);
		elapsedTimeText.setTranslateX(-70);
		elapsedTimeText.setTranslateY(65+difficulty*90);
		highscoreTime.setTranslateX(-70);
		highscoreTime.setTranslateY(130+difficulty*90);
		highscoreTimeText.setTranslateX(-70);
		highscoreTimeText.setTranslateY(110+difficulty*90);
		saveButton.setTranslateX(40);
		saveButton.setTranslateY(120+difficulty*90);
		loadButton.setTranslateX(100);
		loadButton.setTranslateY(120+difficulty*90);
		filename.setTranslateX(70);
		filename.setTranslateY(80+difficulty*90);
		menuButton.setTranslateY(170+difficulty*90);
		defaultFilename.setTranslateX(51);
		defaultFilename.setTranslateY(81+difficulty*90);
		loadingFailText.setTranslateX(70);
		loadingFailText.setTranslateY(55+difficulty*90);
		loadingSuccessText.setTranslateX(70);
		loadingSuccessText.setTranslateY(55+difficulty*90);
		savingSuccessText.setTranslateX(70);
		savingSuccessText.setTranslateY(55+difficulty*90);
	}
	
	private void createBoard() {
		board.getChildren().clear();
		for (int y = 0; y < game.getBoard().getHeight(); y++) {
			for (int x = 0; x < game.getBoard().getWidth(); x++) {
				Pane cell = new Pane();
				cell.setPrefSize(30, 30);
				cell.setTranslateX(x*30);
				cell.setTranslateY(y*30);
				setHiddenColor(cell);
				board.getChildren().add(cell);
				cell.setOnMouseEntered(event -> {
					if (!game.getBoard().getCell(getLocationX(cell), getLocationY(cell)).isRevealed()) {
						cell.setStyle(focusColor + " -fx-cursor: hand;");
					}
					
				});
				cell.setOnMouseExited(event -> {
					if (!game.getBoard().getCell(getLocationX(cell), getLocationY(cell)).isRevealed()) {
						setHiddenColor(cell);
					} else {
						colorBoard();
					}
				});
				cell.setOnMouseClicked(event -> {
					if (event.getButton().equals(MouseButton.PRIMARY)
							&& !game.getBoard().getCell(getLocationX(cell), getLocationY(cell)).isFlagged()
							&& !game.getBoard().getCell(getLocationX(cell), getLocationY(cell)).isRevealed()) {
						game.leftClickCell(getLocationX(cell), getLocationY(cell));
						if (game.getBoard().getCell(getLocationX(cell), getLocationY(cell)).isMine()) {
							timeline.stop();
							try {
								Parent failureParent = FXMLLoader.load(getClass().getResource("SelectionScreen.fxml"));
								Scene failureScene = new Scene(failureParent);
								Stage primaryStage = (Stage)ap.getScene().getWindow();
								primaryStage.setScene(failureScene);
								primaryStage.setWidth(300);
								primaryStage.setHeight(363);
								primaryStage.centerOnScreen();
							} catch(IOException e) {
								System.out.println("Change of scene failed in MinesweeperController!");
							}
						} else if (game.getBoard().getCell(getLocationX(cell), getLocationY(cell)).getAdjacentMines() == 0) {
							floodFill(cell);
						} else {
							addNumber(cell);
						}
						colorBoard();
						if (game.isVictory()) {
							timeline.stop();
							if (game.getTime() < highscores[difficulty]) {
								game.setGameState((char)(difficulty+'0'));
								saveHighscore();
							}
							try {
								Parent victoryParent = FXMLLoader.load(getClass().getResource("SelectionScreen.fxml"));
								Scene victoryScene = new Scene(victoryParent);
								Stage primaryStage = (Stage)ap.getScene().getWindow();
								primaryStage.setScene(victoryScene);
								primaryStage.setWidth(300);
								primaryStage.setHeight(363);
								primaryStage.centerOnScreen();
							} catch(IOException e) {
								System.out.println("Change of scene failed in MinesweeperController!");
							}
						}
					} else if (event.getButton().equals(MouseButton.SECONDARY)) {
						if (game.getBoard().getCell(getLocationX(cell), getLocationY(cell)).isFlagged()) {
							game.rightClickCell(getLocationX(cell), getLocationY(cell));
							removeFlag(cell);
						} else if (!game.getBoard().getCell(getLocationX(cell), getLocationY(cell)).isRevealed()) {
							game.rightClickCell(getLocationX(cell), getLocationY(cell));
							addFlag(cell);
						}
					}
				});
			}
		}
		for (int y = 0; y < game.getBoard().getHeight(); y++) {
			for (int x = 0; x < game.getBoard().getWidth(); x++) {
				if (game.getBoard().getCell(x, y).isRevealed()
						&& game.getBoard().getCell(x, y).getAdjacentMines() != 0) {
					Text text = new Text(String.valueOf(game.getBoard().getCell(x, y).getAdjacentMines()));
					text.setTranslateX(x*30+9);
					text.setTranslateY(y*30+22);
					text.setStyle("-fx-font: 20 helvetica;");
					board.getChildren().add(text);
				} else if (game.getBoard().getCell(x, y).isFlagged()) {
					Rectangle post = new Rectangle();
					Rectangle flag = new Rectangle();
					post.setId(String.valueOf(y) + String.valueOf(x) + "p");
					flag.setId(String.valueOf(y) + String.valueOf(x) + "f");
					post.setTranslateX(x*30+10);
					post.setTranslateY(y*30+10);
					post.setWidth(2);
					post.setHeight(14);
					post.setFill(Color.GREY);
					post.setMouseTransparent(true);
					flag.setTranslateX(x*30+12);
					flag.setTranslateY(y*30+10);
					flag.setWidth(9);
					flag.setHeight(5);
					flag.setFill(Color.RED);
					flag.setMouseTransparent(true);
					board.getChildren().add(post);
					board.getChildren().add(flag);
				}
			}
		}
		colorBoard();
	}
	
	private void colorBoard() {
		for (int y = 0; y < game.getBoard().getHeight(); y++) {
			for (int x = 0; x < game.getBoard().getWidth(); x++) {
				if (game.getBoard().getCell(x, y).isRevealed()) {
					String border = borderStem;
					if (game.getBoard().isValidCell(x, y-1)
							&& !game.getBoard().getCell(x, y-1).isRevealed()) {
						border += "2px ";
					} else {
						border += "0 ";
					}
					if (game.getBoard().isValidCell(x+1, y)
							&& !game.getBoard().getCell(x+1, y).isRevealed()) {
						border += "2px ";
					} else {
						border += "0 ";
					}
					if (game.getBoard().isValidCell(x, y+1)
							&& !game.getBoard().getCell(x, y+1).isRevealed()) {
						border += "2px ";
					} else {
						border += "0 ";
					}
					if (game.getBoard().isValidCell(x-1, y)
							&& !game.getBoard().getCell(x-1, y).isRevealed()) {
						border += "2px;";
					} else {
						border += "0;";
					}
					setRevealedColor((Pane)board.getChildren().get(y*game.getBoard().getWidth() + x), border);
				} 
			}
		}
	}
	
	private int getLocationX(Pane cell) {
		return (int)((cell.getBoundsInParent().getMaxX()/30)-1);
	}
	
	private int getLocationY(Pane cell) {
		return (int)((cell.getBoundsInParent().getMaxY()/30)-1);
	}
	
	private void addNumber(Pane cell) {
		Text text = new Text(String.valueOf(game.getBoard().getCell(getLocationX(cell), getLocationY(cell)).getAdjacentMines()));
		text.setTranslateX(getLocationX(cell)*30+9);
		text.setTranslateY(getLocationY(cell)*30+22);
		text.setStyle("-fx-font: 20 helvetica;");
		board.getChildren().add(text);
	}
	
	private void floodFill(Pane cell) {
		for (int y = -1; y <= 1; y++) {
			for (int x = -1; x <= 1; x++) {
				if (game.getBoard().isValidCell(getLocationX(cell)+x, getLocationY(cell)+y)
						&& !(game.getBoard().getCell(getLocationX(cell)+x, getLocationY(cell)+y).isMine())
						&& !(game.getBoard().getCell(getLocationX(cell)+x, getLocationY(cell)+y).isRevealed())
						&& game.getBoard().getCell(getLocationX(cell)+x, getLocationY(cell)+y).getAdjacentMines() == 0) {
					if (game.getBoard().getCell(getLocationX(cell)+x, getLocationY(cell)+y).isFlagged()) {
						game.rightClickCell(getLocationX(cell)+x, getLocationY(cell)+y);
						removeFlag((Pane)board.getChildren().get((getLocationY(cell)+y)*game.getBoard().getWidth() + (getLocationX(cell)+x)));
					}
					game.getBoard().getCell(getLocationX(cell)+x, getLocationY(cell)+y).setRevealed();
					floodFill((Pane)board.getChildren().get((getLocationY(cell)+y)*game.getBoard().getWidth() + (getLocationX(cell)+x)));
				} else if (game.getBoard().isValidCell(getLocationX(cell)+x, getLocationY(cell)+y)
						&& !(game.getBoard().getCell(getLocationX(cell)+x, getLocationY(cell)+y).isMine())
						&& !(game.getBoard().getCell(getLocationX(cell)+x, getLocationY(cell)+y).isRevealed())
						&& game.getBoard().getCell(getLocationX(cell)+x, getLocationY(cell)+y).getAdjacentMines() != 0) {
					if (game.getBoard().getCell(getLocationX(cell)+x, getLocationY(cell)+y).isFlagged()) {
						game.rightClickCell(getLocationX(cell)+x, getLocationY(cell)+y);
						removeFlag((Pane)board.getChildren().get((getLocationY(cell)+y)*game.getBoard().getWidth() + (getLocationX(cell)+x)));
					}
					game.getBoard().getCell(getLocationX(cell)+x, getLocationY(cell)+y).setRevealed();
					Text text = new Text(String.valueOf(game.getBoard().getCell(getLocationX(cell)+x, getLocationY(cell)+y).getAdjacentMines()));
					text.setTranslateX((getLocationX(cell)+x)*30+9);
					text.setTranslateY((getLocationY(cell)+y)*30+22);
					text.setStyle("-fx-font: 20 helvetica;");
					board.getChildren().add(text);
				}
			}
		}
	}

	private void addFlag(Pane cell) {
		Rectangle post = new Rectangle();
		Rectangle flag = new Rectangle();
		post.setId(String.valueOf(getLocationY(cell)) + String.valueOf(getLocationX(cell)) + "p");
		flag.setId(String.valueOf(getLocationY(cell)) + String.valueOf(getLocationX(cell)) + "f");
		post.setTranslateX(getLocationX(cell)*30+10);
		post.setTranslateY(getLocationY(cell)*30+10);
		post.setWidth(2);
		post.setHeight(14);
		post.setFill(Color.GREY);
		post.setMouseTransparent(true);
		flag.setTranslateX(getLocationX(cell)*30+12);
		flag.setTranslateY(getLocationY(cell)*30+10);
		flag.setWidth(9);
		flag.setHeight(5);
		flag.setFill(Color.RED);
		flag.setMouseTransparent(true);
		board.getChildren().add(post);
		board.getChildren().add(flag);
	}
	
	private void removeFlag(Pane cell) {
		board.getChildren().remove(board
				.lookup("#" + String.valueOf(getLocationY(cell)) + String.valueOf(getLocationX(cell)) + "p"));
		board.getChildren().remove(board
				.lookup("#" + String.valueOf(getLocationY(cell)) + String.valueOf(getLocationX(cell)) + "f"));
	}
	
	private void setHiddenColor(Pane cell) {
		if ((getLocationX(cell) + getLocationY(cell))%2 == 0) {
			cell.setStyle(hiddenColorLight);
		} else {
			cell.setStyle(hiddenColorDark);
		}
	}
	
	private void setRevealedColor(Pane cell, String border) {
		if ((getLocationX(cell) + getLocationY(cell))%2 == 0) {
			cell.setStyle(revealedColorLight + border);
		} else {
			cell.setStyle(revealedColorDark + border);
		}
	}
	
	public static Game getGame() {
		return game;
	}
	
}

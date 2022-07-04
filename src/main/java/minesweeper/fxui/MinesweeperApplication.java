package minesweeper.fxui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MinesweeperApplication extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent intro = FXMLLoader.load(getClass().getResource("SelectionScreen.fxml"));
		Scene introScene = new Scene(intro);
		primaryStage.setTitle("Minesweeper");
		primaryStage.setScene(introScene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(MinesweeperApplication.class, args);
	}
}

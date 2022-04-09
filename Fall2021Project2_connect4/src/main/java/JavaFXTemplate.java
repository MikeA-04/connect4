// Name:	Mike Apreza
// Project Description: This project contains the program to play the classic game of Connect Four.
//		There are three files in this program: "JavaFXTemplate.java", "ConnectFour.java", and "GameButton.java".
//		The first file runs the game. The second file contains all the information for setting up scenes, keeping
//		track of which player needs to take their turn, button even handlers, and so on. The third file contains
//		the class definition of a GameButton, which inherits from the Button class.
//		First, the user will see a welcome screen. Once the user clicks on the button, it will take them to the
//		game screen. The game screen has a menu bar with different options as well as the game board. If a player
//		wins or all the boxes are clicked (such that no one wins), the program will display a new scene telling
//		the players who won or if it was a tie. They can decide to play a new game or exit the program.

import javafx.application.Application;
import javafx.stage.Stage;

public class JavaFXTemplate extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Connect Four");
		ConnectFour CF = new ConnectFour(primaryStage);
		primaryStage.setScene(CF.getCurrentScene());
		primaryStage.show();
	}

}

import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.Stack;

public class ConnectFour {
	// Private data members
	private int theme; // Can only be 0, 1, or 2
	private int playTurn; // Can only be 1 or 2
	private int turns = 0; // Can only be values 0-42
	private Scene currentScene;
	private Stage howToPlayStage; // This will be constant, so it only needs to be initialized once (in the constructor)
	private BorderPane gamePane;
	private Stack<PlayerTurn> playerTrack;
	private ArrayList<ButtonPosition> availablePositions;
	private int[][] boardArray = new int[6][7];
	private ArrayList<ButtonPosition> winningBoxes;
	
	// Nested class for reverse move
	private class PlayerTurn { 
		public int player, X, Y; 
		public PlayerTurn(int p, int x, int y) {
			player = p;
			X = x;
			Y = y;
		}
	}
	
	// Nested class for available buttons
	private class ButtonPosition { 
		public int x, y;
		public ButtonPosition(int xCoor, int yCoor) {
			x = xCoor;
			y = yCoor;
		}
	}
	
	// Constructor
	public ConnectFour(Stage primaryStage) { 
		this.theme = 0;
		this.playTurn = 1;
		this.howToPlayStage = getHTP();
		reset();
		this.currentScene = getWelcome(primaryStage);
	}
		
	// Public methods
	public void reset() {
		// reset winning boxes and available positions and stack
		this.winningBoxes = new ArrayList<ButtonPosition>();
		availablePositions = new ArrayList<ButtonPosition>();
		playerTrack = new Stack<PlayerTurn>();
		for (int i = 0; i < 7; ++i) { availablePositions.add(new ButtonPosition(i, 5)); }
		// reset board array
		for (int k = 0; k < 6; ++ k) {
			for (int j = 0; j < 7; ++j) {
				this.boardArray[k][j] = 0;
			}
		}
		System.out.println("New game. Resetting...");
		printArray();
		
	}

	public void printArray() {
		for (int i = 0; i < 6; ++i) {
			for (int j = 0; j < 7; ++j) {
				System.out.print(this.boardArray[i][j]);
			}
			System.out.print("\n");
		}
		System.out.print("\n");
	}
	
	public Scene getWelcome(Stage primaryStage) {
		Text t1 = new Text(" Welc");
		t1.setFill(Color.WHITESMOKE);
	    t1.setStyle("-fx-font: 60 arial;");
	    Text t2 = new Text("me t");
		t2.setFill(Color.WHITESMOKE);
	    t2.setStyle("-fx-font: 60 arial;");
	    Text t3 = new Text("C");
		t3.setFill(Color.WHITESMOKE);
	    t3.setStyle("-fx-font: 60 arial;");
	    Text t4 = new Text("nnect F");
		t4.setFill(Color.WHITESMOKE);
	    t4.setStyle("-fx-font: 60 arial;");
	    Text t5 = new Text("ur!");
		t5.setFill(Color.WHITESMOKE);
	    t5.setStyle("-fx-font: 60 arial;");
	    Text o1 = new Text("o");
		o1.setFill(Color.RED);
	    o1.setStyle("-fx-font: 60 arial;");
	    Text o2 = new Text("o");
		o2.setFill(Color.YELLOW);
	    o2.setStyle("-fx-font: 60 arial;");
	    Text o3 = new Text("o");
		o3.setFill(Color.RED);
	    o3.setStyle("-fx-font: 60 arial;");
	    Text o4 = new Text("o");
		o4.setFill(Color.YELLOW);
	    o4.setStyle("-fx-font: 60 arial;");
	    
        HBox m1 = new HBox(t1, o1, t2, o2);
        m1.setAlignment(Pos.CENTER);
        HBox m2 = new HBox(t3, o3, t4, o4, t5);
        m2.setAlignment(Pos.CENTER);
        VBox message = new VBox(0, m1, m2);
        message.setAlignment(Pos.CENTER);
	    
		Button startB = new Button("S T A R T");
		startB.setMinSize(90, 30);
		
		// Lambda expression for start button since it should only switch the scene
		startB.setOnAction(e->{
			this.currentScene = getGame(primaryStage);
			primaryStage.setScene(getCurrentScene());
			primaryStage.show();
			});
		
		VBox welcomeMessage = new VBox(50, message, startB);
		welcomeMessage.setAlignment(Pos.CENTER);
		
		BorderPane pane = new BorderPane(welcomeMessage);
		BorderPane.setAlignment(welcomeMessage, Pos.CENTER);
		pane.setPadding(new Insets(50));
		pane.setStyle("-fx-background-color: dodgerBlue;");
		
		return new Scene(pane, 900, 700);
	}
	
	public Scene getEnd(Stage primaryStage, int winner) {
		// Set the text
		Text endText = new Text();
		
		if (winner == 1 || winner == 2) {
			endText.setText("Player " + winner + " wins!");
		} else {
			endText.setText("It's a tie!");
		}
		
		if (winner == 2) { endText.setFill(Color.BLACK); } // To make the text pop out
		else { endText.setFill(Color.WHITE); }
	    endText.setStyle("-fx-font: 60 arial;");
	    
	    // Set the buttons
	    Button playAgain = new Button("PLAY AGAIN");
	    playAgain.setMinWidth(90);
	    Button exit = new Button("EXIT");
	    exit.setMinWidth(90);
	    
	    // Put everything together	    
        HBox buttons = new HBox(50, playAgain, exit);
        buttons.setAlignment(Pos.CENTER);
        VBox items = new VBox(50, endText, buttons);
        items.setAlignment(Pos.CENTER);
		
		// Lambda expression for playAgain button since it should only switch the scene
		playAgain.setOnAction(e->{
			turns = 0;
			playTurn = 1;
			reset();
			currentScene = getGame(primaryStage);
			if (theme == 1) {
				gamePane.setStyle("-fx-background-color: linear-gradient(to bottom right, black, midnightBlue, darkViolet, midnightBlue, black);");
			}
			else if (theme == 2) {
				gamePane.setStyle("-fx-background-color: linear-gradient(#8cc6ff, #ffc28c, #ff8ce8);");
			}
			else {
				gamePane.setStyle("-fx-background-color: skyBlue;");
			}
			primaryStage.setScene(getCurrentScene());
			primaryStage.show();
			});
		
		// Lambda expression for exit button since it should only exit the program
		exit.setOnAction(e->System.exit(0));
		
		BorderPane pane = new BorderPane(items);
		BorderPane.setAlignment(items, Pos.CENTER);
		pane.setPadding(new Insets(50));
		if (winner == 1) {
			pane.setStyle("-fx-background-color: red;");
		} else if (winner == 2) {
			pane.setStyle("-fx-background-color: yellow;");
		} else {
			pane.setStyle("-fx-background-color: dodgerBlue;");
		}		
		
		return new Scene(pane, 900, 700);
	}
	
	public Scene getGame(Stage primaryStage) {
		// Text for player turn
		Text playerTurn = new Text("Turn: Player " + playTurn);
		playerTurn.setFill(Color.WHITESMOKE);
		playerTurn.setStyle("-fx-font: 25 arial;");
		VBox pTurn = new VBox(playerTurn);
		pTurn.setAlignment(Pos.CENTER);
		
		// Text for player info
		Text playerInfo = new Text("Waiting for Player " + playTurn + " to go...");
		playerInfo.setFill(Color.WHITESMOKE);
		playerInfo.setStyle("-fx-font: 20 arial;");
		VBox pInfo = new VBox(playerInfo);
		pInfo.setAlignment(Pos.CENTER);
		pInfo.setPadding(new Insets(20));
		
		// Game board
		GridPane gridPane = new GridPane();
	    for (int i = 0; i < 6; ++i) {
	    	for (int j = 0; j < 7; ++j) {
	    		GameButton gb = new GameButton(j, i);
	    		
	    		// Anonymous class to attach the event handler to game button
	    		gb.setOnAction(new EventHandler<ActionEvent>() {
	    			public void handle(ActionEvent a) {
	    				boolean valid = false;
	    				if (playTurn == 1) {
	    					for (ButtonPosition bp : availablePositions) {
	    						if (gb.getX() == bp.x && gb.getY() == bp.y) {
	    							// It's a valid move
	    							++turns;
	    	    					gb.setStyle("-fx-background-color: red;");
	    	    					playerTrack.add(new PlayerTurn(playTurn, gb.getX(), gb.getY()));
	    	    					System.out.println("Adding move (" + gb.getX() + ", " + gb.getY() + ") from player " + playTurn + " onto the stack.");
	    	    					playerInfo.setText("Player " + playTurn + " selected square (" + gb.getX() + ", " + gb.getY() + ")");
	    	    					playTurn = 2;
	    	    					playerTurn.setText("Turn: Player " + playTurn);
	    	    					valid = true;
	    	    					// update available position in that column
	    	    					bp.y  = bp.y - 1;
	    	    					// disable button
	    	    					gb.setDisable(true);
	    	    					// Edit the board array
	    	    					boardArray[gb.getY()][gb.getX()] = 1;
	    	    					printArray();
	    	    					
	    	    					if (isWin(1)) {
	    	    						// Disable all buttons
	    	    						ObservableList<Node> buttons = gridPane.getChildren();
	    	    						for (Node b : buttons) { ((GameButton) b).setDisable(true); }
	    	    						// Highlight the 4 squares
	    	    						for (ButtonPosition w : winningBoxes) {
	    	    							for (Node b : buttons) { 
	    	    									// See if the coordinates match
	    	    									if(((GameButton) b).getX() == w.x && ((GameButton) b).getY() == w.y) {
	    	    										((GameButton) b).setStyle("-fx-background-color: green");
	    	    									}
	    	    								}
	    	    						}
	    	    						// Pause and switch to end screen
	    	    						turns = 0;
	    	    						currentScene = getEnd(primaryStage, 1);
	    	    						PauseTransition pause = new PauseTransition(Duration.seconds(5));
	    	    						pause.setOnFinished(e->primaryStage.setScene(getCurrentScene()));
	    	    						pause.play();
	    	    						primaryStage.show();
	    	    					}
	    						}
	    					}
	    					// Else
	    					if (valid == false) {
	    						playerInfo.setText("Player " + playTurn + " selected INVALID square (" + gb.getX() + ", " + gb.getY() + "). Please pick again.");
	    					}
	    					else { valid = false; }
	    				}
	    				else {	    					
	    					for (ButtonPosition bp : availablePositions) {
	    						if (gb.getX() == bp.x && gb.getY() == bp.y) {
	    							// It's a valid move
	    							++turns;
	    	    					gb.setStyle("-fx-background-color: yellow;");
	    	    					playerTrack.add(new PlayerTurn(playTurn, gb.getX(), gb.getY()));
	    	    					System.out.println("Adding move (" + gb.getX() + ", " + gb.getY() + ") from player " + playTurn + " onto the stack.");
	    	    					playerInfo.setText("Player " + playTurn + " selected square (" + gb.getX() + ", " + gb.getY() + ")");
	    	    					
	    	    					// Check to see if max # of turns is done
	    	    					if (turns == 42) {
	    	    						turns = 0;
	    	    						currentScene = getEnd(primaryStage, 0);
	    	    						PauseTransition pause = new PauseTransition(Duration.seconds(5));
	    	    						pause.setOnFinished(e->primaryStage.setScene(getCurrentScene()));
	    	    						pause.play();
	    	    						primaryStage.show();
	    	    					}
	    	    					
	    	    					playTurn = 1;
	    	    					playerTurn.setText("Turn: Player " + playTurn);
	    	    					valid = true;
	    	    					// update available position in that column
	    	    					bp.y  = bp.y - 1;
	    	    					// Disable button
	    	    					gb.setDisable(true);
	    	    					// Edit the board array
	    	    					boardArray[gb.getY()][gb.getX()] = 2;
	    	    					printArray();
	    	    					
	    	    					if (isWin(2)) {
	    	    						// Disable all buttons
	    	    						ObservableList<Node> buttons = gridPane.getChildren();
	    	    						for (Node b : buttons) { ((GameButton) b).setDisable(true); }
	    	    						// Highlight the 4 squares
	    	    						for (ButtonPosition w : winningBoxes) {
	    	    							for (Node b : buttons) { 
	    	    									// See if the coordinates match
	    	    									if(((GameButton) b).getX() == w.x && ((GameButton) b).getY() == w.y) {
	    	    										((GameButton) b).setStyle("-fx-background-color: green");
	    	    									}
	    	    							}
	    	    						}
	    	    						// Pause and switch to end screen
	    	    						turns = 0;
	    	    						currentScene = getEnd(primaryStage, 2);
	    	    						PauseTransition pause = new PauseTransition(Duration.seconds(5));
	    	    						pause.setOnFinished(e->primaryStage.setScene(getCurrentScene()));
	    	    						pause.play();
	    	    						primaryStage.show();
	    	    					}
	    						}
	    					}
	    					// Else
	    					if (valid == false) {
	    						playerInfo.setText("Player " + playTurn + " selected INVALID square (" + gb.getX() + ", " + gb.getY() + "). Please pick again.");
	    					}
	    					else { valid = false; }
	    				}
	    			}
	    		});
	    		
	    		gridPane.add(gb, j, i);
	    	}
	    }
	    gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setAlignment(Pos.CENTER);
		
		// 					Menu Bar
		// Game play
		MenuItem mi1 = new MenuItem("Reverse Move");
		mi1.setOnAction(e->{
			// Check if stack is not empty
			if (!playerTrack.isEmpty()) {
				// Remove the last move done
				PlayerTurn lastMove = playerTrack.pop();
				System.out.println("Reversing move (" + lastMove.X + ", " + lastMove.Y + ") from player " + lastMove.player + " from the stack.");
				// Get the list of all the buttons
				ObservableList<Node> buttons = gridPane.getChildren();
				for (Node b : buttons) {
					// If the button with the coordinates are found...
					if (lastMove.X == ((GameButton) b).getX() && lastMove.Y == ((GameButton) b).getY()) {
						// enable button
						((GameButton) b).setDisable(false);
						// return to original color
						((GameButton) b).setStyle("-fx-background-color: darkGray;");
						// Update player turn and show message
						if (lastMove.player == 1) { playTurn = 1; }
						else { playTurn = 2; }
						playerTurn.setText("Turn: Player " + playTurn);
						playerInfo.setText("Move reversed. Player " + playTurn + " it is your turn...");
					}
				}
				// update available moves
				for (ButtonPosition bP : availablePositions) {
					if (lastMove.X == bP.x && (lastMove.Y - 1) == bP.y) { 
						bP.y = bP.y + 1; 
					}
				}
				// Edit the board array
				boardArray[lastMove.Y][lastMove.X] = 0;
				printArray();
			}
			});
		Menu m1 = new Menu("Gameplay", null, mi1);
		
		// Theme
		MenuItem mi2 = new MenuItem("Original Theme");
		mi2.setOnAction(e->{
			theme = 0;
			gamePane.setStyle("-fx-background-color: skyBlue;");
			});
		MenuItem mi3 = new MenuItem("Theme One");
		mi3.setOnAction(e->{ theme = 1;
			gamePane.setStyle("-fx-background-color: linear-gradient(to bottom right, black, midnightBlue, darkViolet, midnightBlue, black);"); });
		MenuItem mi4 = new MenuItem("Theme Two");
		mi4.setOnAction(e->{ theme = 2;
			gamePane.setStyle("-fx-background-color: linear-gradient(#8cc6ff, #ffc28c, #ff8ce8);"); });
		Menu m2 = new Menu("Themes", null, mi2, mi3, mi4);
		
		// Options
		MenuItem mi5 = new MenuItem("How to Play");
		mi5.setOnAction(e->{this.howToPlayStage.show();});
		MenuItem mi6 = new MenuItem("New Game");
		mi6.setOnAction(e->{
			turns = 0;
			playTurn = 1;
			reset();
			playerInfo.setText("Waiting for Player " + playTurn + " to go...");
			currentScene = getGame(primaryStage);
			if (theme == 1) { gamePane.setStyle("-fx-background-color: linear-gradient(to bottom right, black, midnightBlue, darkViolet, midnightBlue, black);"); }
			else if (theme == 2) { gamePane.setStyle("-fx-background-color: linear-gradient(#8cc6ff, #ffc28c, #ff8ce8);"); }
			else { gamePane.setStyle("-fx-background-color: skyBlue;"); }
			primaryStage.setScene(getCurrentScene());
			primaryStage.show();
			});
		MenuItem mi7 = new MenuItem("Exit");
		mi7.setOnAction(e -> {System.exit(0);});
		Menu m3 = new Menu("Options", null, mi5, mi6, mi7);
		
		MenuBar menu = new MenuBar(m1, m2, m3);   
		VBox menuBar = new VBox(menu);
		menuBar.setAlignment(Pos.CENTER);
		
		// Put everything together
	    VBox board = new VBox(pTurn, pInfo, gridPane);
	    board.setAlignment(Pos.CENTER);
		
		BorderPane pane = new BorderPane(board, menuBar, null, null, null);
		BorderPane.setAlignment(menuBar, Pos.CENTER);
		BorderPane.setAlignment(board, Pos.CENTER);
		pane.setStyle("-fx-background-color: skyBlue;");
		
		this.gamePane = pane;
		return new Scene(pane, 900, 700);
	}
	
	public Stage getHTP() { 
		Text htp = new Text(
				"\tHow to Play:\n" +
				"1. The game starts off where player 1 needs to click on a grey square on the board.\n" +
				"    If it is valid, the color should turn the color assigned to player 1.\n" +
				"2. If player 1's move was valid, it is then up to player 2 to click on a valid square.\n" +
				"3. The players will keep on taking turns until a player connects four vertically, horizontally, or\n" +
				"   diagnolly, OR there are no grey squares left. If a player connects four, that player wins.\n" +
				"   Otherwise, it's a tie." +
				"\n\n\tNotes:\n" + 
				"- If a player wants to reverse a move, go to the menu: Game Play => reverse move\n" +
				"- To select a new game theme, go to the menu. Under 'Themes', select the theme desired.\n" +
				"- To start a new game, go to the menu: Options => New Game\n" +
				"- To exit, go to the menu: Options => Exit");
		VBox text = new VBox(htp);
		text.setAlignment(Pos.CENTER);
		Stage stage = new Stage();
        stage.setTitle("Connect 4 - How to Play");
        stage.setScene(new Scene(text, 550, 300));
		return stage; 
	}
	
	public Scene getCurrentScene() { return this.currentScene; }
	
	public boolean isWin(int player) {
	    // Horizontal win?
	    for (int i = 5; i >= 0; --i ) {
	        for (int j = 0; j <= 3; j++) {
	            if (player == boardArray[j][i] && player == boardArray[j+1][i] && player == boardArray[j+2][i] && player == boardArray[j+3][i]) {
	            	winningBoxes.add(new ButtonPosition(i, j));
	            	winningBoxes.add(new ButtonPosition(i, j+1));
	            	winningBoxes.add(new ButtonPosition(i, j+2));
	            	winningBoxes.add(new ButtonPosition(i, j+3));
	            	System.out.println("Horizontal win\n");
	                return true;
	            }
	        }
	    }
	    
	    // Others
	    for (int i = 5; i >= 0; --i ) {
	        for (int j = 0; j <= 3; ++j) {
	            if (player == boardArray[i][j] && player == boardArray[i][j+1] && player == boardArray[i][j+2] && player == boardArray[i][j+3]) {
	            	winningBoxes.add(new ButtonPosition(j, i));
	            	winningBoxes.add(new ButtonPosition(j+1, i));
	            	winningBoxes.add(new ButtonPosition(j+2, i));
	            	winningBoxes.add(new ButtonPosition(j+3, i));
	            	System.out.println("Vertical win\n");
	            	return true;
	            } else if (i > 2) {
	            	if (player == boardArray[i][j] && player == boardArray[i-1][j+1] && player == boardArray[i-2][j+2] && player == boardArray[i-3][j+3]) {
		            	winningBoxes.add(new ButtonPosition(j, i));
		            	winningBoxes.add(new ButtonPosition(j+1, i-1));
		            	winningBoxes.add(new ButtonPosition(j+2, i-2));
		            	winningBoxes.add(new ButtonPosition(j+3, i-3));
		            	System.out.println("Ascending win\n");
		            	return true;
		            }
	            } else if (i < 3) {
	            	if (player == boardArray[i][j] && player == boardArray[i+1][j+1] && player == boardArray[i+2][j+2] && player == boardArray[i+3][j+3]) {
		            	winningBoxes.add(new ButtonPosition(j, i));
		            	winningBoxes.add(new ButtonPosition(j+1, i+1));
		            	winningBoxes.add(new ButtonPosition(j+2, i+2));
		            	winningBoxes.add(new ButtonPosition(j+3, i+3));
		            	System.out.println("Descending win\n");
		            	return true;
		        	}
	            } else { /* Nothing */ }
	        }
	    }
	    
	    // Then there is no win
	    return false;
	}
}

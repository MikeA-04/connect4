import javafx.scene.control.Button;

public class GameButton extends Button {
	// Private data members
	private int player, x_coor, y_coor;
	
	// Constructor
	public GameButton (int x, int y) {
		this.x_coor = x;
		this.y_coor = y;
		this.setStyle("-fx-background-color: darkGray;");
		this.setMinSize(70, 70);
	}
	
	// Getters and setters
	public void setPlayer(int p) { this.player = p; }
	public void setX(int x) { this.x_coor = x; }
	public void setY(int y) { this.y_coor = y; }
	public int getX() { return this.x_coor; }
	public int getY() { return this.y_coor; }
	public int getPlayer() { return this.player; }
}

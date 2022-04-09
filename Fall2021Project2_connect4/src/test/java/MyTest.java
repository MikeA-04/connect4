import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

// I cannot write any other tests because all would depend on what the user selects.
// The ConnectFour class heavily depends on the primaryStage in JavaFXTemplate.java.
// However, what I have done is:
//		- print the array when a new game starts
//		- print the array when a player makes a move (select square or reverse move),
//		- print when a move is added/removed to/from the stack
//		- and what type of win occurred (if any).

class MyTest {
	@Test
	void constructorTest() {
		GameButton gb = new GameButton(1, 2);
		assertEquals(1, gb.getX(), "constructorTest() FAILED: getX");
		assertEquals(2, gb.getY(), "constructorTest() FAILED: getY");
	}
	
	@Test
	void settersTest() {
		GameButton gb = new GameButton(1, 2);
		gb.setX(0);
		gb.setY(1);
		gb.setPlayer(2);
		assertEquals(0, gb.getX(), "constructorTest() FAILED: getX");
		assertEquals(1, gb.getY(), "constructorTest() FAILED: getY");
		assertEquals(2, gb.getPlayer(), "constructorTest() FAILED: getPlayer");
	}

}

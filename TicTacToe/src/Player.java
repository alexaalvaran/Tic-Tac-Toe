
public class Player {
	
	//Passes the turn to human player
	public void move(Board board) {
		board.humanTurn = true;
		board.gameText.setText("Human turn");
	}
	

}

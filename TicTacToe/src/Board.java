import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board implements ActionListener{
	
	JFrame frame;
	JPanel titlePanel, buttonPanel, bottomPanel, sidePanel;
	boolean humanTurn, hasWinner, isMaximizing;
	JLabel gameText;
	JButton[] buttons;
	String[][] gameBoard;
	int bestScore;
	
	//User input Variables
	Integer[] depth = {0, 1, 2, 3, 4, 5, 6,7, 8, 9};
	JComboBox<Integer> depthInput;
	JButton submitButton;
	JButton quitButton;
	JButton restartButton;
	int getDepth;
	JCheckBox firstPlayer, MM, AB, complete;
	
	//AI and Human
	aiAgent ai;
	Player player;
	
	Board board;
	
	//Default constructor 
	public Board() {
		//Initializing variables
		frame = new JFrame("TicTacToe");
		titlePanel = new JPanel();
		buttonPanel = new JPanel();
		bottomPanel = new JPanel();
		sidePanel = new JPanel();
		gameText = new JLabel("TicTacToe", JLabel.CENTER);
		buttons = new JButton[9];
		gameBoard = new String[3][3];
		depthInput = new JComboBox<Integer>(depth);
		submitButton = new JButton("submit");
		quitButton = new JButton("quit game");
		restartButton  = new JButton("restart game");
		firstPlayer = new JCheckBox("Play first");
		MM = new JCheckBox("Minimax");
		AB= new JCheckBox("AlphaBeta Pruning");
		complete = new JCheckBox("Complete");
		hasWinner = false;
		ai = new aiAgent(this);
		player = new Player();
		board = this;
		
		//Set up the UI - JFrame
		setUpUI();
		initializeBoard();
		
		

	}

	//Handles click of each game board button (when a turn is played by the human)
	@Override
	public void actionPerformed(ActionEvent e) {

		if(humanTurn || !hasWinner)
		{
			
		
		for (int i = 0; i < 9; i++) {
			if (e.getSource() == buttons[i]) {
				
					if (buttons[i].getText() == "") {
						buttons[i].setForeground(new Color(255, 0, 0));
						buttons[i].setText("X");
						addToBoard("X", i);
						checkWin();
						if(!hasWinner)
						{
						humanTurn = false;
						gameText.setText("AI turn");
						
						AITurn();
						
						checkWin();
					
						}
					
					}

				}
			}
		}
	}
	
	//Initialize the empty game board
	public void initializeBoard()
	{

		for (int i = 0; i < 9; i++) {
			buttons[i] = new JButton();
			buttonPanel.add(buttons[i]);
			buttons[i].setFocusable(false);
			buttons[i].addActionListener(this);
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				gameBoard[i][j] = "";
			}
		}
	}
	
	//Setting up the JFrame
	public void setUpUI()
	{
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 1000);
		frame.getContentPane().setBackground(new Color(50, 50, 50));
		frame.setLayout(new BorderLayout());
		frame.setVisible(true);

		gameText.setBackground(Color.pink);
		gameText.setForeground(Color.white);
		gameText.setHorizontalAlignment(JLabel.CENTER);
		gameText.setText("TicTacToe");
		gameText.setOpaque(true);
		titlePanel.setLayout(new GridLayout(3, 1));
		titlePanel.add(gameText);

		buttonPanel.setLayout(new GridLayout(3, 3));
		buttonPanel.setBackground(new Color(150, 150, 150));
			
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});


		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restartGame();
			}
		});
		
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restartGame();
				
				getDepth = depthInput.getSelectedIndex();
				
				
				if(firstPlayer.isSelected())
				{
					
					player.move(board);
					isMaximizing = false;
				}
				else 
				{
					AITurn();
				}
			
		
			}
		});
		bottomPanel.setLayout(new GridLayout(1,2));
		bottomPanel.add(restartButton);
		bottomPanel.add(quitButton);
	
		sidePanel.setLayout(new GridLayout(9,1));
		sidePanel.add(depthInput);
		sidePanel.add(firstPlayer);
		sidePanel.add(MM);
		sidePanel.add(AB);
		sidePanel.add(complete);
		sidePanel.add(submitButton);
		
	
	
		frame.add(titlePanel, BorderLayout.NORTH);
		frame.add(sidePanel, BorderLayout.EAST);
		frame.add(buttonPanel, BorderLayout.CENTER);
		frame.add(bottomPanel, BorderLayout.SOUTH);
	}

	//Matching the GUI Game board and the array implementation
	public void addToBoard(String playerLabel, int buttonNum) {
		int row = buttonNum/3;
		int col = buttonNum%3;
		gameBoard[row][col] = playerLabel;
		
	}


	//Checks for winner to return to minimax algorithm
	public String checkWinner() {
	// Check rows
	for (int i = 0; i < 3; i++) {
		if (!gameBoard[i][0].equals("") && gameBoard[i][0].equals(gameBoard[i][1])
				&& gameBoard[i][0].equals(gameBoard[i][2])) {
			return gameBoard[i][0];
		}
	}

	// Check columns
	for (int i = 0; i < 3; i++) {
		if (!gameBoard[0][i].equals("") && gameBoard[0][i].equals(gameBoard[1][i])
				&& gameBoard[0][i].equals(gameBoard[2][i])) {
			return gameBoard[0][i];
		}
	}

	// Check diagonals
	if (!gameBoard[0][0].equals("") && gameBoard[0][0].equals(gameBoard[1][1])
			&& gameBoard[0][0].equals(gameBoard[2][2])) {
		return gameBoard[0][0];
	}
	if (!gameBoard[0][2].equals("") && gameBoard[0][2].equals(gameBoard[1][1])
			&& gameBoard[0][2].equals(gameBoard[2][0])) {
		return gameBoard[0][2];
	}
	
	return checkTie();
	
	}
	
	//Checks for wins in the game board and handle accordingly
	public void checkWin() {
		// X wins
		if (gameBoard[0][0] == "X" && gameBoard[0][1] == "X" && gameBoard[0][2] == "X") {
			wins(0, 1, 2, "X");
		}

		if (gameBoard[1][0] == "X" && gameBoard[1][1] == "X" && gameBoard[1][2] == "X") {
			wins(3, 4, 5, "X");
		}

		if (gameBoard[2][0] == "X" && gameBoard[2][1] == "X" && gameBoard[2][2] == "X") {
			wins(6, 7, 8, "X");
		}

		if (gameBoard[0][0] == "X" && gameBoard[1][1] == "X" && gameBoard[2][2] == "X") {
			wins(0, 4, 8, "X");
		}

		if (gameBoard[0][2] == "X" && gameBoard[1][1] == "X" && gameBoard[2][0] == "X") {
			wins(2, 4, 6, "X");
		}

		if (gameBoard[0][0] == "X" && gameBoard[1][0] == "X" && gameBoard[2][0] == "X") {
			wins(0, 3, 6, "X");
		}

		if (gameBoard[0][1] == "X" && gameBoard[1][1] == "X" && gameBoard[2][1] == "X") {
			wins(1, 4, 7, "X");
		}

		if (gameBoard[0][2] == "X" && gameBoard[1][2] == "X" && gameBoard[2][2] == "X") {
			wins(2, 5, 8, "X");
		}

		// O Wins
		if (gameBoard[0][0] == "O" && gameBoard[0][1] == "O" && gameBoard[0][2] == "O") {
			wins(0, 1, 2, "O");
		}

		if (gameBoard[1][0] == "O" && gameBoard[1][1] == "O" && gameBoard[1][2] == "O") {
			wins(3, 4, 5, "O");
		}

		if (gameBoard[2][0] == "O" && gameBoard[2][1] == "O" && gameBoard[2][2] == "O") {
			wins(6, 7, 8, "O");
		}

		if (gameBoard[0][0] == "O" && gameBoard[1][1] == "O" && gameBoard[2][2] == "O") {
			wins(0, 4, 8, "O");
		}

		if (gameBoard[0][2] == "O" && gameBoard[1][1] == "O" && gameBoard[2][0] == "O") {
			wins(2, 4, 6, "O");
		}

		if (gameBoard[0][0] == "O" && gameBoard[1][0] == "O" && gameBoard[2][0] == "O") {
			wins(0, 3, 6, "O");
		}

		if (gameBoard[0][1] == "O" && gameBoard[1][1] == "O" && gameBoard[2][1] == "O") {
			wins(1, 4, 7, "O");
		}

		if (gameBoard[0][2] == "O" && gameBoard[1][2] == "O" && gameBoard[2][2] == "O") {
			wins(2, 5, 8, "O");
		}
		
		else
		{
			checkTie();
		}
	}

	//Handles wins
	private void wins(int a, int b, int c, String player) {
		buttons[a].setBackground(Color.pink);
		buttons[b].setBackground(Color.pink);
		buttons[c].setBackground(Color.pink);

		if (player.equals("O")) {
			gameText.setText("AI Wins!");
			hasWinner = true;
		} else {
			gameText.setText("Human Wins!");
			hasWinner = true;
		}

		for (int i = 0; i < 9; i++) {
			buttons[i].setEnabled(false);
		}
	}
	
	//Checks for tie
	public String checkTie()
	{
		int openSpots = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (gameBoard[i][j] == "") {
					openSpots++;

				}
			}
		}
		if (openSpots == 0) {
			gameText.setText("Tie Game!");
			return "tie";
		}
		else
			 return null;
	}

		
	//Resets game 
	public void restartGame() {
		for (int i = 0; i < 9; i++) {
			buttons[i].setText("");
			buttons[i].setEnabled(true);
			hasWinner = false;
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				gameBoard[i][j] = "";
			}
		}

	}
	
	public void AITurn()
	{
		if(AB.isSelected())
		{
			if(complete.isSelected())
			{
              ai.aiMove(getDepth, true, true, isMaximizing);
              
			}
			else
			{
				ai.aiMove(getDepth, false, true,isMaximizing);
			    
			}
		}
		else if(MM.isSelected())
		{
			if(complete.isSelected())
			{
				ai.aiMove(getDepth, true, false,isMaximizing);
				
			}
			else
			{
				ai.aiMove(getDepth, false, false,isMaximizing);
				
			}
		}
		
	}
	

}
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Initial implements ActionListener {
	
	//Game Variables
	JFrame frame;
	JPanel titlePanel, buttonPanel, bottomPanel;
	boolean humanTurn, hasWinner, mmComplete, abComplete;
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
	JCheckBox firstPlayer;
	
	
	//Default constructor 
	public Initial() {
		//Initializing variables
		frame = new JFrame("TicTacToe");
		titlePanel = new JPanel();
		buttonPanel = new JPanel();
		bottomPanel = new JPanel();
		gameText = new JLabel("TicTacToe", JLabel.CENTER);
		buttons = new JButton[9];
		gameBoard = new String[3][3];
		depthInput = new JComboBox<Integer>(depth);
		submitButton = new JButton("submit");
		quitButton = new JButton("quit game");
		restartButton  = new JButton("restart game");
		firstPlayer = new JCheckBox("Play first");
		hasWinner = false;
	
		
		
		//Set up the UI - JFrame
		setUpUI();
		initializeBoard();
		
		if(humanTurn)
		{
		humanMove();
		}
		else
		{
			aiMove(getDepth,true);
		}

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
						aiMove(getDepth, true);
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
		frame.setSize(800, 800);
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
				if(firstPlayer.isSelected())
				{
					humanTurn = true;
				}
				else {
					humanTurn = false;
				}
				
				getDepth = depthInput.getSelectedIndex();
				restartGame();
			}
		});
		bottomPanel.setLayout(new GridLayout(1,2));
		bottomPanel.add(restartButton);
		bottomPanel.add(quitButton);
	
		titlePanel.add(depthInput);
		titlePanel.add(submitButton);
		titlePanel.add(firstPlayer);
	
	
		frame.add(titlePanel, BorderLayout.NORTH);

		frame.add(buttonPanel, BorderLayout.CENTER);
		frame.add(bottomPanel, BorderLayout.SOUTH);
	}

	//Matching the GUI Game board and the array implementation
	public void addToBoard(String playerLabel, int buttonNum) {
		int row = buttonNum/3;
		int col = buttonNum%3;
		gameBoard[row][col] = playerLabel;
		
	}

	//Passes the turn to human player
	public void humanMove() {
		humanTurn = true;
		gameText.setText("Human turn");
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
	
	
	
	//Minimax Methods
	
	//Plays the move of the AI using minimax complete or limited 
	public void aiMove(int depth, boolean isComplete) {
		bestScore = Integer.MIN_VALUE;
		int[] bestMove = new int[2]; // Store the best move coordinates (i, j)
		int score;

		// Iterate through all empty cells and find the best move
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (gameBoard[i][j].equals("")) {
					gameBoard[i][j] = "O"; // Assume AI's move
					if(isComplete == true)
					{
						score = minimaxComplete(gameBoard, false);
					}
					
					else
					{
						score = minimaxLimited(gameBoard, depth, false);
					}
				
					gameBoard[i][j] = ""; // Undo the move

					if (score > bestScore) {
						bestScore = score;
						bestMove[0] = i;
						bestMove[1] = j;
					}
				}
			}

		}
		
	

		// Make the best move
		gameBoard[bestMove[0]][bestMove[1]] = "O";
		int buttonIndex = bestMove[0] * 3 + bestMove[1]; // Convert (i, j) to button index
		buttons[buttonIndex].setForeground(new Color(255, 0, 0));
		buttons[buttonIndex].setText("O");

		humanTurn = true;
		gameText.setText("Human turn");

	}
	
	//Minimax Complete function
	public int minimaxComplete(String[][] gameBoard, boolean isMaximizing) {
	    String result = checkWinner();

	    if (result != null) {
	        // If the game is over, return the score
	        if (result.equals("X")) {
	            return -10; // Human wins
	        } else if (result.equals("O")) {
	            return 10; // AI wins
	        } else if (result.equals("tie")) {
	            return 0; // Tie
	        }
	    }

	    // If maximizing player's turn
	    if (isMaximizing) {
	        int bestScore = Integer.MIN_VALUE;
	        for (int i = 0; i < 3; i++) {
	            for (int j = 0; j < 3; j++) {
	                if (gameBoard[i][j].equals("")) {
	                    gameBoard[i][j] = "O"; // Assume AI's move
	                    int score = minimaxComplete(gameBoard, false);
	                    gameBoard[i][j] = ""; // Undo the move
	                    bestScore = Math.max(score, bestScore);
	                }
	            }
	        }
	        return bestScore;
	    }
	    // If minimizing player's turn
	    else {
	        int bestScore = Integer.MAX_VALUE;
	        for (int i = 0; i < 3; i++) {
	            for (int j = 0; j < 3; j++) {
	                if (gameBoard[i][j].equals("")) {
	                    gameBoard[i][j] = "X"; // Assume human's move
	                    int score = minimaxComplete(gameBoard, true);
	                    gameBoard[i][j] = ""; // Undo the move
	                    bestScore = Math.min(score, bestScore);
	                }
	            }
	        }
	        return bestScore;
	    }
	}


	
	//Minimax Limited function
	public int minimaxLimited(String[][] gameBoard, int depth, boolean isMaximizing) {
		
		String result = checkWinner();
	

		if (result != null) {
			// If the game is over, return the score
			if (result.equals("X")) {
				return -10; // Human wins
			} else if (result.equals("O")) {
				return 10; // AI wins
			} else if (result.equals("tie")) {
				return 0; // Tie
			}
		}
		
		if (depth == 0) {
	        return evaluate(gameBoard);
	    }

		// If maximizing player's turn
		if (isMaximizing) {
			int bestScore = Integer.MIN_VALUE;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (gameBoard[i][j].equals("")) {
						gameBoard[i][j] = "O"; // Assume AI's move
						int score = minimaxLimited(gameBoard, depth + 1, false);
						gameBoard[i][j] = ""; // Undo the move
						bestScore = Math.max(score, bestScore);
					}
				}
			}
			return bestScore;
		}
		// If minimizing player's turn
		else {
			int bestScore = Integer.MAX_VALUE;
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (gameBoard[i][j].equals("")) {
						gameBoard[i][j] = "X"; // Assume human's move
						int score = minimaxLimited(gameBoard, depth + 1, true);
						gameBoard[i][j] = ""; // Undo the move
						bestScore = Math.min(score, bestScore);
					}
				}
			}
			return bestScore;
		}
	}

	//Evaluation function using formula given in W2
	private int evaluate(String[][] gameBoard) {
	    int X2 = 0, X1 = 0, O2 = 0, O1 = 0;

	    // Count two-in-a-row and one-in-a-row positions for X and O
	    for (int i = 0; i < 3; i++) {
	        for (int j = 0; j < 3; j++) {
	            // Check rows
	            if (gameBoard[i][j].equals("X")) {
	                if (j < 2 && gameBoard[i][j + 1].equals("X")) {
	                    X2++;
	                }
	                if (j == 0 && gameBoard[i][j + 1].equals("")) {
	                    X1++;
	                }
	            } else if (gameBoard[i][j].equals("O")) {
	                if (j < 2 && gameBoard[i][j + 1].equals("O")) {
	                    O2++;
	                }
	                if (j == 0 && gameBoard[i][j + 1].equals("")) {
	                    O1++;
	                }
	            }
	            // Check columns
	            if (gameBoard[j][i].equals("X")) {
	                if (j < 2 && gameBoard[j + 1][i].equals("X")) {
	                    X2++;
	                }
	                if (j == 0 && gameBoard[j + 1][i].equals("")) {
	                    X1++;
	                }
	            } else if (gameBoard[j][i].equals("O")) {
	                if (j < 2 && gameBoard[j + 1][i].equals("O")) {
	                    O2++;
	                }
	                if (j == 0 && gameBoard[j + 1][i].equals("")) {
	                    O1++;
	                }
	            }
	        }
	    }

	    // Check main diagonal
	    if (gameBoard[0][0].equals("X") && gameBoard[1][1].equals("X")) {
	        X2++;
	    } else if (gameBoard[0][0].equals("O") && gameBoard[1][1].equals("O")) {
	        O2++;
	    }
	    if (gameBoard[1][1].equals("X") && gameBoard[2][2].equals("X")) {
	        X2++;
	    } else if (gameBoard[1][1].equals("O") && gameBoard[2][2].equals("O")) {
	        O2++;
	    }

	    // Check secondary diagonal
	    if (gameBoard[0][2].equals("X") && gameBoard[1][1].equals("X")) {
	        X2++;
	    } else if (gameBoard[0][2].equals("O") && gameBoard[1][1].equals("O")) {
	        O2++;
	    }
	    if (gameBoard[1][1].equals("X") && gameBoard[2][0].equals("X")) {
	        X2++;
	    } else if (gameBoard[1][1].equals("O") && gameBoard[2][0].equals("O")) {
	        O2++;
	    }

	    // Calculate evaluation score based on the provided formula
	    return 3 * X2 + X1 - (3 * O2 + O1);
	}

	
	//Alpha beta methods
	public int alphaBetaLimited(String[][] gameBoard, int depth, int alpha, int beta, boolean isMaximizing) {
	    String result = checkWinner();

	    if (result != null || depth == 0) {
	        // If the game is over or depth limit reached, return the evaluation score
	        return evaluate(gameBoard);
	    }

	    if (isMaximizing) {
	        int maxEval = Integer.MIN_VALUE;
	        for (int i = 0; i < 3; i++) {
	            for (int j = 0; j < 3; j++) {
	                if (gameBoard[i][j].equals("")) {
	                    gameBoard[i][j] = "O"; // Assume AI's move
	                    int eval = alphaBetaLimited(gameBoard, depth - 1, alpha, beta, false);
	                    gameBoard[i][j] = ""; // Undo the move
	                    maxEval = Math.max(maxEval, eval);
	                    alpha = Math.max(alpha, eval);
	                    if (beta <= alpha) {
	                        break; // Beta cutoff
	                    }
	                }
	            }
	        }
	        return maxEval;
	    } else {
	        int minEval = Integer.MAX_VALUE;
	        for (int i = 0; i < 3; i++) {
	            for (int j = 0; j < 3; j++) {
	                if (gameBoard[i][j].equals("")) {
	                    gameBoard[i][j] = "X"; // Assume human's move
	                    int eval = alphaBetaLimited(gameBoard, depth - 1, alpha, beta, true);
	                    gameBoard[i][j] = ""; // Undo the move
	                    minEval = Math.min(minEval, eval);
	                    beta = Math.min(beta, eval);
	                    if (beta <= alpha) {
	                        break; // Alpha cutoff
	                    }
	                }
	            }
	        }
	        return minEval;
	    }
	}

	public int alphaBetaComplete(String[][] gameBoard, int alpha, int beta, boolean isMaximizing) {
	    String result = checkWinner();

	    if (result != null) {
	        // If the game is over, return the evaluation score
	        return evaluate(gameBoard);
	    }

	    if (isMaximizing) {
	        int maxEval = Integer.MIN_VALUE;
	        for (int i = 0; i < 3; i++) {
	            for (int j = 0; j < 3; j++) {
	                if (gameBoard[i][j].equals("")) {
	                    gameBoard[i][j] = "O"; // Assume AI's move
	                    int eval = alphaBetaComplete(gameBoard, alpha, beta, false);
	                    gameBoard[i][j] = ""; // Undo the move
	                    maxEval = Math.max(maxEval, eval);
	                    alpha = Math.max(alpha, eval);
	                    if (beta <= alpha) {
	                        break; // Beta cutoff
	                    }
	                }
	            }
	        }
	        return maxEval;
	    } else {
	        int minEval = Integer.MAX_VALUE;
	        for (int i = 0; i < 3; i++) {
	            for (int j = 0; j < 3; j++) {
	                if (gameBoard[i][j].equals("")) {
	                    gameBoard[i][j] = "X"; // Assume human's move
	                    int eval = alphaBetaComplete(gameBoard, alpha, beta, true);
	                    gameBoard[i][j] = ""; // Undo the move
	                    minEval = Math.min(minEval, eval);
	                    beta = Math.min(beta, eval);
	                    if (beta <= alpha) {
	                        break; // Alpha cutoff
	                    }
	                }
	            }
	        }
	        return minEval;
	    }
	}

}
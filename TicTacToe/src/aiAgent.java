import java.awt.Color;

public class aiAgent {
	
	public Board board;
	public aiAgent(Board board)
	{
		this.board = board;
	}

	//Minimax Methods
	
		//Plays the move of the AI using minimax complete or limited 
		public void aiMove( int depth, boolean isComplete, boolean AB, boolean isMaximizing) {
			int bestScore = Integer.MIN_VALUE;
			int[] bestMove = new int[2]; // Store the best move coordinates (i, j)
			int score;

			// Iterate through all empty cells and find the best move
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (board.gameBoard[i][j].equals("")) {
						board.gameBoard[i][j] = "O"; // Assume AI's move
						
						if(!AB)
						{
						if(isComplete == true)
						{
							score = minimaxComplete(board.gameBoard, isMaximizing);
						}
						
						else
						{
							score = minimaxLimited(board.gameBoard, depth, isMaximizing);
						}
						}
						
						else
						{
						if(isComplete == true)
						{
							score = alphaBetaComplete(board.gameBoard, Integer.MIN_VALUE, Integer.MAX_VALUE, isMaximizing);
						}
						else
						{
							score = alphaBetaLimited(board.gameBoard, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, isMaximizing);
						}
						}
						board.gameBoard[i][j] = ""; // Undo the move

						if (score > bestScore) {
							bestScore = score;
							bestMove[0] = i;
							bestMove[1] = j;
						}
					}
				}

			}
			
		

			// Make the best move
			board.gameBoard[bestMove[0]][bestMove[1]] = "O";
			int buttonIndex = bestMove[0] * 3 + bestMove[1]; // Convert (i, j) to button index
			board.buttons[buttonIndex].setForeground(new Color(255, 0, 0));
			board.buttons[buttonIndex].setText("O");
		
			board.humanTurn = true;
			board.gameText.setText("Human turn");

		}
		
		//Minimax Complete function
		public int minimaxComplete(String[][] gameBoard, boolean isMaximizing) {
		    String result = board.checkWinner();


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
			
			String result = board.checkWinner();


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
		    String result = board.checkWinner();

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
		    String result = board.checkWinner();

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

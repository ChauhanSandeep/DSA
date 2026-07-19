package design.connect4ai;

import java.util.LinkedList;

/**
 * Problem: Connect Four Board State
 *
 * Model a Connect Four board for move generation, win detection, and heuristic
 * scoring. The state records the board grid, the last move, whose turn moved last,
 * and the current winner if one has been found.
 *
 * Pattern:  Design | Board simulation | Heuristic evaluation
 *
 * Example:
 *   Input:  X pieces stacked four high in one column
 *   Output: checkWinState() = true
 *   Why:    the column scan detects four equal non-empty cells vertically.
 *
 * Follow-ups:
 *   1. How would you make win checks faster?
 *      Track the last move and inspect only four directions through that cell.
 *   2. How would you make board copies cheaper?
 *      Use immutable bitboards or copy-on-write rows.
 *   3. How would you improve the heuristic?
 *      Score open-ended windows and center control instead of raw pair/triple counts.
 */
public class State {

    static final int X = 1;     //User (used in Main and switch case)
    static final int O = -1;    //Computer (used in Main and switch case)
    int EMPTY = 0;              //Blank space
    //We need to know the player that made the last move
    GamePlay lastMove;
    int lastLetterPlayed;
    int winner;
    int[][] gameBoard;
    String winningMethod;       //Winner by [row, column, diagonal]
    //------------

    //Constructor of a state (board)
    public State() {
        lastMove = new GamePlay();
        lastLetterPlayed = O; //The user starts first
        winner = 0;
        gameBoard = new int[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                gameBoard[i][j] = EMPTY;
            }
        }
    }//end Constructor

    public void setWinner(int winner) {
        this.winner = winner;
    }//end setWinner

    public void setWinnerMethod(String winningMethod) {
        this.winningMethod = winningMethod;
    }//end setWinnerMethod

    //Make a movement based on a column and a player
    /**
     * Drops a piece into the requested column for the given player letter.
     *
     * Time:  O(R) - scans the column to find the landing row.
     * Space: O(1) - mutates the board in place.
     *
     * @param col target column
     * @param letter player marker to place
     */
    public void makeMove(int col, int letter) {
        lastMove = lastMove.moveDone(getRowPosition(col), col);
        gameBoard[getRowPosition(col)][col] = letter;
        lastLetterPlayed = letter;
    }//end makeMove

    //Checks whether a move is valid; whether a square is empty. Used only when we need to expand a movement
    /**
     * Checks whether a column has an empty landing row.
     *
     * Time:  O(R) - computes the row position for the column.
     * Space: O(1) - no extra storage.
     *
     * @param col column to test
     * @return true if a piece can be dropped in the column
     */
    public boolean isValidMove(int col) {
        int row = getRowPosition(col);
        if ((row == -1) || (col == -1) || (row > 5) || (col > 6)) {
            return false;
        }
        if (gameBoard[row][col] != EMPTY) {
            return false;
        }
        return true;
    }//end isValidMove

    //Is used when we need to make a movement (Is possible to move the piece?)
    public boolean canMove(int row, int col) {
        //We evaluate mainly the limits of the board 
        if ((row <= -1) || (col <= -1) || (row > 5) || (col > 6)) {
            return false;
        }
        return true;
    }//end CanMove

    //Is a column full?
    public boolean checkFullColumn(int col) {
        if (gameBoard[0][col] == EMPTY)
            return false;
        else {
            System.out.println("The column " + (col + 1) + " is full. Select another column.");
            return true;
        }
    }//end checkFullColumn

    //We search for a blank space in the board to put the piece ('X' or 'O')
    public int getRowPosition(int col) {
        int rowPosition = -1;
        for (int row = 0; row < 6; row++) {
            if (gameBoard[row][col] == EMPTY) {
                rowPosition = row;
            }
        }
        return rowPosition;
    }//end getRowPosition

    //This method help us to expand the board (it´s a board state given a move)
    public State boardWithExpansion(State board) {
        State expansion = new State();
        expansion.lastMove = board.lastMove;
        expansion.lastLetterPlayed = board.lastLetterPlayed;
        expansion.winner = board.winner;
        expansion.gameBoard = new int[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                expansion.gameBoard[i][j] = board.gameBoard[i][j];
            }
        }
        return expansion;
    }//end boardWithExpansion

    //Generates the children of the state. The max number of the children is 7 because we have 7 columns
    /**
     * Generates all child states reachable by one legal move for the player.
     *
     * Time:  O(C * R * C) - each legal move copies the full board.
     * Space: O(C * R * C) - stores copied boards for generated children.
     *
     * @param letter player marker for the generated moves
     * @return child states after every valid column move
     */
    public LinkedList<State> getChildren(int letter) {
        LinkedList<State> children = new LinkedList<State>();
        for (int col = 0; col < 7; col++) {
            if (isValidMove(col)) {
                State child = boardWithExpansion(this);
                child.makeMove(col, letter);
                children.add(child);
            }
        }
        return children;
    }//end getChildren

    /**
     * Scores the current board from the O computer player's perspective.
     *
     * Time:  O(R * C) - scans board windows for wins, triples, and pairs.
     * Space: O(1) - uses only counters.
     *
     * @return positive score for O advantage, negative for X advantage
     */
    public int utilityFunction() {
        //MAX plays 'O'
        // +90 if 'O' wins, -90 'X' wins,
        // +10 if three 'O' in a row, -5 three 'X' in a row,
        // +4 if two 'O' in a row, -1 two 'X' in a row
        int Xlines = 0;
        int Olines = 0;
        if (checkWinState()) {
            if (winner == X) {
                Xlines = Xlines + 90;
            } else {
                Olines = Olines + 90;
            }
        }
        Xlines = Xlines + check3In(X) * 10 + check2In(X) * 4;
        Olines = Olines + check3In(O) * 5 + check2In(O);
        return Olines - Xlines;
    }//end utilityFunction

    //Is there a possible winner movement? (4In)
    /**
     * Checks whether either player has connected four pieces.
     *
     * Time:  O(R * C) - scans horizontal, vertical, and diagonal windows.
     * Space: O(1) - updates winner metadata only.
     *
     * @return true when a winner exists
     */
    public boolean checkWinState() {
        //Case if we have 4-row
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 4; j++) {
                if (gameBoard[i][j] == gameBoard[i][j + 1] && gameBoard[i][j] == gameBoard[i][j + 2] && gameBoard[i][j] == gameBoard[i][j + 3] && gameBoard[i][j] != EMPTY) {
                    setWinner(gameBoard[i][j]);
                    setWinnerMethod("Winner by row.");
                    return true;
                }
            }
        }

        //Case we have a 4-column
        for (int i = 5; i >= 3; i--) {
            for (int j = 0; j < 7; j++) {
                if (gameBoard[i][j] == gameBoard[i - 1][j] && gameBoard[i][j] == gameBoard[i - 2][j] && gameBoard[i][j] == gameBoard[i - 3][j] && gameBoard[i][j] != EMPTY) {
                    setWinner(gameBoard[i][j]);
                    setWinnerMethod("Winner by column.");
                    return true;
                }
            }
        }

        //Case we have an ascendent 4-diagonal
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (gameBoard[i][j] == gameBoard[i + 1][j + 1] && gameBoard[i][j] == gameBoard[i + 2][j + 2] && gameBoard[i][j] == gameBoard[i + 3][j + 3] && gameBoard[i][j] != EMPTY) {
                    setWinner(gameBoard[i][j]);
                    setWinnerMethod("Winner by diagonal.");
                    return true;
                }
            }
        }

        //Case we have an descendent 4-diagonal
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 3, j + 3)) {
                    if (gameBoard[i][j] == gameBoard[i - 1][j + 1] && gameBoard[i][j] == gameBoard[i - 2][j + 2] && gameBoard[i][j] == gameBoard[i - 3][j + 3] && gameBoard[i][j] != EMPTY) {
                        setWinner(gameBoard[i][j]);
                        setWinnerMethod("Winner by diagonal.");
                        return true;
                    }
                }
            }
        }
        //There is no winner yet :(
        setWinner(0);
        return false;
    }//end checkWinState

    //Checks if there are 3 pieces of a same player
    public int check3In(int player) {
        int times = 0;
        //In row
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i, j + 2)) {
                    if (gameBoard[i][j] == gameBoard[i][j + 1] && gameBoard[i][j] == gameBoard[i][j + 2] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //In column
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 2, j)) {
                    if (gameBoard[i][j] == gameBoard[i - 1][j] && gameBoard[i][j] == gameBoard[i - 2][j] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //In diagonal ascendent
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i + 2, j + 2)) {
                    if (gameBoard[i][j] == gameBoard[i + 1][j + 1] && gameBoard[i][j] == gameBoard[i + 2][j + 2] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //In diagonal descendent
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 2, j + 2)) {
                    if (gameBoard[i][j] == gameBoard[i - 1][j + 1] && gameBoard[i][j] == gameBoard[i - 2][j + 2] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }
        return times;
    }//end check3In

    //Checks if there are 2 pieces of a same player
    public int check2In(int player) {
        int times = 0;
        //In a row
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i, j + 1)) {
                    if (gameBoard[i][j] == gameBoard[i][j + 1] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //In a column
        for (int i = 5; i >= 0; i--) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 1, j)) {
                    if (gameBoard[i][j] == gameBoard[i - 1][j] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //In a diagonal ascendent
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i + 1, j + 1)) {
                    if (gameBoard[i][j] == gameBoard[i + 1][j + 1] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }

        //In a diagonal descendent
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (canMove(i - 1, j + 1)) {
                    if (gameBoard[i][j] == gameBoard[i - 1][j + 1] && gameBoard[i][j] == player) {
                        times++;
                    }
                }
            }
        }
        return times;
    }//end check2In

    /**
     * Returns whether the board has a winner or no remaining empty cells.
     *
     * Time:  O(R * C) - checks wins and scans for empties.
     * Space: O(1) - no extra storage.
     *
     * @return true if the game is finished
     */
    public boolean checkGameOver() {
        //If there is a winner, we need to know it
        if (checkWinState()) {
            return true;
        }
        //Are there blank spaces in the board?
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                if (gameBoard[row][col] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }//end checkGameOver

    //Print the board
    /**
     * Prints the board to standard output.
     *
     * Time:  O(R * C) - prints each cell once.
     * Space: O(1) - streams output directly.
     */
    public void printBoard() {
        System.out.println("| 1 | 2 | 3 | 4 | 5 | 6 | 7 |");
        System.out.println();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (gameBoard[i][j] == 1) {
                    System.out.print("| " + "X "); //Blue for user
                } else if (gameBoard[i][j] == -1) {
                    System.out.print("| " + "O "); //Red for computer
                } else {
                    System.out.print("| " + "-" + " ");
                }
            }
            System.out.println("|"); //End of each row
        }
    }//end printBoard

    public static void main(String[] args) {
        State board = new State();
        int[] columns = {0, 1, 0, 1, 0, 1, 0};
        int[] letters = {X, O, X, O, X, O, X};
        for (int i = 0; i < columns.length; i++) {
            board.makeMove(columns[i], letters[i]);
        }

        boolean gotWin = board.checkWinState();
        System.out.printf("moves=vertical-X -> %s  expected=true%n", gotWin);
        System.out.printf("winner -> %d  expected=%d%n", board.winner, X);
    }
}//end State
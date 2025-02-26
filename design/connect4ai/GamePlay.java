package design.connect4ai;

/**
 * This class represents the game play actions for the Connect 4 AI, including moves and comparisons for the Minimax algorithm.
 */
public class GamePlay {
    private int row;
    private int col;
    private int value; // Value of the utility function

    public GamePlay() {
        this.row = -1;
        this.col = -1;
        this.value = 0;
    }

    /**
     * Represents a move that has been made.
     * @param row The row of the move.
     * @param col The column of the move.
     * @return A new GamePlay instance representing the move.
     */
    public GamePlay moveDone(int row, int col) {
        GamePlay moveDone = new GamePlay();
        moveDone.row = row;
        moveDone.col = col;
        moveDone.value = -1;
        return moveDone;
    }

    /**
     * Represents a possible move for expansion with a utility value.
     * @param row The row of the move.
     * @param col The column of the move.
     * @param value The utility value of the move.
     * @return A new GamePlay instance representing the possible move.
     */
    public GamePlay possibleMove(int row, int col, int value) {
        GamePlay possibleMove = new GamePlay();
        possibleMove.row = row;
        possibleMove.col = col;
        possibleMove.value = value;
        return possibleMove;
    }

    /**
     * Represents a move used for comparison in the Minimax algorithm.
     * @param value The utility value of the move.
     * @return A new GamePlay instance representing the move for comparison.
     */
    public GamePlay moveToCompare(int value) {
        GamePlay moveToCompare = new GamePlay();
        moveToCompare.row = -1;
        moveToCompare.col = -1;
        moveToCompare.value = value;
        return moveToCompare;
    }

    public int getValue() {
        return value;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}

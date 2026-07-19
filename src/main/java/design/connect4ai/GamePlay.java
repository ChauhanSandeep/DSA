package design.connect4ai;

/**
 * Problem: Connect Four AI Move Record
 *
 * Represent a candidate or completed Connect Four move for the minimax player. The
 * object stores row, column, and heuristic value so recursive search can compare
 * moves without changing the public data layout.
 *
 * Pattern:  Design | Value object | Minimax move metadata
 *
 * Example:
 *   Input:  possibleMove(2, 3, 10)
 *   Output: row = 2, col = 3, value = 10
 *   Why:    the returned record copies the requested coordinates and score.
 *
 * Follow-ups:
 *   1. How would you make move records immutable?
 *      Set fields in a constructor and remove setters.
 *   2. How would you compare equal-valued moves deterministically?
 *      Add a tie-break rule such as center-column preference.
 *   3. How would you store principal variation paths?
 *      Add a parent pointer or a list of subsequent best moves.
 */
public class GamePlay {
    public int row;
    public int col;
    public int value; // Value of the utility function

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

    public static void main(String[] args) {
        GamePlay factory = new GamePlay();
        GamePlay done = factory.moveDone(2, 3);
        int[] gotDone = {done.getRow(), done.getCol(), done.getValue()};
        int[] expectedDone = {2, 3, -1};
        System.out.printf("moveDone(2,3) -> %s  expected=%s%n",
                java.util.Arrays.toString(gotDone), java.util.Arrays.toString(expectedDone));

        GamePlay possible = factory.possibleMove(1, 4, 9);
        int[] gotPossible = {possible.getRow(), possible.getCol(), possible.getValue()};
        int[] expectedPossible = {1, 4, 9};
        System.out.printf("possibleMove(1,4,9) -> %s  expected=%s%n",
                java.util.Arrays.toString(gotPossible), java.util.Arrays.toString(expectedPossible));
    }
}

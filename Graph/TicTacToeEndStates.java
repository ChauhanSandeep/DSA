package Graph;

import java.util.*;

/**
 * **Tic-Tac-Toe End States Calculator**
 *
 * ### **Problem Statement:**
 * Find the total number of unique **end states** in an `N x N` Tic-Tac-Toe game.
 * An **end state** is a board configuration where:
 * - A player has won, or
 * - The board is completely filled (draw).
 *
 * ### **Approach:**
 * - **Backtracking with DFS** is used to explore all possible game states.
 * - The board is represented as a **string**.
 * - We track visited states to **avoid redundant calculations**.
 * - We check for winning conditions using **row, column, and diagonal sums**.
 *
 * ### **Complexity Analysis:**
 * - **Time Complexity:** `O(9!)` for a 3x3 board (worst case), as we explore all possible board states.
 * - **Space Complexity:** `O(N^2)` for storing board configurations.
 */
public class TicTacToeEndStates {

    private final Set<String> endStatesSet = new HashSet<>(); // Stores unique end states

    public static void main(String[] args) {
        int boardSize = 3;
        TicTacToeEndStates solver = new TicTacToeEndStates();
        int totalEndStates = solver.countEndStates(boardSize);
        System.out.println("Total unique end states: " + totalEndStates);
    }

    /**
     * **Counts the total unique end states of an N x N Tic-Tac-Toe game.**
     *
     * @param size Board size (N)
     * @return Number of unique end states
     */
    public int countEndStates(int size) {
        String board = initializeBoard(size);
        Set<String> visitedStates = new HashSet<>();

        int totalEndStates = exploreGameStates(board, 0, visitedStates, 1, size); // Start with 'X'
        System.out.println("End states:\n" + endStatesSet);
        return totalEndStates;
    }

    /**
     * **Recursively explores all possible Tic-Tac-Toe board states using DFS.**
     *
     * @param board         Current board state (encoded as a string)
     * @param movesPlayed   Number of moves made so far
     * @param visitedStates Set of already visited board configurations
     * @param player        Current player (1 = 'X', -1 = 'O')
     * @param size          Board size (N)
     * @return Count of unique end states found
     */
    private int exploreGameStates(String board, int movesPlayed, Set<String> visitedStates, int player, int size) {
        if (visitedStates.contains(board)) return 0;

        // If board is in an end state, add it to result set
        if (isEndState(board, size)) {
            endStatesSet.add(board);
            return 1;
        }

        // If board is full, stop recursion
        if (movesPlayed == size * size) return 0;

        List<String> nextBoards = generateNextStates(board, player);
        int totalEndStates = 0;

        for (String nextBoard : nextBoards) {
            totalEndStates += exploreGameStates(nextBoard, movesPlayed + 1, visitedStates, -player, size);
            visitedStates.add(nextBoard);
        }

        visitedStates.add(board);
        return totalEndStates;
    }

    /**
     * **Checks if a given board configuration is an end state (win or draw).**
     *
     * @param board Board state as a string
     * @param size  Board size (N)
     * @return `true` if the board is an end state, `false` otherwise
     */
    public boolean isEndState(String board, int size) {
        int[] rowSum = new int[size];
        int[] colSum = new int[size];
        int mainDiagSum = 0, antiDiagSum = 0;
        int totalMoves = 0;

        for (int i = 0; i < size * size; i++) {
            char cell = board.charAt(i);
            if (cell == 'x') {
                rowSum[i / size]++;
                colSum[i % size]++;
                if (i / size == i % size) mainDiagSum++;
                if (i / size + i % size == size - 1) antiDiagSum++;
                totalMoves++;
            } else if (cell == 'o') {
                rowSum[i / size]--;
                colSum[i % size]--;
                if (i / size == i % size) mainDiagSum--;
                if (i / size + i % size == size - 1) antiDiagSum--;
                totalMoves++;
            }
        }

        // Check for a win (any row, column, or diagonal fully marked)
        for (int sum : rowSum) if (Math.abs(sum) == size) return true;
        for (int sum : colSum) if (Math.abs(sum) == size) return true;
        if (Math.abs(mainDiagSum) == size || Math.abs(antiDiagSum) == size) return true;

        // If board is full, it's a draw (end state)
        return totalMoves == size * size;
    }

    /**
     * **Generates the next possible board states given the current state.**
     *
     * @param board  Current board state
     * @param player Current player (1 = 'X', -1 = 'O')
     * @return List of next possible board configurations
     */
    private List<String> generateNextStates(String board, int player) {
        char marker = (player == 1) ? 'x' : 'o';
        List<String> nextStates = new ArrayList<>();

        for (int i = 0; i < board.length(); i++) {
            if (board.charAt(i) == '-') {
                nextStates.add(board.substring(0, i) + marker + board.substring(i + 1));
            }
        }
        return nextStates;
    }

    /**
     * **Initializes an empty Tic-Tac-Toe board of size `N x N` as a string.**
     *
     * @param size Board size (N)
     * @return Encoded string representing an empty board
     */
    private String initializeBoard(int size) {
        return String.join("", Collections.nCopies(size * size, "-"));
    }
}

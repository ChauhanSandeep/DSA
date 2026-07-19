package graphs;

import java.util.*;

/**
 * Problem: Tic-Tac-Toe End States
 *
 * For an n by n Tic-Tac-Toe board, count the unique board positions where the
 * game is over. A board is an end state when either player has a full row,
 * column, or diagonal, or when the board is full.
 *
 * Pattern:  Graph | Game-state DFS | Backtracking with terminal-state dedupe
 *
 * Example:
 *   Input:  size = 1
 *   Output: 1
 *   Why:    the first move fills the only cell and also wins, so there is exactly
 *           one possible final board.
 *
 * Follow-ups:
 *   1. Count distinct games instead of distinct final boards?
 *      Do not dedupe by board string; count every move sequence that reaches a terminal state.
 *   2. Return final boards split by X win, O win, and draw?
 *      Classify each terminal board before inserting it into result buckets.
 *   3. Scale beyond 3 by 3?
 *      The state space grows factorially, so add symmetry reduction or memoization.
 */
public class TicTacToeEndStates {

    public static void main(String[] args) {
        TicTacToeEndStates solver = new TicTacToeEndStates();
        int[] sizes = {1, 2};
        int[] expected = {1, 24};
        for (int i = 0; i < sizes.length; i++) {
            int output = solver.countEndStates(sizes[i]);
            System.out.printf("size=%d -> %d  expected=%d%n", sizes[i], output, expected[i]);
        }
    }

    private final Set<String> endStatesSet = new HashSet<>(); // Stores unique end states


    /**
     * Counts the number of unique end states for an N x N Tic-Tac-Toe board.
     *
     * --- Steps ---
     * 1. Initialize an empty board represented as a string of '-'.
     * 2. Use DFS to simulate every valid move sequence starting from 'X'.
     * 3. Check whether a board state ends the game (either by win or draw).
     * 4. Track all unique end states in a set.
     *
     * Time: O((N^2)!), Space: O(N^2)
     *
     * @param size Board size (N)
     * @return Total count of unique end-game board states.
     */
    public int countEndStates(int size) {
        String board = initializeBoard(size);
        Set<String> visitedStates = new HashSet<>();

        visitedStates.add(board); // Start with the initial empty board
        return exploreGameStates(board, visitedStates, 1, size); // Start with 'X'
    }

    /**
     * **Recursively explores all possible Tic-Tac-Toe board states using DFS.**
     *
     * @param board         Current board state (encoded as a string)
     * @param visitedStates Set of already visited board configurations
     * @param player        Current player (1 = 'X', -1 = 'O')
     * @param size          Board size (N)
     * @return Count of unique end states found
     */
    private int exploreGameStates(String board, Set<String> visitedStates, int player, int size) {
        // Step 1. Check terminal state
        if (isEndState(board, size)) {
            endStatesSet.add(board);
            return 1;
        }

        // Step 2: Generate next possible board states by placing the current player's marker in different empty cells
        List<String> nextBoards = generateNextPossibleBoards(board, player);
        int endStatesCount = 0;

        for (String nextBoard : nextBoards) {
            visitedStates.add(nextBoard);
            endStatesCount += exploreGameStates(nextBoard, visitedStates, -player, size);
        }

        return endStatesCount;
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
    private List<String> generateNextPossibleBoards(String board, int player) {
        char marker = (player == 1) ? 'x' : 'o';
        List<String> nextBoards = new ArrayList<>();

        for (int i = 0; i < board.length(); i++) {
            if (board.charAt(i) == '-') {
                nextBoards.add(board.substring(0, i) + marker + board.substring(i + 1));
            }
        }
        return nextBoards;
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

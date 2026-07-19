package graphs;

import java.util.*;

/**
 * Problem: Sliding Puzzle
 *
 * On a 2 by 3 board, tiles 1 through 5 and blank 0 can be swapped with the
 * blank's four-directional neighbor. Return the fewest moves needed to reach
 * [[1,2,3],[4,5,0]], or -1 if the board cannot be solved.
 *
 * Leetcode: https://leetcode.com/problems/sliding-puzzle/
 * Rating:   1815 (zerotrac Elo)
 * Pattern:  Graph | BFS over board states | Permutation reachability
 *
 * Example:
 *   Input:  board = [[1,2,3],[4,0,5]]
 *   Output: 1
 *   Why:    swapping the blank with tile 5 immediately produces the target board.
 *
 * Follow-ups:
 *   1. Return the move sequence?
 *      Store parent state and swapped tile for each BFS discovery.
 *   2. Solve a 4 by 4 puzzle?
 *      Use A* with Manhattan distance; plain BFS is too large.
 *   3. Precompute answers for all 2 by 3 states?
 *      Run BFS once from the target and store distance for every reachable permutation.
 *
 * Related: Shortest Path to Get All Keys (864), Open the Lock (752).
 */
public class SlidingPuzzle {

    public static void main(String[] args) {
        SlidingPuzzle solver = new SlidingPuzzle();
        int[][][] boards = {{{1, 2, 3}, {4, 0, 5}}, {{1, 2, 3}, {5, 4, 0}}};
        int[] expected = {1, -1};
        for (int i = 0; i < boards.length; i++) {
            int output = solver.slidingPuzzle(boards[i]);
            System.out.printf("board=%s -> %d  expected=%d%n", Arrays.deepToString(boards[i]), output, expected[i]);
        }
    }

    private static final String TARGET = "123450";
    private static final int ROWS = 2;
    private static final int COLS = 3;

    /**
     * Solves sliding puzzle using BFS to find minimum moves.
     *
     * Algorithm:
     * 1. Convert 2D board to string representation for easy state tracking
     * 2. Use BFS to explore all possible moves from current state
     * 3. For each state, find position of 0 and try swapping with adjacent numbers
     * 4. Track visited states to avoid cycles
     * 5. Return steps when target state is reached
     *
     * Time Complexity: O(6!) = O(720) for 2x3 board (all possible permutations)
     * Space Complexity: O(6!) for visited states and queue
     *
     * @param board 2x3 board with numbers 0-5
     * @return Minimum moves to solve, -1 if impossible
     */
    public int slidingPuzzle(int[][] board) {
        if (board == null || board.length != ROWS || board[0].length != COLS) {
            return -1;
        }

        String start = boardToString(board); // like "123405"

        // Early termination if already solved
        if (start.equals(TARGET)) {
            return 0;
        }

        // Check if puzzle is solvable
        if (!isSolvable(start)) {
            return -1;
        }

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.offer(start);
        visited.add(start);

        int moves = 0;

        // Adjacent positions for each index in flattened 2x3 board
        // Index mapping: 0->0,0  1->0,1  2->0,2  3->1,0  4->1,1  5->1,2
        int[][] adjacentPositions = {
            {1, 3},       // 0 can swap with 1, 3
            {0, 2, 4},    // 1 can swap with 0, 2, 4
            {1, 5},       // 2 can swap with 1, 5
            {0, 4},       // 3 can swap with 0, 4
            {1, 3, 5},    // 4 can swap with 1, 3, 5
            {2, 4}        // 5 can swap with 2, 4
        };

        while (!queue.isEmpty()) {
            int queueSize = queue.size();
            moves++;

            for (int i = 0; i < queueSize; i++) {
                String current = queue.poll();

                // Find position of 0
                int zeroPos = current.indexOf('0');

                // Try all possible swaps
                for (int adjacentPos : adjacentPositions[zeroPos]) {
                    String nextState = swap(current, zeroPos, adjacentPos);

                    if (nextState.equals(TARGET)) {
                        return moves;
                    }

                    if (!visited.contains(nextState)) {
                        visited.add(nextState);
                        queue.offer(nextState);
                    }
                }
            }
        }

        return -1;
    }

    // Convert 2D board to string representation
    private String boardToString(int[][] board) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                builder.append(board[i][j]);
            }
        }
        return builder.toString();
    }

    // Swap characters at two positions in string
    private String swap(String board, int source, int target) {
        char[] charArray = board.toCharArray();
        char temp = charArray[source];
        charArray[source] = charArray[target];
        charArray[target] = temp;
        return new String(charArray);
    }

    // For 2x3 board, puzzle is solvable if number of inversions is even
    // Inversion: a pair (a,b) such that a appears before b but a > b
    private boolean isSolvable(String state) {
        int inversions = 0;
        String updatedState = state.replace("0", "");

        // Puzzle is solvable if number of inversions is even
        for (int i = 0; i < updatedState.length(); i++) {
            for (int j = i + 1; j < updatedState.length(); j++) {
                if (updatedState.charAt(i) > updatedState.charAt(j)) {
                    inversions++;
                }
            }
        }

        return inversions % 2 == 0;
    }
}
package graphs;

import java.util.*;

/**
 * Problem: Sliding Puzzle
 *
 * On a 2x3 board, there are 5 tiles labeled from 1 to 5, and an empty square represented by 0.
 * A move consists of choosing 0 and a 4-directionally adjacent number and swapping it.
 * The state of the board is solved if and only if the board is [[1,2,3],[4,5,0]].
 * Given the puzzle board, return the least number of moves required so that the state of the board is solved.
 * If it is impossible for the state of the board to be solved, return -1.
 *
 * Example:
 * Input: board = [
 *      [1,2,3],
 *      [4,0,5]
 * ]
 * Output: 1
 * Explanation: Swap the 0 and the 5 in one move.
 * Result board : [
 *      [1,2,3],
 *      [4,5,0]
 * ]
 *
 * LeetCode: https://leetcode.com/problems/sliding-puzzle
 *
 * Follow-up Questions:
 * 1. How would you handle a larger board (e.g., 4x4)?
 *    Answer: Same BFS approach works, but state space grows exponentially. Consider A* with Manhattan distance.
 *
 * 2. What if we need to find the actual sequence of moves?
 *    Answer: Store parent pointers in BFS to reconstruct the path.
 *
 * 3. How would you determine if a puzzle is solvable before starting BFS?
 *    Answer: Count inversions in the puzzle. For 2x3 board, even inversions mean solvable.
 *    Related: https://leetcode.com/problems/shortest-path-to-get-all-keys/
 *
 * @author Sandeep
 */
public class SlidingPuzzle {

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

    // Check if puzzle is solvable using inversion count
    private boolean isSolvable(String state) {
        int inversions = 0;
        String withoutZero = state.replace("0", "");

        // Puzzle is solvable if number of inversions is even
        for (int i = 0; i < withoutZero.length(); i++) {
            for (int j = i + 1; j < withoutZero.length(); j++) {
                if (withoutZero.charAt(i) > withoutZero.charAt(j)) {
                    inversions++;
                }
            }
        }

        return inversions % 2 == 0;
    }
}
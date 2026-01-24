package graphs;

import java.util.*;

/**
 * Problem: Snakes and Ladders
 *
 * You are given an n x n integer matrix board where the cells are labeled from 1 to n² in a
 * Boustrophedon style starting from the bottom left of the board and alternating direction each row.
 * You start on square 1 of the board. In each move, starting from square curr, do the following:
 * Choose a destination square next with a label in the range [curr + 1, min(curr + 6, n²)].
 * If next has a snake or ladder, you must move to the destination of that snake or ladder.
 * The game ends when you reach the square n².
 * Return the least number of moves required to reach the square n². If it is not possible, return -1.
 *
 * Example:
 * Input: board = [[-1,-1,-1,-1,-1,-1],[-1,-1,-1,-1,-1,-1],[-1,-1,-1,-1,-1,-1],[-1,-1,-1,-1,-1,-1],[-1,-1,-1,-1,-1,-1],[-1,-1,-1,-1,-1,-1]]
 * Output: 2
 * Explanation: In the first move, move from start to square 2. In the second move, move from square 2 to square 12.
 *
 * LeetCode: https://leetcode.com/problems/snakes-and-ladders
 *
 * Follow-up Questions:
 * 1. What if we want to find the path with maximum probability of winning?
 *    Answer: Use Dijkstra's algorithm with negative log probabilities or dynamic programming.
 *
 * 2. How would you handle multiple players on the same board?
 *    Answer: Extend state space to include all player positions and use game theory approaches.
 *
 * 3. What if the dice can have different number of faces?
 *    Answer: Modify the range from [curr+1, curr+6] to [curr+1, curr+faces].
 *    Related: https://leetcode.com/problems/minimum-jumps-to-reach-home/
 *
 * @author Sandeep
 * LeetCode Contest Rating: 2020
 */
public class SnakesAndLadders {

    /**
     * Finds minimum moves to reach the end using BFS.
     *
     * Algorithm:
     * 1. Convert 2D board to 1D array using Boustrophedon numbering
     * 2. Use BFS to explore all possible dice rolls from each position
     * 3. For each position, try all dice outcomes (1-6)
     * 4. If destination has snake/ladder, follow it automatically
     * 5. Track visited positions to avoid cycles
     * 6. Return moves when reaching the final square
     *
     * Time Complexity: O(n²) where n is board dimension
     * Space Complexity: O(n²) for visited array and queue
     *
     * @param board n x n board with snakes and ladders
     * @return Minimum moves to reach end, -1 if impossible
     */
    public int snakesAndLadders(int[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) {
            return -1;
        }

        int length = board.length;
        int target = length * length;

        // Convert 2D board to 1D for easier navigation
        int[] flatBoard = convertTo1D(board);

        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[target + 1];

        queue.offer(1);
        visited[1] = true;

        int moves = 0;

        while (!queue.isEmpty()) {
            int queueSize = queue.size();
            moves++;

            for (int i = 0; i < queueSize; i++) {
                int current = queue.poll();

                // Try all possible dice rolls (1-6)
                for (int dice = 1; dice <= 6; dice++) {
                    int next = current + dice;

                    // Check if we've reached or passed the target
                    if (next >= target) {
                        if (next == target) {
                            return moves;
                        }
                        continue; // Skip if we overshoot
                    }

                    // Follow snake or ladder if present
                    int destination = flatBoard[next] != -1 ? flatBoard[next] : next;

                    // Check if we reached target after following snake/ladder
                    if (destination == target) {
                        return moves;
                    }

                    // Add to queue if not visited
                    if (!visited[destination]) {
                        visited[destination] = true;
                        queue.offer(destination);
                    }
                }
            }
        }

        return -1;
    }

    // Convert 2D board to 1D array using Boustrophedon numbering
    private int[] convertTo1D(int[][] board) {
        int length = board.length;
        int[] flatBoard = new int[length * length + 1];

        for (int row = 0; row < length; row++) {
            for (int col = 0; col < length; col++) {
                // Calculate position based on Boustrophedon pattern
                int position;
                if (row % 2 == 0) {
                    // Even rows: left to right
                    position = ((length - 1 - row) * length) + (col + 1);
                } else {
                    // Odd rows: right to left
                    position = ((length - 1 - row) * length) + ((length - 1 - col) + 1);
                }
                flatBoard[position] = board[row][col];
            }
        }

        return flatBoard;
    }

    // Convert position to 2D board coordinates
    private int[] positionToCoordinates(int position, int n) {
        position--; // Convert to 0-based indexing

        int row = n - 1 - position / n;
        int col;

        if ((n - 1 - row) % 2 == 0) {
            // Even rows (from bottom): left to right
            col = position % n;
        } else {
            // Odd rows (from bottom): right to left
            col = n - 1 - (position % n);
        }

        return new int[]{row, col};
    }
}
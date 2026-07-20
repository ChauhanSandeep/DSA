package graphs;

import java.util.*;

/**
 * Problem: Snakes and Ladders
 *
 * Given an n by n board numbered from 1 to n*n in alternating left-to-right and
 * right-to-left rows from the bottom, return the fewest dice moves needed to
 * reach the final square. A snake or ladder is followed at most once per move.
 *
 * Leetcode: https://leetcode.com/problems/snakes-and-ladders/
 * Rating:   2020 (zerotrac Elo)
 * Pattern:  Graph | BFS on numbered squares | Boustrophedon board mapping
 *
 * Example:
 *   Input:  board = [[-1,-1],[-1,3]]
 *   Output: 1
 *   Why:    from square 1, rolling a 1 reaches square 2, then the ladder sends the
 *           player to square 3; rolling further can also finish in one move.
 *
 * Follow-ups:
 *   1. Return the actual squares visited?
 *      Store parent square and dice result for each BFS discovery.
 *   2. Dice has f faces instead of 6?
 *      Change the transition range to current + 1 through current + f.
 *   3. Snakes or ladders can chain in one move?
 *      Keep following destinations until a normal square or a repeated square is reached.
 *
 * Related: Open the Lock (752), Minimum Jumps to Reach Home (1654), Sliding Puzzle (773).
 */
public class SnakesAndLadders {

    public static void main(String[] args) {
        SnakesAndLadders solver = new SnakesAndLadders();
        int[][][] boards = {{{-1}}, {{-1, -1}, {-1, 3}}, {{-1, -1}, {-1, -1}}};
        int[] expected = {0, 1, 1};
        for (int i = 0; i < boards.length; i++) {
            int output = solver.snakesAndLadders(boards[i]);
            System.out.printf("board=%s -> %d  expected=%d%n", Arrays.deepToString(boards[i]), output, expected[i]);
        }
    }

    /**
     * Finds minimum moves to reach the end using BFS.
     *
     * Algorithm:
     * 1. Return 0 immediately when the start square is already the final square.
     * 2. Convert 2D board to 1D array using Boustrophedon numbering.
     * 3. Use BFS to explore all possible dice rolls from each position.
     * 4. If destination has snake/ladder, follow it automatically.
     * 5. Track visited positions to avoid cycles.
     * 6. Return moves when reaching the final square.
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
        if (target == 1) {
            return 0;
        }

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
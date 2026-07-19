package graphs;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Problem: Knight on Chess Board
 *
 * Given a board size and one-indexed start and target cells, return the minimum
 * number of knight moves needed to reach the target. Return -1 when the target
 * cannot be reached.
 *
 * Source: InterviewBit - https://www.interviewbit.com/problems/knight-on-chess-board/
 * Pattern:  Graph | BFS shortest path | Implicit board graph
 *
 * Example:
 *   Input:  rows = 8, cols = 8, start = (1,1), target = (8,8)
 *   Output: 6
 *   Why:    each knight move is one unweighted edge, and BFS finds the shortest
 *           sequence of six legal moves across the board.
 *
 * Follow-ups:
 *   1. Return one actual shortest path?
 *      Store parent coordinates for each visited square and reconstruct from target.
 *   2. Handle obstacles on the board?
 *      Treat blocked squares as invalid neighbors during BFS.
 *   3. Answer many queries on the same board?
 *      Precompute BFS distances from common sources or use bidirectional BFS per query.
 */
public class KnightInChessBoard {


    public static void main(String[] args) {
        KnightInChessBoard solver = new KnightInChessBoard();
        int[][] inputs = {{8, 8, 1, 1, 8, 8}, {2, 2, 1, 1, 2, 2}};
        int[] expected = {6, -1};

        for (int i = 0; i < inputs.length; i++) {
            int[] in = inputs[i];
            int output = solver.knightMoves(in[0], in[1], in[2], in[3], in[4], in[5]);
            System.out.printf("rows=%d cols=%d start=(%d,%d) target=(%d,%d)  ->  %d  expected=%d%n",
                in[0], in[1], in[2], in[3], in[4], in[5], output, expected[i]);
        }
    }
    // Possible moves of a Knight in chess (L-shaped moves)
    private static final int[][] MOVES = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {2, -1}, {2, 1}, {1, -2}, {1, 2}
    };
    /**
     * Intuition: every knight move has equal cost, so BFS from the starting square
     * finds the shortest move count. The first time the destination is dequeued is
     * the minimum number of moves because BFS explores the board level by level.
     *
     * Algorithm:
     *   1. Enqueue the starting cell with distance 0 and mark it visited.
     *   2. Repeatedly pop a cell from the queue.
     *   3. Return its distance when it matches the target cell.
     *   4. Enqueue every in-bounds, unvisited knight move with distance + 1.
     *
     * Time:  O(rows*cols) - each board cell can be visited once.
     * Space: O(rows*cols) - visited storage and BFS queue.
     *
     * @param rows number of board rows
     * @param cols number of board columns
     * @param startX starting row
     * @param startY starting column
     * @param endX target row
     * @param endY target column
     * @return minimum knight moves, or -1 when unreachable
     */
    public int knightMoves(int rows, int cols, int startX, int startY, int endX, int endY) {
        // Edge Case: If start and end positions are the same, return 0 moves.
        if (startX == endX && startY == endY) return 0;

        // Convert to 0-based index for easier array handling
        startX--; startY--;
        endX--; endY--;

        // BFS Queue: Stores {row, col, moves}
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];

        queue.offer(new int[]{startX, startY, 0});
        visited[startX][startY] = true;

        // Perform BFS
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0], col = current[1], moves = current[2];

            // Explore all 8 possible knight moves
            for (int[] move : MOVES) {
                int newRow = row + move[0];
                int newCol = col + move[1];

                // Check if the new position is within bounds and not visited
                if (isValidMove(newRow, newCol, rows, cols, visited)) {
                    // If we reach the destination, return the number of moves
                    if (newRow == endX && newCol == endY) {
                        return moves + 1;
                    }
                    visited[newRow][newCol] = true;
                    queue.offer(new int[]{newRow, newCol, moves + 1});
                }
            }
        }

        // If BFS completes without finding the destination, return -1
        return -1;
    }

    /**
     * Checks if a knight's move is valid (within bounds and not visited).
     *
     * @param x       Current row.
     * @param y       Current column.
     * @param rows    Total rows in the board.
     * @param cols    Total columns in the board.
     * @param visited Boolean matrix to track visited positions.
     * @return True if the move is valid, otherwise false.
     */
    private boolean isValidMove(int x, int y, int rows, int cols, boolean[][] visited) {
        return x >= 0 && y >= 0 && x < rows && y < cols && !visited[x][y];
    }
}

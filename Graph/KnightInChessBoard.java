package Graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Problem: Find the minimum moves required by a knight to move from source to destination on a chessboard.
 * 
 * Intuition:
 * - The knight moves in an "L" shape, meaning it can jump to up to **8 possible positions** from a given cell.
 * - We need to find the **shortest path** from `(startX, startY)` to `(endX, endY)`, making **Breadth-First Search (BFS)** the ideal choice.
 * - We use a **queue (FIFO) for BFS traversal** and maintain a **visited matrix** to track visited positions.
 * 
 * Algorithm:
 * 1. Use BFS to explore all **valid knight moves** from the start position.
 * 2. Maintain a **visited matrix** to avoid cycles and redundant computations.
 * 3. Return the step count when the destination is reached.
 * 4. If BFS completes and we haven't reached the target, return `-1` (no valid path).
 * 
 * Time Complexity: O(N * M) - In the worst case, we may visit all cells.
 * Space Complexity: O(N * M) - For the visited matrix and BFS queue.
 * 
 * LeetCode Link: [Add relevant problem link if available]
 */
public class KnightInChessBoard {
    public static void main(String[] args) {
        KnightInChessBoard solution = new KnightInChessBoard();
        int moves = solution.knightMoves(8, 8, 1, 1, 8, 8);
        System.out.println("Minimum moves required: " + moves);
    }

    // Possible moves of a Knight in chess (L-shaped moves)
    private static final int[][] MOVES = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {2, -1}, {2, 1}, {1, -2}, {1, 2}
    };

    /**
     * Computes the minimum number of moves required for a knight to reach the destination.
     *
     * @param rows   Total rows in the chessboard.
     * @param cols   Total columns in the chessboard.
     * @param startX Starting row (1-based index).
     * @param startY Starting column (1-based index).
     * @param endX   Destination row (1-based index).
     * @param endY   Destination column (1-based index).
     * @return Minimum number of moves required to reach the destination, or -1 if unreachable.
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

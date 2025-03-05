package DynamicProgramming;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a matrix denoting a chessboard with a few queens, compute
 * the number of queens that can attack each cell (i, j).
 * Assume there is no queen at that cell while calculating the count.
 *
 * Approach:
 * - Use **direction-based traversal** (8 directions) to count attacking queens.
 * - **Avoid recursion** (uses iterative traversal).
 * - **Optimized data structures** (`int[][]` instead of nested lists).
 * 
 * Time Complexity: **O(N × M × 8) ≈ O(N × M)**  
 * Space Complexity: **O(N × M)**
 */
public class QueenAttack {
    
    // 8 possible movement directions for a Queen (Row, Column)
    private static final int[] DX = {0, -1, -1, -1, 0, 1, 1, 1};
    private static final int[] DY = {1, 1, 0, -1, -1, -1, 0, 1};
    
    public static void main(String[] args) {
        List<String> board = List.of(
                "010",
                "100",
                "001"
        );

        List<List<Integer>> result = new QueenAttack().queenAttack(board);
        System.out.println(result);
    }

    public List<List<Integer>> queenAttack(List<String> board) {
        int rows = board.size();
        int cols = board.get(0).length();
        int[][] attackCount = new int[rows][cols];

        // Iterate through every cell to find queens
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board.get(i).charAt(j) == '1') {
                    // Mark attack positions in all 8 directions
                    markAttacks(board, attackCount, i, j, rows, cols);
                }
            }
        }

        // Convert result into List<List<Integer>>
        return convertToList(attackCount);
    }

    private void markAttacks(List<String> board, int[][] attackCount, int x, int y, int rows, int cols) {
        for (int dir = 0; dir < 8; dir++) {
            int i = x + DX[dir], j = y + DY[dir];

            while (i >= 0 && i < rows && j >= 0 && j < cols) {
                attackCount[i][j]++; // Increase attack count
                if (board.get(i).charAt(j) == '1') break; // Stop if another queen is encountered
                i += DX[dir];
                j += DY[dir];
            }
        }
    }

    private List<List<Integer>> convertToList(int[][] matrix) {
        List<List<Integer>> result = new ArrayList<>();
        for (int[] row : matrix) {
            List<Integer> listRow = new ArrayList<>();
            for (int cell : row) {
                listRow.add(cell);
            }
            result.add(listRow);
        }
        return result;
    }
}

package graphs;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Capture all regions of 'O' that are completely surrounded by 'X'.
 * https://www.interviewbit.com/problems/capture-regions-on-board/
 *
 * **Approach:**
 * - Use **DFS (Depth-First Search)** to mark all 'O' cells connected to the border as **safe**.
 * - Convert the remaining 'O' cells (which are completely surrounded) to 'X'.
 * - Restore safe regions back to 'O'.
 *
 * **Time Complexity:** O(R × C) (Each cell is processed at most twice)
 * **Space Complexity:** O(R × C) (Recursive stack for DFS in worst case)
 *
 */
public class CaptureRegions {

    public static void main(String[] args) {
        Character[][] matrix = {
                {'O', 'O', 'O', 'X', 'X', 'X', 'O'},
                {'X', 'X', 'X', 'O', 'O', 'O', 'O'},
                {'X', 'X', 'O', 'X', 'O', 'X', 'O'},
                {'O', 'X', 'O', 'X', 'O', 'X', 'O'},
                {'X', 'X', 'O', 'X', 'O', 'X', 'X'},
                {'X', 'O', 'O', 'O', 'X', 'X', 'O'},
                {'O', 'X', 'X', 'O', 'X', 'O', 'O'},
                {'O', 'X', 'O', 'O', 'X', 'O', 'X'}
        };

        List<List<Character>> board = Arrays.stream(matrix)
                .map(row -> new ArrayList<>(Arrays.asList(row)))
                .collect(Collectors.toList());

        new CaptureRegions().solve(board);
        System.out.println(board);
    }

    /**
     * Captures all surrounded 'O' regions by converting them to 'X'.
     *
     * @param board 2D grid of characters ('O' or 'X').
     */
    public void solve(List<List<Character>> board) {
        if (board == null || board.isEmpty() || board.get(0).isEmpty()) return;

        int rows = board.size();
        int cols = board.get(0).size();

        // Step 1: Mark 'O' regions connected to the boundary as 'safe' (temporary marker '#')
        for (int i = 0; i < rows; i++) {
            if (board.get(i).get(0) == 'O') markSafeRegions(board, i, 0);
            if (board.get(i).get(cols - 1) == 'O') markSafeRegions(board, i, cols - 1);
        }
        for (int j = 0; j < cols; j++) {
            if (board.get(0).get(j) == 'O') markSafeRegions(board, 0, j);
            if (board.get(rows - 1).get(j) == 'O') markSafeRegions(board, rows - 1, j);
        }

        // Step 2: Convert surrounded 'O' regions to 'X' and revert '#' back to 'O'
        flipCapturedRegions(board);
    }

    /**
     * Marks all 'O' cells connected to the boundary as 'safe' using DFS.
     */
    private void markSafeRegions(List<List<Character>> board, int row, int col) {
        if (row < 0 || col < 0 || row >= board.size() || col >= board.get(0).size() || board.get(row).get(col) != 'O') {
            return;
        }

        board.get(row).set(col, '#'); // Temporarily mark as safe

        // Explore all 4 directions
        markSafeRegions(board, row + 1, col);
        markSafeRegions(board, row - 1, col);
        markSafeRegions(board, row, col + 1);
        markSafeRegions(board, row, col - 1);
    }

    /**
     * Flips all remaining 'O' to 'X' (captured regions) and restores '#' back to 'O'.
     */
    private void flipCapturedRegions(List<List<Character>> board) {
        for (int i = 0; i < board.size(); i++) {
            for (int j = 0; j < board.get(0).size(); j++) {
                if (board.get(i).get(j) == 'O') {
                    board.get(i).set(j, 'X'); // Convert completely surrounded 'O' to 'X'
                } else if (board.get(i).get(j) == '#') {
                    board.get(i).set(j, 'O'); // Restore boundary-connected 'O'
                }
            }
        }
    }
}

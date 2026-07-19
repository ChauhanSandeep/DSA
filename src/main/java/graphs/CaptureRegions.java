package graphs;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Problem: Surrounded Regions
 *
 * Given a board of 'X' and 'O', capture every region of O cells that is fully
 * surrounded by X cells. O cells connected to the border cannot be captured
 * because they have an escape path outside the board.
 *
 * Leetcode: https://leetcode.com/problems/surrounded-regions/ (Medium)
 * Rating:   acceptance 45.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | Boundary DFS | Reverse marking
 *
 * Example:
 *   Input:  board = [[X,X,X,X],[X,O,O,X],[X,X,O,X],[X,O,X,X]]
 *   Output: [[X,X,X,X],[X,X,X,X],[X,X,X,X],[X,O,X,X]]
 *   Why:    the lower O touches the border and survives, while the middle O region
 *           is enclosed by X cells and is flipped.
 *
 * Follow-ups:
 *   1. Avoid recursion on a huge board?
 *      Use an explicit queue or stack for the same boundary traversal.
 *   2. Return the captured regions before flipping them?
 *      Collect cells that remain O after boundary marking and before conversion.
 *   3. Support many repeated flips on the same board?
 *      Maintain connected components and whether each component touches the border.
 *
 * Related: Number of Islands (200), Pacific Atlantic Water Flow (417).
 */
public class CaptureRegions {



    public static void main(String[] args) {
        CaptureRegions solver = new CaptureRegions();
        Character[][][] inputs = {
            {{'X', 'X', 'X', 'X'}, {'X', 'O', 'O', 'X'}, {'X', 'X', 'O', 'X'}, {'X', 'O', 'X', 'X'}},
            {{'O'}}
        };
        String[] expected = {
            "[[X, X, X, X], [X, X, X, X], [X, X, X, X], [X, O, X, X]]",
            "[[O]]"
        };

        for (int i = 0; i < inputs.length; i++) {
            List<List<Character>> board = new ArrayList<>();
            for (Character[] row : inputs[i]) {
                board.add(new ArrayList<>(Arrays.asList(row)));
            }
            solver.solve(board);
            System.out.printf("board=%s  ->  %s  expected=%s%n",
                Arrays.deepToString(inputs[i]), board, expected[i]);
        }
    }
    /**
     * Intuition: an O-region is captured only if it cannot reach the border. The
     * code protects border-connected O cells first, then flips whatever O cells
     * remain inside. Protected cells are restored after the capture pass.
     *
     * Algorithm:
     *   1. Run DFS from every border O to mark safe cells.
     *   2. Scan the whole board and flip unmarked O cells to X.
     *   3. Restore the marked safe cells back to O.
     *
     * Time:  O(m*n) - each board cell is visited a constant number of times.
     * Space: O(m*n) - recursive DFS can hold a border-connected region on the stack.
     *
     * @param board mutable board represented as rows of characters
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

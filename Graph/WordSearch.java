package Graph;

/**
 * Word Search (LeetCode #79) - https://leetcode.com/problems/word-search/
 * 
 * Given an m x n board and a word, return true if the word exists in the grid.
 * The word must be constructed from adjacent letters, without revisiting a cell.
 *
 * Approach:
 * - Use **DFS with backtracking** to explore all possible paths for the word.
 * - If a character doesn't match or goes out of bounds, backtrack.
 * - Temporarily mark visited cells and restore them after exploring.
 *
 * Time Complexity: O(M * N * 4^L) - Worst case explores 4 directions for each letter.
 * Space Complexity: O(L) - Depth of recursion stack (L = word length).
 */
public class WordSearch {

    public static void main(String[] args) {
        char[][] board = {
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'}
        };

        System.out.println(wordExists(board, "ABCCED")); // true
        System.out.println(wordExists(board, "SEE"));    // true
        System.out.println(wordExists(board, "ABCB"));   // false
    }

    /**
     * Checks if the given word exists in the board.
     *
     * @param board 2D character grid
     * @param word  Target word to search
     * @return true if word exists, false otherwise
     */
    public static boolean wordExists(char[][] board, String word) {
        int rows = board.length, cols = board[0].length;

        // Start DFS search from every cell
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (dfs(board, word, 0, row, col)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Performs Depth-First Search (DFS) with backtracking.
     *
     * @param board  The character grid
     * @param word   The target word
     * @param index  Current character index in the word
     * @param row    Current row position
     * @param col    Current column position
     * @return true if word is found, false otherwise
     */
    private static boolean dfs(char[][] board, String word, int index, int row, int col) {
        // Base case: If entire word is matched
        if (index == word.length()) {
            return true;
        }

        // Boundary checks & character match validation
        if (row < 0 || col < 0 || row >= board.length || col >= board[0].length || board[row][col] != word.charAt(index)) {
            return false;
        }

        // Mark the cell as visited (to avoid reuse in the current search path)
        char temp = board[row][col];
        board[row][col] = '#';

        // Explore all 4 possible directions
        boolean found = dfs(board, word, index + 1, row + 1, col) || // Down
                        dfs(board, word, index + 1, row - 1, col) || // Up
                        dfs(board, word, index + 1, row, col + 1) || // Right
                        dfs(board, word, index + 1, row, col - 1);   // Left

        // Restore the cell back to its original state
        board[row][col] = temp;

        return found;
    }
}

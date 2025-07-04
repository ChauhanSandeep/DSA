package Graph;

/**
 * Problem: Word Search (LeetCode #79)
 * https://leetcode.com/problems/word-search/
 *
 * Given an m x n 2D board and a word, determine if the word exists in the grid.
 * The word can be constructed from letters of sequentially adjacent cells (horizontally or vertically),
 * and the same cell may not be used more than once.
 *
 * Example:
 * Input:
 *   board = [
 *     ['A','B','C','E'],
 *     ['S','F','C','S'],
 *     ['A','D','E','E']
 *   ],
 *   word = "ABCCED"
 * Output: true
 *
 * Follow-up Questions:
 * - What if words can also be constructed diagonally?
 * - How would you handle large dictionaries? (Use Trie + DFS)
 * - What if multiple words need to be found? (Word Search II: https://leetcode.com/problems/word-search-ii/)
 */
public class WordSearch {

    /**
     * Public API to check if the given word exists in the board.
     *
     * Steps:
     * 1. Start DFS from every cell in the board.
     * 2. Backtrack when:
     *    - Character doesn't match.
     *    - Word is fully matched.
     *    - Bounds exceeded or cell reused.
     *
     * Time Complexity: O(M * N * 4^L), where L = length of word, and M, N = board dimensions.
     * Space Complexity: O(L) for recursion stack.
     */
    public static boolean doesWordExist(char[][] board, String word) {
        if (board == null || board.length == 0 || word == null || word.isEmpty()) {
            return false;
        }

        int rows = board.length;
        int cols = board[0].length;

        // Try to start DFS from every cell
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
     * DFS helper to recursively match characters in the word with valid board paths.
     *
     * @param board The character grid
     * @param word The word to search for
     * @param index Current index in the word
     * @param row Current row in the board
     * @param col Current column in the board
     * @return true if path matches the word, false otherwise
     */
    private static boolean dfs(char[][] board, String word, int index, int row, int col) {
        // Entire word matched
        if (index == word.length()) {
            return true;
        }

        // Out-of-bounds or mismatch or already visited
        if (row < 0 || col < 0 || row >= board.length || col >= board[0].length || board[row][col] != word.charAt(
            index)) {
            return false;
        }

        // Mark the current cell as visited
        char temp = board[row][col];
        board[row][col] = '#';

        // Explore all 4 directions
        boolean found = dfs(board, word, index + 1, row + 1, col) ||  // down
            dfs(board, word, index + 1, row - 1, col) ||  // up
            dfs(board, word, index + 1, row, col + 1) ||  // right
            dfs(board, word, index + 1, row, col - 1);    // left

        // Backtrack: restore the original character
        board[row][col] = temp;

        return found;
    }

    public static void main(String[] args) {
        char[][] board = {
            {'A', 'B', 'C', 'E'},
            {'S', 'F', 'C', 'S'},
            {'A', 'D', 'E', 'E'}};

        System.out.println(doesWordExist(board, "ABCCED")); // true
        System.out.println(doesWordExist(board, "SEE"));    // true
        System.out.println(doesWordExist(board, "ABCB"));   // false
    }
}
package graphs;

import java.util.Arrays;

/**
 * Problem: Word Search
 *
 * Given a 2D board of letters and a word, decide whether the word can be formed
 * by walking through horizontally or vertically adjacent cells. The same cell may
 * not be used more than once in one word path.
 *
 * Leetcode: https://leetcode.com/problems/word-search/
 * Rating:   acceptance 47.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Graph | Backtracking DFS | In-place visited marking
 *
 * Example:
 *   Input:  board = [[A,B,C,E],[S,F,C,S],[A,D,E,E]], word = "ABCB"
 *   Output: false
 *   Why:    the letters exist, but forming ABCB would require reusing the B cell,
 *           which the rules forbid.
 *
 * Follow-ups:
 *   1. Search for many words at once?
 *      Build a trie and run shared DFS from the board; see Word Search II.
 *   2. Allow diagonal movement?
 *      Add the four diagonal directions to the DFS transitions.
 *   3. Count all matching paths instead of existence?
 *      Do not stop at the first success; accumulate successful DFS leaves.
 *
 * Related: Word Search II (212), Number of Islands (200), Path With Maximum Gold (1219).
 */
public class WordSearch {

    public static void main(String[] args) {
        char[][] board = {{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}};
        String[] words = {"ABCCED", "ABCB"};
        boolean[] expected = {true, false};
        for (int i = 0; i < words.length; i++) {
            boolean output = doesWordExist(board, words[i]);
            System.out.printf("board=%s word=%s -> %b  expected=%b%n", Arrays.deepToString(board), words[i], output, expected[i]);
        }
    }

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

}

package Graph;

public class WordSearch {

    public static void main(String[] args) {
        char[][] board = {
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'}
        };
        System.out.println(exist(board, "ABCCED")); // true
        System.out.println(exist(board, "ABCB"));   // false
    }

    public static boolean exist(char[][] board, String word) {
        int rows = board.length, cols = board[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (dfs(board, word, 0, i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean dfs(char[][] board, String word, int index, int i, int j) {
        if (index == word.length()) return true; // Found the word

        if (i < 0 || j < 0 || i >= board.length || j >= board[0].length || board[i][j] != word.charAt(index)) {
            return false;
        }

        // Mark the cell as visited by modifying it
        char temp = board[i][j];
        board[i][j] = '#';

        boolean found = dfs(board, word, index + 1, i + 1, j) ||
                        dfs(board, word, index + 1, i - 1, j) ||
                        dfs(board, word, index + 1, i, j + 1) ||
                        dfs(board, word, index + 1, i, j - 1);

        // Restore the cell
        board[i][j] = temp;

        return found;
    }
}

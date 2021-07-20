package Graph;

public class WordSearch {
    public static void main(String[] args) {
        char[][] board = {
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'}};
        System.out.println(exist(board, "ABCCED"));
        System.out.println(exist(board, "ABCB"));
    }


    /**
     * Given a grid of character, find if word exists or not. Same character cannot be used twice.
     */
    public static boolean exist(char[][] board, String word) {
        if (word == null || word.length() == 0) return true;
        if (board == null || board.length == 0) return false;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == word.charAt(0)) {
                    boolean[][] used = new boolean[board.length][board[0].length];
                    boolean result = doDFS(board, word, 0, i, j, used);
                    if (result) return true;
                }
            }
        }
        return false;
    }

    public static boolean doDFS(char[][] board, String word, int index, int i, int j, boolean[][] used) {
        if (index == word.length()) {
            return true;
        }
        if (i < 0 || j < 0 || i >= board.length || j >= board[i].length || board[i][j] != word.charAt(index) || used[i][j])
            return false;

        used[i][j] = true;
        boolean wordExist = doDFS(board, word, index + 1, i + 1, j, used)
                || doDFS(board, word, index + 1, i - 1, j, used)
                || doDFS(board, word, index + 1, i, j + 1, used)
                || doDFS(board, word, index + 1, i, j - 1, used);
        used[i][j] = false;
        return wordExist;
    }
}

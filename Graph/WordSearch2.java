package Graph;

import java.util.*;

public class WordSearch2 {

    public static void main(String[] args) {
        char[][] board = {
                {'o','a','a','n'},
                {'e','t','a','e'},
                {'i','h','k','r'},
                {'i','f','l','v'}
        };
        String[] words = {"oath", "pea", "eat", "rain"};
        System.out.println(new WordSearch2().findWords(board, words));
    }

    List<String> result;
    int rows, cols;
    char[][] board;

    public List<String> findWords(char[][] board, String[] words) {
        result = new ArrayList<>();
        if (board == null || board.length == 0 || board[0].length == 0) return result;

        this.rows = board.length;
        this.cols = board[0].length;
        this.board = board;

        // Build the Trie
        TrieNode root = new TrieNode();
        for (String word : words) {
            addWord(root, word);
        }

        // Start DFS from every cell
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (root.children.containsKey(board[i][j])) {
                    backtrack(i, j, root);
                }
            }
        }

        return result;
    }

    private void backtrack(int row, int col, TrieNode parent) {
        char letter = board[row][col];
        TrieNode currNode = parent.children.get(letter);

        if (currNode.word != null) {
            result.add(currNode.word);
            currNode.word = null;  // Prevent duplicate words in result
        }

        // Mark cell as visited
        board[row][col] = '#';

        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, -1, 0, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = row + dx[i];
            int newCol = col + dy[i];

            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols &&
                currNode.children.containsKey(board[newRow][newCol])) {
                backtrack(newRow, newCol, currNode);
            }
        }

        // Restore cell
        board[row][col] = letter;

        // **Optimization**: Remove leaf nodes from Trie
        if (currNode.children.isEmpty()) {
            parent.children.remove(letter);
        }
    }

    private void addWord(TrieNode root, String word) {
        TrieNode temp = root;
        for (char c : word.toCharArray()) {
            temp.children.putIfAbsent(c, new TrieNode());
            temp = temp.children.get(c);
        }
        temp.word = word;
    }
}

class TrieNode {
    HashMap<Character, TrieNode> children = new HashMap<>();
    String word = null;
}

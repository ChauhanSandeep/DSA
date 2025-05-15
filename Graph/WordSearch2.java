package Graph;

import java.util.*;

/**
 * Word Search II (LeetCode #212) - https://leetcode.com/problems/word-search-ii/
 * 
 * Given an m x n board of characters and a list of words, return all words that 
 * can be found in the grid. A word must be formed by adjacent letters and the same
 * cell cannot be used more than once per word.
 *
 * Approach:
 * - **Trie (Prefix Tree)** is built to efficiently store words.
 * - **Backtracking DFS** is used to traverse the board and match words.
 * - **Pruning Optimization**: Remove Trie nodes when a word is found to improve efficiency.
 *
 * Time Complexity: O(M * N * 4^L) - Worst case for DFS traversal (L = word length).
 * Space Complexity: O(W * L) - Trie storage for words (W = words count, L = max word length).
 */
public class WordSearch2 {

    private List<String> foundWords;
    private int rows, cols;
    private char[][] board;

    public static void main(String[] args) {
        char[][] board = {
                {'o', 'a', 'a', 'n'},
                {'e', 't', 'a', 'e'},
                {'i', 'h', 'k', 'r'},
                {'i', 'f', 'l', 'v'}
        };
        String[] words = {"oath", "pea", "eat", "rain"};
        System.out.println(new WordSearch2().findWords(board, words));
    }

    /**
     * Finds all words in the board that exist in the given word list.
     *
     * @param board 2D character grid
     * @param words List of words to search for
     * @return List of found words
     */
    public List<String> findWords(char[][] board, String[] words) {
        foundWords = new ArrayList<>();
        if (board == null || board.length == 0 || board[0].length == 0) return foundWords;

        this.rows = board.length;
        this.cols = board[0].length;
        this.board = board;

        // Build the Trie for word storage
        TrieNode root = buildTrie(words);

        // Start DFS from every cell
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (root.children.containsKey(board[row][col])) {
                    backtrack(row, col, root);
                }
            }
        }

        return foundWords;
    }

    /**
     * Backtracking DFS to search words in the board.
     *
     * @param row    Current row position
     * @param col    Current column position
     * @param parent Current TrieNode reference
     */
    private void backtrack(int row, int col, TrieNode parent) {
        char letter = board[row][col];
        TrieNode currentNode = parent.children.get(letter);

        // Word found, add to result and prevent duplicate entries
        if (currentNode.word != null) {
            foundWords.add(currentNode.word);
            currentNode.word = null;
        }

        // Mark the cell as visited
        board[row][col] = '#';

        // Explore in all 4 directions (Up, Left, Down, Right)
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, -1, 0, 1};

        for (int i = 0; i < 4; i++) {
            int newRow = row + dx[i];
            int newCol = col + dy[i];

            if (isValidCell(newRow, newCol) && currentNode.children.containsKey(board[newRow][newCol])) {
                backtrack(newRow, newCol, currentNode);
            }
        }

        // Restore cell for backtracking
        board[row][col] = letter;

        // **Optimization**: Remove leaf Trie nodes to save memory
        if (currentNode.children.isEmpty()) {
            parent.children.remove(letter);
        }
    }

    /**
     * Checks if the given cell is within bounds.
     *
     * @param row Row index
     * @param col Column index
     * @return True if the cell is valid, otherwise false
     */
    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    /**
     * Builds a Trie (Prefix Tree) from the given word list.
     *
     * @param words List of words to add to the Trie
     * @return Root TrieNode
     */
    private TrieNode buildTrie(String[] words) {
        TrieNode root = new TrieNode();
        for (String word : words) {
            TrieNode current = root;
            for (char c : word.toCharArray()) {
                current.children.putIfAbsent(c, new TrieNode());
                current = current.children.get(c);
            }
            current.word = word;
        }
        return root;
    }
}

/**
 * TrieNode class representing each node in the Trie.
 */
class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    String word = null;
}

package graphs;

import java.util.*;

/**
 * Problem: Word Search II
 * https://leetcode.com/problems/word-search-ii/
 *
 * Given a 2D board and a list of words from the dictionary, find all words that
 * can be formed by sequentially adjacent letters (horizontally or vertically). Each cell
 * may be used only once per word.
 *
 * Example:
 * Input:
 *   board = [
 *     ['o','a','a','n'],
 *     ['e','t','a','e'],
 *     ['i','h','k','r'],
 *     ['i','f','l','v']
 *   ]
 *   words = ["oath","pea","eat","rain"]
 * Output:
 *   ["oath","eat"]
 *
 * Follow-ups:
 * - How would you handle a dynamic dictionary (add/remove at runtime)?
 * - Can this be parallelized using multiple threads on rows/regions?
 */
public class WordSearch2 {

    private List<String> matchedWords;
    private char[][] board;
    private int rows, cols;

    public static void main(String[] args) {
        char[][] board = {
            {'o', 'a', 'a', 'n'},
            {'e', 't', 'a', 'e'},
            {'i', 'h', 'k', 'r'},
            {'i', 'f', 'l', 'v'}
        };

        String[] words = {"oath", "pea", "eat", "rain"};
        List<String> result = new WordSearch2().findWords(board, words);
        System.out.println("Words found: " + result); // ["oath", "eat"]
    }

    /**
     * Main API to find all valid words from the board using DFS + Trie.
     *
     * Approach:
     * 1. Build a Trie (Prefix Tree) to hold all input words.
     * 2. Run DFS with backtracking from each cell that matches a prefix.
     * 3. When a word is matched, record it and **prune the Trie** to save memory and time.
     *
     * Time Complexity: O(M * N * 4^L), where M*N is the board size and L is max word length.
     * Space Complexity: O(W * L) for Trie (W = word count, L = average length of each word).
     */
    public List<String> findWords(char[][] board, String[] words) {
        matchedWords = new ArrayList<>();
        if (board == null || board.length == 0 || board[0].length == 0 || words == null) return matchedWords;

        this.board = board;
        this.rows = board.length;
        this.cols = board[0].length;

        TrieNode trieRoot = buildTrie(words);

        // Start backtracking DFS from every cell
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (trieRoot.children.containsKey(board[row][col])) {
                    // Start DFS if the first character matches
                    backtrackDFS(row, col, trieRoot);
                }
            }
        }

        return matchedWords;
    }

    /**
     * Recursive backtracking DFS to explore words starting from (row, col).
     *
     * @param row    Current row position
     * @param col    Current column position
     * @param parent TrieNode of previous character
     */
    private void backtrackDFS(int row, int col, TrieNode parent) {
        char currentChar = board[row][col];
        TrieNode currentNode = parent.children.get(currentChar);

        // Word match found at this node
        if (currentNode.word != null) {
            matchedWords.add(currentNode.word);
            currentNode.word = null;  // Avoid duplicates
        }

        // Mark current cell as visited
        board[row][col] = '#';

        // Directions: up, left, down, right
        int[] dRow = {-1, 0, 1, 0};
        int[] dCol = {0, -1, 0, 1};

        for (int dir = 0; dir < 4; dir++) {
            int nextRow = row + dRow[dir];
            int nextCol = col + dCol[dir];

            if (isValidCell(nextRow, nextCol)) {
                char nextChar = board[nextRow][nextCol];
                if (currentNode.children.containsKey(nextChar)) {
                    backtrackDFS(nextRow, nextCol, currentNode);
                }
            }
        }

        // Backtrack: restore original character
        board[row][col] = currentChar;

        // Prune Trie node if it's now a leaf (no further children)
        if (currentNode.children.isEmpty()) {
            parent.children.remove(currentChar);
        }
    }

    /**
     * Helper to check if a given cell is within grid bounds.
     *
     * @param row Row index
     * @param col Column index
     * @return true if (row, col) is valid, false otherwise
     */
    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    /**
     * Builds a Trie from a list of input words.
     *
     * @param words List of words
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
            current.word = word;  // Store full word at terminal node
        }
        return root;
    }

    /**
     * TrieNode structure with children map and optional full word.
     */
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        String word = null;  // Non-null only at word-end
    }
}
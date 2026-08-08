package trie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ✅ Problem: Word Search II
 *
 * Given an {@code m x n} board of lowercase letters and a dictionary of words,
 * return every word that can be formed by walking through horizontally or
 * vertically adjacent cells. A single cell may be used at most once per word.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/word-search-ii/   (Hard)
 * 🏷️ Pattern:  Trie · Board DFS backtracking · Prefix pruning
 *
 * 🧪 Example:
 *   Input:  board = [[o,a,a,n],[e,t,a,e],[i,h,k,r],[i,f,l,v]], words = [oath,pea,eat,rain]
 *   Output: [eat, oath]
 *   Why:    "oath" and "eat" have adjacent, non-reusing paths; "pea" has no
 *           starting cell and "rain" cannot be connected on the board.
 *
 * 🚧 Edge cases to remember:
 *   - null / empty board or words → empty list
 *   - duplicate paths to one word  → clear the terminal word after reporting it
 *   - repeated words in dictionary → still reported once (word cleared on first hit)
 *
 * 🔍 Follow-ups:
 *   1. Huge dictionary? The trie already stops a path the moment its prefix
 *      leaves the trie, so search cost scales with the board, not the word count.
 *   2. Dictionary changes between searches? Keep a mutable trie and update only
 *      the inserted / removed terminal nodes.
 *   3. Diagonal or 3D movement? Swap the fixed 4-neighbor offsets for the
 *      allowed neighbor set.
 *
 * 🔁 Related: Word Search (79), Implement Trie (208), Add and Search Words (211).
 */
public class WordSearchII {

    private static final int ALPHABET_SIZE = 26;
    private static final char VISITED = '#';

    /**
     * 🧠 Intuition: searching the board once per word repeats the same prefix
     * walks. A trie collapses that work — one board path represents every word
     * sharing its prefix, so the instant the next board character has no trie
     * edge, no dictionary word can complete along that path and we bail out.
     * A terminal node stores the full word; clearing it after reporting avoids
     * duplicates, and dropping now-empty branches keeps later paths short.
     *
     * Algorithm:
     *   1. Build a trie from every dictionary word.
     *   2. Launch DFS from each board cell against the trie root.
     *   3. Stop a path when it leaves the board, revisits a cell, or misses an edge.
     *   4. Record + clear terminal words, backtrack the cell, then prune dead branches.
     *
     * Time:  O(W*L + M*N*4^L) — trie build over W words of length L, then each of
     *        the M*N cells can branch four ways up to depth L.
     * Space: O(W*L) for the trie plus O(L) recursion depth.
     *
     * @param board letter grid searched in four directions
     * @param words dictionary words to locate on the board
     * @return the dictionary words that can be formed on the board
     */
    public List<String> findWords(char[][] board, String[] words) {
        List<String> matchedWords = new ArrayList<>();
        if (board == null || board.length == 0 || board[0].length == 0
                || words == null || words.length == 0) {
            return matchedWords;
        }

        TrieNode root = buildTrie(words);

        int rows = board.length, cols = board[0].length;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                backtrack(board, row, col, root, matchedWords);
            }
        }
        return matchedWords;
    }

    /**
     * 🧠 Intuition: {@code parent} is the trie node reached just before this
     * cell, so the current character must be one of its child edges. If that
     * edge is missing the path is not a valid prefix and we return immediately.
     *
     * Algorithm:
     *   1. Reject out-of-bounds, visited ('#'), or non-matching cells.
     *   2. Descend to the child node; report and clear any word that ends here.
     *   3. Mark the cell visited, recurse into all four neighbours, then restore it.
     *   4. Prune the child edge once it holds no word and no children.
     *
     * Time:  O(4^L) in the worst case along one path.
     * Space: O(L) recursion depth.
     *
     * @param board        letter grid, temporarily mutated to mark visited cells
     * @param row          current row
     * @param col          current column
     * @param parent       trie node reached before consuming this cell
     * @param matchedWords accumulator for words found so far
     */
    private void backtrack(char[][] board, int row, int col, TrieNode parent, List<String> matchedWords) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return;
        }
        char currentChar = board[row][col];
        if (currentChar == VISITED) {
            return;
        }
        TrieNode node = parent.children[currentChar - 'a'];
        if (node == null) {
            return; // no dictionary word continues along this path
        }

        if (node.word != null) {
            matchedWords.add(node.word);
            node.word = null; // ✅ clear terminal so the same word is not reported twice
        }

        board[row][col] = VISITED;
        backtrack(board, row - 1, col, node, matchedWords);
        backtrack(board, row + 1, col, node, matchedWords);
        backtrack(board, row, col - 1, node, matchedWords);
        backtrack(board, row, col + 1, node, matchedWords);
        board[row][col] = currentChar;

        // ✅ prune the dead edge so future paths skip an exhausted branch
        if (node.word == null && node.isEmpty()) {
            parent.children[currentChar - 'a'] = null;
        }
    }

    /** Builds a trie whose terminal nodes store the complete dictionary word. */
    private TrieNode buildTrie(String[] words) {
        TrieNode root = new TrieNode();
        for (String word : words) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
                node = node.children[index];
            }
            node.word = word;
        }
        return root;
    }

    private static class TrieNode {
        TrieNode[] children = new TrieNode[ALPHABET_SIZE];
        String word = null; // non-null only at a word terminal

        boolean isEmpty() {
            for (TrieNode child : children) {
                if (child != null) {
                    return false;
                }
            }
            return true;
        }
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        WordSearchII solver = new WordSearchII();

        char[][][] boards = {
            {{'o', 'a', 'a', 'n'}, {'e', 't', 'a', 'e'}, {'i', 'h', 'k', 'r'}, {'i', 'f', 'l', 'v'}},
            {{'a', 'b'}, {'c', 'd'}}
        };
        String[][] words = {
            {"oath", "pea", "eat", "rain"},
            {"abcb"}
        };
        List<List<String>> expected = Arrays.asList(
            Arrays.asList("eat", "oath"),
            Collections.emptyList()
        );

        for (int i = 0; i < boards.length; i++) {
            List<String> output = solver.findWords(boards[i], words[i]);
            Collections.sort(output);
            System.out.printf("words=%s  →  %s  expected=%s%n",
                Arrays.toString(words[i]), output, expected.get(i));
        }
    }
}

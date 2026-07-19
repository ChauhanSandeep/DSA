package trie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Problem: Search Suggestions System
 *
 * Given product names and a search word, return up to three lexicographically
 * smallest products after each typed character. Every suggestion for a step must
 * start with the prefix typed so far.
 *
 * Leetcode: https://leetcode.com/problems/search-suggestions-system/ (Medium)
 * Rating:   1573 (zerotrac Elo)
 * Pattern:  Trie | Autocomplete | Lexicographic DFS with result cap
 *
 * Example:
 *   Input:  products = ["mobile", "mouse", "moneypot", "monitor", "mousepad"], searchWord = "mouse"
 *   Output: [["mobile", "moneypot", "monitor"], ["mobile", "moneypot", "monitor"], ["mouse", "mousepad"], ["mouse", "mousepad"], ["mouse", "mousepad"]]
 *   Why:    prefixes "m" and "mo" still match the three smallest mo-products, but "mou"
 *           narrows the trie path to only "mouse" and "mousepad".
 *
 * Follow-ups:
 *   1. Serve millions of products with very low query latency?
 *      Store the top three suggestions directly on each trie node during insertion.
 *   2. Support case-insensitive search?
 *      Normalize products and queries, while storing the original display string at terminal nodes.
 *   3. Support products being added and removed continuously?
 *      Keep per-node ordered sets or heaps and update them along each product's prefix path.
 *   4. Rank suggestions by popularity before lexicographic order?
 *      Store top-k candidates per prefix using a comparator over score, then name.
 *
 * Related: Implement Trie (Prefix Tree) (208), Design Search Autocomplete System (642).
 */
public class SuggestedProducts {

  public static void main(String[] args) {
    SuggestedProducts solver = new SuggestedProducts();

    String[][] productCases = {
        {"mobile", "mouse", "moneypot", "monitor", "mousepad"},
        {"havana"}
    };
    String[] searchWords = {"mouse", "tatiana"};
    String[] expected = {
        "[[mobile, moneypot, monitor], [mobile, moneypot, monitor], [mouse, mousepad], [mouse, mousepad], [mouse, mousepad]]",
        "[[], [], [], [], [], [], []]"
    };

    for (int i = 0; i < productCases.length; i++) {
      List<List<String>> got = solver.getSuggestedProducts(productCases[i].clone(), searchWords[i]);
      System.out.printf("products=%s searchWord=%s -> %s  expected=%s%n",
          Arrays.toString(productCases[i]), searchWords[i], got, expected[i]);
    }
  }

  /**
   * Intuition: each typed prefix corresponds to one trie node. If that path is
   * missing, no product can start with the prefix; if it exists, every terminal
   * word below that node is a valid suggestion. Sorting before insertion and DFS
   * through child slots from 'a' to 'z' makes the first three results the smallest.
   *
   * Algorithm:
   *   1. Sort products so trie DFS observes lexicographic product order.
   *   2. Insert every product into the trie.
   *   3. Grow the typed prefix one character at a time.
   *   4. For each prefix, collect up to three words from the matching subtree.
   *
   * Time:  O(P log P * L + P*L + S*L) - sorting compares products, insertion reads characters, and each prefix search is capped at three outputs.
   * Space: O(P*L) - the trie stores product characters, sharing common prefixes.
   *
   * @param products product names made of lowercase English letters
   * @param searchWord user query typed from left to right
   * @return suggestions after each typed character
   */
  public List<List<String>> getSuggestedProducts(String[] products, String searchWord) {
    Trie trie = new Trie();
    List<List<String>> suggestionsByPrefix = new ArrayList<>();

    Arrays.sort(products); // Ensure lexicographical order
    for (String product : products) {
      trie.insert(product);
    }

    StringBuilder prefixBuilder = new StringBuilder();
    for (char c : searchWord.toCharArray()) {
      prefixBuilder.append(c);
      suggestionsByPrefix.add(trie.getWordsStartingWith(prefixBuilder.toString()));
    }

    return suggestionsByPrefix;
  }

  /**
   * Trie data structure for prefix search.
   */
  static class Trie {

    /** Node representing a single character in the Trie */
    private static class Node {
      boolean isEndOfWord = false;
      Node[] children = new Node[26];
    }

    private final Node root;

    public Trie() {
      this.root = new Node();
    }

    /**
     * Inserts one product into the trie and marks its terminal node.
     *
     * @param word product name to insert
     */
    public void insert(String word) {
      Node current = root;
      for (char c : word.toCharArray()) {
        int index = c - 'a';
        if (current.children[index] == null) {
          current.children[index] = new Node();
        }
        current = current.children[index];
      }
      current.isEndOfWord = true;
    }

    /**
     * Returns up to three lexicographically smallest words starting with prefix.
     *
     * @param prefix prefix to complete
     * @return list of matching words, capped at three
     */
    public List<String> getWordsStartingWith(String prefix) {
      Node current = root;
      List<String> results = new ArrayList<>();

      // Traverse down the trie to the end of the prefix
      for (char c : prefix.toCharArray()) {
        int index = c - 'a';
        if (current.children[index] == null) {
          return results; // Prefix not found
        }
        current = current.children[index];
      }

      dfs(current, new StringBuilder(prefix), results);
      return results;
    }

    /** Collects at most three words below a prefix node in lexicographic order. */
    private void dfs(Node node, StringBuilder wordSoFar, List<String> result) {
        if (result.size() == 3) {
            return;
        }

      if (node.isEndOfWord) {
        result.add(wordSoFar.toString());
      }

      for (char c = 'a'; c <= 'z'; c++) {
        int index = c - 'a';
        if (node.children[index] != null) {
          wordSoFar.append(c);
          dfs(node.children[index], wordSoFar, result);
          wordSoFar.deleteCharAt(wordSoFar.length() - 1); // backtrack
        }
      }
    }
  }
}
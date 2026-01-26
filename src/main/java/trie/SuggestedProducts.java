package trie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Problem: Search Suggestions System
 * Leetcode: https://leetcode.com/problems/search-suggestions-system/
 *
 * Given a list of product strings and a search word, suggest at most 3 product names
 * after each character of the search word is typed. The suggested products must start
 * with the typed prefix and be in lexicographically increasing order.
 *
 * Example:
 * Input:
 *    products = ["mobile","mouse","moneypot","monitor","mousepad"]
 *    searchWord = "mouse"
 * Output:
 *    [
 *      ["mobile","moneypot","monitor"],
 *      ["mobile","moneypot","monitor"],
 *      ["mouse","mousepad"],
 *      ["mouse","mousepad"],
 *      ["mouse","mousepad"]
 *    ]
 *
 * Follow-up:
 * - How would you scale to millions of products?
 *  → Use Trie with indexing.
 * - What if prefix search must be case-insensitive?
 *  → Normalize input to lowercase.
 * LeetCode Contest Rating: 1573
 */
public class SuggestedProducts {

  public static void main(String[] args) {
    String[] products = {"mobile", "mouse", "moneypot", "monitor", "mousepad"};
    String searchWord = "mouse";

    List<List<String>> suggestions = new SuggestedProducts().getSuggestedProducts(products, searchWord);
    System.out.println(suggestions);
  }

  /**
   * Builds a Trie and returns up to 3 suggestions for each prefix of searchWord.
   *
   * @param products Array of product names
   * @param searchWord The search string typed by the user
   * @return Suggestions for each prefix of searchWord
   *
   * 🔹 Time Complexity:
   *     - Insert: O(N * L) for N words of length L
   *     - DFS per prefix: O(3 * 26 * L) worst case (bounded by 3 results)
   * 🔹 Space Complexity: O(N * L) for Trie
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
     * Inserts a word into the Trie.
     * @param word The word to be inserted
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
     * Returns up to 3 lexicographically smallest words starting with the given prefix.
     *
     * @param prefix Prefix to search
     * @return List of up to 3 matching words
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

    /**
     * Performs DFS to find up to 3 words from the given node.
     *
     * @param node Current node in trie
     * @param wordSoFar Prefix built so far
     * @param result Accumulator for result list (max size = 3)
     */
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
package arrays;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Implement Trie (Prefix Tree)
 *
 * A Trie (prefix tree) is a tree data structure used to efficiently store and retrieve keys
 * in a dataset of strings. This implementation supports insert, search, and prefix matching operations.
 *
 * 🔗 LeetCode: https://leetcode.com/problems/implement-trie-prefix-tree/
 *
 * 📝 Example:
 * Input:
 *   trie.insert("apple");
 *   trie.search("apple");   → true
 *   trie.search("app");     → false
 *   trie.startsWith("app"); → true
 *   trie.insert("app");
 *   trie.search("app");     → true
 *
 * 🎯 Constraints:
 * - 1 <= word.length, prefix.length <= 2000
 * - word and prefix consist only of lowercase English letters
 * - At most 3 * 10^4 calls in total to insert, search, and startsWith
 *
 * 💡 Follow-up Questions with Answers:
 * 1. Q: How would you optimize memory usage for a Trie with many words sharing common prefixes?
 *    A: Use an array of size 26 instead of HashMap for children (for lowercase letters only),
 *       or implement compressed trie (radix tree/Patricia trie) that merges single-child paths.
 *
 * 2. Q: How would you implement delete operation in Trie?
 *    A: Recursively traverse to the word's end, mark isWord=false, then backtrack and remove
 *       nodes that have no children and are not end of another word.
 *
 * 3. Q: How would you count total number of words with a given prefix?
 *    A: Add a count field to TrieNode. Increment count at each node during insert. When searching
 *       for prefix, return count at the prefix's last node.
 *
 * 4. Q: How would you implement autocomplete/word suggestions?
 *    A: After finding prefix node, perform DFS/BFS from that node collecting all words
 *       (nodes with isWord=true), building full word paths during traversal.
 *
 * 5. Q: What's the advantage of Trie over HashMap for word storage?
 *    A: Trie enables prefix-based operations (autocomplete, word suggestions) in O(k) where k is
 *       prefix length. HashMap requires O(n) to find all words with given prefix. Trie also saves
 *       space when many words share common prefixes.
 */
public class TrieTest {
  public static void main(String[] args) {
    Trie trie = new Trie();
    trie.insert("apple");
    System.out.println(trie.search("apple"));  // true
    System.out.println(trie.search("app"));    // false
    System.out.println(trie.startsWith("app"));// true
    trie.insert("app");
    System.out.println(trie.search("app"));    // true
    System.out.println(trie.startsWith("app"));// true
  }
}

class Trie {
  private TrieNode root;

  public Trie() {
    root = new TrieNode();
  }

  /**
   * Inserts a word into the Trie.
   *
   * Algorithm:
   * 1. Start from root node
   * 2. For each character, create new node if it doesn't exist
   * 3. Move to next node and continue
   * 4. Mark the last node as end of word
   *
   * Time Complexity: O(m) where m is word length
   * Space Complexity: O(m) in worst case when word shares no prefix with existing words
   *
   * @param word The word to insert into the Trie
   */
  public void insert(String word) {
    TrieNode currentNode = root;
    for (char character : word.toCharArray()) {
      currentNode.children.putIfAbsent(character, new TrieNode());
      currentNode = currentNode.children.get(character);
    }
    currentNode.isWord = true;
  }

  /**
   * Searches for an exact word match in the Trie.
   *
   * Algorithm:
   * 1. Traverse Trie following word's characters
   * 2. If any character path missing, word doesn't exist
   * 3. If reach end, check if node is marked as word ending
   *
   * Time Complexity: O(m) where m is word length
   * Space Complexity: O(1) - no extra space used
   *
   * @param word The word to search for
   * @return true if word exists in Trie, false otherwise
   */
  public boolean search(String word) {
    TrieNode node = findNode(word);
    return node != null && node.isWord;
  }

  /**
   * Checks if any word in the Trie starts with the given prefix.
   *
   * Algorithm:
   * 1. Traverse Trie following prefix characters
   * 2. If all prefix characters found, prefix exists regardless of isWord flag
   *
   * Time Complexity: O(m) where m is prefix length
   * Space Complexity: O(1) - no extra space used
   *
   * @param prefix The prefix to check
   * @return true if any word starts with prefix, false otherwise
   */
  public boolean startsWith(String prefix) {
    return findNode(prefix) != null;
  }

  /**
   * Helper method to traverse Trie and find node corresponding to given string.
   * Returns null if any character in path is missing.
   */
  private TrieNode findNode(String str) {
    TrieNode currentNode = root;
    for (char character : str.toCharArray()) {
      if (!currentNode.children.containsKey(character)) {
        return null;
      }
      currentNode = currentNode.children.get(character);
    }
    return currentNode;
  }
}

/**
 * TrieNode class representing each node in the Trie.
 * Contains a map of children nodes and a boolean to indicate if it's the end of a word.
 */
class TrieNode {
  Map<Character, TrieNode> children = new HashMap<>();
  boolean isWord;
}

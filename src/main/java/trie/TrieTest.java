package trie;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Implement Trie (Prefix Tree)
 *
 * Build a prefix tree that stores lowercase words and supports three operations:
 * insert a word, search for an exact word, and check whether any stored word
 * starts with a given prefix.
 *
 * Leetcode: https://leetcode.com/problems/implement-trie-prefix-tree/ (Medium)
 * Rating:   acceptance 69.8% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Trie | Prefix tree operations | Shared-prefix traversal
 *
 * Example:
 *   Input:  insert("apple"), search("app"), startsWith("app"), insert("app"), search("app")
 *   Output: null, false, true, null, true
 *   Why:    "app" is only a prefix after inserting "apple"; it becomes a full word only
 *           after its own terminal node is marked as an end-of-word node.
 *
 * Follow-ups:
 *   1. Support delete(word) without breaking words that share the same prefix?
 *      Unmark the terminal node, then prune unused nodes while backtracking.
 *   2. Count how many stored words start with a prefix?
 *      Keep a prefixCount on each node and increment it during insert.
 *   3. Return all words with a prefix in lexicographic order?
 *      Traverse to the prefix node, then DFS through children in sorted key order.
 *   4. Reduce memory when the alphabet is large or sparse?
 *      Use HashMap children, as this file does, or compress single-child paths in a radix tree.
 *
 * Related: Design Add and Search Words Data Structure (211), Search Suggestions System (1268).
 */
public class TrieTest {
  public static void main(String[] args) {
    Trie trie = new Trie();

    trie.insert("apple");
    System.out.printf("insert(\"apple\") -> %s  expected=null%n", "null");
    System.out.printf("search(\"apple\") -> %s  expected=true%n", trie.search("apple"));
    System.out.printf("search(\"app\") -> %s  expected=false%n", trie.search("app"));
    System.out.printf("startsWith(\"app\") -> %s  expected=true%n", trie.startsWith("app"));

    trie.insert("app");
    System.out.printf("insert(\"app\") -> %s  expected=null%n", "null");
    System.out.printf("search(\"app\") -> %s  expected=true%n", trie.search("app"));
    System.out.printf("startsWith(\"ban\") -> %s  expected=false%n", trie.startsWith("ban"));
  }
}

class Trie {
  private TrieNode root;

  public Trie() {
    root = new TrieNode();
  }

  /**
   * Inserts a lowercase word by creating any missing child links along its path.
   *
   * Time:  O(L) - one node is visited per character.
   * Space: O(L) - a word with no shared prefix can add one node per character.
   *
   * @param word word to insert into the trie
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
   * Searches for an exact word, not just a prefix path.
   *
   * Time:  O(L) - one child lookup per character.
   * Space: O(1) - traversal keeps only the current node reference.
   *
   * @param word word to search for
   * @return true if the word was inserted before
   */
  public boolean search(String word) {
    TrieNode node = findNode(word);
    return node != null && node.isWord;
  }

  /**
   * Checks whether any inserted word starts with the given prefix.
   *
   * Time:  O(L) - one child lookup per prefix character.
   * Space: O(1) - traversal keeps only the current node reference.
   *
   * @param prefix prefix to search for
   * @return true if the prefix path exists in the trie
   */
  public boolean startsWith(String prefix) {
    return findNode(prefix) != null;
  }

  /** Returns the node reached by a string path, or null when any edge is missing. */
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

/** Trie node holding outgoing character edges and whether this prefix is a complete word. */
class TrieNode {
  Map<Character, TrieNode> children = new HashMap<>();
  boolean isWord;
}

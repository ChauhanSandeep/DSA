package dynamicprogramming.MiscellaneousDP;

import java.util.*;


/**
 * ConcatenatedWords.java
 *
 * Problem Statement:
 * Given an array of strings words (without duplicates), return all the concatenated words in the given list of words.
 * A concatenated word is defined as a string that is comprised entirely of at least two shorter words
 * (not necessarily distinct) in the given array.
 *
 * Example 1:
 * Input: words = ["cat","cats","catsdogcats","dog","dogcatsdog","hippopotamuses","rat","ratcatdogcat"]
 * Output: ["catsdogcats","dogcatsdog","ratcatdogcat"]
 * Explanation:
 * "catsdogcats" can be concatenated by "cats", "dog" and "cats"
 * "dogcatsdog" can be concatenated by "dog", "cats" and "dog"
 * "ratcatdogcat" can be concatenated by "rat", "cat", "dog" and "cat"
 *
 * Example 2:
 * Input: words = ["cat","dog","catdog"]
 * Output: ["catdog"]
 * Explanation:
 * "catdog" can be concatenated by "cat" and "dog"
 *
 * LeetCode link: https://leetcode.com/problems/concatenated-words/
 *
 * Follow-up Questions FAANG Interviews Might Ask:
 *  - How would you modify the solution if words could be used multiple times to form a concatenated word?
 *    → The current solution already handles this case since we check all possible splits and words can repeat.
 *  - What if you need to return the actual composition of concatenated words, not just identify them?
 *    → Modify the DP approach to store the split points or component words in a separate structure during the recursion.
 *  - Can you optimize for the case where most words are very short or very long?
 *    → Sort words by length first to process shorter words before longer ones, allowing early termination.
 *  - How would you handle the problem if words could contain uppercase letters or special characters?
 *    → Modify the Trie structure to handle a larger character set or use a HashMap-based Trie instead of array-based.
 *
 * Relevant Follow-up Problems:
 *  - LeetCode 139 (Word Break): https://leetcode.com/problems/word-break/
 *  - LeetCode 140 (Word Break II): https://leetcode.com/problems/word-break-ii/
 *  - LeetCode 820 (Short Encoding of Words): https://leetcode.com/problems/short-encoding-of-words/
 */
public class ConcatenatedWords {

  /**
   * Main method: Finds all concatenated words using Dynamic Programming with HashSet.
   * Step-by-step:
   *  1. Sort words by length to process shorter words first (optimization).
   *  2. Build a HashSet of all words for O(1) lookup.
   *  3. For each word, check if it can be formed by concatenating at least 2 other words.
   *  4. Use dynamic programming (word break pattern) to check if word can be split.
   *  5. Add valid concatenated words to result list.
   *  6. After checking each word, add it to the set for use in forming longer words.
   *
   * Algorithm: Dynamic Programming with HashSet (Word Break pattern).
   * Time Complexity: O(n * L^2), where n is the number of words and L is the maximum word length.
   *                  For each word, we check all possible splits (O(L^2) using DP).
   * Space Complexity: O(n * L), for storing all words in the HashSet and recursion stack.
   */
  public List<String> findAllConcatenatedWordsInADict(String[] words) {
    List<String> result = new ArrayList<>();
    Set<String> wordSet = new HashSet<>();

    // Sort by length to process shorter words first
    Arrays.sort(words, (word1, word2) -> word1.length() - word2.length());

    for (String word : words) {
      if (canFormByConcat(word, wordSet)) {
        result.add(word);
      }
      wordSet.add(word);
    }

    return result;
  }

  // Helper: Checks if a word can be formed by concatenating words from the set (at least 2 words).
  private boolean canFormByConcat(String word, Set<String> wordSet) {
    if (wordSet.isEmpty()) {
      return false;
    }

    int length = word.length();
    boolean[] dp = new boolean[length + 1]; // dp[i] means substring [0, i) can be formed
    dp[0] = true; // Empty string can always be formed

    for (int endIndex = 1; endIndex <= length; endIndex++) {
      for (int startIndex = 0; startIndex < endIndex; startIndex++) {
        // Check if we can split at startIndex
        // dp[startIndex] means substring [0, startIndex) can be formed
        // We need to check if substring [startIndex, endIndex) exists in wordSet
        if (dp[startIndex]) {
          String substring = word.substring(startIndex, endIndex);
          if (wordSet.contains(substring)) {
            dp[endIndex] = true;
            break;
          }
        }
      }
    }

    // Word must be formed by at least 2 words, so it cannot equal itself
    return dp[length];
  }

  /**
   * Alternative method: Using DFS with Memoization.
   * Step-by-step:
   *  1. Build a HashSet of all words.
   *  2. For each word, remove it from the set temporarily (to avoid using itself).
   *  3. Use DFS with memoization to check if word can be formed from other words.
   *  4. At each position, try all possible prefixes and recursively check the rest.
   *  5. Memoize results to avoid redundant computation.
   *  6. Add word back to set after checking.
   *
   * Algorithm: DFS with Memoization (Word Break pattern).
   * Time Complexity: O(n * L^2), where n is number of words and L is maximum word length.
   * Space Complexity: O(n * L) for HashSet and memoization cache, plus O(L) recursion stack.
   */
  public List<String> findAllConcatenatedWordsInADictDFS(String[] words) {
    List<String> result = new ArrayList<>();
    Set<String> wordSet = new HashSet<>(Arrays.asList(words));

    for (String word : words) {
      if (word.isEmpty()) {
        continue;
      }

      // Remove current word to avoid using itself
      wordSet.remove(word);

      if (canFormByDFS(word, wordSet, 0, new Boolean[word.length()])) {
        result.add(word);
      }

      // Add word back for forming other words
      wordSet.add(word);
    }

    return result;
  }

  // Helper: DFS with memoization to check if word starting from startIndex can be formed.
  private boolean canFormByDFS(String word, Set<String> wordSet, int startIndex, Boolean[] memo) {
    if (startIndex == word.length()) {
      return true;
    }

    if (memo[startIndex] != null) {
      return memo[startIndex];
    }

    // Try all possible end positions from current start
    for (int endIndex = startIndex + 1; endIndex <= word.length(); endIndex++) {
      String prefix = word.substring(startIndex, endIndex);

      if (wordSet.contains(prefix) && canFormByDFS(word, wordSet, endIndex, memo)) {
        memo[startIndex] = true;
        return true;
      }
    }

    memo[startIndex] = false;
    return false;
  }
}

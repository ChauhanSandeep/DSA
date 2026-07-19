package dynamicprogramming.MiscellaneousDP;

import java.util.*;


/**
 * Problem: Concatenated Words
 *
 * Given a list of unique words, return all words that can be built by joining at
 * least two shorter words from the same list. A shorter word may be reused.
 *
 * Leetcode: https://leetcode.com/problems/concatenated-words/
 * Rating:   acceptance 49.9% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic programming | Word break | Hash set lookup
 *
 * Example:
 *   Input:  words = ["cat","cats","dog","catsdog"]
 *   Output: ["catsdog"]
 *   Why:    "catsdog" is exactly "cats" + "dog", and both pieces are shorter words in the list.
 *
 * Follow-ups:
 *   1. Return the component words for each concatenated word?
 *      Store split parents in the word-break DP and reconstruct the path.
 *   2. What if the dictionary changes often?
 *      Use a trie or incremental index so insertions do not require rebuilding all lookup state.
 *   3. What if many words share long prefixes?
 *      A trie can avoid allocating every substring during the DP check.
 *
 * Related: Word Break (139), Word Break II (140), Short Encoding of Words (820).
 */
public class ConcatenatedWords {

  /**
     * Intuition: for one word, this is the Word Break problem: dp[end] means the
     * prefix word[0..end) can be formed from dictionary words. Sorting words by
     * length lets us build the dictionary gradually, so when a word is checked, the
     * set contains only shorter words and the word cannot accidentally use itself.
     * Building prefixes from left to right means every split reads an already known
     * smaller prefix state.
     *
     * Algorithm:
     *   1. Sort words by length so shorter building blocks enter wordSet first.
     *   2. For each word, run word-break DP against the current wordSet.
     *   3. Add the word to result if dp[length] is true, then insert it into wordSet for later words.
     *
     * Time:  O(n*L^2) - each of n words checks O(L^2) prefix splits and substrings.
     * Space: O(n*L) - the set stores all words, and each check uses an O(L) DP array.
     *
     * @param words unique dictionary words
     * @return all concatenated words in the input order after length sorting
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

    public static void main(String[] args) {
        ConcatenatedWords solver = new ConcatenatedWords();
        String[][] inputs = {
            {"cat", "dog"},
            {"cat", "cats", "catsdogcats", "dog", "dogcatsdog", "hippopotamuses", "rat", "ratcatdogcat"}
        };
        String[] expected = {"[]", "[dogcatsdog, catsdogcats, ratcatdogcat]"};

        for (int i = 0; i < inputs.length; i++) {
            List<String> got = solver.findAllConcatenatedWordsInADict(inputs[i].clone());
            System.out.printf("words=%s -> %s  expected=%s%n", Arrays.toString(inputs[i]), got, expected[i]);
        }
    }
}

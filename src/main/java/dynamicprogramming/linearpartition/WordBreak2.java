package dynamicprogramming.linearpartition;

import java.util.*;


/**
 * Problem: Word Break II
 * LeetCode: https://leetcode.com/problems/word-break-ii/
 *
 * Problem Statement:
 * Given a string `s` and a dictionary of words `wordDict`, return all possible sentences
 * where each word is a valid word in the dictionary.
 *
 * Example:
 * Input: s = "catsanddog", wordDict = ["cat","cats","and","sand","dog"]
 * Output: ["cats and dog","cat sand dog"]
 *
 * Approach: Top-Down DP using Recursion + Memoization
 * - For every starting index, try all end indices
 * - If substring from start to end is a valid word, recursively generate sentences from end index onward
 * - Memoize results for each index to avoid recomputation
 */
public class WordBreak2 {

  /**
   * Steps:
   * 1. Convert the wordDict list to a HashSet for O(1) lookups.
   * 2. Use a recursive helper function that generates sentences starting from a given index.
   * 3. For each possible end index, check if the substring from start to end is in the word set.
   * 4. If it is, recursively generate sentences for the remaining substring.
   * 5. Use a memoization map to store results for already computed start indices to avoid redundant calculations.
   *
   * Time Complexity: O(N^2), where N = length of string str
   * Space Complexity: O(N), for recursion stack and memo array
   *
   * @param str
   * @param wordDict
   * @return
   */
  public List<String> wordBreakRecursiveApproach(String str, List<String> wordDict) {
    Set<String> wordSet = new HashSet<>(wordDict);
    Map<Integer, List<String>> memo = new HashMap<>();
    return wordBreakRecursiveHelper(0, str, wordSet, memo);
  }

  private List<String> wordBreakRecursiveHelper(int start, String str, Set<String> wordSet, Map<Integer, List<String>> memo) {
    if (memo.containsKey(start)) {
      return memo.get(start);
    }

    List<String> result = new ArrayList<>();

    // Base case: if we've reached the end of the string
    if (start == str.length()) {
      result.add(""); // Add empty string to allow sentence concatenation
      return result;
    }

    for (int end = start + 1; end <= str.length(); end++) {
      String prefix = str.substring(start, end);

      if (wordSet.contains(prefix)) {
        List<String> suffixSentences = wordBreakRecursiveHelper(end, str, wordSet, memo);

        for (String sentence : suffixSentences) {
          // If sentence is empty, we don't need extra space
          if (sentence.isEmpty()) {
            result.add(prefix);
          } else {
            result.add(prefix + " " + sentence);
          }
        }
      }
    }

    memo.put(start, result);
    return result;
  }
}
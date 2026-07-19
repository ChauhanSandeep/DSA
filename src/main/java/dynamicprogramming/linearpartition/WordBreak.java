package dynamicprogramming.linearpartition;

import java.util.*;


/**
 * Problem: Word Break
 *
 * Given a string and a dictionary, decide whether the string can be split into
 * one or more dictionary words. Words may be reused, and the split must cover
 * the full string.
 *
 * Leetcode: https://leetcode.com/problems/word-break/ (Medium)
 * Rating:   acceptance 49.7% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | Linear partition | Dictionary lookup
 *
 * Example:
 *   Input:  str = "leetcode", wordDict = ["leet", "code"]
 *   Output: true
 *   Why:    the split "leet code" covers the full string with dictionary words.
 *
 * Follow-ups:
 *   1. Return all valid sentences?
 *      Use memoized DFS that returns sentence lists for each start index.
 *   2. Count the number of segmentations?
 *      Replace booleans with counts and sum valid predecessor counts.
 *   3. Minimize the number of words used?
 *      Store minimum segment counts instead of reachability booleans.
 *
 * Related: Word Break II (140), Concatenated Words (472).
 */
public class WordBreak {

  public static void main(String[] args) {
    WordBreak solver = new WordBreak();
    String[] strings = { "leetcode", "catsandog" };
    List<List<String>> dictionaries = Arrays.asList(
        Arrays.asList("leet", "code"),
        Arrays.asList("cats", "dog", "sand", "and", "cat"));
    boolean[] expected = { true, false };

    for (int i = 0; i < strings.length; i++) {
      boolean got = solver.wordBreakIterativeApproach(strings[i], dictionaries.get(i));
      System.out.printf("str=%s wordDict=%s -> %s  expected=%s%n",
          strings[i], dictionaries.get(i), got, expected[i]);
    }
  }

  /**
   * Top-Down Dynamic Programming using Recursion + Memoization
   *
   * Steps:
   * 1. Convert wordDict to a HashSet for O(1) lookups.
   * 2. Use a recursive helper function that checks if the substring starting from index `start`
   *   can be segmented.
   * 3. For each possible end index, check if the substring from `start` to `end`
   *   is in the word set.
   * 4. If it is, recursively check if the remaining substring can also be segmented.
   * 5. Use a memoization map to store results for already computed start indices
   * to avoid redundant calculations.
   *
   * Time Complexity: O(N^2), where N = length of string str
   * Space Complexity: O(N), for recursion stack and memo array
   */
  public boolean wordBreakRecursive(String str, List<String> wordDict) {
    Set<String> wordSet = new HashSet<>(wordDict);
    Map<Integer, Boolean> memo = new HashMap<>(); // <startIndex, canSegment>
    return canSegmentFromRecHelper(0, str, wordSet, memo);
  }

  private boolean canSegmentFromRecHelper(int start, String str, Set<String> wordSet, Map<Integer, Boolean> memo) {
    if (start == str.length()) return true;
    if (memo.containsKey(start)) return memo.get(start);

    for (int end = start + 1; end <= str.length(); end++) {
      String prefix = str.substring(start, end);
      // If the prefix is in the word set and the rest of the string can be segmented
      // end becomes the next start index
      if (wordSet.contains(prefix) && canSegmentFromRecHelper(end, str, wordSet, memo)) {
        memo.put(start, true);
        return true;
      }
    }

    memo.put(start, false);
    return false;
  }

    /**
   * Intuition: dp[i] means the prefix str[0..i-1] can be segmented into
   * dictionary words. To decide dp[end], try every previous cut position start.
   * If dp[start] is already true and str[start..end-1] is a dictionary word,
   * then the whole prefix through end is segmentable.
   *
   * Algorithm:
   *   1. Put the dictionary words in a set for fast membership checks.
   *   2. Mark dp[0] true because the empty prefix needs no words.
   *   3. For each end index, try all starts and mark dp[end] once a valid final word is found.
   *
   * Time:  O(n^2) - every start/end cut pair can be checked.
   * Space: O(n) - the DP array stores reachability for each prefix length.
   *
   * @param str string to segment
   * @param wordDict dictionary of reusable words
   * @return true if str can be fully segmented into dictionary words
   */
  public boolean wordBreakIterativeApproach(String str, List<String> wordDict) {
    Set<String> wordSet = new HashSet<>(wordDict);
    boolean[] dp = new boolean[str.length() + 1]; // dp[i] means str[0..i-1] is segmentable
    dp[0] = true; // Empty string is always segmentable

    for (int end = 1; end <= str.length(); end++) {
      for (int start = 0; start < end; start++) {
        boolean isFirstPartSegmentable = dp[start]; // str[0..start-1] is segmentable
        boolean isSecondPartInDict = wordSet.contains(str.substring(start, end)); // str[start..end-1] is in dict
        if (isFirstPartSegmentable && isSecondPartInDict) {
          dp[end] = true;
          break;
        }
      }
    }

    return dp[str.length()];
  }
}
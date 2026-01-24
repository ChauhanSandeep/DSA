package dynamicprogramming.linearpartition;

import java.util.*;


/**
 * Problem: Word Break
 * LeetCode: https://leetcode.com/problems/word-break/
 *
 * Problem Statement:
 * Given a non-empty string `s` and a dictionary of words `wordDict`, determine whether `s`
 * can be segmented into a space-separated sequence of one or more dictionary words.
 * The same word from the dictionary may be reused multiple times in the segmentation.
 *
 * Example:
 * Input: s = "leetcode", wordDict = ["leet", "code"]
 * Output: true
 * Explanation: "leetcode" can be segmented as "leet code".
 *
 * Follow-Up Questions:
 * - Return all possible segmentations (see: https://leetcode.com/problems/word-break-ii/)
 * - Minimize the number of segments
 * - Count total number of ways to segment
 */
public class WordBreak {

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
   * Bottom-Up Dynamic Programming
   *
   * Steps:
   * 1. Convert wordDict to a HashSet for fast lookups.
   * 2. Initialize a dp array where dp[i] means str[0..i-1] is segmentable.
   * 3. For each i, check every j < i. If dp[j] is true and str[j..i-1] is in dict, mark dp[i] as true.
   *
   * Time Complexity: O(N^2)
   * Space Complexity: O(N)
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
package dynamicprogramming.LongestCommonSubsequence;

/**
 * Problem: Minimum Window Subsequence
 *
 * Given two strings s1 and s2, return the minimum contiguous substring of s1 such that s2
 * is a subsequence of that substring. If there is no such substring, return an empty string.
 *
 * A subsequence means characters of s2 appear in the substring in the same order but not
 * necessarily consecutively. If multiple substrings of the same minimum length exist,
 * return the one with the leftmost starting index.
 *
 * Example:
 * Input: s1 = "abcdebdde", s2 = "bde"
 * Output: "bcde"
 * Explanation: "bcde" is the answer because it occurs before "bdde" which has the same length.
 * "deb" is not valid because elements of s2 must occur in order in the window.
 *
 * Constraints:
 * - 1 <= s1.length <= 2 * 10^4
 * - 1 <= s2.length <= 100
 * - s1 and s2 consist of lowercase English letters
 *
 * LeetCode Problem: https://leetcode.com/problems/minimum-window-subsequence
 *
 * Follow-up Questions:
 *
 * 1. What if s2 can appear multiple times and you need to find all minimum windows?
 *    Answer: Store each minimum window start and length during the search. After finding all,
 *    filter to keep only those with minimum length and return all matching substrings.
 *
 * 2. How would you modify this to find the maximum window instead of minimum?
 *    Answer: Track the maximum window length instead of minimum. The algorithm structure
 *    remains the same but comparison logic reverses to find largest valid window.
 *
 * 3. What if you need to find windows where s2 appears as a substring instead of subsequence?
 *    Answer: Use simpler sliding window with exact character matching. This becomes easier
 *    since we need consecutive matches rather than maintaining order with gaps.
 *    Related problem: https://leetcode.com/problems/minimum-window-substring/
 *
 * 4. Can you solve this if s2 characters can appear in any order (like anagram)?
 *    Answer: Use frequency map and sliding window to track character counts. Expand window
 *    until all characters are found, then shrink to minimize. This is the classic minimum
 *    window substring problem with character frequency matching.
 *
 * 5. How would you optimize for multiple queries with different s2 values on same s1?
 *    Answer: Preprocess s1 to build a next occurrence map for each character at each position.
 *    This allows O(|s2|) lookup per query after O(26 * |s1|) preprocessing.
 */
public class MinimumWindowSubsequence {

  /**
   * Finds minimum window using two-pointer forward-backward technique.
   *
   * Algorithm:
   * 1. Forward scan: Match all characters of target in source sequentially
   * 2. Once target is fully matched, record the end position
   * 3. Backward scan: Shrink window from end backwards to find start of minimum window
   * 4. Match target in reverse order while moving backwards in source
   * 5. Calculate window length and update minimum if smaller
   * 6. Continue forward scan from position after current window start
   * 7. Repeat until entire source is processed
   *
   * Key insight: After matching target completely, we shrink backwards to find the tightest
   * window. This ensures we don't include unnecessary characters at the beginning.
   *
   * Time Complexity: O(N * M) where N is length of source and M is length of target. In worst case,
   * for each character in source, we might traverse entire target during forward scan, and go back
   * M positions during backward scan.
   *
   * Space Complexity: O(1) as we only use pointers and variables, no additional data structures.
   *
   * @param source the source string to search within
   * @param target the target subsequence to find
   * @return minimum window substring containing target as subsequence, or empty string
   */
  public String minWindow(String source, String target) {
    int sourceLen = source.length();
    int targetLen = target.length();
    int minWindowLen = Integer.MAX_VALUE;
    int minWindowStart = 0;
    int sourcePtr = 0;

    while (sourcePtr < sourceLen) {
      int targetPtr = 0;

      // Forward scan: find where all target characters appear in order
      while (sourcePtr < sourceLen && targetPtr < targetLen) {
        if (source.charAt(sourcePtr) == target.charAt(targetPtr)) {
          targetPtr++;
        }
        sourcePtr++;
      }

      // We found a valid window
      if (targetPtr == targetLen) {
        int windowEnd = sourcePtr;
        targetPtr--;
        sourcePtr--;

        // Backward scan: shrink window from right to left. This finds the start of the minimum window.
        // This is done because there may be extra characters at the start that are not needed.
        // For example, source = "axbxbde" and target = "bde", after forward scan we have window "bxbde".
        // Backward scan will help us find minimum window "bde".
        while (targetPtr >= 0) {
          if (source.charAt(sourcePtr) == target.charAt(targetPtr)) {
            targetPtr--;
          }
          sourcePtr--;
        }

        // sourcePtr is now one position before window start so move it forward
        sourcePtr++;

        int currentWindowLen = windowEnd - sourcePtr;

        if (currentWindowLen < minWindowLen) {
          minWindowLen = currentWindowLen;
          minWindowStart = sourcePtr;
        }

        // Move past current window start to find next potential window
        sourcePtr++;
      }
    }

    return minWindowLen == Integer.MAX_VALUE ? "" : source.substring(minWindowStart, minWindowStart + minWindowLen);
  }

  /**
   * Alternative dynamic programming approach for finding minimum window.
   * Uses 2D DP table where dp[i][j] represents starting index of minimum window
   * ending at position i in source that contains first j characters of target.
   *
   * Algorithm:
   * 1. Create DP table where dp[i][j] = start index of window ending at i containing target[0:j]
   * 2. Initialize: dp[i][0] = i for all i (empty subsequence starts anywhere)
   * 3. For each position i in source and j in target:
   *    - If source[i] matches target[j], window extends from dp[i-1][j-1]
   *    - Otherwise, use previous window from dp[i-1][j]
   * 4. Find minimum length among all dp[i][n2] values
   *
   * Time Complexity: O(N * M) where N is length of source and M is length of target.
   *
   * Space Complexity: O(N * M) for the DP table. Can be optimized to O(M) using
   * rolling array since we only need previous row.
   *
   * @param source the source string to search within
   * @param target the target subsequence to find
   * @return minimum window substring containing target as subsequence, or empty string
   */
  public String minWindowDP(String source, String target) {
    int sourceLen = source.length();
    int targetLen = target.length();
    int[][] dp = new int[sourceLen + 1][targetLen + 1]; // dp[i][j] = start index of window in source[0:i] containing target[0:j]

    // Initialize all positions as invalid
    for (int i = 0; i <= sourceLen; i++) {
      for (int j = 0; j <= targetLen; j++) {
        dp[i][j] = -1;
      }
    }

    // Base case: empty subsequence can start at any position
    for (int i = 0; i <= sourceLen; i++) {
      dp[i][0] = i;
    }

    // Fill DP table
    for (int sourcePointer = 1; sourcePointer <= sourceLen; sourcePointer++) {
      for (int targetPointer = 1; targetPointer <= targetLen; targetPointer++) {
        if (source.charAt(sourcePointer - 1) == target.charAt(targetPointer - 1)) {
          // Characters match: extend window from where j-1 characters were matched
          dp[sourcePointer][targetPointer] = dp[sourcePointer - 1][targetPointer - 1];
        } else {
          // No match: carry forward previous window
          dp[sourcePointer][targetPointer] = dp[sourcePointer - 1][targetPointer];
        }
      }
    }

    int minWindowLen = Integer.MAX_VALUE;
    int minWindowStart = 0;

    for (int endPos = 1; endPos <= sourceLen; endPos++) {
      if (dp[endPos][targetLen] != -1) {
        int startPos = dp[endPos][targetLen]; // Start index of window
        int currentLen = endPos - startPos;

        if (currentLen < minWindowLen) {
          minWindowLen = currentLen;
          minWindowStart = startPos;
        }
      }
    }

    return minWindowLen == Integer.MAX_VALUE ? "" : source.substring(minWindowStart, minWindowStart + minWindowLen);
  }
}

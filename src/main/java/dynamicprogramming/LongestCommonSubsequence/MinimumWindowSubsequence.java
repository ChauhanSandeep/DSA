package dynamicprogramming.longestcommonsubsequence;

import java.util.Arrays;

/**
 * Problem: Minimum Window Subsequence
 *
 * Given source and target strings, return the shortest contiguous substring of
 * source that contains target as a subsequence. If several windows have the same
 * length, return the leftmost one; if none exists, return the empty string.
 *
 * Leetcode: https://leetcode.com/problems/minimum-window-subsequence/ (Hard)
 * Rating:   acceptance 43.8% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | Two pointers | Forward match and backward shrink
 *
 * Example:
 *   Input:  source = "abcdebdde", target = "bde"
 *   Output: "bcde"
 *   Why:    both "bcde" and "bdde" contain b, d, e in order, but "bcde" is
 *           the leftmost minimum-length window.
 *
 * Follow-ups:
 *   1. How would you answer many target queries on the same source?
 *      Precompute next-occurrence positions for each character in source.
 *   2. Return all minimum windows instead of one?
 *      Track every window whose length equals the best length after each shrink.
 *   3. What if target must appear as a substring?
 *      Use direct substring search or sliding window instead of subsequence matching.
 *
 * Related: Minimum Window Substring (76), Longest Common Subsequence (1143).
 */
public class MinimumWindowSubsequence {

  public static void main(String[] args) {
    MinimumWindowSubsequence solver = new MinimumWindowSubsequence();
    String[][] cases = { {"abcdebdde", "bde"}, {"abc", "d"} };
    String[] expected = { "bcde", "" };

    for (int i = 0; i < cases.length; i++) {
      String got = solver.minWindow(cases[i][0], cases[i][1]);
      System.out.printf("strings=%s -> %s  expected=%s%n",
          Arrays.toString(cases[i]), got, expected[i]);
    }
  }

    /**
   * Intuition: a valid window is found only after every target character has
   * been matched in order while scanning source. Once that happens, the right
   * boundary is fixed, so the tightest window ending there is found by walking
   * backward and matching target in reverse. That backward pass removes every
   * unnecessary leading character before the next forward scan begins.
   *
   * Algorithm:
   *   1. Scan source forward until all target characters are matched in order.
   *   2. Record the end, then scan backward to find the tightest start for that end.
   *   3. Update the best window and resume just after the current start.
   *
   * Time:  O(n * m) - repeated forward and backward scans can revisit source characters around each target match.
   * Space: O(1) - only pointers and best-window indices are stored.
   *
   * @param source string to search inside
   * @param target subsequence that must appear inside the returned window
   * @return shortest source substring containing target as a subsequence, or empty string
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

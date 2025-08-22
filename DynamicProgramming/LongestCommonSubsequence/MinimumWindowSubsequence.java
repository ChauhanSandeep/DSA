package DynamicProgramming.LongestCommonSubsequence;

import java.util.Arrays;


/**
 * Problem: Minimum Window Subsequence
 *
 * Given strings s and t, return the minimum window substring of s such that every character in t
 * (including duplicates) is included in the window subsequence. If there is no such substring, return the empty string "".
 * A subsequence of a string is a new string that is formed from the original string by deleting
 * some (can be none) of the characters without disturbing the relative positions of the remaining characters.
 *
 * Example:
 * Input: s = "abcdebdde", t = "bde"
 * Output: "bcde"
 * Explanation: "bcde" is the answer because it occurs before "bdde" which has the same length.
 * "deb" is not a smaller window because the elements of t in the window must occur in order.
 *
 * LeetCode Problem Link: https://leetcode.com/problems/minimum-window-subsequence/
 *
 * Follow-up Questions:
 * 1. Q: What if we need to find all minimum windows of the same length?
 *    A: Track all positions with minimum length during DP traversal and return list of substrings
 * 2. Q: How to handle case where characters can be reused in target?
 *    A: Modify DP state to track character frequencies instead of simple matching
 * 3. Q: What about maximum window subsequence?
 *    A: Change min comparison to max and track longest valid windows
 * 4. Q: Find minimum window substring (not subsequence) containing all characters of target?
 *    A: Use sliding window with character frequency map (https://leetcode.com/problems/minimum-window-substring/)
 * 5. Q: Count number of distinct minimum window subsequences?
 *    A: Add counting dimension to DP state: dp[i][j][count]
 */
public class MinimumWindowSubsequence {

  public static void main(String[] args) {
    MinimumWindowSubsequence solver = new MinimumWindowSubsequence();
    String sourceString = "abcdebdde";
    String targetSequence = "bde";

    System.out.println("Minimum Window (DP): " + solver.minWindow(sourceString, targetSequence));
    System.out.println("Minimum Window (Space Optimized): " + solver.minWindowOptimized(sourceString, targetSequence));
    System.out.println("Minimum Window (Two Pointers): " + solver.minWindowTwoPointers(sourceString, targetSequence));
  }

  /**
   * Finds minimum window substring containing target as subsequence using 2D dynamic programming.
   *
   * Algorithm Steps:
   * 1. Create DP table where dp[i][j] represents starting position of window ending at j that contains target[0..i-1]
   * 2. Base case: dp[0][j] = j+1 (empty target can match at any position)
   * 3. If characters match: dp[i][j] = dp[i-1][j-1] (inherit start from diagonal)
   * 4. If no match: dp[i][j] = dp[i][j-1] (inherit start from left, skip current source char)
   * 5. Scan last row to find minimum length window
   *
   * Time Complexity: O(sourceLen * targetLen) for filling DP table
   * Space Complexity: O(sourceLen * targetLen) for 2D DP table
   *
   * @param sourceString The source string to search within
   * @param targetSequence The target subsequence to find
   * @return Minimum window substring containing target as subsequence
   */
  public String minWindow(String sourceString, String targetSequence) {
    if (sourceString == null || targetSequence == null || sourceString.length() < targetSequence.length()) {
      return "";
    }

    int sourceLength = sourceString.length();
    int targetLength = targetSequence.length();

    // dp[i][j] = starting position of window ending at position j that contains target[0..i-1]
    // -1 means no valid window exists
    int[][] windowStartPosition = new int[targetLength + 1][sourceLength + 1];

    // Initialize DP table with -1 (no valid window)
    for (int[] row : windowStartPosition) {
      Arrays.fill(row, -1);
    }

    // Base case: empty target can match at any position
    for (int sourceIndex = 0; sourceIndex <= sourceLength; sourceIndex++) {
      windowStartPosition[0][sourceIndex] = sourceIndex + 1; // 1-based indexing
    }

    // Fill DP table
    for (int targetIndex = 1; targetIndex <= targetLength; targetIndex++) {
      for (int sourceIndex = 1; sourceIndex <= sourceLength; sourceIndex++) {

        char targetChar = targetSequence.charAt(targetIndex - 1);
        char sourceChar = sourceString.charAt(sourceIndex - 1);

        if (targetChar == sourceChar) {
          // Characters match: inherit starting position from diagonal
          windowStartPosition[targetIndex][sourceIndex] = windowStartPosition[targetIndex - 1][sourceIndex - 1];
        } else {
          // No match: inherit from left (skip current source character)
          windowStartPosition[targetIndex][sourceIndex] = windowStartPosition[targetIndex][sourceIndex - 1];
        }
      }
    }

    // Find minimum window by scanning last row
    int minWindowLength = sourceLength + 1;
    int bestStartIndex = -1;

    for (int sourceIndex = 1; sourceIndex <= sourceLength; sourceIndex++) {
      int windowStart = windowStartPosition[targetLength][sourceIndex];

      if (windowStart != -1) { // Valid window found
        int actualStartIndex = windowStart - 1; // Convert to 0-based
        int windowLength = sourceIndex - actualStartIndex;

        if (windowLength < minWindowLength) {
          minWindowLength = windowLength;
          bestStartIndex = actualStartIndex;
        }
      }
    }

    return bestStartIndex == -1 ? "" : sourceString.substring(bestStartIndex, bestStartIndex + minWindowLength);
  }

  /**
   * Space-optimized version using 1D DP array instead of 2D table.
   *
   * Algorithm Steps:
   * 1. Use rolling array technique with previous and current row
   * 2. Process each target character one by one
   * 3. Update current row based on previous row values
   * 4. Swap arrays for next iteration
   *
   * Time Complexity: O(sourceLen * targetLen)
   * Space Complexity: O(sourceLen) using only two 1D arrays
   *
   * @param sourceString The source string to search within
   * @param targetSequence The target subsequence to find
   * @return Minimum window substring containing target as subsequence
   */
  public String minWindowOptimized(String sourceString, String targetSequence) {
    if (sourceString == null || targetSequence == null || sourceString.length() < targetSequence.length()) {
      return "";
    }

    int sourceLength = sourceString.length();
    int targetLength = targetSequence.length();

    // Use two arrays for space optimization
    int[] previousRow = new int[sourceLength + 1];
    int[] currentRow = new int[sourceLength + 1];

    // Initialize base case
    Arrays.fill(previousRow, -1);
    for (int i = 0; i <= sourceLength; i++) {
      previousRow[i] = i + 1;
    }

    // Process each target character
    for (int targetIndex = 1; targetIndex <= targetLength; targetIndex++) {
      Arrays.fill(currentRow, -1);

      for (int sourceIndex = 1; sourceIndex <= sourceLength; sourceIndex++) {
        char targetChar = targetSequence.charAt(targetIndex - 1);
        char sourceChar = sourceString.charAt(sourceIndex - 1);

        if (targetChar == sourceChar) {
          currentRow[sourceIndex] = previousRow[sourceIndex - 1];
        } else {
          currentRow[sourceIndex] = currentRow[sourceIndex - 1];
        }
      }

      // Swap arrays for next iteration
      int[] temp = previousRow;
      previousRow = currentRow;
      currentRow = temp;
    }

    // Find minimum window
    int minWindowLength = sourceLength + 1;
    int bestStartIndex = -1;

    for (int sourceIndex = 1; sourceIndex <= sourceLength; sourceIndex++) {
      int windowStart = previousRow[sourceIndex];

      if (windowStart != -1) {
        int actualStartIndex = windowStart - 1;
        int windowLength = sourceIndex - actualStartIndex;

        if (windowLength < minWindowLength) {
          minWindowLength = windowLength;
          bestStartIndex = actualStartIndex;
        }
      }
    }

    return bestStartIndex == -1 ? "" : sourceString.substring(bestStartIndex, bestStartIndex + minWindowLength);
  }

  /**
   * Alternative two-pointer approach for finding minimum window subsequence.
   *
   * Algorithm Steps:
   * 1. Use two pointers to find each valid window
   * 2. Forward pass: find end of window by matching all target characters
   * 3. Backward pass: find start of window by matching target in reverse
   * 4. Track minimum window found across all iterations
   *
   * Time Complexity: O(sourceLen * targetLen) in worst case
   * Space Complexity: O(1) excluding result string
   *
   * @param sourceString The source string to search within
   * @param targetSequence The target subsequence to find
   * @return Minimum window substring containing target as subsequence
   */
  public String minWindowTwoPointers(String sourceString, String targetSequence) {
    if (sourceString == null || targetSequence == null || sourceString.length() < targetSequence.length()) {
      return "";
    }

    int sourceLength = sourceString.length();
    int targetLength = targetSequence.length();
    int minWindowLength = Integer.MAX_VALUE;
    int bestStartIndex = -1;

    int sourcePointer = 0;

    while (sourcePointer < sourceLength) {
      int targetPointer = 0;

      // Forward pass: find end of current window
      while (sourcePointer < sourceLength && targetPointer < targetLength) {
        if (sourceString.charAt(sourcePointer) == targetSequence.charAt(targetPointer)) {
          targetPointer++;
        }
        sourcePointer++;
      }

      // If we couldn't match all target characters, no more windows exist
      if (targetPointer < targetLength) {
        break;
      }

      // Backward pass: find start of current window
      int windowEnd = sourcePointer - 1;
      targetPointer = targetLength - 1;
      sourcePointer--;

      while (targetPointer >= 0) {
        if (sourceString.charAt(sourcePointer) == targetSequence.charAt(targetPointer)) {
          targetPointer--;
        }
        sourcePointer--;
      }

      int windowStart = sourcePointer + 1;
      int currentWindowLength = windowEnd - windowStart + 1;

      // Update minimum window if current is smaller
      if (currentWindowLength < minWindowLength) {
        minWindowLength = currentWindowLength;
        bestStartIndex = windowStart;
      }

      // Move to next potential window start
      sourcePointer = windowStart + 1;
    }

    return bestStartIndex == -1 ? "" : sourceString.substring(bestStartIndex, bestStartIndex + minWindowLength);
  }
}

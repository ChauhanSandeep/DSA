package dynamicprogramming.LongestCommonSubsequence;

import java.util.Arrays;


public class LongestCommonSubsequence {

  /**
   * Problem: Longest Common Subsequence
   * Statement: Given two strings, find the length of their longest common subsequence.
   *
   * Intuition:
   * - A subsequence is a sequence derived from another sequence where some elements may be deleted.
   * - The longest common subsequence (LCS) is the longest sequence that appears in both strings in the same order.
   *
   * Recursive Approach:
   * - Use recursion to explore all possible subsequences.
   * - If the character matches, make recursive calls for the next characters in both strings.
   * - If they don't match, explore both possibilities: skipping a character from either string.
   *
   * Time Complexity: O(M * N) (where M and N are lengths of the two strings)
   * Space Complexity: O(M * N) for memoization + O(M + N) for recursion stack
   *
   *
   * @param text1
   * @param text2
   * @return
   */
  public static int findLcsRecursive(String text1, String text2) {
    // Initialize memoization table with -1 (meaning un calculated)
    int[][] dp = new int[text1.length()][text2.length()];
    for (int[] row : dp) {
      Arrays.fill(row, -1);
    }

    return findLcsHelper(text1, text2, 0, 0, dp);
  }

  private static int findLcsHelper(String text1, String text2, int i, int j, int[][] dp) {
    // Base Case: If any string is exhausted
    if (i == text1.length() || j == text2.length()) {
      return 0;
    }

    // If already computed, return stored value
    if (dp[i][j] != -1) {
      return dp[i][j];
    }

    // If characters match,  daddamove diagonally
    if (text1.charAt(i) == text2.charAt(j)) {
      dp[i][j] = 1 + findLcsHelper(text1, text2, i + 1, j + 1, dp);
    } else {
      // Else, try skipping one character either from text1 or text2
      dp[i][j] = Math.max(
          findLcsHelper(text1, text2, i + 1, j, dp),
          findLcsHelper(text1, text2, i, j + 1, dp)
      );
    }
    return dp[i][j];
  }

  /**
   * Iterative Approach (Bottom-Up DP):
   *
   * Intuition:
   * - Build a DP table where dp[i][j] represents the length of LCS of text1[0..i-1] and text2[0..j-1].
   * - If characters match, increment the diagonal value.
   * - If they don't match, take the maximum from left or top cell.
   *
   * Time Complexity: O(M * N)
   * Space Complexity: O(M * N)
   *
   * @param text1
   * @param text2
   * @return
   */
  public static int findLcsIterative(String text1, String text2) {
    int length1 = text1.length();
    int length2 = text2.length();

    // Create a DP table
    int[][] dp = new int[length1 + 1][length2 + 1];

    // Build the table bottom-up
    for (int i = 1; i <= length1; i++) {
      for (int j = 1; j <= length2; j++) {
        // If characters match, 1 + diagonal
        if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
          dp[i][j] = 1 + dp[i - 1][j - 1]; // check if the characters match in previous indices of both strings
        } else {
          // check if the character matches in previous indices of either string, take the max
          dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
        }
      }
    }
    printLongestCommonSubsequence(dp, text1, text2);

    return dp[length1][length2];
  }

  /**
   * Print the longest common subsequence from the dp table
   */
  private static void printLongestCommonSubsequence(int[][] dp, String text1, String text2) {
    // Backtrack from dp[m][n]
    StringBuilder sb = new StringBuilder();
    int i = text1.length();
    int j = text2.length();
    // Traverse the dp table to find the LCS
    while (i > 0 && j > 0) {
      if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
        // If characters match, add to result
        sb.append(text1.charAt(i - 1));
        i--;
        j--;
      } else if (dp[i - 1][j] > dp[i][j - 1]) {
        // Move in the direction of the larger value because it indicates a longer subsequence.
        // This is how we created the dp table to find the longest common subsequence.
        i--;
      } else {
        j--;
      }
    }

    System.out.println(sb.reverse());
  }
}

package strings.twopointers;

/**
 * Problem: Minimum Insertion Steps to Make a String Palindrome
 *
 * Given a string, return the fewest characters that must be inserted anywhere
 * to make it a palindrome. Insertions can mirror unmatched characters from either
 * side.
 *
 * Leetcode: https://leetcode.com/problems/minimum-insertion-steps-to-make-a-string-palindrome/ (Hard)
 * Rating:   contest Elo 1787
 * Pattern:  Strings | Dynamic programming | Interval DP
 *
 * Example:
 *   Input:  s = "mbadm"
 *   Output: 2
 *   Why:    two insertions can form a palindrome such as "mbdadbm".
 *
 * Follow-ups:
 *   1. How do you reconstruct one resulting palindrome?
 *      Walk the DP table and append mirrored characters as decisions are made.
 *   2. How is this related to LCS?
 *      The answer is n minus the longest palindromic subsequence length.
 *   3. Can space be reduced to O(n)?
 *      Yes, keep only the previous interval row/diagonal values needed by DP.
 *
 * Related: Longest Palindromic Subsequence (516), Edit Distance (72).
 */

public class MinInsertionPalindrome {

  public static void main(String[] args) {
    String[] inputs = {"mbadm", "zzazz", "geeks"};
    int[] expected = {2, 0, 3};

    for (int i = 0; i < inputs.length; i++) {
      int got = minInsertionsDP(inputs[i]);
      System.out.printf("s=%s -> %d  expected=%d%n", inputs[i], got, expected[i]);
    }
  }


    /**
   * Intuition: compare the two ends. Matching ends can stay together, but a
   * mismatch means one side needs a mirrored insertion, so try both choices and
   * keep the cheaper one.
   *
   * Algorithm:
   *   1. Recurse on the full range.
   *   2. If the ends cross, no insertion is needed.
   *   3. If end characters match, recurse inward.
   *   4. Otherwise insert against the left or right side and take the minimum plus one.
   *
   * Time:  O(2^n) - overlapping subproblems are recomputed recursively.
   * Space: O(n) - recursion depth can reach the string length.
   *
   * @param input String to make palindromic.
   * @return Minimum insertions needed.
   */
  public static int minInsertionsRecursive(String input) {
    return recursiveHelper(input, 0, input.length() - 1);
  }

  /** Solves the minimum-insertion problem for input[left, right]. */
  private static int recursiveHelper(String input, int left, int right) {
    // Base case: string is already a palindrome
      if (left >= right) {
          return 0;
      }

    if (input.charAt(left) == input.charAt(right)) {
      return recursiveHelper(input, left + 1, right - 1);
    }

    int insertLeft = recursiveHelper(input, left + 1, right);
    int insertRight = recursiveHelper(input, left, right - 1);

    return Math.min(insertLeft, insertRight) + 1;
  }

    /**
   * Intuition: dp[left][right] stores the minimum insertions for one substring.
   * Matching ends inherit the inside answer; mismatching ends need one insertion
   * plus the cheaper of skipping the left or right end.
   *
   * Algorithm:
   *   1. Create a DP table over substring boundaries.
   *   2. Fill by increasing substringLength so smaller intervals are ready first.
   *   3. Copy the inner answer when ends match.
   *   4. Otherwise take min(left-skipped, right-skipped) + 1.
   *
   * Time:  O(n^2) - every substring interval is evaluated once.
   * Space: O(n^2) - the DP table stores all intervals.
   *
   * @param input String to make palindromic.
   * @return Minimum insertions needed.
   */
  public static int minInsertionsDP(String input) {
    int length = input.length();
    int[][] dp = new int[length][length];

    // Bottom-up: fill diagonally by increasing substring length
    for (int substringLength = 2; substringLength <= length; substringLength++) {
      for (int left = 0; left <= length - substringLength; left++) {
        int right = left + substringLength - 1;

        if (input.charAt(left) == input.charAt(right)) {
          dp[left][right] = dp[left + 1][right - 1]; // min insertions required in the inner substring
        } else {
          dp[left][right] = Math.min(
              dp[left + 1][right],
              dp[left][right - 1]
          ) + 1;
        }
      }
    }

    return dp[0][length - 1]; // Min insertions to convert entire string to palindrome
  }
}

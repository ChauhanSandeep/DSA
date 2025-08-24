package dynamicprogramming;

/**
 * Problem: Interleaving String
 * LeetCode: https://leetcode.com/problems/interleaving-string/
 *
 * Given three strings s1, s2, and s3, determine whether s3 is formed by an interleaving of s1 and s2.
 * Interleaving means characters from both strings are used in a way that maintains the left-to-right order
 * of characters from each string individually.
 *
 * Example:
 * Input: s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"
 * Output: true
 * Explanation: "aa" + "dbbc" + "bc" + "a" + "c" = "aadbbcbcac".
 *
 * Follow-Up Questions:
 * - Can we use 2 pointers approach?
 *   - No, example : s1 = "aab" s2 = "aac" s3 = "aaabac". The order in which we move the pointer matters
 * - Can you solve this using Bottom-Up DP with reduced space? (Yes: 2D array → 1D array)
 * - Can you solve using BFS/DFS iterative approach?
 *   LeetCode: https://leetcode.com/problems/interleaving-string/
 */
public class InterleavingString {

  public static void main(String[] args) {
    InterleavingString solver = new InterleavingString();
    System.out.println(solver.isInterleavingTopDown("aab", "axy", "aaxaaby")); // true
    System.out.println(solver.isInterleaveBottomUp("aab", "axy", "aaxaaby")); // true
  }

  /**
   * Top-Down Recursive Approach with Memoization.
   *
   * Steps:
   * 1. Check if combined lengths of s1 and s2 match s3; return false if not.
   * 2. Use a 2D memoization array where memo[i][j] stores whether s3[i + j:] can be formed using s1[i:] and s2[j:].
   * 3. Recursively attempt to match current character of s3 with either s1 or s2:
   *    - If s1[i] == s3[i+j], recurse with i+1, j.
   *    - If s2[j] == s3[i+j], recurse with i, j+1.
   * 4. Store and return the result to avoid recomputation.
   *
   * Algorithm: Top-Down DP with Memoization (Recursion + Caching)
   *
   * Time Complexity: O(s1Length * s2Length)
   * Space Complexity: O(s1Length * s2Length) for memoization table
   *
   * @param first     First string (s1)
   * @param second    Second string (s2)
   * @param target    Target string to match (s3)
   * @return true if target is a valid interleaving of first and second
   */
  public boolean isInterleavingTopDown(String first, String second, String target) {
    if (first.length() + second.length() != target.length()) {
      return false;
    }

    Boolean[][] memo = new Boolean[first.length() + 1][second.length() + 1];
    return isInterleaveRecursive(first, second, target, 0, 0, memo);
  }

  private boolean isInterleaveRecursive(String first, String second, String target, int indexFirst, int indexSecond,
      Boolean[][] memo) {
    if (indexFirst == first.length() && indexSecond == second.length()) {
      return true;
    }

    if (memo[indexFirst][indexSecond] != null) {
      return memo[indexFirst][indexSecond];
    }

    int indexTarget = indexFirst + indexSecond;
    boolean isValid = false;

    // Try to match current character of target with first
    if (indexFirst < first.length() && first.charAt(indexFirst) == target.charAt(indexTarget)) {
      isValid = isInterleaveRecursive(first, second, target, indexFirst + 1, indexSecond, memo);
    }
    if (isValid) {
      memo[indexFirst][indexSecond] = true;
      return true;
    }

    // If first index match didn't work, try to match current character of target with second
    if (indexSecond < second.length() && second.charAt(indexSecond) == target.charAt(indexTarget)) {
      isValid = isInterleaveRecursive(first, second, target, indexFirst, indexSecond + 1, memo);
    }

    memo[indexFirst][indexSecond] = isValid;
    return isValid;
  }

  /**
   * Bottom-Up Dynamic Programming Approach.
   *
   * Steps:
   * 1. Create a 2D DP table where dp[i][j] = true if target[0...i+j-1] can be formed using first[0...i-1] and second[0...j-1].
   * 2. Initialize dp[0][0] = true (empty strings form empty target).
   * 3. Fill first row and column using direct character matches with target.
   * 4. For each dp[i][j], check if:
   *    - dp[i-1][j] is true and first[i-1] matches target[i+j-1]
   *    - OR dp[i][j-1] is true and second[j-1] matches target[i+j-1]
   * 5. Final result is dp[first.length()][second.length()]
   *
   * Algorithm: Bottom-Up Dynamic Programming
   *
   * Time Complexity: O(firstLength * secondLength)
   * Space Complexity: O(firstLength * secondLength)
   *
   * @param first  First input string
   * @param second Second input string
   * @param target Interleaved result string
   * @return True if target is valid interleaving of first and second
   */
  public boolean isInterleaveBottomUp(String first, String second, String target) {
    int lengthFirst = first.length();
    int lengthSecond = second.length();

    if (lengthFirst + lengthSecond != target.length()) {
      return false;
    }

    boolean[][] dp = new boolean[lengthFirst + 1][lengthSecond + 1];
    dp[0][0] = true;

    // Only use characters from second to match target
    for (int j = 1; j <= lengthSecond; j++) {
      dp[0][j] = dp[0][j - 1] && second.charAt(j - 1) == target.charAt(j - 1);
    }

    // Only use characters from first to match target
    for (int i = 1; i <= lengthFirst; i++) {
      dp[i][0] = dp[i - 1][0] && first.charAt(i - 1) == target.charAt(i - 1);
    }

    // Fill full DP table
    for (int i = 1; i <= lengthFirst; i++) {
      for (int j = 1; j <= lengthSecond; j++) {
        char charFromFirst = first.charAt(i - 1);
        char charFromSecond = second.charAt(j - 1);
        char charFromTarget = target.charAt(i + j - 1);

        dp[i][j] =
            (dp[i - 1][j] && charFromFirst == charFromTarget) || (dp[i][j - 1] && charFromSecond == charFromTarget);
      }
    }

    return dp[lengthFirst][lengthSecond];
  }
}
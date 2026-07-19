package dynamicprogramming.stringmatching;

/**
 * Problem: Interleaving String
 *
 * Decide whether target can be formed by using every character from two source strings while preserving each source order.
 *
 * Leetcode: https://leetcode.com/problems/interleaving-string/ (Medium)
 * Rating:   not available (not a contest problem)
 * Pattern:  Dynamic programming | String matching | Prefix feasibility
 *
 * Example:
 *   Input:  first = "aabcc", second = "dbbca", target = "aadbbcbcac"
 *   Output: true
 *   Why:    each target prefix can be formed from a valid prefix of the two sources.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Edit Distance (72), Distinct Subsequences (115).
 */
public class InterleavingString {

    public static void main(String[] args) {
    InterleavingString solver = new InterleavingString();
    String[][] inputs = { {"aabcc", "dbbca", "aadbbcbcac"}, {"aabcc", "dbbca", "aadbbbaccc"}, {"", "abc", "abc"} };
    boolean[] expected = {true, false, true};
    for (int i = 0; i < inputs.length; i++) {
      boolean got = solver.isInterleaveBottomUp(inputs[i][0], inputs[i][1], inputs[i][2]);
      System.out.printf("first=%s second=%s target=%s -> %s  expected=%s%n", inputs[i][0], inputs[i][1], inputs[i][2], got, expected[i]);
    }
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

  /** Solves one memoized interleaving suffix state. */
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
   * Intuition: dp[i][j] means first[0..i - 1] and second[0..j - 1] can form target[0..i + j - 1]. The last target character must come from either first or second, so top and left predecessor states decide the cell.
   *
   * Algorithm:
   *   1. Reject mismatched total lengths.
   *   2. Create dp and set dp[0][0] = true.
   *   3. Initialize first row and first column.
   *   4. Fill inner cells from top and left predecessor states.
   *   5. Return dp[lengthFirst][lengthSecond].
   *
   * Time:  O(lengthFirst * lengthSecond) - every prefix pair is checked.
   * Space: O(lengthFirst * lengthSecond) - stores the table.
   *
   * @param first first source
   * @param second second source
   * @param target candidate interleaving
   * @return true if target is a valid interleaving
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
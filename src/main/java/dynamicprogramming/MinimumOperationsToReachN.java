package dynamicprogramming;

/**
 * Problem: Minimum Operations to Reach N
 *
 * Given a number `N`, find the minimum number of operations required to reach `N` starting from `0`.
 * Allowed operations:
 * 1. **Double the number** (Multiply by 2)
 * 2. **Add one** (+1)
 *
 * Example:
 * Input: N = 3
 * Output: 3
 * Explanation: Operations sequence is 0 → 1 → 2 → 3
 *
 * LeetCode Equivalent: Closest problem is "Broken Calculator"
 * https://leetcode.com/problems/broken-calculator/
 * (But in this variation we only start from 0.)
 *
 * Follow-up Questions for Interviews:
 * 1. Q: Can we solve using a greedy reverse approach?
 *    A: Yes, starting from N and reducing it backward to 0 using reverse operations (/2 if even, -1 if odd).
 * 2. Q: Can the space complexity be optimized?
 *    A: Yes, we only need the last state, thus O(1) space using greedy.
 * 3. Q: What about very large N (1e9)?
 *    A: Dynamic programming won't scale, so greedy reverse solution is preferred.
 *
 */
public class MinimumOperationsToReachN {

  public static void main(String[] args) {
    int target = 15;
    System.out.println("DP Approach: Minimum operations to reach " + target + ": " + minOperationsDP(target));
    System.out.println("Greedy Approach: Minimum operations to reach " + target + ": " + minOperationsGreedy(target));
  }

  /**
   * Dynamic Programming Bottom-Up approach.
   *
   * Steps:
   * 1. Initialize dp[0] = 0 since no operations needed to reach 0.
   * 2. Iterate from 1 to target:
   *    - If number is even: dp[i] = min(dp[i/2] + 1, dp[i-1] + 1)
   *    - If number is odd: dp[i] = dp[i-1] + 1
   *
   * Time Complexity: O(N)
   * Space Complexity: O(N)
   *
   * @param target the number we want to reach from 0
   * @return minimum operations required
   */
  public static int minOperationsDP(int target) {
      if (target == 0) {
          return 0;
      }

    int[] dp = new int[target + 1];
    dp[0] = 0;
    dp[1] = 1;

    for (int i = 2; i <= target; i++) {
      if (i % 2 == 0) {
        dp[i] = Math.min(dp[i / 2] + 1, dp[i - 1] + 1);
      } else {
        dp[i] = dp[i - 1] + 1;
      }
    }

    return dp[target];
  }

  /**
   * Greedy Reverse Approach (Interview-Optimized).
   *
   * Idea:
   * - Instead of building from 0 → N, reverse the process: start from N and reduce to 0
   * - Reverse operations:
   *   - If N is even → divide by 2
   *   - If N is odd → subtract 1 (make it even, then divide)
   *
   * Time Complexity: O(log N)
   * Space Complexity: O(1)
   *
   * @param target the number we want to reach from 0
   * @return minimum operations required
   */
  public static int minOperationsGreedy(int target) {
    int operations = 0;
    while (target > 0) {
      if (target % 2 == 0) {
        target /= 2;
      } else {
        target -= 1;
      }
      operations++;
    }
    return operations;
  }
}

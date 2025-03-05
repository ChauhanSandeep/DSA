package DynamicProgramming;

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
 * Output: 3  (Operations: 0 → 1 → 2 → 3)
 * 
 * Approach:
 * - Use **Dynamic Programming (Bottom-Up)**.
 * - Let `dp[i]` represent the **minimum operations** required to reach `i`.
 * - Transition:
 *   - If `i` is even: `dp[i] = min(dp[i/2] + 1, dp[i-1] + 1)`.
 *   - If `i` is odd: `dp[i] = dp[i-1] + 1` (because we can only reach it from `i-1`).
 * 
 * Time Complexity: **O(N)**
 * Space Complexity: **O(N)**
 */
public class MinOperations {
    public static void main(String[] args) {
        int target = 3;
        System.out.println("Minimum operations to reach " + target + ": " + minOperations(target));
    }

    public static int minOperations(int target) {
        if (target == 0) return 0; // Edge case: No operations needed for 0

        int[] dp = new int[target + 1]; // dp[i] stores min operations to reach i
        dp[1] = 1; // Base case: 0 → 1 (1 operation)

        for (int i = 2; i <= target; i++) {
            if (i % 2 == 0) {
                dp[i] = Math.min(dp[i / 2] + 1, dp[i - 1] + 1);
            } else {
                dp[i] = dp[i - 1] + 1; // Odd numbers can only be reached from i-1
            }
        }

        return dp[target];
    }
}

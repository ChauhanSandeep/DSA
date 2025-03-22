package String;

/**
 * Problem: Given a string, find the minimum number of insertions required to make it a palindrome.
 *
 * Approaches:
 * 1. **Recursive Approach - O(2ⁿ) (Exponential, Inefficient)**
 *    - Compares characters from both ends and recursively finds the minimum insertions.
 *    - **Issue:** Leads to **TLE** for large inputs due to redundant computations.
 *
 * 2. **Dynamic Programming Approach - O(n²)**
 *    - Uses a DP table where `dp[i][j]` stores the minimum insertions required for substring `s[i:j]`.
 *    - Follows bottom-up approach, **eliminates redundant calculations**.
 *
 * Time Complexity: **O(n²)**  
 * Space Complexity: **O(n²) for DP Table**  
 */
public class MinInsertionPalindrome {
    public static void main(String[] args) {
        String input = "geeks";
        
        System.out.println("Min Insertions (Recursive): " + minInsertionsRecursive(input));
        System.out.println("Min Insertions (DP): " + minInsertionsDP(input));
    }

    /**
     * **Recursive Approach (Inefficient)**
     * Time Complexity: O(2ⁿ), Space Complexity: O(n) (due to recursion depth)
     */
    public static int minInsertionsRecursive(String input) {
        return minInsertionsRecHelper(input, 0, input.length() - 1);
    }

    private static int minInsertionsRecHelper(String input, int left, int right) {
        // Base cases
        if (left >= right) return 0; // Single character or empty string is already a palindrome

        // If characters match, move inward
        if (input.charAt(left) == input.charAt(right)) {
            return minInsertionsRecHelper(input, left + 1, right - 1);
        }

        // Otherwise, we need an insertion either at the left or right
        return Math.min(
                minInsertionsRecHelper(input, left + 1, right),
                minInsertionsRecHelper(input, left, right - 1)
        ) + 1;
    }

    /**
     * **Optimized Dynamic Programming Approach**
     * Time Complexity: O(n²), Space Complexity: O(n²)
     */
    public static int minInsertionsDP(String input) {
        int n = input.length();
        int[][] dp = new int[n][n];

        // Fill the DP table bottom-up
        for (int len = 2; len <= n; len++) { // length of substring
            for (int left = 0; left <= n - len; left++) {
                int right = left + len - 1;

                if (input.charAt(left) == input.charAt(right)) {
                    dp[left][right] = dp[left + 1][right - 1]; // No insertion needed
                } else {
                    dp[left][right] = Math.min(dp[left + 1][right], dp[left][right - 1]) + 1;
                }
            }
        }

        return dp[0][n - 1]; // Minimum insertions for entire string
    }
}

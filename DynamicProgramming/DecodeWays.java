package DynamicProgramming;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Decode Ways
 *
 * Given a string of digits representing an encoded message, determine the number
 * of ways it can be decoded, assuming:
 * - 'A' -> 1, 'B' -> 2, ..., 'Z' -> 26
 * - Leading zeros are invalid.
 *
 * Approach:
 * 1. **Recursive + Memoization:** Uses a top-down approach with a HashMap for memoization.
 * 2. **Iterative (Bottom-Up DP):** Uses a table (`dp[]`) to avoid recursion overhead.
 *
 * Time Complexity:
 * - **Recursive with Memoization:** O(N) (Each subproblem is solved once)
 * - **Iterative DP:** O(N) (Single pass through the string)
 *
 * Space Complexity:
 * - **Recursive with Memoization:** O(N) (HashMap stores computed values)
 * - **Iterative DP:** O(N) (DP array)
 *
 * LeetCode Problem Link:
 * https://leetcode.com/problems/decode-ways/
 */
public class DecodeWays {

    public static void main(String[] args) {
        String str = "226";
        System.out.println("Recursive Memoization: " + numDecodingsMemo(str));
        System.out.println("Iterative DP: " + numDecodingsDP(str));
        System.out.println("Iterative DP: " + numDecodingsDP("206"));
        System.out.println("Iterative DP: " + numDecodingsDP("228"));
    }

    /**
     * Recursive approach with memoization
     * @param str Input encoded string
     * @return Number of ways to decode
     */
    public static int numDecodingsMemo(String str) {
        if (str.isEmpty()) return 0;
        Map<Integer, Integer> memo = new HashMap<>();
        return decodeHelper(0, str, memo);
    }

    /**
     * Helper function for recursive decoding with memoization
     * @param index Current position in the string
     * @param str Input encoded string
     * @param memo Memoization map to store subproblem results
     * @return Number of ways to decode from this index onwards
     */
    private static int decodeHelper(int index, String str, Map<Integer, Integer> memo) {
        int length = str.length();
        
        // Base case: Reached the end of the string
        if (index == length) return 1;

        // Leading zero means invalid encoding
        if (str.charAt(index) == '0') return 0;

        // Return cached result if already computed
        if (memo.containsKey(index)) return memo.get(index);

        // Consider single-digit decoding
        int ways = decodeHelper(index + 1, str, memo);

        // Consider two-digit decoding if valid (10-26)
        if (index < length - 1) {
            int twoDigit = Integer.parseInt(str.substring(index, index + 2));
            if (twoDigit <= 26) {
                ways += decodeHelper(index + 2, str, memo);
            }
        }

        // Store computed result
        memo.put(index, ways);
        return ways;
    }

    /**
     * Iterative bottom-up DP approach (More efficient)
     * @param str Input encoded string
     * @return Number of ways to decode
     */
    public static int numDecodingsDP(String str) {
        if (str.isEmpty() || str.charAt(0) == '0') return 0; // Edge case

        int length = str.length();
        int[] dp = new int[length + 1];

        dp[length] = 1; // Base case: One way to decode empty string

        for (int i = length - 1; i >= 0; i--) {
            if (str.charAt(i) == '0') {
                dp[i] = 0; // Leading zero makes it invalid
            } else {
                dp[i] = dp[i + 1]; // Decode as a single digit

                if (i < length - 1) {
                    int twoDigit = Integer.parseInt(str.substring(i, i + 2));
                    if (twoDigit <= 26) {
                        dp[i] += dp[i + 2]; // Decode as two digits
                    }
                }
            }
        }

        return dp[0]; // Final answer stored at dp[0]
    }
}

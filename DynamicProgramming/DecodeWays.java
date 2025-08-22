package DynamicProgramming;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Decode Ways
 *
 * Given a string of digits representing an encoded message, determine the number of ways it can be decoded
 * using the mapping: 'A' -> 1, 'B' -> 2, ..., 'Z' -> 26. Leading zeroes are invalid.
 *
 * Example:
 * Input: "226"
 * Output: 3
 * Explanation: "2 2 6" -> B B F, "22 6" -> V F, "2 26" -> B Z
 *
 * LeetCode: https://leetcode.com/problems/decode-ways/
 *
 * Follow-up Questions (FAANG-style):
 * 1. If `*` can be used as any digit 1-9, how would you adapt the computation?
 *    - For each `*`, consider all valid mappings and use DP to aggregate possibilities.
 *    - See: https://leetcode.com/problems/decode-ways-ii/
 * 2. What if the alphabet mapping is changed or generalized?
 *    - Adapt DP logic to respect new mapping rules and valid substring lengths.
 * 3. How to return all possible decoded strings, instead of just the count?
 *    - Use backtracking (DFS) to generate all valid parses.
 * 4. How can you optimize for very long strings (e.g., streaming input)?
 *    - Use rolling DP array/variables to reduce space.
 * 5. Can the problem be solved in O(1) space?
 *    - Yes, use two rolling variables by iterating from the right to left.
 */
public class DecodeWays {

    public static void main(String[] args) {
        System.out.println("Recursive Memoization: " + numDecodeRecursive("226")); // 3
        System.out.println("Iterative DP: " + numDecodingIterative("226"));            // 3
        System.out.println("Iterative DP: " + numDecodingIterative("206"));            // 1
        System.out.println("Iterative DP: " + numDecodingIterative("228"));            // 2
    }

    /**
     * Recursive + Memoization Solution
     *
     * Steps:
     * - Use a helper method that starts from each position, counts decodings for the remainder of the string.
     * - For each step, if current character is not '0', try one-digit and, if valid, two-digit decode.
     * - Use memoization to cache and reuse results for each start index.
     *
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     *
     * @param str Encoded string
     * @return Number of decoding ways
     */
    public static int numDecodeRecursive(String str) {
        if (str == null || str.isEmpty()) return 0;
        Map<Integer, Integer> memo = new HashMap<>();
        return numDecodeRecHelper(0, str, memo);
    }

    private static int numDecodeRecHelper(int idx, String str, Map<Integer, Integer> memo) {
        int length = str.length();
        if (idx == length) return 1;
        if (str.charAt(idx) == '0') return 0;

        if (memo.containsKey(idx)) return memo.get(idx);

        int ways = numDecodeRecHelper(idx + 1, str, memo); // One-digit
        if (idx + 1 < length) {
            int num = Integer.parseInt(str.substring(idx, idx + 2));
            if (num >= 10 && num <= 26) {
                ways += numDecodeRecHelper(idx + 2, str, memo); // Two-digit
            }
        }

        memo.put(idx, ways);
        return ways;
    }

    /**
     * Bottom-up Dynamic Programming Solution (Preferred for Interviews)
     *
     * Steps:
     * - dp[i] = number of ways to decode from index i to end
     * - dp[n] = 1 (empty string base case)
     * - For each i from n-1 down to 0:
     *      - If s[i] == '0', dp[i] = 0 (invalid)
     *      - Else, dp[i] = dp[i+1] (decode one digit)
     *      - If s[i:i+2] is between "10" and "26", add dp[i+2] (decode two digits)
     *
     * Time Complexity: O(N)
     * Space Complexity: O(N)
     *
     * @param str Encoded string
     * @return Number of decoding ways
     */
    public static int numDecodingIterative(String str) {
        if (str == null || str.isEmpty() || str.charAt(0) == '0') return 0;

        int length = str.length();
        int[] dp = new int[length + 1];
        dp[length] = 1;

        for (int i = length - 1; i >= 0; i--) {
            char ch = str.charAt(i);
            if (ch == '0') {
                dp[i] = 0;
            } else {
                dp[i] = dp[i + 1];
                if (i + 1 < length) {
                    int num = Integer.parseInt(str.substring(i, i + 2));
                    if (num >= 10 && num <= 26) {
                        dp[i] += dp[i + 2];
                    }
                }
            }
        }

        return dp[0];
    }
}

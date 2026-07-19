package dynamicprogramming.linearpartition;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Decode Ways
 *
 * A digit string encodes letters using 1 -> A through 26 -> Z. Return how many
 * valid decodings exist, where leading zeroes and standalone zeroes are invalid.
 *
 * Leetcode: https://leetcode.com/problems/decode-ways/ (Medium)
 * Rating:   acceptance 38.3% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Dynamic Programming | Linear partition | One- or two-digit choices
 *
 * Example:
 *   Input:  input = "226"
 *   Output: 3
 *   Why:    it can be split as 2-2-6, 22-6, or 2-26.
 *
 * Follow-ups:
 *   1. What if '*' can represent digits 1 through 9?
 *      Count all valid expansions per position and take results modulo 1e9+7.
 *   2. Return all decoded strings?
 *      Use backtracking and emit each valid one- or two-digit choice.
 *   3. Can space be reduced to O(1)?
 *      Keep only dp[i - 1] and dp[i - 2] while scanning.
 *
 * Related: Decode Ways II (639), Restore IP Addresses (93).
 */
public class DecodeWays {

    public static void main(String[] args) {
        String[] inputs = { "226", "06", "206" };
        int[] expected = { 3, 0, 1 };

        for (int i = 0; i < inputs.length; i++) {
            int got = numDecodingIterative(inputs[i]);
            System.out.printf("input=%s -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
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
        Map<Integer, Integer> dp = new HashMap<>();
        return numDecodeRecHelper(0, str, dp);
    }

    private static int numDecodeRecHelper(int index, String str, Map<Integer, Integer> dp) {
        int length = str.length();
        if (index == length) return 1;
        if (str.charAt(index) == '0') return 0;

        if (dp.containsKey(index)) return dp.get(index);

        int ways = numDecodeRecHelper(index + 1, str, dp); // One digit taken
        if (index + 1 < length) {
            int twoDigits = Integer.parseInt(str.substring(index, index + 2));
            if (twoDigits >= 10 && twoDigits <= 26) {
                ways += numDecodeRecHelper(index + 2, str, dp); // Two digits taken
            }
        }

        dp.put(index, ways);
        return ways;
    }

        /**
     * Intuition: dp[i] means the number of ways to decode the prefix input[0..i-1].
     * The last decoded letter of that prefix must use either the final one digit
     * or the final two digits. If the one-digit piece is valid, every decoding of
     * the prefix before it contributes dp[i - 1]. If the two-digit piece is
     * between 10 and 26, every decoding before those two digits contributes dp[i - 2].
     *
     * Algorithm:
     *   1. Reject null, empty, or leading-zero input.
     *   2. Initialize dp[0] for the empty prefix and dp[1] for the first valid digit.
     *   3. For each prefix length, add ways from valid one-digit and two-digit final pieces.
     *
     * Time:  O(n) - each prefix length is processed once.
     * Space: O(n) - the DP array stores one count per prefix length.
     *
     * @param input encoded digit string
     * @return number of valid decodings
     */
    public static int numDecodingIterative(String input) {
        if (input == null || input.length() == 0 || input.charAt(0) == '0') {
            return 0;
        }

        int length = input.length();
        // dp[i] = number of ways to decode prefix input[0...i-1]
        int[] dp = new int[length + 1];

        // Base case: empty string
        dp[0] = 1;
        // Base case: first character (already checked for '0' above)
        dp[1] = 1;

        for (int i = 2; i <= length; i++) {
            // Option 1: The last piece is a single digit (input[i-1])
            int oneDigit = Integer.parseInt(input.substring(i - 1, i));
            if (oneDigit >= 1 && oneDigit <= 9) {
                dp[i] += dp[i - 1];
            }

            // Option 2: The last piece is two digits (input[i-2...i-1])
            int twoDigits = Integer.parseInt(input.substring(i - 2, i));
            if (twoDigits >= 10 && twoDigits <= 26) {
                dp[i] += dp[i - 2];
            }
        }

        return dp[length];
    }
}

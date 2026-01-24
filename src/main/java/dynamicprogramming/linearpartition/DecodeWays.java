package dynamicprogramming.linearpartition;

import java.util.HashMap;
import java.util.Map;

/**
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
     * Decodes a string of numbers into letters (1='A', 2='B', ..., 26='Z').
     * * STEPS TO SOLVE:
     * 1. Define State: dp[i] is the number of ways to decode the prefix of length 'i'.
     * 2. Base Case: An empty string (length 0) has 1 way to be decoded (doing nothing).
     * 3. Linear Transition:
     * - Check the last 1 digit: If it's valid (1-9), add dp[i-1] to dp[i].
     * - Check the last 2 digits: If they form a valid number (10-26), add dp[i-2] to dp[i].
     * 4. The "Last Piece" Logic: At each step 'i', we only look back at the most recent 
     * possible valid segments (of length 1 and 2).
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

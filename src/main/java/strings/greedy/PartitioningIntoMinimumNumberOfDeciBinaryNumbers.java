package strings.greedy;

/**
 * LeetCode 1689. Partitioning Into Minimum Number Of Deci-Binary Numbers
 *
 * A decimal number is called deci-binary if each of its digits is either 0 or 1 without any leading zeros.
 * For example, 101 and 1100 are deci-binary, while 112 and 3001 are not.
 *
 * Given a string n that represents a positive decimal integer, return the minimum number of positive
 * deci-binary numbers needed to sum up to n.
 *
 * Example 1:
 * Input: n = "32"
 * Output: 3
 * Explanation: 10 + 11 + 11 = 32
 *
 * Example 2:
 * Input: n = "82734"
 * Output: 8
 * Explanation: We need at least 8 deci-binary numbers to sum up to 82734
 *
 * LeetCode Link: https://leetcode.com/problems/partitioning-into-minimum-number-of-deci-binary-numbers/
 *
 * Follow-up Questions:
 * - How would you prove this greedy approach is optimal? (Each position needs max digit iterations)
 * - Can you optimize for very long numbers? (Early termination when max digit 9 is found)
 * - How would you construct actual deci-binary numbers instead of just count? (Build numbers digit by digit)
 * - What if we need to minimize sum of deci-binary numbers instead of count? (Different optimization problem)
 * LeetCode Contest Rating: 1355
 **/
public class PartitioningIntoMinimumNumberOfDeciBinaryNumbers {

    /**
     * Finds minimum number of deci-binary numbers needed to sum to given number.
     *
     * Algorithm:
     * Key insight: minimum count equals the maximum digit in the number because each
     * deci-binary number can contribute at most 1 to any digit position
     *
     * 1. Each deci-binary number can contribute at most 1 to any digit position
     * 2. Position with largest digit d requires exactly d deci-binary numbers
     * 3. All other positions can be satisfied within these d numbers
     * 4. Simply find and return the maximum digit
     *
     * Time Complexity: O(n) where n is length of input string
     * Space Complexity: O(1) - only uses constant extra space
     *
     * @param input String representation of positive decimal integer
     * @return Minimum number of deci-binary numbers needed to sum to n
     */
    public int minPartitions(String input) {
        int maxDigit = 0;

        for (char c : input.toCharArray()) {
            int digit = Character.getNumericValue(c);
            maxDigit = Math.max(maxDigit, digit);

            if (maxDigit == 9) {
                return 9; // Early return for optimal case
            }
        }

        return maxDigit;
    }

    /**
     * Stream-based approach for functional programming style.
     */
    public int minPartitionsStream(String input) {
        return input.chars()
                .map(c -> c - '0')
                .max()
                .orElse(0);
    }
}

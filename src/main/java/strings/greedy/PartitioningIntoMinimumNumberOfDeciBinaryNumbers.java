package strings.greedy;

/**
 * Problem: Partitioning Into Minimum Number Of Deci-Binary Numbers
 *
 * A deci-binary number contains only digits 0 and 1. Given a decimal string,
 * return the fewest positive deci-binary numbers whose digit-wise sum can form
 * that number.
 *
 * Leetcode: https://leetcode.com/problems/partitioning-into-minimum-number-of-deci-binary-numbers/ (Medium)
 * Rating:   acceptance 88.9% (Medium), contest rating 1355
 * Pattern:  Greedy | Digit maximum | Lower bound equals construction
 *
 * Example:
 *   Input:  n = "82734"
 *   Output: 8
 *   Why:    the digit 8 needs eight numbers contributing 1 in that position, and eight numbers can cover every smaller digit.
 *
 * Follow-ups:
 *   1. Construct the actual deci-binary addends?
 *      For round r, place 1 in every position whose digit is at least r.
 *   2. Can scanning stop early?
 *      Yes, digit 9 is the maximum possible answer.
 *   3. What if digits were base b but addends still used 0/1?
 *      The answer remains the maximum digit in that base representation.
 *
 * Related: Additive Number (306), Split a String Into the Max Number of Unique Substrings (1593).
 */
public class PartitioningIntoMinimumNumberOfDeciBinaryNumbers {

    public static void main(String[] args) {
        PartitioningIntoMinimumNumberOfDeciBinaryNumbers solver = new PartitioningIntoMinimumNumberOfDeciBinaryNumbers();
        String[] inputs = {"32", "82734", "111"};
        int[] expected = {3, 8, 1};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.minPartitions(inputs[i]);
            System.out.printf("n=%s -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }


    /**
     * Intuition: one deci-binary addend can contribute at most 1 to any digit
     * position. Therefore a digit d needs at least d addends. The largest digit is
     * also sufficient, because each smaller digit can choose that many of those
     * addends to receive a 1.
     *
     * Algorithm:
     *   1. Scan every digit in the input string.
     *   2. Convert the digit character to its numeric value.
     *   3. Track the maximum digit seen so far.
     *   4. Return 9 immediately if found; otherwise return the maximum digit.
     *
     * Time:  O(n) - one scan of the input digits.
     * Space: O(1) - only the current maximum digit is stored.
     *
     * @param input decimal string representation of a positive integer
     * @return minimum number of deci-binary numbers needed
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

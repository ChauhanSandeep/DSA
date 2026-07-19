package arrays;

import java.util.Arrays;


/**
 * Problem: Sign of the Product of an Array
 *
 * Return the sign of the product of all array elements: 1 for positive, -1 for
 * negative, and 0 if the product is zero. The actual product may overflow, so it
 * should not be computed directly.
 *
 * Leetcode: https://leetcode.com/problems/sign-of-the-product-of-an-array/ (Easy)
 * Rating:   1210 (zerotrac Elo)
 * Pattern:  Array | Sign tracking | Negative parity
 *
 * Example:
 *   Input:  nums = [-1,-2,-3,-4,3,2,1]
 *   Output: 1
 *   Why:    there are four negative numbers, and an even number of negatives
 *           makes the product positive.
 *
 * Follow-ups:
 *   1. Answer product-sign range queries?
 *      Precompute prefix counts of zeros and negatives, then answer each range in O(1).
 *   2. Return the actual product without overflow?
 *      Use BigInteger or a modular product if exact magnitude is not required.
 *   3. Handle floating-point values?
 *      Track signs similarly but define behaviour for NaN, infinity, and negative zero.
 *
 * Related: Product of Array Except Self (238), Maximum Product Subarray (152).
 */
public class SignOfTheProductOfAnArray {

    public static void main(String[] args) {
        SignOfTheProductOfAnArray solver = new SignOfTheProductOfAnArray();

        int[][] inputs = { {-1, -2, -3, -4, 3, 2, 1}, {1, 5, 0, 2, -3}, {-1, 1, -1, 1, -1} };
        int[] expected = { 1, 0, -1 };

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.arraySign(inputs[i]);
            System.out.printf("nums=%s  ->  %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

    /**
     * Intuition: the product's exact magnitude can overflow, but its sign depends only
     * on whether any zero exists and whether the count of negative numbers is odd or
     * even. A zero makes the product zero immediately; otherwise each negative flips
     * the sign.
     *
     * Algorithm:
     *   1. Initialize negativeCount to 0.
     *   2. Scan nums and return 0 immediately if any value is zero.
     *   3. Count negative values.
     *   4. Return 1 for an even negative count and -1 for an odd count.
     *
     * Time:  O(n) - each number is inspected once until a zero or the end.
     * Space: O(1) - only the negative count is stored.
     *
     * @param nums input values
     * @return sign of the full product: 1, -1, or 0
     */
    public int arraySign(int[] nums) {
        int negativeCount = 0;

        for (int num : nums) {
            if (num == 0) {
                return 0; // Product is zero
            }

            if (num < 0) {
                negativeCount++;
            }
        }

        // Even number of negatives → positive, odd → negative
        return (negativeCount % 2 == 0) ? 1 : -1;
    }

    /**
     * Using Stream with reduce operation
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int arraySignReduce(int[] nums) {
        return Arrays.stream(nums)
                .map(Integer::signum)
                .reduce(1, (a, b) -> a * b);
    }
}

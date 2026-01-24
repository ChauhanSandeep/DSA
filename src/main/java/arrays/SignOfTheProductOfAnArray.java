package arrays;

import java.util.Arrays;


/**
 * Sign Of The Product Of An Array
 *
 * Problem: Return sign of the product of all elements in array.
 * Return 1 if positive, -1 if negative, 0 if zero.
 *
 * Example: nums = [-1,-2,-3,-4,3,2,1] -> Output: 1
 * Product is positive due to even number of negative values.
 *
 * LeetCode: https://leetcode.com/problems/sign-of-the-product-of-an-array
 *
 * Follow-up Questions:
 * 1. What if we need to handle very large arrays where overflow is a concern?
 *    Answer: The current approach already handles this by not computing actual product.
 * 2. How would you modify this to return the actual product instead of just sign?
 *    Answer: Use BigInteger for arbitrary precision arithmetic or implement modular arithmetic.
 * 3. What if we need to find the sign of product of subarrays?
 *    Answer: Use prefix arrays to track sign changes and zero positions for range queries.
 * 4. How would you handle floating point numbers in the array?
 *    Answer: Apply same logic but handle special cases like NaN and infinity.
 *
 * Related Problems:
 * - 238. Product of Array Except Self: https://leetcode.com/problems/product-of-array-except-self/
 * - 152. Maximum Product Subarray: https://leetcode.com/problems/maximum-product-subarray/
 * - 1464. Maximum Product of Two Elements in an Array: https://leetcode.com/problems/maximum-product-of-two-elements-in-an-array/
 * LeetCode Contest Rating: 1210
 **/
public class SignOfTheProductOfAnArray {

    /**
     * Determines sign of product by counting negative numbers.
     *
     * Algorithm:
     * 1. If any element is zero, product is zero
     * 2. Count number of negative elements
     * 3. Even count of negatives → positive, odd count → negative
     * 4. This avoids potential integer overflow from actual multiplication
     *
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(1) - only using constant extra space
     *
     * @param nums input array of integers
     * @return 1 if product positive, -1 if negative, 0 if zero
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

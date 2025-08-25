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
 * - What if array contains very large numbers? (Current approach avoids overflow)
 * - How to handle floating point numbers? (Same logic applies)
 * - Can we determine sign without counting? (Use running sign product)
 */
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
     * Alternative using running sign product
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int arraySignRunningProduct(int[] nums) {
        int sign = 1;

        for (int num : nums) {
            if (num == 0) {
                return 0;
            }

            if (num < 0) {
                sign = -sign;
            }
        }

        return sign;
    }

    /**
     * Mathematical approach using Integer.signum()
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int arraySignMath(int[] nums) {
        int result = 1;

        for (int num : nums) {
            result *= Integer.signum(num);

            if (result == 0) {
                return 0; // Early termination if zero found
            }
        }

        return result;
    }

    /**
     * Stream-based functional approach
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int arraySignStream(int[] nums) {
        // Check if any element is zero
        boolean hasZero = Arrays.stream(nums).anyMatch(x -> x == 0);
        if (hasZero) return 0;

        // Count negative numbers
        long negativeCount = Arrays.stream(nums).filter(x -> x < 0).count();

        return (negativeCount % 2 == 0) ? 1 : -1;
    }

    /**
     * Using reduce operation
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int arraySignReduce(int[] nums) {
        return Arrays.stream(nums)
                .map(Integer::signum)
                .reduce(1, (a, b) -> a * b);
    }

    /**
     * Verbose approach showing all cases explicitly
     * Time Complexity: O(n), Space Complexity: O(1)
     */
    public int arraySignVerbose(int[] nums) {
        boolean hasZero = false;
        int negativeCount = 0;

        for (int num : nums) {
            if (num == 0) {
                hasZero = true;
            } else if (num < 0) {
                negativeCount++;
            }
            // Positive numbers don't affect the sign calculation
        }

        if (hasZero) {
            return 0;
        }

        if (negativeCount % 2 == 0) {
            return 1; // Even negatives → positive
        } else {
            return -1; // Odd negatives → negative
        }
    }

    /**
     * Early termination version for better average performance
     * Time Complexity: O(n) worst case, O(k) average where k is position of first zero
     * Space Complexity: O(1)
     */
    public int arraySignEarlyTermination(int[] nums) {
        int negativeCount = 0;

        for (int num : nums) {
            if (num == 0) {
                return 0; // Immediate return
            }

            if (num < 0) {
                negativeCount++;
            }
        }

        return (negativeCount % 2 == 0) ? 1 : -1;
    }

    /**
     * Helper method to validate result by computing actual product sign
     * (Only for testing with small arrays to avoid overflow)
     */
    public int validateWithActualProduct(int[] nums) {
        long product = 1;

        for (int num : nums) {
            product *= num;

            // Prevent overflow by checking sign early
            if (product == 0) return 0;
            if (product > Integer.MAX_VALUE || product < Integer.MIN_VALUE) {
                // For very large products, just return current sign
                return product > 0 ? 1 : -1;
            }
        }

        return product > 0 ? 1 : (product < 0 ? -1 : 0);
    }
}

package arrays;

import java.util.Arrays;

/**
 * Given an integer array nums, return an array such that answer[i] is the product of all elements except nums[i].
 * You must write an algorithm that runs in O(n) time and without using the division operation.
 *
 * Leetcode link: https://leetcode.com/problems/product-of-array-except-self/description/
 */
public class ProductExceptSelf {
    public static void main(String[] args) {
        int[] nums = {1,2,3,4};
        System.out.println("Optimized Output: " + Arrays.toString(productExceptSelf(nums)));
    }

    /**
     * Optimized Approach (No Extra Space, O(n) Time Complexity)
     * - Uses a single output array (`result[]`) for storing left products.
     * - Then iterates backward, multiplying with right-side values.
     * - **Space Complexity: O(1) (excluding output array)**
     */
    public static int[] productExceptSelf(int[] nums) {
        int len = nums.length;
        int[] result = new int[len];

        // Compute left products
        result[0] = 1;
        for (int i = 1; i < len; i++) {
            result[i] = result[i - 1] * nums[i - 1];
        }

        // Multiply by right products in reverse
        int rightProduct = 1;
        for (int i = len - 1; i >= 0; i--) {
            result[i] *= rightProduct;
            rightProduct *= nums[i]; // Update right product
        }

        return result;
    }
}

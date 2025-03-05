package DynamicProgramming;

/**
 * Problem: Find the contiguous subarray with the maximum product.
 * 
 * Approach:
 * - Maintain two variables `localMax` and `localMin` to track max/min product ending at the current index.
 * - If `nums[i]` is negative, `localMax` and `localMin` swap values (since multiplying by a negative flips sign).
 * - Update the global maximum product.
 * 
 * Time Complexity: O(N) (single pass through the array)
 * Space Complexity: O(1) (constant space used)
 * 
 * Link: https://leetcode.com/problems/maximum-product-subarray/
 */
public class MaxProductSubarray {
    
    public static void main(String[] args) {
        MaxProductSubarray maxProduct = new MaxProductSubarray();
        
        int[] nums1 = {2, 3, -2, 4};
        System.out.println(maxProduct.maxProduct(nums1)); // Output: 6

        int[] nums2 = {2, -5, 3, 1, -4, 0, -10, 2, 8};
        System.out.println(maxProduct.maxProduct(nums2)); // Output: 80

        int[] nums3 = {-1, -2, -3, 0};
        System.out.println(maxProduct.maxProduct(nums3)); // Output: 6
    }

    public int maxProduct(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int globalMax = nums[0];
        int localMax = nums[0];
        int localMin = nums[0];

        for (int i = 1; i < nums.length; i++) {
            int curr = nums[i];

            // If current number is negative, swap localMax & localMin
            if (curr < 0) {
                int temp = localMax;
                localMax = localMin;
                localMin = temp;
            }

            // Compute local max/min products ending at index i
            localMax = Math.max(curr, localMax * curr);
            localMin = Math.min(curr, localMin * curr);

            // Update global max product
            globalMax = Math.max(globalMax, localMax);
        }

        return globalMax;
    }
}

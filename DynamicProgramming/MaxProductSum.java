package DynamicProgramming;

/**
 * Find subarray (continous) with max product
 * https://leetcode.com/problems/maximum-product-subarray/solution/
 */
public class MaxProductSum {

    public static void main(String[] args) {
        MaxProductSum maxProductSum = new MaxProductSum();
        int[] nums = {2,3,-2,4};
        System.out.println(maxProductSum.maxProduct(nums));

        nums = new int[]{2, -5, 3, 1, -4, 0, -10, 2, 8};
        System.out.println(maxProductSum.maxProduct(nums));
    }

    public int maxProduct(int[] nums) {
        int max = nums[0];
        int currMax = nums[0];
        int currMin = nums[0];

        for(int i=1; i<nums.length; i++) {
            int temp = Math.max(nums[i], Math.max(currMax * nums[i], currMin * nums[i]));
            currMin = Math.min(nums[i], Math.min(currMax * nums[i], currMin * nums[i]));
            currMax = temp;

            // System.out.println(currMax + "   " + currMin);

            max = Math.max(max, currMax);
        }
        return max;
    }
}

package Array;

import java.util.Arrays;

/**
 * Given an array of integers containing elements from -2^31 to 2^31 -1.
 * Find the first positive number which is missing in the array.
 *
 * https://leetcode.com/problems/first-missing-positive/
 */
public class FirstMissingPositive {

    public static void main(String[] args) {
        int[] arr = {1, 2, 0};
        int result = new FirstMissingPositive().firstMissingPositive(arr);
        System.out.println("First missing positive number in array is " + result);
    }

    public int firstMissingPositive(int[] nums) {
        // Step 1: Check if 1 exists in the array
        boolean containsOne = false;
        for (int num : nums) {
            if (num == 1) {
                containsOne = true;
                break;
            }
        }
        if (!containsOne) return 1;

        // Step 2: Replace elements <= 0 or elements > size with 1
        // The missing element will always be in the range 1 to size
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] <= 0 || nums[i] > nums.length) {
                nums[i] = 1;
            }
        }

        // Step 3: Mark the index of found numbers with a negative sign
        for (int i = 0; i < nums.length; i++) {
            int curr = Math.abs(nums[i]);
            if (curr == nums.length) {
                nums[0] = -Math.abs(nums[0]);
            } else {
                nums[curr] = -Math.abs(nums[curr]);
            }
        }
        System.out.println(Arrays.toString(nums));

        // Step 4: Find the first positive number
        // This index was never marked, so this element is missing
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > 0) {
                return i;
            }
        }
        if (nums[0] > 0) return nums.length;
        return nums.length + 1;
    }
}

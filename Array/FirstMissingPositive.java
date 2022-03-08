package Array;

import java.util.Arrays;

/**
 * Given an array of integers containing element from -2^31 to 2^31 -1.
 * Find the first positive number which is missing in array.
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
        // Step1. check if 1 exists in array
        boolean containsOne = false;
        for (int num : nums) {
            if (num == 1) {
                containsOne = true;
                break;
            }
        }
        if(!containsOne) return 1;

        //Step2. If 1 exists, then replace elements <= 0 or element > size with 1
        // Missing element will always be in range 1 to size
        for(int i= 0; i< nums.length; i++) {
            if(nums[i] <= 0 || nums[i] > nums.length){
                nums[i] = 1;
            }
        }

        // Step3. Iterate over array and mark index of found number with - sign
        for(int i= 0; i<nums.length; i++) {
            int curr = Math.abs(nums[i]);
            if(curr == nums.length) {
                nums[0] = -Math.abs(nums[0]);
            } else {
                nums[curr] = - Math.abs(nums[curr]);
            }
        }
        System.out.println(Arrays.toString(nums));

        // Step4. Check the first number with positive sign.
        // (This index was never marked hence this element is missing)
        for(int i=1; i<nums.length; i++) {
            if(nums[i] > 0)
                return i;
        }
        if(nums[0] > 0) return nums.length;
        return nums.length + 1;
    }
}

package Array;

import java.util.Arrays;

/**
 * https://leetcode.com/problems/first-missing-positive/
 */
public class FirstMissingPositive {

    public static void main(String[] args) {
        int[] arr = {1, 2, 0};
        int result = new FirstMissingPositive().firstMissingPositive(arr);
        System.out.println("First missing positive number in array is " + result);
    }

    public int firstMissingPositive(int[] nums) {
        boolean containsOne = false;
        for (int num : nums) {
            if (num == 1) {
                containsOne = true;
                break;
            }
        }
        if(!containsOne) return 1;

        for(int i= 0; i< nums.length; i++) {
            if(nums[i] <= 0 || nums[i] > nums.length){
                nums[i] = 1;
            }
        }

        for(int i= 0; i<nums.length; i++) {
            int curr = Math.abs(nums[i]);

            if(curr == nums.length) {
                nums[0] = -Math.abs(nums[0]);
            } else {
                nums[curr] = - Math.abs(nums[curr]);
            }

        }
        System.out.println(Arrays.toString(nums));

        for(int i=1; i<nums.length; i++) {
            if(nums[i] > 0)
                return i;
        }
        if(nums[0] > 0) return nums.length;
        return nums.length + 1;
    }
}

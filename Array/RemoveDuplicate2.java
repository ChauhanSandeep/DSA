package Array;

import java.util.Arrays;

/**
 * Given an integer array nums sorted in non-decreasing order,
 * remove some duplicates in-place such that each unique element appears at most twice.
 * The relative order of the elements should be kept the same.
 */
public class RemoveDuplicate2 {
    public static void main(String[] args) {
        int[] nums = {0,0,1,1,1,1,2,3,3};
        int k = new RemoveDuplicate2().removeDuplicates(nums);
        System.out.println(k);
        System.out.println(Arrays.toString(nums));
    }

    public int removeDuplicates(int[] nums) {
        int count = 1;
        int k = 1;

        for(int i=1; i<nums.length; i++) {
            if(nums[i] == nums[i-1]) count++;
            else count = 1;

            if(count <= 2) nums[k++] = nums[i];
        }
        return k;
    }


}

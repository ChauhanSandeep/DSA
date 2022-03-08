package Array;

/**
 * Find duplicate number in array containing elements from 1 - n(duplicate occures 1 or more times)
 * You must solve the problem without modifying the array nums and uses only constant extra space.
 *
 * https://leetcode.com/problems/find-the-duplicate-number/
 */
public class DuplicateNumber {

    public static void main(String[] args) {
        int[] nums = {1,3,4,2,2};
        int result = new DuplicateNumber().findDuplicate(nums);
        System.out.println(result);
    }

    public int findDuplicate(int[] nums) {
        // This problem is like finding starting of loop in cyclic linked list
        int slow = nums[0];
        int fast = nums[0];

        // find collision in loop of numbers
        do {
            slow = nums[slow];
            fast = nums[nums[fast]];
        }while(slow != fast);

        // find the starting of loop
        fast = nums[0];
        while(slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }
        return slow;
    }

}

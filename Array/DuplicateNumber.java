package Array;

/**
 * Find duplicate number in array containing elements from 1 - n(duplicate occurs 1 or more times)
 * You must solve the problem without modifying the array nums and use only constant extra space.
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
        // This problem is like finding the start of a loop in a cyclic linked list
        int slow = nums[0];
        int fast = nums[0];

        // Find collision point in the loop of numbers
        do {
            slow = nums[slow];
            fast = nums[nums[fast]];
        } while (slow != fast);

        // Find the starting point of the loop
        fast = nums[0];
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }
        return slow;
    }
}

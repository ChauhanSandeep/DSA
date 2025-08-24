package array;

/**
 * Problem: Find the Duplicate Number
 *
 * You are given an array `nums` of length n + 1 where each number is in the range [1, n].
 * There is exactly one duplicate number (which could appear more than once) and few number from 1 to n are missing.
 *
 * You must:
 * - Not modify the array (read-only)
 * - Use only constant extra space
 * - Have a runtime complexity less than O(n²)
 *
 * LeetCode Link:
 * https://leetcode.com/problems/find-the-duplicate-number/
 *
 * Example:
 * Input: nums = [1, 3, 4, 2, 2]
 * Output: 2
 *
 * Follow-Up Questions:
 * - Can you do this using bit manipulation?
 * - Can you find the duplicate if there are multiple duplicates? (Not allowed in original constraints)
 * - What if modifying the array was allowed? (Negative marking or cyclic sort variants work)
 */
public class DuplicateNumber {

  public static void main(String[] args) {
    int[] nums = {1, 3, 4, 2, 2};
    int result = new DuplicateNumber().findDuplicate(nums);
    System.out.println("Duplicate number is: " + result);
  }

  /**
   * Floyd’s Tortoise and Hare (Cycle Detection) Algorithm.
   * Treat the array as a linked list where each index points to nums[i],
   * and find the start of the cycle, which is the duplicate number.
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1)
   *
   * Steps:
   * 1. Initialize two pointers: slow and fast.
   * 2. Move slow by 1 step and fast by 2 steps to detect a cycle.
   * 3. Reset one pointer to the beginning and move both one step at a time to find the entrance to the cycle.
   *
   * @param nums The input array (read-only)
   * @return The duplicate number
   */
  public int findDuplicate(int[] nums) {
      if (nums == null || nums.length <= 1) {
          return -1;
      }

    int slow = nums[0];
    int fast = nums[0];

    // Phase 1: Detect intersection point in cycle
    do {
      slow = nums[slow];
      fast = nums[nums[fast]];
    } while (slow != fast);

    // Phase 2: Find entrance to the cycle — the duplicate
    fast = nums[0];
    while (slow != fast) {
      slow = nums[slow];
      fast = nums[fast];
    }

    return slow;
  }
}
package arrays.twopointers;

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
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class DuplicateNumber {

  public static void main(String[] args) {
    int[] nums = {1, 3, 4, 2, 2};
    int result = new DuplicateNumber().findDuplicate(nums);
    System.out.println("Duplicate number is: " + result);
  }

  /**
   * Using mathematical formula to find duplicate
   * 
   * Algorithm:
   * 1. Calculate the expected sum of numbers from 1 to n using the formula n*(n+1)/2
   * 2. Calculate the actual sum of the elements in the array
   * 3. The difference between the actual sum and expected sum gives the duplicate number
   * 
   * Note : This approach works only if there is exactly one duplicate and no missing numbers.
   * Time Complexity: O(n)
   * Space Complexity: O(1)
   * @param nums
   * @return
   */
  public int findDuplicateUsingSumFormula(int[] nums) {
    int maxNum = nums.length - 1; // Since nums contains n + 1 elements
    int expectedSum = maxNum * (maxNum + 1) / 2;
    int actualSum = 0;

    for (int num : nums) {
      actualSum += num;
    }

    return actualSum - expectedSum;
  }

  /**
   * Alternative Solution (Negation - MODIFIES ARRAY)
   *
   * Negation Approach - VIOLATES the "do not modify array" constraint.
   * This approach is O(n) time and O(1) space but modifies the input.
   * Useful when array modification is allowed (like in LeetCode 442).
   *
   * Algorithm:
   * 1. Iterate through each number in the array.
   * 2. For each number, calculate the index it points to (value - 1 for 0-based index).
   * 3. Negate the value at that index to mark it as visited.
   * 4. If you encounter a negative value at that index, it means the index has been visited before → duplicate found.
   * 5. Return the duplicate number.
   *
   * Time Complexity: O(n)
   * Space Complexity: O(1)
   * @param nums the input array (will be modified)
   * @return the duplicate number
   */
  public int findDuplicateUsingNegation(int[] nums) {
    // We assume nums contains integers in the range [1, n]
    // where n = nums.length - 1 (pigeonhole principle ensures a duplicate exists).

    for (int i = 0; i < nums.length; i++) {
      // Convert value to index (shift by -1 for 0-based indexing).
      int indexToVisit = Math.abs(nums[i]) - 1;

      // If the value at this index is already negative,
      // it means we've visited this index before → duplicate found.
      if (nums[indexToVisit] < 0) {
        return Math.abs(nums[i]);
      }

      // Mark this index as visited by negating its value.
      nums[indexToVisit] = -nums[indexToVisit];
    }

    // By problem constraints, we should never reach here.
    return -1;
  }

  /**
   * Optimized Solution - Cycle Detection (Floyd's Tortoise and Hare)
   *
   * Treat the array as a linked list where each index points to nums[i],
   * and find the start of the cycle, which is the duplicate number.
   * 
   * Steps:
   * 1. Initialize two pointers, slow and fast, both starting at the first element
   * 2. Move slow by one step and fast by two steps until they meet (detect cycle)
   * 3. Reset one pointer to the start and move both one step at a time until they meet again
   * 4. The meeting point is the duplicate number
   * 
   * Why it works with formula:
   * Let:
   * - x = distance from start to cycle entrance
   * - y = distance from cycle entrance to meeting point
   * - z = distance from meeting point back to cycle entrance
   * At meeting point:
   * - Slow has traveled: x + y
   * - Fast has traveled: x + y + n*(y + z) for some n ≥ 1
   * Since fast moves twice as fast:
   * 2 * (x + y) = x + y + n*(y + z)
   * => x + y = n*(y + z)
   * => x = (n - 1)*(y + z) + z
   * This means if we start one pointer at the beginning and one at the meeting point,
   * and move both one step at a time, they will meet at the cycle entrance (duplicate number).
   * 
   * Dry run:
   *       [1, 3, 4, 2, 2]
   * Index: 0  1  2  3  4
   * Value: 1->3->2->4->2 (cycle starts at index 2)
   * One each iteration:
   * Iteration 1: slow=3, fast=2
   * Iteration 2: slow=2, fast=2 (intersection point)
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
package arrays.twopointers;

import java.util.Arrays;

/**
 * Problem: Find the Duplicate Number
 *
 * Given n + 1 integers where every value is in [1, n], return the repeated
 * value. The primary solution treats each value as a next pointer and finds the
 * entrance of the cycle created by the duplicate.
 *
 * Leetcode: https://leetcode.com/problems/find-the-duplicate-number/ (Medium)
 * Rating:   acceptance 64.6% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array | Two pointers | Floyd cycle detection
 *
 * Example:
 *   Input:  nums = [1,3,4,2,2]
 *   Output: 2
 *   Why:    value 2 is reached from more than one index, creating the cycle entrance.
 *
 * Follow-ups:
 *   1. What if modifying the array is allowed?
 *      Use negative marking or cyclic sort, but both violate the read-only constraint.
 *   2. What if multiple values are duplicated?
 *      Floyd's single-cycle model no longer identifies every duplicate; use counting or marking.
 *   3. Can this be solved with binary search on values?
 *      Yes, count values <= mid and apply the pigeonhole principle in O(n log n).
 *
 * Related: Linked List Cycle II (142), Find All Duplicates in an Array (442).
 */
public class DuplicateNumber {

public static void main(String[] args) {
  DuplicateNumber solver = new DuplicateNumber();
  int[][] inputs = { {1, 3, 4, 2, 2}, {3, 1, 3, 4, 2} };
  int[] expected = { 2, 3 };

  for (int i = 0; i < inputs.length; i++) {
    int got = solver.findDuplicate(inputs[i]);
    System.out.printf("nums=%s -> %d  expected=%d%n",
        Arrays.toString(inputs[i]), got, expected[i]);
  }
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
 * Intuition: use each value as a pointer to the next index. Because there are
 * n + 1 positions but only n values, two paths eventually point into the same
 * cycle. Floyd's first phase finds an intersection inside that cycle; resetting
 * fast to nums[0] makes slow and fast meet at the duplicate value.
 *
 * Algorithm:
 *   1. Start slow and fast at nums[0].
 *   2. Move slow one hop and fast two hops until they meet inside the cycle.
 *   3. Reset fast to nums[0].
 *   4. Move both one hop at a time; their meeting value is the duplicate.
 *
 * Time:  O(n) - the two Floyd phases traverse a linear number of hops.
 * Space: O(1) - only two pointer values are stored.
 *
 * @param nums read-only array containing values in [1, n]
 * @return duplicate value, or -1 for the guard-clause invalid input case
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
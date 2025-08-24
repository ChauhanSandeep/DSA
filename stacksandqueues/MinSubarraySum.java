package stacksandqueues;

import java.util.ArrayDeque;
import java.util.Deque;


/**
 * Problem: Sum of Subarray Minimums
 * -----------------------------------------
 * Given an array of integers, return the sum of the minimum value of all possible contiguous subarrays.
 *
 * Example:
 * Input: arr = [3,1,2,4]
 * Output: 17
 * Explanation:
 * Subarrays = [3], [1], [2], [4], [3,1], [1,2], [2,4], [3,1,2], [1,2,4], [3,1,2,4]
 * Minimums =   3,   1,   2,   4,   1,     1,      2,      1,       1,        1
 * Total = 17
 *
 * LeetCode Link: https://leetcode.com/problems/sum-of-subarray-minimums/
 *
 * 🔍 Follow-up Questions:
 * 1. **Can we find the sum of maximums in subarrays using similar approach?**
 *    - Yes, just reverse the comparison logic (use monotonic decreasing stack).
 *    - Related: https://leetcode.com/problems/sum-of-subarray-ranges/
 * 2. **Can we do it without stack?**
 *    - Naively yes, but that takes O(N²) time. Stack is optimal O(N).
 * 3. **Can we find how many subarrays each element is the minimum for?**
 *    - Yes. That's the core of this approach using left and right spans.
 */
public class MinSubarraySum {

  private static final int MODULO = (int) 1e9 + 7;

  public static void main(String[] args) {
    int[] nums = {3, 5, 4, 12, 8, 10, 11, 4};
    System.out.println("Sum of Subarray Minimums: " + new MinSubarraySum().sumSubarrayMins(nums));
  }

  /**
   * Computes the sum of the minimum values across all subarrays of the input array.
   *
   * Approach:
   * - Use a monotonic increasing stack to determine, for each element:
   *   - The previous smaller element index (left boundary)
   *   - The next smaller element index (right boundary)
   * - For an element at index `i`, the total number of subarrays where it is the minimum is:
   *      (i - previousSmallerIndex) * (nextSmallerIndex - i)
   * - Its total contribution is:
   *      nums[i] * leftCount * rightCount
   * - Sum all such contributions modulo 1e9+7.
   *
   * Time Complexity: O(N) — each element is pushed and popped once from the stack.
   * Space Complexity: O(N) — space used by the stack and intermediate variables.
   *
   * @param nums the input array
   * @return sum of subarray minimums modulo 1e9+7
   */
  public int sumSubarrayMins(int[] nums) {
      if (nums == null || nums.length == 0) {
          return 0;
      }

    int length = nums.length;
    long totalSum = 0;
    Deque<Integer> stack = new ArrayDeque<>();
    stack.push(-1);  // Sentinel value to handle left boundaries

    for (int i = 0; i < length; i++) {
      // Maintain a monotonic increasing stack
      while (stack.peek() != -1 && nums[stack.peek()] > nums[i]) {
        int mid = stack.pop();                // Current minimum
        int left = stack.peek();              // Previous smaller element's index
        int right = i;                        // Next smaller element's index
        totalSum = (totalSum + computeContribution(nums[mid], left, mid, right)) % MODULO;
      }
      stack.push(i);
    }

    // Process remaining elements in stack for which no smaller element on the right
    while (stack.peek() != -1) {
      int mid = stack.pop();
      int left = stack.peek();
      int right = length;
      totalSum = (totalSum + computeContribution(nums[mid], left, mid, right)) % MODULO;
    }

    return (int) totalSum;
  }

    /**
     * Helper method to compute the contribution of an element at index `midIndex` being the minimum
     * in all subarrays between indices `leftIndex` and `rightIndex`.
     *
     * Formula: value * (number of subarrays where it is minimum)
     *  ie. value * (midIndex - leftIndex) * (rightIndex - midIndex)
     */
    private long computeContribution(int value, int leftIndex, int midIndex, int rightIndex) {
        int leftCombinations = midIndex - leftIndex;
        int rightCombinations = rightIndex - midIndex;
        int totalCombinations = leftCombinations * rightCombinations;
        return (long) totalCombinations * value;
    }
}

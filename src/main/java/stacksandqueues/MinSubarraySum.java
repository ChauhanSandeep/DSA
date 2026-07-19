package stacksandqueues;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Arrays;


/**
 * Problem: Sum of Subarray Minimums
 *
 * For every contiguous subarray, take its minimum value and return the sum of
 * all those minimums modulo 1,000,000,007. The key is counting how many
 * subarrays choose each index as their representative minimum.
 *
 * Leetcode: https://leetcode.com/problems/sum-of-subarray-minimums/ (Medium)
 * Rating:   zerotrac 1976
 * Pattern:  Monotonic stack | Contribution counting | Previous/next smaller
 *
 * Example:
 *   Input:  arr = [3,1,2,4]
 *   Output: 17
 *   Why:    the minimums across all ten subarrays add up to 17, with 1 contributing most of them.
 *
 * Follow-ups:
 *   1. Sum subarray maximums instead?
 *      Flip the comparisons and use a monotonic decreasing stack.
 *   2. Sum subarray ranges, max minus min?
 *      Compute sum of maximums minus sum of minimums with two contribution passes.
 *   3. Need stable ownership when equal values repeat?
 *      Use strict comparison on one side and non-strict on the other to avoid double counting.
 *   4. Values arrive online and queries ask the current total?
 *      Maintain a stack of (value,count) groups and the running sum of suffix minimums.
 *
 * Related: Sum of Subarray Ranges (2104), Largest Rectangle in Histogram (84).
 */
public class MinSubarraySum {

  private static final int MODULO = (int) 1e9 + 7;
  public static void main(String[] args) {
    MinSubarraySum solver = new MinSubarraySum();
    int[][] inputs = { {}, {3, 1, 2, 4}, {11, 81, 94, 43, 3}, {2, 2, 2} };
    int[] expected = {0, 17, 444, 12};
    for (int i = 0; i < inputs.length; i++) {
      int got = solver.sumSubarrayMins(inputs[i]);
      System.out.printf("arr=%s -> %d  expected=%d%n", Arrays.toString(inputs[i]), got, expected[i]);
    }
  }

    /**
   * Intuition: count each value by how many subarrays choose it as the minimum.
   * When an index is popped from the increasing stack, the new top is its left
   * smaller boundary and the current index is its right smaller boundary, so its
   * contribution is value * left choices * right choices.
   *
   * Algorithm:
   *   1. Use an increasing stack of indices with a -1 sentinel.
   *   2. Pop while the stack top value is greater than the current value.
   *   3. Add each popped index's contribution from its left and right boundaries.
   *   4. Flush remaining stack entries using nums.length as the right boundary.
   *
   * Time:  O(n) - every index is pushed and popped at most once.
   * Space: O(n) - the stack can hold all indices.
   *
   * @param nums input array
   * @return sum of all subarray minimums modulo 1,000,000,007
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
        int mid = stack.pop();                // Current local maxima index
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

    /** Computes one value's contribution from its left and right boundaries. */
    private long computeContribution(int value, int leftIndex, int midIndex, int rightIndex) {
        int leftCombinations = midIndex - leftIndex;
        int rightCombinations = rightIndex - midIndex;
        int totalCombinations = leftCombinations * rightCombinations;
        return (long) totalCombinations * value;
    }
}

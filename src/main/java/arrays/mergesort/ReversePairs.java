package arrays.mergesort;



import java.util.Arrays;/**
 * Problem: Reverse Pairs
 *
 * Given an integer array, count pairs (i, j) such that i < j and nums[i] is
 * greater than twice nums[j]. The pair relation depends on both order and value,
 * so simply sorting the whole array would lose the original left-before-right rule.
 *
 * Leetcode: https://leetcode.com/problems/reverse-pairs/
 * Rating:   acceptance 34.8% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Arrays | Merge sort | Count cross pairs before merging
 *
 * Example:
 *   Input:  [2,4,3,5,1]
 *   Output: 3
 *   Why:    the valid pairs are (4,1), (3,1), and (5,1), each with the left value
 *           greater than twice the right value.
 *
 * Follow-ups:
 *   1. What if the condition is nums[i] > k * nums[j]?
 *      Keep the same merge-sort count and replace 2 with k using long arithmetic.
 *   2. What if values are updated between queries?
 *      Use a segment tree or binary indexed tree with coordinate compression.
 *   3. What if you need to list the pairs, not just count them?
 *      The output can be quadratic, so emit pairs during the cross-count scan.
 *
 * Related: Count of Smaller Numbers After Self (315), Inversion Count.
 */
public class ReversePairs {
  public int reversePairs(int[] nums) {
    if (nums == null || nums.length < 2) {
      return 0;
    }

    int[] buffer = new int[nums.length];
    return (int) mergeSortAndCount(nums, 0, nums.length - 1, buffer);
  }

  private long mergeSortAndCount(int[] nums, int left, int right, int[] buffer) {
    if (left >= right) {
      return 0;
    }

    int mid = left + (right - left) / 2;
    long leftCount = mergeSortAndCount(nums, left, mid, buffer);
    long rightCount = mergeSortAndCount(nums, mid + 1, right, buffer);
    long crossCount = countCrossPairs(nums, left, mid, right);

    merge(nums, left, mid, right, buffer);
    return leftCount + rightCount + crossCount;
  }

  private long countCrossPairs(int[] nums, int left, int mid, int right) {
    long reversePairs = 0;
    int rightPointer = mid + 1;

    for (int leftPointer = left; leftPointer <= mid; leftPointer++) {
      while (rightPointer <= right
          && (long) nums[leftPointer] > 2L * nums[rightPointer]) {
        rightPointer++;
      }
      reversePairs += rightPointer - (mid + 1);
    }

    return reversePairs;
  }

  private void merge(int[] nums, int left, int mid, int right, int[] buffer) {
    int i = left;
    int j = mid + 1;
    int k = left;

    while (i <= mid && j <= right) {
      if (nums[i] <= nums[j]) {
        buffer[k++] = nums[i++];
      } else {
        buffer[k++] = nums[j++];
      }
    }

    while (i <= mid) {
      buffer[k++] = nums[i++];
    }

    while (j <= right) {
      buffer[k++] = nums[j++];
    }

    for (int index = left; index <= right; index++) {
      nums[index] = buffer[index];
    }
  }
    public static void main(String[] args) {
        ReversePairs solver = new ReversePairs();
        int[][] inputs = {{1, 3, 2, 3, 1}, {2, 4, 3, 5, 1}, {1, 2, 3, 4}};
        int[] expected = {2, 3, 0};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.reversePairs(inputs[i].clone());
            System.out.printf("nums=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }
}

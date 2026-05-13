package arrays.mergesort;

/**
 * 493. Reverse Pairs
 *
 * Given an integer array nums, return the number of reverse pairs in the array.
 *
 * A reverse pair is a pair (i, j) where:
 * - 0 <= i < j < nums.length
 * - nums[i] > 2 * nums[j]
 *
 * Example 2:
 * Input: nums = [2,4,3,5,1]
 * Output: 3
 *
 * LeetCode: https://leetcode.com/problems/reverse-pairs/
 *
 * Approach:
 * Use merge sort. When both halves are sorted, we can count cross-half reverse pairs
 * in linear time using a two-pointer scan.
 *
 * Time Complexity: O(n log n)
 * Space Complexity: O(n)
 *
 * @author Sandeep Chauhan
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

    // Example 1 -> 2
    System.out.println(solver.reversePairs(new int[]{1, 3, 2, 3, 1}));

    // Example 2 -> 3
    System.out.println(solver.reversePairs(new int[]{2, 4, 3, 5, 1}));

    // No reverse pairs -> 0
    System.out.println(solver.reversePairs(new int[]{1, 2, 3, 4}));
  }
}

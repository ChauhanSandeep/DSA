package arrays.twopointers;

import java.util.Arrays;

/**
 * Problem: Median of Two Sorted Arrays
 *
 * Given two sorted arrays, return the median of their combined sorted order.
 * The optimal method finds a valid left/right partition without fully merging
 * the arrays.
 *
 * Leetcode: https://leetcode.com/problems/median-of-two-sorted-arrays/ (Hard)
 * Rating:   acceptance 47.1% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Binary search | Partition | Two sorted arrays
 *
 * Example:
 *   Input:  nums1 = [1,2], nums2 = [3,4]
 *   Output: 2.5
 *   Why:    combined order is [1,2,3,4], so the median is (2 + 3) / 2.
 *
 * Follow-ups:
 *   1. Find the kth element instead of the median?
 *      Use the same partition idea or recursively discard k / 2 elements.
 *   2. Extend to many sorted arrays?
 *      Use a min heap for merging or binary search on value counts.
 *   3. What if one array is streamed?
 *      Maintain two heaps, or buffer enough data to support partitioning.
 *
 * Related: Kth Smallest Element in a Sorted Matrix (378), Merge k Sorted Lists (23).
 */
public class MedianArrays {

public static void main(String[] args) {
  int[][] nums1Cases = { {1, 2}, {1, 3} };
  int[][] nums2Cases = { {3, 4}, {2} };
  double[] expected = { 2.5, 2.0 };

  for (int i = 0; i < nums1Cases.length; i++) {
    double got = findMedianBinarySearch(nums1Cases[i], nums2Cases[i]);
    System.out.printf("nums1=%s nums2=%s -> %.1f  expected=%.1f%n",
        Arrays.toString(nums1Cases[i]), Arrays.toString(nums2Cases[i]), got, expected[i]);
  }
}

  /**
   * Approach 1: Two-pointer Merge Technique
   * Simulates merging of two sorted arrays until the median is found.
   *
   * Steps:
   * 1. Use two pointers to merge up to (n1 + n2)/2 elements.
   * 2. Track current and previous elements to compute median.
   *
   * Time Complexity: O(m + n)
   * Space Complexity: O(1)
   *
   * @param nums1 First sorted array
   * @param nums2 Second sorted array
   * @return Median of the merged array
   */
  public static double findMedianByMerging(int[] nums1, int[] nums2) {
      if ((nums1 == null || nums1.length == 0) && (nums2 == null || nums2.length == 0)) {
          return 0.0;
      }

    int len1 = nums1.length, len2 = nums2.length;
    int totalSize = len1 + len2;
    int mid = totalSize / 2;

    int pointer1 = 0;
    int pointer2 = 0;
    int count = 0;
    double prevNum = 0;
    double currNum = 0;

    while (count <= mid) {
      prevNum = currNum;

      if (pointer1 < len1 && (pointer2 >= len2 || nums1[pointer1] < nums2[pointer2])) {
        currNum = nums1[pointer1++];
      } else {
        currNum = nums2[pointer2++];
      }

      count++;
    }

    return (totalSize % 2 == 0) ? (prevNum + currNum) / 2.0 : currNum;
  }

  /**
 * Intuition: a median partition puts half the combined elements on the left and
 * half on the right. Binary search the cut in the smaller array; the other cut
 * is forced. When both left maxima are <= both right minima, the median is at
 * the boundary of those partitions.
 *
 * Algorithm:
 *   1. Ensure nums1 is the shorter array.
 *   2. Binary search cut1, deriving cut2 from the combined midpoint.
 *   3. Use sentinel min/max values when a cut is at an array boundary.
 *   4. Move the search toward a valid partition, then compute odd/even median.
 *
 * Time:  O(log(min(m, n))) - binary search runs on the smaller array.
 * Space: O(1) - only partition indices and boundary values are stored.
 *
 * @param nums1 first sorted array
 * @param nums2 second sorted array
 * @return median of the combined sorted values
 */
  public static double findMedianBinarySearch(int[] nums1, int[] nums2) {
    // Always binary search on the smaller array
      if (nums1.length > nums2.length) {
          return findMedianBinarySearch(nums2, nums1);
      }

    int len1 = nums1.length;
    int len2 = nums2.length;

    int low = 0;
    int high = len1;
    int totalMidPoint = (len1 + len2 + 1) / 2; // +1 to handle odd-length cases

    while (low <= high) {
      // cut in nums1 is at the middle of the current search range
      int cut1 = (low + high) / 2;

      /*
       As cut1 + cut2 should cover half of the total elements,
       cut1 + cut2 = totalMidPoint   ------ Equation 1 (+1 added to handle odd-length cases)
       Therefore cut2 = totalMidPoint - cut1
       */
      int cut2 = totalMidPoint - cut1;

      int leftMax1 = (cut1 != 0) ? nums1[cut1 - 1] : Integer.MIN_VALUE;
      int leftMax2 = (cut2 != 0) ? nums2[cut2 - 1] : Integer.MIN_VALUE;

      int rightMin1 = (cut1 != len1) ? nums1[cut1] : Integer.MAX_VALUE;
      int rightMin2 = (cut2 != len2) ? nums2[cut2] : Integer.MAX_VALUE;

      // Correct partition found
      if (leftMax1 <= rightMin2 && leftMax2 <= rightMin1) {
        if ((len1 + len2) % 2 == 0) {
          return (Math.max(leftMax1, leftMax2) + Math.min(rightMin1, rightMin2)) / 2.0;
        } else {
          return Math.max(leftMax1, leftMax2);
        }
      } else if (leftMax1 > rightMin2) {
        high = cut1 - 1;
      } else {
        low = cut1 + 1;
      }
    }

    // Unreachable under valid input assumptions
    throw new IllegalArgumentException("Invalid input: unable to find median.");
  }
}
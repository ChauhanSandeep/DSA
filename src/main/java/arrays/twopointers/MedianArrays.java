package arrays.twopointers;

/**
 * Problem: Median of Two Sorted Arrays
 *
 * You are given two sorted arrays `nums1` and `nums2` of sizes m and n.
 * Find the median of the combined sorted array in O(log(min(m, n))) time.
 *
 * Leetcode Link:
 * https://leetcode.com/problems/median-of-two-sorted-arrays/
 *
 * Example:
 * Input: nums1 = [1, 2], nums2 = [3, 4]
 * Output: 2.5
 * Explanation: Combined array = [1, 2, 3, 4], median = (2 + 3) / 2
 *
 * Follow-up Questions:
 * - What if the arrays are unsorted? (Sort before applying logic)
 * - Can this be done without merging? (Yes, use binary search on partition)
 * - Can you extend this to k sorted arrays? (Use min-heap)
 */
public class MedianArrays {

  public static void main(String[] args) {
    int[] nums1 = {1, 2};
    int[] nums2 = {3, 4};

    System.out.println("Two-Pointer Median: " + findMedianByMerging(nums1, nums2));
    System.out.println("Binary Search Median: " + findMedianBinarySearch(nums1, nums2));
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
   * Approach 2: Binary Search Partitioning
   * Perform binary search on the smaller array to find a partition where:
   *   maxLeft1 <= minRight2 && maxLeft2 <= minRight1
   *   This ensures the left partition contains all elements less than or equal to the right partition.
   *   Once the correct partition is found, compute the median based on the total length (even/odd).
   *
   * Steps:
   * 1. Perform binary search on nums1 to find the correct split.
   * 2. Use INT_MIN/INT_MAX for out-of-bound values.
   * 3. Check partition condition; compute median accordingly.
   *
   * Time Complexity: O(log(min(m, n)))
   * Space Complexity: O(1)
   *
   * @param nums1 First sorted array
   * @param nums2 Second sorted array
   * @return Median of the merged array
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
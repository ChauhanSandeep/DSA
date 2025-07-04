package Array;

/**
 * 🔹 Problem: Maximum Score of Spliced Array
 * 🔗 Leetcode: https://leetcode.com/problems/maximum-score-of-spliced-array/
 *
 * You are given two integer arrays nums1 and nums2 of equal length.
 * You may choose a subarray from either of the arrays and splice it into the other
 * to maximize the total sum of the destination array.
 *
 * A splicing operation replaces a subarray from one array into the other (of the same indices).
 *
 * 📌 Example:
 * Input:
 *   nums1 = [60, 60, 60]
 *   nums2 = [10, 90, 10]
 * Output:
 *   210
 *
 * Explanation:
 * - Splice subarray [90] from nums2 into nums1 at index 1: [60, 90, 60] → sum = 210
 *
 * ✅ Follow-ups:
 * - Can this be extended to k splices?
 *  → Yes, we can generalize the approach to allow multiple splices by iterating through all possible subarrays.
 * - What if the cost of splice varies by index?
 *  → We can adjust the gain calculation to account for variable costs.
 */
public class MaxScoreSplicedArray {

  public static void main(String[] args) {
    MaxScoreSplicedArray obj = new MaxScoreSplicedArray();

    int[] nums1 = {60, 60, 60};
    int[] nums2 = {10, 90, 10};

    System.out.println("Max Spliced Score: " + obj.maximumSplicedArray(nums1, nums2));
  }

  /**
   * Computes the maximum sum possible by splicing one subarray from either nums1 or nums2.
   * Approach:
   * 1. Calculate the total sum of both arrays.
   * 2. For each array, compute the maximum gain by replacing a subarray from the other array.
   * 3. Use Kadane's algorithm to find the maximum subarray gain:
   *   - Gain at index i is defined as (source[i] - target[i]).
   * 4. The final result is the maximum of:
   *   - sum1 + maxGain1 (splicing nums2 into nums1)
   *   - sum2 + maxGain2 (splicing nums1 into nums2)
   *
   * 🔹 Time Complexity: O(N)
   * 🔹 Space Complexity: O(1)
   *
   * @param nums1 First input array
   * @param nums2 Second input array
   * @return Maximum possible sum after optimal splice
   */
  public int maximumSplicedArray(int[] nums1, int[] nums2) {
    int sum1 = arraySum(nums1);
    int sum2 = arraySum(nums2);

    // Splice from nums2 into nums1. Gain = nums2[i] - nums1[i]
    int maxGain1 = maxSubarrayGain(nums2, nums1);

    // Splice from nums1 into nums2. Gain = nums1[i] - nums2[i]
    int maxGain2 = maxSubarrayGain(nums1, nums2);

    // Return the best possible final score
    return Math.max(sum1 + maxGain1, sum2 + maxGain2);
  }

  /**
   * Computes the maximum gain by replacing a subarray from source into target.
   * The gain at index i is (source[i] - target[i]).
   * Uses Kadane’s algorithm to find the max subarray sum of the gain array.
   *
   * 🔹 Time Complexity: O(N)
   * 🔹 Space Complexity: O(1)
   *
   * @param source The array being spliced in
   * @param target The array being spliced into
   * @return Maximum gain obtainable
   */
  private int maxSubarrayGain(int[] source, int[] target) {
    int maxGain = 0;      // Maximum gain found
    int currentGain = 0; // Current subarray gain being calculated

    // Calculate the gain for each index
    for (int i = 0; i < source.length; i++) {
      int gain = source[i] - target[i];
      currentGain = Math.max(gain, currentGain + gain);
      maxGain = Math.max(maxGain, currentGain);
    }

    return maxGain;
  }

  private int arraySum(int[] arr) {
    int sum = 0;
    for (int value : arr) {
      sum += value;
    }
    return sum;
  }
}
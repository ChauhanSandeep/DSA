package Array;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * 🔹 Problem: Maximum Score of Spliced Array
 * 🔗 Leetcode: https://leetcode.com/problems/maximum-score-of-spliced-array/
 *
 * You are given two integer arrays nums1 and nums2 of equal length.
 * You may choose a subarray from either of the arrays and splice it into the other
 * to maximize the total sum of the destination array. The splicing can be done at any index,
 * and the subarray can be of any length (including zero).
 *
 * A splicing operation replaces a subarray from one array into the other (of the same indices).
 *
 * Example:
 * Input:
 *   nums1 = [60, 60, 60]
 *   nums2 = [10, 90, 10]
 * Output:
 *   210
 *
 * Explanation:
 * - Splice subarray [90] from nums2 into nums1 at index 1: [60, 90, 60] → sum = 210
 *
 * FAANG level Follow-ups:
 * Q: What if we can splice multiple subarrays?
 * A: Extend the algorithm to handle multiple splice operations.
 *
 * Q: How to find the indices of the spliced subarrays?
 * A: Track start and end indices during the maximum gain calculation.
 *
 * Q: Can we optimize for space complexity?
 * A: Use in-place modifications or single-pass algorithms to reduce space usage.
 *
 * Q: How does this relate to other array manipulation problems?
 * A: Similar to maximum sum subarray problems, but with splicing flexibility.
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
    SpliceResult spliceInto1 = maxSubarrayGainWithIndices(nums2, nums1);

    // Splice from nums1 into nums2. Gain = nums1[i] - nums2[i]
    SpliceResult spliceInto2 = maxSubarrayGainWithIndices(nums1, nums2);

    // Return the best possible final score
    int maxScore = Math.max(sum1 + spliceInto1.gain, sum2 + spliceInto2.gain);

    if (sum1 + spliceInto1.gain >= sum2 + spliceInto2.gain) {
      System.out.println("Splice nums2 into nums1 from index " + spliceInto1.start + " to " + spliceInto1.end);
    } else {
      System.out.println("Splice nums1 into nums2 from index " + spliceInto2.start + " to " + spliceInto2.end);
    }

    return maxScore;
  }

  /**
   * Computes the maximum gain by replacing a subarray from source into target.
   * The gain at index i is (source[i] - target[i]).
   * Uses Kadane’s algorithm to find the max subarray sum of the gain array.
   *
   * Time Complexity: O(N)
   * Space Complexity: O(1)
   *
   * @param source The array being spliced in
   * @param target The array being spliced into
   * @return Maximum gain obtainable
   */
  private SpliceResult maxSubarrayGainWithIndices(int[] source, int[] target) {
    int maxGainSoFar = 0;
    int maxGainEndingHere = 0;

    int start = 0;
    int tempStart = 0;
    int end = -1;

    for (int i = 0; i < source.length; i++) {
      int gain = source[i] - target[i];
      maxGainEndingHere += gain;

      if (maxGainEndingHere < 0) {
        // Reset if the current gain is negative
        maxGainEndingHere = 0;
        tempStart = i + 1;
      } else if (maxGainEndingHere > maxGainSoFar) {
        // Update max gain and indices if we found a new maximum
        maxGainSoFar = maxGainEndingHere;
        start = tempStart;
        end = i;
      }
    }

    return new SpliceResult(maxGainSoFar, start, end);
  }

  private int arraySum(int[] arr) {
    int sum = 0;
    for (int value : arr) {
      sum += value;
    }
    return sum;
  }

  private class SpliceResult {
    public int gain;  // Maximum gain from splicing
    public int start; // Start index of the subarray spliced in
    public int end;   // End index of the subarray spliced in

    public SpliceResult(int gain, int start, int end) {
      this.gain = gain;
      this.start = start;
      this.end = end;
    }
  }

}
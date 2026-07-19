package arrays.kadane;



import java.util.Arrays;/**
 * Problem: Maximum Score of Spliced Array
 *
 * You are given two equal-length arrays. You may choose one contiguous subarray
 * from one array and splice it into the same positions of the other array. Return
 * the best possible sum of either final array after at most one splice.
 *
 * Leetcode: https://leetcode.com/problems/maximum-score-of-spliced-array/
 * Rating:   1791 (zerotrac Elo, Q3, weekly-contest-299)
 * Pattern:  Arrays | Kadane | Maximum gain over differences
 *
 * Example:
 *   Input:  nums1 = [60,60,60], nums2 = [10,90,10]
 *   Output: 210
 *   Why:    replacing the middle 60 in nums1 with 90 from nums2 adds a gain of 30,
 *           turning nums1's sum from 180 into 210.
 *
 * Follow-ups:
 *   1. What if you can splice multiple non-overlapping subarrays?
 *      Use dynamic programming over the difference array with a transaction count.
 *   2. What if you need the chosen splice indices?
 *      Track Kadane's candidate start and best end while computing gain.
 *   3. What if arrays are streamed and cannot be stored?
 *      Maintain sums and Kadane gains online as paired values arrive.
 *
 * Related: Maximum Subarray (53), Best Time to Buy and Sell Stock (121).
 */
public class MaxScoreSplicedArray {
    public static void main(String[] args) {
        MaxScoreSplicedArray solver = new MaxScoreSplicedArray();
        int[][] nums1Cases = {{60, 60, 60}, {20, 40, 20, 70, 30}};
        int[][] nums2Cases = {{10, 90, 10}, {50, 20, 50, 40, 20}};
        int[] expected = {210, 220};

        for (int i = 0; i < nums1Cases.length; i++) {
            int got = solver.maximumSplicedArray(nums1Cases[i], nums2Cases[i]);
            System.out.printf("nums1=%s nums2=%s -> %d  expected=%d%n",
                Arrays.toString(nums1Cases[i]), Arrays.toString(nums2Cases[i]), got, expected[i]);
        }
    }

  /**
     * Intuition: splicing nums2 into nums1 changes nums1 only by the differences
     * nums2[i] - nums1[i] over the chosen subarray. So the best splice into nums1
     * is the maximum subarray sum over that difference stream. The same reasoning
     * applies in the other direction with nums1[i] - nums2[i]. We compute both base
     * sums and both best gains; the answer is whichever destination array gets the
     * larger final sum. A zero-length splice is allowed, so negative gains are reset
     * to zero.
     *
     * Time:  O(n) - each paired index contributes to sums and gain scans a constant number of times.
     * Space: O(1) - only running sums and Kadane state are stored.
     *
     * @param nums1 first array
     * @param nums2 second array of the same length
     * @return maximum possible array sum after at most one splice
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
   *
   * Steps:
   * 1. Create a difference array where diff[i] = source[i] - target[i].
   * 2. Use Kadane's algorithm to find the maximum sum subarray in the difference array.
   *    This signifies the maximum gain obtainable by splicing.
   * 3. Track the start and end indices of the maximum gain subarray.
   *
   * Time Complexity: O(N)
   * Space Complexity: O(N) for the difference array
   *
   * @param source The array from where subarray is picked
   * @param target The array into which subarray is placed
   * @return Maximum gain obtainable
   */
  private SpliceResult maxSubarrayGainWithIndices(int[] source, int[] target) {
    int length = source.length;

    // Create difference array
    int[] diff = new int[length];
    for (int i = 0; i < length; i++) {
      diff[i] = source[i] - target[i];
    }

    // Kadane's algorithm to find max subarray sum with indices
    int maxGainSoFar = 0;
    int maxGainEndingHere = 0;
    int start = 0;
    int tempStart = 0;
    int end = -1;

    for (int i = 0; i < length; i++) {
      maxGainEndingHere += diff[i];
      if (maxGainEndingHere < 0) {
        maxGainEndingHere = 0;
        tempStart = i + 1;
      } else if (maxGainEndingHere > maxGainSoFar) {
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

  private static class SpliceResult {
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
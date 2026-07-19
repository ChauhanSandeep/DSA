package arrays.xor;



import java.util.Arrays;/**
 * Problem: Missing Number in 1..N
 *
 * Given an array of N - 1 distinct integers from the range 1 through N, find the
 * one missing number. The array may be in any order. This is the 1-based variant
 * of the common LeetCode problem that uses the range 0 through N.
 *
 * Leetcode: https://leetcode.com/problems/missing-number/ (0..N variant)
 * Rating:   acceptance 72.3% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Arrays | XOR | Cancellation or arithmetic sum
 *
 * Example:
 *   Input:  [1,2,3,4,5,6,7,8,10]
 *   Output: 9
 *   Why:    the complete range is 1..10, and every value except 9 appears once.
 *
 * Follow-ups:
 *   1. What if the range starts at 0 instead of 1?
 *      Include 0..N in the sum or XOR range; XOR with 0 has no effect.
 *   2. What if one number is duplicated and one is missing?
 *      Use sum and square-sum equations, or an index-marking approach.
 *   3. What if the array is streamed?
 *      Maintain running XOR or running sum without storing the array.
 *
 * Related: First Missing Positive (41), Set Mismatch (645).
 */
public class MissingNumber {
    public static void main(String[] args) {
        int[][] inputs = {{1, 2, 3, 4, 5, 6, 7, 8, 10}, {2, 3}, {1}};
        int[] expected = {9, 1, 2};

        for (int i = 0; i < inputs.length; i++) {
            int got = findMissingNumberUsingXOR(inputs[i]);
            System.out.printf("arr=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

  /**
     * Intuition: the full range 1..N has a known arithmetic sum. The input sum is
     * exactly the same except that the missing value is absent. Subtracting the
     * actual sum from the expected sum leaves only the missing value. Using long for
     * the expected and actual sums avoids overflow while multiplying N by N + 1.
     *
     * Time:  O(n) - the array is scanned once to compute the actual sum.
     * Space: O(1) - only two running sums are stored.
     *
     * @param arr array of N - 1 distinct values from 1 through N
     * @return missing value from the range
     */
  public static int findMissingNumberUsingSum(int[] arr) {
    int length = arr.length + 1; // Total number expected
    long expectedSum = ((long) length * (length + 1)) / 2;

    long actualSum = 0;
    for (int num : arr) {
      actualSum += num;
    }

    return (int) (expectedSum - actualSum);
  }

  /**
     * Intuition (interview default): XOR cancels equal values because x xor x is
     * zero, and zero xor x gives x back. If we XOR every number in the complete
     * range and every number in the input, all present values appear exactly twice
     * and vanish. The only value that appears once in the combined XOR is the
     * missing number, so it remains as the final result.
     *
     * Time:  O(n) - one pass over the range and one pass over the array are both linear.
     * Space: O(1) - only running XOR values are stored.
     *
     * @param arr array of N - 1 distinct values from 1 through N
     * @return missing value from the range
     */
  public static int findMissingNumberUsingXOR(int[] arr) {
    int n = arr.length + 1;

    int xorAll = 0;
    for (int i = 1; i <= n; i++) {
      xorAll ^= i;
    }

    int xorArr = 0;
    for (int num : arr) {
      xorArr ^= num;
    }

    return xorAll ^ xorArr;
  }
}

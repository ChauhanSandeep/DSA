package arrays.xor;

/**
 * 🔍 Problem: Given an array containing N-1 distinct integers from the range 1 to N,
 * find the missing number.
 *
 * ✅ The array is guaranteed to have no duplicates, and exactly one number is missing.
 *
 * Example:
 * Input:  [1, 2, 3, 4, 5, 6, 7, 8, 10]
 * Output: 9
 *
 * 🔗 LeetCode Link: https://leetcode.com/problems/missing-number/ (variation with 0 to N)
 *
 * Approaches Covered:
 * 1. Sum Formula
 * 2. XOR Method
 *
 * Follow-up Questions:
 * - Q: What if elements are not sorted?
 *   A: These methods work regardless of order.
 * - Q: What if elements start from 0?
 *   A: Modify formula/XOR to go from 0 to N instead of 1 to N.
 * - Q: How to detect duplicates instead of missing?
 *   A: Use HashSet or frequency counter approach.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class MissingNumber {

  public static void main(String[] args) {
    int[] input = {1, 2, 3, 4, 5, 6, 7, 8, 10};

    System.out.println("Missing Number (Sum Method): " + findMissingNumberUsingSum(input));
    System.out.println("Missing Number (XOR Method): " + findMissingNumberUsingXOR(input));
  }

  /**
   * ✅ Method 1: Sum Formula
   * - Uses the arithmetic sum formula to find missing number:
   *   sum(1 to N) - sum(array)
   *
   * Time Complexity: O(N)
   * Space Complexity: O(1)
   *
   * @param arr Array of N-1 integers in the range 1 to N
   * @return Missing number
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
   * ✅ Method 2: XOR Method
   * - XOR of 1^2^3^...^N ^ arr[0]^arr[1]^...^arr[N-2] gives the missing number.
   *
   * XOR Truth:
   *   A ^ A = 0
   *   A ^ 0 = A
   *
   * Time Complexity: O(N)
   * Space Complexity: O(1)
   *
   * @param arr Array of N-1 integers in the range 1 to N
   * @return Missing number
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
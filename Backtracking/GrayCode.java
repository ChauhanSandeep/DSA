package Backtracking;

import java.util.ArrayList;
import java.util.List;


/**
 * Problem: Gray Code
 * LeetCode: https://leetcode.com/problems/gray-code/
 *
 * Statement:
 * The Gray Code is a binary numeral system in which two successive values differ in only one bit.
 * Given a non-negative integer n representing the total number of bits in the code,
 * return the sequence of Gray Code, starting from 0.
 *
 * Example:
 * Input: n = 2
 * Output: [0, 1, 3, 2]
 * Explanation: In binary, this corresponds to: 00, 01, 11, 10.
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. Can you generate Gray Code without recursion?
 *    - Yes, by iteratively generating the sequence using the reflection method.
 * 2. Can you generate directly in integer form without using strings?
 *    - Yes, using the formula: grayCode(i) = i ^ (i >> 1). Solved in method 2.
 * 3. How would you adapt this for very large n?
 *    - Use bit manipulation to generate integers directly, avoiding string operations.
 */
public class GrayCode {
  public static void main(String[] args) {
    List<Integer> grayCodeSequence = generateGrayCode(4);
    for (Integer num : grayCodeSequence) {
      System.out.println(Integer.toBinaryString(num));  // Print as binary representation
    }
  }

  /**
   * Generates the Gray Code sequence for a given number of bits.
   *
   * Approach:
   * 1. Gray Code of size num can be derived from Gray Code of size num-1.
   * 2. Take the sequence for num-1 bits:
   *    - Prefix '0' to all codes (first half).
   *    - Prefix '1' to the reversed sequence (second half).
   * 3. Convert binary strings to integers before returning.
   *
   * Algorithm: Recursive generation + reflection method.
   * Time Complexity: O(2^num) — Each sequence has length 2^num.
   * Space Complexity: O(2^num) — To store all Gray codes.
   *
   * @param num Number of bits.
   * @return List of integers representing the Gray Code sequence.
   */
  public static List<Integer> generateGrayCode(int num) {
      // binaryGrayCode = ["0", "1", "11", "10"]
    List<String> binaryGrayCode = generateGrayCodeRecursive(num);
    // grayCodeNumbers = [0, 1, 3, 2]
    List<Integer> grayCodeNumbers = new ArrayList<>(binaryGrayCode.size());

    for (String binaryCode : binaryGrayCode) {
        // radix 2 means binary conversion. similarly radix 8 for octal. Without radix, it will be decimal
      grayCodeNumbers.add(Integer.parseInt(binaryCode, 2));
    }
    return grayCodeNumbers;
  }

  /**
   * Recursively generates Gray Code sequence in binary string format.
   *
   * Steps:
   * 1. Base case for n=0: ["0"], for n=1: ["0","1"].
   * 2. Recursively generate Gray Code for (n-1) bits.
   * 3. First half: prefix '0' to each code.
   * 4. Second half: reverse the previous sequence and prefix '1'.
   *
   * @param n Number of bits.
   * @return List of binary strings representing the Gray Code sequence.
   */
  private static List<String> generateGrayCodeRecursive(int n) {
    if (n == 0) {
      List<String> baseCase = new ArrayList<>();
      baseCase.add("0");
      return baseCase;
    }
    if (n == 1) {
      List<String> baseCase = new ArrayList<>();
      baseCase.add("0");
      baseCase.add("1");
      return baseCase;
    }

    List<String> previousGrayCode = generateGrayCodeRecursive(n - 1);
    List<String> currentGrayCode = new ArrayList<>(previousGrayCode.size() * 2);

    // Prefix '0' to first half
    for (String code : previousGrayCode) {
      currentGrayCode.add("0" + code);
    }

    // Prefix '1' to reversed second half
    for (int i = previousGrayCode.size() - 1; i >= 0; i--) {
      currentGrayCode.add("1" + previousGrayCode.get(i));
    }

    return currentGrayCode;
  }

  /**
   * Optimized iterative solution using bit manipulation.
   *
   * Intuition:
   * Gray code of an integer i can be computed as i ^ (i >> 1).
   *
   * | i (Decimal) | i (Binary) | i >> 1 (Binary) | XOR Result | Gray Code (Binary) |
   * | ----------- | ---------- | --------------- | ---------- | ------------------ |
   * | 0           | 000        | 000             | 000        | 000                |
   * | 1           | 001        | 000             | 001        | 001                |
   * | 2           | 010        | 001             | 011        | 011                |
   * | 3           | 011        | 001             | 010        | 010                |
   * | 4           | 100        | 010             | 110        | 110                |
   * | 5           | 101        | 010             | 111        | 111                |
   * | 6           | 110        | 011             | 101        | 101                |
   * | 7           | 111        | 011             | 100        | 100                |
   *
   * Time Complexity: O(2^num)
   * Space Complexity: O(1) extra space (excluding output list).
   *
   * @param num Number of bits.
   * @return List of integers representing the Gray Code sequence.
   */
  public static List<Integer> generateGrayCodeOptimized(int num) {
    List<Integer> result = new ArrayList<>(1 << num); // means that the size of the list will be 2^num
    for (int i = 0; i < (1 << num); i++) {
      result.add(i ^ (i >> 1));
    }
    return result;
  }
}

package arrays.binarysearch;

import java.util.Arrays;


/**
 * Problem: Generate Boolean Expression from Integer Array
 * No direct LeetCode link available. Related idea: Range OR expressions for set membership checks.
 *
 * Problem Statement:
 * Given an array of integers, generate a boolean expression string that evaluates to `true`
 * only for the integers in the array, and `false` for everything else.
 *
 * Expression format: "x == 1 || x == 3 || x == 5"
 *
 * Example:
 * Input: [1, 3, 4]
 * Output: ((x == 1 || x == 3) || x == 4)
 *
 * Follow-up Questions:
 * - Q: Can we optimize the expression by detecting contiguous ranges?
 *   A: Yes. Instead of listing all values, express ranges as: `(x >= 1 && x <= 5)`.
 *      Leetcode follow-up idea: https://leetcode.com/problems/summary-ranges/
 * - Q: How would you optimize for large datasets (e.g., 10^6 values)?
 *   A: Use Trie or Interval Trees to compact expression or generate bytecode instead of a string.
 * - Q: Can you minimize the output string length further?
 *   A: Merge contiguous numbers or use regular expressions (in case of strings).
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class CreateExpression {

  /**
   * Entry point for test execution.
   */
  public static void main(String[] args) {
    int[] inputArray = {1, 3, 4, 5, 6, 10, 11, 12, 16, 19, 20};
    String expression = buildBooleanExpression(inputArray);
    System.out.println(expression);
  }

  /**
   * Constructs a balanced boolean expression that evaluates to true for the given numbers.
   *
   * 📌 Steps:
   * - Validate input and return empty string for empty arrays.
   * - Sort the input for consistency.
   * - Build balanced OR-tree recursively for better readability.
   *
   * ⏱ Time Complexity: O(n log n) for sorting + O(n) expression building
   * 📦 Space Complexity: O(log n) recursion stack
   *
   * @param numbers Array of integers to build expression for
   * @return String representing the boolean expression
   */
  public static String buildBooleanExpression(int[] numbers) {
    if (numbers == null || numbers.length == 0) {
      return "";  // Edge case: empty input
    }

    Arrays.sort(numbers); // Ensure deterministic structure
    return constructExpression(numbers, 0, numbers.length - 1);
  }

  /**
   * Recursively builds a balanced boolean OR expression.
   *
   * 📌 Steps:
   * - Base case 1: Single element → return (x == val)
   * - Base case 2: Two elements → return (x == a || x == b)
   * - Recursive case: Split array and recurse on left and right halves
   *
   * @param sortedNumbers Sorted array of integers
   * @param start          Start index of subarray
   * @param end            End index of subarray
   * @return String containing OR-based expression for subarray
   */
  private static String constructExpression(int[] sortedNumbers, int start, int end) {
    if (start == end) {
      return "(x == " + sortedNumbers[start] + ")";
    }

    if (end - start == 1) {
      return "(x == " + sortedNumbers[start] + " || x == " + sortedNumbers[end] + ")";
    }

    int mid = start + (end - start) / 2;
    String leftExpression = constructExpression(sortedNumbers, start, mid);
    String rightExpression = constructExpression(sortedNumbers, mid + 1, end);

    return "(" + leftExpression + " || " + rightExpression + ")";
  }
}

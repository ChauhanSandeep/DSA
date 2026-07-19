package strings.sorting;

import java.util.Arrays;


/**
 * Problem: Largest Number
 *
 * Given non-negative integers, arrange them so their concatenation forms the
 * largest possible decimal number. Return the result as a string because it may
 * be larger than any numeric type.
 *
 * Leetcode: https://leetcode.com/problems/largest-number/ (Medium)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Sorting | Custom comparator | Concatenation order
 *
 * Example:
 *   Input:  nums = [3,30,34,5,9]
 *   Output: "9534330"
 *   Why:    the comparator places x before y exactly when xy is larger than yx.
 *
 * Follow-ups:
 *   1. Return the k-th largest concatenation?
 *      This becomes permutation ranking/search and is much harder than one sort.
 *   2. Handle a stream of numbers?
 *      Maintain a balanced structure ordered by the same comparator, then emit in order.
 *   3. Prove the comparator is safe for sorting?
 *      Show that choosing the better pair order locally gives the best global concatenation.
 *
 * Related: Create Maximum Number (321), Largest Divisible Subset (368).
 */
public class LargestNumber {

    public static void main(String[] args) {
        int[][] inputs = { {3, 30, 34, 5, 9}, {0, 0}, {10, 2} };
        String[] expected = {"9534330", "0", "210"};

        for (int i = 0; i < inputs.length; i++) {
            String got = largestNumber(inputs[i]);
            System.out.printf("nums=%s -> %s  expected=%s%n",
                java.util.Arrays.toString(inputs[i]), got, expected[i]);
        }
    }


  /**
   * Intuition: for any two numbers a and b, only two relative orders matter: ab
   * or ba. Put a before b when ab is larger; sorting all numbers by that rule
   * creates the lexicographically largest concatenation.
   *
   * Algorithm:
   *   1. Return "0" for null or empty input.
   *   2. Convert each number to a string.
   *   3. Sort strings by comparing (b + a) against (a + b).
   *   4. If the largest string is "0", return "0"; otherwise concatenate all strings.
   *
   * Time:  O(n log n) - sorting n string numbers dominates.
   * Space: O(n) - string representations and the output builder are stored.
   *
   * @param nums non-negative integers to arrange
   * @return largest concatenated number as a string
   */
  public static String largestNumber(int[] nums) {
    if (nums == null || nums.length == 0) {
      return "0";
    }

    // Step 1: Convert integers to strings
    String[] strNums = Arrays.stream(nums)
        .mapToObj(String::valueOf)
        .toArray(String[]::new);

    // Step 2: Sort using custom comparator
    // [3, 30] => "330" vs "303" => "330" is larger, so "3" should come before "30"
    Arrays.sort(strNums, (a, b) -> (b + a).compareTo(a + b));

    // Step 3: Edge case — all zeroes (e.g., [0,0])
    if (strNums[0].equals("0")) {
      return "0";
    }

    // Step 4: Build final result
    StringBuilder resultBuilder = new StringBuilder();
    for (String num : strNums) {
      resultBuilder.append(num);
    }

    return resultBuilder.toString();
  }
}
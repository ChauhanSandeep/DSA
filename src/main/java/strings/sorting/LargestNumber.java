package strings.sorting;

import java.util.Arrays;


/**
 *
 * Given an array of non-negative integers, arrange them such that they form the largest number.
 *
 * ### Example:
 * Input: [3, 30, 34, 5, 9]
 * Output: "9534330"
 *
 *  https://leetcode.com/problems/largest-number/
 * 
 * ### Follow-up:
 * - Can you modify this to return the k-th largest number instead of the largest overall?
 * - What if the input is a stream of numbers?
 */
public class LargestNumber {

  public static void main(String[] args) {
    int[] nums = {3, 30, 34, 5, 9};
    String largest = largestNumber(nums);
    System.out.println("Largest number is: " + largest);
  }

  /**
   * Forms the largest number possible by rearranging integers.
   *
   * ### Approach:
   * 1. Convert integers to strings.
   * 2. Sort using a custom comparator: for strings a and b, compare (b + a) vs (a + b).
   *    - This ensures that when combined, the number is maximized.
   * 3. Concatenate the sorted strings.
   * 4. Handle edge case: when all numbers are zero, return "0".
   *
   * ### Time Complexity: O(n log n) — due to sorting.
   * ### Space Complexity: O(n) — to store string representations.
   *
   * @param nums Array of non-negative integers.
   * @return The largest number in string format.
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
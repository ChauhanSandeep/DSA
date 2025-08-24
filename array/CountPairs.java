package array;

import java.util.HashMap;
import java.util.Map;


/**
 * 🔹 Problem: Count Valid Pairs with Index Difference
 * 🔗 GeeksforGeeks: https://www.geeksforgeeks.org/count-the-pairs-in-an-array-such-that-the-difference-between-them-and-their-indices-is-equal/
 *
 * Given an array `arr[]`, count total number of pairs (i, j) such that:
 *     arr[i] - arr[j] = i - j
 *
 * ✨ Observation:
 * Rearranging the condition gives:
 *     arr[i] - i = arr[j] - j
 * So we are essentially counting how many elements have the same value of (arr[k] - k).
 *
 * 📌 Example:
 * Input: [1, 2, 3]
 * Transform: [1-0=1, 2-1=1, 3-2=1] → All values = 1
 * Output: 3 valid pairs: (0,1), (0,2), (1,2)
 *
 * ✅ Follow-up:
 * - How would this change if you wanted `arr[i] + arr[j] = i + j`?
 *   → Transform: `arr[i] - i = -(arr[j] - j)`
 *   → Still hash-based but with negative counterpart lookup.
 */
public class CountPairs {

  public static void main(String[] args) {
    int[] arr = {1, 2, 3};
    int result = new CountPairs().countValidPairs(arr);
    System.out.println("Total valid pairs: " + result);
  }

  /**
   * Counts number of valid index pairs (i, j) such that:
   *     arr[i] - arr[j] = i - j
   *
   * 🔹 Algorithm:
   * 1. For each index `i`, compute transformedValue = arr[i] - i
   * 2. Keep a frequency map of how many times each transformed value has occurred so far
   * 3. For every repeat occurrence, all previous occurrences can pair with the current one
   *    (i.e., count += frequency[transformedValue])
   *
   * 🔹 Time Complexity: O(N) — one pass through array + constant map ops
   * 🔹 Space Complexity: O(N) — for hashmap
   *
   * @param arr Input integer array
   * @return Number of valid (i, j) pairs satisfying the given condition
   */
  public int countValidPairs(int[] arr) {
    Map<Integer, Integer> valueToFrequency = new HashMap<>();
    int totalValidPairs = 0;

    for (int index = 0; index < arr.length; index++) {
      int transformedValue = arr[index] - index;

      // All previous elements with same transformed value form valid pairs with current index
      int existingCount = valueToFrequency.getOrDefault(transformedValue, 0);
      totalValidPairs += existingCount;

      // Update frequency map for current transformed value
      valueToFrequency.put(transformedValue, existingCount + 1);
    }

    return totalValidPairs;
  }
}
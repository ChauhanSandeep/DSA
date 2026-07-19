package arrays.hashmap;

import java.util.HashMap;
import java.util.Map;


import java.util.Arrays;
/**
 * Problem: Count Valid Pairs with Index Difference
 *
 * Given an array, count pairs of indices (i, j) with i < j such that
 * arr[i] - arr[j] equals i - j. The values do not need to be adjacent; the
 * condition only cares about how each value compares with its index.
 *
 * Source: GeeksforGeeks, Count pairs where difference between values and indices is equal
 * Pattern:  Arrays | Hash map | Rearranging an equation into equal keys
 *
 * Example:
 *   Input:  [1, 2, 3]
 *   Output: 3
 *   Why:    each index has arr[index] - index equal to 1, so all three index
 *           pairs share the same transformed key and are valid.
 *
 * Follow-ups:
 *   1. What if the condition is arr[i] + arr[j] = i + j?
 *      Store arr[i] - i and look for its negated counterpart.
 *   2. What if the array is streamed and cannot be stored?
 *      Keep only the frequency map of transformed values and update the count online.
 *   3. What if the answer can exceed int range?
 *      Return a long and store the running pair count in a long.
 */
public class CountPairs {
    public static void main(String[] args) {
        CountPairs solver = new CountPairs();
        int[][] inputs = {{1, 2, 3}, {3, 1, 4, 1, 5}, {7}};
        int[] expected = {3, 0, 0};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.countValidPairs(inputs[i]);
            System.out.printf("arr=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

  /**
     * Intuition: the equation looks like it compares two positions, but it can be
     * rearranged so each index has its own independent key: arr[i] - i. Two indices
     * form a valid pair exactly when their keys match. That turns the problem into
     * counting equal-key pairs. As we scan left to right, every previous occurrence
     * of the same key forms one new pair with the current index, so the frequency
     * map tells us the number of new pairs immediately.
     *
     * Time:  O(n) - the array is scanned once and each map update is average O(1).
     * Space: O(n) - in the worst case every transformed key is different.
     *
     * @param arr input integer array
     * @return number of valid pairs satisfying arr[i] - arr[j] = i - j
 *
 * Algorithm:
 *   1. Transform the input into the state described by the intuition.
 *   2. Scan the relevant values once, updating the running data structure or answer.
 *   3. Return the accumulated result after all constraints have been checked.
     */
  public int countValidPairs(int[] arr) {
    Map<Integer, Integer> valueToFrequency = new HashMap<>(); // <arr[i] - i, frequency>
    int totalValidPairs = 0;

    for (int index = 0; index < arr.length; index++) {
      int transformedValue = arr[index] - index;

      // All previous elements with same transformed value form valid pairs with current index.
      int existingCount = valueToFrequency.getOrDefault(transformedValue, 0);
      totalValidPairs += existingCount;

      // Update frequency map for current transformed value
      valueToFrequency.put(transformedValue, existingCount + 1);
    }

    return totalValidPairs;
  }
}
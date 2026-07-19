package arrays.hashmap;

import java.util.*;

/**
 * Problem: Find Original Array From Doubled Array
 *
 * A changed array was made by taking an original array, appending twice every
 * original value, and shuffling the result. Reconstruct one valid original array,
 * or return an empty array when the changed array cannot be paired this way.
 *
 * Leetcode: https://leetcode.com/problems/find-original-array-from-doubled-array/
 * Rating:   1557 (zerotrac Elo, Q2, biweekly-contest-61)
 * Pattern:  Arrays | Hash map | Sort and consume pairs
 *
 * Example:
 *   Input:  [1,3,4,2,6,8]
 *   Output: [1,3,4]
 *   Why:    1 pairs with 2, 3 pairs with 6, and 4 pairs with 8, using every
 *           value in the changed array exactly once.
 *
 * Follow-ups:
 *   1. What if values may be negative too?
 *      Process keys by absolute value so -2 is paired before -4.
 *   2. What if each value was tripled instead of doubled?
 *      Pair x with 3*x using the same frequency-consumption idea.
 *   3. What if you only need to validate, not return the original?
 *      Keep the same counts but avoid storing the result array.
 *
 * Related: Array of Doubled Pairs (954).
 */
public class FindOriginalArrayFromDoubledArray {

    public static void main(String[] args) {
        FindOriginalArrayFromDoubledArray solver = new FindOriginalArrayFromDoubledArray();
        int[][] inputs = {{1, 3, 4, 2, 6, 8}, {0, 0, 0}, {2, 4, 4, 8}};
        String[] expected = {"[1, 3, 4]", "[]", "[2, 4]"};

        for (int i = 0; i < inputs.length; i++) {
            int[] got = solver.findOriginalArray(inputs[i].clone());
            System.out.printf("changed=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), Arrays.toString(got), expected[i]);
        }
    }


    /**
     * Intuition (interview default): once the numbers are sorted, the smallest
     * unused value cannot be the double of any later positive value, so it must be
     * an original value that needs a matching double. A frequency map lets us skip
     * values that were already consumed by an earlier pair. Zeros still need even
     * counts because each zero pairs with another zero. If any needed double is
     * missing, no shuffle of the same numbers can form a valid doubled array.
     *
     * Time:  O(n log n) - sorting dominates the linear pairing pass.
     * Space: O(n) - the frequency map and original list may each hold proportional data.
     *
     * @param changed shuffled doubled array with non-negative values
     * @return reconstructed original array, or an empty array when pairing is impossible
     */
    public int[] findOriginalArray(int[] changed) {
        int length = changed.length;
        if (length % 2 == 1) return new int[0];

        // Count frequencies
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int num : changed) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }

        // Sort to process smaller elements first
        Arrays.sort(changed);
        List<Integer> result = new ArrayList<>();

        for (int num : changed) {
            if (freqMap.get(num) == 0) continue;

            // Special case for zero
            if (num == 0) {
                if (freqMap.get(0) >= 2) {
                    result.add(0);
                    freqMap.put(0, freqMap.get(0) - 2);
                } else {
                    return new int[0];
                }
            } else {
                // Check if double exists
                int doubled = num * 2;
                if (freqMap.getOrDefault(doubled, 0) > 0) {
                    result.add(num);
                    freqMap.put(num, freqMap.get(num) - 1);
                    freqMap.put(doubled, freqMap.get(doubled) - 1);
                } else {
                    return new int[0];
                }
            }
        }

        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Alternative approach using TreeMap for automatic sorting
     * Time Complexity: O(n log n),
     * Space Complexity: O(n)
     */
    public int[] findOriginalArrayTreeMap(int[] changed) {
        if (changed.length % 2 == 1) return new int[0];

        TreeMap<Integer, Integer> freq = new TreeMap<>();
        for (int num : changed) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }

        List<Integer> result = new ArrayList<>();

        while (!freq.isEmpty()) {
            int smallest = freq.firstKey();

            if (smallest == 0) {
                int count = freq.get(0);
                if (count % 2 != 0) return new int[0];

                for (int i = 0; i < count / 2; i++) {
                    result.add(0);
                }
                freq.remove(0);
            } else {
                int doubled = smallest * 2;
                if (!freq.containsKey(doubled)) {
                    return new int[0];
                }

                result.add(smallest);

                // Decrease frequencies
                freq.put(smallest, freq.get(smallest) - 1);
                freq.put(doubled, freq.get(doubled) - 1);

                // Remove if frequency becomes 0
                if (freq.get(smallest) == 0) freq.remove(smallest);
                if (freq.get(doubled) == 0) freq.remove(doubled);
            }
        }

        return result.stream().mapToInt(Integer::intValue).toArray();
    }
}

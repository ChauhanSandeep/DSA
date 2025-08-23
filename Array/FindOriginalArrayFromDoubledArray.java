package Array;

import java.util.*;

/**
 * Find Original Array From Doubled Array
 * 
 * Problem: Given an array of integers, determine if it's a doubled array.
 * A doubled array contains pairs where one element is exactly twice the other.
 * 
 * Example: changed = [1,3,4,2,6,8] -> Output: [1,3,4]
 * Original array [1,3,4] becomes [1,3,4,2,6,8] when doubled.
 * 
 * LeetCode: https://leetcode.com/problems/find-original-array-from-doubled-array
 * 
 * Follow-up Questions:
 * - What if elements can be tripled instead? (Check for x*3 instead of x*2)
 * - How to handle negative numbers? (Current solution handles them correctly)
 * - What if multiple original arrays are possible? (Problem guarantees unique solution)
 */
public class FindOriginalArrayFromDoubledArray {

    /**
     * Reconstructs original array from doubled array.
     * 
     * Algorithm:
     * 1. Check if array length is odd (impossible to be doubled)
     * 2. Count frequency of each element
     * 3. Process elements in sorted order
     * 4. For each element x, check if 2x exists with sufficient frequency
     * 5. Add x to result and decrease frequency of both x and 2x
     * 
     * Time Complexity: O(n log n) due to sorting
     * Space Complexity: O(n) for frequency map and result
     * 
     * @param changed the doubled array
     * @return original array or empty array if not possible
     */
    public int[] findOriginalArray(int[] changed) {
        int n = changed.length;
        if (n % 2 == 1) return new int[0];

        // Count frequencies
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : changed) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }

        // Sort to process smaller elements first
        Arrays.sort(changed);
        List<Integer> result = new ArrayList<>();

        for (int num : changed) {
            if (freq.get(num) == 0) continue;

            // Special case for zero
            if (num == 0) {
                if (freq.get(0) >= 2) {
                    result.add(0);
                    freq.put(0, freq.get(0) - 2);
                } else {
                    return new int[0];
                }
            } else {
                // Check if double exists
                int doubled = num * 2;
                if (freq.getOrDefault(doubled, 0) > 0) {
                    result.add(num);
                    freq.put(num, freq.get(num) - 1);
                    freq.put(doubled, freq.get(doubled) - 1);
                } else {
                    return new int[0];
                }
            }
        }

        return result.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Alternative approach using TreeMap for automatic sorting
     * Time Complexity: O(n log n), Space Complexity: O(n)
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

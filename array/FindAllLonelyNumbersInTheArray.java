package array;

import java.util.*;

/**
 * Find All Lonely Numbers In The Array
 *
 * Problem: A number is lonely if it appears exactly once and neither x+1 nor x-1 appears.
 * Return all lonely numbers in any order.
 *
 * Example: nums = [10,6,5,8] -> Output: [10,8]
 * 10 appears once, 9 and 11 don't appear. 8 appears once, 7 and 9 don't appear.
 *
 * LeetCode: https://leetcode.com/problems/find-all-lonely-numbers-in-the-array
 *
 * Follow-up Questions:
 * - How to find numbers that appear exactly k times with no neighbors? (Modify frequency check)
 * - What if we want lonely numbers within distance d? (Check range [x-d, x+d])
 * - Can we solve without using extra space? (Sorting approach possible but O(n log n))
 */
public class FindAllLonelyNumbersInTheArray {

    /**
     * Finds all lonely numbers in the array.
     *
     * Algorithm:
     * 1. Count frequency of each number using map
     * 2. For each number that appears exactly once
     * 3. Check if neither x-1 nor x+1 exists in the map
     * 4. Add to result if condition satisfied
     *
     * Time Complexity: O(n) where n is array length
     * Space Complexity: O(n) for frequency map and result list
     *
     * @param nums input array of integers
     * @return list of all lonely numbers
     */
    public List<Integer> findLonely(int[] nums) {
        List<Integer> result = new ArrayList<>();
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        // Count frequencies
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        // Find lonely numbers
        for (int num : frequencyMap.keySet()) {
            // Number must appear exactly once
            if (frequencyMap.get(num) == 1) {
                // Check if neighbors don't exist
                boolean hasLeftNeighbor = frequencyMap.containsKey(num - 1);
                boolean hasRightNeighbor = frequencyMap.containsKey(num + 1);

                if (!hasLeftNeighbor && !hasRightNeighbor) {
                    result.add(num);
                }
            }
        }

        return result;
    }

    /**
     * Cleaner implementation with stream API
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public List<Integer> findLonelyStream(int[] nums) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }

        return freq.keySet().stream()
                .filter(num -> freq.get(num) == 1 &&
                              !freq.containsKey(num - 1) &&
                              !freq.containsKey(num + 1))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    /**
     * Set-based approach (two passes)
     * Time Complexity: O(n), Space Complexity: O(n)
     */
    public List<Integer> findLonelySet(int[] nums) {
        List<Integer> result = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicates = new HashSet<>();

        // First pass: identify unique vs duplicate numbers
        for (int num : nums) {
            if (seen.contains(num)) {
                duplicates.add(num);
            } else {
                seen.add(num);
            }
        }

        // Second pass: check unique numbers for loneliness
        for (int num : seen) {
            if (!duplicates.contains(num) &&
                !seen.contains(num - 1) &&
                !seen.contains(num + 1)) {
                result.add(num);
            }
        }

        return result;
    }
}

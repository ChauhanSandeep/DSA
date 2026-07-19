package arrays.slidingwindow;

import java.util.*;

/**
 * Problem: Find All Lonely Numbers in the Array
 *
 * A number is lonely when it appears exactly once and neither neighboring value
 * x - 1 nor x + 1 appears anywhere in the array. Return all lonely values in any
 * order.
 *
 * Leetcode: https://leetcode.com/problems/find-all-lonely-numbers-in-the-array/ (Medium)
 * Rating:   acceptance 60.2% (Medium) - contest rating 1276
 * Pattern:  Hash map | Frequency counting | Neighbor lookup
 *
 * Example:
 *   Input:  nums = [10,6,5,8]
 *   Output: [10,8]
 *   Why:    10 and 8 each appear once, and neither has an adjacent value present.
 *
 * Follow-ups:
 *   1. Find values appearing exactly k times with no neighbors?
 *      Replace the frequency check with freq == k.
 *   2. What if loneliness means no value within distance d?
 *      Check the range [x - d, x + d], or sort and inspect neighbors.
 *   3. Can this be solved with less extra space?
 *      Sort in place and scan adjacent values, trading O(n log n) time for O(1) space.
 */
public class FindAllLonelyNumbersInTheArray {

    public static void main(String[] args) {
        FindAllLonelyNumbersInTheArray solver = new FindAllLonelyNumbersInTheArray();
        int[][] inputs = {{10, 6, 5, 8}, {1, 3, 5, 3}, {1, 2, 3}};
        List<List<Integer>> expected = Arrays.asList(
            Arrays.asList(8, 10),
            Arrays.asList(1, 5),
            Collections.emptyList()
        );

        for (int i = 0; i < inputs.length; i++) {
            List<Integer> got = solver.findLonely(inputs[i]);
            Collections.sort(got);
            System.out.printf("nums=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), got, expected.get(i));
        }
    }


    /**
     * Intuition: loneliness depends on global facts: the value's own frequency
     * and whether neighboring values exist anywhere. A frequency map answers all
     * three questions in constant time per distinct value.
     *
     * Algorithm:
     *   1. Count each value in frequencyMap.
     *   2. Visit each distinct value and keep only values with frequency one.
     *   3. Add the value when neither num - 1 nor num + 1 exists in the map.
     *
     * Time:  O(n) - build the map and scan its keys.
     * Space: O(n) - the map may store every value.
     *
     * @param nums input array
     * @return all lonely values in any order
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
     * Time Complexity: O(n),
     * Space Complexity: O(n)
     */
    public List<Integer> findLonelyStream(int[] nums) {
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int num : nums) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }

        return freqMap.keySet().stream()
                .filter(num -> freqMap.get(num) == 1 &&
                              !freqMap.containsKey(num - 1) &&
                              !freqMap.containsKey(num + 1))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}

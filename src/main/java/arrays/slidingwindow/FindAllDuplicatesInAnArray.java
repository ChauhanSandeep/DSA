package arrays.slidingwindow;

import java.util.*;

/**
 * Problem: Find All Duplicates in an Array
 *
 * Values are in the range 1..n, where n is the array length. Some values appear
 * twice and others once. Return every value that appears twice.
 *
 * Leetcode: https://leetcode.com/problems/find-all-duplicates-in-an-array/ (Medium)
 * Rating:   acceptance 76.0% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Array as hash table | Sign marking | In-place detection
 *
 * Example:
 *   Input:  nums = [4,3,2,7,8,2,3,1]
 *   Output: [2,3]
 *   Why:    visiting values 2 and 3 reaches already-negated marker positions.
 *
 * Follow-ups:
 *   1. What if values may be outside 1..n?
 *      Use a HashMap or HashSet instead of index-based marking.
 *   2. What if the array is read-only?
 *      Use extra space or a sorting copy because sign marking mutates input.
 *   3. What if values can appear more than twice?
 *      Count frequencies or use modular counting in the array if constraints allow.
 *
 * Related: Find All Numbers Disappeared in an Array (448), First Missing Positive (41).
 */
public class FindAllDuplicatesInAnArray {

    public static void main(String[] args) {
        FindAllDuplicatesInAnArray solver = new FindAllDuplicatesInAnArray();
        int[][] inputs = {{4, 3, 2, 7, 8, 2, 3, 1}, {1}, {1, 1, 2}};
        List<List<Integer>> expected = Arrays.asList(
            Arrays.asList(2, 3),
            Collections.emptyList(),
            Arrays.asList(1)
        );

        for (int i = 0; i < inputs.length; i++) {
            int[] input = inputs[i].clone();
            List<Integer> got = solver.findDuplicates(input);
            System.out.printf("nums=%s -> %s  expected=%s%n",
                Arrays.toString(inputs[i]), got, expected.get(i));
        }
    }


    /**
     * Intuition: values are guaranteed to be in 1..n, so value x can use index
     * x - 1 as its marker slot. The first visit negates that slot; seeing it
     * already negative means x has appeared before.
     *
     * Algorithm:
     *   1. For each value, map abs(value) to index abs(value) - 1.
     *   2. If nums[index] is negative, add the value to the duplicate list.
     *   3. Otherwise negate nums[index] to mark the value as seen.
     *
     * Time:  O(n) - each element is processed once.
     * Space: O(1) - excluding the returned list, marking happens in the input array.
     *
     * @param nums array containing values in the range 1..nums.length
     * @return values that appear twice
     */
    public List<Integer> findDuplicates(int[] nums) {
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < nums.length; i++) {
            int index = Math.abs(nums[i]) - 1;

            if (nums[index] < 0) {
                // Already visited, this is a duplicate
                result.add(Math.abs(nums[i]));
            } else {
                // First visit, mark as visited by negating
                nums[index] = -nums[index];
            }
        }

        return result;
    }

    /**
     * Alternative approach using cyclic sort concept.
     * Places each number at its correct position.
     */
    public List<Integer> findDuplicatesCyclicSort(int[] nums) {
        List<Integer> result = new ArrayList<>();

        // Place each number at its correct position
        for (int i = 0; i < nums.length; i++) {
            while (nums[i] != nums[nums[i] - 1]) {
                swap(nums, i, nums[i] - 1);
            }
        }

        // Find numbers not at their correct positions
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) {
                result.add(nums[i]);
            }
        }

        return result;
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    /**
     * HashMap approach for general case (when constraint 1≤nums[i]≤n doesn't hold).
     * Uses extra space but works for any integer values.
     */
    public List<Integer> findDuplicatesHashMap(int[] nums) {
        List<Integer> result = new ArrayList<>();
        Map<Integer, Integer> count = new HashMap<>();

        // Count frequencies
        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }

        // Collect duplicates
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            if (entry.getValue() == 2) {
                result.add(entry.getKey());
            }
        }

        return result;
    }

    /**
     * Set-based approach for detecting duplicates.
     * Simple and readable but uses O(n) extra space.
     */
    public List<Integer> findDuplicatesSet(int[] nums) {
        List<Integer> result = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();

        for (int num : nums) {
            if (seen.contains(num)) {
                result.add(num);
            } else {
                seen.add(num);
            }
        }

        return result;
    }

    /**
     * Mathematical approach using sum formula.
     * Only works when we know exactly which numbers should be present.
     */
    public List<Integer> findDuplicatesMath(int[] nums) {
        List<Integer> result = new ArrayList<>();
        int n = nums.length;

        // Calculate expected sum if all numbers 1 to n appear once
        long expectedSum = (long) n * (n + 1) / 2;

        // Calculate actual sum
        long actualSum = 0;
        for (int num : nums) {
            actualSum += num;
        }

        // The difference tells us the sum of duplicates
        // This approach is limited and doesn't identify individual duplicates
        // Keeping for educational purposes but not practical for this problem

        return findDuplicates(nums); // Fall back to optimal solution
    }

    /**
     * Bit manipulation approach using XOR properties.
     * Works when we know there's exactly one number appearing twice.
     */
    public List<Integer> findDuplicatesBitwise(int[] nums) {
        // This approach is more suitable for finding a single duplicate
        // For multiple duplicates, the index marking approach is better
        return findDuplicates(nums);
    }

    /**
     * Non-destructive version that preserves original array.
     * Uses additional space to avoid modifying input.
     */
    public List<Integer> findDuplicatesPreserveArray(int[] nums) {
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[nums.length + 1];

        for (int num : nums) {
            if (visited[num]) {
                result.add(num);
            } else {
                visited[num] = true;
            }
        }

        return result;
    }

    /**
     * Sorting-based approach.
     * Modifies array order but easy to understand.
     */
    public List<Integer> findDuplicatesSorting(int[] nums) {
        List<Integer> result = new ArrayList<>();
        Arrays.sort(nums);

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1] && (result.isEmpty() || result.get(result.size() - 1) != nums[i])) {
                result.add(nums[i]);
            }
        }

        return result;
    }
}

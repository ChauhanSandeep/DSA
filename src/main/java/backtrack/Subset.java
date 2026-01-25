package backtrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Subsets
 *
 * Given an integer array nums of unique elements, return all possible subsets (the power set).
 * The solution set must not contain duplicate subsets. Return the answer in any order.
 *
 * A subset is a collection that can be formed by including or excluding each element from
 * the original array. The empty subset and the full array itself are both valid subsets.
 *
 * Example:
 * Input: nums = [1,2,3]
 * Output: [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
 * Explanation: All 2^3 = 8 subsets of [1,2,3]. Each element can either be included or excluded,
 * creating binary choices that result in 2^n total subsets.
 *
 * LeetCode: https://leetcode.com/problems/subsets/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. How would you handle duplicate elements in the input array?
 *    Answer: Sort array first, then skip consecutive duplicates during backtracking (LeetCode 90).
 * 2. What if you need only subsets of a specific size k?
 *    Answer: Add size parameter to backtracking and stop when current subset reaches size k.
 * 3. How to optimize memory usage when generating millions of subsets?
 *    Answer: Use iterative approaches, streaming, or generate subsets on-demand without storing all.
 * 4. What if you need to find subsets with specific sum or property?
 *    Answer: Add constraint checking during backtracking with pruning for invalid branches.
 *
 * Related Problems:
 * - LeetCode 90: Subsets II (With duplicates)
 * - LeetCode 77: Combinations (Subsets of specific size)
 * - LeetCode 39: Combination Sum (Subsets with target sum)
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class Subset {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3};

        Subset subset = new Subset();
        // Backtracking Approach
        System.out.println("Backtracking Subsets: " + subset.subsetsRecursiveApproach(nums));

        // Iterative Approach
        System.out.println("Iterative Subsets: " + subset.subsetsIterativeApproach(nums));
    }

    /**
     * Generates all possible subsets using backtracking approach.
     *
     * Algorithm: Backtracking with Binary Choices
     * For each element at index i, we have exactly two choices:
     * 1. Exclude the element: move to next index without adding current element
     * 2. Include the element: add to current subset, recurse, then backtrack
     *
     * Key insights:
     * - Each element has binary choice (include/exclude) forming decision tree
     * - 2^n total subsets generated from n elements with binary choices
     * - Backtracking ensures all combinations explored without duplicates
     * - Copy current subset when base case reached to preserve state
     *
     * Time Complexity: O(n * 2^n) - generate 2^n subsets, each copy operation takes O(n)
     * Space Complexity: O(n) - recursion stack depth and temporary subset storage
     *
     * @param nums array of unique integers
     * @return list of all possible subsets
     */
    public List<List<Integer>> subsetsRecursiveApproach(int[] nums) {
        List<List<Integer>> allSubsets = new ArrayList<>();
        List<Integer> currentSubset = new ArrayList<>();

        generateSubsetsBacktrack(nums, 0, currentSubset, allSubsets);
        return allSubsets;
    }

    /**
     * Recursive helper function implementing backtracking logic.
     *
     * Algorithm Steps:
     * 1. Base case: if reached end of array, add current subset copy to results
     * 2. Exclude current element: recurse with next index
     * 3. Include current element: add to subset, recurse, then remove (backtrack)
     *
     * The order of operations is crucial:
     * - First explore "exclude" branch to maintain systematic traversal
     * - Then explore "include" branch with proper backtracking cleanup
     */
    private void generateSubsetsBacktrack(int[] nums, int startIndex, List<Integer> currentSubset,
        List<List<Integer>> allSubsets) {
        // Base case: processed all elements, add current subset to results
        if (startIndex == nums.length) {
            // Critical: create copy since currentSubset will be modified further
            allSubsets.add(new ArrayList<>(currentSubset));
            return;
        }

        // Choice 1: Exclude current element from subset
        generateSubsetsBacktrack(nums, startIndex + 1, currentSubset, allSubsets);

        // Choice 2: Include current element in subset
        currentSubset.add(nums[startIndex]);
        generateSubsetsBacktrack(nums, startIndex + 1, currentSubset, allSubsets);

        // Backtrack: restore state by removing added element
        currentSubset.remove(currentSubset.size() - 1);
    }

    /**
     * Cascading approach that builds subsets iteratively level by level.
     * Starts with empty subset and progressively adds each element to existing subsets.
     *
     * Algorithm: Iterative Expansion
     * 1. Start with empty subset [[]]
     * 2. For each element, double the subsets by adding element to copies of existing subsets
     * 3. After processing element i, we have all subsets using elements [0...i]
     *
     * Example progression for [1,2,3]:
     * Start: [[]]
     * Add 1: [[], [1]]
     * Add 2: [[], [1], [2], [1,2]]
     * Add 3: [[], [1], [2], [1,2], [3], [1,3], [2,3], [1,2,3]]
     *
     * Time Complexity: O(n * 2^n) - for each element, copy and extend 2^i existing subsets
     * Space Complexity: O(1) - excluding output space, builds result incrementally
     *
     * @param nums array of unique integers
     * @return list of all possible subsets
     */
    public List<List<Integer>> subsetsIterativeApproach(int[] nums) {
        List<List<Integer>> allSubsets = new ArrayList<>();
        allSubsets.add(new ArrayList<>()); // Start with empty subset

        // Process each element and expand existing subsets
        for (int currentElement : nums) {
            int currentSubsetSize = allSubsets.size();

            // For each existing subset, create new subset by adding current element
            for (int subsetIndex = 0; subsetIndex < currentSubsetSize; subsetIndex++) {
                List<Integer> existingSubset = allSubsets.get(subsetIndex);
                List<Integer> newSubset = new ArrayList<>(existingSubset);
                newSubset.add(currentElement);
                allSubsets.add(newSubset);
            }
        }

        return allSubsets;
    }
}

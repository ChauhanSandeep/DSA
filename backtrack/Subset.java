package backtrack;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode: https://leetcode.com/problems/subsets/
 *
 * Problem:
 * Given an integer array `nums` of unique elements, return all possible subsets (the power set).
 *
 * Approach:
 * 1️⃣ **Backtracking (Recursive)**
 *    - We either include or exclude each number to form subsets.
 *    - Time Complexity: **O(2^N)** (Each element can be included/excluded).
 *    - Space Complexity: **O(N)** (Recursive call stack).
 *
 * 2️⃣ **Iterative Approach**
 *    - Start with an empty subset `[]`, then iteratively add elements to existing subsets.
 *    - More memory-efficient (no recursion).
 */
public class Subset {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3};

        // Backtracking Approach
        System.out.println("Backtracking Subsets: " + subsets(nums));

        // Iterative Approach
        System.out.println("Iterative Subsets: " + subsetsIterative(nums));
    }

    /**
     * Generates all subsets using **backtracking**.
     *
     * @param nums Input array of unique integers.
     * @return List of all subsets.
     */
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        generateSubsets(nums, 0, new ArrayList<>(), result);
        return result;
    }

    /**
     * Recursive helper method for subset generation.
     */
    private static void generateSubsets(int[] nums, int index, List<Integer> currentSubset, List<List<Integer>> result) {
        // Add the current subset to the result
        result.add(new ArrayList<>(currentSubset));

        // Iterate through remaining elements
        for (int i = index; i < nums.length; i++) {
            // Include nums[i] in the current subset
            currentSubset.add(nums[i]);

            // Recursive call with the next index
            generateSubsets(nums, i + 1, currentSubset, result);

            // Backtrack: remove last element before next iteration
            currentSubset.remove(currentSubset.size() - 1);
        }
    }

    /**
     * Generates all subsets using **iterative approach**.
     *
     * @param nums Input array of unique integers.
     * @return List of all subsets.
     */
    public static List<List<Integer>> subsetsIterative(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>()); // Start with an empty subset

        for (int num : nums) {
            int size = result.size();
            for (int i = 0; i < size; i++) {
                List<Integer> newSubset = new ArrayList<>(result.get(i));
                newSubset.add(num);
                result.add(newSubset);
            }
        }
        return result;
    }
}

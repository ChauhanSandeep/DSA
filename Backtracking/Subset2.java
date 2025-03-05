package Backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode: https://leetcode.com/problems/subsets-ii/
 *
 * Problem:
 * Given an integer array `nums` that may contain duplicates, return all possible subsets (the power set).
 * Each subset must be **unique**.
 *
 * Approach:
 * 1️⃣ **Backtracking with Sorting**
 *    - Sort the array to easily identify duplicates.
 *    - Skip duplicate elements within the same recursive call.
 *    - Time Complexity: **O(2^N)** (Each element can be included/excluded).
 *    - Space Complexity: **O(N)** (Recursive call stack).
 */
public class Subset2 {
    public static void main(String[] args) {
        int[] nums = {2, 1, 2};

        // Generating subsets with duplicates handled
        List<List<Integer>> subsets = subsetsWithDup(nums);
        System.out.println(subsets);
    }

    /**
     * Generates all unique subsets, handling duplicate numbers.
     *
     * @param nums Input array with possible duplicates.
     * @return List of unique subsets.
     */
    public static List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);  // Sorting helps in identifying duplicates easily.
        generateUniqueSubsets(nums, 0, new ArrayList<>(), result);
        return result;
    }

    /**
     * Recursive function to generate subsets while skipping duplicates.
     */
    private static void generateUniqueSubsets(int[] nums, int index, List<Integer> currentSubset, List<List<Integer>> result) {
        // Add current subset to the result list
        result.add(new ArrayList<>(currentSubset));

        // Iterate through remaining elements
        for (int i = index; i < nums.length; i++) {
            // Skip duplicates within the same recursive call
            if (i > index && nums[i] == nums[i - 1]) continue;

            // Include current element
            currentSubset.add(nums[i]);

            // Recursive call with next index
            generateUniqueSubsets(nums, i + 1, currentSubset, result);

            // Backtrack: remove last element before next iteration
            currentSubset.remove(currentSubset.size() - 1);
        }
    }
}

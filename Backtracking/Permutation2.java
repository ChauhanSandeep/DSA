package Backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode: https://leetcode.com/problems/permutations-ii/
 *
 * Problem:
 * Given a collection of numbers, where some numbers may be duplicates,
 * return all unique permutations in any order.
 * For example: Input: [1,1,2]
 * Output: [[1,1,2],[1,2,1],[2,1,1]]
 * The number of unique combinations for array of length N and M1, M2 ... Mk duplicate combination is : N!/(M1!*M2!*...*Mk!)
 *
 * Approach:
 * - **Backtracking with Pruning**:
 *   - Sort the array to group duplicates together.
 *   - Use a `used[]` boolean array to track which elements are in the current permutation.
 *   - **Skip duplicates** by ensuring that each duplicate number is used **only once per recursion level**.
 *
 * Time Complexity:  O(N * N!) → N! permutations, O(N) time to copy each one.
 * Space Complexity: O(N)      → Extra space for recursion stack & `used[]` array.
 */
public class Permutation2 {

    public static void main(String[] args) {
        int[] arr = {3, 3, 0, 3};
        List<List<Integer>> permutations = permuteUnique(arr);
        System.out.println(permutations);
    }

    /**
     * Generates all unique permutations of an array with possible duplicates.
     *
     * @param nums The input array.
     * @return A list of unique permutations.
     */
    public static List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums); // Sort to handle duplicates effectively
        backtrack(result, new ArrayList<>(), nums, new boolean[nums.length]);
        return result;
    }

    /**
     * Backtracking helper function to generate unique permutations.
     *
     * @param result            The list storing all unique permutations.
     * @param currentPermutation The current permutation being built.
     * @param nums              The original sorted input array.
     * @param used              Boolean array to track used elements.
     */
    private static void backtrack(List<List<Integer>> result, List<Integer> currentPermutation, int[] nums, boolean[] used) {
        // Base case: if the current permutation is complete
        if (currentPermutation.size() == nums.length) {
            result.add(new ArrayList<>(currentPermutation));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            // Skip used elements or duplicate elements (if previous duplicate is not used)
            boolean isCurrentElementAlreadyUsed = used[i];
            boolean isDuplicateWithoutPreviousUsed = i > 0 && nums[i] == nums[i - 1] && !used[i - 1];
            if (isCurrentElementAlreadyUsed || isDuplicateWithoutPreviousUsed) {
                // 1. If the current element is already used then we cannot use it again
                // 2. If the current number is the same as the previous one and the previous one has not been used
                // in this recursive call, skip this to avoid generating a duplicate permutation
                continue;
            }

            // Include the current element in the permutation
            used[i] = true;
            currentPermutation.add(nums[i]);

            // Recursively generate further permutations
            backtrack(result, currentPermutation, nums, used);

            // Backtrack: Remove the last element to explore other options
            used[i] = false;
            currentPermutation.remove(currentPermutation.size() - 1);
        }
    }
}

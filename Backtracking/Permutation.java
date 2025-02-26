package Backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode: https://leetcode.com/problems/permutations/
 *
 * Problem:
 * Given an array of distinct integers, return all possible permutations.
 *
 * Approach:
 * - Uses **backtracking** to generate permutations recursively.
 * - The algorithm builds the permutation list dynamically.
 * - Elements are picked one by one, ensuring that each arrangement is unique.
 *
 * Time Complexity:  O(N!) → There are N! permutations.
 * Space Complexity: O(N)  → Extra space for recursion stack.
 */
public class Permutation {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        List<List<Integer>> permutations = permute(nums);
        System.out.println(permutations);
    }

    /**
     * Generates all permutations of a given array.
     *
     * @param nums Array of unique integers.
     * @return List of all possible permutations.
     */
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, new ArrayList<>(), result, new boolean[nums.length]);
        return result;
    }

    /**
     * Backtracking helper function to generate permutations.
     *
     * @param nums   The original array.
     * @param cur    Current permutation being constructed.
     * @param result The list of all valid permutations.
     * @param used   Boolean array to track used elements.
     */
    private static void backtrack(int[] nums, List<Integer> cur, List<List<Integer>> result, boolean[] used) {
        // Base case: if current list contains all elements
        if (cur.size() == nums.length) {
            result.add(new ArrayList<>(cur));
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue; // Skip already used elements

            // Choose element
            cur.add(nums[i]);
            used[i] = true;

            // Explore further
            backtrack(nums, cur, result, used);

            // Backtrack (undo the choice)
            cur.remove(cur.size() - 1);
            used[i] = false;
        }
    }
}

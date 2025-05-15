package Backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode: https://leetcode.com/problems/permutations/
 *
 * Problem:
 * Given an array of distinct integers, return all possible permutations.
 * For example : Input: [1,2,3]
 * Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 *
 * Approach:
 * - Uses **backtracking** to generate permutations recursively.
 * - The algorithm builds the permutation list dynamically.
 * - Elements are picked one by one, ensuring that each arrangement is unique.
 *
 */
public class Permutation {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        List<List<Integer>> permutations = permute(nums);
        System.out.println(permutations);
    }

    /**
     * Steps:
     * 1. If the current permutation is complete (size equals nums.length), add it to the result.
     * 2. Iterate through each number in nums:
     *    - If the number is already used, skip it.
     *    - Otherwise, add the number to the current permutation and mark it as used.
     *    - Recursively call backtrack to continue building the permutation.
     *
     *  * Time Complexity:  O(N!) → There are N! permutations.
     *  * Space Complexity: O(N)  → Extra space for recursion stack.
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

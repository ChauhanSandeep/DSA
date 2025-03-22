package recursion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Problem: Find all unique combinations where the numbers sum up to a target.
 * Each number can be used **only once** in a combination.
 *
 * Approach:
 * - Use **backtracking** with sorting to handle duplicates.
 * - At each step, either **include or exclude** the current element.
 * - **Sort input array** to easily skip duplicates while iterating.
 * - Use **DFS (Depth-First Search)** to explore different combinations.
 *
 * Time Complexity: O(2^N) (Exponential in the worst case)
 * Space Complexity: O(N) (Recursion stack depth)
 *
 * LeetCode Link: https://leetcode.com/problems/combination-sum-ii/
 */
public class CombinationSum2 {

    public static void main(String[] args) {
        List<Integer> candidates = Stream.of(10, 1, 2, 7, 6, 1, 5).collect(Collectors.toList());
        List<List<Integer>> result = findCombinationSum2(new ArrayList<>(candidates), 8);
        System.out.println(result);
    }

    /**
     * Finds all unique combinations that sum up to the target.
     *
     * @param candidates List of positive integers (may contain duplicates).
     * @param target     Target sum to achieve.
     * @return List of all possible unique combinations.
     */
    public static List<List<Integer>> findCombinationSum2(List<Integer> candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (candidates == null || candidates.isEmpty()) return result;

        // Sort input to handle duplicates easily
        Collections.sort(candidates);
        backtrack(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }

    /**
     * Recursive backtracking function to generate valid combinations.
     *
     * @param candidates Input list of numbers.
     * @param target     Remaining sum to be achieved.
     * @param index      Current index in the list.
     * @param current    Temporary list storing the current combination.
     * @param result     Final list containing all valid combinations.
     */
    private static void backtrack(List<Integer> candidates, int target, int index, List<Integer> current, List<List<Integer>> result) {
        if (target == 0) {
            result.add(new ArrayList<>(current)); // Add valid combination
            return;
        }

        for (int i = index; i < candidates.size(); i++) {
            // Skip duplicate elements (Only pick the first occurrence)
            if (i > index && candidates.get(i).equals(candidates.get(i - 1))) continue;

            // Stop if the number is greater than the remaining target
            if (candidates.get(i) > target) break;

            // Include the number
            current.add(candidates.get(i));
            // Recur with the next index (since we can't reuse elements)
            backtrack(candidates, target - candidates.get(i), i + 1, current, result);
            // Backtrack (undo the choice)
            current.remove(current.size() - 1);
        }
    }
}

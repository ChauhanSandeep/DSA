package trees.dfs;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

/**
 * Problem: Path Sum III
 *
 * Count downward paths whose node values sum to a target. A path may start at
 * any node and end at any descendant, but it must always move from parent to
 * child.
 *
 * Leetcode: https://leetcode.com/problems/path-sum-iii/ (Medium)
 * Rating:   not available
 * Pattern:  Trees | DFS | Prefix sums | Backtracking counts
 *
 * Example:
 *   Input:  root = [10,5,-3,3,2,null,11,3,-2,null,1], targetSum = 8
 *   Output: 3
 *   Why:    the matching paths are 5 -> 3, 5 -> 2 -> 1, and -3 -> 11.
 *
 * Follow-ups:
 *   1. How would you return the actual paths?
 *      Store path indices for prefix sums and copy the matching suffixes.
 *   2. What if many target queries are asked on the same tree?
 *      Precompute root-to-node prefix sums, but arbitrary descendant queries still need indexing.
 *   3. What if values can overflow int sums?
 *      Use long keys for cumulative sums and differences.
 *
 * Related: Path Sum II (113), Subarray Sum Equals K (560).
 */
public class PathSum3 {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(5);
        root.right = new TreeNode(-3);
        root.left.left = new TreeNode(3);
        root.left.right = new TreeNode(2);
        root.right.right = new TreeNode(11);
        root.left.left.left = new TreeNode(3);
        root.left.left.right = new TreeNode(-2);
        root.left.right.right = new TreeNode(1);

        TreeNode single = new TreeNode(1);
        System.out.printf("root=%s targetSum=%d -> %d  expected=%d%n",
            Arrays.toString(new int[] {10, 5, -3, 3, 2, 11, 3, -2, 1}), 8,
            new PathSum3().pathSum(root, 8), 3);
        System.out.printf("root=%s targetSum=%d -> %d  expected=%d%n",
            Arrays.toString(new int[] {1}), 1, new PathSum3().pathSum(single, 1), 1);
    }


    // Variable to store the result (number of paths with sum equal to targetSum)
    private int result;

        /**
     * Intuition: along the current root-to-node path, two prefix sums that differ
     * by targetSum define one downward path with that sum. sumFrequencyMap stores
     * only prefixes from the active recursion path, then backtracking removes the
     * current prefix before returning to siblings.
     *
     * Algorithm:
     *   1. Start DFS with currSum 0 and an empty prefix-frequency map.
     *   2. Add root.val to currSum and count direct root-starting matches.
     *   3. Add frequencies of currSum - targetSum to result.
     *   4. Record currSum, recurse left then right, and remove currSum while backtracking.
     *
     * Time:  O(n) - each node performs constant map work once.
     * Space: O(h) - the map and recursion hold prefixes on the active path.
     *
     * @param root root of the binary tree
     * @param sum target path sum
     * @return number of downward paths that sum to sum
     */
    public int pathSum(TreeNode root, int sum) {
        // Initialize the result and hashmap to store cumulative sums
        Map<Integer, Integer> map = new HashMap<>(); // <sum, frequency>

        // Start DFS traversal from the root node
        pathSum(root, 0, sum, map);

        // Return the total result
        return result;
    }

        // DFS helper that maintains prefix-sum frequencies for the current path only.
    private void pathSum(TreeNode root, int currSum, int targetSum, Map<Integer, Integer> sumFrequencyMap) {
        if (root == null) {
            return;
        }

        // Update the cumulative sum by adding the current node's value
        currSum += root.val;

        // If the current sum equals the target sum, increment the result count
        if (currSum == targetSum) {
            result++;
        }

        // If there exists a previous sum that, when subtracted from the current sum, equals the target sum, it means a valid path is found
        int complementarySum = currSum - targetSum;
        if (sumFrequencyMap.containsKey(complementarySum)) {
            result += sumFrequencyMap.get(complementarySum);
        }

        // Add the current sum to the sumFrequencyMap (for future paths to use)
        sumFrequencyMap.put(currSum, sumFrequencyMap.getOrDefault(currSum, 0) + 1);

        // Traverse the left and right subtrees
        pathSum(root.left, currSum, targetSum, sumFrequencyMap);
        pathSum(root.right, currSum, targetSum, sumFrequencyMap);

        // After traversing both children, backtrack by removing the current sum from the sumFrequencyMap
        sumFrequencyMap.put(currSum, sumFrequencyMap.get(currSum) - 1);
        if (sumFrequencyMap.get(currSum) == 0) {
            sumFrequencyMap.remove(currSum);
        }
    }

    // TreeNode class to represent each node in the binary tree
    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
            this.left = this.right = null;
        }
    }
}

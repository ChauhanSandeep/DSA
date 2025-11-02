package com.leetcode.tree;

/**
 * Problem: Binary Tree Maximum Path Sum
 *
 * A path in a binary tree is a sequence of nodes where each pair of adjacent nodes
 * has an edge connecting them. A node can only appear in the sequence at most once.
 * Note that the path does not need to pass through the root.
 *
 * The path sum of a path is the sum of the node's values in the path.
 * Given the root of a binary tree, return the maximum path sum of any non-empty path.
 *
 * Example:
 * Input: root = [1,2,3]
 * Output: 6
 * Explanation: The optimal path is 2 -> 1 -> 3 with a path sum of 2 + 1 + 3 = 6.
 *
 * Constraints:
 * - The number of nodes in the tree is in the range [1, 3 * 10^4]
 * - -1000 <= Node.val <= 1000
 *
 * LeetCode Problem: https://leetcode.com/problems/binary-tree-maximum-path-sum
 *
 * Follow-up Questions:
 *
 * 1. What if the path must start at a leaf and end at a leaf?
 *    Answer: Modify base case to return large negative value for null nodes instead
 *    of 0. Track whether current node is leaf. Only update max when both subtrees
 *    are non-null (ensuring path spans from leaf to leaf through current node).
 *
 * 2. How would you find the actual path, not just the sum?
 *    Answer: Store node references along with sums during DFS. When updating global
 *    maximum, also store the current path (left path + node + right path). Return
 *    the path corresponding to maximum sum found.
 *
 * 3. What if negative values are not allowed in the path?
 *    Answer: Similar to current solution where we use Math.max(0, childSum). Already
 *    handles this by ignoring negative contributions. The solution naturally excludes
 *    negative paths unless all values are negative.
 *
 * 4. How would you handle if each node has a weight/cost to traverse through it?
 *    Answer: Add weight parameter to nodes. Subtract traversal cost when calculating
 *    path sums. The DFS logic remains similar but accounts for costs in addition to
 *    node values.
 *
 * 5. What if you need to find k paths with maximum sums instead of just one?
 *    Answer: Use a min-heap of size k to track top k path sums. During DFS, whenever
 *    calculating a path sum, add to heap. Keep heap size at k by removing minimum
 *    when size exceeds k. Return heap contents at end.
 */
public class BinaryTreeMaximumPathSum {
    /**
     * Finds maximum path sum using array return values.
     * Returns int[2] where:
     * - index 0: maximum path sum in entire subtree (can be through node or below)
     * - index 1: maximum path sum that can extend to parent (single branch only)
     *
     * Algorithm:
     * 1. For each node, calculate both values from left and right subtrees
     * 2. maxPathSum = max of (path through node, left subtree max, right subtree max)
     * 3. maxBranchSum = node + max(left branch, right branch, 0)
     * 4. Return both values as array
     *
     * Key insight: We need to track two different values:
     * - Global maximum (can split at any node)
     * - Branch maximum (can only go in one direction for parent)
     *
     * Time Complexity: O(N) where N is number of nodes. Visit each node once.
     * Space Complexity: O(H) where H is tree height for recursion stack.
     *
     * @param root root of the binary tree
     * @return maximum path sum in the tree
     */
    public int maxPathSum(TreeNode root) {
        int[] result = calculateMaxPath(root);
        return result[0]; // Return the global maximum
    }

    /**
     * Calculate maximum path sums for a subtree.
     *
     * @param node current node being processed
     * @return int[2] where:
     *         [0] = maximum path sum in this subtree (global max for this subtree)
     *         [1] = maximum sum of single branch extending to parent
     */
    private int[] calculateMaxPath(TreeNode node) {
        // Base case: null node contributes nothing
        if (node == null) {
            return new int[]{Integer.MIN_VALUE, 0};
        }

        // Recursively get results from left and right subtrees
        int[] leftResult = calculateMaxPath(node.left);
        int[] rightResult = calculateMaxPath(node.right);

        int leftMaxPath = leftResult[0];      // Best path in left subtree
        int leftMaxBranch = leftResult[1];    // Best branch from left child

        int rightMaxPath = rightResult[0];    // Best path in right subtree
        int rightMaxBranch = rightResult[1];  // Best branch from right child

        // Calculate best single branch going through current node
        // Can go left, right, or neither (just node itself)
        int leftBranch = Math.max(0, leftMaxBranch);
        int rightBranch = Math.max(0, rightMaxBranch);
        int maxBranchToParent = node.val + Math.max(leftBranch, rightBranch);

        // Calculate best path going through current node (can use both branches)
        int pathThroughNode = node.val + leftBranch + rightBranch;

        // Global maximum is best of:
        // 1. Path through current node (using both branches)
        // 2. Best path in left subtree
        // 3. Best path in right subtree
        int maxPathInSubtree = Math.max(pathThroughNode,
            Math.max(leftMaxPath, rightMaxPath));

        return new int[]{maxPathInSubtree, maxBranchToParent};
    }

    /**
     * Alternative approach using array to pass maximum value by reference.
     * Avoids instance variable for cleaner encapsulation.
     *
     * Algorithm:
     * Same as above but uses array to track maximum across recursive calls.
     *
     * Time Complexity: O(N) where N is number of nodes.
     *
     * Space Complexity: O(H) for recursion stack where H is tree height.
     *
     * @param root root of the binary tree
     * @return maximum path sum in the tree
     */
    public int maxPathSumArray(TreeNode root) {
        int[] max = new int[]{Integer.MIN_VALUE};
        calculateMaxPathWithArray(root, max);
        return max[0];
    }

    private int calculateMaxPathWithArray(TreeNode node, int[] max) {
        if (node == null) {
            return 0;
        }

        int leftMax = Math.max(0, calculateMaxPathWithArray(node.left, max));
        int rightMax = Math.max(0, calculateMaxPathWithArray(node.right, max));

        max[0] = Math.max(max[0], leftMax + node.val + rightMax);

        return node.val + Math.max(leftMax, rightMax);
    }

    /**
     * Verbose version with detailed comments for educational purposes.
     *
     * Algorithm:
     * Same logic but with extensive inline documentation explaining each step.
     *
     * Time Complexity: O(N) where N is number of nodes.
     *
     * Space Complexity: O(H) for recursion stack where H is tree height.
     *
     * @param root root of the binary tree
     * @return maximum path sum in the tree
     */
    public int maxPathSumVerbose(TreeNode root) {
        int[] globalMax = new int[]{Integer.MIN_VALUE};
        dfsMaxPath(root, globalMax);
        return globalMax[0];
    }

    private int dfsMaxPath(TreeNode node, int[] globalMax) {
        // Base case: null node contributes 0 to any path
        if (node == null) {
            return 0;
        }

        // Get maximum gain from left subtree
        // Math.max(0, ...) means we ignore negative paths
        int leftGain = Math.max(0, dfsMaxPath(node.left, globalMax));

        // Get maximum gain from right subtree
        int rightGain = Math.max(0, dfsMaxPath(node.right, globalMax));

        // Calculate path sum going through current node
        // This creates a path: left subtree -> current node -> right subtree
        int currentPathSum = leftGain + node.val + rightGain;

        // Update global maximum if current path is better
        globalMax[0] = Math.max(globalMax[0], currentPathSum);

        // Return maximum gain if parent uses this node in its path
        // Can only choose one branch (left or right), not both
        // Adding node.val because parent needs this node's contribution
        return node.val + Math.max(leftGain, rightGain);
    }

    /**
     * Definition for a binary tree node.
     */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}

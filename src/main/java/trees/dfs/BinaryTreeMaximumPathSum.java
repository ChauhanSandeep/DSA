package trees.dfs;

/**
 * Problem: Binary Tree Maximum Path Sum
 *
 * A path can start and end at any nodes as long as it follows parent-child edges
 * and never repeats a node. Return the largest possible sum of node values along
 * any non-empty path in the tree.
 *
 * Leetcode: https://leetcode.com/problems/binary-tree-maximum-path-sum/ (Hard)
 * Rating:   not available
 * Pattern:  Trees | DFS | Postorder DP | Split path versus extendable path
 *
 * Example:
 *   Input:  root = [-10,9,20,null,null,15,7]
 *   Output: 42
 *   Why:    the best path is 15 -> 20 -> 7, which sums to 42.
 *
 * Follow-ups:
 *   1. How would you return the path, not just the sum?
 *      Store the best branch nodes and snapshot them when the global sum improves.
 *   2. What if the path must start and end at leaves?
 *      Only update a split path when both child branches exist, with leaf-aware bases.
 *   3. What if you need the top k path sums?
 *      Keep candidate path sums in a bounded heap while computing branch gains.
 *
 * Related: Diameter of Binary Tree (543), Path Sum III (437).
 */
public class BinaryTreeMaximumPathSum {

    public static void main(String[] args) {
        BinaryTreeMaximumPathSum solver = new BinaryTreeMaximumPathSum();
        TreeNode simple = new TreeNode(1, new TreeNode(2), new TreeNode(3));
        TreeNode mixed = new TreeNode(-10);
        mixed.left = new TreeNode(9);
        mixed.right = new TreeNode(20, new TreeNode(15), new TreeNode(7));

        System.out.printf("root=%s -> %d  expected=%d%n",
            "[1,2,3]", solver.maxPathSum(simple), 6);
        System.out.printf("root=%s -> %d  expected=%d%n",
            "[-10,9,20,null,null,15,7]", solver.maxPathSum(mixed), 42);
    }

        /**
     * Intuition: every node has two different answers. One path may split
     * through the node and use both children, but only a one-sided branch can be
     * extended upward to the parent. calculateMaxPath returns both values so the
     * parent can combine them correctly.
     *
     * Algorithm:
     *   1. Recursively get the best complete path and extendable branch from each child.
     *   2. Drop negative child branches by comparing each gain with zero.
     *   3. Compute the best branch that can extend to the parent.
     *   4. Compute the best complete path in this subtree, including a split through node.
     *
     * Time:  O(n) - each node is evaluated once.
     * Space: O(h) - recursion stack follows the tree height.
     *
     * @param root root of the binary tree
     * @return maximum sum of any non-empty path
     */
    public int maxPathSum(TreeNode root) {
        int[] result = calculateMaxPath(root);
        return result[0]; // Return the global maximum
    }

        // Returns {best path in subtree, best single branch that can extend upward}.
    private int[] calculateMaxPath(TreeNode node) {
        // Base case: null node contributes nothing
        if (node == null) {
            return new int[]{Integer.MIN_VALUE, 0};
        }

        // Recursively get results from left and right subtrees
        int[] leftResult = calculateMaxPath(node.left);
        int[] rightResult = calculateMaxPath(node.right);

        int bestPathInLeftSubtree = leftResult[0];      // Best complete path in left subtree
        int leftGainToParent = leftResult[1];           // Gain from left child if we continue upward

        int bestPathInRightSubtree = rightResult[0];    // Best complete path in right subtree
        int rightGainToParent = rightResult[1];         // Gain from right child if we continue upward

        // Calculate best single branch going through current node
        // Can go left, right, or neither (just node itself)
        int leftContribution = Math.max(0, leftGainToParent);
        int rightContribution = Math.max(0, rightGainToParent);
        int maxSinglePathToParent = node.val + Math.max(leftContribution, rightContribution);

        // Calculate best path going through current node (can use both branches)
        int pathThroughCurrentNode = node.val + leftContribution + rightContribution;

        // Global maximum is best of:
        // 1. Path through current node (using both branches)
        // 2. Best path in left subtree
        // 3. Best path in right subtree
        int bestPathInCurrentSubtree = Math.max(pathThroughCurrentNode,
            Math.max(bestPathInLeftSubtree, bestPathInRightSubtree));

        return new int[]{bestPathInCurrentSubtree, maxSinglePathToParent};
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

package trees.dfs;

import trees.TreeNode;
import trees.Node;
import java.util.*;

/**
 * Problem: Path Sum II
 *
 * Given a binary tree and targetSum, return all root-to-leaf paths whose values
 * add up to targetSum. Each returned path contains the node values in top-down
 * order.
 *
 * Leetcode: https://leetcode.com/problems/path-sum-ii/ (Medium)
 * Rating:   not available
 * Pattern:  Trees | DFS | Backtracking | Root-to-leaf sum
 *
 * Example:
 *   Input:  root = [5,4,8,11,null,13,4,7,2,null,null,5,1], targetSum = 22
 *   Output: [[5,4,11,2], [5,8,4,5]]
 *   Why:    those are the only root-to-leaf paths whose values sum to 22.
 *
 * Follow-ups:
 *   1. What if paths may start and end anywhere downward?
 *      Use prefix sums as in Path Sum III.
 *   2. Can this be solved iteratively?
 *      Push node, remaining sum, and copied path states on a stack.
 *   3. What if all node values are positive and target is small?
 *      Prune a branch once its remaining sum becomes negative.
 *
 * Related: Path Sum (112), Path Sum III (437), Binary Tree Paths (257).
 */
public class PathSum2 {

    public static void main(String[] args) {
        PathSum2 solver = new PathSum2();
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(4);
        root.right = new TreeNode(8);
        root.left.left = new TreeNode(11);
        root.left.left.left = new TreeNode(7);
        root.left.left.right = new TreeNode(2);
        root.right.left = new TreeNode(13);
        root.right.right = new TreeNode(4);
        root.right.right.left = new TreeNode(5);
        root.right.right.right = new TreeNode(1);

        System.out.printf("root=%s targetSum=%d -> %s  expected=%s%n",
            "[5,4,8,11,null,13,4,7,2,null,null,5,1]", 22,
            solver.pathSum(root, 22), "[[5, 4, 11, 2], [5, 8, 4, 5]]");
        System.out.printf("root=%s targetSum=%d -> %s  expected=%s%n",
            "[]", 0, solver.pathSum(null, 0), "[]");
    }


        /**
     * Intuition: during a DFS, currentPath is the exact path from root to the
     * current node and remainingSum is what that path still needs. A path can be
     * accepted only at a leaf; copying currentPath there preserves it before
     * backtracking removes nodes for sibling branches.
     *
     * Algorithm:
     *   1. Start DFS with an empty currentPath and the full targetSum.
     *   2. Add each node to currentPath and subtract its value from remainingSum.
     *   3. If the node is a leaf and remainingSum is zero, copy currentPath to result.
     *   4. Otherwise recurse left then right, then remove the current node.
     *
     * Time:  O(n * h) - each valid path copy can contain up to h node values.
     * Space: O(h) - recursion and currentPath hold one branch, excluding output.
     *
     * @param root root of the binary tree
     * @param targetSum required sum from root to leaf
     * @return all root-to-leaf paths whose values sum to targetSum
     */
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> currentPath = new ArrayList<>();

        dfsWithBacktracking(root, targetSum, currentPath, result);
        return result;
    }

    // Explores root-to-leaf paths while adding and removing the current node.
    private void dfsWithBacktracking(TreeNode node, int remainingSum, List<Integer> currentPath,
        List<List<Integer>> result) {
        if (node == null) {
            return;
        }

        // Add current node to path
        currentPath.add(node.val);
        remainingSum -= node.val;

        // If leaf node and sum matches, add path to result
        if (node.left == null && node.right == null && remainingSum == 0) {
            result.add(new ArrayList<>(currentPath)); // Create copy
        } else {
            // Continue to children
            dfsWithBacktracking(node.left, remainingSum, currentPath, result);
            dfsWithBacktracking(node.right, remainingSum, currentPath, result);
        }

        // Backtrack: remove current node
        currentPath.remove(currentPath.size() - 1);
    }

    /**
     * Iterative approach using stack.
     *
     * Algorithm: DFS with Stack
     * - Use a stack to simulate recursion
     * - Each stack entry holds current node, remaining sum, and path so far
     * - Process nodes until stack is empty
     * - When leaf is reached with sum = 0, add path to result
     *
     * Time Complexity: O(n²)
     * Space Complexity: O(n²)
     */
    public List<List<Integer>> pathSumIterative(TreeNode root, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();

        if (root == null) {
            return result;
        }

        Stack<PathState> stack = new Stack<>();
        List<Integer> initialPath = new ArrayList<>();
        initialPath.add(root.val);
        stack.push(new PathState(root, targetSum - root.val, initialPath));

        while (!stack.isEmpty()) {
            PathState current = stack.pop();
            TreeNode node = current.node;
            int remainingSum = current.remainingSum;
            List<Integer> path = current.path;

            // If leaf and sum matches
            if (node.left == null && node.right == null && remainingSum == 0) {
                result.add(new ArrayList<>(path));
                continue;
            }

            // Add children to stack
            if (node.left != null) {
                List<Integer> leftPath = new ArrayList<>(path);
                leftPath.add(node.left.val);
                stack.push(new PathState(node.left, remainingSum - node.left.val, leftPath));
            }

            if (node.right != null) {
                List<Integer> rightPath = new ArrayList<>(path);
                rightPath.add(node.right.val);
                stack.push(new PathState(node.right, remainingSum - node.right.val, rightPath));
            }
        }

        return result;
    }

    // Helper class for iterative approach
    private static class PathState {
        TreeNode node;
        int remainingSum;
        List<Integer> path;

        PathState(TreeNode node, int remainingSum, List<Integer> path) {
            this.node = node;
            this.remainingSum = remainingSum;
            this.path = path;
        }
    }

    // Definition for a binary tree node
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
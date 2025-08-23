package Tree;

import java.util.*;

/**
 * Lowest Common Ancestor Of A Binary Tree
 * 
 * Problem Statement:
 * Given a binary tree, find the lowest common ancestor (LCA) of two given nodes in the tree.
 * The LCA is defined as the lowest node that has both p and q as descendants 
 * (where we allow a node to be a descendant of itself).
 * 
 * Example:
 * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
 * Output: 3
 * Explanation: The LCA of nodes 5 and 1 is 3
 * 
 * LeetCode Link: https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree
 * 
 * Follow-up Questions:
 * 1. What if nodes might not exist? - Add existence check before LCA search
 * 2. What about LCA in BST? - Use BST properties for O(h) solution
 * 3. How to find LCA of multiple nodes? - Extend algorithm to handle array of nodes
 */
public class LowestCommonAncestorOfABinaryTree {

    /**
     * Finds LCA using recursive DFS approach.
     * 
     * Algorithm: Post-order DFS with early termination
     * - If current node is null, return null
     * - If current node is p or q, return current node
     * - Recursively search in left and right subtrees
     * - If both subtrees return non-null, current node is LCA
     * - If only one subtree returns non-null, return that result
     * 
     * Time Complexity: O(n) - visit each node at most once
     * Space Complexity: O(h) - recursion stack depth
     * 
     * @param root root of binary tree
     * @param p first target node
     * @param q second target node
     * @return lowest common ancestor of p and q
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // Base case: if root is null or root is one of the target nodes
        if (root == null || root == p || root == q) {
            return root;
        }

        // Search in left and right subtrees
        TreeNode leftResult = lowestCommonAncestor(root.left, p, q);
        TreeNode rightResult = lowestCommonAncestor(root.right, p, q);

        // If both subtrees found a target, current node is LCA
        if (leftResult != null && rightResult != null) {
            return root;
        }

        // Return the non-null result (if any)
        return leftResult != null ? leftResult : rightResult;
    }

    /**
     * Alternative approach using parent pointers.
     * 
     * Algorithm: Build parent map and find intersection
     * - Build map of node -> parent for all nodes
     * - Traverse ancestors of p and mark them
     * - Traverse ancestors of q until we find marked node
     * 
     * Time Complexity: O(n) - to build parent map + O(h) to find LCA
     * Space Complexity: O(n) - parent map storage
     */
    public TreeNode lowestCommonAncestorWithParents(TreeNode root, TreeNode p, TreeNode q) {
        // Build parent map
        Map<TreeNode, TreeNode> parentMap = new HashMap<>();
        buildParentMap(root, null, parentMap);

        // Mark all ancestors of p
        Set<TreeNode> ancestors = new HashSet<>();
        TreeNode current = p;
        while (current != null) {
            ancestors.add(current);
            current = parentMap.get(current);
        }

        // Find first common ancestor in q's path
        current = q;
        while (current != null) {
            if (ancestors.contains(current)) {
                return current;
            }
            current = parentMap.get(current);
        }

        return null;
    }

    // Helper to build parent map
    private void buildParentMap(TreeNode node, TreeNode parent, Map<TreeNode, TreeNode> parentMap) {
        if (node == null) {
            return;
        }

        parentMap.put(node, parent);
        buildParentMap(node.left, node, parentMap);
        buildParentMap(node.right, node, parentMap);
    }

    /**
     * Approach using path tracking.
     * 
     * Find paths from root to both nodes, then find divergence point.
     * Less efficient but demonstrates alternative thinking.
     */
    public TreeNode lowestCommonAncestorWithPaths(TreeNode root, TreeNode p, TreeNode q) {
        List<TreeNode> pathToP = new ArrayList<>();
        List<TreeNode> pathToQ = new ArrayList<>();

        findPath(root, p, pathToP);
        findPath(root, q, pathToQ);

        TreeNode lca = null;
        int minLength = Math.min(pathToP.size(), pathToQ.size());

        for (int i = 0; i < minLength; i++) {
            if (pathToP.get(i) == pathToQ.get(i)) {
                lca = pathToP.get(i);
            } else {
                break;
            }
        }

        return lca;
    }

    // Helper to find path from root to target
    private boolean findPath(TreeNode root, TreeNode target, List<TreeNode> path) {
        if (root == null) {
            return false;
        }

        path.add(root);

        if (root == target) {
            return true;
        }

        if (findPath(root.left, target, path) || findPath(root.right, target, path)) {
            return true;
        }

        path.remove(path.size() - 1);
        return false;
    }

    /**
     * Optimized approach with existence validation.
     * 
     * First check if both nodes exist, then find LCA.
     * Useful when nodes might not be in the tree.
     */
    public TreeNode lowestCommonAncestorSafe(TreeNode root, TreeNode p, TreeNode q) {
        // First verify both nodes exist
        if (!nodeExists(root, p) || !nodeExists(root, q)) {
            return null;
        }

        return lowestCommonAncestor(root, p, q);
    }

    // Helper to check if node exists in tree
    private boolean nodeExists(TreeNode root, TreeNode target) {
        if (root == null) {
            return false;
        }

        if (root == target) {
            return true;
        }

        return nodeExists(root.left, target) || nodeExists(root.right, target);
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
package trees.binarysearchtree;

import trees.Node;
import trees.TreeNode;
import java.util.*;

/**
 * Problem: Lowest Common Ancestor of a Binary Tree
 *
 * Given a binary tree and two nodes, find the lowest node that has both nodes as
 * descendants. A node is allowed to be a descendant of itself, so one target can
 * be the answer when it contains the other target below it.
 *
 * Leetcode: https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Trees | Postorder recursion | Split-point detection
 *
 * Example:
 *   Input:  root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
 *   Output: 3
 *   Why:    5 is in the left subtree of 3 and 1 is in the right subtree of 3.
 *
 * Follow-ups:
 *   1. What if one or both nodes may be missing?
 *      Return found-node counts along with the candidate LCA.
 *   2. How would you answer many LCA queries?
 *      Preprocess depth and ancestors with binary lifting or Euler tour plus RMQ.
 *   3. What if parent pointers are available?
 *      Walk ancestors with a set or align depths and move upward together.
 *   4. What changes for a BST?
 *      Use value ordering to walk only one path from the root.
 *
 * Related: Lowest Common Ancestor of a BST (235), LCA of Binary Tree III (1650).
 */
public class LowestCommonAncestorOfABinaryTree {

    public static void main(String[] args) {
        LowestCommonAncestorOfABinaryTree solver = new LowestCommonAncestorOfABinaryTree();
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(5);
        root.right = new TreeNode(1);
        root.left.left = new TreeNode(6);
        root.left.right = new TreeNode(2);
        root.right.left = new TreeNode(0);
        root.right.right = new TreeNode(8);

        TreeNode first = solver.lowestCommonAncestor(root, root.left, root.right);
        TreeNode second = solver.lowestCommonAncestor(root, root.left, root.left.right);
        System.out.printf("p=5 q=1 -> %d  expected=3%n", first.val);
        System.out.printf("p=5 q=2 -> %d  expected=5%n", second.val);
    }


    /**
     * Definition for a binary tree node.
     */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

        /**
     * Intuition: each recursive call reports whether its subtree contains p, q, or an
     * already discovered ancestor. If left and right both report a target, the current
     * root is the first place where the two search paths meet.
     *
     * Algorithm:
     *   1. Return root for null, p, or q base cases.
     *   2. Recurse into root.left.
     *   3. Recurse into root.right.
     *   4. If both sides are non-null, return root; otherwise return the non-null side.
     *
     * Time:  O(n) - each node is visited at most once.
     * Space: O(h) - recursion depth is the tree height.
     *
     * @param root current tree root
     * @param p first target node
     * @param q second target node
     * @return lowest common ancestor when both targets are present
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) {
            return root;
        }

        TreeNode leftResult = lowestCommonAncestor(root.left, p, q);
        TreeNode rightResult = lowestCommonAncestor(root.right, p, q);

        if (leftResult != null && rightResult != null) {
            return root;
        }

        return leftResult != null ? leftResult : rightResult;
    }

        /**
     * Records the path from root to target, removing nodes while backtracking.
     */
    public TreeNode lowestCommonAncestorPathBased(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return null;
        }

        List<TreeNode> pathToP = new ArrayList<>();
        List<TreeNode> pathToQ = new ArrayList<>();

        if (!findPath(root, p, pathToP) || !findPath(root, q, pathToQ)) {
            return null;
        }

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

    // Helper method to find path from root to target node
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
}

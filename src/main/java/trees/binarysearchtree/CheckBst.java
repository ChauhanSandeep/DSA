package trees.binarysearchtree;

import trees.Node;
import trees.TreeNode;

/**
 * Problem: Validate a Binary Search Tree
 *
 * Given a binary tree, decide whether every node obeys the strict BST ordering
 * rules. Each node must be greater than every value allowed on its left side and
 * smaller than every value allowed on its right side.
 *
 * Leetcode: https://leetcode.com/problems/validate-binary-search-tree/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Trees | BST | Recursive min-max bounds
 *
 * Example:
 *   Input:  root = [4,2,5,1,3]
 *   Output: true
 *   Why:    1 < 2 < 3 < 4 < 5, and every subtree also stays within its bounds.
 *
 * Follow-ups:
 *   1. What if duplicates are allowed on one side?
 *      Change the strict boundary checks to match the duplicate policy.
 *   2. How would you validate a very deep tree?
 *      Use iterative inorder traversal to avoid recursion-stack overflow.
 *   3. How would you return the first violating node?
 *      Carry bounds with node references and return the node that breaks them.
 *   4. How would you recover a BST with two swapped nodes?
 *      Inorder traversal reveals one or two inversions; swap the misplaced values.
 *
 * Related: Validate Binary Search Tree (98), Recover Binary Search Tree (99).
 */
public class CheckBst {

        public static void main(String[] args) {
        TreeNode valid = new TreeNode(4);
        valid.left = new TreeNode(2);
        valid.right = new TreeNode(5);
        valid.left.left = new TreeNode(1);
        valid.left.right = new TreeNode(3);

        TreeNode invalid = new TreeNode(5);
        invalid.left = new TreeNode(1);
        invalid.right = new TreeNode(4);
        invalid.right.left = new TreeNode(3);
        invalid.right.right = new TreeNode(6);

        System.out.printf("root=[4,2,5,1,3] -> %b  expected=true%n", isBST(valid));
        System.out.printf("root=[5,1,4,null,null,3,6] -> %b  expected=false%n", isBST(invalid));
    }


        /**
     * Intuition: a node is not checked only against its parent; it must also respect
     * all ancestor limits. Passing a minimum and maximum value down the recursion
     * makes those inherited constraints explicit for each subtree.
     *
     * Algorithm:
     *   1. Start the root with the widest integer bounds.
     *   2. Reject a node whose val is outside the open range (min, max).
     *   3. Recurse left with max set to node.val.
     *   4. Recurse right with min set to node.val and combine both answers with AND.
     *
     * Time:  O(n) - each node is visited once.
     * Space: O(h) - recursion depth equals the tree height.
     *
     * @param root root of the binary tree
     * @return true if the tree satisfies strict BST ordering
     */
    public static boolean isBST(TreeNode root) {
        return isBstRec(root, Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

        /**
     * Returns whether node and its children fit inside the current open bounds.
     */
    private static boolean isBstRec(TreeNode node, int max, int min) {
        if (node == null) return true; // An empty subtree is always a valid BST.

        // Check if the current node's value is within the valid range (min, max).
        if (node.val <= min || node.val >= max) {
            return false;
        }

        // Recursively check the left and right subtrees with updated boundaries.
        return isBstRec(node.left, node.val, min) &&
               isBstRec(node.right, max, node.val);
    }
}


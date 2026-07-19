package trees.binarysearchtree;

import trees.Node;
import trees.TreeNode;
import java.util.*;

/**
 * Problem: Kth Smallest Element in a BST
 *
 * Given a BST root and an integer k, return the kth smallest node value using
 * 1-based indexing. The BST property means inorder traversal lists values from
 * smallest to largest.
 *
 * Leetcode: https://leetcode.com/problems/kth-smallest-element-in-a-bst/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Trees | BST | Inorder traversal with early stop
 *
 * Example:
 *   Input:  root = [3,1,4,null,2], k = 1
 *   Output: 1
 *   Why:    inorder traversal is [1,2,3,4], so the first value is 1.
 *
 * Follow-ups:
 *   1. What if the BST changes frequently?
 *      Store subtree sizes in each node and update them on insert/delete.
 *   2. How would you return kth largest?
 *      Traverse right-root-left or convert it to the (n-k+1)th smallest query.
 *   3. How would you handle many kth queries?
 *      Precompute inorder values for a static tree or augment nodes for updates.
 *   4. What if k is invalid?
 *      Validate k against node count and throw or return a sentinel consistently.
 *
 * Related: Binary Tree Inorder Traversal (94), Kth Largest Element in a Stream (703).
 */
public class KthSmallestElementInABst {

    public static void main(String[] args) {
        KthSmallestElementInABst solver = new KthSmallestElementInABst();
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(1);
        root.left.right = new TreeNode(2);
        root.right = new TreeNode(4);

        int[] ks = {1, 3};
        int[] expected = {1, 3};
        for (int i = 0; i < ks.length; i++) {
            int got = solver.kthSmallest(root, ks[i]);
            System.out.printf("root=[3,1,4,null,2] k=%d -> %d  expected=%d%n",
                ks[i], got, expected[i]);
        }
    }


        /**
     * Intuition: inorder traversal of a BST is sorted. The stack version preserves the
     * original left-root-right order while letting us stop as soon as the kth visited
     * node is found.
     *
     * Algorithm:
     *   1. Push left children until the current path is exhausted.
     *   2. Pop the next inorder node and increment count.
     *   3. Return current.val when count reaches k.
     *   4. Continue from current.right if more nodes are needed.
     *
     * Time:  O(h + k) - reach the left spine, then visit k nodes.
     * Space: O(h) - stack stores a root-to-leaf path.
     *
     * @param root root of the BST
     * @param k 1-based target position in sorted order
     * @return kth smallest value, or -1 if traversal ends first
     */
    public int kthSmallest(TreeNode root, int k) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;
        int count = 0;

        while (current != null || !stack.isEmpty()) {
            // Go to leftmost node
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            // Process current node
            current = stack.pop();
            count++;

            // If we've reached kth node, return its value
            if (count == k) {
                return current.val;
            }

            // Move to right subtree
            current = current.right;
        }

        return -1; // Should never reach here for valid input
    }

        /**
     * Visits nodes in inorder until count reaches k.
     */
    private int count = 0;
    private int result = 0;

    public int kthSmallestRecursive(TreeNode root, int k) {
        count = 0;
        result = 0;
        inorderTraversal(root, k);
        return result;
    }

    // Helper for recursive inorder traversal
    private void inorderTraversal(TreeNode node, int k) {
        if (node == null || count >= k) {
            return;
        }

        // Traverse left subtree
        inorderTraversal(node.left, k);

        // Process current node
        if (count < k) {
            count++;
            if (count == k) {
                result = node.val;
                return;
            }
        }

        // Traverse right subtree
        inorderTraversal(node.right, k);
    }

    /**
     * Follow-up: If BST is modified frequently, use augmented BST.
     *
     * Add size field to each node indicating subtree size.
     * This allows O(h) lookup for kth element.
     */
    static class AugmentedTreeNode {
        int val;
        int size; // Size of subtree rooted at this node
        AugmentedTreeNode left;
        AugmentedTreeNode right;

        AugmentedTreeNode(int val) {
            this.val = val;
            this.size = 1;
        }
    }

    // Find kth smallest in augmented BST
    public int kthSmallestAugmented(AugmentedTreeNode root, int k) {
        int leftSize = (root.left != null) ? root.left.size : 0;

        if (k <= leftSize) {
            return kthSmallestAugmented(root.left, k);
        } else if (k == leftSize + 1) {
            return root.val;
        } else {
            return kthSmallestAugmented(root.right, k - leftSize - 1);
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
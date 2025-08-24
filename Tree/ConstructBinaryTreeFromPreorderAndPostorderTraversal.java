package Tree;

import java.util.*;

/**
 * 889. Construct Binary Tree from Preorder and Postorder Traversal
 *
 * Problem: Given two integer arrays, preorder and postorder, representing the preorder
 * and postorder traversal of a full binary tree, reconstruct the binary tree.
 *
 * Example:
 * Input: preorder = [1,2,4,5,3,6,7], postorder = [4,5,2,6,7,3,1]
 * Output: [1,2,3,4,5,6,7]
 *
 * LeetCode: https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal
 *
 * Follow-up questions:
 * Q: What if the tree is not full?
 * A: Impossible to uniquely reconstruct; need inorder array as well.
 *
 * Q: How to handle duplicates?
 * A: Tree reconstruction fails; values must be unique.
 *
 * Q: Can we do this in O(n)?
 * A: Yes, with index maps for quick lookup in postorder.
 */
public class ConstructBinaryTreeFromPreorderAndPostorderTraversal {

    // Definition for binary tree node
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    /**
     * Reconstructs full binary tree from preorder and postorder arrays.
     *
     * Algorithm: Recursive split using next preorder element to find left subtree
     * - Base: if empty range, return null; if single node, return node
     * - Root = preorder[pStart]
     * - Left root = preorder[pStart+1], find its index in postorder
     * - Compute left subtree size = index_post - postStart + 1
     * - Recurse on left and right subtrees
     *
     * Time Complexity: O(n) with hashmap for postorder indices
     * Space Complexity: O(n) for recursion stack and index map
     */
    public TreeNode constructFromPrePost(int[] preorder, int[] postorder) {
        Map<Integer, Integer> postIndex = new HashMap<>();
        for (int i = 0; i < postorder.length; i++) {
            postIndex.put(postorder[i], i);
        }
        return helper(preorder, 0, preorder.length - 1, postorder, 0, postorder.length - 1, postIndex);
    }

    private TreeNode helper(int[] preorder, int pStart, int pEnd,
                            int[] postorder, int postStart, int postEnd,
                            Map<Integer, Integer> postIndex) {
        if (pStart > pEnd) {
            return null;
        }
        TreeNode root = new TreeNode(preorder[pStart]);
        if (pStart == pEnd) {
            return root;
        }
        int leftRootVal = preorder[pStart + 1];
        int leftRootIdx = postIndex.get(leftRootVal);
        int leftSize = leftRootIdx - postStart + 1;

        root.left = helper(preorder, pStart + 1, pStart + leftSize,
                           postorder, postStart, leftRootIdx, postIndex);
        root.right = helper(preorder, pStart + leftSize + 1, pEnd,
                            postorder, leftRootIdx + 1, postEnd - 1, postIndex);
        return root;
    }
}
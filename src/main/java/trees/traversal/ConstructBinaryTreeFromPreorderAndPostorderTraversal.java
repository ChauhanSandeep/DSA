package trees.traversal;

import trees.Node;
import trees.TreeNode;
import java.util.*;

/**
 * Problem: Construct Binary Tree from Preorder and Postorder Traversal
 *
 * Given preorder and postorder traversals of a full binary tree, reconstruct one
 * matching tree. Preorder identifies each root first; postorder lets us locate
 * where the left subtree ends.
 *
 * Leetcode: https://leetcode.com/problems/construct-binary-tree-from-preorder-and-postorder-traversal/ (Medium)
 * Rating:   1732
 * Pattern:  Trees | Recursion | Traversal splitting | Index map
 *
 * Example:
 *   Input:  preorder = [1,2,4,5,3,6,7], postorder = [4,5,2,6,7,3,1]
 *   Output: root with children 2 and 3
 *   Why:    preorder[1] is the left-root 2, whose position in postorder fixes the left subtree size.
 *
 * Follow-ups:
 *   1. What if the tree is not full?
 *      Preorder and postorder alone do not uniquely determine the tree.
 *   2. What if values are duplicated?
 *      Add unique ids or another traversal; a value-to-index map is ambiguous.
 *   3. Can this be done without a HashMap?
 *      Yes, but searching postorder each time can degrade to O(n^2).
 *
 * Related: Construct Binary Tree from Preorder and Inorder Traversal (105).
 */
public class ConstructBinaryTreeFromPreorderAndPostorderTraversal {

    public static void main(String[] args) {
        ConstructBinaryTreeFromPreorderAndPostorderTraversal solver =
            new ConstructBinaryTreeFromPreorderAndPostorderTraversal();
        int[] preorder = {1, 2, 4, 5, 3, 6, 7};
        int[] postorder = {4, 5, 2, 6, 7, 3, 1};
        TreeNode root = solver.constructFromPrePost(preorder, postorder);
        int[] singlePreorder = {1};
        int[] singlePostorder = {1};
        TreeNode single = solver.constructFromPrePost(singlePreorder, singlePostorder);

        System.out.printf("preorder=%s postorder=%s -> root,left,right=%s  expected=%s%n",
            Arrays.toString(preorder), Arrays.toString(postorder),
            root.val + "," + root.left.val + "," + root.right.val, "1,2,3");
        System.out.printf("preorder=%s postorder=%s -> root=%d  expected=%d%n",
            Arrays.toString(singlePreorder), Arrays.toString(singlePostorder), single.val, 1);
    }


    // Definition for binary tree node
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

        /**
     * Intuition: preorder gives the current root immediately. If there is more
     * than one node, the next preorder value is the left subtree root; finding it
     * in postorder tells exactly how many nodes belong to the left subtree.
     *
     * Algorithm:
     *   1. Build postIndex so any value's postorder position is available in O(1).
     *   2. Create the root from preorder[pStart].
     *   3. Use preorder[pStart + 1] to find leftRootIdx and leftSize.
     *   4. Recurse on the left and right traversal slices.
     *
     * Time:  O(n) - each node is created once and each index lookup is constant time.
     * Space: O(n) - the index map plus recursion stack in the worst case.
     *
     * @param preorder preorder traversal of the full binary tree
     * @param postorder postorder traversal of the same tree
     * @return root of a reconstructed tree
     */
    public TreeNode constructFromPrePost(int[] preorder, int[] postorder) {
        Map<Integer, Integer> postIndex = new HashMap<>();
        for (int i = 0; i < postorder.length; i++) {
            postIndex.put(postorder[i], i);
        }
        return helper(preorder, 0, preorder.length - 1, postorder, 0, postorder.length - 1, postIndex);
    }

// Recursively builds the subtree represented by matching preorder and postorder ranges.
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
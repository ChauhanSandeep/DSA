package trees.binarysearchtree;

import java.util.HashMap;
import java.util.Map;

import trees.Node;
import trees.TreeNode;
import java.util.Arrays;

/**
 * Problem: Construct Binary Tree from Preorder and Inorder Traversal
 *
 * Given preorder and inorder traversals of the same binary tree, rebuild the
 * original tree. Preorder reveals each subtree root first; inorder tells how many
 * nodes belong to that root's left and right subtrees.
 *
 * Leetcode: https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Trees | Divide and conquer | Preorder cursor plus inorder index map
 *
 * Example:
 *   Input:  preorder = [1,2,4,5,3,6], inorder = [4,2,5,1,3,6]
 *   Output: root = 1 with left child 2 and right child 3
 *   Why:    preorder picks 1 first, and inorder splits values left of 1 into the left subtree.
 *
 * Follow-ups:
 *   1. What if duplicate values appear?
 *      Store queues of inorder indexes or use value plus occurrence identifiers.
 *   2. Can this be built from preorder and postorder?
 *      Only uniquely when the tree is full; otherwise multiple trees can match.
 *   3. How would you avoid recursion for a skewed tree?
 *      Simulate the recursive ranges with an explicit stack.
 *   4. How would you validate bad input traversals?
 *      Check lengths, matching value counts, and index bounds during construction.
 *
 * Related: Construct Binary Tree from Inorder and Postorder Traversal (106).
 */
public class ConstructBst {

    /**
     * Main method to test the tree construction.
     */
        public static void main(String[] args) {
        ConstructBst treeBuilder = new ConstructBst();
        int[][] preorderCases = { {1, 2, 4, 5, 3, 6}, {1} };
        int[][] inorderCases = { {4, 2, 5, 1, 3, 6}, {1} };
        String[] expected = { "root=1 left=2 right=3", "root=1 left=null right=null" };

        for (int i = 0; i < preorderCases.length; i++) {
            TreeNode root = treeBuilder.buildTree(preorderCases[i], inorderCases[i]);
            String output = "root=" + root.val
                + " left=" + (root.left == null ? "null" : root.left.val)
                + " right=" + (root.right == null ? "null" : root.right.val);
            System.out.printf("preorder=%s inorder=%s -> %s  expected=%s%n",
                Arrays.toString(preorderCases[i]), Arrays.toString(inorderCases[i]), output, expected[i]);
        }
    }



        /**
     * Intuition: preorder is a stream of roots. For the next root value, the inorder
     * position splits the remaining values into left and right subtree ranges. A
     * shared preorderCursor advances once per created node, so recursive calls consume
     * roots in the original preorder order.
     *
     * Algorithm:
     *   1. Map each inorder value to its index.
     *   2. Keep preorderCursor as a shared one-element array.
     *   3. Recursively build the current inorder range from the next preorder value.
     *   4. Split around the root index, then build left before right.
     *
     * Time:  O(n) - each node is created once and each inorder lookup is O(1).
     * Space: O(n) - index map plus recursion stack and output tree.
     *
     * @param preorder root-left-right traversal of the tree
     * @param inorder left-root-right traversal of the tree
     * @return root of the reconstructed binary tree
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        // Map each value in the inorder traversal to its index for O(1) root lookup.
        Map<Integer, Integer> valueToInorderIndex = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            valueToInorderIndex.put(inorder[i], i);
        }

        // Wrap the preorder cursor in a single-element array so it is shared
        // (and incremented) across recursive calls.
        int[] preorderCursor = {0};
        return buildSubtree(preorder, 0, inorder.length - 1, preorderCursor, valueToInorderIndex);
    }

    /**
     * Builds one subtree for the current inorder range using the shared preorder cursor.
     */
    private TreeNode buildSubtree(int[] preorder, int inorderStart, int inorderEnd,
                                  int[] preorderCursor, Map<Integer, Integer> valueToInorderIndex) {
        // No elements remain in this inorder range — subtree is empty.
        if (inorderStart > inorderEnd) return null;

        // The next value in preorder is the root of the current subtree.
        int rootValue = preorder[preorderCursor[0]++];
        TreeNode root = new TreeNode(rootValue);

        // Split the inorder range around the root to form left and right subtrees.
        int rootInorderIndex = valueToInorderIndex.get(rootValue);
        root.left = buildSubtree(preorder, inorderStart, rootInorderIndex - 1, preorderCursor, valueToInorderIndex);
        root.right = buildSubtree(preorder, rootInorderIndex + 1, inorderEnd, preorderCursor, valueToInorderIndex);
        return root;
    }
}

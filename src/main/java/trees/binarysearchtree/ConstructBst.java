package trees.binarysearchtree;

import java.util.HashMap;
import java.util.Map;

import trees.Node;
import trees.TreeNode;

/**
 * Given preorder and inorder traversal of a binary tree, construct the binary tree.
 * Preorder is where the root node is visited first, followed by the left subtree and then the right subtree.
 * Inorder is where the left subtree is visited first, followed by the root node and then the right subtree.
 *
 * Example:
 * Input:   preorder = [1,2,4,5,3,6],
 *          inorder =  [4,2,5,1,3,6]
 * Output: [1,2,3,4,5,6]
 *          1
 *         / \
 *        2   3
 *       / \   \
 *      4   5   6
 *
 * LeetCode Link:
 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class ConstructBst {

    /**
     * Main method to test the tree construction.
     */
    public static void main(String[] args) {
        int[] preorder = {1,2,4,5,3,6};
        int[] inorder = {4,2,5,1,3,6};

        // Build the binary tree using the given preorder and inorder traversals
        ConstructBst treeBuilder = new ConstructBst();
        TreeNode root = treeBuilder.buildTree(preorder, inorder);

        // Print the resulting tree (optional: toString method of TreeNode must be implemented to display tree)
        System.out.println(root);
    }


    /**
     * This method constructs the binary tree using preorder and inorder traversals.
     *
     * Intuition:
     * - In preorder traversal, the first element is the root of the tree.
     * - In inorder traversal, the elements to the left of the root represent the left subtree,
     *   and the elements to the right represent the right subtree.
     * - The goal is to recursively build the tree using this information.
     *
     * Algorithm:
     * 1. Identify the root node of the current subtree from the first element of the preorder array segment.
     * 2. Locate the root node's index in the inorder array segment to determine the boundaries of the left and right subtrees.
     * 3. Recursively construct the left subtree using the elements to the left of the root in the inorder array and
     *    the corresponding elements in the preorder array.
     * 4. Recursively construct the right subtree using the elements to the right of the root in the inorder array
     *    and the corresponding elements in the preorder array.
     * 5. Attach the constructed left and right subtrees to the root node and repeat the process until all nodes are placed.
     *
     * Time Complexity:
     * - The time complexity is O(n) where n is the number of nodes in the tree because each node is processed once.
     * Space Complexity:
     * - The space complexity is O(n) due to the recursive call stack and the space required to store the tree.
     *
     *
     * @param preorder  The preorder traversal array.
     * @param inorder   The inorder traversal array.
     * @return          The root of the constructed binary tree.
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

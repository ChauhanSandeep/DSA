package trees;

/**
 * Given preorder and inorder traversal of a binary tree, construct the binary tree.
 * Preorder is where the root node is visited first, followed by the left subtree and then the right subtree.
 * Inorder is where the left subtree is visited first, followed by the root node and then the right subtree.
 *
 * Example:
 * Input: preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]
 * Output: [3,9,20,null,null,15,7]
 *          3
 *         / \
 *        9  20
 *          /  \
 *         15   7
 * Intuition:
 * - In preorder traversal, the first element is the root of the tree.
 * - In inorder traversal, the elements to the left of the root represent the left subtree,
 *   and the elements to the right represent the right subtree.
 * - The goal is to recursively build the tree using this information.
 *
 * Algorithm:
 * 1. Start with the root node (first element in preorder).
 * 2. Find the index of the root in inorder.
 * 3. Recursively build the left and right subtrees by using the corresponding segments
 *    of the preorder and inorder arrays.
 * 4. Continue until the tree is fully constructed.
 *
 * Time Complexity:
 * - The time complexity is O(n) where n is the number of nodes in the tree because each node is processed once.
 *
 * Space Complexity:
 * - The space complexity is O(n) due to the recursive call stack and the space required to store the tree.
 *
 * LeetCode Link:
 * https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
 */
public class ConstructBst {

    /**
     * Main method to test the tree construction.
     */
    public static void main(String[] args) {
        int[] preorder = {3, 9, 20, 15, 7};
        int[] inorder = {9, 3, 15, 20, 7};

        // Build the binary tree using the given preorder and inorder traversals
        ConstructBst treeBuilder = new ConstructBst();
        TreeNode root = treeBuilder.buildTree(preorder, inorder);

        // Print the resulting tree (optional: toString method of TreeNode must be implemented to display tree)
        System.out.println(root);
    }

    /**
     * This method constructs the binary tree using preorder and inorder traversals.
     *
     * @param preorder  The preorder traversal array.
     * @param inorder   The inorder traversal array.
     * @return          The root of the constructed binary tree.
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder == null || inorder == null || preorder.length == 0 || inorder.length == 0) {
            return null; // Base case: if input arrays are empty, return null
        }
        // Call the helper method to build the tree
        return buildTree(preorder, inorder, 0, inorder.length - 1, 0);
    }

    /**
     * Helper method to recursively construct the binary tree.
     *
     * @param preorder   The preorder traversal array.
     * @param inorder    The inorder traversal array.
     * @param inorderLeftIndex     The left index of the inorder array for the current subtree.
     * @param inorderRightIndex    The right index of the inorder array for the current subtree.
     * @param preorderStartIndex   The current index in the preorder array.
     * @return           The root of the current subtree.
     */
    private TreeNode buildTree(int[] preorder, int[] inorder, int inorderLeftIndex, int inorderRightIndex, int preorderStartIndex) {
        // Base case: if the current segment is invalid, return null
        if (inorderLeftIndex > inorderRightIndex || preorderStartIndex >= preorder.length) {
            return null;
        }

        // Create the current node (root) using the current element from preorder
        TreeNode rootNode = new TreeNode(preorder[preorderStartIndex]);

        // Find the index of the current root in inorder traversal
        int rootIndexInInorder = findIndex(inorder, preorder[preorderStartIndex]);

        // Recursively build the left and right subtrees
        // Left subtree will be constructed using elements before the root in inorder
        TreeNode leftSubtree = buildTree(preorder, inorder, inorderLeftIndex, rootIndexInInorder - 1, preorderStartIndex + 1);

        // Right subtree will be constructed using elements after the root in inorder
        int nextPreorderStartIndex = preorderStartIndex + 1 + (rootIndexInInorder - inorderLeftIndex); // because we have already used one element for the left subtree
        TreeNode rightSubtree = buildTree(preorder, inorder, rootIndexInInorder + 1, inorderRightIndex, nextPreorderStartIndex);

        // Attach the left and right subtrees to the current root
        rootNode.left = leftSubtree;
        rootNode.right = rightSubtree;

        return rootNode;
    }

    /**
     * Finds the index of the target element in the given array.
     *
     * @param arr   The array to search in.
     * @param target The element to find in the array.
     * @return      The index of the target element, or -1 if not found.
     */
    private int findIndex(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i; // Return the index if found
            }
        }
        return -1; // Return -1 if the target is not found
    }
}

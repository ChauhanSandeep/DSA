package trees.dfs;
import trees.Node;

/**
 * Problem: Check if a binary tree is a SumTree.
 * A SumTree is defined as a binary tree where each node's value is equal to the sum of the values of its left and right subtrees.
 * The leaves of the tree are considered SumTrees by default, as they don't have any children.
 *
 * Intuition:
 * We can recursively check whether the tree is a SumTree by:
 * - Starting from the leaf nodes, which are trivially SumTrees with their own value.
 * - For each internal node, check if the value of the node is equal to the sum of the values of its left and right subtrees.
 * - If any node fails this condition, the tree is not a SumTree.
 *
 * Algorithm:
 * 1. Recursively traverse the tree, checking if each node is a SumTree.
 * 2. For each node, calculate the sum of its left and right subtrees and compare it with the node's value.
 * 3. Return `true` if the tree is a SumTree and `false` otherwise.
 *
 * Time Complexity: O(N), where N is the number of nodes in the tree, as each node is visited once.
 * Space Complexity: O(H), where H is the height of the tree, due to the recursive call stack.
 *
 * LeetCode Link: https://leetcode.com/problems/check-if-a-binary-tree-is-a-sum-tree/
 * LeetCode Contest Rating: Not available (not a contest problem)
 */

public class SumTree {

    public static void main(String[] args) {
        // Example binary tree:
        /*
                26
                /  \
              10     3
            /    \     \
          4      6      3
        */
        Node root = new Node(26);
        root.left = new Node(10);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(6);
        root.right.right = new Node(3);

        // Check if the tree is a SumTree
        System.out.println("Is the tree a SumTree? " + isSumTree(root)); // Expected Output: true
    }

    /**
     * Checks if the given binary tree is a SumTree.
     * A SumTree is a binary tree where the value of a node is equal to the sum of the values
     * of its left and right subtrees.
     *
     * @param root The root of the binary tree.
     * @return True if the tree is a SumTree, otherwise false.
     */
    public static boolean isSumTree(Node root) {
        // Base case: An empty tree or a leaf node is trivially a SumTree
        if (root == null) return true;

        // Try-catch block to catch the runtime exception if the tree is not a SumTree
        try {
            // Call the helper function to check the SumTree condition and return the result
            int subtreeSum = isSumTreeRec(root);
            // The node's value should be equal to the sum of the left and right subtrees
            return root.data == subtreeSum;
        } catch (RuntimeException e) {
            // Return false if a RuntimeException is thrown (meaning the tree is not a SumTree)
            return false;
        }
    }

    /**
     * Recursively checks whether a binary tree is a SumTree.
     *
     * @param node The current node being checked.
     * @return The sum of the node's value and its left and right subtrees.
     * @throws RuntimeException If the tree is not a SumTree.
     */
    private static int isSumTreeRec(Node node) {
        // Base case: If it's a leaf node, its value is the sum itself.
        if (node.left == null && node.right == null) {
            return node.data;
        }

        int leftSum = 0, rightSum = 0;

        // If left child exists, recursively check its subtree
        if (node.left != null) {
            leftSum = isSumTreeRec(node.left);
        }

        // If right child exists, recursively check its subtree
        if (node.right != null) {
            rightSum = isSumTreeRec(node.right);
        }

        // Check if the node's value is equal to the sum of its left and right subtrees
        if (node.data != leftSum + rightSum) {
            throw new RuntimeException("Not a SumTree");
        }

        // Return the sum of the node's value and its left and right subtrees
        return node.data + leftSum + rightSum;
    }
}

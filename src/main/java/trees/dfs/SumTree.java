package trees.dfs;
import trees.Node;

/**
 * Problem: Sum Tree
 *
 * A binary tree is a SumTree when every non-leaf node equals the sum of all
 * values in its left and right subtrees. Empty trees and leaves are considered
 * valid SumTrees.
 *
 * Leetcode: n/a (GeeksforGeeks SumTree practice)
 * Rating:   not available
 * Pattern:  Trees | DFS | Postorder subtree sums | Failure sentinel by exception
 *
 * Example:
 *   Input:  root = [26,10,3,4,6,null,3]
 *   Output: true
 *   Why:    10 = 4 + 6, 3 = 3, and 26 equals the sum of both child subtrees.
 *
 * Follow-ups:
 *   1. Can you avoid exceptions for control flow?
 *      Return a pair of isSumTree and subtree sum from the helper.
 *   2. What if node values may overflow int totals?
 *      Return long subtree sums instead of int.
 *   3. How would you find all nodes that violate the property?
 *      Continue traversal and collect failing nodes instead of stopping early.
 */

public class SumTree {

        public static void main(String[] args) {
        Node leaf = new Node(26);

        Node invalid = new Node(10);
        invalid.left = new Node(20);
        invalid.right = new Node(30);

        System.out.printf("root=%s -> %s  expected=%s%n",
            "[26]", isSumTree(leaf), true);
        System.out.printf("root=%s -> %s  expected=%s%n",
            "[10,20,30]", isSumTree(invalid), false);
    }


        /**
     * Intuition: a node can be checked only after knowing the sums of both
     * subtrees. isSumTreeRec returns that subtree total for valid subtrees and
     * throws when it sees a mismatch. The restored original public method then
     * compares root.data with that returned subtree total.
     *
     * Algorithm:
     *   1. Return true immediately for null input.
     *   2. Recursively compute leftSum and rightSum for every non-leaf node.
     *   3. Throw if node.data is not leftSum + rightSum.
     *   4. Return true only when the root value matches the helper's returned total.
     *
     * Time:  O(n) - each node is visited once until a mismatch stops traversal.
     * Space: O(h) - recursion stack follows the tree height.
     *
     * @param root root of the binary tree
     * @return true if the tree satisfies the SumTree property
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

        // Returns the subtree sum if valid, otherwise throws to stop the check.
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

package trees.traversal;

import trees.Node;

/**
 * This class converts a binary tree to a flattened linked list in-place.
 * The flattened list should follow the preorder traversal order of the binary tree.
 * 
 * Leetcode link : https://leetcode.com/problems/flatten-binary-tree-to-linked-list/
 *
 * Intuition:
 * - We recursively flatten the left subtree and then attach it to the right side of the current node.
 * - We then attach the original right subtree to the end of the newly flattened left subtree.
 * - Finally, recursively flatten the right subtree.
 *
 * Algorithm:
 * 1. Recursively flatten the left subtree.
 * 2. Attach the flattened left subtree to the right of the current node.
 * 3. Append the original right subtree to the end of the right subtree.
 * 4. Flatten the right subtree.
 *
 * Time Complexity:
 * - The time complexity is O(n), where n is the number of nodes in the binary tree.
 *   Each node is visited once during the recursion.
 *
 * Space Complexity:
 * - The space complexity is O(h), where h is the height of the tree,
 *   due to the recursion stack. In the worst case, this is O(n) for a skewed tree.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class FlattenTree {

    /**
     * Main method to test the flatten functionality on a sample binary tree.
     */
    public static void main(String[] args) {
        // Construct the following binary tree
        //                1
        //              /   \
        //             /     \
        //            2       3
        //           / \     /
        //          4   5   6
        //                 /
        //                7
        //
        // The resulting flattened tree should look like:
        // 1 -> 2 -> 4 -> 5 -> 3 -> 6 -> 7

        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.left.left = new Node(7);

        // Flatten the tree into a linked list
        FlattenTree treeFlattener = new FlattenTree();
        treeFlattener.flatten(root);

        // Print the flattened tree (linked list)
        printFlattenedList(root);
    }

    public void flatten(Node root) {
        flattenAndReturnTail(root);
    }

    /**
     * Recursively flattens the tree rooted at 'node' and returns the tail of the
     * flattened list.
     *
     * Idea:
     * - Recursively flatten left and right subtrees.
     * - If there is a left subtree:
     *     - Attach the flattened left list between current node and flattened right list.
     *     - Move left list to the right and set left to null.
     */
    private Node flattenAndReturnTail(Node node) {
        if (node == null) {
            return null;
        }

        // Flatten left and right subtrees
        Node leftTail = flattenAndReturnTail(node.left);
        Node rightTail = flattenAndReturnTail(node.right);

        // If there was a left subtree, we shuffle the connections
        if (leftTail != null) {
            leftTail.right = node.right; // attach original right to end of left list
            node.right = node.left;      // move left list to the right
            node.left = null;            // set left to null
        }

        // Return the tail of the flattened list for this subtree
        if (rightTail != null) {
            return rightTail;
        }
        if (leftTail != null) {
            return leftTail;
        }
        return node;
    }

    /**
     * This method prints the flattened linked list (flattened tree).
     *
     * @param node The root node of the flattened tree.
     */
    public static void printFlattenedList(Node node) {
        while (node != null) {
            System.out.print(node.val + " ");
            node = node.right; // Move to the next node in the flattened list
        }
    }

    /**
     * Node class to represent each node in the binary tree.
     */
    static class Node {
        int val;
        Node left, right;

        public Node(int val) {
            this.val = val;
            this.left = null;
            this.right = null;
        }
    }
}

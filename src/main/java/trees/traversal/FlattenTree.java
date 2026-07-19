package trees.traversal;

import trees.Node;

/**
 * Problem: Flatten Binary Tree to Linked List
 *
 * Rearrange a binary tree in place into a right-child-only linked list. The list
 * must follow the same order as preorder traversal, and every left pointer must
 * become null.
 *
 * Leetcode: https://leetcode.com/problems/flatten-binary-tree-to-linked-list/ (Medium)
 * Rating:   not available
 * Pattern:  Trees | DFS | Postorder tail return | In-place rewiring
 *
 * Example:
 *   Input:  root = [1,2,5,3,4,null,6]
 *   Output: [1,null,2,null,3,null,4,null,5,null,6]
 *   Why:    preorder visits 1, 2, 3, 4, 5, 6, which becomes the right-pointer chain.
 *
 * Follow-ups:
 *   1. Can you flatten iteratively with O(1) extra space?
 *      Use Morris-style rewiring by finding the rightmost node of each left subtree.
 *   2. How would you produce inorder flattening instead?
 *      Change the traversal order and keep previous/tail pointers accordingly.
 *   3. How can you verify no left pointers remain?
 *      Walk the right chain after flattening and check every left pointer is null.
 *
 * Related: Binary Tree Preorder Traversal (144), Convert Binary Search Tree to Sorted Doubly Linked List.
 */
public class FlattenTree {

    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(5);
        root.left.left = new Node(3);
        root.left.right = new Node(4);
        root.right.right = new Node(6);

        new FlattenTree().flatten(root);
        StringBuilder flattened = new StringBuilder();
        for (Node node = root; node != null; node = node.right) {
            if (flattened.length() > 0) {
                flattened.append(", ");
            }
            flattened.append(node.val);
        }

        Node single = new Node(7);
        new FlattenTree().flatten(single);
        System.out.printf("root=%s -> [%s]  expected=%s%n",
            "[1,2,5,3,4,null,6]", flattened, "[1, 2, 3, 4, 5, 6]");
        System.out.printf("root=%s -> [%d]  expected=%s%n",
            "[7]", single.val, "[7]");
    }


    public void flatten(Node root) {
        flattenAndReturnTail(root);
    }

        // Flattens node in preorder and returns the tail of the flattened chain.
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

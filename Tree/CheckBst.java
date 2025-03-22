package Tree;

/**
 * **Check if a Binary Tree is a Binary Search Tree (BST)**
 * 
 * A binary tree is a Binary Search Tree (BST) if, for every node:
 * 1. All values in the left subtree are smaller than the node’s value.
 * 2. All values in the right subtree are greater than the node’s value.
 * 3. The left and right subtrees must also be BSTs.
 * 
 * **Approach:**
 * - Use a **recursive approach** with **min-max boundaries** for each node.
 * - For each node, check if its value lies between the min and max constraints.
 * - Recursively check the left and right subtrees with updated boundaries.
 * 
 * **Time Complexity:** **O(N)** (Each node is visited once)
 * **Space Complexity:** **O(H)** (recursion stack, where H is the height of the tree)
 */
public class CheckBst {

    public static void main(String[] args) {
        // Construct the following tree:
        //        4
        //       / \
        //      2   5
        //     / \  
        //    1   3

        Node root = new Node(4);
        root.left = new Node(2);
        root.right = new Node(5);
        root.left.left = new Node(1);
        root.left.right = new Node(3);

        System.out.println("Is the tree a BST? " + isBST(root));
    }

    /**
     * Public method to check if the binary tree is a Binary Search Tree (BST).
     * 
     * @param root The root node of the binary tree.
     * @return True if the tree is a BST, false otherwise.
     */
    public static boolean isBST(Node root) {
        return isBstRec(root, Integer.MAX_VALUE, Integer.MIN_VALUE);
    }

    /**
     * Recursive method to check if the subtree rooted at the given node is a BST.
     * 
     * @param node The current node.
     * @param max The maximum allowable value for the current node.
     * @param min The minimum allowable value for the current node.
     * @return True if the subtree rooted at the current node is a BST, false otherwise.
     */
    private static boolean isBstRec(Node node, int max, int min) {
        if (node == null) return true; // An empty subtree is always a valid BST.

        // Check if the current node's value is within the valid range (min, max).
        if (node.data <= min || node.data >= max) {
            return false;
        }

        // Recursively check the left and right subtrees with updated boundaries.
        return isBstRec(node.left, node.data, min) &&
               isBstRec(node.right, max, node.data);
    }
}

/**
 * Node structure for a binary tree.
 */
class Node {
    int data;
    Node left, right;

    public Node(int value) {
        this.data = value;
        this.left = null;
        this.right = null;
    }
}

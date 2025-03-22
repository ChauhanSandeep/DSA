package tree;

/**
 * Node class to represent a node in a binary tree.
 * Each node contains:
 * - an integer value (`data`),
 * - a reference to the left child (`left`),
 * - a reference to the right child (`right`).
 *
 * The Node class is used as a building block for creating various binary tree structures.
 */
public class Node {

    // Data stored at the current node.
    int data;

    // Reference to the left child of the node.
    public Node left;

    // Reference to the right child of the node.
    public Node right;

    /**
     * Constructor to initialize the node with a given value.
     *
     * @param item The value to store in the node.
     */
    public Node(int item) {
        this.data = item;  // Set the node's value.
        this.left = null;  // Initially, the left child is set to null.
        this.right = null; // Initially, the right child is set to null.
    }
}

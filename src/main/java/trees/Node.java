package trees;

/**
 * Problem: Binary Tree Node
 *
 * Represent one node in a binary tree using the original data, left, and right
 * fields. The class is a small shared building block for tree examples that use
 * data instead of val as the node value name.
 *
 * Pattern:  Trees | Pointer structure | Mutable node object
 *
 * Example:
 *   Input:  item = 7
 *   Output: node.data = 7, node.left = null, node.right = null
 *   Why:    the constructor stores the value and starts both child pointers empty.
 *
 * Follow-ups:
 *   1. How would you add parent pointers?
 *      Store a parent field and update it whenever left or right is assigned.
 *   2. How would you make nodes immutable?
 *      Make fields final and pass children through the constructor.
 *   3. How would this support duplicate counts in a BST?
 *      Add a frequency field while preserving left and right child references.
 */
public class Node {

    public static void main(String[] args) {
        Node node = new Node(7);
        System.out.printf("item=%d -> data=%d left=%s right=%s  expected=data=7 left=null right=null%n",
            7, node.data, node.left, node.right);
    }


    // Data stored at the current node.
    public int data;

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

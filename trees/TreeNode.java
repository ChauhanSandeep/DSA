package trees;

/**
 * Definition for a binary tree node.
 *
 * The `TreeNode` class represents a node in a binary tree.
 * Each node contains a value (`val`), a reference to its left child (`left`),
 * and a reference to its right child (`right`).
 *
 * Constructor:
 * - Default constructor initializes an empty node (with no value or children).
 * - Constructor with a value (`val`) initializes a node with a given value,
 *   leaving the left and right children as `null`.
 * - Constructor with a value (`val`), left child (`left`), and right child (`right`)
 *   initializes a node with specific values for the node and its children.
 */
public class TreeNode {

    // Value stored in the node.
    int val;

    // Reference to the left child node.
    TreeNode left;

    // Reference to the right child node.
    TreeNode right;

    // Default constructor: Creates a node with no value or children.
    public TreeNode() {}

    // Constructor that initializes the node with a given value.
    public TreeNode(int val) {
        this.val = val;
    }

    // Constructor that initializes the node with a given value and specified left and right children.
    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

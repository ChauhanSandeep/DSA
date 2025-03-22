package utils;

/**
 * This class represents a node in a binary tree.
 * 
 * Purpose:
 * - A TreeNode object contains data and references to its left and right children.
 * - This is typically used for tree-based data structures, such as binary search trees or binary heaps.
 * 
 * Intuition & Logic:
 * - Each node has a data field and two pointers (left and right) to its children in the binary tree.
 * - The constructor initializes the node with the given data and sets the children to null.
 * 
 * Algorithm:
 * - The construction of a tree node is O(1) since only three fields are set (data, left, right).
 * 
 * Time Complexity: O(1) for node creation.
 * Space Complexity: O(1) for storing the node's data and references.
 * 
 * LeetCode problem link (if applicable): N/A (this is a utility class, not directly related to a problem).
 */
public class TreeNode {
  
    // Data contained in the node
    public int value;

    // Reference to the left child node
    public TreeNode leftChild;

    // Reference to the right child node
    public TreeNode rightChild;

    /**
     * Constructor to initialize a new tree node with the given value.
     * The left and right children are initialized as null by default.
     * 
     * @param value The value to be stored in this node.
     */
    public TreeNode(int value) {
        this.value = value;
        this.leftChild = null;
        this.rightChild = null;
    }

}

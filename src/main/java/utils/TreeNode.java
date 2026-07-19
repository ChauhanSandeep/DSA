package utils;

/**
 * Shared binary-tree node model for tree data structures and demos.
 *
 * Each node stores one integer value and references to its optional left and
 * right children. Construction creates a leaf; callers wire children later.
 */
public class TreeNode {
  
    /** Data contained in the node. */
    public int value;

    /** Reference to the left child node. */
    public TreeNode leftChild;

    /** Reference to the right child node. */
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

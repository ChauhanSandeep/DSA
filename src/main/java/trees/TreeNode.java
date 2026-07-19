package trees;

/**
 * Problem: Binary Tree Node With val
 *
 * Represent one node in a binary tree using the original val, left, and right
 * fields. This shape matches many Leetcode tree signatures and is shared by
 * several BST and traversal examples in the repository.
 *
 * Pattern:  Trees | Pointer structure | Leetcode-style node object
 *
 * Example:
 *   Input:  val = 1, left = 2, right = 3
 *   Output: root.val = 1, root.left.val = 2, root.right.val = 3
 *   Why:    the three-argument constructor wires the supplied children directly.
 *
 * Follow-ups:
 *   1. How would you support generic values?
 *      Change val to a type parameter and compare through a Comparator when needed.
 *   2. How would you serialize this node?
 *      Traverse the tree and emit null markers so child positions are recoverable.
 *   3. How would you add parent pointers?
 *      Add a parent field and keep it synchronized when children are assigned.
 */
public class TreeNode {

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1, new TreeNode(2), new TreeNode(3));
        System.out.printf("root=%d -> left=%d right=%d  expected=left=2 right=3%n",
            root.val, root.left.val, root.right.val);
    }


    // Value stored in the node.
    public int val;

    // Reference to the left child node.
    public TreeNode left;

    // Reference to the right child node.
    public TreeNode right;

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

package two;

/**
 * Given a binary tree where all the right nodes are either empty or leaf nodes, flip it upside down
 * and turn it into a tree with left leaf nodes.
 * In the original tree, if a node has a right child, it also must have a left child.
 *
 * for example, turn these:
 *
 *        1                1
 *       / \              / \
 *      2   3            2   3
 *     /
 *    4
 *   / \
 *  5   6
 *
 * into these:
 *
 *        1               1
 *       /               /
 *      2---3           2---3
 *     /
 *    4
 *   /
 *  5---6
 *
 * where 5 is the new root node for the left tree, and 2 for the right tree.
 * oriented correctly:
 *
 *     5                  2
 *    / \                / \
 *   6   4              3   1
 *        \
 *         2
 *        / \
 *       3   1
 *
 */
public class SidewaysTree {

  public TreeNode reverseIteratively(TreeNode root) {
    // Store the current root node and it's left and right child
    TreeNode currentRoot = root;
    TreeNode leftChild = root.left;
    TreeNode rightChild = root.right;

    // Initialize variables to store the new left and right children
    TreeNode nextLeftChild;
    TreeNode nextRightChild;

    // Fix the root cycle problem by setting the current node's left and right pointers to null
    currentRoot.left = null;
    currentRoot.right = null;

    // Iterate through the tree, flipping it upside down
    while (leftChild != null) {
      // Store the left and right children of the current node. This create path for next level iteration
      nextLeftChild = leftChild.left;
      nextRightChild = leftChild.right;

      // Update the current node's pointers to flip the tree
      leftChild.right = currentRoot;
      leftChild.left = rightChild;

      // Move to next level
      currentRoot = leftChild;
      leftChild = nextLeftChild;
      rightChild = nextRightChild;
    }
    return currentRoot;
  }

  class TreeNode {
    TreeNode left;
    TreeNode right;
    int data;
  }
}
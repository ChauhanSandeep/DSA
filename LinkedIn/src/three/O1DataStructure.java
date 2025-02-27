package one;
import utils.TreeNode;


public class TreeFlipTest {
  // Solution
  public static TreeNode reverse(TreeNode node) {
    if (node == null) return null;
    if (node.left == null) return node;

    TreeNode newRoot = reverse(node.left);
    node.left.left = node.right;   // Connect parent's right child as left child of current left
    node.left.right = node;        // Connect parent as right child of current left
    node.left = null;
    node.right = null;
    return newRoot; // always return newNode to finally return the left most children which will be new root
  }

  // Helper method to print tree in preorder for verification
  public static void printPreOrder(TreeNode node) {
    if (node == null) {
      System.out.print("null ");
      return;
    }
    System.out.print(node.data + " ");
    printPreOrder(node.left);
    printPreOrder(node.right);
  }

  // Test cases
  public static void main(String[] args) {
    // Test Case 1: Example from the problem
    System.out.println("\nTest Case 1: Problem Example");
    TreeNode root1 = new TreeNode(1);
    root1.left = new TreeNode(2);
    root1.right = new TreeNode(3);
    root1.left.left = new TreeNode(4);
    root1.left.left.left = new TreeNode(5);
    root1.left.left.right = new TreeNode(6);

    System.out.println("Original Tree:");
    printPreOrder(root1);
    TreeNode flipped1 = reverse(root1);
    System.out.println("\nFlipped Tree:");
    printPreOrder(flipped1);
  }
}

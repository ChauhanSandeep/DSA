package trees;

/**
 * **Balanced Binary Tree Check**
 *
 * A binary tree is considered balanced if for every node in the tree, the absolute difference in height
 * between its left and right subtrees is at most 1, and both subtrees are also balanced binary trees.
 *
 * **Approach:**
 * - Perform a **post-order traversal** to calculate the height of each subtree.
 * - During traversal, check if the subtree is balanced at each node by comparing the heights of its left and right subtrees.
 * - If at any point, the difference in height between left and right subtrees exceeds 1, return that the tree is unbalanced.
 *
 * **Time Complexity:** **O(N)** (Each node is visited once)
 * **Space Complexity:** **O(H)** (recursion stack, where H is the height of the tree)
 */
class CheckBalanced {

    public static void main(String[] args) {
        /*
                1
              /   \
             2     3
            / \   /
           4   5 6
                /
               7
        */

        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
//        root.right.left.left = new Node(7);

        if (isBalanced(root)) {
            System.out.println("Binary tree is balanced.");
        } else {
            System.out.println("Binary tree is not balanced.");
        }
    }

    /**
     * Checks if the binary tree is balanced. A balanced binary tree is one where for every node,
     * the difference in height between its left and right subtrees is at most 1.
     *
     * @param node The root node of the tree.
     * @return True if the tree is balanced, false otherwise.
     */
    public static boolean isBalanced(Node node) {
        return checkHeight(node) != -1;
    }

    /**
     * Recursively computes the height of the subtree rooted at the given node.
     * If at any point the subtree is unbalanced, return -1 to indicate imbalance.
     *
     * @param node The node to check.
     * @return The height of the subtree, or -1 if the subtree is unbalanced.
     */
    private static int checkHeight(Node node) {
        if (node == null) return 0; // Base case: height of an empty tree is 0

        int leftHeight = checkHeight(node.left);
        if (leftHeight == -1) return -1; // Left subtree is unbalanced

        int rightHeight = checkHeight(node.right);
        if (rightHeight == -1) return -1; // Right subtree is unbalanced

        // Check if the current node is unbalanced
        if (Math.abs(leftHeight - rightHeight) > 1) {
            return -1; // Tree is unbalanced
        }

        // Return the height of the current subtree
        return Math.max(leftHeight, rightHeight) + 1;
    }
}


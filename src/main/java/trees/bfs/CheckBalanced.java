package trees.bfs;
import trees.Node;

/**
 * Problem: Balanced Binary Tree
 *
 * A binary tree is balanced when every node has left and right subtree heights
 * that differ by at most one. Return whether the whole tree satisfies that
 * condition, not just the root.
 *
 * Leetcode: https://leetcode.com/problems/balanced-binary-tree/ (Easy)
 * Rating:   not available
 * Pattern:  Trees | DFS | Postorder height check | Sentinel return
 *
 * Example:
 *   Input:  root = [1,2,2,3,3,null,null,4,4]
 *   Output: false
 *   Why:    the left side is two levels deeper than the right at one ancestor.
 *
 * Follow-ups:
 *   1. Can you also return the first unbalanced node?
 *      Return a pair of height and failing node from the same postorder traversal.
 *   2. How would you handle a tree with millions of nodes?
 *      Use an explicit stack to avoid recursion depth limits if the language requires it.
 *   3. What if balance means subtree sizes differ by at most one?
 *      Return subtree sizes instead of heights and keep the same sentinel idea.
 */
class CheckBalanced {

        public static void main(String[] args) {
        Node balanced = new Node(1);
        balanced.left = new Node(2);
        balanced.right = new Node(3);
        balanced.left.left = new Node(4);
        balanced.left.right = new Node(5);

        Node unbalanced = new Node(1);
        unbalanced.left = new Node(2);
        unbalanced.left.left = new Node(3);

        System.out.printf("root=%s -> %s  expected=%s%n",
            "[1,2,3,4,5]", isBalanced(balanced), true);
        System.out.printf("root=%s -> %s  expected=%s%n",
            "[1,2,null,3]", isBalanced(unbalanced), false);
    }


        /**
     * Intuition: height is only useful while every subtree below it is already
     * balanced. checkHeight returns the real height for balanced subtrees and
     * -1 as a stop signal once any child is unbalanced, so callers can avoid
     * extra work.
     *
     * Algorithm:
     *   1. Ask checkHeight for the root height.
     *   2. Let checkHeight recurse left, then right, and propagate -1 immediately.
     *   3. Treat any height difference greater than one as -1; otherwise return height + 1.
     *
     * Time:  O(n) - each node's height is computed once.
     * Space: O(h) - the recursion stack follows one root-to-leaf path.
     *
     * @param node root of the tree to test
     * @return true if every subtree is height-balanced
     */
    public static boolean isBalanced(Node node) {
        return checkHeight(node) != -1;
    }

        // Returns subtree height, or -1 as soon as an imbalance is found.
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


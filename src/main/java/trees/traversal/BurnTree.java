package trees.traversal;

import trees.Node;
import trees.TreeNode;

/**
 * Problem: Burn a Binary Tree from a Leaf
 *
 * Fire starts at a node value and spreads to connected nodes every second
 * through parent, left child, and right child links. Return the time required
 * for the fire to reach every node in the tree.
 *
 * Leetcode: n/a (GeeksforGeeks burning tree variant)
 * Rating:   not available
 * Pattern:  Trees | DFS | Distance bubbling | Opposite-subtree propagation
 *
 * Example:
 *   Input:  root = [5,4,6,3,14,null,7,null,null,null,null,9,8], leaf = 14
 *   Output: 5
 *   Why:    the farthest nodes, 9 and 8, burn five seconds after node 14.
 *
 * Follow-ups:
 *   1. How would you support any starting node, not only a leaf?
 *      The same distance-bubbling DFS works as long as values are unique.
 *   2. How would you return nodes burned at each second?
 *      Build parent links and run BFS levels from the start node.
 *   3. What if edges have different burn times?
 *      Model the tree as a weighted graph and run Dijkstra.
 *
 * Related: Amount of Time for Binary Tree to Be Infected (2385), All Nodes Distance K (863).
 */
public class BurnTree {

    private int maxBurnTime = 0; // Tracks the maximum time taken to burn the tree

        public static void main(String[] args) {
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(4);
        root.left.right = new TreeNode(14);
        root.left.left = new TreeNode(3);
        root.right = new TreeNode(6);
        root.right.right = new TreeNode(7);
        root.right.right.left = new TreeNode(9);
        root.right.right.right = new TreeNode(8);

        TreeNode single = new TreeNode(1);
        System.out.printf("root=%s leaf=%d -> %d  expected=%d%n",
            "[5,4,6,3,14,null,7,null,null,null,null,9,8]", 14,
            new BurnTree().burnTree(root, 14), 5);
        System.out.printf("root=%s leaf=%d -> %d  expected=%d%n",
            "[1]", 1, new BurnTree().burnTree(single, 1), 0);
    }


        /**
     * Intuition: once the burning node is found, ancestors learn how many seconds
     * away they are as recursion unwinds. At each ancestor, fire also enters the
     * opposite subtree, so propagateFire measures the deepest node reached from
     * that side with the elapsed time already spent.
     *
     * Algorithm:
     *   1. DFS left before right to find leafValue.
     *   2. Return -1 from subtrees that do not contain the burning node.
     *   3. When a child path contains the start, update maxBurnTime with that distance.
     *   4. Propagate fire into the opposite child using the known elapsed time.
     *   5. Return the distance to the parent and finally return maxBurnTime.
     *
     * Time:  O(n) - each node is visited by search or propagation a constant number of times.
     * Space: O(h) - recursion follows tree height.
     *
     * @param root root of the binary tree
     * @param leafValue value where the fire starts
     * @return seconds needed to burn the whole tree
     */
    public int burnTree(TreeNode root, int leafValue) {
        findBurningNode(root, leafValue);
        return maxBurnTime;
    }

        // Finds the start node and returns its distance to each ancestor.
    private int findBurningNode(TreeNode node, int leafValue) {
        if (node == null) return -1;

        if (node.val == leafValue) return 0; // Found the leaf node, fire starts here

        // Recurse into left and right subtrees
        int leftDepth = findBurningNode(node.left, leafValue);
        if (leftDepth != -1) { // If leaf found in left subtree
            maxBurnTime = Math.max(maxBurnTime, leftDepth + 1);
            propagateFire(node.right, leftDepth + 1); // Burn right subtree
            return leftDepth + 1;
        }

        int rightDepth = findBurningNode(node.right, leafValue);
        if (rightDepth != -1) { // If leaf found in right subtree
            maxBurnTime = Math.max(maxBurnTime, rightDepth + 1);
            propagateFire(node.left, rightDepth + 1); // Burn left subtree
            return rightDepth + 1;
        }

        return -1; // Leaf node not found in this path
    }

        // Updates maxBurnTime for every node in a subtree reached after time seconds.
    private void propagateFire(TreeNode node, int time) {
        if (node == null) return;

        maxBurnTime = Math.max(maxBurnTime, time + 1);
        propagateFire(node.left, time + 1);
        propagateFire(node.right, time + 1);
    }
}

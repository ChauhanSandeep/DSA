package trees.dfs;

import trees.TreeNode;
import trees.Node;
import java.util.ArrayList;

/**
 * Problem: All Nodes Distance K in Binary Tree
 *
 * Given a binary tree, a target node value, and a distance k, return all node
 * values exactly k edges away from the target. Distance may go downward into the
 * target subtree or upward through ancestors and into opposite subtrees.
 *
 * Leetcode: https://leetcode.com/problems/all-nodes-distance-k-in-binary-tree/ (Medium)
 * Rating:   1663
 * Pattern:  Trees | DFS | Distance bubbling | Opposite-subtree search
 *
 * Example:
 *   Input:  root = [3,5,1,6,2,0,8,null,null,7,4], target = 5, k = 2
 *   Output: [7, 4, 1]
 *   Why:    7 and 4 are two edges below 5, and 1 is reached through parent 3.
 *
 * Follow-ups:
 *   1. How would you support repeated distance queries?
 *      Build parent pointers once, then BFS from each target.
 *   2. What if target is a node reference instead of a value?
 *      Compare object identity and avoid relying on unique values.
 *   3. What if edges have weights?
 *      Use Dijkstra from the target on the implicit parent-child graph.
 *
 * Related: Amount of Time for Binary Tree to Be Infected (2385).
 */
public class KDistance {

        public static void main(String[] args) {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(5);
        root.right = new TreeNode(1);
        root.left.left = new TreeNode(6);
        root.left.right = new TreeNode(2);
        root.right.left = new TreeNode(0);
        root.right.right = new TreeNode(8);
        root.left.right.left = new TreeNode(7);
        root.left.right.right = new TreeNode(4);

        TreeNode chain = new TreeNode(0);
        chain.right = new TreeNode(1);
        chain.right.right = new TreeNode(2);
        chain.right.right.right = new TreeNode(3);

        KDistance solver = new KDistance();
        System.out.printf("root=%s target=%d k=%d -> %s  expected=%s%n",
            "[3,5,1,6,2,0,8,null,null,7,4]", 5, 2,
            solver.distanceK(root, 5, 2), "[7, 4, 1]");
        System.out.printf("root=%s target=%d k=%d -> %s  expected=%s%n",
            "[0,null,1,null,2,null,3]", 1, 2,
            solver.distanceK(chain, 1, 2), "[3]");
    }


        /**
     * Intuition: the target can be found by DFS without building a parent map.
     * When recursion returns from the target, each ancestor learns how far it is
     * from target. If the ancestor is not exactly distance away, the missing
     * distance must be searched in the opposite child subtree.
     *
     * Algorithm:
     *   1. DFS until the node with value target is found.
     *   2. From the target, mark descendants at the requested distance.
     *   3. While unwinding, add an ancestor if its returned distance equals distance.
     *   4. Otherwise search the opposite subtree with the already-traveled distance.
     *
     * Time:  O(n) - every node is visited by at most one search path or child marking.
     * Space: O(h) - recursion stack height, excluding the result list.
     *
     * @param root root of the binary tree
     * @param target value of the target node
     * @param distance requested edge distance from target
     * @return node values exactly distance edges away
     */
    public ArrayList<Integer> distanceK(TreeNode root, int target, int distance) {
        ArrayList<Integer> result = new ArrayList<>();
        findNodes(root, target, distance, result);
        return result;
    }

        // Finds target and returns its distance to ancestors, or -1 when absent.
    private int findNodes(TreeNode node, int target, int distance, ArrayList<Integer> result) {
        if (node == null) return -1; // If current node is null, return -1.

        // If the current node is the target node, start marking children at distance K
        if (node.val == target) {
            markChild(node, distance, 0, result); // Start marking from the target node.
            return 1; // Return 1 to signify that we've found the target node.
        }

        // Search in the left subtree
        int left = findNodes(node.left, target, distance, result);

        // If the target is found in the left subtree and is within the distance
        if (left > 0 && left <= distance) {
            if (left == distance) {
                result.add(node.val); // Add the current node to the result if it is at distance K
                return left + 1; // Return the distance from the current node to its parent
            }
            // Otherwise, continue searching in the right subtree
            markChild(node.right, distance, left + 1, result);
            return left + 1;
        }

        // Search in the right subtree
        int right = findNodes(node.right, target, distance, result);

        // If the target is found in the right subtree and is within the distance
        if (right > 0 && right <= distance) {
            if (right == distance) {
                result.add(node.val); // Add the current node to the result if it is at distance K
                return right + 1;
            }
            // Otherwise, continue searching in the left subtree
            markChild(node.left, distance, right + 1, result);
            return right + 1;
        }

        return -1; // Return -1 if the target node is not found in this subtree.
    }

    /**
     * A helper function that marks all nodes at a certain distance starting from the given node.
     *
     * @param node The current node being explored.
     * @param distance The distance `K` from the target node.
     * @param curr The current distance from the target node.
     * @param result The list to store nodes at distance K from the target.
     */
    public void markChild(TreeNode node, int distance, int curr, ArrayList<Integer> result) {
        if (node == null || curr > distance) return; // Stop if we reach a null node or exceed the distance.

        // If the current distance matches the target distance, add the node value to the result
        if (curr == distance) result.add(node.val);

        // Recur for both left and right children
        markChild(node.left, distance, curr + 1, result);
        markChild(node.right, distance, curr + 1, result);
    }

}

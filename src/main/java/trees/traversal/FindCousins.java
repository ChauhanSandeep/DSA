package trees.traversal;

import trees.Node;
import trees.TreeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Problem: Cousins in Binary Tree
 *
 * Given a binary tree and a target value, return the values of nodes on the same
 * level as the target that do not share its parent. The root has no cousins.
 *
 * Leetcode: n/a (InterviewBit cousins in binary tree)
 * Rating:   not available
 * Pattern:  Trees | DFS | Level grouping | Sibling exclusion
 *
 * Example:
 *   Input:  root = [1,2,3,4,5,6,7], target = 5
 *   Output: [6, 7]
 *   Why:    nodes 6 and 7 are at node 5's level but have parent 3, not parent 2.
 *
 * Follow-ups:
 *   1. How would you return a boolean for two target nodes being cousins?
 *      Track each target's depth and parent, then compare both fields.
 *   2. Can BFS make sibling removal simpler?
 *      Process one level at a time and skip all children of the target's parent.
 *   3. What if values are not unique?
 *      Accept a node reference or augment traversal with identity information.
 */
public class FindCousins {

    // Map to store nodes at each level of the tree
    private Map<Integer, ArrayList<Integer>> levelNodesMap;

    // Variable to store the level of the target node
    private int targetNodeLevel;

    public static void main(String[] args) {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);

        FindCousins finder = new FindCousins();
        System.out.printf("root=%s target=%d -> %s  expected=%s%n",
            "[1,2,3,4,5,6,7]", 5, finder.solve(root, 5), "[6, 7]");
        System.out.printf("root=%s target=%d -> %s  expected=%s%n",
            "[1,2,3,4,5,6,7]", 2, new FindCousins().solve(root, 2), "[]");
    }


        /**
     * Intuition: cousins are just the target's level group after removing the
     * target and its sibling. traverse fills levelNodesMap while searching for
     * target; when target is found in one child, the other child value is removed
     * from the next level's list.
     *
     * Algorithm:
     *   1. Return an empty list for null root or when target is the root.
     *   2. Initialize levelNodesMap and targetNodeLevel.
     *   3. DFS from root, adding non-target nodes to their currentLevel bucket.
     *   4. When target is found under a parent, remove its sibling from the target level.
     *   5. Return the bucket stored at targetNodeLevel.
     *
     * Time:  O(n) - each node is visited once.
     * Space: O(n) - levelNodesMap can store every non-target value.
     *
     * @param root root of the binary tree
     * @param target target node value
     * @return values of target's cousins
     */
    public ArrayList<Integer> solve(TreeNode root, int target) {
        // Edge case: if the root is null or the target node is the root itself
        if (root == null || root.val == target) {
            return new ArrayList<>();
        }

        // Initialize the map to store nodes at each level
        levelNodesMap = new HashMap<>();
        targetNodeLevel = -1;

        // Perform the DFS to find the target node and populate the map with nodes at each level
        traverse(root, target, 0);

        // Return the list of cousins at the target node's level, excluding the target's sibling
        return levelNodesMap.get(targetNodeLevel);
    }

        // DFS that records level buckets and signals when target was found below a node.
    private boolean traverse(TreeNode node, int target, int currentLevel) {
        if (node == null) {
            return false;
        }

        // Add the node's value to the map at the current level if not already present
        levelNodesMap.putIfAbsent(currentLevel, new ArrayList<>());

        // If the current node is the target, store its level and return true
        if (node.val == target) {
            targetNodeLevel = currentLevel;
            return true;
        }

        // Add the current node's value to the map for the current level
        levelNodesMap.get(currentLevel).add(node.val);

        // Recursively search for the target in the left subtree
        boolean isLeftChildTarget = traverse(node.left, target, currentLevel + 1);

        // If target is found in the left subtree, no need to search the right subtree
        if (isLeftChildTarget) {
            return false;
        }

        // Recursively search for the target in the right subtree
        boolean isRightChildTarget = traverse(node.right, target, currentLevel + 1);

        // If the target is found in the right subtree and the left child exists,
        // remove the left child from the cousins list
        if (isRightChildTarget && node.left != null) {
            levelNodesMap.get(currentLevel + 1).remove(Integer.valueOf(node.left.val));
        }

        return false;
    }
}

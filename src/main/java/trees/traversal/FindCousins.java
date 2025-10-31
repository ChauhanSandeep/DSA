package trees.traversal;

import trees.Node;
import trees.TreeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Given a binary tree and a target node, find all the cousins of the target node.
 * Cousins are nodes at the same level in the tree, but with different parents.
 *
 * Intuition:
 * - To find the cousins, we need to first locate the target node and its level.
 * - Once the level is determined, we can identify all nodes at that level except for the siblings of the target node.
 *
 * Algorithm:
 * 1. Perform a level order traversal of the binary tree.
 * 2. Track nodes at each level in a map.
 * 3. Once the target node is found, retrieve all nodes at the same level, except the target's sibling.
 *
 * Time Complexity:
 * - The time complexity is O(n), where n is the number of nodes in the binary tree.
 *   We visit each node exactly once.
 *
 * Space Complexity:
 * - The space complexity is O(n), which is required to store the nodes at each level in a map.
 *
 * InterviewBit Link:
 * https://www.interviewbit.com/problems/cousins-in-binary-tree/
 */
public class FindCousins {

    // Map to store nodes at each level of the tree
    private Map<Integer, ArrayList<Integer>> levelNodesMap;

    // Variable to store the level of the target node
    private int targetNodeLevel;

    /**
     * Main method to test the FindCousins functionality.
     */
    public static void main(String[] args) {
        /**
         * Construct the following binary tree:
         *                   1
         *                  / \
         *                 2   3
         *               / \  / \
         *              4  5 6   7
         */
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);

        // Find cousins of node with value 5
        FindCousins finder = new FindCousins();
        ArrayList<Integer> cousins = finder.solve(root, 5);

        // Print the cousins
        System.out.println(cousins);
    }

    /**
     * This method returns a list of cousin nodes of the target node.
     *
     * @param root   The root of the binary tree.
     * @param target The value of the target node.
     * @return A list of integers representing the cousins of the target node.
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

    /**
     * A helper method to perform a DFS traversal of the tree and populate the levelNodesMap.
     * It also identifies the level of the target node.
     *
     * @param node     The current node being visited.
     * @param target   The value of the target node.
     * @param currentLevel The current level in the tree.
     * @return boolean indicating whether the target node was found.
     */
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

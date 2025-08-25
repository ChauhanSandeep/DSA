package tree;

import java.util.HashMap;
import java.util.Map;

/**
 * Given the root of a binary tree and an integer targetSum, this program returns the number of paths where the sum of the values along the path equals targetSum.
 * The path must go downwards (i.e., traveling only from parent nodes to child nodes), and it does not need to start or end at the root or a leaf.
 *
 * Algorithm:
 * - We use a depth-first search (DFS) approach to traverse the tree.
 * - As we traverse, we maintain the cumulative sum (`currSum`) and use a hashmap to track the number of times a particular sum has occurred.
 * - If at any point the difference between the current sum and the target sum (`currSum - targetSum`) exists in the hashmap, it means we've found a path that sums up to the target.
 *
 * Time Complexity: O(N), where N is the number of nodes in the tree.
 * Space Complexity: O(H), where H is the height of the tree (due to recursion stack and hashmap storage).
 *
 * LeetCode Problem Link: https://leetcode.com/problems/path-sum-iii/
 */
public class PathSum3 {

    // Variable to store the result (number of paths with sum equal to targetSum)
    private int result;

    public static void main(String[] args) {
        // Constructing a test tree
        TreeNode root = new TreeNode(10);
        root.left = new TreeNode(5);
        root.left.left = new TreeNode(3);
        root.left.left.left = new TreeNode(3);
        root.left.left.right = new TreeNode(-2);
        root.left.right = new TreeNode(2);
        root.left.right.right = new TreeNode(1);
        root.right = new TreeNode(-3);
        root.right.right = new TreeNode(11);

        // Create an instance of the PathSum3 class and call the pathSum method
        int result = new PathSum3().pathSum(root, 8);

        // Print the result
        System.out.println("Number of paths with sum equal to target: " + result);
    }

    /**
     * This method starts the path sum calculation and initiates the DFS traversal.
     *
     * @param root The root node of the binary tree.
     * @param sum The target sum to find paths for.
     * @return The number of paths where the sum of values equals targetSum.
     */
    public int pathSum(TreeNode root, int sum) {
        // Initialize the result and hashmap to store cumulative sums
        Map<Integer, Integer> map = new HashMap<>();

        // Start DFS traversal from the root node
        pathSum(root, 0, sum, map);

        // Return the total result
        return result;
    }

    /**
     * This helper method performs a DFS traversal and checks for paths with sum equal to the target.
     *
     * @param root The current node being processed.
     * @param currSum The cumulative sum from the root to the current node.
     * @param targetSum The target sum we are trying to find.
     * @param sumFrequencyMap A hashmap storing the frequency of cumulative sums encountered during traversal like <sum, frequency>.
     */
    private void pathSum(TreeNode root, int currSum, int targetSum, Map<Integer, Integer> sumFrequencyMap) {
        if (root == null) {
            return;
        }

        // Update the cumulative sum by adding the current node's value
        currSum += root.val;

        // If the current sum equals the target sum, increment the result count
        if (currSum == targetSum) {
            result++;
        }

        // If there exists a previous sum that, when subtracted from the current sum, equals the target sum, it means a valid path is found
        int complementarySum = currSum - targetSum;
        if (sumFrequencyMap.containsKey(complementarySum)) {
            result += sumFrequencyMap.get(complementarySum);
        }

        // Add the current sum to the sumFrequencyMap (for future paths to use)
        sumFrequencyMap.put(currSum, sumFrequencyMap.getOrDefault(currSum, 0) + 1);

        // Traverse the left and right subtrees
        pathSum(root.left, currSum, targetSum, sumFrequencyMap);
        pathSum(root.right, currSum, targetSum, sumFrequencyMap);

        // After traversing both children, backtrack by removing the current sum from the sumFrequencyMap
        sumFrequencyMap.put(currSum, sumFrequencyMap.get(currSum) - 1);
        if (sumFrequencyMap.get(currSum) == 0) {
            sumFrequencyMap.remove(currSum);
        }
    }

    // TreeNode class to represent each node in the binary tree
    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
            this.left = this.right = null;
        }
    }
}

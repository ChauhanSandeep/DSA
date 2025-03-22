package Tree;
import java.util.*;

/**
 * This class calculates the number of unique Binary Search Trees (BSTs) 
 * that can be formed with 'n' nodes. 
 * 
 * Intuition:
 * - A binary search tree is constructed by selecting each node as the root
 *   and recursively constructing left and right subtrees with the remaining nodes.
 * - The problem is solved using Dynamic Programming (DP) by using a memoization approach to store
 *   already computed results and avoid redundant computations.
 * 
 * Algorithm:
 * - For each number of nodes from 1 to 'n', compute the number of BSTs by 
 *   iterating over each node as the root and calculating the product of the number of 
 *   possible left and right subtrees.
 * - Use a map (memoization) to store the number of unique BSTs for each number of nodes.
 * 
 * Time Complexity: O(n^2) - For each node count (1 to n), we iterate through all previous nodes.
 * Space Complexity: O(n) - We store the results in a map of size 'n'.
 * 
 * LeetCode Problem Link: https://leetcode.com/problems/unique-binary-search-trees/
 */
public class UniqueBinaryTree {

    public static void main(String[] args) {
        // Example usage: Find the number of unique BSTs that can be formed with 3 nodes
        int result = new UniqueBinaryTree().numTrees(3);
        System.out.println(result); // Expected output: 5
    }

    /**
     * This method calculates the number of unique binary search trees that can be created with 'n' nodes.
     * 
     * @param n The number of nodes.
     * @return The number of unique BSTs.
     */
    public int numTrees(int n) {
        // Memoization map to store already computed results
        Map<Integer, Integer> memo = new HashMap<>();
        
        // Base cases
        memo.put(0, 1); // 0 nodes -> 1 BST (empty tree)
        memo.put(1, 1); // 1 node -> 1 BST (single node tree)
        
        return countUniqueBSTs(n, memo);
    }

    /**
     * Helper method that computes the number of unique BSTs using memoization.
     * 
     * @param n The number of nodes.
     * @param memo The memoization map to store results of subproblems.
     * @return The number of unique BSTs that can be formed with 'n' nodes.
     */
    private int countUniqueBSTs(int n, Map<Integer, Integer> memo) {
        // Return the result from the memoization map if it is already computed
        if (memo.containsKey(n)) {
            return memo.get(n);
        }

        // Calculate the number of unique BSTs for 'n' nodes
        int numBSTs = 0;
        for (int root = 1; root <= n; root++) {
            // Calculate the number of left and right subtrees recursively
            int leftSubtreeCount = countUniqueBSTs(root - 1, memo);
            int rightSubtreeCount = countUniqueBSTs(n - root, memo);
            
            // The total number of unique BSTs is the product of left and right subtrees
            numBSTs += leftSubtreeCount * rightSubtreeCount;
        }

        // Store the computed result in the memoization map
        memo.put(n, numBSTs);
        return numBSTs;
    }
}

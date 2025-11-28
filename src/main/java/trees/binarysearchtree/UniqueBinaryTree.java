package trees.binarysearchtree;

import trees.Node;
import java.util.*;

/**
 * This class calculates the number of unique Binary Search Trees (BSTs)
 * that can be formed with 'n' nodes.
 *
 * LeetCode Problem Link: https://leetcode.com/problems/unique-binary-search-trees/
 */
public class UniqueBinaryTree {

    public static void main(String[] args) {
        // Example usage: Find the number of unique BSTs that can be formed with 3 nodes
        UniqueBinaryTree solution = new UniqueBinaryTree();
        
        int n = 3;
        System.out.println("Recursive DP: " + solution.numTreesRecursive(n)); // Expected output: 5
        System.out.println("Iterative DP: " + solution.numTreesIterative(n)); // Expected output: 5
        System.out.println("Catalan Number: " + solution.numTreesCatalan(n)); // Expected output: 5
    }

    /**
     * This method calculates the number of unique binary search trees that can be created with 'numNodes' nodes.
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
     * @param numNodes The number of nodes.
     * @return The number of unique BSTs.
     */
    public int numTreesRecursive(int numNodes) {
        // Memoization map to store already computed results
        Map<Integer, Integer> memo = new HashMap<>();

        // Base cases
        memo.put(0, 1); // 0 nodes -> 1 BST (empty tree)
        memo.put(1, 1); // 1 node -> 1 BST (single node tree)

        return countUniqueBSTs(numNodes, memo);
    }

    /**
     * Helper method that computes the number of unique BSTs using memoization.
     *
     * @param numNodes The number of nodes.
     * @param memo The memoization map to store results of subproblems.
     * @return The number of unique BSTs that can be formed with 'numNodes' nodes.
     */
    private int countUniqueBSTs(int numNodes, Map<Integer, Integer> memo) {
        // Return the result from the memoization map if it is already computed
        if (memo.containsKey(numNodes)) {
            return memo.get(numNodes);
        }

        // Calculate the number of unique BSTs for 'numNodes' nodes
        int numBSTs = 0;
        for (int root = 1; root <= numNodes; root++) {
            // Calculate the number of left and right subtrees recursively
            int leftSubtreeCount = countUniqueBSTs(root - 1, memo);
            int rightSubtreeCount = countUniqueBSTs(numNodes - root, memo);

            // The total number of unique BSTs is the product of left and right subtrees
            // This is because each combination of left and right subtrees can form a unique BST
            numBSTs += leftSubtreeCount * rightSubtreeCount;
        }

        // Store the computed result in the memoization map
        memo.put(numNodes, numBSTs);
        return numBSTs;
    }


    /**
     * Iterative Dynamic Programming solution for Unique Binary Search Trees
     * Steps
     * 1. Create a dp array where dp[i] represents the number of unique BSTs that can be formed with i nodes.
     * 2. Initialize base cases: dp[0] = 1 (empty tree) and dp[1] = 1 (single node tree).
     * 3. For each number of nodes from 2 to numNodes, calculate dp[i]:
     *    - For each node as the root, multiply the number of unique BSTs possible in the left subtree
     *      and the right subtree, and sum these products to get dp[i].
     * 
     * Time Complexity: O(numNodes^2)
     * Space Complexity: O(numNodes)
     */
    public int numTreesIterative(int numNodes) {
        int[] dp = new int[numNodes + 1];
        dp[0] = 1;
        dp[1] = 1;

        // Build dp array iteratively
        for (int node = 2; node <= numNodes; node++) {
            for (int root = 1; root <= node; root++) {
                dp[node] += dp[root - 1] * dp[node - root];
            }
        }
        return dp[numNodes];
    }

    /**
     * Catalan Number solution for Unique Binary Search Trees
     *
     * Intuition:
     * - The number of unique BSTs with n nodes is the nth Catalan number.
     * - Catalan numbers have the formula: C(n) = (2n)! / ((n+1)! * n!)
     * - This can be simplified to: C(n) = C(2n, n) / (n+1)
     *   where C(2n, n) is the binomial coefficient "2n choose n"
     *
     * Mathematical Formula:
     * - C(n) = (2n choose n) / (n+1)
     * - C(n) = (2n)! / ((n+1)! * n!)
     * - Alternatively: C(n) = Product from i=2 to n of (n+i)/i
     *
     * Why Catalan Numbers?
     * - Each way to arrange n nodes in a BST corresponds to a Catalan structure
     * - This is because we're counting the number of ways to parenthesize expressions,
     *   which is equivalent to counting binary tree structures
     *
     * Algorithm:
     * - We use the iterative formula: C(n) = C(n) * (4*i - 2) / (i + 1)
     * - Starting with C(0) = 1, we compute each subsequent Catalan number
     * - This approach avoids factorial overflow and is more efficient
     *
     * Time Complexity: O(n) - Single pass through n iterations
     * Space Complexity: O(1) - Only using a constant amount of extra space
     *
     * @param numNodes The number of nodes
     * @return The number of unique BSTs (nth Catalan number)
     */
    public int numTreesCatalan(int numNodes) {
        // Use long to avoid integer overflow during calculation
        long catalan = 1;

        // Calculate nth Catalan number using the formula:
        // C(n) = C(n-1) * (4*n - 2) / (n + 1)
        // This is derived from: C(n) = (2n)! / ((n+1)! * n!)
        for (int i = 1; i <= numNodes; i++) {
            // Multiply first to maintain precision, then divide
            catalan = catalan * (4L * i - 2) / (i + 1);
        }

        return (int) catalan;
    }
}

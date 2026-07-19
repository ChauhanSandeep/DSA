package trees.binarysearchtree;

import trees.Node;

/**
 * Problem: Count BST Permutations With Height Limit
 *
 * Count how many BST shapes can be formed with a fixed number of nodes while not
 * exceeding a maximum height. Each possible root splits the remaining nodes into
 * left and right subtrees that must both fit in one less remaining height.
 *
 * Leetcode: https://leetcode.com/problems/unique-binary-search-trees-ii/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Trees | BST counting | Recursive Catalan-style partitioning
 *
 * Example:
 *   Input:  numOfNodes = 3, maxHeight = 2
 *   Output: 5
 *   Why:    all five 3-node BST shapes fit when two edges of height are allowed.
 *
 * Follow-ups:
 *   1. How would you count trees of exactly height h?
 *      Subtract count(height <= h-1) from count(height <= h).
 *   2. How would you avoid repeated subproblems?
 *      Memoize by (numNodes, remainingHeightAllowed).
 *   3. How would you include labeled insertion orders?
 *      Multiply subtree counts by combinations choosing which labels go left.
 *   4. How would you return actual trees instead of counts?
 *      Generate every root choice and combine every left/right subtree pair.
 *
 * Related: Unique Binary Search Trees (96), Unique Binary Search Trees II (95).
 */

public class PermutationBst {

    private int[] memoizationArray;

        public static void main(String[] args) {
        PermutationBst solver = new PermutationBst();
        int[][] inputs = { {3, 2}, {1, 0} };
        int[] expected = {5, 1};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.countTrees(inputs[i][0], inputs[i][1]);
            System.out.printf("numOfNodes=%d maxHeight=%d -> %d  expected=%d%n",
                inputs[i][0], inputs[i][1], got, expected[i]);
        }
    }


        /**
     * Intuition: choosing a root decides exactly how many nodes must go left and how
     * many go right. Both subtrees consume one level of allowed height, and every left
     * shape can pair with every right shape.
     *
     * Algorithm:
     *   1. Call the recursive counter with numOfNodes and maxHeight.
     *   2. For 0 or 1 nodes, return one possible tree.
     *   3. If height is exhausted with more nodes, return zero.
     *   4. Try each root, multiply left and right counts, and add the products.
     *
     * Time:  O(Catalan-style exponential) - the original recursion recomputes subproblems.
     * Space: O(h) - recursion depth follows the remaining height.
     *
     * @param numOfNodes number of nodes to place
     * @param maxHeight maximum allowed height counter
     * @return number of valid BST shapes under the height limit
     */
    public int countTrees(int numOfNodes, int maxHeight) {
        return countTreesRecursive(numOfNodes, maxHeight);
    }

        /**
     * Counts BST shapes for numNodes while decreasing the remaining height each level.
     */
    private int countTreesRecursive(int numNodes, int remainingHeightAllowed) {
        // Base Case: With 0 or 1 node, only one BST is possible.
        if (numNodes <= 1) {
            return 1;
        }
        // If no remaining height is available but more than one node exists, no valid tree can be formed.
        if (remainingHeightAllowed == 0) {
            return 0;
        }

        int totalCount = 0;

        // For each possible choice of root, partition the nodes into left and right subtrees.
        for (int root = 1; root <= numNodes; root++) {
            int leftSubtrees = countTreesRecursive(root - 1, remainingHeightAllowed - 1);
            int rightSubtrees = countTreesRecursive(numNodes - root, remainingHeightAllowed - 1);
            totalCount += leftSubtrees * rightSubtrees;
        }

        return totalCount;
    }

    /**
     * Optimized approach (commented out in the original code) for counting the number of BSTs
     * using dynamic programming and combinatorics.
     *
     * @param nodes     The number of nodes
     * @param height    The maximum allowed height
     * @return The number of valid BSTs modulo 1e9+7
     */
    public int optimizedCountTrees(int nodes, int height) {
        int MOD = (int) (1e9 + 7);

        // Combinatorics table for binomial coefficients
        int[][] binomialCoeff = new int[nodes + 1][nodes + 1];
        // DP table to store the number of BSTs for a given node count and height
        long[][] dp = new long[nodes + 1][height + 1];

        // Initialize the binomial coefficients
        for (int i = 0; i <= nodes; i++) {
            binomialCoeff[i][0] = 1;
            binomialCoeff[i][i] = 1;
        }
        for (int i = 1; i <= nodes; i++) {
            for (int j = 1; j < i; j++) {
                binomialCoeff[i][j] = (binomialCoeff[i - 1][j - 1] + binomialCoeff[i - 1][j]) % MOD;
            }
        }

        // Initialize the DP table
        dp[0][0] = 1;
        dp[1][0] = 1;

        // DP to calculate the number of BSTs
        for (int len = 2; len <= nodes; len++) {
            for (int h = 1; h <= height; h++) {
                for (int i = 0; i < len; i++) {
                    int left = i;
                    int right = len - i - 1;
                    long current = 0;

                    // Combine results from left and right subtrees
                    for (int j = 0; j <= h - 2; j++) {
                        current = current + (dp[left][j] * dp[right][h - 1]) % MOD;
                        current = current + (dp[left][h - 1] * dp[right][j]) % MOD;
                    }

                    current = current + (dp[left][h - 1] * dp[right][h - 1]) % MOD;
                    current = (current * binomialCoeff[left + right][left]) % MOD;
                    dp[len][h] = (dp[len][h] + current) % MOD;
                }
            }
        }
        return (int) (dp[nodes][height]) % MOD;
    }
}

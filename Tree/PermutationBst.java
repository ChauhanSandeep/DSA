package Tree;

/**
 * Problem: Find the number of Binary Search Trees (BSTs) that can be created
 * with N nodes, with a maximum height of h.
 *
 * Intuition: The problem is a combination of dynamic programming (DP) and
 * combinatorics. The idea is to recursively calculate the number of BSTs 
 * for subproblems based on the number of nodes and height constraints.
 *
 * The total number of BSTs for a given number of nodes and a specific height 
 * is determined by the recursive partitioning of nodes into left and right subtrees,
 * ensuring that the maximum height constraint is adhered to.
 *
 * Algorithm:
 * 1. The base case: If the number of nodes is less than or equal to 1, we have only one possible BST (empty tree or single node).
 * 2. For each root node, recursively calculate the number of BSTs for the left and right subtrees.
 * 3. The height constraint is enforced by limiting the depth of recursive calls.
 * 4. The final result is the sum of all valid BSTs formed by different root placements.
 *
 * Time Complexity: O(N^2 * h), where N is the number of nodes, and h is the maximum height.
 * Space Complexity: O(N), for the memoization array.
 *
 * LeetCode Link: https://leetcode.com/problems/unique-binary-search-trees-ii/
 */

public class PermutationBst {
    
    private int[] memoizationArray;

    public static void main(String[] args) {
        System.out.println(new PermutationBst().countTrees(3, 2));  // Example with 3 nodes and height 2
    }

    /**
     * Public method to calculate the number of BSTs that can be formed
     * with 'N' nodes and a maximum height of 'h'.
     * 
     * @param numOfNodes The number of nodes in the BST
     * @param maxHeight  The maximum allowed height for the BST
     * @return The number of valid BSTs that can be formed
     */
    public int countTrees(int numOfNodes, int maxHeight) {
        this.memoizationArray = new int[numOfNodes];
        return calculateTrees(numOfNodes, maxHeight, 0);
    }

    /**
     * Helper method to recursively calculate the number of BSTs
     * that can be formed with the given number of nodes and height.
     * 
     * @param numNodes   The number of nodes in the subtree
     * @param maxHeight  The maximum allowed height for the subtree
     * @param currentHeight The current height of the subtree
     * @return The number of valid BSTs that can be formed
     */
    private int calculateTrees(int numNodes, int maxHeight, int currentHeight) {
        // Base case: If there are no nodes or only one node, there is one BST
        if (numNodes <= 1) {
            return 1;
        }

        int totalCount = 0;
        int leftSubtreeCount, rightSubtreeCount;

        // Try placing each node as the root and recursively calculate left and right subtrees
        for (int root = 1; root <= numNodes; root++) {
            if (currentHeight + 1 <= maxHeight) {
                leftSubtreeCount = calculateTrees(root - 1, maxHeight, currentHeight + 1);
                rightSubtreeCount = calculateTrees(numNodes - root, maxHeight, currentHeight + 1);

                // Memoize the result for root node placement at the first level
                if (currentHeight == 0) {
                    memoizationArray[root - 1] += leftSubtreeCount * rightSubtreeCount;
                }

                // Add the valid BSTs formed by this root placement
                totalCount += leftSubtreeCount * rightSubtreeCount;
            }
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

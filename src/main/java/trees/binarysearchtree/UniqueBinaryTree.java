package trees.binarysearchtree;

import trees.Node;
import java.util.*;

/**
 * Problem: Unique Binary Search Trees
 *
 * Given n distinct values 1..n, count how many structurally unique BSTs can be
 * built. Each choice of root splits the values into independent left and right
 * subtree counts.
 *
 * Leetcode: https://leetcode.com/problems/unique-binary-search-trees/ (Medium)
 * Rating:   not available (pre-contest problem)
 * Pattern:  Trees | Dynamic programming | Catalan numbers
 *
 * Example:
 *   Input:  n = 3
 *   Output: 5
 *   Why:    choosing roots 1, 2, and 3 yields 2 + 1 + 2 valid left/right combinations.
 *
 * Follow-ups:
 *   1. How would you generate the actual trees?
 *      Recursively combine every possible left and right subtree for each root.
 *   2. How would you compute very large n modulo M?
 *      Use DP modulo M or Catalan with modular inverses when M is prime.
 *   3. How would you count trees with a height limit?
 *      Add height as a DP state and decrease it for child subtrees.
 *   4. How would duplicates change the count?
 *      A fixed duplicate placement policy reduces choices; otherwise structures depend on multiset rules.
 *
 * Related: Unique Binary Search Trees II (95).
 */
public class UniqueBinaryTree {

        public static void main(String[] args) {
        UniqueBinaryTree solution = new UniqueBinaryTree();
        int[] inputs = {0, 3};
        int[] expected = {1, 5};

        for (int i = 0; i < inputs.length; i++) {
            int got = solution.numTreesRecursive(inputs[i]);
            System.out.printf("n=%d -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }


        /**
     * Intuition: after picking a root, every smaller value must be in the left subtree
     * and every larger value must be in the right subtree. If left has L shapes and
     * right has R shapes, that root contributes L * R complete trees.
     *
     * Algorithm:
     *   1. Seed memo with 0 nodes and 1 node both having one valid tree.
     *   2. Recursively ask for the count for numNodes.
     *   3. For each root position, multiply left and right subtree counts.
     *   4. Cache and return the total for that node count.
     *
     * Time:  O(n^2) - each node count tries every possible root once after memoization.
     * Space: O(n) - memo stores one count per node count plus recursion stack.
     *
     * @param numNodes number of distinct values
     * @return count of structurally unique BSTs
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
     * Returns the memoized Catalan count for the given number of nodes.
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

package Array;

import java.util.Arrays;

/**
 * Problem: Find the number of valid (x, y) pairs such that x^y > y^x.
 * LeetCode Link: https://leetcode.com/problems/count-pairs-for-which-x-y-y-x/
 *
 * Approach:
 * - Sort both input arrays to enable efficient pair comparison.
 * - Iterate over elements in X and count valid Y elements satisfying the condition.
 * - Leverage binary search or two-pointer techniques to optimize the process.
 *
 * Time Complexity: O(M log M + N log N + M * N) in the worst case.
 * Space Complexity: O(1), as sorting is in-place and no extra space is used apart from variables.
 */
public class FindPairs {
    public static void main(String[] args) {
        int[] X = {2, 1, 6};
        int[] Y = {1, 5};

        long pairs = countValidPairs(X, Y);
        System.out.println("Number of valid pairs: " + pairs);
    }

    /**
     * Counts pairs (x, y) where x^y > y^x.
     *
     * @param X First input array
     * @param Y Second input array
     * @return Count of valid (x, y) pairs
     */
    public static long countValidPairs(int[] X, int[] Y) {
        Arrays.sort(X); // Sort X to enable efficient traversal
        Arrays.sort(Y); // Sort Y for structured comparisons

        long validPairs = 0;
        int yIndex = 0;
        
        for (int x : X) {
            // Reset yIndex for each new x to ensure all pairs are considered
            yIndex = 0;
            while (yIndex < Y.length && Math.pow(x, Y[yIndex]) > Math.pow(Y[yIndex], x)) {
                validPairs++;
                yIndex++;
            }
        }
        return validPairs;
    }
}

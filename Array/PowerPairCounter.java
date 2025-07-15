package Array;

import java.util.Arrays;

/**
 * ✅ Problem: Count Pairs For Which x^y > y^x
 *
 * Given two integer arrays X and Y, find the number of pairs (x, y)
 * such that x^y > y^x.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/count-pairs-for-which-x-y-y-x/
 *
 * 🧠 Example:
 * Input: X = [2, 1, 6], Y = [1, 5]
 * Output: 3
 * Explanation: Valid pairs are (2,1), (6,1), (6,5)
 *
 * 🔍 Follow-ups:
 * 1. Why is binary search used? ➤ To efficiently count Ys > x, skipping brute-force
 * 2. Can this be solved without math.pow? ➤ Yes, avoid for small integers — use direct math rules
 * 3. Can you generalize for BigInteger? ➤ Yes, but would require `log` based comparison
 */
public class PowerPairCounter {

    public static void main(String[] args) {
        int[] X = {2, 1, 6};
        int[] Y = {1, 5};
        long result = countValidPairs(X, Y);
        System.out.println("Number of valid (x, y) pairs: " + result);
    }

    /**
     * Counts valid (x, y) pairs such that x^y > y^x using sorting and binary search.
     *
     * Time Complexity: O(M log M + N log N), where M = X.length, N = Y.length
     * Space Complexity: O(1), aside from constant-size arrays
     *
     * @param X input array X
     * @param Y input array Y
     * @return total valid (x, y) pairs
     */
    public static long countValidPairs(int[] X, int[] Y) {
        Arrays.sort(Y); // Binary search target
        int[] specialCount = preprocessSpecialY(Y); // For Y values 0 to 4

        long totalPairs = 0;

        for (int x : X) {
            totalPairs += countPairsForX(x, Y, specialCount);
        }

        return totalPairs;
    }

    /**
     * Precompute frequencies of Y values from 0 to 4 since they behave non-monotonically
     */
    private static int[] preprocessSpecialY(int[] Y) {
        int[] count = new int[5]; // only index 0 to 4 matter
        for (int y : Y) {
            if (y < 5) count[y]++;
        }
        return count;
    }

    /**
     * Computes number of valid Y values for a single X using math rules and binary search.
     */
    private static long countPairsForX(int x, int[] Y, int[] specialCount) {
        int n = Y.length;

        // Handle edge cases for small x
        if (x == 0) return 0;
        if (x == 1) return specialCount[0]; // Only y == 0 satisfies 1^y > y^1

        // Binary search to find number of Ys > x
        int index = upperBound(Y, x);
        long count = n - index;

        // Add special Y counts based on x
        count += specialCount[0] + specialCount[1]; // x^0 > 0^x and x^1 > 1^x for x > 1

        // Exceptions:
        if (x == 2) {
            // Remove overcounted Y=3 and Y=4
            count -= (specialCount[3] + specialCount[4]);
        }

        if (x == 3) {
            // Add back Y=2
            count += specialCount[2];
        }

        return count;
    }

    /**
     * Custom upperBound implementation to find first index in Y where y > x.
     */
    private static int upperBound(int[] arr, int x) {
        int left = 0, right = arr.length;

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] <= x) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }
}
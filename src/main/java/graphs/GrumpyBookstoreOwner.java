package graphs;


import java.util.Arrays;
/**
 * Problem: Grumpy Bookstore Owner
 *
 * A bookstore owner has customers each minute and is grumpy during some minutes.
 * For one contiguous window of length minutes, the owner can suppress grumpiness.
 * Return the maximum number of satisfied customers.
 *
 * Leetcode: https://leetcode.com/problems/grumpy-bookstore-owner/ (Medium)
 * Rating:   1418 (zerotrac Elo)
 * Pattern:  Array | Sliding window | Baseline plus best gain
 *
 * Example:
 *   Input:  customers = [1,0,1,2,1,1,7,5], grumpy = [0,1,0,1,0,1,0,1], minutes = 3
 *   Output: 16
 *   Why:    customers already satisfied contribute 10, and using the trick on
 *           minutes 5 through 7 adds 6 more otherwise-grumpy customers.
 *
 * Follow-ups:
 *   1. Choose two non-overlapping calm windows?
 *      Use prefix/suffix best-window gains or dynamic programming over windows.
 *   2. Window lengths vary by cost budget?
 *      Turn the gain array into a variable-size sliding window or knapsack variant.
 *   3. Return which window was chosen?
 *      Track the start index whenever the best extra gain improves.
 *
 * Related: Maximum Average Subarray I (643), Sliding Window Maximum (239).
 */
public class GrumpyBookstoreOwner {


    public static void main(String[] args) {
        GrumpyBookstoreOwner solver = new GrumpyBookstoreOwner();
        int[][] customers = {{1, 0, 1, 2, 1, 1, 7, 5}, {4, 10, 10}};
        int[][] grumpy = {{0, 1, 0, 1, 0, 1, 0, 1}, {0, 0, 0}};
        int[] minutes = {3, 2};
        int[] expected = {16, 24};

        for (int i = 0; i < customers.length; i++) {
            int output = solver.maxSatisfied(customers[i], grumpy[i], minutes[i]);
            System.out.printf("customers=%s grumpy=%s minutes=%d  ->  %d  expected=%d%n",
                Arrays.toString(customers[i]), Arrays.toString(grumpy[i]), minutes[i], output, expected[i]);
        }
    }
    /**
     * Intuition: customers during non-grumpy minutes are already satisfied, so the
     * secret technique should target one window where it recovers the most otherwise
     * lost customers. Sliding the window keeps that recoverable gain in O(1) update
     * time per minute.
     *
     * Algorithm:
     *   1. Add customers from non-grumpy minutes to the always-satisfied base total.
     *   2. Build a sliding window of grumpy-minute customers that could be recovered.
     *   3. Keep the maximum recoverable window sum of length X.
     *   4. Return base satisfaction plus that maximum extra gain.
     *
     * Time:  O(n) - one scan maintains the window and totals.
     * Space: O(1) - only counters and window sums are stored.
     *
     * @param customers customers entering each minute
     * @param grumpy 1 when the owner is grumpy at that minute, otherwise 0
     * @param X length of the secret technique window
     * @return maximum number of satisfied customers
     */
    public int maxSatisfied(int[] customers, int[] grumpy, int X) {
        int n = customers.length;

        // Calculate baseline satisfaction (naturally satisfied customers)
        int baseSatisfied = 0;
        for (int i = 0; i < n; i++) {
            if (grumpy[i] == 0) {
                baseSatisfied += customers[i];
            }
        }

        // Find maximum additional customers we can satisfy using technique
        int maxAdditional = 0;
        int currentAdditional = 0;

        // Initialize first window
        for (int i = 0; i < X && i < n; i++) {
            if (grumpy[i] == 1) {
                currentAdditional += customers[i];
            }
        }
        maxAdditional = currentAdditional;

        // Slide the window
        for (int i = X; i < n; i++) {
            // Add new element to window
            if (grumpy[i] == 1) {
                currentAdditional += customers[i];
            }

            // Remove element going out of window
            if (grumpy[i - X] == 1) {
                currentAdditional -= customers[i - X];
            }

            maxAdditional = Math.max(maxAdditional, currentAdditional);
        }

        return baseSatisfied + maxAdditional;
    }

    /**
     * Alternative approach using prefix sums for clearer logic.
     * Separates calculation of satisfied and potentially satisfiable customers.
     */
    public int maxSatisfiedPrefixSum(int[] customers, int[] grumpy, int X) {
        int n = customers.length;

        // Arrays to store cumulative counts
        int[] satisfied = new int[n];      // Naturally satisfied customers
        int[] unsatisfied = new int[n];    // Customers that could be saved

        // Build arrays
        for (int i = 0; i < n; i++) {
            if (grumpy[i] == 0) {
                satisfied[i] = customers[i];
                unsatisfied[i] = 0;
            } else {
                satisfied[i] = 0;
                unsatisfied[i] = customers[i];
            }
        }

        // Calculate total naturally satisfied customers
        int baseSatisfied = 0;
        for (int count : satisfied) {
            baseSatisfied += count;
        }

        // Find maximum customers we can save with technique
        int maxSaved = 0;
        for (int i = 0; i <= n - X; i++) {
            int saved = 0;
            for (int j = i; j < i + X; j++) {
                saved += unsatisfied[j];
            }
            maxSaved = Math.max(maxSaved, saved);
        }

        return baseSatisfied + maxSaved;
    }

    /**
     * Optimized sliding window with single pass calculation.
     * Combines baseline calculation with window sliding.
     */
    public int maxSatisfiedOptimized(int[] customers, int[] grumpy, int X) {
        int totalSatisfied = 0;
        int maxGain = 0;
        int currentGain = 0;

        for (int i = 0; i < customers.length; i++) {
            // Add to total if naturally satisfied
            if (grumpy[i] == 0) {
                totalSatisfied += customers[i];
            }

            // Calculate potential gain for current window
            if (grumpy[i] == 1) {
                currentGain += customers[i];
            }

            // Remove element going out of window
            if (i >= X && grumpy[i - X] == 1) {
                currentGain -= customers[i - X];
            }

            // Update maximum gain
            if (i >= X - 1) {
                maxGain = Math.max(maxGain, currentGain);
            }
        }

        return totalSatisfied + maxGain;
    }

    /**
     * Brute force approach for small inputs or verification.
     * Tests all possible positions for applying the technique.
     */
    public int maxSatisfiedBruteForce(int[] customers, int[] grumpy, int X) {
        int n = customers.length;
        int maxSatisfied = 0;

        // Try starting technique at each possible position
        for (int start = 0; start <= n - X; start++) {
            int satisfied = 0;

            for (int i = 0; i < n; i++) {
                if (grumpy[i] == 0 || (i >= start && i < start + X)) {
                    satisfied += customers[i];
                }
            }

            maxSatisfied = Math.max(maxSatisfied, satisfied);
        }

        return maxSatisfied;
    }

    /**
     * Two-pointer approach with explicit window tracking.
     * More verbose but clearer for understanding the algorithm.
     */
    public int maxSatisfiedTwoPointer(int[] customers, int[] grumpy, int X) {
        int n = customers.length;

        // Calculate baseline satisfied customers
        int baseSatisfied = 0;
        for (int i = 0; i < n; i++) {
            if (grumpy[i] == 0) {
                baseSatisfied += customers[i];
            }
        }

        // Use two pointers to maintain window of size X
        int left = 0, right = 0;
        int currentSaved = 0;
        int maxSaved = 0;

        while (right < n) {
            // Expand window
            if (grumpy[right] == 1) {
                currentSaved += customers[right];
            }

            // Shrink window if too large
            while (right - left + 1 > X) {
                if (grumpy[left] == 1) {
                    currentSaved -= customers[left];
                }
                left++;
            }

            // Update maximum if window is exactly size X
            if (right - left + 1 == X) {
                maxSaved = Math.max(maxSaved, currentSaved);
            }

            right++;
        }

        return baseSatisfied + maxSaved;
    }

    /**
     * Dynamic programming approach for multiple technique applications.
     * Extension for cases where technique can be used multiple times.
     */
    public int maxSatisfiedDP(int[] customers, int[] grumpy, int X) {
        int n = customers.length;

        // dp[i] = maximum satisfaction achievable considering first i minutes
        int[] dp = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            // Option 1: Don't use technique at position i-1
            int withoutTechnique = dp[i - 1];
            if (grumpy[i - 1] == 0) {
                withoutTechnique += customers[i - 1];
            }

            // Option 2: Use technique starting at position max(0, i-X)
            int withTechnique = 0;
            int start = Math.max(0, i - X);

            // Add satisfaction from using technique
            for (int j = start; j < i; j++) {
                withTechnique += customers[j];
            }

            // Add satisfaction from previous optimal solution
            if (start > 0) {
                withTechnique += dp[start];
            }

            dp[i] = Math.max(withoutTechnique, withTechnique);
        }

        return dp[n];
    }

    /**
     * Segment tree approach for range maximum queries.
     * Overkill for this problem but demonstrates advanced technique.
     */
    public int maxSatisfiedSegmentTree(int[] customers, int[] grumpy, int X) {
        // For this specific problem, sliding window is more appropriate
        // Segment tree would be useful if we had multiple queries
        return maxSatisfied(customers, grumpy, X);
    }
}

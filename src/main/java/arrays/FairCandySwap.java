package arrays;

import java.util.*;

/**
 * Problem: Fair Candy Swap
 *
 * Alice and Bob each have boxes of candy with given sizes. Return one box size
 * from Alice and one box size from Bob that they can swap so both people end up
 * with the same total candy. The problem guarantees that a valid swap exists.
 *
 * Leetcode: https://leetcode.com/problems/fair-candy-swap/ (Easy)
 * Rating:   1334 (zerotrac Elo)
 * Pattern:  Array | Hash set | Sum difference balancing
 *
 * Example:
 *   Input:  aliceSizes = [1,1], bobSizes = [2,2]
 *   Output: [1,2]
 *   Why:    Alice moves from total 2 to 3, Bob moves from total 4 to 3, so the
 *           totals become equal after swapping 1 and 2.
 *
 * Follow-ups:
 *   1. Return all valid swaps?
 *      Keep scanning Alice's boxes and collect every matching Bob size from the set.
 *   2. What if no valid swap is guaranteed?
 *      Return an empty array or optional result after the scan finds no match.
 *   3. Minimize the absolute size difference among valid swaps?
 *      Generate valid pairs with the same equation and track the best difference.
 *
 * Related: Two Sum (1), Find Common Characters (1002).
 */
public class FairCandySwap {

    public static void main(String[] args) {
        FairCandySwap solver = new FairCandySwap();

        int[][] aliceInputs = { {1, 1}, {1, 2, 5}, {2} };
        int[][] bobInputs = { {2, 2}, {2, 4}, {1, 3} };
        String[] expected = { "[1, 2]", "[5, 4]", "[2, 3]" };

        for (int i = 0; i < aliceInputs.length; i++) {
            int[] got = solver.fairCandySwap(aliceInputs[i], bobInputs[i]);
            System.out.printf("alice=%s bob=%s  ->  %s  expected=%s%n",
                Arrays.toString(aliceInputs[i]), Arrays.toString(bobInputs[i]),
                Arrays.toString(got), expected[i]);
        }
    }

    /**
     * Intuition: after swapping Alice box a with Bob box b, Alice loses a and gains b.
     * The totals become equal exactly when b = a - (aliceTotal - bobTotal) / 2. Put all
     * Bob box sizes in a set so each Alice box can ask for its one required partner in
     * constant time.
     *
     * Algorithm:
     *   1. Compute Alice's total, Bob's total, and their half-difference diff.
     *   2. Store Bob's box sizes in bobSet for fast lookup.
     *   3. For each aliceBox, compute requiredBobBox = aliceBox - diff.
     *   4. Return the first pair whose required Bob box exists.
     *
     * Time:  O(a + b) - both arrays are scanned once.
     * Space: O(b) - Bob's box sizes are stored in a hash set.
     *
     * @param aliceSizes Alice's candy box sizes
     * @param bobSizes Bob's candy box sizes
     * @return one valid [aliceBox, bobBox] swap, or [-1, -1] if none is found
     */
    public int[] fairCandySwap(int[] aliceSizes, int[] bobSizes) {
        int aliceTotal = Arrays.stream(aliceSizes).sum();
        int bobTotal = Arrays.stream(bobSizes).sum();

        // Difference that needs to be balanced
        int diff = (aliceTotal - bobTotal) / 2;

        // Create set for fast lookup of Bob's box sizes
        Set<Integer> bobSet = new HashSet<>();
        for (int size : bobSizes) {
            bobSet.add(size);
        }

        // Find valid swap
        for (int aliceBox : aliceSizes) {
            int requiredBobBox = aliceBox - diff;
            if (bobSet.contains(requiredBobBox)) {
                return new int[]{aliceBox, requiredBobBox};
            }
        }

        // Should never reach here given problem constraints
        return new int[]{-1, -1};
    }

    /**
     * Alternative approach using manual sum calculation
     * Time Complexity: O(n + m), Space Complexity: O(m)
     */
    public int[] fairCandySwapManual(int[] aliceSizes, int[] bobSizes) {
        int aliceTotal = 0;
        int bobTotal = 0;

        // Calculate totals manually
        for (int size : aliceSizes) aliceTotal += size;
        for (int size : bobSizes) bobTotal += size;

        int targetDiff = (aliceTotal - bobTotal) / 2;

        // Use set for Bob's sizes
        Set<Integer> bobSet = new HashSet<>();
        for (int size : bobSizes) {
            bobSet.add(size);
        }

        // Find the swap
        for (int aliceBox : aliceSizes) {
            int neededBobBox = aliceBox - targetDiff;
            if (bobSet.contains(neededBobBox)) {
                return new int[]{aliceBox, neededBobBox};
            }
        }

        return new int[]{-1, -1};
    }

    /**
     * Brute force approach for verification
     * Time Complexity: O(n * m),
     * Space Complexity: O(1)
     */
    public int[] fairCandySwapBruteForce(int[] aliceSizes, int[] bobSizes) {
        int aliceTotal = Arrays.stream(aliceSizes).sum();
        int bobTotal = Arrays.stream(bobSizes).sum();

        for (int aliceBox : aliceSizes) {
            for (int bobBox : bobSizes) {
                // Check if swap results in equal totals
                int newAliceTotal = aliceTotal - aliceBox + bobBox;
                int newBobTotal = bobTotal - bobBox + aliceBox;

                if (newAliceTotal == newBobTotal) {
                    return new int[]{aliceBox, bobBox};
                }
            }
        }

        return new int[]{-1, -1};
    }
}

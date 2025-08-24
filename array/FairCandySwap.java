package array;

import java.util.*;

/**
 * Fair Candy Swap
 *
 * Problem: Alice and Bob have candy boxes. Find one box from each person to swap
 * so that both end up with the same total amount of candy.
 *
 * Example: aliceSizes = [1,1], bobSizes = [2,2] -> Output: [1,2]
 * Alice gives 1, Bob gives 2. Both end up with total 2.
 *
 * LeetCode: https://leetcode.com/problems/fair-candy-swap
 *
 * Follow-up Questions:
 * - What if multiple valid swaps exist? (Return any one, current solution returns first found)
 * - How to find all possible fair swaps? (Modify to collect all valid pairs)
 * - What if no fair swap exists? (Problem guarantees solution exists)
 */
public class FairCandySwap {

    /**
     * Finds a fair candy swap between Alice and Bob.
     *
     * Algorithm:
     * 1. Calculate total candies for Alice and Bob
     * 2. Calculate the difference that needs balancing
     * 3. For each of Alice's boxes, calculate required Bob's box size
     * 4. Check if Bob has a box of that size using set lookup
     * 5. Return first valid swap found
     *
     * Time Complexity: O(n + m) where n, m are array lengths
     * Space Complexity: O(m) for storing Bob's box sizes in set
     *
     * @param aliceSizes Alice's candy box sizes
     * @param bobSizes Bob's candy box sizes
     * @return array [alice_box, bob_box] representing fair swap
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
     * Time Complexity: O(n * m), Space Complexity: O(1)
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

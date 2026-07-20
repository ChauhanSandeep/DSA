package bitwiseoperation;

import java.util.Arrays;

import java.util.HashSet;

/**
 * Problem: Maximum XOR of Two Numbers in an Array
 *
 * Given an array of non-negative integers, return the largest value obtainable
 * by XORing any two numbers from the array. The implementation builds the answer
 * greedily from the highest relevant bit to the lowest bit.
 *
 * Leetcode: https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/ (Medium)
 * Rating:   acceptance 53.6% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Bit manipulation | Prefix masks | Greedy XOR construction
 *
 * Example:
 *   Input:  arr = [5, 8, 2]
 *   Output: 13
 *   Why:    5 ^ 8 = 13, and no other pair can produce a larger XOR value.
 *
 * Follow-ups:
 *   1. How would you return the actual pair too?
 *      Store representative numbers for prefixes and reconstruct the two prefixes that prove a bit.
 *   2. How would you answer maximum-XOR queries against a fixed array?
 *      Build a binary trie once, then greedily walk opposite bits for each query.
 *   3. What if the array contains very large unsigned values?
 *      Use a wider type and define unsigned comparison for the highest bit.
 *   4. How would you handle a stream of incoming numbers?
 *      Insert each number into a trie and update the best XOR against prior numbers online.
 *
 * Related: Maximum XOR With an Element From Array (1707).
 */
public class MaximumXor {

    public static void main(String[] args) {
        MaximumXor solver = new MaximumXor();

        int[][] inputs = {
            {5, 8, 2},
            {0},
            {3, 10, 5, 25, 2, 8},
            {1 << 30, 0}
        };
        int[] expected = {13, 0, 28, 1 << 30};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.findMaximumXor(inputs[i]);
            System.out.printf("arr=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), output, expected[i]);
        }
    }

    /**
     * Intuition: XOR is maximized by making high bits differ first. The code keeps
     * a mask for the bits already being considered and stores every number's
     * prefix under that mask. If candidate is achievable, then for some prefix p
     * there must be another prefix equal to candidate ^ p, because p ^ other is
     * the desired XOR. When such prefixes exist, the current bit can stay set.
     *
     * Algorithm:
     *   1. Find the highest set bit present in the input array.
     *   2. Grow mask from that bit down to 0 and collect all num & mask prefixes.
     *   3. Try setting the current bit in maxXorValue as candidate.
     *   4. Keep candidate when two stored prefixes can XOR to it.
     *
     * Time:  O(n * b) - b is the highest bit index scanned, and each bit scans all numbers.
     * Space: O(n) - the prefix set can store one prefix per number.
     *
     * @param arr input array of non-negative integers
     * @return maximum XOR value achievable by two array elements
     */
    public int findMaximumXor(int[] arr) {
        int maxXorValue = 0;
        int mask = 0;
        int highestBit = findHighestBit(arr);

        // Iterate from highest significant bit to lowest
        for (int bit = highestBit; bit >= 0; bit--) {
            mask |= (1 << bit);
            HashSet<Integer> prefixes = new HashSet<>();

            // Extract current prefixes for this mask
            for (int num : arr) {
                prefixes.add(num & mask);
            }

            // Candidate for new maximum (try to set this bit)
            int candidate = maxXorValue | (1 << bit);

            // Check if any two prefixes can form candidate via XOR relation
            for (int prefix : prefixes) {
                if (prefixes.contains(candidate ^ prefix)) {
                    maxXorValue = candidate;
                    break;
                }
            }
            // No need to retain prefixes across bits, clear for next iteration
        }
        return maxXorValue;
    }

    /** Returns the highest set-bit index among all numbers in the array. */
    private int findHighestBit(int[] arr) {
        int max = 0;
        for (int num : arr) {
            max = Math.max(max, num);
        }
        return max == 0 ? -1 : 31 - Integer.numberOfLeadingZeros(max);
    }
}

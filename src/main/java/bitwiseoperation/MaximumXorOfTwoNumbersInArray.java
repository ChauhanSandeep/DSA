package bitwiseoperation;

import java.util.Arrays;

import java.util.HashSet;
import java.util.Set;


/**
 * Problem: Maximum XOR of Two Numbers in an Array
 *
 * Given an integer array nums, return the maximum value of nums[i] XOR nums[j]
 * across two numbers in the array. A binary trie makes it cheap to look for the
 * opposite bit at each position.
 *
 * Leetcode: https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/ (Medium)
 * Rating:   acceptance 53.6% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Bit manipulation | Binary trie | Opposite-bit greedy walk
 *
 * Example:
 *   Input:  nums = [3,10,5,25,2,8]
 *   Output: 28
 *   Why:    5 ^ 25 = 28, and choosing opposite high bits greedily finds that value.
 *
 * Follow-ups:
 *   1. How would you return the pair of numbers too?
 *      Store a representative number at each trie leaf and return it with the query number.
 *   2. How would you support maximum-XOR queries with an upper bound?
 *      Sort nums and queries by bound, inserting only eligible numbers into the trie.
 *   3. Can the trie depth be reduced?
 *      Start at the highest set bit seen in the input instead of always scanning 32 bits.
 *   4. How would you process a stream of numbers?
 *      Query the trie before inserting the new number, then update the running maximum.
 *
 * Related: Maximum XOR With an Element From Array (1707).
 */
public class MaximumXorOfTwoNumbersInArray {

    public static void main(String[] args) {
        MaximumXorOfTwoNumbersInArray solver = new MaximumXorOfTwoNumbersInArray();

        int[][] inputs = {
            {3, 10, 5, 25, 2, 8},
            {0},
            {2, 4}
        };
        int[] expected = {28, 0, 6};

        for (int i = 0; i < inputs.length; i++) {
            int output = solver.findMaximumXOR(inputs[i]);
            System.out.printf("nums=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), output, expected[i]);
        }
    }

    /**
     * Intuition: to maximize XOR, each bit wants to meet its opposite bit, and
     * the most significant wins matter first. The trie stores every number as a
     * path of 32 bits. For each query number, the original code walks from bit 31
     * to bit 0, choosing the opposite child when it exists and setting that bit in
     * xorValue; otherwise it follows the same-bit child because no better choice exists.
     *
     * Algorithm:
     *   1. Return 0 when fewer than two numbers are available.
     *   2. Insert every number into the binary trie using bits 31 down to 0.
     *   3. For each number, walk the trie preferring the opposite bit at each level.
     *   4. Keep the largest xorValue found across all trie walks.
     *
     * Time:  O(32 * n) - each number is inserted once and queried once across 32 bits.
     * Space: O(32 * n) - the trie can allocate a node per bit per number in the worst case.
     *
     * @param nums array of non-negative integers
     * @return maximum XOR value achievable by any pair
     */
    public int findMaximumXOR(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }

        TrieNode root = new TrieNode();

        // Build Trie with all numbers
        for (int num : nums) {
            insertIntoTrie(root, num);
        }

        int maxXor = 0;

        // For each number, find maximum XOR with numbers in Trie
        for (int num : nums) {
            int currentXor = findMaxXorInTrie(root, num);
            maxXor = Math.max(maxXor, currentXor);
        }

        return maxXor;
    }

    /** Inserts a number into the trie from bit 31 down to bit 0. */
    private void insertIntoTrie(TrieNode root, int num) {
        TrieNode current = root;

        // Process 31 bits (since numbers are non-negative, MSB is always 0)
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;

            if (current.children[bit] == null) {
                current.children[bit] = new TrieNode();
            }
            current = current.children[bit];
        }
    }

    /** Finds the best XOR for one number by preferring opposite trie bits. */
    private int findMaxXorInTrie(TrieNode root, int num) {
        TrieNode current = root;
        int xorValue = 0;

        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            int oppositeBit = 1 - bit;

            // Try to take opposite bit for maximum XOR
            if (current.children[oppositeBit] != null) {
                xorValue |= (1 << i);  // Set this bit in result
                current = current.children[oppositeBit];
            } else {
                // Opposite bit not available, take same bit
                current = current.children[bit];
            }
        }

        return xorValue;
    }

    /**
     * Alternative approach using HashSet and bit manipulation without Trie.
     * Builds answer bit by bit from MSB to LSB using prefix matching.
     *
     * Algorithm:
     * 1. For each bit position from MSB to LSB, try to set that bit in result
     * 2. Create set of prefixes of all numbers up to current bit
     * 3. For each prefix, check if there exists another prefix such that their XOR
     *    equals current tentative maximum
     * 4. If yes, that bit can be set in the result
     *
     * Time Complexity: O(N * 32) = O(N) where N is array length.
     *
     * Space Complexity: O(N) for the HashSet storing prefixes.
     *
     * @param nums array of non-negative integers
     * @return maximum XOR value achievable by any pair
     */
    public int findMaximumXORHashSet(int[] nums) {
        int maxXor = 0;
        int mask = 0;

        // Build result bit by bit from MSB to LSB
        for (int i = 31; i >= 0; i--) {
            mask |= (1 << i);  // Add current bit to mask

            Set<Integer> prefixes = new HashSet<>();
            for (int num : nums) {
                prefixes.add(num & mask);  // Get prefix up to current bit
            }

            int tentativeMax = maxXor | (1 << i);  // Try to set current bit

            // Check if we can achieve this tentativeMax
            for (int prefix : prefixes) {
                // If prefix ^ someOtherPrefix = tentativeMax, then someOtherPrefix = prefix ^ tentativeMax
                if (prefixes.contains(prefix ^ tentativeMax)) {
                    maxXor = tentativeMax;
                    break;
                }
            }
        }

        return maxXor;
    }

    /** Trie node with one child slot for bit 0 and one child slot for bit 1. */
    private static class TrieNode {
        TrieNode[] children = new TrieNode[2];
    }
}

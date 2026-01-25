package bitwiseoperation;

import java.util.HashSet;

/**
 * Problem: Find the Maximum XOR of Any Two Numbers in an Array
 *
 * Given an array of non-negative integers, find the maximum result of `ai XOR aj`, where i ≠ j.
 *
 * Example:
 * Input: arr = [5, 8, 2]
 * Output: 13
 * Explanation: 5 ^ 8 = 13 is the maximum possible XOR.
 *
 * LeetCode: Similar to https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/
 *
 * Follow-up Questions (FAANG-style):
 * 1. How would you return the actual pair (numbers) that yields the max XOR?
 *    - Store prefixes and check corresponding values that produce newMax during iteration.
 * 2. How can this be adapted for subarrays or contiguous segments?
 *    - Use Trie of prefixes for efficient range queries.
 * 3. What if the array contains negatives or is very large?
 *    - Use unsigned masks, take care with Java's sign extension, optimize with Trie or BitSet.
 * 4. How do you handle streaming data to maintain maximum XOR in real time?
 *    - Use online Trie insertions and maintain max as stream arrives.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class MaximumXor {

    public static void main(String[] args) {
        int[] arr = {5, 8, 2};
        MaximumXor instance = new MaximumXor();
        System.out.println(instance.findMaximumXor(arr)); // Expected: 13
    }

    /**
     * Finds the maximum XOR value of any two numbers in the array.
     *
     * Steps of Solution:
     * - Iteratively build a bitmask for the current bit position, from most significant to least.
     * - For each bit position, store all left-prefixes (i.e., arr[i] & mask) in a set.
     * - Try to "greedily" set the current bit in the maxXorValue and check if two prefixes exist that would produce newMax.
     * - Update maxXorValue if possible and continue to next bit position.
     *
     * Algorithm: Bit Manipulation, Set-based Prefix Lookup
     * Time Complexity: O(n * logM), where n = array length, M = max element value
     * Space Complexity: O(n) for prefix set
     *
     * @param arr Input array of integers
     * @return Maximum XOR value achievable by any two distinct elements
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

    /**
     * Finds the index of the highest set bit among all numbers in the array.
     *
     * @param arr The input array
     * @return Index of most significant bit (0-based, from right)
     */
    private int findHighestBit(int[] arr) {
        int max = 0;
        for (int num : arr) {
            max = Math.max(max, num);
        }
        int bitIdx = 0;
        while ((1 << bitIdx) <= max) {
            bitIdx++;
        }
        return bitIdx - 1; // Subtract 1 because last increment exceeded max
    }
}

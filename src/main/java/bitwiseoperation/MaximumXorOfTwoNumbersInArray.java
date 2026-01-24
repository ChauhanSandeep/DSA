package bitwiseoperation;

import java.util.HashSet;
import java.util.Set;


/**
 * Problem: Maximum XOR of Two Numbers in an Array
 *
 * Given an integer array nums, return the maximum result of nums[i] XOR nums[j],
 * where 0 <= i <= j < n.
 *
 * Example:
 * Input: nums = [3,10,5,25,2,8]
 * Output: 28
 * Explanation: The maximum result is 5 XOR 25 = 28.
 * Binary: 5 = 00101, 25 = 11001, XOR = 11100 = 28
 *
 * Constraints:
 * - 1 <= nums.length <= 2 * 10^5
 * - 0 <= nums[i] <= 2^31 - 1
 *
 * LeetCode Problem: https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array
 *
 * Follow-up Questions:
 *
 * 1. What if you need to find the actual pair of numbers instead of just the XOR value?
 *    Answer: Store the original number at each leaf node in the Trie. During traversal,
 *    return both the XOR value and the leaf number that achieved it.
 *
 * 2. How would you handle queries for maximum XOR with a specific number not in array?
 *    Answer: Build the Trie once with all array elements. For each query number, traverse
 *    the Trie choosing opposite bits. Time: O(N) build + O(log MAX) per query.
 *    Related problem: https://leetcode.com/problems/maximum-xor-with-an-element-from-array/
 *
 * 3. What if you need to find maximum XOR for all pairs and return top k results?
 *    Answer: Use a max heap to store XOR values. For each element, find its maximum XOR
 *    and add to heap. Keep heap size at k by removing minimum when size exceeds k.
 *
 * 4. Can you optimize space if numbers have many leading zeros?
 *    Answer: Find the most significant bit position across all numbers and only build
 *    Trie for those relevant bits. This reduces Trie depth from 32 to actual bit length.
 *
 * 5. How would you solve this in a streaming scenario where numbers arrive one by one?
 *    Answer: Maintain the Trie and update maximum XOR as each number arrives. Insert new
 *    number into Trie, compute its max XOR with existing numbers, update global maximum.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class MaximumXorOfTwoNumbersInArray {

    /**
     * Finds maximum XOR using Trie (prefix tree) for efficient bit matching.
     *
     * Algorithm:
     * 1. Build a binary Trie where each path represents a number's bit pattern
     * 2. For each number, traverse Trie trying to take opposite bit at each level
     * 3. Taking opposite bit maximizes XOR (0^1=1, 1^0=1 gives 1)
     * 4. Track maximum XOR value found across all numbers
     *
     * Key insight: To maximize XOR, we want bits to be as different as possible.
     * Starting from MSB, we greedily choose the opposite bit if available. Trie
     * allows O(32) lookup for each number instead of O(N) comparison.
     *
     * Time Complexity: O(N * 32) = O(N) where N is array length. We insert N numbers
     * into Trie (each takes 32 operations), then query N times (each takes 32 operations).
     *
     * Space Complexity: O(N * 32) = O(N) for Trie nodes. In worst case where all numbers
     * have completely different bit patterns, we create 32*N nodes.
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

    // Insert number into Trie starting from MSB
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

    // Find maximum XOR by traversing Trie and choosing opposite bits
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

    // Trie node with two children (for bits 0 and 1)
    private static class TrieNode {
        TrieNode[] children = new TrieNode[2];
    }
}

package hashing;

import java.util.ArrayList;
import java.util.List;


/**
* You are given a string s. Partition the string into as many parts as possible so that 
 * each letter appears in at most one part.
 * 
 * Return a list of integers representing the size of these parts.
 *
 * Example:
 * Input: s = "ababcbacadefegdehijhklij"
 * Output: [9,7,8]
 * Explanation:
 * The partition is "ababcbaca", "defegde", "hijhklij".
 * - "ababcbaca": 'a', 'b', 'c' appear only in this partition
 * - "defegde": 'd', 'e', 'f', 'g' appear only in this partition
 * - "hijhklij": 'h', 'i', 'j', 'k', 'l' appear only in this partition
 * This is the maximum number of partitions possible.
 *
 * LeetCode link: https://leetcode.com/problems/partition-labels/
 *
 * Follow-up Questions FAANG Interviews Might Ask:
 *  - Can you return the actual partitions instead of just their sizes?
 *    → Store substring start/end indices and extract substrings at the end.
 *  - What if we need to minimize the number of partitions instead?
 *    → Merge adjacent partitions greedily while maintaining the constraint.
 *  - How would you handle Unicode characters or case sensitivity?
 *    → Use HashMap instead of array for last occurrence mapping.
 *  - What if multiple valid maximum partitions exist?
 *    → Problem guarantees unique solution due to greedy nature, but could track alternatives.
 *
 * Relevant Follow-up Problems:
 *  - LeetCode 56 (Merge Intervals): https://leetcode.com/problems/merge-intervals/
 *  - LeetCode 435 (Non-overlapping Intervals): https://leetcode.com/problems/non-overlapping-intervals/
 *  - LeetCode 1024 (Video Stitching): https://leetcode.com/problems/video-stitching/
 */
public class PartitionLabels {

   /**
     * Main method: Finds maximum partitions using Greedy approach with last occurrence tracking.
     * Step-by-step:
     *  1. Pre-process: Record last occurrence index of each character in string
     *  2. Initialize partition tracking variables:
     *     - partitionStart: beginning of current partition
     *     - partitionEnd: farthest last occurrence seen so far in current partition
     *  3. Iterate through string:
     *     a. For each character, update partitionEnd to max(partitionEnd, lastOccurrence[char])
     *     b. This ensures partition extends to include all occurrences of seen characters
     *     c. When current index reaches partitionEnd: all characters in partition won't appear later
     *     d. Record partition size and start new partition
     *  4. Return list of partition sizes
     *
     * Key Insight:
     * A partition can end at position i if and only if all characters from partition start to i
     * have their last occurrences at or before i. We track the farthest last occurrence seen so far,
     * and when we reach that position, we know no character will appear later - safe to partition.
     *
     * Algorithm: Greedy with Last Occurrence Map.
     * Time Complexity: O(n), where n is string length. Two passes: one to build map, one to partition.
     * Space Complexity: O(1), fixed-size array of 26 characters (or O(k) for k unique characters).
     */
    public List<Integer> partitionLabels(String input) {
        int length = input.length();
        
        // Record last occurrence index of each character
        int[] lastOccurrence = new int[26];
        for (int i = 0; i < length; i++) {
            lastOccurrence[input.charAt(i) - 'a'] = i;
        }
        
        List<Integer> result = new ArrayList<>();
        int partitionStart = 0;
        int partitionEnd = 0;
        
        // Iterate through string to determine partitions
        for (int i = 0; i < length; i++) {
            char currentChar = input.charAt(i);
            
            // Extend partition to include all occurrences of current character
            partitionEnd = Math.max(partitionEnd, lastOccurrence[currentChar - 'a']);
            
            // If reached the end of current partition
            if (i == partitionEnd) {
                // Add partition size to result
                int partitionSize = partitionEnd - partitionStart + 1;
                result.add(partitionSize);
                System.out.println("Partition: " + input.substring(partitionStart, partitionEnd + 1));
                
                // Start new partition from next index
                partitionStart = i + 1;
            }
        }
        
        return result;
    }
}
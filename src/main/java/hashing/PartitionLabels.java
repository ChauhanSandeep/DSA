package hashing;

import java.util.ArrayList;
import java.util.List;


/**
 * Problem: Partition Labels
 *
 * Split a string into as many parts as possible so that each character appears
 * in at most one part. Return the size of each partition in left-to-right order.
 *
 * Leetcode: https://leetcode.com/problems/partition-labels/ (Medium)
 * Rating:   1443
 * Pattern:  Hashing | Greedy | Last occurrence boundary
 *
 * Example:
 *   Input:  s = "ababcbacadefegdehijhklij"
 *   Output: [9,7,8]
 *   Why:    the first cut must wait until the last a, b, and c are included;
 *           the same rule then forms the next two maximal cuts.
 *
 * Follow-ups:
 *   1. How would you return the substrings instead of their lengths?
 *      Store each start and end boundary and slice the original string at the end.
 *   2. How would you support arbitrary Unicode characters?
 *      Replace the fixed 26-slot array with a map from character to last index.
 *   3. What if you wanted at most k partitions?
 *      Build the greedy partitions first, then merge adjacent partitions as needed.
 *   4. Can this be viewed as an interval problem?
 *      Yes, each character defines an interval from first to last occurrence, and overlapping intervals merge.
 *
 * Related: Merge Intervals (56), Non-overlapping Intervals (435).
 */
public class PartitionLabels {

    public static void main(String[] args) {
        PartitionLabels solver = new PartitionLabels();
        String[] inputs = { "ababcbacadefegdehijhklij", "eccbbbbdec" };
        String[] expected = { "[9, 7, 8]", "[10]" };

        for (int i = 0; i < inputs.length; i++) {
            List<Integer> got = solver.partitionLabels(inputs[i]);
            System.out.printf("s=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }

       /**
     * Intuition: once a partition contains a character, it must extend at least
     * to that character's last occurrence. Track the farthest last occurrence of
     * all characters seen so far; when the scan reaches it, the partition can end.
     *
     * Algorithm:
     *   1. Record the last index of each lowercase character.
     *   2. Scan the string while expanding the current partition end to each character's last index.
     *   3. When the scan reaches the partition end, record its size and start the next partition.
     *
     * Time:  O(n) - one pass records last positions and one pass builds partitions.
     * Space: O(1) - the last-occurrence array has 26 slots.
     *
     * @param input lowercase string to partition
     * @return sizes of the maximum number of valid partitions
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
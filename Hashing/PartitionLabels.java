package Hashing;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Partition Labels
 * https://leetcode.com/problems/partition-labels/
 *
 * Given a string s, partition it into as many parts as possible so that each letter appears in at most one part.
 * Return a list of integers representing the size of these parts.
 *
 * Approach:
 * 1. Compute the last occurrence index for each character in the string.
 * 2. Iterate through the string, keeping track of the farthest index needed for a valid partition.
 * 3. When the current index reaches the farthest required index, create a partition.
 *
 * Time Complexity: O(N) - Single pass to compute last occurrences + Single pass to form partitions.
 * Space Complexity: O(1) - Uses an array of fixed size (26).
 */
public class PartitionLabels {
    
    public static void main(String[] args) {
        PartitionLabels partitionLabels = new PartitionLabels();
        List<Integer> result = partitionLabels.getPartitionSizes("ababcbacadefegdehijhklij");
        System.out.println(result);
    }

    /**
     * Finds the partition sizes based on character occurrence constraints.
     *
     * @param str The input string.
     * @return A list containing sizes of each partition.
     */
    public List<Integer> getPartitionSizes(String str) {
        int[] lastOccurrence = new int[26]; // Stores last index of each character in 'str'

        // Step 1: Compute the last occurrence index for each character
        for (int i = 0; i < str.length(); i++) {
            lastOccurrence[str.charAt(i) - 'a'] = i;
        }

        List<Integer> partitionSizes = new ArrayList<>();
        int partitionStart = 0;
        int maxPartitionEnd = 0;

        // Step 2: Iterate through the string to form partitions
        for (int i = 0; i < str.length(); i++) {
            maxPartitionEnd = Math.max(maxPartitionEnd, lastOccurrence[str.charAt(i) - 'a']);

            // When we reach the farthest index of the current partition, finalize it
            if (i == maxPartitionEnd) {
                partitionSizes.add(i - partitionStart + 1); // Store partition size
                partitionStart = i + 1; // Start a new partition
            }
        }

        return partitionSizes;
    }
}

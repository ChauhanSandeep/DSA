package Hashing;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.com/problems/partition-labels/
 *
 * Given a string s, partition the string into as many parts as possible so that each letter appears in at most one part.
 * Return a list of integers representing the size of these parts.
 */
public class PartitionLabels {
    public static void main(String[] args) {
        List<Integer> result = new PartitionLabels().partitionLabels("ababcbacadefegdehijhklij");
        System.out.println(result);
    }

    public List<Integer> partitionLabels(String str) {
        int[] lastOccurrence = new int[26]; // Stores last index of each character

        // Step 1: Compute last occurrence of each character
        for (int i = 0; i < str.length(); i++) {
            lastOccurrence[str.charAt(i) - 'a'] = i;
        }

        List<Integer> result = new ArrayList<>();
        int partitionStart = 0;
        int maxPartitionEnd = 0;

        // Step 2: Iterate through the string and determine partitions
        for (int i = 0; i < str.length(); i++) {
            maxPartitionEnd = Math.max(maxPartitionEnd, lastOccurrence[str.charAt(i) - 'a']);

            // If current index matches the furthest occurrence of any character seen so far
            if (i == maxPartitionEnd) {
                result.add(i - partitionStart + 1);
                partitionStart = i + 1;
            }
        }

        return result;
    }
}
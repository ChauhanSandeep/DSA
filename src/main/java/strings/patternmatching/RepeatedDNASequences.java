package strings.patternmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Problem: Repeated DNA Sequences
 *
 * Given a DNA string made of A, C, G, and T, return every 10-letter substring
 * that appears more than once. The answer may be returned in any order.
 *
 * Leetcode: https://leetcode.com/problems/repeated-dna-sequences/ (Medium)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Strings | Fixed-size sliding window | Hashing
 *
 * Example:
 *   Input:  s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"
 *   Output: ["AAAAACCCCC","CCCCCAAAAA"]
 *   Why:    each listed 10-letter DNA sequence occurs at least twice.
 *
 * Follow-ups:
 *   1. What if the repeated length is k instead of 10?
 *      Make the window size configurable and update the rolling hash mask.
 *   2. How do you reduce memory for very large DNA strings?
 *      Encode each nucleotide in two bits and store integer hashes.
 *   3. How would you process streaming DNA data?
 *      Maintain only the last k characters and update counts as the stream advances.
 *
 * Related: Find All Anagrams in a String (438), Substring with Concatenation of All Words (30).
 */
public class RepeatedDNASequences {

    public static void main(String[] args) {
        RepeatedDNASequences solver = new RepeatedDNASequences();

        String[] inputs = {"AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT", "AAAAAAAAAAAAA", "ACGT"};
        String[] expected = {"[AAAAACCCCC, CCCCCAAAAA]", "[AAAAAAAAAA]", "[]"};

        for (int i = 0; i < inputs.length; i++) {
            List<String> got = solver.findRepeatedDnaSequences(inputs[i]);
            java.util.Collections.sort(got);
            System.out.printf("s=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }
        /**
     * Intuition: every answer is a fixed 10-character window. Count each window
     * string, then report the windows whose count is greater than one.
     *
     * Algorithm:
     *   1. Return an empty list when fewer than 10 characters are available.
     *   2. Slide a length-10 window and count each sequence in a map.
     *   3. Add every sequence whose final count is greater than one.
     *
     * Time:  O(n) - there are n - 9 windows and each has fixed length 10.
     * Space: O(n) - distinct 10-letter sequences are stored in the map.
     *
     * @param s DNA sequence string.
     * @return Repeated 10-letter DNA sequences.
     */
    public List<String> findRepeatedDnaSequences(String s) {
        List<String> result = new ArrayList<>();
        if (s == null || s.length() < 10) {
            return result;
        }

        // Use a map to store the count of each 10-letter sequence
        Map<String, Integer> sequenceCount = new HashMap<>();

        // Slide a window of size 10 through the string
        for (int i = 0; i <= s.length() - 10; i++) {
            String sequence = s.substring(i, i + 10);
            sequenceCount.put(sequence, sequenceCount.getOrDefault(sequence, 0) + 1);
        }

        // Add sequences that appear more than once to the result
        for (Map.Entry<String, Integer> entry : sequenceCount.entrySet()) {
            if (entry.getValue() > 1) {
                result.add(entry.getKey());
            }
        }

        return result;
    }

    /**
     * Optimized solution using bit manipulation and rolling hash.
     * This approach is more memory efficient as it uses integers to represent sequences.
     */
    public List<String> findRepeatedDnaSequencesBitManipulation(String s) {
        List<String> result = new ArrayList<>();
        if (s == null || s.length() < 10) {
            return result;
        }

        // Map to store the count of each 20-bit number (10 letters * 2 bits each)
        Map<Integer, Integer> sequenceCount = new HashMap<>();

        // Map to convert characters to 2-bit representation
        Map<Character, Integer> toInt = new HashMap<>();
        toInt.put('A', 0); // 00
        toInt.put('C', 1); // 01
        toInt.put('G', 2); // 10
        toInt.put('T', 3); // 11

        int bitmask = 0;
        int n = s.length();

        // Process the first 10 characters
        for (int i = 0; i < 10; i++) {
            bitmask <<= 2; // Make room for the next character
            bitmask |= toInt.get(s.charAt(i)); // Add the current character
        }
        sequenceCount.put(bitmask, 1);

        // Slide the window one character at a time
        for (int i = 10; i < n; i++) {
            // Remove the leftmost character (20 bits - 2*10 = 20 bits total)
            bitmask &= ~(3 << 18); // Clear the leftmost 2 bits

            // Add the new character
            bitmask <<= 2;
            bitmask |= toInt.get(s.charAt(i));

            // Update the count for this sequence
            sequenceCount.put(bitmask, sequenceCount.getOrDefault(bitmask, 0) + 1);

            // If this is the second time we've seen this sequence, add it to the result
            if (sequenceCount.get(bitmask) == 2) {
                result.add(s.substring(i - 9, i + 1));
            }
        }

        return result;
    }

    /**
     * Alternative solution using a set to track seen sequences.
     * This approach is simpler but uses more memory for large inputs.
     */
    public List<String> findRepeatedDnaSequencesSet(String s) {
        Set<String> seen = new HashSet<>();
        Set<String> repeated = new HashSet<>();

        for (int i = 0; i <= s.length() - 10; i++) {
            String sequence = s.substring(i, i + 10);
            if (!seen.add(sequence)) {
                repeated.add(sequence);
            }
        }

        return new ArrayList<>(repeated);
    }

    /**
     * Optimized solution using a custom rolling hash function.
     * This approach is memory efficient and handles very large inputs well.
     */
    public List<String> findRepeatedDnaSequencesRollingHash(String s) {
        List<String> result = new ArrayList<>();
        if (s == null || s.length() < 10) {
            return result;
        }

        // Map to store the count of each hash value
        Map<Long, Integer> hashCount = new HashMap<>();

        // Precompute powers of 4 for the rolling hash
        long[] power4 = new long[11];
        power4[0] = 1;
        for (int i = 1; i <= 10; i++) {
            power4[i] = power4[i - 1] * 4;
        }

        // Map characters to values (A=0, C=1, G=2, T=3)
        Map<Character, Integer> toInt = new HashMap<>();
        toInt.put('A', 0);
        toInt.put('C', 1);
        toInt.put('G', 2);
        toInt.put('T', 3);

        // Compute the initial hash for the first 10 characters
        long hash = 0;
        for (int i = 0; i < 10; i++) {
            hash = hash * 4 + toInt.get(s.charAt(i));
        }
        hashCount.put(hash, 1);

        // Slide the window and update the hash
        for (int i = 10; i < s.length(); i++) {
            // Remove the leftmost character
            hash -= toInt.get(s.charAt(i - 10)) * power4[9];

            // Add the new character
            hash = hash * 4 + toInt.get(s.charAt(i));

            // Update the count for this hash
            int count = hashCount.getOrDefault(hash, 0) + 1;
            hashCount.put(hash, count);

            // If this is the second time we've seen this hash, add the sequence to the result
            if (count == 2) {
                result.add(s.substring(i - 9, i + 1));
            }
        }

        return result;
    }
}

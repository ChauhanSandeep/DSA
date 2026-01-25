package strings.patternmatching;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The DNA sequence is composed of a series of nucleotides abbreviated as 'A', 'C', 'G', and 'T'.
 * For example, "ACGAATTCCG" is a DNA sequence.
 *
 * Given a string s that represents a DNA sequence, return all the 10-letter-long sequences (substrings)
 * that occur more than once in the DNA molecule. You may return the answer in any order.
 *
 * Example 1:
 * Input: s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"
 * Output: ["AAAAACCCCC","CCCCCAAAAA"]
 *
 * Example 2:
 * Input: s = "AAAAAAAAAAAAA"
 * Output: ["AAAAAAAAAA"]
 *
 * LeetCode: https://leetcode.com/problems/repeated-dna-sequences/
 *
 * Follow-up Questions:
 * 1. How would you handle very large input strings (e.g., 1,000,000 characters)?
 *    - The rolling hash solution is efficient with O(n) time and O(n) space.
 * 2. What if we need to find sequences of length k instead of 10?
 *    - We can make the sequence length a parameter and adjust the solution accordingly.
 * 3. How would you optimize for memory usage if the input is extremely large?
 *    - We could use a sliding window with a rolling hash to process the string in chunks.
 *
 * Related Problems:
 * - Substring with Concatenation of All Words (https://leetcode.com/problems/substring-with-concatenation-of-all-words/)
 * - Find All Anagrams in a String (https://leetcode.com/problems/find-all-anagrams-in-a-string/)
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class RepeatedDNASequences {
    /**
     * Finds all 10-letter-long sequences that occur more than once in the DNA molecule.
     *
     * @param s DNA sequence string
     * @return List of repeated 10-letter sequences
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

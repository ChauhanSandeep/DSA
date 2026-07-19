package strings.hashmap;

import java.util.*;


/**
 * Problem: Group Anagrams
 *
 * Given an array of strings, group together words that are anagrams of each
 * other. The groups may be returned in any order, but each group contains words
 * with the same multiset of characters.
 *
 * Leetcode: https://leetcode.com/problems/group-anagrams/ (Medium)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Hash map | Canonical key | Sorting characters
 *
 * Example:
 *   Input:  strs = ["eat","tea","tan","ate","nat","bat"]
 *   Output: [["ate","eat","tea"],["bat"],["nat","tan"]]
 *   Why:    each group shares the same sorted-character key.
 *
 * Follow-ups:
 *   1. Optimize for very long strings?
 *      Use frequency-count keys to avoid sorting every word.
 *   2. Support Unicode characters?
 *      Build keys from a Map<Character, Integer> rather than a 26-entry array.
 *   3. Process a huge stream of words?
 *      Emit groups by partitioning or external sorting on the canonical key.
 *
 * Related: Valid Anagram (242), Find All Anagrams in a String (438), Group Shifted Strings (249).
 */
public class GroupAnagrams {

    public static void main(String[] args) {
        GroupAnagrams solver = new GroupAnagrams();
        String[][] inputs = { {"eat", "tea", "tan", "ate", "nat", "bat"}, {""} };
        String[][][] expected = {
            {{"ate", "eat", "tea"}, {"bat"}, {"nat", "tan"}},
            {{""}}
        };

        for (int i = 0; i < inputs.length; i++) {
            java.util.List<java.util.List<String>> got = solver.groupAnagrams(inputs[i]);
            for (java.util.List<String> group : got) {
                java.util.Collections.sort(group);
            }
            got.sort(java.util.Comparator.comparing(group -> group.get(0)));
            System.out.printf("strs=%s -> %s  expected=%s%n",
                java.util.Arrays.toString(inputs[i]), got, java.util.Arrays.deepToString(expected[i]));
        }
    }


    /**
     * Intuition: anagrams become identical after their characters are sorted. That
     * sorted form is a canonical key: every word with the same key belongs in the
     * same bucket, and words with different keys cannot be anagrams.
     *
     * Algorithm:
     *   1. Return an empty list for null or empty input.
     *   2. For each string, sort its characters to build the key.
     *   3. Add the original string to the list stored for that key.
     *   4. Return all map values as the grouped anagrams.
     *
     * Time:  O(n * k log k) - n words, each sorting up to k characters.
     * Space: O(n * k) - groups store all input strings and keys.
     *
     * @param strs strings to group by anagram equivalence
     * @return grouped anagrams
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        if (strs == null || strs.length == 0) {
            return new ArrayList<>();
        }

        Map<String, List<String>> anagramGroups = new HashMap<>();

        for (String currentString : strs) {
            // Create canonical form by sorting characters
            String sortedKey = getSortedKey(currentString);

            // Add to existing group or create new group
            anagramGroups.computeIfAbsent(sortedKey, k -> new ArrayList<>())
                .add(currentString);
        }

        return new ArrayList<>(anagramGroups.values());
    }

    /** Returns the sorted-character canonical key for an anagram group. */
    private String getSortedKey(String str) {
        char[] charArray = str.toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }

    /**
     * Alternative approach using frequency counting as key.
     * Better for very long strings as it avoids sorting overhead.
     *
     * Algorithm:
     * 1. For each string, count frequency of each character
     * 2. Create a frequency-based key (e.g., "a2b1c1" for "abc")
     * 3. Use frequency key to group anagrams
     * 4. Return all groups as list of lists
     *
     * Time Complexity: O(n * k) where n is number of strings, k is max string length
     * Space Complexity: O(n * k) for storing all strings in the result
     *
     * @param strs array of strings to group
     * @return list of grouped anagrams
     */
    public List<List<String>> groupAnagramsFrequency(String[] strs) {
        if (strs == null || strs.length == 0) {
            return new ArrayList<>();
        }

        Map<String, List<String>> anagramGroups = new HashMap<>(); // Key: frequency key, Value: list of anagrams

        for (String currentString : strs) {
            // Create frequency-based key
            String frequencyKey = getFrequencyKey(currentString);

            // Add to existing group or create new group
            anagramGroups.computeIfAbsent(frequencyKey, k -> new ArrayList<>())
                .add(currentString);
        }

        return new ArrayList<>(anagramGroups.values());
    }

    /** Returns the frequency-count canonical key for lowercase anagrams. */
    private String getFrequencyKey(String str) {
        int[] charCount = new int[26]; // For lowercase English letters

        // Count frequency of each character
        for (char c : str.toCharArray()) {
            charCount[c - 'a']++;
        }

        // Build frequency key using delimiter to avoid collisions
        StringBuilder keyBuilder = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            if (charCount[i] > 0) {
                keyBuilder.append((char) (i + 'a'))
                    .append(charCount[i])
                    .append('#');
            }
        }

        return keyBuilder.toString();
    }
}

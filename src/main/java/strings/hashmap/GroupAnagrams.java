package strings.hashmap;

import java.util.*;


/**
 * Problem: Group Anagrams
 *
 * Given an array of strings `strs`, group the anagrams together. You can return the answer in any order.
 * An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase,
 * typically using all the original letters exactly once.
 *
 * Example:
 * Input: strs = ["eat","tea","tan","ate","nat","bat"]
 * Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
 *
 * Explanation:
 * - "eat", "tea", "ate" are anagrams (all contain letters 'a', 'e', 't')
 * - "tan", "nat" are anagrams (both contain letters 'a', 'n', 't')
 * - "bat" has no anagrams in the list
 *
 * LeetCode: https://leetcode.com/problems/group-anagrams/
 *
 * Follow-up Questions:
 * 1. Q: What if the strings contain Unicode characters?
 *    A: The current sorting approach works for Unicode. Frequency counting would need
 *       to use a Map instead of fixed-size array.
 * 2. Q: How would you optimize for very long strings?
 *    A: Use frequency counting instead of sorting to avoid O(k log k) per string.
 * 3. Q: What about case sensitivity?
 *    A: Convert to lowercase before processing, or modify comparison logic.
 * 4. Q: Memory constraints for large datasets?
 *    A: Consider streaming approach or external sorting for very large inputs.
 *
 * Related Problems:
 * - Valid Anagram: https://leetcode.com/problems/valid-anagram/
 * - Find All Anagrams in a String: https://leetcode.com/problems/find-all-anagrams-in-a-string/
 */
public class GroupAnagrams {

    /**
     * Groups anagrams together using sorted string as key.
     *
     * Algorithm:
     * 1. For each string, sort its characters to create a canonical form
     * 2. Use the sorted string as key in HashMap
     * 3. Group all strings that have the same sorted form
     * 4. Return all groups as list of lists
     *
     * Time Complexity: O(n * k * log k) where n is number of strings, k is max string length
     * Space Complexity: O(n * k) for storing all strings in the result
     *
     * @param strs array of strings to group
     * @return list of grouped anagrams
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

    // Helper method to create sorted key from string
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

    // Helper method to create frequency-based key
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

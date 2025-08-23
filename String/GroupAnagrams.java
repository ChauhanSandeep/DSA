package String;

import java.util.*;

/**
 * LeetCode 49. Group Anagrams
 *
 * Given an array of strings strs, group the anagrams together. You can return the answer in any order.
 * An Anagram is a word or phrase formed by rearranging the letters of a different word or phrase.
 *
 * Example 1:
 * Input: strs = ["eat","tea","tan","ate","nat","bat"]
 * Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
 *
 * LeetCode Link: https://leetcode.com/problems/group-anagrams/
 */
public class GroupAnagrams {

    /**
     * Groups anagrams using sorted string as key for identification.
     *
     * Algorithm:
     * 1. For each string, sort its characters to create a canonical form
     * 2. Use sorted string as key in HashMap
     * 3. Group all strings with same canonical form together
     * 4. Return all groups as list of lists
     *
     * Time Complexity: O(n * m * log m) where n = number of strings, m = average string length
     * Space Complexity: O(n * m) for storing all strings in the result
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> anagramGroups = new HashMap<>();

        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String sortedStr = new String(chars);

            anagramGroups.computeIfAbsent(sortedStr, k -> new ArrayList<>()).add(str);
        }

        return new ArrayList<>(anagramGroups.values());
    }

    /**
     * Alternative approach using character frequency as key.
     */
    public List<List<String>> groupAnagramsFrequency(String[] strs) {
        Map<String, List<String>> anagramGroups = new HashMap<>();

        for (String str : strs) {
            String frequencyKey = getFrequencyKey(str);
            anagramGroups.computeIfAbsent(frequencyKey, k -> new ArrayList<>()).add(str);
        }

        return new ArrayList<>(anagramGroups.values());
    }

    // Helper method to create frequency-based key
    private String getFrequencyKey(String str) {
        int[] frequency = new int[26];

        for (char c : str.toCharArray()) {
            frequency[c - 'a']++;
        }

        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            if (frequency[i] > 0) {
                key.append((char) ('a' + i)).append(frequency[i]);
            }
        }

        return key.toString();
    }
}

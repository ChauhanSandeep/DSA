package strings.patternmatching;

import java.util.*;

/**
 * Problem: Substring with Concatenation of All Words
 *
 * You are given a string s and an array of strings words of the same length.
 * Return all starting indices of substring(s) in s that is a concatenation of
 * each word in words exactly once, in any order, and without any intervening characters.
 *
 * Example:
 * Input: s = "barfoothefoobarman", words = ["foo","bar"]
 * Output: [0,9]
 * Explanation: Substrings starting at index 0 and 9 are "barfoo" and "foobar" respectively.
 *
 * LeetCode: https://leetcode.com/problems/substring-with-concatenation-of-all-words
 *
 * Time Complexity: O(n * m * k) where n is the length of s, m is the number of words, and k is the length of each word
 * Space Complexity: O(m) for the frequency map
 */
public class SubstringWithConcatenationOfAllWords {
    public List<Integer> findSubstring(String s, String[] words) {
        List<Integer> result = new ArrayList<>();
        if (s == null || s.length() == 0 || words == null || words.length == 0) {
            return result;
        }

        int wordLength = words[0].length();
        int totalWords = words.length;
        int totalLength = wordLength * totalWords;

        if (s.length() < totalLength) {
            return result;
        }

        // Create a frequency map of words
        Map<String, Integer> wordFreq = new HashMap<>();
        for (String word : words) {
            wordFreq.put(word, wordFreq.getOrDefault(word, 0) + 1);
        }

        // We only need to check starting positions from 0 to wordLength - 1
        for (int i = 0; i < wordLength; i++) {
            int left = i; // Left boundary of the sliding window
            int count = 0; // Count of words in the current window
            Map<String, Integer> currentFreq = new HashMap<>();

            // j is the right boundary of the sliding window
            for (int j = i; j <= s.length() - wordLength; j += wordLength) {
                String word = s.substring(j, j + wordLength);

                if (wordFreq.containsKey(word)) {
                    currentFreq.put(word, currentFreq.getOrDefault(word, 0) + 1);
                    count++;

                    // If we have more occurrences of a word than needed
                    while (currentFreq.get(word) > wordFreq.get(word)) {
                        String leftWord = s.substring(left, left + wordLength);
                        currentFreq.put(leftWord, currentFreq.get(leftWord) - 1);
                        count--;
                        left += wordLength;
                    }

                    // If we found all words
                    if (count == totalWords) {
                        result.add(left);
                        // Move the left boundary to find other possible substrings
                        String leftWord = s.substring(left, left + wordLength);
                        currentFreq.put(leftWord, currentFreq.get(leftWord) - 1);
                        count--;
                        left += wordLength;
                    }
                } else {
                    // Reset the window if we encounter a word not in the list
                    currentFreq.clear();
                    count = 0;
                    left = j + wordLength;
                }
            }
        }

        return result;
    }
}

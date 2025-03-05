package DynamicProgramming;

import java.util.*;

/**
 * Problem: Concatenated Words
 * 
 * Given an array of strings `words`, return all the words that are formed 
 * by concatenating two or more words from the given list.
 * 
 * Approach:
 * - Sort words based on length (shortest first) so that we build from smaller words.
 * - Use a `Set<String>` to store already processed words for quick lookup.
 * - For each word, check if it can be formed using the previously added words.
 * - Use **Dynamic Programming (DP)** to check if a word can be formed from words in the set.
 * 
 * Time Complexity:
 * - Sorting: **O(n log n)**
 * - Checking each word: **O(n * m²)** (where `m` is the average word length)
 * - Overall: **O(n log n + n * m²)**
 * 
 * Space Complexity:
 * - **O(n * m)** (for storing words in the set and DP array)
 * 
 * LeetCode Problem Link:
 * https://leetcode.com/problems/concatenated-words/
 */
public class ConcatenatedWords {
    public static void main(String[] args) {
        String[] words = {"cat", "cats", "catsdogcats", "dog", "dogcatsdog", "hippopotamuses", "rat", "ratcatdogcat"};
        ConcatenatedWords solver = new ConcatenatedWords();
        List<String> result = solver.findAllConcatenatedWords(words);
        System.out.println("Concatenated Words: " + result);
    }

    /**
     * Finds all concatenated words in the dictionary.
     * 
     * @param words Array of input words.
     * @return List of concatenated words.
     */
    public List<String> findAllConcatenatedWords(String[] words) {
        List<String> concatenatedWords = new ArrayList<>();
        Set<String> dictionary = new HashSet<>();

        // Sort words by length to ensure we build from smaller words
        Arrays.sort(words, Comparator.comparingInt(String::length));

        for (String word : words) {
            if (!word.isEmpty() && canBeFormed(word, dictionary)) {
                concatenatedWords.add(word);
            }
            dictionary.add(word);
        }

        return concatenatedWords;
    }

    /**
     * Checks if a given word can be formed by concatenating words in the set.
     * 
     * @param word The word to check.
     * @param dictionary Set of words to check against.
     * @return true if the word can be formed, false otherwise.
     */
    private boolean canBeFormed(String word, Set<String> dictionary) {
        if (dictionary.isEmpty()) return false;

        int n = word.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true; // Base case: empty string can be formed

        // Check for all substrings if they exist in the dictionary
        for (int right = 1; right <= n; right++) {
            for (int left = 0; left < right; left++) {
                if (!dp[left]) continue;
                if (dictionary.contains(word.substring(left, right))) {
                    dp[right] = true;
                    break; // No need to check further once found
                }
            }
        }
        return dp[n];
    }
}

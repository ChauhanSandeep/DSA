package Hashing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://leetcode.com/problems/palindrome-pairs/
 */
public class PalindromePairs {


    /**
     * There are three scenarios in which palindrom pair can be found (word1 + word2)
     * word1 is reverse of word2
     * word2 = palindrome substring + reverse of word1
     * word1 = reverse of word2 + palindrome substring
     * @param words
     * @return
     */
    public List<List<Integer>> palindromePairs(String[] words) {
        // Build a word -> original index mapping for efficient lookup.
        Map<String, Integer> wordMap = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            wordMap.put(words[i], i);
        }

        // Make a list to put all the palindrome pairs we find in.
        List<List<Integer>> solution = new ArrayList<>();

        for (String word : wordMap.keySet()) {

            int currentWordIndex = wordMap.get(word);
            String reversedWord = new StringBuilder(word).reverse().toString();

            // Build solutions of case #1. This word will be word 1.
            if (wordMap.containsKey(reversedWord) && wordMap.get(reversedWord) != currentWordIndex) {
                solution.add(Arrays.asList(currentWordIndex, wordMap.get(reversedWord)));
            }

            // Build solutions of case #2. This word will be word 2.
            for (String suffix : allValidSuffixes(word)) {
                String reversedSuffix = new StringBuilder(suffix).reverse().toString();
                if (wordMap.containsKey(reversedSuffix)) {
                    solution.add(Arrays.asList(wordMap.get(reversedSuffix), currentWordIndex));
                }
            }

            // Build solutions of case #3. This word will be word 1.
            for (String prefix : allValidPrefixes(word)) {
                String reversedPrefix = new StringBuilder(prefix).reverse().toString();
                if (wordMap.containsKey(reversedPrefix)) {
                    solution.add(Arrays.asList(currentWordIndex, wordMap.get(reversedPrefix)));
                }
            }
        }
        return solution;
    }

    /**
     * Return list of prefix such that remaining suffix is palindrome
     */
    private List<String> allValidPrefixes(String word) {
        List<String> validPrefixes = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            if (isPalindromeBetween(word, i, word.length() - 1)) {
                validPrefixes.add(word.substring(0, i));
            }
        }
        return validPrefixes;
    }

    /**
     * Return list of suffix such that remaining prefix is palindrome
     */
    private List<String> allValidSuffixes(String word) {
        List<String> validSuffixes = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            if (isPalindromeBetween(word, 0, i)) {
                validSuffixes.add(word.substring(i + 1, word.length()));
            }
        }
        return validSuffixes;
    }

    // Is the prefix ending at i a palindrome?
    private boolean isPalindromeBetween(String word, int front, int back) {
        while (front < back) {
            if (word.charAt(front) != word.charAt(back)) return false;
            front++;
            back--;
        }
        return true;
    }
}

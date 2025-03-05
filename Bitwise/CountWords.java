package Bitwise;

import java.util.*;

/**
 * This class contains a method to count the number of target words that can be obtained by adding
 * exactly one letter to a start word and rearranging the letters.
 * 
 * Algorithm:
 * - Use two maps to store sorted versions of start and target words grouped by length.
 * - Check for each target word if removing one character results in a start word.
 * - Time Complexity: O(n * m * log m) where n is the number of words and m is the average word length.
 * - Space Complexity: O(n * m)
 * 
 * LeetCode Problem Link: https://leetcode.com/problems/count-words-obtained-after-adding-a-letter/
 */
public class CountWords {

    private Map<String, Boolean> validationCache;

    public static void main(String[] args) {
        String[] startWords = {"ant", "act", "tack"};
        String[] targetWords = {"tack", "act", "acti"};
        int result = new CountWords().wordCount(startWords, targetWords);
        System.out.println(result);
    }

    public int wordCount(String[] startWords, String[] targetWords) {
        Map<Integer, Set<String>> sortedStartWordsMap = new HashMap<>();
        Map<Integer, List<String>> sortedTargetWordsMap = new HashMap<>();
        validationCache = new HashMap<>();

        // Process and store start words
        for (String word : startWords) {
            char[] characters = word.toCharArray();
            Arrays.sort(characters);
            String sortedWord = new String(characters);
            sortedStartWordsMap
                .computeIfAbsent(sortedWord.length(), k -> new HashSet<>())
                .add(sortedWord);
        }

        // Process and store target words
        for (String word : targetWords) {
            char[] characters = word.toCharArray();
            Arrays.sort(characters);
            String sortedWord = new String(characters);
            sortedTargetWordsMap
                .computeIfAbsent(sortedWord.length(), k -> new ArrayList<>())
                .add(sortedWord);
        }

        // Count valid target words
        int validWordCount = 0;
        for (Map.Entry<Integer, List<String>> entry : sortedTargetWordsMap.entrySet()) {
            int targetWordLength = entry.getKey();
            List<String> targetWordList = entry.getValue();
            Set<String> correspondingStartWords = sortedStartWordsMap.get(targetWordLength - 1);

            if (correspondingStartWords != null && !correspondingStartWords.isEmpty()) {
                for (String targetWord : targetWordList) {
                    if (isTransformable(targetWord, correspondingStartWords)) {
                        validWordCount++;
                    }
                }
            }
        }
        return validWordCount;
    }

    private boolean isTransformable(String targetWord, Set<String> startWords) {
        for (int i = 0; i < targetWord.length(); i++) {
            String potentialStartWord = targetWord.substring(0, i) + targetWord.substring(i + 1);
            if (startWords.contains(potentialStartWord)) {
                return true;
            }
        }
        return false;
    }
}

package strings.patternmatching;

import java.util.*;

/**
 * Problem: Number of Matching Subsequences
 *
 * Given a target string and a list of words, count how many words are
 * subsequences of the target. A subsequence keeps character order but may skip
 * target characters.
 *
 * Leetcode: https://leetcode.com/problems/number-of-matching-subsequences/ (Medium)
 * Rating:   contest Elo 1695
 * Pattern:  Strings | Buckets by next needed character | Queues
 *
 * Example:
 *   Input:  target = "abcde", words = ["a","bb","acd","ace"]
 *   Output: 3
 *   Why:    "a", "acd", and "ace" can advance through target in order; "bb" cannot.
 *
 * Follow-ups:
 *   1. How would you handle millions of duplicate words?
 *      Count unique words first, then multiply each successful result by frequency.
 *   2. What if target is fixed for many online queries?
 *      Preprocess target positions and binary-search the next occurrence for each character.
 *   3. How can this be parallelized?
 *      Partition words across workers and sum their local match counts.
 */
public class NumberOfMatchingSubsequences {

    public static void main(String[] args) {
        NumberOfMatchingSubsequences solver = new NumberOfMatchingSubsequences();

        String[] targets = {"abcde", "dsahjpjauf"};
        String[][] words = {
            {"a", "bb", "acd", "ace"},
            {"ahjpjau", "ja", "ahbwzgqnuk", "tnmlanowax"}
        };
        int[] expected = {3, 2};

        for (int i = 0; i < targets.length; i++) {
            int got = solver.numMatchingSubseqHashMap(targets[i], words[i]);
            System.out.printf("target=%s words=%s -> %d  expected=%d%n",
                targets[i], Arrays.toString(words[i]), got, expected[i]);
        }
    }

        /**
     * Intuition: each word is waiting for exactly one next character. Bucket words
     * by that needed character; when targetString provides it, advance those words
     * to their next needed character or count them as complete.
     *
     * Algorithm:
     *   1. Put every word tracker into the bucket for its first character.
     *   2. Scan targetString and process only the bucket matching the current character.
     *   3. Advance each tracker, counting finished words and rebucketing unfinished ones.
     *
     * Time:  O(T + W) - target characters and total advanced word characters are processed once.
     * Space: O(W) - one tracker is stored for each word in the bucket map.
     *
     * @param targetString String that candidate words must be subsequences of.
     * @param words Candidate words to count.
     * @return Number of words that are subsequences of targetString.
     */
    public int numMatchingSubseqHashMap(String targetString, String[] words) {
        Map<Character, Deque<WordIndexTracker>> subsequenceBuckets = new HashMap<>();
        
        // Initialize buckets for all words
        for (String word : words) {
            char firstChar = word.charAt(0);
            subsequenceBuckets.computeIfAbsent(firstChar, k -> new ArrayDeque<>()).offer(new WordIndexTracker(word, 0));
        }
        
        int matchCount = 0;
        
        // Process each character in targetString
        for (char currentChar : targetString.toCharArray()) {
            if (!subsequenceBuckets.containsKey(currentChar)) continue;
            
            // Process all words waiting for currentChar
            Deque<WordIndexTracker> currentBucket = subsequenceBuckets.get(currentChar);
            int bucketSize = currentBucket.size();
            
            for (int i = 0; i < bucketSize; i++) {
                WordIndexTracker iterator = currentBucket.poll();
                // Move to next character in the word
                iterator.index++;
                
                if (iterator.index == iterator.word.length()) {
                    matchCount++;
                } else {
                    char nextChar = iterator.word.charAt(iterator.index);
                    subsequenceBuckets.putIfAbsent(nextChar, new ArrayDeque<>());
                    subsequenceBuckets.get(nextChar).offer(iterator);
                }
            }
        }
        
        return matchCount;
    }

    // Helper class to track current position in a word
    private static class WordIndexTracker {
        String word;
        int index;

        WordIndexTracker(String word, int index) {
            this.word = word;
            this.index = index;
        }
    }
}
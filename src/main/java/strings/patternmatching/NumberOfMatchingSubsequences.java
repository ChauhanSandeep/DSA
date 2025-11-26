package strings.patternmatching;

import java.util.*;

/**
 * LeetCode 792. Number of Matching Subsequences
 *
 * Given a string s and an array of strings words, return the number of words[i]
 * that is a subsequence of s. A subsequence of a string is a new string generated
 * from the original string with some characters deleted without changing the relative order.
 *
 * Example 1:
 * Input: s = "abcde", words = ["a","bb","acd","ace"]
 * Output: 3
 * Explanation: There are three strings in words that are a subsequence of s: "a", "acd", "ace".
 *
 * LeetCode Link: https://leetcode.com/problems/number-of-matching-subsequences/
 *
 * Follow-up Questions:
 *  - How would you optimize if s is very long (millions of characters)?
 *    → Pre-process s to build position indices for each character, use binary search for matching.
 *  - What if words array contains millions of duplicate words?
 *    → Use HashMap to count word frequencies, process unique words only once.
 *  - Can you support real-time queries (adding new words dynamically)?
 *    → Maintain bucket structure and just add new words to appropriate buckets.
 *  - How would you parallelize this for distributed systems?
 *    → Partition words array across machines, each processes independently and aggregates results.
 *
 */
public class NumberOfMatchingSubsequences {

    /**
     * Bucket-based approach to count matching subsequences.
     * 
     * Intuition:
     * Each character in targetString can be seen as an opportunity to advance the matching progress of words waiting for that character.
     * By organizing words into buckets based on their current matching character, we can efficiently process targetString in a single pass.
     * For each character in targetString, we only need to process the words in the corresponding bucket, advancing their matching state.
     * This avoids redundant checks and allows us to handle large inputs efficiently.
     * 
     * Algorithm:
     * 1. Create Map of character to Queue of WordIterators (word + current index)
     * 2. For each character in targetString, process its bucket:
     *    - For each WordIterator in the bucket, move to next character
     *    - If end of word reached, increment match count
     *    - Else, add WordIterator to bucket of its next character 
     * 3. Return total match count
     * @param targetString
     * @param words
     * @return
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
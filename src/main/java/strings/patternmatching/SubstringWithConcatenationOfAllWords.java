package strings.patternmatching;

import java.util.*;

/**
 * Problem: Substring with Concatenation of All Words
 *
 * Given a string and equal-length words, return every start index where a
 * contiguous block contains each word exactly once in any order. There can be no
 * extra characters between words in that block.
 *
 * Leetcode: https://leetcode.com/problems/substring-with-concatenation-of-all-words/ (Hard)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Strings | Sliding window by word length | Frequency map
 *
 * Example:
 *   Input:  s = "barfoothefoobarman", words = ["foo","bar"]
 *   Output: [0, 9]
 *   Why:    starts 0 and 9 form "barfoo" and "foobar", each using both words once.
 *
 * Follow-ups:
 *   1. What if words have different lengths?
 *      Use a trie or DP over word boundaries instead of fixed-size jumps.
 *   2. How would you handle many duplicate words?
 *      Keep required counts and shrink the window when one word is overused.
 *   3. How can this be optimized for repeated queries on the same s?
 *      Precompute word hashes for each length and reuse them across word sets.
 */
public class SubstringWithConcatenationOfAllWords {

    public static void main(String[] args) {
        SubstringWithConcatenationOfAllWords solver = new SubstringWithConcatenationOfAllWords();

        String[] inputs = {"barfoothefoobarman", "barfoofoobarthefoobarman", "wordgoodgoodgoodbestword"};
        String[][] words = { {"foo", "bar"}, {"bar", "foo", "the"}, {"word", "good", "best", "word"} };
        String[] expected = {"[0, 9]", "[6, 9, 12]", "[]"};

        for (int i = 0; i < inputs.length; i++) {
            List<Integer> got = solver.findSubstring(inputs[i], words[i]);
            System.out.printf("s=%s words=%s -> %s  expected=%s%n",
                inputs[i], Arrays.toString(words[i]), got, expected[i]);
        }
    }
    /**
     * Intuition: because all words have the same length, valid starts align by
     * wordLength offsets. For each offset, slide in whole-word jumps, keeping word
     * counts and shrinking whenever a word appears too many times.
     *
     * Algorithm:
     *   1. Build the required word frequency map and total concatenation length.
     *   2. For each offset from 0 to wordLength - 1, slide a word-sized window.
     *   3. Add known words, shrink overused words, and record starts with all words.
     *   4. Reset the window when an unknown word appears.
     *
     * Time:  O(n * w) - each offset scans word-sized chunks across s.
     * Space: O(m) - maps store counts for the m words in the current window.
     *
     * @param s String to search.
     * @param words Equal-length words that must all appear once.
     * @return Starting indices of valid concatenations.
     */
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

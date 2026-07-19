package strings.hashmap;

import java.util.*;

/**
 * Problem: Most Common Word
 *
 * Given a paragraph and a list of banned words, return the lowercase word that
 * appears most often while not being banned. Punctuation separates words and the
 * answer is guaranteed to be unique.
 *
 * Leetcode: https://leetcode.com/problems/most-common-word/ (Easy)
 * Rating:   acceptance 44.4% (Easy), contest rating 1298
 * Pattern:  Hash set | Frequency map | Text normalization
 *
 * Example:
 *   Input:  paragraph = "Bob hit a ball, the hit BALL flew far after it was hit.", banned = ["hit"]
 *   Output: "ball"
 *   Why:    hit is ignored, and ball appears twice while every other allowed word appears once.
 *
 * Follow-ups:
 *   1. Return the top k allowed words?
 *      Count the same frequencies, then use a heap or sort by count.
 *   2. Process a very large paragraph stream?
 *      Tokenize character by character and update counts incrementally.
 *   3. Preserve case sensitivity?
 *      Remove lowercase normalization from both paragraph words and banned words.
 *
 * Related: Top K Frequent Words (692), Word Pattern (290).
 */
public class MostCommonWord {

    public static void main(String[] args) {
        MostCommonWord solver = new MostCommonWord();
        String[] paragraphs = {"Bob hit a ball, the hit BALL flew far after it was hit.", "a."};
        String[][] banned = { {"hit"}, {} };
        String[] expected = {"ball", "a"};

        for (int i = 0; i < paragraphs.length; i++) {
            String got = solver.mostCommonWord(paragraphs[i], banned[i]);
            System.out.printf("paragraph=%s banned=%s -> %s  expected=%s%n",
                paragraphs[i], java.util.Arrays.toString(banned[i]), got, expected[i]);
        }
    }


    /**
     * Intuition: banned lookup should be constant time, and punctuation should not
     * affect word identity. Normalize the paragraph to lowercase words, skip banned
     * words, and keep the best frequency while counting.
     *
     * Algorithm:
     *   1. Return an empty string for null or empty paragraph input.
     *   2. Put lowercase banned words into a HashSet.
     *   3. Split the lowercase paragraph on non-letter characters.
     *   4. Count each allowed word and update the best word whenever its count increases past maxCount.
     *
     * Time:  O(n + m) - n paragraph characters plus m banned words.
     * Space: O(n + m) - stored word counts and banned words.
     *
     * @param paragraph text containing words and punctuation
     * @param banned words to ignore while counting
     * @return most frequent non-banned lowercase word
     */
    public String mostCommonWord(String paragraph, String[] banned) {
        if (paragraph == null || paragraph.isEmpty()) {
            return "";
        }

        // Convert banned array to set for efficient lookup
        Set<String> bannedSet = new HashSet<>();
        for (String word : banned) {
            bannedSet.add(word.toLowerCase());
        }

        Map<String, Integer> wordCount = new HashMap<>();

        // Split paragraph by non-alphabetic characters and process each word
        String[] words = paragraph.toLowerCase().split("[^a-zA-Z]+");
        int maxCount = 0;
        String mostCommon = "";

        for (String word : words) {
            if (!word.isEmpty() && !bannedSet.contains(word)) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                if (wordCount.get(word) > maxCount) {
                    maxCount = wordCount.get(word);
                    mostCommon = word;  
                }
            }
        }
        return mostCommon;
        
    }

    /**
     * Better approach using character-by-character parsing for better control.
     *
     * Steps:
     * 1. Use StringBuilder to build words character by character.
     * 2. Handle punctuation and spaces manually.
     * 3. Count frequencies and track maximum in a single pass.
     *
     * Time Complexity: O(n + m) where n is paragraph length, m is banned array size
     * Space Complexity: O(n + m) for word storage and banned set
     *
     * @param paragraph Input text containing words and punctuation
     * @param banned    Array of words that should be ignored
     * @return Most frequent word that is not banned
     */
    public String mostCommonWordOptimized(String paragraph, String[] banned) {
        // Lowercase banned words when building set
        Set<String> bannedSet = new HashSet<>();
        for (String word : banned) {
            bannedSet.add(word.toLowerCase());
        }

        Map<String, Integer> wordCount = new HashMap<>();
        StringBuilder currBuilder = new StringBuilder();

        String mostCommon = "";
        int maxCount = 0;

        // Process each character in paragraph
        for (int i = 0; i <= paragraph.length(); i++) {
            char c = i < paragraph.length() ? paragraph.charAt(i) : ' ';

            if (Character.isLetter(c)) {
                currBuilder.append(Character.toLowerCase(c));
            } else if (currBuilder.length() > 0) {
                String currWord = currBuilder.toString();

                if (!bannedSet.contains(currWord)) {
                    int count = wordCount.getOrDefault(currWord, 0) + 1;
                    wordCount.put(currWord, count);

                    if (count > maxCount) {
                        maxCount = count;
                        mostCommon = currWord;
                    }
                }

                currBuilder.setLength(0); // Clear the word buffer
            }
        }

        return mostCommon;
    }
}

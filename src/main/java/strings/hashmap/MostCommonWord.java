package strings.hashmap;

import java.util.*;

/**
 * LeetCode 819. Most Common Word
 *
 * Given a string paragraph and a string array of the banned words banned,
 * return the most frequent word that is not banned. It is guaranteed there is
 * at least one word that is not banned, and that the answer is unique.
 *
 * Example 1:
 * Input: paragraph = "Bob hit a ball, the hit BALL flew far after it was hit.", banned = ["hit"]
 * Output: "ball"
 * Explanation: "hit" occurs 3 times, but it's banned. "ball" occurs twice and is not banned.
 *
 * LeetCode Link: https://leetcode.com/problems/most-common-word/
 *
 * Follow-up Questions:
 * - How would you handle case-sensitive comparison? (Remove toLowerCase() calls)
 * - Can you optimize for very long paragraphs with few unique words? (Use StringBuilder for word building)
 * - How would you extend to return top k most common words? (Use priority queue)
 * - What if banned words can contain punctuation? (Apply same normalization to banned words)
 * LeetCode Contest Rating: 1298
 */
public class MostCommonWord {

    /**
     * Finds the most frequent non-banned word in the paragraph.
     *
     * Algorithm:
     * 1. Convert banned array to HashSet for O(1) lookup
     * 2. Split paragraph by non-alphabetic characters using regex
     * 3. Process each word: convert to lowercase and check if not banned
     * 4. Count word frequencies using HashMap
     * 5. Find word with maximum frequency
     *
     * Time Complexity: O(n + m) where n is paragraph length, m is banned array size
     * Space Complexity: O(n + m) for word storage and banned set
     *
     * @param paragraph Input text containing words and punctuation
     * @param banned Array of words that should be ignored
     * @return Most frequent word that is not banned
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

        for (String word : words) {
            if (!word.isEmpty() && !bannedSet.contains(word)) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
        }

        // Find the word with maximum frequency
        String mostCommon = "";
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostCommon = entry.getKey();
            }
        }

        return mostCommon;
    }

    /**
     * Alternative approach using character-by-character parsing for better control.
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
     * @param banned Array of words that should be ignored
     * @return Most frequent word that is not banned
     */
    public String mostCommonWordOptimized(String paragraph, String[] banned) {
        Set<String> bannedSet = new HashSet<>(Arrays.asList(banned));
        Map<String, Integer> wordCount = new HashMap<>();
        StringBuilder word = new StringBuilder();

        String mostCommon = "";
        int maxCount = 0;

        // Process each character in paragraph
        for (int i = 0; i <= paragraph.length(); i++) {
            char c = i < paragraph.length() ? paragraph.charAt(i) : ' ';

            if (Character.isLetter(c)) {
                word.append(Character.toLowerCase(c));
            } else if (word.length() > 0) {
                String currentWord = word.toString();

                if (!bannedSet.contains(currentWord)) {
                    int count = wordCount.getOrDefault(currentWord, 0) + 1;
                    wordCount.put(currentWord, count);

                    if (count > maxCount) {
                        maxCount = count;
                        mostCommon = currentWord;
                    }
                }

                word.setLength(0); // Clear the word buffer
            }
        }

        return mostCommon;
    }
}

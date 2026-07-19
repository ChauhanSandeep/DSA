package strings.greedy;

import java.util.PriorityQueue;

/**
 * Problem: Reorganize String
 *
 * Rearrange the characters of a lowercase string so no two adjacent characters
 * are equal. Return any valid rearrangement, or the empty string when no such
 * arrangement exists.
 *
 * Leetcode: https://leetcode.com/problems/reorganize-string/ (Medium)
 * Rating:   acceptance 56.4% (Medium), contest rating 1681
 * Pattern:  Greedy | Max heap | Pair most frequent characters
 *
 * Example:
 *   Input:  s = "aab"
 *   Output: "aba"
 *   Why:    the two 'a' characters are separated by 'b', so no equal characters touch.
 *
 * Follow-ups:
 *   1. Keep identical characters at least k distance apart?
 *      Use a cooldown queue before a character can return to the heap.
 *   2. Return the lexicographically smallest valid rearrangement?
 *      Combine feasibility checks with ordered candidate selection at each position.
 *   3. Support arbitrary Unicode characters?
 *      Replace the 26-count array with a frequency map keyed by character.
 *   4. Count all valid rearrangements?
 *      Use backtracking with memoization over remaining counts and previous character.
 *
 * Related: Rearrange String k Distance Apart (358), Task Scheduler (621), Longest Happy String (1405).
 */
public class ReorganizeString {

    public static void main(String[] args) {
        ReorganizeString solver = new ReorganizeString();
        String[] inputs = {"aab", "aaab", "a"};
        String[] expected = {"aba", "", "a"};

        for (int i = 0; i < inputs.length; i++) {
            String got = solver.reorganizeString(inputs[i]);
            System.out.printf("s=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }

    /**
     * Intuition: if one character appears more than half the string length rounded
     * up, there are not enough other characters to separate its copies. Otherwise,
     * repeatedly placing the two most frequent remaining characters keeps the most
     * dangerous counts apart.
     *
     * Algorithm:
     *   1. Count character frequencies and reject impossible inputs using the max count.
     *   2. Put every present character into a max heap ordered by remaining count.
     *   3. While at least two characters remain, append the two most frequent and decrement them.
     *   4. Append the last remaining character if any, then return the built string.
     *
     * Time:  O(n log k) - each appended character may update the heap, with k <= 26.
     * Space: O(n + k) - the result builder plus the heap and frequency array.
     *
     * @param str lowercase input string to reorganize
     * @return a valid reorganization, or the empty string if impossible
     */
    public String reorganizeString(String str) {
        if (str == null || str.length() <= 1) {
            return str == null ? "" : str;
        }

        int length = str.length();
        int[] freq = new int[26];

        int maxFreq = 0;
        for (char ch : str.toCharArray()) {
            int index = ch - 'a';
            freq[index]++;
            maxFreq = Math.max(maxFreq, freq[index]);
        }

        // Feasibility check: if any character appears more than (length + 1) / 2 times, no valid arrangement exists.
        if (maxFreq > (length + 1) / 2) {
            return "";
        }

        // Max-heap based on remaining frequency of characters.
        java.util.PriorityQueue<CharacterCount> maxHeap =
                new java.util.PriorityQueue<>((a, b) -> Integer.compare(b.count, a.count));

        // Seed the heap with characters that appear at least once.
        for (int i = 0; i < 26; i++) {
            if (freq[i] > 0) {
                maxHeap.offer(new CharacterCount((char) ('a' + i), freq[i]));
            }
        }

        StringBuilder result = new StringBuilder();

        while (maxHeap.size() >= 2) {
            // Take the two most frequent characters.
            CharacterCount first = maxHeap.poll();
            CharacterCount second = maxHeap.poll();

            // Append them in sequence to ensure they are different adjacent characters.
            result.append(first.character);
            result.append(second.character);

            // Decrement counts and reinsert if they still have remaining occurrences.
            if (--first.count > 0) {
                maxHeap.offer(first);
            }

            if (--second.count > 0) {
                maxHeap.offer(second);
            }
        }

        // If one character remains, append it (safe due to earlier feasibility check).
        if (!maxHeap.isEmpty()) {
            CharacterCount last = maxHeap.poll();
            // As a safety check, ensure not equal to the last appended character.
            if (result.length() > 0 && result.charAt(result.length() - 1) == last.character) {
                // This scenario should not occur given our feasibility check and construction logic.
                return "";
            }
            result.append(last.character);
        }

        return result.toString();
    }

    // Helper class to store character and its remaining count.
    private static class CharacterCount {
        private final char character;
        private int count;

        CharacterCount(char character, int count) {
            this.character = character;
            this.count = count;
        }
    }
}

package stacksandqueues;

import java.util.*;

/**
 * Problem: First Non-Repeating Character
 *
 * Given characters in a fixed string or in arrival order from a stream, report
 * the earliest character whose frequency is exactly one. The fixed-string
 * version returns that character; the stream version reports the current first
 * unique character after every input character.
 *
 *
 * Leetcode: https://leetcode.com/problems/first-unique-character-in-a-string/ (Easy)
 * Pattern:  Hash map | Queue | Insertion order
 *
 * Example:
 *   Input:  stream = "aabc"
 *   Output: "a#bb"
 *   Why:    after the second 'a' there is no unique character, so that prefix outputs '#'.
 *
 * Follow-ups:
 *   1. Need Unicode code points instead of Java char units?
 *      Iterate over code points and store Integer keys rather than Character keys.
 *   2. The stream is infinite and memory must be bounded?
 *      Exact answers need all candidates; approximate only with eviction or sketches.
 *   3. Need deletion events as well as insertions?
 *      Maintain counts plus a linked order structure that can re-activate old characters.
 *   4. Need the first unique index instead of character?
 *      Store each character's first index with its count and scan insertion order.
 */
public class FirstNonRepeating {
    public static void main(String[] args) {
        String[] inputs = {"abcdefghija", "hello", "aabb"};
        String[] expected = {"b", "h", "throws"};
        for (int i = 0; i < inputs.length; i++) {
            String got;
            try { got = String.valueOf(findFirstUniqueCharacter(inputs[i])); }
            catch (NoSuchElementException ex) { got = "throws"; }
            System.out.printf("input=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }

        /**
     * Intuition: the answer needs final frequency and original order. A
     * LinkedHashMap counts characters while preserving first-seen order, so the
     * first entry with count one after counting is the first non-repeating
     * character.
     *
     * Algorithm:
     *   1. Count every character in a LinkedHashMap.
     *   2. Iterate entries in first-seen order.
     *   3. Return the first character whose count is one.
     *   4. Throw when no unique character exists.
     *
     * Time:  O(n) - one count pass plus one distinct-character scan.
     * Space: O(u) - one entry per distinct character.
     *
     * @param input input string to inspect
     * @return first character that appears exactly once
     */
public static char findFirstUniqueCharacter(String input) {
        // LinkedHashMap is used because it maintains insertion order and allows O(1) access
        // It do it by using a doubly linked list internally which is used to maintain the order
        Map<Character, Integer> charFrequency = new LinkedHashMap<>();

        // Count occurrences of each character
        for (char ch : input.toCharArray()) {
            charFrequency.put(ch, charFrequency.getOrDefault(ch, 0) + 1);
        }

        // Find the first character with a count of 1
        for (Map.Entry<Character, Integer> entry : charFrequency.entrySet()) {
            if (entry.getValue() == 1) {
                return entry.getKey();
            }
        }

        throw new NoSuchElementException("No non-repeating character found in the string.");
    }

    /**
     * Processes a stream of characters and prints the first non-repeating character
     * in real-time after each character input.
     *
     * Approach
     * - Use a HashMap to count character frequencies.
     * - Use a Queue to maintain the order of unique characters.
     * - For each character:
     *  - Update its frequency in the HashMap.
     *  - If the frequency is 1, add it to the Queue.
     *  - If the frequency is greater than 1, remove it from the Queue.
     *  - Print the first character in the Queue or '#' if it's empty.
     *
     *  Time complexity : O(N)
     *  Space complexity : O(N)
     *
     * @param stream The input stream of characters.
     */
    public static void processCharacterStream(String stream) {
        Map<Character, Integer> charCount = new HashMap<>();
        Queue<Character> uniqueChars = new LinkedList<>();

        for (char ch : stream.toCharArray()) {
            // Update character frequency
            charCount.put(ch, charCount.getOrDefault(ch, 0) + 1);

            // Add character to queue if seen for the first time
            if (charCount.get(ch) == 1) {
                uniqueChars.offer(ch);
            }

            // Remove characters from the front if they are no longer unique
            while (!uniqueChars.isEmpty() && charCount.get(uniqueChars.peek()) > 1) {
                uniqueChars.poll();
            }

            // Print the first non-repeating character, or '#' if none exist
            System.out.println(uniqueChars.isEmpty() ? "#" : uniqueChars.peek());
        }
    }
}

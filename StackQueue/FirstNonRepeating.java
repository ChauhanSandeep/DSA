package StackQueue;

import java.util.*;

/**
 * This class provides solutions to find the first non-repeating character in a given string.
 * 
 * <p><b>Approach:</b></p>
 * - The `findFirstUniqueCharacter` method finds the first non-repeating character in a string
 *   using a LinkedHashMap for maintaining insertion order.
 * - The `findFirstUniqueInStream` method finds the first non-repeating character in a real-time
 *   streaming fashion using a HashMap and a Queue.
 * 
 * <p><b>Time Complexity:</b></p>
 * - `findFirstUniqueCharacter`: O(N) (single pass for counting, single pass for lookup)
 * - `findFirstUniqueInStream`: O(N) (each character is processed at most twice)
 * 
 * <p><b>Space Complexity:</b> O(N) for storing character counts.</p>
 * 
 * <p>LeetCode Problem Link: 
 * <a href="https://leetcode.com/problems/first-unique-character-in-a-string/">First Unique Character in a String</a>
 * </p>
 */
public class FirstNonRepeating {

    public static void main(String[] args) {
        System.out.println(findFirstUniqueCharacter("abcdefghija")); // Output: b
        System.out.println(findFirstUniqueCharacter("hello"));       // Output: h
        System.out.println(findFirstUniqueCharacter("Java"));        // Output: J
        System.out.println(findFirstUniqueCharacter("simplest"));    // Output: i

        processCharacterStream("aabc");
    }

    /**
     * Finds the first non-repeating character in a given string.
     *
     * @param input The input string.
     * @return The first non-repeating character.
     * @throws NoSuchElementException If no unique character is found.
     */
    public static char findFirstUniqueCharacter(String input) {
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

package string;

import java.util.HashSet;
import java.util.Set;

/**
 * Problem: Remove duplicate characters from a given string while maintaining order.
 * 
 * Intuition:
 * - Use a HashSet to track encountered characters.
 * - Append characters to a StringBuilder only if they have not been added before.
 * - Maintain the original order of first occurrences.
 * 
 * Time Complexity: O(N) - Single pass through the string.
 * Space Complexity: O(N) - HashSet for storing unique characters.
 */
public class RemoveDuplicateCharacters {

    public static void main(String[] args) {
        String input = "geeksforGeeks";
        String output = removeDuplicates(input, false); // Case-sensitive
        System.out.println("String after removing duplicates: " + output);

        String outputIgnoreCase = removeDuplicates(input, true); // Case-insensitive
        System.out.println("String after removing duplicates (ignoring case): " + outputIgnoreCase);
    }

    /**
     * Removes duplicate characters from the input string while maintaining order.
     *
     * @param input        The input string.
     * @param ignoreCase   Whether to ignore case sensitivity (true for case-insensitive).
     * @return The string with duplicate characters removed.
     */
    public static String removeDuplicates(String input, boolean ignoreCase) {
        if (input == null || input.isEmpty()) {
            return input; // Return original string for null/empty cases.
        }

        Set<Character> seenCharacters = new HashSet<>();
        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            char characterToCheck = ignoreCase ? Character.toLowerCase(c) : c;

            if (!seenCharacters.contains(characterToCheck)) {
                result.append(c); // Append original character
                seenCharacters.add(characterToCheck);
            }
        }
        return result.toString();
    }
}

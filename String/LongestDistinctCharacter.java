package String;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode Problem: https://leetcode.com/problems/longest-substring-without-repeating-characters/
 *
 * Problem:
 * Given a string, find the length of the longest substring with all unique characters.
 *
 * Approach (Sliding Window + HashMap):
 * - Maintain a **hash map (charIndexMap)** to store the latest index of each character.
 * - Use two pointers: 
 *   - `start`: Tracks the beginning of the window.
 *   - `end`: Expands the window by iterating over the string.
 * - If a duplicate character is found:
 *   - Move `start` to the right of the last occurrence of that character.
 * - Update `maxLength` to track the longest unique substring.
 *
 * Time Complexity: O(N) (each character is processed at most twice)
 * Space Complexity: O(min(N, 26)) (since the map stores unique characters)
 */
public class LongestDistinctCharacter {
    public static void main(String[] args) {
        String input = "abababcdefababcdab";
        int result = findLongestDistinctLength(input);
        System.out.println("Length of longest substring with unique characters: " + result);
    }

    public static int findLongestDistinctLength(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }

        Map<Character, Integer> charIndexMap = new HashMap<>();
        int start = 0; // Left boundary of the sliding window
        int maxLength = 0; // Stores the maximum length of unique substring

        for (int end = 0; end < input.length(); end++) {
            char currentChar = input.charAt(end);

            // If character is repeated within the window, move 'start' ahead
            if (charIndexMap.containsKey(currentChar) && charIndexMap.get(currentChar) >= start) {
                start = charIndexMap.get(currentChar) + 1;
            }

            // Store/update character's latest index
            charIndexMap.put(currentChar, end);

            // Update the max length of substring found
            maxLength = Math.max(maxLength, end - start + 1);
        }

        return maxLength;
    }
}

package DailyBytes.ArrayPackage;

import java.util.HashMap;
import java.util.Map;

/**
 * This class finds the index of the first unique character in a given string.
 * 
 * Algorithm:
 * - Use a hashmap to store the indices of characters and remove entries if the character appears more than once.
 * - Iterate through the hashmap to find the smallest index.
 * - Time Complexity: O(n)
 * - Space Complexity: O(n)
 * 
 * LeetCode Problem Link: https://leetcode.com/problems/first-unique-character-in-a-string/
 */
public class FirstUniqueChar {

    public static void main(String[] args) {
        System.out.println("First unique character is at index: " + getFirstUniqueChar("loveleetcode"));
        System.out.println("First unique character is at index: " + getFirstUniqueChar("thedailybyte"));
    }

    /**
     * Finds the index of the first unique character in the given string.
     * @param str The input string.
     * @return The index of the first unique character, or -1 if no unique character exists.
     */
    public static int getFirstUniqueChar(String str) {
        Map<Character, Integer> characterIndexMap = new HashMap<>();

        // Iterate through the string to populate the character-index map
        for (int i = 0; i < str.length(); i++) {
            char currentChar = str.charAt(i);
            if (characterIndexMap.containsKey(currentChar)) {
                characterIndexMap.remove(currentChar);
            } else {
                characterIndexMap.put(currentChar, i);
            }
        }

        // Find the smallest index of unique characters
        int smallestIndex = Integer.MAX_VALUE;
        for (Map.Entry<Character, Integer> entry : characterIndexMap.entrySet()) {
            if (entry.getValue() < smallestIndex) {
                smallestIndex = entry.getValue();
            }
        }

        // If no unique character found, return -1
        if (smallestIndex == Integer.MAX_VALUE) {
            return -1;
        }

        return smallestIndex;
    }
}

package DailyBytes.ArrayPackage;

/**
 * This class contains a method to determine if two strings are valid anagrams of each other.
 * 
 * Algorithm:
 * - Use a frequency array to count the occurrences of each character in both strings.
 * - Compare the frequency arrays to check for anagram validity.
 * - Time Complexity: O(n)
 * - Space Complexity: O(1)
 * 
 * LeetCode Problem Link: https://leetcode.com/problems/valid-anagram/
 */
public class ValidAnagram {

    public static void main(String[] args) {
        System.out.println("Are 'cat' and 'tac' anagrams? " + validAnagrams("cat", "tac"));
        System.out.println("Are 'listen' and 'silent' anagrams? " + validAnagrams("listen", "silent"));
        System.out.println("Are 'program' and 'function' anagrams? " + validAnagrams("program", "function"));
    }

    /**
     * Determines if two strings are anagrams of each other.
     * @param str1 The first input string.
     * @param str2 The second input string.
     * @return True if the strings are anagrams, false otherwise.
     */
    public static boolean validAnagrams(String str1, String str2) {
        if (str1.length() != str2.length()) {
            return false;
        }

        int[] charFrequency = new int[26];

        // Increment counts for characters in the first string
        for (char c : str1.toCharArray()) {
            charFrequency[c - 'a']++;
        }

        // Decrement counts for characters in the second string
        for (char c : str2.toCharArray()) {
            charFrequency[c - 'a']--;
        }

        // Check if all counts are zero
        for (int count : charFrequency) {
            if (count != 0) {
                return false;
            }
        }

        return true;
    }
}

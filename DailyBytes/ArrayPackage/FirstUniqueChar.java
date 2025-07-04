package DailyBytes.ArrayPackage;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * ✅ Problem: First Unique Character in a String
 *
 * Given a string `s`, return the index of the **first non-repeating character** in it.
 * If it doesn't exist, return -1.
 *
 * 🔗 Leetcode: https://leetcode.com/problems/first-unique-character-in-a-string/
 *
 * 🧠 Example:
 * Input:  "loveleetcode"
 * Output: 2 ('v' is the first non-repeating character)
 *
 * 🔍 Follow-Up Questions:
 * 1. What if you need to return the first unique *character* instead of index? ➤ Small change in logic
 * 2. Can this be done in one pass? ➤ Not reliably without using LinkedHashMap
 * 3. What if the input stream is continuous? ➤ Use a frequency map + queue to track candidates
 */
public class FirstUniqueChar {

    public static void main(String[] args) {
        System.out.println("First unique character is at index: " + findFirstUniqueCharIndex("loveleetcode"));
        System.out.println("First unique character is at index: " + findFirstUniqueCharIndex("thedailybyte"));
    }

    /**
     * ✅ Optimized approach using frequency array.
     *
     * Steps:
     * 1. Count frequency of each character.
     * 2. In a second pass, return index of the first char with frequency 1.
     *
     * Time Complexity: O(n) — two passes over the input string
     * Space Complexity: O(1) — constant space as char set is fixed (only lowercase a-z)
     *
     * @param str The input string
     * @return Index of first unique character or -1 if none exists
     */
    public static int findFirstUniqueCharIndex(String str) {
        if (str == null || str.isEmpty()) return -1;

        int[] freq = new int[26];
        int length = str.length();

        for (int i = 0; i < length; i++) {
            freq[str.charAt(i) - 'a']++;
        }

        for (int i = 0; i < length; i++) {
            if (freq[str.charAt(i) - 'a'] == 1) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 🔁 Alternative method using Map to track index of first occurrence,
     * and remove if character repeats (less efficient due to removals).
     *
     * Time: O(n) on average, worst-case O(n²) due to `remove()` in `HashMap`
     * Space: O(n)
     */
    public static int findFirstUniqueCharIndexWithMap(String str) {
        if (str == null || str.isEmpty()) return -1;

        Map<Character, Integer> charIndexMap = new LinkedHashMap<>();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (charIndexMap.containsKey(ch)) {
                charIndexMap.remove(ch); // Not unique anymore
            } else {
                charIndexMap.put(ch, i);
            }
        }

        return charIndexMap.values().stream().findFirst().orElse(-1);
    }
}

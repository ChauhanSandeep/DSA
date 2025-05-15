package String;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Find All Anagrams in a String
 *
 * Given two strings `text` and `word`, find all **start indices** of `word`'s anagrams in `text`.
 *
 * Intuition:
 * - Use the **Sliding Window** technique to check anagram matches efficiently.
 * - Instead of `HashMap<Character, Integer>`, use a **fixed-size array** (`int[26]`) since `word` contains only lowercase letters.
 * - Maintain a `matchCount` to track matched characters instead of comparing maps.
 *
 * Approach:
 * 1. **Precompute the frequency count of `word`**.
 * 2. **Initialize a sliding window** of size `word.length()`.
 * 3. **Slide the window across `text`**, updating character frequencies dynamically.
 * 4. **Check if window matches `word`'s frequency** and store valid indices.
 *
 * Time Complexity: **O(N)**, where N = length of `text` (Sliding Window runs in linear time)
 * Space Complexity: **O(1)** (Fixed-size frequency arrays)
 *
 * Problem Link: https://leetcode.com/problems/find-all-anagrams-in-a-string/
 */
public class AllAnagrams {

    public static void main(String[] args) {
        List<Integer> result = new AllAnagrams().findAnagrams("abab", "ab");
        System.out.println(result); // Expected: [0, 1, 2]
    }

    public List<Integer> findAnagrams(String text, String word) {
        List<Integer> result = new ArrayList<>();
        if (text == null || word == null || text.length() < word.length()) return result;

        int[] wordFreq = new int[26];
        int[] windowFreq = new int[26];

        // Step 1: Count frequency of characters in `word`
        for (char ch : word.toCharArray()) {
            wordFreq[ch - 'a']++;
        }

        int left = 0, right = 0, matchCount = 0;
        int wordLength = word.length();
        int textLength = text.length();

        // Step 2: Expand the window while tracking frequency
        for (; right < textLength; right++) {
            char addedChar = text.charAt(right);
            windowFreq[addedChar - 'a']++;

            // If window size exceeds `wordLength`, shrink from the left
            if (right - left + 1 > wordLength) {
                char removedChar = text.charAt(left);
                windowFreq[removedChar - 'a']--;
                left++;
            }

            // Step 3: If the window matches `wordFreq`, store the starting index
            if (isAnagram(wordFreq, windowFreq)) {
                result.add(left);
            }
        }

        return result;
    }

    /**
     * Checks if two frequency arrays are identical.
     */
    private boolean isAnagram(int[] freq1, int[] freq2) {
        for (int i = 0; i < 26; i++) {
            if (freq1[i] != freq2[i]) return false;
        }
        return true;
    }
}

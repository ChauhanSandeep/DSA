package strings;

/**
 * LeetCode 953. Verifying an Alien Dictionary
 *
 * In an alien language, surprisingly, they also use English lowercase letters, but possibly
 * in a different order. The order of the alphabet is some permutation of lowercase letters.
 *
 * Given a sequence of words written in the alien language, and the order of the alphabet,
 * return true if and only if the given words are sorted lexicographically in this alien language.
 *
 * Example 1:
 * Input: words = ["hello","leetcode"], order = "hlabcdefgijkmnopqrstuvwxyz"
 * Output: true
 * Explanation: As 'h' comes before 'l' in this language, then the sequence is sorted.
 *
 * Example 2:
 * Input: words = ["word","world","row"], order = "worldabcefghijkmnpqstuvxyz"
 * Output: false
 * Explanation: As 'd' comes after 'l' in this language, then words[0] > words[1], hence the sequence is unsorted.
 *
 * LeetCode Link: https://leetcode.com/problems/verifying-an-alien-dictionary/
 *
 * Follow-up Questions:
 * - How would you optimize for very long word lists? (Early termination on first violation)
 * - Can you handle case-insensitive comparison? (Convert to lowercase before processing)
 * - How would you extend to support numbers and special characters? (Expand character mapping)
 * - What if we need to sort the words instead of just verify? (Use custom comparator with same logic)
 */
public class VerifyingAnAlienDictionary {

    /**
     * Verifies if words are sorted according to alien dictionary order.
     *
     * Algorithm:
     * 1. Create mapping from each character to its order index in alien alphabet
     * 2. Compare each adjacent pair of words using alien order
     * 3. For each pair, compare character by character using alien order
     * 4. If first word is lexicographically larger, return false
     * 5. If all pairs are in correct order, return true
     *
     * Time Complexity: O(n * m) where n is number of words, m is average word length
     * Space Complexity: O(1) - fixed size array for character mapping (26 letters)
     *
     * @param words Array of words to verify ordering
     * @param order String representing alien alphabet order
     * @return true if words are sorted in alien dictionary order, false otherwise
     */
    public boolean isAlienSorted(String[] words, String order) {
        if (words == null || words.length <= 1) {
            return true;
        }

        // Create character order mapping
        int[] charOrder = new int[26]; // charOrder contains the index of each character in the alien order
        for (int i = 0; i < order.length(); i++) {
            int normalizedChar = order.charAt(i) - 'a';
            charOrder[normalizedChar] = i;
        }

        // Check each adjacent pair of words
        for (int i = 0; i < words.length - 1; i++) {
            if (!isInOrder(words[i], words[i + 1], charOrder)) {
                return false;
            }
        }

        return true;
    }

    // Helper method to check if two words are in correct order
    private boolean isInOrder(String word1, String word2, int[] charOrder) {
        int minLength = Math.min(word1.length(), word2.length());

        // Compare character by character
        for (int i = 0; i < minLength; i++) {
            char c1 = word1.charAt(i);
            char c2 = word2.charAt(i);

            int order1 = charOrder[c1 - 'a'];
            int order2 = charOrder[c2 - 'a'];

            if (order1 < order2) {
                return true; // word1 comes before word2
            } else if (order1 > order2) {
                return false; // word1 comes after word2
            }
            // Continue if characters are equal
        }

        // All compared characters are equal, check lengths
        // Shorter word should come first
        return word1.length() <= word2.length();
    }
}

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
        int[] charOrder = new int[26];
        for (int i = 0; i < order.length(); i++) {
            charOrder[order.charAt(i) - 'a'] = i;
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

    /**
     * Alternative approach using custom comparator logic inline.
     */
    public boolean isAlienSortedInline(String[] words, String order) {
        int[] charOrder = new int[26];
        for (int i = 0; i < order.length(); i++) {
            charOrder[order.charAt(i) - 'a'] = i;
        }

        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];

            boolean foundDifference = false;

            // Compare characters
            for (int j = 0; j < Math.min(word1.length(), word2.length()); j++) {
                int order1 = charOrder[word1.charAt(j) - 'a'];
                int order2 = charOrder[word2.charAt(j) - 'a'];

                if (order1 > order2) {
                    return false;
                } else if (order1 < order2) {
                    foundDifference = true;
                    break;
                }
            }

            // If no difference found and first word is longer, it's incorrect
            if (!foundDifference && word1.length() > word2.length()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Optimized approach with early termination and cleaner comparison logic.
     */
    public boolean isAlienSortedOptimized(String[] words, String order) {
        // Create order mapping
        int[] orderMap = new int[26];
        for (int i = 0; i < 26; i++) {
            orderMap[order.charAt(i) - 'a'] = i;
        }

        // Check adjacent pairs
        for (int i = 1; i < words.length; i++) {
            if (compareWords(words[i - 1], words[i], orderMap) > 0) {
                return false;
            }
        }

        return true;
    }

    // Helper method that returns comparison result like compareTo
    private int compareWords(String word1, String word2, int[] orderMap) {
        int i = 0;
        while (i < word1.length() && i < word2.length()) {
            int diff = orderMap[word1.charAt(i) - 'a'] - orderMap[word2.charAt(i) - 'a'];
            if (diff != 0) {
                return diff;
            }
            i++;
        }

        return word1.length() - word2.length();
    }
}

package strings.hashmap;

/**
 * Problem: Verifying an Alien Dictionary
 *
 * Given words written in an alien alphabet order, decide whether the list is
 * sorted lexicographically according to that custom order.
 *
 * Leetcode: https://leetcode.com/problems/verifying-an-alien-dictionary/ (Easy)
 * Rating:   acceptance 55.4% (Easy), contest rating 1300
 * Pattern:  Hashing | Custom order array | Adjacent word comparison
 *
 * Example:
 *   Input:  words = ["hello","leetcode"], order = "hlabcdefgijkmnopqrstuvwxyz"
 *   Output: true
 *   Why:    h comes before l in the alien order, so hello is before leetcode.
 *
 * Follow-ups:
 *   1. Sort the words using the alien order?
 *      Reuse the same rank array inside a custom comparator.
 *   2. Support uppercase or extra symbols?
 *      Build a map from character to rank instead of a 26-entry array.
 *   3. Validate the alien order itself?
 *      Ensure it contains each supported character exactly once.
 *
 * Related: Alien Dictionary (269), Lexicographical Numbers (386).
 */
public class VerifyingAnAlienDictionary {

    public static void main(String[] args) {
        VerifyingAnAlienDictionary solver = new VerifyingAnAlienDictionary();
        String[][] words = { {"hello", "leetcode"}, {"word", "world", "row"}, {"apple", "app"} };
        String[] orders = {"hlabcdefgijkmnopqrstuvwxyz", "worldabcefghijkmnpqstuvxyz", "abcdefghijklmnopqrstuvwxyz"};
        boolean[] expected = {true, false, false};

        for (int i = 0; i < words.length; i++) {
            boolean got = solver.isAlienSorted(words[i], orders[i]);
            System.out.printf("words=%s order=%s -> %s  expected=%s%n",
                java.util.Arrays.toString(words[i]), orders[i], got, expected[i]);
        }
    }


    /**
     * Intuition: a list is sorted if every adjacent pair is sorted. Converting the
     * alien alphabet into ranks lets each character comparison behave like normal
     * lexicographic comparison, but with custom numeric priorities.
     *
     * Algorithm:
     *   1. Return true for null, empty, or single-word input.
     *   2. Build charOrder where each letter maps to its alien rank.
     *   3. Compare every adjacent word pair with the helper.
     *   4. Return false on the first inversion; otherwise return true.
     *
     * Time:  O(n * m) - adjacent comparisons scan up to the average word length.
     * Space: O(1) - the order array has 26 entries.
     *
     * @param words words to verify
     * @param order alien alphabet order
     * @return true if words are sorted according to order
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

    /** Returns whether word1 is lexicographically no greater than word2 under charOrder. */
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

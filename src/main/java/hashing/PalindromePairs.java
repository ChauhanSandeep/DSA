package hashing;

import java.util.*;

/**
 * Problem: Given a list of unique words, find all pairs of distinct indices (i, j)
 * such that the concatenation of words[i] + words[j] forms a palindrome.
 *
 * Example:
 * Input: ["abcd", "dcba", "lls", "s", "sssll"]
 * Output: [[0, 1], [1, 0], [3, 2], [2, 4]]
 * Explanation:
 * - "abcd" + "dcba" = "abcddcba" (palindrome)
 *
 * Leetcode Link: https://leetcode.com/problems/palindrome-pairs/
 *
 * Follow-up Questions:
 * 1. Can we do this in O(n * k) where k = average word length? (Trie-based optimization possible)
 *    Leetcode follow-up ref: https://leetcode.com/problems/palindrome-pairs/solutions/79215/o-n-k-2-java-solution-with-trie-structure/
 * 2. How to handle duplicates in the list? (Not required here as words are unique)
 */
public class PalindromePairs {

    public static void main(String[] args) {
        String[] words = {"abcd", "dcba", "lls", "s", "sssll"};
        System.out.println(new PalindromePairs().findPalindromePairs(words));
    }

    /**
     * Problem: Given a list of unique words, find all pairs of distinct indices (i, j)
     * such that the concatenation of words[i] + words[j] forms a palindrome.
     *
     * Approach:
     * 1. Store words in a HashMap with their corresponding indices for O(1) lookups.
     * 2. Check for three cases:
     *    - Direct reverse match (word1 == reverse(word2)).
     *    - Palindromic prefix with a corresponding reversed suffix.
     *    - Palindromic suffix with a corresponding reversed prefix.
     *
     * Complexity:
     * - Time complexity : O(n * k^2) where n is number of words and k is average length of words.
     *      - because for each word we may check all prefixes and suffixes (k) and palindrome check (k).
     * - Space complexity: O(n) for storing words in the HashMap.
     */
    public List<List<Integer>> findPalindromePairs(String[] words) {
        Map<String, Integer> wordIndexMap = new HashMap<>(); // mapping between word and its index
        List<List<Integer>> palindromePairs = new ArrayList<>(); // to store the result pairs

        // Store each word with its index for fast lookup
        for (int i = 0; i < words.length; i++) {
            wordIndexMap.put(words[i], i);
        }

        // Iterate through each word in the list
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            String reversedWord = new StringBuilder(word).reverse().toString();

            // Case 1: Check if reversed word exists in the map (direct palindrome pair)
            if (wordIndexMap.containsKey(reversedWord) && wordIndexMap.get(reversedWord) != i) {
                palindromePairs.add(Arrays.asList(i, wordIndexMap.get(reversedWord)));
            }

            // Case 2: Check valid palindromic prefixes
            for (String suffix : getValidSuffixes(word)) {
                String reversedSuffix = new StringBuilder(suffix).reverse().toString();
                if (wordIndexMap.containsKey(reversedSuffix)) {
                    palindromePairs.add(Arrays.asList(wordIndexMap.get(reversedSuffix), i));
                }
            }

            // Case 3: Check valid palindromic suffixes
            for (String prefix : getValidPrefixes(word)) {
                String reversedPrefix = new StringBuilder(prefix).reverse().toString();
                if (wordIndexMap.containsKey(reversedPrefix)) {
                    palindromePairs.add(Arrays.asList(i, wordIndexMap.get(reversedPrefix)));
                }
            }
        }

        return palindromePairs;
    }

    /**
     * Returns a list of prefixes where the remaining suffix is a palindrome.
     * If a suffix is a palindrome, the corresponding prefix can form a valid pair.
     *
     * Example: "lls" -> "l" is a valid prefix because "ls" is a palindrome.
     *
     * @param word - Input string
     * @return List of valid prefixes
     */
    private List<String> getValidPrefixes(String word) {
        List<String> validPrefixes = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            if (isPalindrome(word, i, word.length() - 1)) {
                validPrefixes.add(word.substring(0, i)); // Prefix before palindrome suffix
            }
        }
        return validPrefixes;
    }

    /**
     * Returns a list of suffixes where the remaining prefix is a palindrome.
     * If a prefix is a palindrome, the corresponding suffix can form a valid pair.
     *
     * Example: "sll" -> "ll" is a valid suffix because "s" is a palindrome.
     *
     * @param word - Input string
     * @return List of valid suffixes
     */
    private List<String> getValidSuffixes(String word) {
        List<String> validSuffixes = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            if (isPalindrome(word, 0, i)) {
                validSuffixes.add(word.substring(i + 1)); // Suffix after palindrome prefix
            }
        }
        return validSuffixes;
    }

    /**
     * Checks if a substring within a word is a palindrome.
     *
     * @param word - Input string
     * @param left - Start index
     * @param right - End index
     * @return True if the substring is a palindrome, otherwise false
     */
    private boolean isPalindrome(String word, int left, int right) {
        while (left < right) {
            if (word.charAt(left) != word.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}

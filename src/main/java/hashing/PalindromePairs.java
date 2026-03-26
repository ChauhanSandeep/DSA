package hashing;

import java.util.*;

/**
 * Problem: Given a list of unique words, find all pairs of distinct indices (i,
 * j)
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
 * 1. Can we do this in O(n * k) where k = average word length? (Trie-based
 * optimization possible)
 * Leetcode follow-up ref:
 * https://leetcode.com/problems/palindrome-pairs/solutions/79215/o-n-k-2-java-solution-with-trie-structure/
 * 2. How to handle duplicates in the list? (Not required here as words are
 * unique)
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class PalindromePairs {

    public static void main(String[] args) {
        String[] words = { "abcd", "dcba", "lls", "s", "sssll" };
        System.out.println(new PalindromePairs().findPalindromePairs(words));
        System.out.println(new PalindromePairs().findPalindromePairsUsingTrie(words));
    }

    /**
     * TrieNode represents a node in the Trie data structure.
     * Each node can store:
     * - Children nodes (one for each character)
     * - Word index if a word ends at this node
     * - List of word indices where remaining suffix forms a palindrome
     */
    private static class TrieNode {
        Map<Character, TrieNode> children;
        int wordEndIndex; // Index of word that ends at this node (-1 if no word ends here)
        List<Integer> palindromeSuffixWordIndices; // Indices of words where remaining part is palindrome

        TrieNode() {
            children = new HashMap<>();
            wordEndIndex = -1;
            palindromeSuffixWordIndices = new ArrayList<>();
        }
    }

    /**
     * Trie-based solution for finding palindrome pairs.
     * 
     * Key Insight:
     * - Build a Trie with REVERSED words
     * - For each word, traverse the Trie character by character
     * - At each step, check if we can form a palindrome pair
     *
     * Three cases to consider:
     * 1. Word1 length == Word2 length: Complete reverse match
     * 2. Word1 shorter: Remaining part of Word2 (in Trie) must be palindrome
     * 3. Word1 longer: Remaining part of Word1 must be palindrome
     *
     * Time Complexity: O(n * k^2) where n = number of words, k = average word
     * length
     * - Building Trie: O(n * k^2) due to palindrome checks
     * - Searching: O(n * k^2) due to palindrome checks
     * Space Complexity: O(n * k) for Trie structure
     *
     * Advantages over HashMap approach:
     * - Cleaner logic and more intuitive
     * - Better cache locality
     * - Easier to extend for prefix/suffix patterns
     */
    public List<List<Integer>> findPalindromePairsUsingTrie(String[] words) {
        List<List<Integer>> palindromePairs = new ArrayList<>();
        TrieNode root = buildTrieWithReversedWords(words);

        // Search for palindrome pairs for each word
        for (int currentWordIndex = 0; currentWordIndex < words.length; currentWordIndex++) {
            searchPalindromePairs(words[currentWordIndex], currentWordIndex, root, palindromePairs);
        }

        return palindromePairs;
    }

    /**
     * Builds a Trie with all words inserted in REVERSED order.
     * While building, tracks indices where remaining suffix forms a palindrome.
     *
     * @param words Array of words to insert
     * @return Root of the constructed Trie
     */
    private TrieNode buildTrieWithReversedWords(String[] words) {
        TrieNode root = new TrieNode();

        for (int wordIndex = 0; wordIndex < words.length; wordIndex++) {
            String word = words[wordIndex];
            TrieNode currentNode = root;

            // Insert word in reverse order
            for (int charIndex = word.length() - 1; charIndex >= 0; charIndex--) {
                char currentChar = word.charAt(charIndex);

                // If remaining prefix (before charIndex) is a palindrome, record this word's
                // index
                if (isPalindrome(word, 0, charIndex)) {
                    currentNode.palindromeSuffixWordIndices.add(wordIndex);
                }

                // Create new node if path doesn't exist
                currentNode.children.putIfAbsent(currentChar, new TrieNode());
                currentNode = currentNode.children.get(currentChar);
            }

            // Mark end of word and store its index
            currentNode.wordEndIndex = wordIndex;
            // Empty string is always a palindrome - add at word end
            currentNode.palindromeSuffixWordIndices.add(wordIndex);
        }

        return root;
    }

    /**
     * Searches for all palindrome pairs involving the given word.
     * Traverses the Trie while checking three cases for palindrome formation.
     *
     * @param word      Current word to find pairs for
     * @param wordIndex Index of current word
     * @param root      Root of the Trie
     * @param result    List to store found palindrome pairs
     */
    private void searchPalindromePairs(String word, int wordIndex, TrieNode root,
            List<List<Integer>> result) {
        TrieNode currentNode = root;

        // Traverse Trie with current word
        for (int charIndex = 0; charIndex < word.length(); charIndex++) {
            // Case 1: Found a complete word in Trie AND remaining part of current word is
            // palindrome
            // Example: word="lls", Trie has "s" (reversed), remaining "ll" is palindrome
            if (currentNode.wordEndIndex != -1 &&
                    currentNode.wordEndIndex != wordIndex &&
                    isPalindrome(word, charIndex, word.length() - 1)) {
                result.add(Arrays.asList(wordIndex, currentNode.wordEndIndex));
            }

            // Move to next character in Trie
            char currentChar = word.charAt(charIndex);
            if (!currentNode.children.containsKey(currentChar)) {
                return; // No valid pairs possible
            }
            currentNode = currentNode.children.get(currentChar);
        }

        // Case 2: Traversed entire word, check all words in Trie where remaining part
        // is palindrome
        // Example: word="s", Trie has "sssll" (reversed "llsss"), "sss" part is
        // palindrome
        for (int pairedWordIndex : currentNode.palindromeSuffixWordIndices) {
            if (pairedWordIndex != wordIndex) {
                result.add(Arrays.asList(wordIndex, pairedWordIndex));
            }
        }
    }

    /**
     * Problem: Given a list of unique words, find all pairs of distinct indices (i,
     * j)
     * such that the concatenation of words[i] + words[j] forms a palindrome.
     *
     * Approach:
     * 1. Store words in a HashMap with their corresponding indices for O(1)
     * lookups.
     * 2. Check for three cases:
     * - Direct reverse match (word1 == reverse(word2)).
     * - Palindromic prefix with a corresponding reversed suffix.
     * - Palindromic suffix with a corresponding reversed prefix.
     *
     * Complexity:
     * - Time complexity : O(n * k^2) where n is number of words and k is average
     * length of words.
     * - because for each word we may check all prefixes and suffixes (k) and
     * palindrome check (k).
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
            for (String suffix : getSuffixesPostPalindrome(word)) {
                String reversedSuffix = new StringBuilder(suffix).reverse().toString();
                if (wordIndexMap.containsKey(reversedSuffix)) {
                    palindromePairs.add(Arrays.asList(wordIndexMap.get(reversedSuffix), i));
                }
            }

            // Case 3: Check valid palindromic suffixes
            for (String prefix : getPrefixesBeforePalindrome(word)) {
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
    private List<String> getPrefixesBeforePalindrome(String word) {
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
    private List<String> getSuffixesPostPalindrome(String word) {
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
     * @param word  - Input string
     * @param left  - Start index
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

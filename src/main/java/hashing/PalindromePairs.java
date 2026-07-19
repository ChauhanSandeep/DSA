package hashing;

import java.util.*;

/**
 * Problem: Palindrome Pairs
 *
 * Given a list of unique words, find all ordered index pairs (i, j) where
 * i != j and words[i] + words[j] is a palindrome. Empty strings and words with
 * palindromic prefixes or suffixes need special care.
 *
 * Leetcode: https://leetcode.com/problems/palindrome-pairs/ (Hard)
 * Rating:   not available (not a contest problem)
 * Pattern:  Hashing | Trie | Palindromic prefix/suffix splits
 *
 * Example:
 *   Input:  ["abcd", "dcba", "lls", "s", "sssll"]
 *   Output: [[0,1], [1,0], [3,2], [2,4]]
 *   Why:    each listed ordered pair concatenates to a palindrome, such as
 *           "abcd" + "dcba" = "abcddcba".
 *
 * Follow-ups:
 *   1. How can the repeated reverse lookups be optimized?
 *      Insert reversed words into a trie and carry palindrome-suffix indices at each node.
 *   2. How would duplicates in words change the result?
 *      Map each word to all of its indices instead of a single index.
 *   3. How would you reduce repeated palindrome checks?
 *      Precompute palindromic prefixes and suffixes for each word.
 *   4. How do empty strings affect the answer?
 *      The empty string pairs with every non-empty word that is already a palindrome.
 *
 * Related: Longest Palindromic Substring (5), Palindrome Partitioning (131).
 */
public class PalindromePairs {

    public static void main(String[] args) {
        PalindromePairs solver = new PalindromePairs();
        String[][] cases = {
            { "abcd", "dcba", "lls", "s", "sssll" },
            { "a", "" }
        };
        String[] expected = {
            "[[0, 1], [1, 0], [2, 4], [3, 2]]",
            "[[0, 1], [1, 0]]"
        };

        for (int i = 0; i < cases.length; i++) {
            List<List<Integer>> got = solver.findPalindromePairs(cases[i]);
            got.sort(Comparator.<List<Integer>>comparingInt(pair -> pair.get(0))
                .thenComparingInt(pair -> pair.get(1)));
            System.out.printf("words=%s method=hash -> %s  expected=%s%n",
                Arrays.toString(cases[i]), got, expected[i]);

            List<List<Integer>> gotTrie = solver.findPalindromePairsUsingTrie(cases[i]);
            gotTrie.sort(Comparator.<List<Integer>>comparingInt(pair -> pair.get(0))
                .thenComparingInt(pair -> pair.get(1)));
            System.out.printf("words=%s method=trie -> %s  expected=%s%n",
                Arrays.toString(cases[i]), gotTrie, expected[i]);
        }
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
     * Intuition: a word can form a palindrome with another word when the unmatched
     * part left after a reverse-prefix match is itself a palindrome. A trie of
     * reversed words makes those reverse-prefix matches explicit while storing
     * which remaining prefixes are palindromic.
     *
     * Algorithm:
     *   1. Insert every word into a trie in reverse order, recording palindrome-prefix indices along the path.
     *   2. For each original word, walk the trie and add pairs when the rest of the word is palindromic.
     *   3. After the word is consumed, pair it with trie words whose remaining part is palindromic.
     *
     * Time:  O(n * k^2) - each of n words may run palindrome checks across k split positions.
     * Space: O(n * k) - the trie stores characters from all words plus index lists.
     *
     * @param words unique input words
     * @return ordered index pairs whose concatenation is a palindrome
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
     * Intuition: split each word into a prefix and suffix. If one side is already
     * a palindrome, the reverse of the other side can be attached on the opposite
     * side to make the whole concatenation a palindrome.
     *
     * Algorithm:
     *   1. Map every word to its index for constant-time reverse lookups.
     *   2. Add direct reverse matches for whole words.
     *   3. Add matches from palindromic prefixes and palindromic suffixes.
     *
     * Time:  O(n * k^2) - each word has k splits and palindrome/reverse work can cost k.
     * Space: O(n * k) - the map and generated split strings store word content.
     *
     * @param words unique input words
     * @return ordered index pairs whose concatenation is a palindrome
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

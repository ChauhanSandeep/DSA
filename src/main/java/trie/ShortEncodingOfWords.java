package trie;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Problem: Short Encoding of Words
 *
 * Given lowercase words, return the length of the shortest reference string
 * ending in '#' markers that can recover every word. A word does not need its
 * own copy when it is already a suffix of a longer encoded word.
 *
 * Leetcode: https://leetcode.com/problems/short-encoding-of-words/ (Medium)
 * Rating:   1632 (zerotrac Elo)
 * Pattern:  Trie | Suffix elimination | Reversed suffix trie
 *
 * Example:
 *   Input:  words = ["time", "me", "bell"]
 *   Output: 10
 *   Why:    "me" is a suffix of "time", so "time#bell#" already contains all three words.
 *
 * Follow-ups:
 *   1. Return the actual reference string and indices?
 *      Keep only non-suffix words, concatenate them with '#', and record starting offsets.
 *   2. Support a larger character set than lowercase English?
 *      Use map-based trie children instead of a fixed 26-slot array.
 *   3. Stream many words with duplicates under memory pressure?
 *      Deduplicate first, then process longer words before shorter suffix candidates.
 *   4. Support removing words from the encoding?
 *      Keep terminal counts and recompute affected suffix paths, because relationships can change.
 *
 * Related: Implement Trie (Prefix Tree) (208), Replace Words (648).
 */
public class ShortEncodingOfWords {

    /**
     * Intuition: only words that are not suffixes of longer words need their own
     * word + '#' entry. Keep every distinct word as a candidate, then for each
     * word remove all proper suffixes it already covers. Anything left in the set
     * cannot be recovered from another encoded word and must be counted directly.
     *
     * Algorithm:
     *   1. Put all words in a set to deduplicate candidates.
     *   2. Sort words from longest to shortest so longer words remove covered suffixes.
     *   3. For each word, remove each proper suffix from the set.
     *   4. Sum length + 1 for every remaining word to include the '#'.
     *
     * Time:  O(N log N + N*L^2) - sorting plus making and removing up to L suffix strings per word.
     * Space: O(N*L) - the set stores distinct input words.
     *
     * @param words array of words to encode
     * @return minimum length of the encoded reference string
     */
    public int minimumLengthEncoding(String[] words) {
        Set<String> wordSet = new HashSet<>(Arrays.asList(words));

        // Sort words by length in descending order
        Arrays.sort(words, (a, b) -> Integer.compare(b.length(), a.length()));

        // Remove words from the set which will be encoded by their longer words
        for (String word : words) {
            // Remove all suffixes of the current word
            for (int i = 1; i < word.length(); i++) {
                String suffix = word.substring(i);
                wordSet.remove(suffix);
            }
        }

        // Calculate total length of remaining words + '#' for each
        int totalLength = 0;
        for (String word : wordSet) {
            totalLength += word.length() + 1; // +1 for '#'
        }
        return totalLength;
    }

    /**
     * Finds minimum encoding length using Trie with reversed words.
     * 
     * Key Insight: If word A is suffix of word B, we only need B in encoding.
     * Reverse words so suffixes become prefixes, then find non-prefix words.
     *
     * Algorithm:
     * 1. Build Trie with reversed words (suffixes become prefixes)
     * 2. DFS to find leaf nodes (words not suffix of any other)
     * 3. Sum: length of each leaf word + 1 (for '#')
     *
     * Example: ["time", "me", "bell"]
     * - Reversed: ["emit", "em", "lleb"]
     * - Trie: root → e → m → i → t (leaf, length 4)
     *         root → l → l → e → b (leaf, length 4)
     * - "em" is prefix of "emit" (not leaf), so "me" is suffix of "time"
     * - Result: (4+1) + (4+1) = 10
     *
     * Time: O(n * L) where n = number of words, L = average word length
     * Space: O(n * L) for Trie
     */
    public int minimumLengthEncodingUsingTrie(String[] words) {
        TrieNode root = new TrieNode();
        
        // Build Trie with reversed words
        for (String word : words) {
            insertReversed(root, word);
        }
        
        // DFS to find total encoding length (only leaf nodes count)
        return dfs(root, 0);
    }

    /** Inserts a word from last character to first so suffixes share trie paths. */
    private void insertReversed(TrieNode root, String word) {
        TrieNode current = root;
        // Insert word in reverse (so suffixes become prefixes)
        for (int i = word.length() - 1; i >= 0; i--) {
            char ch = word.charAt(i);
            if (current.children[ch - 'a'] == null) {
                current.children[ch - 'a'] = new TrieNode();
            }
            current = current.children[ch - 'a'];
        }
    }

    /** Returns the encoded length contributed by leaf paths below this node. */
    private int dfs(TrieNode node, int depth) {
        int totalLength = 0;
        
        // Recursively sum lengths from all children
        for (TrieNode child : node.children) {
            if (child != null) {
                totalLength += dfs(child, depth + 1);
            }
        }
        
        // If this is a leaf node (no children) and not root, count it
        // Leaf means this word isn't a suffix of any other word
        if (totalLength == 0 && depth > 0) {
            return depth + 1; // word length + '#' delimiter
        }
        
        return totalLength;
    }

    /** Trie node with one child slot per lowercase letter. */
    private static class TrieNode {
        TrieNode[] children = new TrieNode[26];
    }

    public static void main(String[] args) {
        ShortEncodingOfWords solver = new ShortEncodingOfWords();

        String[][] inputs = {
            {"time", "me", "bell"},
            {"t"},
            {"me", "time", "time"}
        };
        int[] expected = {10, 2, 5};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.minimumLengthEncoding(inputs[i].clone());
            System.out.printf("words=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

}

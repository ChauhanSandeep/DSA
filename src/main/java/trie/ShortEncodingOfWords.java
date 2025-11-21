package trie;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Problem: Short Encoding of Words
 *
 * A valid encoding of an array of words is any reference string s and array of indices
 * such that words.length == indices.length, the reference string s ends with '#', and for
 * each index indices[i], the substring of s starting from indices[i] up to (but not including)
 * the next '#' character equals words[i].
 *
 * Given an array of words, return the length of the shortest reference string s possible of
 * any valid encoding of words.
 *
 * Example:
 * Input: words = ["time", "me", "bell"]
 * Output: 10
 * Explanation: A valid encoding is s = "time#bell#" and indices = [0, 2, 5].
 * words[0] = "time" starts at index 0, words[1] = "me" starts at index 2 (suffix of "time"),
 * words[2] = "bell" starts at index 5. Since "me" is a suffix of "time", we don't need
 * separate encoding for it. Total length = 5 ("time#") + 5 ("bell#") = 10.
 *
 * Constraints:
 * - 1 <= words.length <= 2000
 * - 1 <= words[i].length <= 7
 * - words[i] consists of only lowercase letters
 *
 * LeetCode Problem: https://leetcode.com/problems/short-encoding-of-words
 *
 * Follow-up Questions:
 *
 * 1. How would you handle duplicate words in the input array?
 *    Answer: The Set-based approach naturally handles duplicates by removing them during
 *    suffix elimination. The Trie approach would insert the same word multiple times but
 *    only count its length once in the final encoding.
 *
 * 2. What if words could contain uppercase letters or special characters?
 *    Answer: The current solution works for any character set. For Trie approach, we'd need
 *    to adjust the children array size or use a HashMap instead of array for more flexibility.
 *
 * 3. Can you extend this to return the actual encoded string and indices array?
 *    Answer: Yes, we'd modify the Trie DFS to build the actual string by concatenating
 *    characters along paths to leaf nodes. For indices, we'd track the current position
 *    and record it when we complete each word.
 *
 * 4. How would you optimize for very large word arrays with memory constraints?
 *    Answer: We could process words in batches, or use a more memory-efficient Trie
 *    implementation with HashMap children instead of array. Alternatively, sort by length
 *    and process longer words first to identify suffixes early.
 *    Related problem: https://leetcode.com/problems/implement-trie-prefix-tree/
 *
 * 5. What if we need to support prefix matching instead of suffix matching?
 *    Answer: We would use a standard Trie without reversing words. Words that are prefixes
 *    of others would be eliminated instead of suffixes.
 *    Related problem: https://leetcode.com/problems/replace-words/
 */
public class ShortEncodingOfWords {

    /**
     * Finds minimum encoding length using Set-based suffix elimination approach.
     *
     * Algorithm:
     * 1. Sort words by length in descending order to process longer words first
     * 2. Add all words to a Set for efficient lookup and removal
     * 3. For each word, remove all its suffixes from the Set
     * 4. Sum the length of remaining words (each +1 for '#' delimiter)
     *
     * Key insight: If word A is a suffix of word B, we only need to encode B.
     * By processing longer words first and removing their suffixes, we ensure
     * only necessary words remain for encoding.
     *
     * Time Complexity: O(N log N + N*L²) where N is number of words and L is average word length.
     * - O(N log N) for sorting words by length
     * - O(N * L²) for suffix removal (each word of length L has up to L suffixes, each requiring O(L) substring operations)
     *
     * Space Complexity: O(N * L) for storing all words in the Set.
     *
     * @param words array of words to encode
     * @return minimum length of encoded reference string
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
     * Finds minimum encoding length using Trie data structure with reversed words.
     *
     * Algorithm:
     * 1. Build a Trie by inserting all words in reverse order.
     *    In reverse order because suffixes so that prefixes which are easier to manage in a Trie.
     * 2. Words that are suffixes become prefixes in reversed form
     * 3. Perform DFS to find all leaf nodes in the Trie
     * 4. Only leaf nodes represent words needing separate encoding
     * 5. Sum depths of all leaf nodes (each +1 for '#' delimiter)
     *
     * Key insight: Reversing words transforms suffix relationship into prefix relationship.
     * Leaf nodes in the reversed Trie represent words that aren't suffixes of any other word.
     *
     * Time Complexity: O(N * L) where N is number of words and L is average word length.
     * Each word is inserted once (O(L) per word), and DFS visits each node once.
     *
     * Space Complexity: O(N * L) for Trie structure storing all unique prefixes.
     *
     * @param words array of words to encode
     * @return minimum length of encoded reference string
     */
    public int minimumLengthEncodingTrie(String[] words) {
        TrieNode root = new TrieNode();

        for (String word : words) {
            TrieNode node = root;
            for (int i = word.length() - 1; i >= 0; i--) {
                char c = word.charAt(i);
                if (node.children[c - 'a'] == null) {
                    node.children[c - 'a'] = new TrieNode();
                }
                node = node.children[c - 'a'];
            }
        }

        return dfs(root, 0);
    }

    // Helper method to calculate total encoding length via DFS
    private int dfs(TrieNode node, int depth) {
        if (node == null) {
            return 0;
        }

        boolean isLeaf = true;
        int length = 0;

        for (TrieNode child : node.children) {
            if (child != null) {
                isLeaf = false;
                length += dfs(child, depth + 1);
            }
        }

        return isLeaf ? depth + 1 : length;
    }

    // Trie node with children array for lowercase letters
    private static class TrieNode {
        TrieNode[] children = new TrieNode[26];
    }
}

package strings;

import java.util.*;

/**
 * LeetCode 820. Short Encoding of Words
 *
 * A valid encoding of an array of words is any reference string s and array of indices indices such that:
 * words[i] = s.substring(indices[i], indices[i] + words[i].length())
 * We want to find the encoding with the minimum length.
 *
 * Example 1:
 * Input: words = ["time", "me", "bell"]
 * Output: 10
 * Explanation: A valid encoding would be s = "time#bell#" and indices = [0, 2, 5].
 *
 * LeetCode Link: https://leetcode.com/problems/short-encoding-of-words/
 *
 * Follow-up Questions:
 * - How would you handle very large word lists efficiently? (Use trie with suffix optimization)
 * - Can you construct the actual encoded string? (Build string while calculating length)
 * - How would you optimize for words with common prefixes vs suffixes? (Different trie structures)
 * - What if we need multiple valid encodings? (Track all possible trie paths)
 */
public class ShortEncodingOfWords {

    /**
     * Finds minimum length of encoding string using suffix elimination approach.
     *
     * Algorithm:
     * 1. Add all words to a set for efficient lookup and removal
     * 2. For each word, check all its suffixes
     * 3. Remove any suffix that exists in the set (it's redundant)
     * 4. Remaining words in set are the ones that need their own encoding
     * 5. Sum up lengths of remaining words plus separators
     *
     * Time Complexity: O(sum of word_length^2) for suffix checking
     * Space Complexity: O(sum of word_lengths) for storing words in set
     *
     * @param words Array of words to encode
     * @return Minimum length of encoded string
     */
    public int minimumLengthEncoding(String[] words) {
        if (words == null || words.length == 0) {
            return 0;
        }

        Set<String> wordSet = new HashSet<>(Arrays.asList(words));

        // Remove all suffixes that exist as separate words
        for (String word : words) {
            for (int i = 1; i < word.length(); i++) {
                String suffix = word.substring(i);
                wordSet.remove(suffix);
            }
        }

        // Calculate total length: sum of remaining words + separators
        int totalLength = 0;
        for (String word : wordSet) {
            totalLength += word.length() + 1; // +1 for separator '#'
        }

        return totalLength;
    }

    /**
     * Trie-based approach for more efficient processing of large word lists.
     */
    public int minimumLengthEncodingTrie(String[] words) {
        TrieNode root = new TrieNode();

        // Build trie with reversed words (suffixes become prefixes)
        Map<TrieNode, String> nodeToWord = new HashMap<>();

        for (String word : words) {
            TrieNode current = root;

            // Insert word in reverse order
            for (int i = word.length() - 1; i >= 0; i--) {
                char c = word.charAt(i);
                if (!current.children.containsKey(c)) {
                    current.children.put(c, new TrieNode());
                }
                current = current.children.get(c);
            }

            nodeToWord.put(current, word);
        }

        // Calculate encoding length by finding leaf nodes
        int totalLength = 0;
        for (TrieNode node : nodeToWord.keySet()) {
            if (node.children.isEmpty()) { // Leaf node
                totalLength += nodeToWord.get(node).length() + 1;
            }
        }

        return totalLength;
    }

    /**
     * Alternative approach using sorting by length for optimization.
     */
    public int minimumLengthEncodingSorted(String[] words) {
        // Sort words by length in descending order
        Arrays.sort(words, (a, b) -> b.length() - a.length());

        List<String> encoding = new ArrayList<>();

        for (String word : words) {
            boolean isSuffix = false;

            // Check if current word is suffix of any encoded word
            for (String encoded : encoding) {
                if (encoded.endsWith(word)) {
                    isSuffix = true;
                    break;
                }
            }

            if (!isSuffix) {
                encoding.add(word);
            }
        }

        // Calculate total length
        int totalLength = 0;
        for (String word : encoding) {
            totalLength += word.length() + 1;
        }

        return totalLength;
    }

    // Helper class for trie implementation
    private static class TrieNode {
        Map<Character, TrieNode> children;

        TrieNode() {
            children = new HashMap<>();
        }
    }
}

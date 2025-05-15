package Tree;

import java.util.Arrays;

/**
 * Given a string representation of a dictionary and an array of hotel reviews,
 * this class ranks the reviews based on how many words from the dictionary they contain.
 * Reviews with more matching dictionary words appear first in the result.
 * Link: https://www.interviewbit.com/problems/hotel-reviews/
 *
 * Intuition:
 * - We use a Trie data structure to efficiently store the dictionary words.
 * - For each review, we traverse its words and count how many words exist in the dictionary.
 * - The reviews are then ranked based on the count of matched words in descending order.
 *
 * Algorithm:
 * 1. Build a Trie from the dictionary string.
 * 2. For each review, count how many dictionary words are present by splitting the review based on underscores ('_').
 * 3. Sort the reviews based on the count of matches in descending order.
 * 4. If two reviews have the same count, their original index determines the order.
 *
 * Time Complexity:
 * - Building the Trie: O(n), where n is the length of the dictionary string.
 * - Counting dictionary words in each review: O(m), where m is the average length of the reviews.
 * - Sorting reviews: O(k log k), where k is the number of reviews.
 *
 * Space Complexity:
 * - The space complexity is O(n + k), where n is the number of characters in the dictionary string and
 *   k is the number of reviews stored.
 */
public class HotelReviews {

    /**
     * Solves the problem of ranking hotel reviews based on dictionary word matches.
     *
     * @param dictStr The string representation of the dictionary (words are separated by underscores).
     * @param arr The array of hotel reviews to be ranked.
     * @return An array of indices representing the reviews in the ranked order.
     */
    public int[] solve(String dictStr, String[] arr) {
        // Step 1: Build the Trie from the dictionary string
        TrieNode root = buildTrie(dictStr);

        // Step 2: Count how many dictionary words each review contains
        Pair[] reviewPairs = new Pair[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reviewPairs[i] = new Pair(i, countDictionaryMatches(arr[i], root));
        }

        // Step 3: Sort reviews based on the count of dictionary words (in descending order)
        Arrays.sort(reviewPairs, (pair1, pair2) -> {
            if (pair2.count == pair1.count) return pair1.index - pair2.index;
            return pair2.count - pair1.count;
        });

        // Step 4: Return the sorted indices
        int[] sortedIndices = new int[reviewPairs.length];
        for (int i = 0; i < reviewPairs.length; i++) {
            sortedIndices[i] = reviewPairs[i].index;
        }

        return sortedIndices;
    }

    /**
     * Builds a Trie from a given dictionary string.
     * The dictionary string contains words separated by underscores ('_').
     *
     * @param dictStr The string containing the dictionary words.
     * @return The root of the Trie that stores the dictionary words.
     */
    private TrieNode buildTrie(String dictStr) {
        TrieNode root = new TrieNode();
        TrieNode currentNode = root;

        // Split dictionary string into words and insert them into the Trie
        for (int i = 0; i < dictStr.length(); i++) {
            char c = dictStr.charAt(i);
            if (c == '_') {
                currentNode.isWord = true;
                currentNode = root; // Reset to the root for the next word
            } else {
                int index = c - 'a';
                if (currentNode.children[index] == null) {
                    currentNode.children[index] = new TrieNode();
                }
                currentNode = currentNode.children[index];
            }
        }
        currentNode.isWord = true; // Mark the last word as valid
        return root;
    }

    /**
     * Counts how many dictionary words are present in a given review string.
     * The review string contains words separated by underscores ('_').
     *
     * @param review The review string to be checked.
     * @param root The root of the Trie that contains the dictionary words.
     * @return The count of matching dictionary words in the review.
     */
    private int countDictionaryMatches(String review, TrieNode root) {
        TrieNode currentNode = root;
        int matchCount = 0;
        boolean isValidWord = true;

        // Traverse the review and check for dictionary word matches
        for (int i = 0; i < review.length(); i++) {
            char c = review.charAt(i);

            if (c == '_') {
                if (currentNode.isWord && isValidWord) {
                    matchCount++; // Increment the count if a valid dictionary word is found
                }
                currentNode = root; // Reset to the root for the next word
                isValidWord = true; // Reset flag for the next word
            } else if (isValidWord) {
                int index = c - 'a';
                if (currentNode.children[index] == null) {
                    isValidWord = false; // Mark as invalid if the character is not in the Trie
                } else {
                    currentNode = currentNode.children[index];
                }
            }
        }

        // Final check if the last part of the review is a valid word
        if (currentNode.isWord && isValidWord) {
            matchCount++;
        }

        return matchCount;
    }

    /**
     * Helper class to store index and match count for each review.
     */
    private static class Pair {
        int index;
        int count;

        Pair(int index, int count) {
            this.index = index;
            this.count = count;
        }
    }

    /**
     * TrieNode class to represent each node in the Trie.
     */
    private static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        boolean isWord;

        TrieNode() {
            isWord = false;
        }
    }
}

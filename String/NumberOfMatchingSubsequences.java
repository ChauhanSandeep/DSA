package String;

import java.util.*;

/**
 * LeetCode 792. Number of Matching Subsequences
 *
 * Given a string s and an array of strings words, return the number of words[i]
 * that is a subsequence of s. A subsequence of a string is a new string generated
 * from the original string with some characters deleted without changing the relative order.
 *
 * Example 1:
 * Input: s = "abcde", words = ["a","bb","acd","ace"]
 * Output: 3
 * Explanation: There are three strings in words that are a subsequence of s: "a", "acd", "ace".
 *
 * LeetCode Link: https://leetcode.com/problems/number-of-matching-subsequences/
 *
 * Follow-up Questions:
 * - How would you optimize for very long string s with short words? (Pre-compute character positions)
 * - Can you handle case where words array is very large? (Use trie structure for word grouping)
 * - How would you modify to return actual matching words instead of count? (Store words in result list)
 * - What if we need to find longest matching subsequence? (Track and compare lengths during matching)
 */
public class NumberOfMatchingSubsequences {

    /**
     * Counts words that are subsequences of string s using waiting lists approach.
     *
     * Algorithm:
     * 1. Create 26 waiting lists (buckets) for each letter a-z
     * 2. Initially place all words in bucket corresponding to their first character
     * 3. For each character in s, process all words waiting for that character
     * 4. Move each word to next waiting bucket or mark as complete
     * 5. Count completed words as valid subsequences
     *
     * Time Complexity: O(s.length() + sum of all word lengths)
     * Space Complexity: O(number of words) for the waiting lists
     *
     * @param s Target string to check subsequences against
     * @param words Array of words to check if they are subsequences of s
     * @return Number of words that are subsequences of s
     */
    public int numMatchingSubseq(String s, String[] words) {
        if (s == null || words == null) {
            return 0;
        }

        // Create waiting lists for each character a-z
        List<WordPointer>[] waitingLists = new List[26];
        for (int i = 0; i < 26; i++) {
            waitingLists[i] = new ArrayList<>();
        }

        // Initialize waiting lists with all words
        for (String word : words) {
            if (word.length() > 0) {
                char firstChar = word.charAt(0);
                waitingLists[firstChar - 'a'].add(new WordPointer(word, 0));
            }
        }

        int matchCount = 0;

        // Process each character in s
        for (char c : s.toCharArray()) {
            int charIndex = c - 'a';
            List<WordPointer> currentWaitingList = waitingLists[charIndex];
            waitingLists[charIndex] = new ArrayList<>(); // Reset current waiting list

            // Process all words waiting for current character
            for (WordPointer wp : currentWaitingList) {
                wp.index++; // Move to next character in word

                if (wp.index == wp.word.length()) {
                    // Word is completely matched
                    matchCount++;
                } else {
                    // Move word to waiting list for its next character
                    char nextChar = wp.word.charAt(wp.index);
                    waitingLists[nextChar - 'a'].add(wp);
                }
            }
        }

        return matchCount;
    }

    /**
     * Alternative approach using binary search on pre-computed character positions.
     */
    public int numMatchingSubseqBinarySearch(String s, String[] words) {
        // Pre-compute positions of each character in s
        Map<Character, List<Integer>> charPositions = new HashMap<>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            charPositions.computeIfAbsent(c, k -> new ArrayList<>()).add(i);
        }

        int matchCount = 0;

        for (String word : words) {
            if (isSubsequenceUsingBinarySearch(word, charPositions)) {
                matchCount++;
            }
        }

        return matchCount;
    }

    // Helper class to track word and current index being matched
    private static class WordPointer {
        String word;
        int index;

        WordPointer(String word, int index) {
            this.word = word;
            this.index = index;
        }
    }

    // Helper method to check subsequence using binary search
    private boolean isSubsequenceUsingBinarySearch(String word, Map<Character, List<Integer>> charPositions) {
        int currentPosition = -1;

        for (char c : word.toCharArray()) {
            if (!charPositions.containsKey(c)) {
                return false;
            }

            List<Integer> positions = charPositions.get(c);
            int nextPos = binarySearchNextPosition(positions, currentPosition);

            if (nextPos == -1) {
                return false;
            }

            currentPosition = nextPos;
        }

        return true;
    }

    // Binary search to find next position greater than current
    private int binarySearchNextPosition(List<Integer> positions, int currentPos) {
        int left = 0, right = positions.size() - 1;
        int result = -1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (positions.get(mid) > currentPos) {
                result = positions.get(mid);
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return result;
    }
}

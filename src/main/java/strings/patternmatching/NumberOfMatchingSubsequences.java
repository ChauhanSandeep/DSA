package strings.patternmatching;

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
     * Counts how many words from the given list are subsequences of the input text.
     *
     * Algorithm:
     * - Maintain 26 buckets (a–z), each storing words currently waiting for that character.
     * - Place every word into the bucket of its first required character.
     * - Iterate over each character of the target:
     *     - Take all words from that character’s bucket (they are waiting for it).
     *     - Remove the matched character from each word:
     *         - If no characters remain → the word is fully matched (count++).
     *         - Otherwise → move the word to the bucket of its next required character.
     *
     * Dry Run:
     * text = "abcde", words = ["a", "bb", "acd", "ace"]
     *
     * Initial buckets:
     *   'a': ["a", "acd", "ace"]
     *   'b': ["bb"]
     *
     * Step 1: process 'a'
     *   - "a" → fully matched ✅
     *   - "acd" → now "cd" → move to 'c'
     *   - "ace" → now "ce" → move to 'c'
     *
     * Step 2: process 'b'
     *   - "bb" → now "b" → move to 'b'
     *
     * Step 3: process 'c'
     *   - "cd" → now "d" → move to 'd'
     *   - "ce" → now "e" → move to 'e'
     *
     * Step 4: process 'd'
     *   - "d" → fully matched ✅
     *
     * Step 5: process 'e'
     *   - "e" → fully matched ✅
     *
     * Final count = 3 ("a", "acd", "ace")
     *
     * Complexity:
     * Each word progresses one character at a time → O(|target| + total word length).
     * @param target Target string to check subsequences against
     * @param words Array of words to check if they are subsequences of target
     * @return Number of words that are subsequences of target
     */
    public int numMatchingSubseq(String target, String[] words) {
        // Buckets for words waiting on each character ('a' to 'z')
        List<StringBuilder>[] waitingBuckets = new List[26];
        for (int i = 0; i < 26; i++) {
            waitingBuckets[i] = new ArrayList<>();
        }

        // Place each word in the bucket of the first character it is waiting for
        for (String word : words) {
            char firstChar = word.charAt(0);
            waitingBuckets[firstChar - 'a'].add(new StringBuilder(word));
        }

        int matchingSubsequences = 0;

        // Process each character of the main string
        for (char currentChar : target.toCharArray()) {
            List<StringBuilder> waitingList = waitingBuckets[currentChar - 'a'];
            // Reset this bucket because we'll redistribute its words
            waitingBuckets[currentChar - 'a'] = new ArrayList<>();

            // Update all words waiting for this character
            for (StringBuilder currentWord : waitingList) {
                currentWord.deleteCharAt(0); // remove matched char

                if (currentWord.length() == 0) {
                    // Word completely matched
                    matchingSubsequences++;
                } else {
                    // Move word to the bucket of its next required character
                    char nextRequiredChar = currentWord.charAt(0);
                    waitingBuckets[nextRequiredChar - 'a'].add(currentWord);
                }
            }
        }

        return matchingSubsequences;
    }

    /**
     * Alternative approach using binary search on pre-computed character positions.
     *
     * Algorithm:
     * 1. Pre-compute positions of each character in target string
     * 2. For each word, use binary search to find if it is a subse
     * quence of target
     * 3. Maintain the last matched index to ensure order is preserved
     * 4. Count words that are confirmed as subsequences
     *
     * Time Complexity: O(target.length() + m * log n) where m is number of words and n is average word length
     * Space Complexity: O(n) for storing character positions
     */
    public int numMatchingSubseqBinarySearch(String target, String[] words) {
        // Pre-compute positions of each character in s
        Map<Character, List<Integer>> charPositions = new HashMap<>();

        for (int i = 0; i < target.length(); i++) {
            char c = target.charAt(i);
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

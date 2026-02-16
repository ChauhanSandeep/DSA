package strings.greedy;

import java.util.PriorityQueue;

/**
 * Given a string s, rearrange the characters of s so that any two adjacent characters are not the same.
 * Return any possible rearrangement of s or return an empty string if not possible.
 *
 * Example 1:
 * Input: s = "aab"
 * Output: "aba"
 *
 * Example 2:
 * Input: s = "aaab"
 * Output: ""
 *
 * LeetCode: https://leetcode.com/problems/reorganize-string/
 *
 * Follow-up Questions:
 * 1. How would you handle the case where we need to ensure that no two identical characters are at least k distance apart?
 *    - We could modify the solution to maintain a queue of recently used characters and their availability times.
 * 2. What if the string contains Unicode characters?
 *    - The solution would work the same way since we're working with character frequencies.
 * 3. How would you find all possible valid rearrangements instead of just one?
 *    - We would need to use backtracking to explore all possible valid orderings.
 *
 * Related Problems:
 * - Task Scheduler (https://leetcode.com/problems/task-scheduler/)
 * - Rearrange String k Distance Apart (https://leetcode.com/problems/rearrange-string-k-distance-apart/)
 * LeetCode Contest Rating: 1681
 */
public class ReorganizeString {
    /**
     * Optimal solution using a max-heap (priority queue) and greedy strategy.
     *
     * Steps:
     * 1. Count the frequency of each character in s using an array of size 26.
     * 2. Compute the maximum frequency; if it exceeds (n + 1) / 2, immediately return "" because
     *    you cannot arrange characters to avoid adjacent duplicates (pigeonhole principle).
     * 3. Build a max-heap where each entry contains (remainingCount, character). The heap is
     *    ordered by remainingCount in descending order.
     * 4. While the heap contains at least two characters:
     *    - Pop the two characters with highest remaining counts (say c1 and c2).
     *    - Append c1 and c2 to the result (ensuring they are different).
     *    - Decrement their remaining counts and push them back into the heap if counts remain > 0.
     * 5. If one character remains in the heap after the loop:
     *    - Append it only if it does not match the last character already in the result.
     *    - Because of the earlier feasibility check, if it remains, it will be safe to append.
     * 6. Convert the result to a string and return it.
     *
     * Algorithm:
     * - Greedy with a max-heap (priority queue) of character-count pairs.
     *
     * Time Complexity:
     * - O(n log k), where n is the length of str and k is the number of distinct characters (k <= 26).
     *   - Counting frequencies is O(n).
     *   - Each push/pop operation on the heap is O(log k), and we perform O(n) such operations.
     *
     * Space Complexity:
     * - O(k) for the heap and O(n) for the output string builder.
     *
     * Edge cases handled:
     * - str has length 1 (always valid).
     * - str where one character frequency is too high (immediately return "").
     * - str where all characters are the same (length > 1, invalid).
     * - General cases with multiple characters and varying frequencies.
     *
     * @param str input string consisting of lowercase English letters
     * @return any valid reorganization of str such that no two adjacent characters are the same,
     *         or the empty string "" if such reorganization is not possible
     */
    public String reorganizeString(String str) {
        if (str == null || str.length() <= 1) {
            return str == null ? "" : str;
        }

        int length = str.length();
        int[] freq = new int[26];

        int maxFreq = 0;
        for (char ch : str.toCharArray()) {
            int index = ch - 'a';
            freq[index]++;
            maxFreq = Math.max(maxFreq, freq[index]);
        }

        // Feasibility check: if any character appears more than (length + 1) / 2 times, no valid arrangement exists.
        if (maxFreq > (length + 1) / 2) {
            return "";
        }

        // Max-heap based on remaining frequency of characters.
        java.util.PriorityQueue<CharacterCount> maxHeap =
                new java.util.PriorityQueue<>((a, b) -> Integer.compare(b.count, a.count));

        // Seed the heap with characters that appear at least once.
        for (int i = 0; i < 26; i++) {
            if (freq[i] > 0) {
                maxHeap.offer(new CharacterCount((char) ('a' + i), freq[i]));
            }
        }

        StringBuilder result = new StringBuilder();

        while (maxHeap.size() >= 2) {
            // Take the two most frequent characters.
            CharacterCount first = maxHeap.poll();
            CharacterCount second = maxHeap.poll();

            // Append them in sequence to ensure they are different adjacent characters.
            result.append(first.character);
            result.append(second.character);

            // Decrement counts and reinsert if they still have remaining occurrences.
            if (--first.count > 0) {
                maxHeap.offer(first);
            }

            if (--second.count > 0) {
                maxHeap.offer(second);
            }
        }

        // If one character remains, append it (safe due to earlier feasibility check).
        if (!maxHeap.isEmpty()) {
            CharacterCount last = maxHeap.poll();
            // As a safety check, ensure not equal to the last appended character.
            if (result.length() > 0 && result.charAt(result.length() - 1) == last.character) {
                // This scenario should not occur given our feasibility check and construction logic.
                return "";
            }
            result.append(last.character);
        }

        return result.toString();
    }

    // Helper class to store character and its remaining count.
    private static class CharacterCount {
        private final char character;
        private int count;

        CharacterCount(char character, int count) {
            this.character = character;
            this.count = count;
        }
    }
}

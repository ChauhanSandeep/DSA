package strings.patternmatching;

import java.util.*;

/**
 * Problem: Longest Repeating Character Replacement
 *
 * Given an uppercase string and at most k replacements, return the longest
 * substring that can be turned into one repeated character. A window is valid
 * when every non-majority character inside it can be replaced within the budget.
 *
 * Leetcode: https://leetcode.com/problems/longest-repeating-character-replacement/ (Medium)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Strings | Sliding window | Frequency counting
 *
 * Example:
 *   Input:  s = "AABABBA", k = 1
 *   Output: 4
 *   Why:    "AABA" can become "AAAA" by replacing the single 'B'.
 *
 * Follow-ups:
 *   1. What if the alphabet is not fixed to uppercase English letters?
 *      Use a map and recompute or maintain the maximum frequency per window.
 *   2. How would you return the actual substring?
 *      Track the best window's start and length whenever the answer improves.
 *   3. What if replacement costs differ by character?
 *      Maintain the cheapest target character cost instead of windowSize - maxFreq.
 */
public class LongestRepeatingCharacterReplacement {

    public static void main(String[] args) {
        LongestRepeatingCharacterReplacement solver = new LongestRepeatingCharacterReplacement();

        String[] inputs = {"ABAB", "AABABBA", ""};
        int[] replacements = {2, 1, 3};
        int[] expected = {4, 4, 0};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.characterReplacement(inputs[i], replacements[i]);
            System.out.printf("s=%s k=%d -> %d  expected=%d%n",
                inputs[i], replacements[i], got, expected[i]);
        }
    }

        /**
     * Intuition: inside a window, the cheapest way to make every character equal
     * is to keep the most frequent character and replace the rest. Therefore the
     * window is valid exactly when window length minus maxFreq is at most k.
     *
     * Algorithm:
     *   1. Expand right, updating the frequency count and current maxFreq.
     *   2. While replacements needed exceed k, shrink from left and recompute maxFreq.
     *   3. Record the largest valid window length seen.
     *
     * Time:  O(26 * n) - each shrink recomputes over the fixed uppercase alphabet.
     * Space: O(1) - the frequency array has 26 slots.
     *
     * @param s Uppercase string to edit.
     * @param k Maximum replacements allowed.
     * @return Length of the longest replaceable repeating-character substring.
     */
    public int characterReplacement(String s, int k) {
        if (s == null || s.length() == 0) return 0;

        int[] count = new int[26]; // Frequency of each character
        int left = 0, maxFreq = 0, result = 0;

        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            count[rightChar - 'A']++;
            maxFreq = Math.max(maxFreq, count[rightChar - 'A']);

            // If current window is invalid, shrink from left
            while (right - left + 1 - maxFreq > k) {
                char leftChar = s.charAt(left);
                count[leftChar - 'A']--;
                left++;

                // Recalculate maxFreq after removing left character
                maxFreq = 0;
                for (int freq : count) {
                    maxFreq = Math.max(maxFreq, freq);
                }
            }

            result = Math.max(result, right - left + 1);
        }

        return result;
    }

    /**
     * Optimized sliding window without recalculating maxFreq.
     * Maintains maxFreq as monotonic upper bound.
     */
    public int characterReplacementOptimized(String s, int k) {
        if (s == null || s.length() == 0) return 0;

        int[] count = new int[26];
        int left = 0, maxFreq = 0, result = 0;

        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            count[rightChar - 'A']++;
            maxFreq = Math.max(maxFreq, count[rightChar - 'A']);

            // If window becomes invalid, move left pointer
            // Note: maxFreq might be overestimate, but it doesn't affect correctness
            if (right - left + 1 - maxFreq > k) {
                char leftChar = s.charAt(left);
                count[leftChar - 'A']--;
                left++;
            }

            result = Math.max(result, right - left + 1);
        }

        return result;
    }

    /**
     * HashMap-based approach for handling arbitrary characters.
     * Works with any character set, not just uppercase English letters.
     */
    public int characterReplacementGeneral(String s, int k) {
        if (s == null || s.length() == 0) return 0;

        Map<Character, Integer> count = new HashMap<>();
        int left = 0, maxFreq = 0, result = 0;

        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            count.put(rightChar, count.getOrDefault(rightChar, 0) + 1);
            maxFreq = Math.max(maxFreq, count.get(rightChar));

            while (right - left + 1 - maxFreq > k) {
                char leftChar = s.charAt(left);
                count.put(leftChar, count.get(leftChar) - 1);
                if (count.get(leftChar) == 0) {
                    count.remove(leftChar);
                }
                left++;

                // Recalculate maxFreq
                maxFreq = count.values().stream().mapToInt(Integer::intValue).max().orElse(0);
            }

            result = Math.max(result, right - left + 1);
        }

        return result;
    }

    /**
     * Brute force approach for verification and understanding.
     * Checks all possible substrings explicitly.
     */
    public int characterReplacementBruteForce(String s, int k) {
        int n = s.length();
        int result = 0;

        for (int i = 0; i < n; i++) {
            int[] count = new int[26];
            int maxFreq = 0;

            for (int j = i; j < n; j++) {
                count[s.charAt(j) - 'A']++;
                maxFreq = Math.max(maxFreq, count[s.charAt(j) - 'A']);

                int windowSize = j - i + 1;
                int replacements = windowSize - maxFreq;

                if (replacements <= k) {
                    result = Math.max(result, windowSize);
                } else {
                    break; // No longer valid, and won't be valid for longer windows
                }
            }
        }

        return result;
    }

    /**
     * Character-specific approach trying each character as the target.
     * Alternative perspective: for each character, find longest substring.
     */
    public int characterReplacementByChar(String s, int k) {
        int result = 0;

        // Try each character as the target character
        for (char target = 'A'; target <= 'Z'; target++) {
            result = Math.max(result, longestSubstringWithChar(s, k, target));
        }

        return result;
    }

    // Find longest substring containing mostly 'target' character
    private int longestSubstringWithChar(String s, int k, char target) {
        int left = 0, replacements = 0, result = 0;

        for (int right = 0; right < s.length(); right++) {
            if (s.charAt(right) != target) {
                replacements++;
            }

            while (replacements > k) {
                if (s.charAt(left) != target) {
                    replacements--;
                }
                left++;
            }

            result = Math.max(result, right - left + 1);
        }

        return result;
    }

    /**
     * Two-pass approach: first find all valid windows, then determine maximum.
     * Demonstrates alternative algorithmic structure.
     */
    public int characterReplacementTwoPass(String s, int k) {
        if (s == null || s.length() == 0) return 0;

        List<Window> validWindows = new ArrayList<>();
        int[] count = new int[26];
        int left = 0, maxFreq = 0;

        // First pass: find all maximal valid windows
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            count[rightChar - 'A']++;
            maxFreq = Math.max(maxFreq, count[rightChar - 'A']);

            while (right - left + 1 - maxFreq > k) {
                char leftChar = s.charAt(left);
                count[leftChar - 'A']--;
                left++;

                // Recalculate maxFreq
                maxFreq = Arrays.stream(count).max().orElse(0);
            }

            validWindows.add(new Window(left, right, right - left + 1));
        }

        // Second pass: find maximum window size
        return validWindows.stream().mapToInt(w -> w.size).max().orElse(0);
    }

    // Helper class for window tracking
    private static class Window {
        int left, right, size;

        Window(int left, int right, int size) {
            this.left = left;
            this.right = right;
            this.size = size;
        }
    }

    /**
     * Segment tree approach for range frequency queries.
     * Overkill for this problem but demonstrates advanced technique.
     */
    public int characterReplacementSegmentTree(String s, int k) {
        // For this specific problem, sliding window is more appropriate
        // Segment tree would be useful for multiple range queries
        return characterReplacement(s, k);
    }

    /**
     * Returns the actual longest substring instead of just length.
     * Extension that provides the substring content.
     */
    public String longestRepeatingSubstring(String s, int k) {
        if (s == null || s.length() == 0) return "";

        int[] count = new int[26];
        int left = 0, maxFreq = 0;
        int bestLeft = 0, bestRight = -1, maxLength = 0;

        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            count[rightChar - 'A']++;
            maxFreq = Math.max(maxFreq, count[rightChar - 'A']);

            while (right - left + 1 - maxFreq > k) {
                char leftChar = s.charAt(left);
                count[leftChar - 'A']--;
                left++;

                maxFreq = Arrays.stream(count).max().orElse(0);
            }

            if (right - left + 1 > maxLength) {
                maxLength = right - left + 1;
                bestLeft = left;
                bestRight = right;
            }
        }

        return bestRight >= bestLeft ? s.substring(bestLeft, bestRight + 1) : "";
    }

    /**
     * Parallel processing approach for very long strings.
     * Divides string into segments for concurrent processing.
     */
    public int characterReplacementParallel(String s, int k) {
        if (s == null || s.length() <= 1000) {
            return characterReplacement(s, k); // Use sequential for small strings
        }

        int segmentSize = s.length() / Runtime.getRuntime().availableProcessors();
        List<String> segments = new ArrayList<>();

        for (int i = 0; i < s.length(); i += segmentSize) {
            int end = Math.min(i + segmentSize * 2, s.length()); // Overlap segments
            segments.add(s.substring(i, end));
        }

        return segments.parallelStream()
            .mapToInt(segment -> characterReplacement(segment, k))
            .max()
            .orElse(0);
    }
}
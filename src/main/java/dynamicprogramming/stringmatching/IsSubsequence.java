package dynamicprogramming.stringmatching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Problem: Is Subsequence
 *
 * Return whether source can be obtained by deleting characters from target without changing the order of what remains.
 *
 * Leetcode: https://leetcode.com/problems/is-subsequence/ (Easy)
 * Rating:   not available (not a contest problem)
 * Pattern:  Two pointers | String matching | Preprocessed indices
 *
 * Example:
 *   Input:  source = "abc", target = "ahbgdc"
 *   Output: true
 *   Why:    a, b, and c appear in target in the same order.
 *
 * Follow-ups:
 *   1. How would you return an actual solution, not only the value?
 *      Store predecessor or choice information while filling the same states.
 *   2. How can space be reduced?
 *      Keep only the previous row or active states when the recurrence allows it.
 *   3. How would constraints such as fees, limits, or weights change it?
 *      Add the constraint to the state or transition and keep the same invariant.
 *
 * Related: Distinct Subsequences (115), Number of Matching Subsequences (792).
 */
public class IsSubsequence {

    public static void main(String[] args) {
        IsSubsequence solution = new IsSubsequence();
        String[][] inputs = { {"abc", "ahbgdc"}, {"axc", "ahbgdc"}, {"", "abc"} };
        boolean[] expected = {true, false, true};
        for (int i = 0; i < inputs.length; i++) {
            boolean got = solution.isSubsequence(inputs[i][0], inputs[i][1]);
            System.out.printf("source=%s target=%s -> %s  expected=%s%n", inputs[i][0], inputs[i][1], got, expected[i]);
        }
    }


        /**
     * Intuition: sourceIndex is the next needed source character. Scanning target once preserves order; matching target characters advance sourceIndex, and success means every source character was matched.
     *
     * Algorithm:
     *   1. Return true for empty source.
     *   2. Start both indices at 0.
     *   3. Scan target while source remains.
     *   4. Advance sourceIndex on a character match.
     *   5. Return whether all source characters matched.
     *
     * Time:  O(target.length()) - target is scanned once.
     * Space: O(1) - two indices are stored.
     *
     * @param source candidate subsequence
     * @param target string to scan
     * @return true if source is a subsequence
     */
public boolean isSubsequence(String source, String target) {
        // If source is empty, it's always a subsequence
        if (source.isEmpty()) {
            return true;
        }

        int sourceIndex = 0;
        int targetIndex = 0;

        // Traverse both strings
        while (sourceIndex < source.length() && targetIndex < target.length()) {
            // If characters match, move to next character in source
            if (source.charAt(sourceIndex) == target.charAt(targetIndex)) {
                sourceIndex++;
            }
            // Always move to next character in target
            targetIndex++;
        }

        // If we've gone through all characters in source, it's a subsequence
        return sourceIndex == source.length();
    }

    /**
     * Follow-up solution for checking multiple source strings against the same target
     * This solution preprocesses the target string for efficient lookups
     *
     * Steps:
     * 1. Preprocess the target string to create a map of character to list of indices
     *   where each character appears in the target string.
     *   2. For each character in the source string, use binary search to find the next occurrence
     *   in the target string that comes after the last matched index.
     *   3. If we can find all characters in order, return true; otherwise, return false.
     *
     * Time Complexity: O(n + m log n) where n is the length of target and m is the length of source
     * Space Complexity: O(n) for storing the character indices
     */
    public boolean isSubsequenceForMultipleSources(String source, String target) {
        // Preprocess the target string to create a map of character to list of indices
        // where each character appears in the target string
        Map<Character, List<Integer>> charToIndices = new HashMap<>();

        // Populate the map with character indices
        for (int i = 0; i < target.length(); i++) {
            char c = target.charAt(i);
            charToIndices.putIfAbsent(c, new ArrayList<>());
            charToIndices.get(c).add(i);
        }

        int prevIndex = -1; // To keep track of the last matched index in target

        for (char c : source.toCharArray()) {
            // If the character doesn't exist in target, source can't be a subsequence
            if (!charToIndices.containsKey(c)) {
                return false;
            }

            // Find the first occurrence of c in target that comes after prevIndex
            List<Integer> indices = charToIndices.get(c);
            int insertionPoint = Collections.binarySearch(indices, prevIndex + 1);

            // If the exact value is not found, binarySearch returns (-(insertion point) - 1)
            if (insertionPoint < 0) {
                insertionPoint = -insertionPoint - 1;
            }

            // If no such index exists, source is not a subsequence
            if (insertionPoint == indices.size()) {
                return false;
            }

            // Update prevIndex to the found index
            prevIndex = indices.get(insertionPoint);
        }

        return true;
    }

    /**
     * Alternative solution using Java's built-in indexOf method
     * This is more concise but may be slightly less efficient for very large inputs
     *
     * Time Complexity: O(n*m) in the worst case, where n is the length of source and m is the length of target
     * Space Complexity: O(1)
     */
    public boolean isSubsequenceUsingIndexOf(String source, String target) {
        int currentIndex = -1;

        for (char c : source.toCharArray()) {
            // Find the next occurrence of c in target after currentIndex
            currentIndex = target.indexOf(c, currentIndex + 1);

            // If character not found, source is not a subsequence
            if (currentIndex == -1) {
                return false;
            }
        }

        return true;
    }
}

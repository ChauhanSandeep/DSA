package StackQueue;

import java.util.ArrayList;
import java.util.List;

public class ParenthesisChecker2 {

    public static void main(String[] args) {
        String str = "lee(t(c)o)de)";
        System.out.println(minRemoveToMakeValid(str));
    }

    /**
     * Removes the minimum number of parentheses to make the string valid.
     *
     * Example:
     * Input: s = "lee(t(c)o)de)"
     * Output: "lee(t(c)o)de"
     * Explanation: "lee(t(co)de)" , "lee(t(c)ode)" would also be accepted.
     *
     * Intuition:
     * - If an opening parenthesis ( does not have a matching closing parenthesis ), it is invalid.
     * - If a closing parenthesis ) does not have a preceding unmatched opening parenthesis (, it is invalid.
     * - Identify these indices by traversing the string and keeping track of the parentheses which are not matched.
     *
     * Approach:
     * - Traverse the string and track indices of misplaced '(' and ')'.
     * - Remove these indices to generate a valid string.
     *
     * Time Complexity: O(N), where N is the length of the string.
     * Space Complexity: O(N), as we store indices of invalid parentheses.
     *
     * LeetCode Problem: https://leetcode.com/problems/minimum-remove-to-make-valid-parentheses/
     */
    public static String minRemoveToMakeValid(String str) {
        // opening parenthesis which are not able to closed are stored in invalidOpenIndices
        List<Integer> invalidOpenIndices = new ArrayList<>();
        // closing parenthesis which do not have corresponding opening parenthesis are stored in invalidCloseIndices
        List<Integer> invalidCloseIndices = new ArrayList<>();

        // Identify misplaced parentheses
        for (int i = 0; i < str.length(); i++) {
            char currentChar = str.charAt(i);
            if (currentChar == '(') {
                invalidOpenIndices.add(i);
            } else if (currentChar == ')') {
                if (!invalidOpenIndices.isEmpty()) {
                    invalidOpenIndices.remove(invalidOpenIndices.size() - 1);
                } else {
                    invalidCloseIndices.add(i);
                }
            }
        }

        // Combine invalid indices and sort them
        List<Integer> indicesToRemove = new ArrayList<>(invalidOpenIndices);
        indicesToRemove.addAll(invalidCloseIndices);
        indicesToRemove.sort(Integer::compareTo);

        // Construct the valid string by skipping invalid indices
        StringBuilder validString = new StringBuilder();
        int removalIndex = 0;
        for (int i = 0; i < str.length(); i++) {
            if (removalIndex < indicesToRemove.size() && i == indicesToRemove.get(removalIndex)) {
                removalIndex++;
            } else {
                validString.append(str.charAt(i));
            }
        }
        return validString.toString();
    }
}

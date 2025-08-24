package strings;

import java.util.Stack;

/**
 * Given a string s, remove duplicate letters so that every letter appears once and only once.
 * You must make sure your result is the smallest in lexicographical order among all possible results.
 *
 * Example 1:
 * Input: s = "bcabc"
 * Output: "abc"
 *
 * Example 2:
 * Input: s = "cbacdcbc"
 * Output: "acdb"
 *
 * LeetCode: https://leetcode.com/problems/remove-duplicate-letters/
 *
 * Follow-up Questions:
 * 1. How would you handle the case where we need to keep exactly k occurrences of each character?
 *    - We could modify the solution to track the count of each character and ensure we have exactly k in the result.
 * 2. What if the input string is very large (e.g., 1,000,000 characters)?
 *    - The stack-based solution is O(n) time and O(1) space (since the alphabet size is constant).
 * 3. How would you find all possible results instead of just the lexicographically smallest one?
 *    - We would need to use backtracking to explore all possible valid orderings.
 *
 * Related Problems:
 * - Smallest Subsequence of Distinct Characters (https://leetcode.com/problems/smallest-subsequence-of-distinct-characters/)
 * - Remove K Digits (https://leetcode.com/problems/remove-k-digits/)
 */
public class RemoveDuplicateLetters {
    /**
     * Removes duplicate letters to get the lexicographically smallest result.
     *
     * @param s Input string
     * @return String with duplicates removed and in lexicographical order
     */
    public String removeDuplicateLetters(String s) {
        // Count the frequency of each character
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }

        // To track if a character is already in the result
        boolean[] inStack = new boolean[26];
        Stack<Character> stack = new Stack<>();

        for (char c : s.toCharArray()) {
            // Decrease the count for the current character
            count[c - 'a']--;

            // If the character is already in the stack, skip it
            if (inStack[c - 'a']) {
                continue;
            }

            // While the current character is smaller than the last character in the stack,
            // and the last character appears later in the string, pop it from the stack
            while (!stack.isEmpty()
                   && c < stack.peek()
                   && count[stack.peek() - 'a'] > 0) {
                char removed = stack.pop();
                inStack[removed - 'a'] = false;
            }

            // Add the current character to the stack
            stack.push(c);
            inStack[c - 'a'] = true;
        }

        // Build the result string from the stack
        StringBuilder result = new StringBuilder();
        for (char c : stack) {
            result.append(c);
        }

        return result.toString();
    }

    /**
     * Alternative solution using a recursive approach.
     * This approach is less efficient but demonstrates a different way to solve the problem.
     */
    public String removeDuplicateLettersRecursive(String s) {
        // Find the position of the smallest character that has all other characters after it
        int[] count = new int[26];
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) - 'a']++;
        }

        int pos = 0; // Position of the smallest character
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < s.charAt(pos)) {
                pos = i;
            }

            // If we've seen the last occurrence of this character, break
            if (--count[s.charAt(i) - 'a'] == 0) {
                break;
            }
        }

        // If the string is empty, return it
        if (s.length() == 0) {
            return "";
        }

        // The character at pos is our first character in the result
        char c = s.charAt(pos);

        // Recursively process the remaining string, removing all occurrences of c
        String remaining = s.substring(pos + 1).replaceAll(String.valueOf(c), "");

        return c + removeDuplicateLettersRecursive(remaining);
    }

    /**
     * Another approach using a greedy algorithm with a stack (alternative implementation).
     * This version might be more intuitive for some readers.
     */
    public String removeDuplicateLettersGreedy(String s) {
        // Count the frequency of each character
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }

        // To track if a character is already in the result
        boolean[] used = new boolean[26];
        Stack<Character> stack = new Stack<>();

        for (char c : s.toCharArray()) {
            // Decrease the count for the current character
            count[c - 'a']--;

            // If the character is already used, skip it
            if (used[c - 'a']) {
                continue;
            }

            // While the current character is smaller than the last character in the stack,
            // and the last character appears later in the string, pop it from the stack
            while (!stack.isEmpty()
                   && c < stack.peek()
                   && count[stack.peek() - 'a'] > 0) {
                char removed = stack.pop();
                used[removed - 'a'] = false;
            }

            // Add the current character to the stack
            stack.push(c);
            used[c - 'a'] = true;
        }

        // Build the result string from the stack
        StringBuilder result = new StringBuilder();
        for (char c : stack) {
            result.append(c);
        }

        return result.toString();
    }
}

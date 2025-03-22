package StackQueue;

import java.util.Stack;

public class RemoveDuplicate {
    public static void main(String[] args) {
        String str = "pbbcggttciiippooaais";
        int k = 2;
        System.out.println(removeDuplicates(str, k)); // Expected Output: "ps"
    }

    /**
     * Removes adjacent duplicate characters appearing exactly 'k' times.
     * 
     * Approach:
     * - Use a stack to store character-frequency pairs.
     * - Traverse the string, pushing characters onto the stack.
     * - If a character matches the stack's top, increment its count.
     * - If the count reaches `k`, remove the top element.
     * - Construct the result string from remaining elements in the stack.
     * 
     * Time Complexity: O(N), where N is the length of the string.
     * Space Complexity: O(N), for storing character-frequency pairs in the stack.
     * 
     * LeetCode Problem: https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string-ii/
     */
    public static String removeDuplicates(String str, int k) {
        Stack<Pair> stack = new Stack<>();

        for (char c : str.toCharArray()) {
            if (!stack.isEmpty() && stack.peek().character == c) {
                stack.peek().count++;
                if (stack.peek().count == k) {
                    stack.pop(); // Remove when count reaches 'k'
                }
            } else {
                stack.push(new Pair(c, 1));
            }
        }

        // Build the final result string
        StringBuilder result = new StringBuilder();
        for (Pair pair : stack) {
            result.append(String.valueOf(pair.character).repeat(pair.count));
        }
        return result.toString();
    }

    /**
     * Helper class to store character-frequency pairs.
     */
    static class Pair {
        char character;
        int count;

        Pair(char character, int count) {
            this.character = character;
            this.count = count;
        }
    }
}

package String;

import java.util.PriorityQueue;

/**
 * Problem: Given counts of 'a', 'b', and 'c', construct the longest "happy" string.
 * A string is "happy" if it does not contain "aaa", "bbb", or "ccc" as a substring.
 * 
 * Approach:
 * - Use a max heap (PriorityQueue) to always select the character with the highest remaining count.
 * - Append the character to the result string unless it would create three consecutive occurrences.
 * - If a character reaches two consecutive occurrences, use the second-highest frequency character instead.
 * - Repeat until no valid characters are left.
 * 
 * Optimized Approach:
 * - Greedy character selection without using a heap to achieve O(n) time complexity and O(1) space complexity.
 * 
 * Time Complexity: O(n) (where n = a + b + c)
 * Space Complexity: O(n) for the result string.
 * 
 * LeetCode Problem: https://leetcode.com/problems/longest-happy-string/
 */
public class LongestHappyString {
    public static void main(String[] args) {
        LongestHappyString solver = new LongestHappyString();
        String result = solver.longestHappyStringWithHeap(1, 1, 7);
        System.out.println("Longest happy string using heap: " + result);

        String optimizedResult = solver.longestHappyStringGreedy(1, 1, 7);
        System.out.println("Longest happy string using greedy approach: " + optimizedResult);
    }

    /**
     * Heap-based approach to construct the longest happy string.
     * Uses a max heap to ensure the character with the highest remaining count is used first.
     */
    public String longestHappyStringWithHeap(int a, int b, int c) {
        PriorityQueue<CharCount> maxHeap = new PriorityQueue<>((x, y) -> y.count - x.count);

        // Add characters to the heap if they have a nonzero count.
        if (a > 0) maxHeap.offer(new CharCount('a', a));
        if (b > 0) maxHeap.offer(new CharCount('b', b));
        if (c > 0) maxHeap.offer(new CharCount('c', c));

        StringBuilder result = new StringBuilder();

        while (!maxHeap.isEmpty()) {
            CharCount current = maxHeap.poll(); // Pick the highest frequency character

            int resultLength = result.length();
            // Prevent adding three consecutive identical characters
            if (resultLength >= 2 && result.charAt(resultLength - 1) == current.character &&
                result.charAt(resultLength - 2) == current.character) {

                // If there are no alternative characters left, break
                if (maxHeap.isEmpty()) break;

                // Use the next most frequent character instead
                CharCount next = maxHeap.poll();
                result.append(next.character);
                if (--next.count > 0) maxHeap.offer(next);

                // Reinsert the current character back into the heap for later use
                maxHeap.offer(current);
            } else {
                // Append the current character and decrement its count
                result.append(current.character);
                if (--current.count > 0) maxHeap.offer(current);
            }
        }
        return result.toString();
    }

    /**
     * Optimized greedy approach without using a heap.
     * Constructs the longest happy string by always choosing the most frequent character while avoiding consecutive repetitions.
     */
    public String longestHappyStringGreedy(int a, int b, int c) {
        StringBuilder result = new StringBuilder();

        // Character frequencies
        int remainingA = a, remainingB = b, remainingC = c;
        // Track consecutive occurrences
        int countA = 0, countB = 0, countC = 0;

        int totalCharacters = a + b + c;
        while (totalCharacters > 0) {
            if ((remainingA >= remainingB && remainingA >= remainingC && countA < 2) ||
                (remainingA > 0 && (countB == 2 || countC == 2))) {
                result.append('a');
                remainingA--;
                countA++;
                countB = 0;
                countC = 0;
            } else if ((remainingB >= remainingA && remainingB >= remainingC && countB < 2) ||
                       (remainingB > 0 && (countA == 2 || countC == 2))) {
                result.append('b');
                remainingB--;
                countB++;
                countA = 0;
                countC = 0;
            } else if ((remainingC >= remainingA && remainingC >= remainingB && countC < 2) ||
                       (remainingC > 0 && (countA == 2 || countB == 2))) {
                result.append('c');
                remainingC--;
                countC++;
                countA = 0;
                countB = 0;
            }

            totalCharacters--;
        }
        return result.toString();
    }
}

/**
 * Helper class to store character and its remaining count.
 */
class CharCount {
    char character;
    int count;

    public CharCount(char character, int count) {
        this.character = character;
        this.count = count;
    }
}

package String;

import java.util.PriorityQueue;

/**
 * Problem: Given counts of 'a', 'b', and 'c', construct the longest "happy" string.
 * A string is "happy" if it does not contain "aaa", "bbb", or "ccc" as a substring.
 * Example: For counts a = 1, b = 1, c = 7, the longest happy string is "ccbccacc".
 * Explanation: The string alternates characters to avoid three consecutive occurrences.
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
     * 1. Heap-based approach to construct the longest happy string.
     * Approach:
     * 1. Use a max-heap to always select the character with the highest count.
     * 2. Append the character to the result string unless it would create three consecutive occurrences.
     * 3. If a character reaches two consecutive occurrences, use the second-highest frequency character instead.
     *
     * * Time Complexity: O(n log k) where n = a + b + c and k is the number of unique characters (3 in this case).
     * * Space Complexity: O(n) for the result string and O(k) for the heap.
     */
    public String longestHappyStringWithHeap(int countA, int countB, int countC) {
        // Max-heap based on character count
        PriorityQueue<CharCount> maxHeap = new PriorityQueue<>((x, y) -> y.count - x.count);

        // Add initial characters to the heap if count > 0
        if (countA > 0) {
            maxHeap.add(new CharCount('a', countA));
        }
        if (countB > 0) {
            maxHeap.add(new CharCount('b', countB));
        }
        if (countC > 0) {
            maxHeap.add(new CharCount('c', countC));
        }

        StringBuilder result = new StringBuilder();

        while (!maxHeap.isEmpty()) {
            CharCount current = maxHeap.poll();

            // Check if last two characters are same as the current one
            int currLength = result.length();
            if (currLength >= 2 && result.charAt(currLength - 1) == current.character
                && result.charAt(currLength - 2) == current.character) {
                if (maxHeap.isEmpty()) {
                    break;  // No alternative character to use
                }

                // Use the second most frequent character instead
                CharCount next = maxHeap.poll();
                result.append(next.character);
                next.count--;

                // Put it back if still available
                if (next.count > 0) {
                    maxHeap.add(next);
                }

                // Also put the skipped current character back for future
                maxHeap.add(current);
            } else {
                // Safe to use current character
                result.append(current.character);
                current.count--;

                if (current.count > 0) {
                    maxHeap.add(current);
                }
            }
        }

        return result.toString();
    }

    /**
     * Optimized greedy approach without using a heap.
     * * Approach:
     * 1. Always choose the character with the highest remaining count.
     * 2. Append it to the result string unless it would create three consecutive occurrences.
     * 3. If a character reaches two consecutive occurrences, switch to the second-highest frequency character.
     *
     * * Time Complexity: O(n) where n = a + b + c.
     * * Space Complexity: O(n) for the result string.
     *
     */
    public String longestHappyStringGreedy(int a, int b, int c) {
        StringBuilder result = new StringBuilder();

        // Character frequencies
        int remainingA = a, remainingB = b, remainingC = c;
        // Track consecutive occurrences
        int streakA = 0, streakB = 0, streakC = 0;

        int totalCharacters = a + b + c;
        while (totalCharacters > 0) {
            if ((remainingA >= remainingB && remainingA >= remainingC && streakA < 2) ||
                (remainingA > 0 && (streakB == 2 || streakC == 2))) {
                result.append('a');
                remainingA--;
                streakA++;
                streakB = 0;
                streakC = 0;
            } else if ((remainingB >= remainingA && remainingB >= remainingC && streakB < 2) ||
                       (remainingB > 0 && (streakA == 2 || streakC == 2))) {
                result.append('b');
                remainingB--;
                streakB++;
                streakA = 0;
                streakC = 0;
            } else if ((remainingC >= remainingA && remainingC >= remainingB && streakC < 2) ||
                       (remainingC > 0 && (streakA == 2 || streakB == 2))) {
                result.append('c');
                remainingC--;
                streakC++;
                streakA = 0;
                streakB = 0;
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

    CharCount(char character, int count) {
        this.character = character;
        this.count = count;
    }
}

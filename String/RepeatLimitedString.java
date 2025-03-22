package string;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Problem: Construct a repeat-limited string using characters from s such that:
 * - No character appears more than `repeatLimit` times consecutively.
 * - The lexicographically largest valid string is constructed.
 *
 * Approach:
 * - Count character frequencies.
 * - Use a max heap (PriorityQueue) to always pick the highest lexicographical character.
 * - Append characters up to `repeatLimit`, then insert the next largest character to break consecutive repetitions.
 * - If no other character is available, return the result.
 *
 * Time Complexity: O(N log A), where N is the string length and A is the number of distinct characters (≤ 26).
 * Space Complexity: O(A) for storing character frequencies.
 *
 * LeetCode Link: https://leetcode.com/problems/construct-string-with-repeat-limit/
 */
public class RepeatLimitedString {
    public static void main(String[] args) {
        System.out.println(repeatLimitedString("aababab", 2));  // Output: "bababa"
        System.out.println(repeatLimitedString("cczazcc", 3));  // Output: "zzcccac"
        System.out.println(repeatLimitedString("a", 1));        // Output: "a"
    }

    /**
     * Constructs a repeat-limited string with the largest lexicographical order.
     *
     * @param str   The input string.
     * @param limit The maximum consecutive occurrences of any character.
     * @return A valid string satisfying the constraints.
     */
    public static String repeatLimitedString(String str, int limit) {
        // Step 1: Count character frequencies
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : str.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        // Step 2: Use a max heap to get characters in descending order
        PriorityQueue<CharNode> maxHeap = new PriorityQueue<>(
            (a, b) -> Character.compare(b.character, a.character)
        );

        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            maxHeap.offer(new CharNode(entry.getKey(), entry.getValue()));
        }

        StringBuilder result = new StringBuilder();
        CharNode prevNode = null;

        // Step 3: Build the largest lexicographical string
        while (!maxHeap.isEmpty()) {
            CharNode currNode = maxHeap.poll();

            int useCount = Math.min(currNode.frequency, limit);
            result.append(String.valueOf(currNode.character).repeat(useCount));
            currNode.frequency -= useCount;

            // If there's a previous node waiting to be reinserted, add it back
            if (prevNode != null) {
                maxHeap.offer(prevNode);
                prevNode = null;
            }

            // If current character still has remaining occurrences, store it for next use
            if (currNode.frequency > 0) {
                prevNode = currNode;
            } else {
                prevNode = null;
            }

            // If the top character is the same and we need a breaker character
            if (prevNode != null && !maxHeap.isEmpty() && maxHeap.peek().character != prevNode.character) {
                CharNode nextNode = maxHeap.poll();
                result.append(nextNode.character);
                nextNode.frequency--;

                if (nextNode.frequency > 0) {
                    maxHeap.offer(nextNode);
                }
            }
        }

        return result.toString();
    }
}

/**
 * Helper class to store character-frequency pairs.
 */
class CharNode {
    char character;
    int frequency;

    public CharNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }
}

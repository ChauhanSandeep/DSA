package string;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Problem: Rearrange characters in a string so that no two adjacent characters are the same.
 * 
 * Approach:
 * - Count the frequency of each character.
 * - Use a max heap (PriorityQueue) to always place the most frequent character first.
 * - Greedily place the two most frequent characters at a time.
 * - If a character’s frequency is greater than (n + 1) / 2, return an empty string as it's impossible to rearrange.
 * 
 * Time Complexity: O(N log A), where N is the string length and A is the alphabet size (26 for lowercase letters).
 * Space Complexity: O(A) for storing character frequencies.
 * 
 * LeetCode Link: https://leetcode.com/problems/reorganize-string/
 */
public class ReorganizeString {

    public static void main(String[] args) {
        System.out.println(reorganizeString("aab"));  // Output: "aba"
        System.out.println(reorganizeString("aaab")); // Output: ""
    }

    /**
     * Rearranges the string so that no two adjacent characters are the same.
     * 
     * @param str The input string.
     * @return A valid rearranged string or an empty string if not possible.
     */
    public static String reorganizeString(String str) {
        // Step 1: Count character frequencies
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : str.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        // Step 2: Max heap to store characters based on frequency (highest frequency first)
        PriorityQueue<Character> maxHeap = new PriorityQueue<>(
            (a, b) -> frequencyMap.get(b) - frequencyMap.get(a)
        );
        maxHeap.addAll(frequencyMap.keySet());

        // Step 3: Check if valid rearrangement is possible
        int maxFrequency = maxHeap.peek() != null ? frequencyMap.get(maxHeap.peek()) : 0;
        if (maxFrequency > (str.length() + 1) / 2) {
            return ""; // Not possible to reorganize
        }

        // Step 4: Construct the result using a greedy approach
        StringBuilder result = new StringBuilder();
        while (maxHeap.size() >= 2) {
            char first = maxHeap.poll();
            char second = maxHeap.poll();

            result.append(first).append(second);

            // Decrease frequency and re-add to heap if still needed
            frequencyMap.put(first, frequencyMap.get(first) - 1);
            frequencyMap.put(second, frequencyMap.get(second) - 1);

            if (frequencyMap.get(first) > 0) maxHeap.offer(first);
            if (frequencyMap.get(second) > 0) maxHeap.offer(second);
        }

        // Step 5: Handle the last remaining character (if any)
        if (!maxHeap.isEmpty()) {
            char last = maxHeap.poll();
            if (frequencyMap.get(last) > 1) return ""; // Not possible to reorganize
            result.append(last);
        }

        return result.toString();
    }
}

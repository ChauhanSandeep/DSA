package DynamicProgramming;

import java.util.*;

/**
 * Problem: Count Unique Characters of All Substrings of a Given String
 * LeetCode: https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/
 *
 * Intuition:
 * - Each character in the string contributes to multiple substrings.
 * - We calculate the contribution of each character independently by determining:
 *   - How many substrings it uniquely appears in.
 *   - The contribution formula relies on previous and next occurrences of a character.
 *
 * Approach:
 * 1. **Optimized Contribution Method (`uniqueLetterStringOptimized`)**
 *    - Uses an array `lastSeenIndex` to track the last position of each character.
 *    - An array `charContributions` keeps track of individual character contributions.
 *    - Iterates through the string to update these values dynamically.
 *    - Time Complexity: O(N) (Single pass with O(1) updates)
 *    - Space Complexity: O(1) (Constant extra space for 26 letters)
 *
 * 2. **Alternative Method (`uniqueLetterString`)**
 *    - Uses a HashMap to track the indices of each character.
 *    - Computes contributions using left and right ranges.
 *    - Time Complexity: O(N) (Single pass through characters and their occurrences)
 *    - Space Complexity: O(N) (Stores occurrences of characters)
 */
public class UniqueLetterString {

    public static void main(String[] args) {
        UniqueLetterString solver = new UniqueLetterString();
        System.out.println(solver.uniqueLetterStringOptimized("ABC")); // Expected: 10
        System.out.println(solver.uniqueLetterString("ABC")); // Expected: 10
    }

    /**
     * Optimized approach using last seen index tracking.
     * Computes character contributions dynamically.
     */
    public int uniqueLetterStringOptimized(String str) {
        int[] lastSeenIndex = new int[26]; // Tracks last occurrence of each character
        Arrays.fill(lastSeenIndex, -1);

        int[] charContributions = new int[26]; // Tracks each character's contribution
        int totalUniqueCount = 0;

        for (int i = 0; i < str.length(); i++) {
            int charIndex = str.charAt(i) - 'A';

            // Number of substrings ending at i
            int substringsEndingHere = i + 1;

            // Compute new contribution for this character
            charContributions[charIndex] = substringsEndingHere - (lastSeenIndex[charIndex] + 1);

            // Sum contributions of all characters
            int currentContributionSum = 0;
            for (int j = 0; j < 26; j++) {
                currentContributionSum += charContributions[j];
            }

            // Update final result
            totalUniqueCount += currentContributionSum;

            // Update last seen index for this character
            lastSeenIndex[charIndex] = i;
        }
        return totalUniqueCount;
    }

    /**
     * Alternative approach using a HashMap to store character occurrences.
     * Uses contribution formula: leftRange * rightRange.
     */
    public int uniqueLetterString(String str) {
        Map<Character, List<Integer>> occurrenceMap = new HashMap<>();

        // Step 1: Build a map of character occurrences
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            occurrenceMap.computeIfAbsent(c, key -> new ArrayList<>()).add(i);
        }

        int totalUniqueCount = 0;

        // Step 2: Compute contributions for each character
        for (Map.Entry<Character, List<Integer>> entry : occurrenceMap.entrySet()) {
            List<Integer> indexes = entry.getValue();

            for (int i = 0; i < indexes.size(); i++) {
                int prevIndex = (i == 0) ? -1 : indexes.get(i - 1);
                int nextIndex = (i == indexes.size() - 1) ? str.length() : indexes.get(i + 1);

                int leftRange = indexes.get(i) - prevIndex;
                int rightRange = nextIndex - indexes.get(i);

                totalUniqueCount += leftRange * rightRange;
            }
        }
        return totalUniqueCount;
    }
}

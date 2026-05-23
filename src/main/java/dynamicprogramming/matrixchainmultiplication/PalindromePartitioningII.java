package dynamicprogramming.matrixchainmultiplication;

import java.util.Arrays;

/**
 * Given a string s, partition s such that every substring of the partition is a palindrome.
 * Return the minimum cuts needed for a palindrome partitioning of s.
 *
 * Example:
 * Input: s = "aab"
 * Output: 1
 * Explanation: The palindrome partitioning ["aa","b"] could be produced using 1 cut.
 *
 * LeetCode Link: https://leetcode.com/problems/palindrome-partitioning-ii
 *
 * Follow-up Questions:
 * 1. What if we want to return one of the actual partitions achieving the minimum cuts?
 *    Answer: Track the chosen split index for each prefix in a parent array, then backtrack from minCuts[length].
 * 2. How would you reduce the space complexity?
 *    Answer: Replace the 2D palindrome table with Manacher's algorithm or expand-around-center to get O(N) extra space.
 * 3. What if cuts had non-uniform costs (e.g., cost depends on position)?
 *    Answer: Replace the "+1" in the recurrence with the position-specific cut cost and minimize total cost.
 * 4. How would you extend this to allow at most K non-palindromic segments?
 *    Answer: Add a second dimension minCuts[i][k] tracking remaining allowance of non-palindromic pieces.
 * 5. What if the string is streamed and you must answer queries online?
 *    Answer: Use incremental palindrome tracking (e.g., Eertree/palindromic tree) along with DP updates per new character.
 *
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class PalindromePartitioningII {

    /**
     * Finds the minimum number of cuts needed for a palindrome partitioning of a string.
     * * STEPS TO SOLVE:
     * 1. Precompute Palindromes (Interval DP Pattern):
     * - Use a 2D boolean table 'isPalindrome[left][right]' to store whether substring
     * from index 'left' to 'right' is a palindrome.
     * - Use the 'gap' strategy to fill this table in O(N^2) time.
     * * 2. Calculate Minimum Cuts (Linear Partition DP Pattern):
     * - Define 'minCuts[i]' as the minimum cuts needed for the prefix of length 'i' (substring [0...i-1]).
     * - For each position 'i', iterate through all possible 'j' (where j is the start of the last potential palindrome).
     * - If 'input[j...i-1]' is a palindrome, then:
     * cuts for prefix 'i' = min(current_value, cuts for prefix 'j' + 1).
     * * @param input The string to be partitioned
     * @return The minimum number of cuts needed
     */
    public int minCut(String input) {
        if (input == null || input.length() <= 1) {
            return 0;
        }

        int length = input.length();

        // STEP 1: Precompute palindrome information using Interval DP (The "Gap" Pattern)
        boolean[][] isPalindrome = new boolean[length][length];

        // Every single character is a palindrome (gap 1)
        for (int i = 0; i < length; i++) {
            isPalindrome[i][i] = true;
        }

        // Fill table for gaps of 2 and more
        for (int gap = 2; gap <= length; gap++) {
            for (int left = 0; left <= length - gap; left++) {
                int right = left + gap - 1;
                if (input.charAt(left) == input.charAt(right)) {
                    // A substring is a palindrome if outer chars match AND inner part is a palindrome
                    isPalindrome[left][right] = (gap == 2) || isPalindrome[left + 1][right - 1];
                }
            }
        }

        // STEP 2: Build minimum cuts using Linear Partition DP
        // minCuts[i] stores the min cuts for input.substring(0, i)
        int[] minCuts = new int[length + 1];

        // Initialize with worst case: a cut between every single character
        // A string of length i can be cut at most i-1 times.
        for (int i = 0; i <= length; i++) {
            minCuts[i] = i - 1;
        }

        // Fill minCuts table linearly from left to right
        for (int right = 1; right <= length; right++) {
            // Try every possible index 'j' to see if input[j...i-1] is the last palindromic piece
            for (int left = 0; left < right; left++) {
                if (isPalindrome[left][right - 1]) {
                    // If the whole prefix [0...right-1] is a palindrome, 0 cuts are needed
                    if (left == 0) {
                        minCuts[right] = 0;
                    } else {
                        // Otherwise, take the best solution for the prefix ending at left,
                        // and add 1 more cut for the current palindrome [left...right-1]
                        minCuts[right] = Math.min(minCuts[right], minCuts[left] + 1);
                    }
                }
            }
        }

        return minCuts[length];
    }
}
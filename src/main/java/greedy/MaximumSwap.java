package greedy;

/**
 * 🔢 Problem: Maximum Swap
 * ------------------------------------------------------------------------------------
 * Given a non-negative integer `num`, you can swap **at most one pair of digits**
 * to get the maximum possible number. Return that maximum number.
 *
 * Input: 2736
 * Output: 7236   // swap 2 and 7
 *
 * - Leetcode: https://leetcode.com/problems/maximum-swap/
 *
 * FAANG Follow-up Questions + Short Answers:
 * ------------------------------------------------------------------------------------
 * Q1. What if you can do **K swaps** instead of one?
 * Use **backtracking** to try all swap combinations and keep the max result.
 *
 * Q2. What if the number is represented as a **string or linked list**?
 * Same logic applies, but manipulate characters or nodes accordingly.
 *
 * Q3. Can you solve this using **O(1)** space?
 * Yes, by modifying the digit array in-place using a constant-size index map.
 *
 * Q4. Why doesn't a greedy strategy always work for **K swaps**?
 * Greedy might miss optimal swaps because the best option may involve future decisions. Backtracking required to explore all possibilities.
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class MaximumSwap {

    /**
     * Finds the maximum number obtainable by swapping at most one pair of digits.
     *
     * Steps:
     * 1. Convert the number into a digit array (char array).
     * 2. Record the **last index** of each digit (0 to 9).
     * 3. Traverse the digits from left to right:
     *    - For the current digit, check if any larger digit (9 to current+1) occurs after it.
     *    - If such a digit exists, swap them and return the result.
     * 4. If no better digit is found, return the number as-is.
     *
     * Time Complexity: O(N)
     * - One pass to record last indices
     * - One pass to find the first beneficial swap
     *
     * Space Complexity: O(1)
     * - Fixed-size array of 10 integers (for digit tracking)
     *
     * @param num The original input number
     * @return The maximum number after at most one digit swap
     */
    public int maximumSwap(int num) {
        // Convert the number to a character array for easy digit swaps
        char[] digits = Integer.toString(num).toCharArray();

        // Store the last index of each digit (0 to 9)
        int[] lastIndexOfDigit = new int[10];
        for (int i = 0; i < digits.length; i++) {
            lastIndexOfDigit[digits[i] - '0'] = i;
        }

        // Traverse each digit and try to find a larger digit that occurs later
        for (int i = 0; i < digits.length; i++) {
            int currentDigit = digits[i] - '0';

            // Look for a bigger digit (9 to currentDigit + 1) that appears later
            for (int d = 9; d > currentDigit; d--) {
                if (lastIndexOfDigit[d] > i) {
                    // Swap current digit with the later larger digit
                    int j = lastIndexOfDigit[d];
                    char temp = digits[i];
                    digits[i] = digits[j];
                    digits[j] = temp;

                    // Return the number after the optimal swap
                    return Integer.parseInt(new String(digits));
                }
            }
        }

        // No beneficial swap found; return the original number
        return num;
    }
}
package greedy;

/**
 * Problem: Maximum Swap
 *
 * Given a non-negative integer, swap at most one pair of digits to make the
 * largest possible number. If the digits are already in the best order, return
 * the original number unchanged.
 *
 * Leetcode: https://leetcode.com/problems/maximum-swap/ (Medium)
 * Rating:   acceptance 52.1% (Medium) - no contest Elo (pre-contest problem)
 * Pattern:  Greedy | Rightmost best digit | One beneficial swap
 *
 * Example:
 *   Input:  num = 98368
 *   Output: 98863
 *   Why:    the first improvable digit is 3, and swapping it with the rightmost 8
 *           makes the earliest possible digit larger.
 *
 * Follow-ups:
 *   1. Allow at most k swaps?
 *      Use backtracking with pruning, because the locally best first swap can block a better sequence.
 *   2. The number arrives as a string?
 *      Run the same last-index greedy on the character array and return a string.
 *   3. Return the swap positions instead of the number?
 *      Store the first pair found by the greedy scan before mutating the digits.
 *   4. Minimize the number with one swap?
 *      Mirror the search: find the first digit that can be replaced by a smaller later digit.
 *
 * Related: Create Maximum Number (321), Remove K Digits (402).
 */
public class MaximumSwap {

    public static void main(String[] args) {
        MaximumSwap solver = new MaximumSwap();
        int[] inputs = {2736, 9973, 98368};
        int[] expected = {7236, 9973, 98863};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.maximumSwap(inputs[i]);
            System.out.printf("num=%d -> %d  expected=%d%n", inputs[i], got, expected[i]);
        }
    }


    /**
     * Intuition: a larger digit earlier in the number dominates any improvement
     * later, so the first digit we can improve is the only place worth swapping.
     * Remember the last position of each digit. At each current digit, search from
     * 9 down to currentDigit + 1; the first later digit found gives the largest
     * possible prefix, and using its rightmost occurrence leaves the suffix best.
     *
     * Algorithm:
     *   1. Convert the number to a digit array.
     *   2. Record the last index where each digit 0 through 9 appears.
     *   3. Scan left to right and look for a larger digit that appears later.
     *   4. Swap with the best later digit and return, or return num if no swap helps.
     *
     * Time:  O(d) - each of d digits checks at most 10 larger digits.
     * Space: O(1) - the last-index table always has size 10.
     *
     * @param num original non-negative number
     * @return largest number obtainable with at most one digit swap
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
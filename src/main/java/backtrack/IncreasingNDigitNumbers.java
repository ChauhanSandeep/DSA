package backtrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem: Increasing N-Digit Numbers
 *
 * Generate every integer with exactly n digits whose digits are strictly
 * increasing from left to right. Digits come from 1..9, so there are no leading
 * zeros and no repeated digits.
 *
 * Pattern:  Backtracking | Digit combinations | Build number incrementally
 *
 * Example:
 *   Input:  n = 2
 *   Output: [12, 13, 14, 15, 16, 17, 18, 19, 23, 24, 25, 26, 27, 28, 29,
 *            34, 35, 36, 37, 38, 39, 45, 46, 47, 48, 49, 56, 57, 58, 59,
 *            67, 68, 69, 78, 79, 89]
 *   Why:    every number has exactly two digits and the tens digit is smaller
 *           than the ones digit; choices like 21 or 22 are therefore excluded.
 *
 * Follow-ups:
 *   1. Find the k-th increasing n-digit number without generating all of them?
 *      Use combination ranking over the digit set 1..9.
 *   2. Allow digit 0 after the first position?
 *      Start the first digit at 1, then rank/DFS over remaining digits 0..9 greater than previous.
 *   3. Generate strictly decreasing numbers instead?
 *      Choose digits in descending order or generate combinations and reverse each one.
 *   4. Count values satisfying an additional digit-sum target?
 *      Use DP over (next digit, remaining length, remaining sum).
 *
 *   Approach            Method                              Time      Space (extra)
 *   ------------------  ----------------------------------  --------  -------------
 *   Digit list          generateIncreasingNDigitNumbersList O(C(9,n)) O(n)
 *   Direct arithmetic   generateIncreasingNDigitNumbers     O(C(9,n)) O(n)
 */
public class IncreasingNDigitNumbers {
    private static final int MIN_DIGIT = 1;
    private static final int MAX_DIGIT = 9;

    /**
     * Intuition: a strictly increasing n-digit number is just a choice of n digits
     * from 1..9 written in sorted order. If we pick digits from left to right and
     * only allow the next digit to be larger than the last one, the number is
     * automatically valid and no duplicate orderings are possible. This version
     * keeps the chosen digits in a list, which is easy to read, then turns the
     * complete list into an integer at the leaf.
     *
     * Algorithm:
     *   1. Return an empty list when n is outside 1..9, because there are only nine usable digits.
     *   2. Start DFS from digit 1 with an empty current digit list.
     *   3. At each level, try only digits that leave enough larger digits to reach length n.
     *   4. Append a digit, recurse starting from the next larger digit, then remove
     *      it before trying the next digit.
     *   5. When n digits have been chosen, convert the digit list into one number and record it.
     *
     * Time:  O(C(9,n) * n) - we choose n digits from 9, then convert each chosen digit list.
     * Space: O(n) recursion depth and current digit list, excluding output.
     *
     * @param n number of digits required
     * @return all n-digit numbers with strictly increasing digits
     */
    public List<Integer> generateIncreasingNDigitNumbersList(int n) {
        List<Integer> increasingNumbers = new ArrayList<>();
        if (n <= 0 || n > MAX_DIGIT) return increasingNumbers;

        backtrackWithDigits(MIN_DIGIT, n, new ArrayList<>(), increasingNumbers);
        return increasingNumbers;
    }

    /** Builds increasing digit lists before converting each complete list into a number. */
    private void backtrackWithDigits(int startDigit, int targetLength,
                                     List<Integer> currentDigits,
                                     List<Integer> increasingNumbers) {
        if (currentDigits.size() == targetLength) {
            increasingNumbers.add(toNumber(currentDigits));
            return;
        }

        int slotsRemaining = targetLength - currentDigits.size();
        int lastUsefulDigit = MAX_DIGIT - slotsRemaining + 1;
        for (int digit = startDigit; digit <= lastUsefulDigit; digit++) {
            currentDigits.add(digit);
            backtrackWithDigits(digit + 1, targetLength, currentDigits, increasingNumbers);
            currentDigits.remove(currentDigits.size() - 1);
        }
    }

    /** Converts a list of decimal digits into its integer value. */
    private int toNumber(List<Integer> digits) {
        int number = 0;
        for (int digit : digits) number = number * 10 + digit;
        return number;
    }

    /**
     * Intuition (interview default): the digit-list idea can be made lighter by
     * carrying the number itself as recursion state. Appending digit d is the same
     * as currentNumber * 10 + d, so each leaf is already the answer number. The
     * same increasing-start rule keeps every generated number valid and sorted in
     * natural order.
     *
     * Algorithm:
     *   1. Return an empty list when n is outside 1..9.
     *   2. DFS with the next allowed digit, how many digits have been chosen, and the current number.
     *   3. If n digits have been chosen, add the current number to the answer.
     *   4. Otherwise try each digit that leaves enough larger digits, append it
     *      arithmetically, and recurse from the next larger digit.
     *
     * Time:  O(C(9,n)) - each valid digit combination becomes one number directly.
     * Space: O(n) recursion depth, excluding output.
     *
     * @param n number of digits required
     * @return all n-digit numbers with strictly increasing digits
     */
    public List<Integer> generateIncreasingNDigitNumbers(int n) {
        List<Integer> increasingNumbers = new ArrayList<>();
        if (n <= 0 || n > MAX_DIGIT) return increasingNumbers;

        buildNumber(MIN_DIGIT, 0, n, 0, increasingNumbers);
        return increasingNumbers;
    }

    /** Builds increasing numbers directly by appending the next larger digit. */
    private void buildNumber(int startDigit, int digitsChosen, int targetLength,
                             int currentNumber, List<Integer> increasingNumbers) {
        if (digitsChosen == targetLength) {
            increasingNumbers.add(currentNumber);
            return;
        }

        int slotsRemaining = targetLength - digitsChosen;
        int lastUsefulDigit = MAX_DIGIT - slotsRemaining + 1;
        for (int digit = startDigit; digit <= lastUsefulDigit; digit++) {
            buildNumber(digit + 1, digitsChosen + 1, targetLength,
                currentNumber * 10 + digit, increasingNumbers);
        }
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        IncreasingNDigitNumbers solver = new IncreasingNDigitNumbers();

        int[] inputs = {1, 2, 10};
        String[] expected = {
            "[1, 2, 3, 4, 5, 6, 7, 8, 9]",
            "[12, 13, 14, 15, 16, 17, 18, 19, 23, 24, 25, 26, 27, 28, 29, 34, 35, 36, 37, 38, 39, 45, 46, 47, 48, 49, 56, 57, 58, 59, 67, 68, 69, 78, 79, 89]",
            "[]"
        };

        for (int i = 0; i < inputs.length; i++) {
            List<Integer> got = solver.generateIncreasingNDigitNumbers(inputs[i]);
            System.out.printf("n=%d  ->  %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }
}

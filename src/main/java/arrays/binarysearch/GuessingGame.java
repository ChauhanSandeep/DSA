package arrays.binarysearch;

/**
 * Problem: Guess Number Higher or Lower
 *
 * A hidden number lies in 1..n and a guess API tells whether a guess is correct, too high, or too low. Return the hidden number with binary search.
 *
 * Leetcode: https://leetcode.com/problems/guess-number-higher-or-lower/ (Easy)
 * Rating:   acceptance 57.9% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  Binary search | API feedback | Inclusive bounds
 *
 * Example:
 *   Input:  n = 10, pick = 6
 *   Output: 6
 *   Why:    API feedback discards the half that cannot contain 6.
 *
 * Follow-ups:
 *   1. Wrong guesses have costs? Use interval DP as in problem 375.
 *   2. API can lie once? Track uncertainty ranges or use error-correcting search.
 *   3. n exceeds int? Use long bounds and overflow-safe mid.
 *   4. API calls are expensive? Binary search minimizes ordered yes/no calls.
 *
 * Related: Guess Number Higher or Lower II (375), First Bad Version (278).
 */
public class GuessingGame {

    public static void main(String[] args) {
        int[] limits = { 10, 1, 100 };
        int[] picks = { 6, 1, 73 };
        for (int i = 0; i < limits.length; i++) {
            GuessingGame game = new GuessingGame(picks[i]);
            int got = game.guessNumber(limits[i]);
            System.out.printf("n=%d pick=%d -> %d  expected=%d%n", limits[i], picks[i], got, picks[i]);
        }
    }


    private final int pickedNumber;
    private static final Integer SAME_NUMBER = 0;
    private static final Integer NUMBER_TOO_HIGH = 1;
    private static final Integer NUMBER_TOO_LOW = -1;

    /**
     * Constructor to initialize the game with the picked number.
     * @param pickedNumber the number to guess
     */
    public GuessingGame(int pickedNumber) {
        this.pickedNumber = pickedNumber;
    }

        /** Mock API response for the stored picked number. */
    private int guess(int num) {
        if (num == pickedNumber) {
            return SAME_NUMBER;
        } else if (num > pickedNumber) {
            return NUMBER_TOO_HIGH;
        } else {
            return NUMBER_TOO_LOW;
        }
    }

    /**
     * Main method to test the guessing game logic.
     */


        /**
     * Intuition: The API orders the hidden number relative to mid. A too-high guess discards mid and larger values; a too-low guess discards mid and smaller values.
     *
     * Algorithm:
     *   1. Set low = 1 and high = n.
     *   2. Guess mid with overflow-safe arithmetic.
     *   3. Return mid on SAME_NUMBER.
     *   4. Move high or low past mid based on the API response.
     *
     * Time:  O(log n) - each guess halves the range.
     * Space: O(1) - only bounds are stored.
     *
     * @param n upper search bound
     * @return picked number, or -1 if not found
     */
    public int guessNumber(int n) {
        int low = 1;
        int high = n;

        while (low <= high) {
            // Prevent integer overflow when calculating mid
            int mid = low + (high - low) / 2;
            int apiResponse = guess(mid);

            if (apiResponse == SAME_NUMBER) {
                // Guessed correctly
                return mid;
            } else if (apiResponse == NUMBER_TOO_HIGH) {
                // Guessed too high, move to the left side
                high = mid - 1;
            } else {
                // Guessed too low, move to the right side
                low = mid + 1;
            }
        }

        // If the number isn't found, return -1 (though this shouldn't occur with correct inputs)
        return -1;
    }
}
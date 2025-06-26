package BinarySearch;

/**
 * Problem: Guess Number Higher or Lower
 *
 * You are playing a game where you need to guess a number chosen by an API.
 * The API provides feedback indicating if your guess is lower, higher, or equal to the picked number.
 *
 * Example:
 *   Input: n = 10, pick = 6
 *   Output: 6
 *   Explanation: The API will guide you through guesses until you find the number.
 *
 * Leetcode: https://leetcode.com/problems/guess-number-higher-or-lower/
 *
 * Follow-up questions:
 * 1️⃣ Can you optimize it further if the guess API is expensive?
 *     - If API calls are costly, consider caching previous guesses or using a more balanced search strategy to minimize calls.
 * 2️⃣ What if the range is huge (like 10^9)?
 *     - The same binary search approach still applies, as it’s logarithmic in time.
 */
public class GuessingGame {

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

    /**
     * Mock API that returns:
     *  - 0 if the guess is correct
     *  - -1 if the picked number is smaller than the guess
     *  - 1 if the picked number is larger than the guess
     */
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
    public static void main(String[] args) {
        GuessingGame game = new GuessingGame(3);
        int result = game.guessNumber(10);
        System.out.println("Guessed Number: " + result);
    }

    /**
     * Uses binary search to find the number picked by the game.
     *
     * Steps:
     *  1. Initialize the search boundaries as [1, n].
     *  2. Perform binary search:
     *     - If guess(mid) == 0, return mid.
     *     - If guess(mid) < 0, search in the left half.
     *     - If guess(mid) > 0, search in the right half.
     *  3. If no number found (shouldn’t happen with correct input), return -1.
     *
     * Algorithm:
     *  - Binary search algorithm
     *
     * Time Complexity: O(log n) — each iteration halves the search range.
     * Space Complexity: O(1) — constant space.
     *
     * @param n The upper bound of the number range.
     * @return The guessed number if found, -1 otherwise.
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
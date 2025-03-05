package BinarySearch;

/**
 * https://leetcode.com/problems/guess-number-higher-or-lower/
 */
public class GuessingGame {

    private final int ans;
    
    public GuessingGame(int ans) {
        this.ans = ans;
    }

    private int guess(int num) {
        if (num == ans) return 0;
        else if (num > ans) return -1;
        else return 1;
    }

    public static void main(String[] args) {
        GuessingGame game = new GuessingGame(3);
        int guessedNumber = game.guessNumber(10);
        System.out.println("Guessed Number: " + guessedNumber);
    }

    public int guessNumber(int n) {
        int left = 1, right = n;

        while (left <= right) {
            int mid = left + ((right - left) >>> 1); // Prevents overflow
            int res = guess(mid);

            if (res == 0) return mid;
            else if (res == -1) right = mid - 1;
            else left = mid + 1;
        }
        
        return -1; // This should never be reached unless input is incorrect
    }
}

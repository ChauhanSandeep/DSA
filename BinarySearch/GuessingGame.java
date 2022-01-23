package BinarySearch;

/**
 * https://leetcode.com/problems/guess-number-higher-or-lower/
 */
public class GuessingGame {

    private final int ans;
    public GuessingGame(int ans) {
        this.ans = ans;
    }
    private int guess(int guess) {
        if(guess == ans) return 0;
        else if (guess > ans) return -1;
        else return 1;
    }

    public static void main(String[] args) {
        GuessingGame game = new GuessingGame(3);
    }

    // your code goes here
    public int guessNumber(int n) {
        int left = 1;
        int right = n;

        while(true) {
            int mid = left + (right - left)/2;
            int res = guess(mid);
            if(res == 0) {
                return mid;
            }else if(res == -1) {
                right = mid - 1;
            }else {
                left = mid + 1;
            }
        }
    }
}

package Array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Given an integer n, return the least number of perfect square numbers that sum to n.
 * Input: n = 13
 * Output: 2
 * Explanation: 13 = 4 + 9.
 */
public class PerfectSquares {
    public static void main(String[] args) {
        int result = new PerfectSquares().numSquaresDp(13);
        System.out.println(result);
    }

    public int numSquaresDp(int n) {
        final int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);

        for (int i = 1; i * i <= n; i++) {
            dp[i * i] = 1;
        }

        dp[0] = 0;
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j * j <= i; j++) {
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
            }
        }

        return dp[n];
    }

    public int numSquares(int n) {
        List<Integer> squares = findPerfectSquare(n);
        Collections.sort(squares);
        return numSquares(n, squares);
    }

    public int numSquares(int n, List<Integer> squares) {
        int result = n;
        for(Integer i: squares) {
            if(i == n) return 1;
            else if(i < n) {
                int tentative = numSquares(n - i, squares);
                result = Math.min(result, tentative+1);
            }

        }
        return result;
    }

    public List<Integer> findPerfectSquare(int n) {
        List<Integer> result = new ArrayList<>();
        for(int i=1; i<=n; i++) {
            int sqrt = (int) Math.sqrt(i);
            if (sqrt*sqrt == i) result.add(i);
        }
        return result;
    }
}

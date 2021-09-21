package Array;

import java.util.ArrayList;
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
        int result = new PerfectSquares().numSquares(13);
        System.out.println(result);
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

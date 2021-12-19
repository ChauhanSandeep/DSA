package DynamicProgramming;

import java.util.Arrays;

/**
 * Find min cuts required to make in a string to make all the substrings palindromic
 * Input: "aab"
 * Output: 1 cut ("aa", "b")
 *
 */
public class MinCutPallindrome {

    public static void main(String[] args) {
        System.out.println(new MinCutPallindrome().minCut("aab"));
    }

    int size;
    public int minCut(String str) {
        this.size = str.length();
        int[][] dp = new int[size][size];
        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }
        return minCutRec(str, 0, size - 1, dp);
    }


    private int minCutRec(String str, int first, int last, int[][] dp) {
        if (dp[first][last] != -1) return dp[first][last];

        if (isPalindrome(str, first, last)) {
            dp[first][last] = 0;
            return 0;
        }
        int result = last - first;

        for (int i = first+1; i <= last; i++) {
            result = Math.min(result, 1 + minCutRec(str, first, i - 1, dp) + minCutRec(str, i, last, dp));
        }
        dp[first][last] = result;
        return result;
    }

    private boolean isPalindrome(String str, int left, int right) {
        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }
}

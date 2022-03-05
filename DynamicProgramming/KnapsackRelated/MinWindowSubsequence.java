package DynamicProgramming.KnapsackRelated;

import java.util.Arrays;

/**
 * https://leetcode.com/problems/minimum-window-subsequence/
 */
public class MinWindowSubsequence {

    public static void main(String[] args) {
        String s1 = "abcdebd";
        String s2 = "bde";
        System.out.println(new MinWindowSubsequence().minWindow(s1, s2));
    }

    public String minWindow(String source, String target) {
        int targetLen = target.length();
        int sourceLen = source.length();
        int[][] dp = new int[targetLen + 1][sourceLen + 1];
//      dp[i][j] stores index of source which contains target subsequence from i to j
        for (int j = 0; j <= sourceLen; j++) {
            dp[0][j] = j + 1;
        }

        for (int i = 1; i <= targetLen; i++) {
            for (int j = 1; j <= sourceLen; j++) {
                char tchar = target.charAt(i-1);
                char schar = source.charAt(j-1);
                if (tchar == schar) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = dp[i][j - 1];
                }
            }
        }
        System.out.println(Arrays.deepToString(dp));

        int resStart = 0;
        int resLen = sourceLen + 1;
        for (int j = 1; j <= sourceLen; j++) {
            int currStart = dp[targetLen][j];
            if ( currStart != 0) {
                if (j - currStart + 1 < resLen) {
                    resStart = currStart - 1;
                    resLen = j - currStart + 1;
                }
            }
        }
        return resLen == sourceLen + 1 ? "" : source.substring(resStart, resStart + resLen);
    }
}

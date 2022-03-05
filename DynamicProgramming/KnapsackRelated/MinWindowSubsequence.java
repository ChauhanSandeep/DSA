package DynamicProgramming.KnapsackRelated;

import java.util.Arrays;

/**
 * https://leetcode.com/problems/minimum-window-subsequence/
 */
public class MinWindowSubsequence {

    public static void main(String[] args) {
        String s1 = "abcdebdde";
        String s2 = "bde";
        System.out.println(new MinWindowSubsequence().minWindow(s1, s2));
    }

    public String minWindow(String source, String target) {
        if(source.length() < target.length()) return "";

        int sourceLen = source.length();
        int targetLen = target.length();

        /**
         * in dp[i][j]
         * target substring 0-i
         * source substring dp[i][j] - j
         * dp[i][j] contains start of the source substring
         */
        int[][] dp = new int[targetLen + 1][sourceLen + 1];
        for(int[] row: dp) {
            Arrays.fill(row, -1);
        }

        for(int j=0; j<sourceLen+1; j++) {
            // j+1 because it will be used by right-down diagonal
            dp[0][j] = j+1;
        }

        for(int i=1; i<targetLen+1; i++) {
            for(int j=1; j<sourceLen+1; j++) {
                char tchar = target.charAt(i-1);
                char schar = source.charAt(j-1);
                if(schar == tchar) dp[i][j] = dp[i-1][j-1];
                else dp[i][j] = dp[i][j-1];
            }
        }

        int resStart = -1;
        int resLen = sourceLen + 1;
        for(int j=1; j<sourceLen+1; j++) {
            if(dp[targetLen][j] != -1) {
                int currStart = dp[targetLen][j] - 1;
                int currEnd = j-1;
                int currLen = currEnd - currStart + 1;
                if(currLen < resLen) {
                    resLen = currLen;
                    resStart = currStart;
                }
            }
        }
        if(resLen == sourceLen + 1) return "";
        return source.substring(resStart, resStart + resLen);

    }
}

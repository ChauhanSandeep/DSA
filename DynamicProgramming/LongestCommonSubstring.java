package DynamicProgramming;

public class LongestCommonSubstring {
    public static void main(String[] args) {
        String str1 = "ABCDGH";
        String str2 = "ACDGHR";
        String str3 = findLongestCommonSubstring(str1, str2);
        System.out.println(str3);
    }

    /**
     * Given 2 strings find the longest common substing in them
     * @param str1
     * @param str2
     * @return
     */
    public static String findLongestCommonSubstring(String str1, String str2) {
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];
        for(int i=0; i<dp.length; i++) {
            dp[i][0] = 0;
        }
        for(int i=0; i<dp[0].length; i++) {
            dp[0][i] = 0;
        }

        int max = 0;
        int end = 0;

        for(int i=1; i<dp.length; i++) {
            for(int j=1; j<dp[0].length; j++) {
                if(str1.charAt(i-1) == str2.charAt(j-1)) {
                    dp[i][j] = dp[i-1][j-1] + 1;
                    if(dp[i][j] > max) {
                        max = dp[i][j];
                        end = i;
                    }
                }
            }
        }

        return str1.substring(end-max, end);
    }
}

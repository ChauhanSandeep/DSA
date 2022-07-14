package DynamicProgramming;

/**
 * https://leetcode.com/problems/interleaving-string/
 */
public class InterleavingString {

    public boolean isInterleave(String s1, String s2, String s3) {
        if (s1.length() + s2.length() != s3.length()) return false;
        return isInterleave(s1, s2, s3, 0, 0, 0, new Boolean[s1.length()][s2.length()][s3.length()]);
    }

    public boolean isInterleave(String s1, String s2, String s3, int is1, int is2, int is3, Boolean[][][] dp) {
        if (is3 == s3.length() && is1 == s1.length() && is2 == s3.length()) return true;
        if (is2 == s2.length()) return s1.substring(is1).equals(s3.substring(is3));
        if (is1 == s1.length()) return s2.substring(is2).equals(s3.substring(is3));

        if (dp[is1][is2][is3] != null) return dp[is1][is2][is3];
        char c1 = s1.charAt(is1);
        char c2 = s2.charAt(is2);
        char c3 = s3.charAt(is3);

        boolean result = false;
        if (c1 == c3 && c2 == c3) {
            result = isInterleave(s1, s2, s3, is1 + 1, is2, is3 + 1, dp) || isInterleave(s1, s2, s3, is1, is2 + 1, is3 + 1, dp);
        } else if (c1 == c3) {
            result = isInterleave(s1, s2, s3, is1 + 1, is2, is3 + 1, dp);
        } else if (c2 == c3) {
            result = isInterleave(s1, s2, s3, is1, is2 + 1, is3 + 1, dp);
        }

        dp[is1][is2][is3] = result;
        return result;
    }
}

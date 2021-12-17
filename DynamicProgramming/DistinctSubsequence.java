package DynamicProgramming;

import java.util.HashMap;
import java.util.Map;

/**
 * Find the number of distinct subsequence of target String
 * that can be created from source string
 */
public class DistinctSubsequence {
    public static void main(String[] args) {
        System.out.println(new DistinctSubsequence().numDistinctItr(
                "rabbbit",
              "rabbit"));
    }

    /**
     * Recursive approach. Gives TLE  for long inputs
     * @param source
     * @param target
     * @return
     */
    public int numDistinct(String source, String target) {
        if(source == null && target == null) return 0;
        if(source.length() == 0 && target.length() == 0) return 0;
        Map<String, Integer> map = new HashMap<>();

        return find(source, target, "", 0, map);
    }

    public int find(String source, String target, String curr, int index, Map<String, Integer> map) {
        System.out.println(curr);
        if(curr.equals(target)) return 1;
        if(curr.length() > target.length()) return 0;
        if(map.containsKey(curr + "|" + index)) return map.get(curr + "|" + index);
        int count = 0;
        for(int i=index; i<source.length(); i++) {
            count += find(source, target, curr+source.charAt(i), i+1, map);
        }
        map.put(curr + "|" + index, count);
        return count;
    }

    /**
     * Iterative approach
     * @param source
     * @param target
     * @return
     */
    public int numDistinctItr(String source, String target) {
        // array creation
        int[][] dp = new int[target.length()+1][source.length()+1];

        // filling the first row: with 1s
        for(int j=0; j<=source.length(); j++) {
            dp[0][j] = 1;
        }

        // the first column is 0 by default in every other rows but the first, which we need.
        for(int i=1; i<=target.length(); i++) {
            for(int j=1; j<=source.length(); j++) {

                char targetChar = target.charAt(i-1);
                char sourceChar = source.charAt(j-1);
                if(targetChar == sourceChar) {
                    dp[i][j] = dp[i-1][j-1] + dp[i][j-1];
                } else {
                    dp[i][j] = dp[i][j-1];
                }

            }
        }

        return dp[target.length()][source.length()];
    }

}

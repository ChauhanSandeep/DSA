package DynamicProgramming;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DecodeWays {

    public static void main(String[] args) {
        String str = "226";
        System.out.println(numDecodingsItr(str));
        System.out.println(numDecodingsItr("206"));
        System.out.println(numDecodingsItr("228"));
    }

    /**
     *Recursive + Memoization
     */
    public static int numDecodings(String str) {
        Map<Integer, Integer> map = new HashMap<>();
        return str.length() == 0 ? 0 : numDecodings(0, str, map);
    }

    private static int numDecodings(int index, String str, Map<Integer, Integer> map) {
        int len = str.length();
        if (index == len) return 1;
        if (str.charAt(index) == '0'){
            return 0;
        }
        if(map.containsKey(index)) {
            return map.get(index);
        }

        int result = numDecodings(index + 1, str, map);
        if (index < len - 1 && Integer.parseInt(str.substring(index, index+2)) <= 26){
            result += numDecodings(index + 2, str, map);
        }
        map.put(index, result);
        return result;
    }


    /**
     * Iterative
     */
    public static int numDecodingsItr(String str) {
        int len=str.length();
        int[] dp=new int[len+1];

        dp[len]=1;
        for(int i=len-1;i>=0;i--)
            if(str.charAt(i)!='0') {
                dp[i]=dp[i+1]; // take 1 at a time and add remaining ways from (i+1)
                if(i<len-1&&(str.charAt(i)=='1'||str.charAt(i)=='2'&&str.charAt(i+1)<'7')) {
                    dp[i]+=dp[i+2]; // take 2 at a time and add remaining ways from (i+2)
                }
            }
        return dp[0];
    }


}

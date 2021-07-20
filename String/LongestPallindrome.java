package String;

import java.util.Arrays;

public class LongestPallindrome {
    public static void main(String[] args) {
        String str = "kjqlrfzzfmlvyoshiktodnsjjp";
        System.out.println(longestPalin(str));
    }

    private static String longestPalin(String str){
        // basic cases
        if(str.length() < 1) return "";
        if(str.length() == 1) return str;

        int[][] arr = new int [str.length()][str.length()];
        int max = 1;
        String result = str.charAt(0) + "";

        // for 1 character long str
        for(int i=0; i<arr.length; i++) {
            arr[i][i] = 1;
        }
        // for 2 character long str
        for(int i=0; i<str.length()-1; i++) {
            if(str.charAt(i) == str.charAt(i+1)) {
                arr[i][i+1] = 2;
                if(arr[i][i+1] > max) {
                    max = 2;
                    result = str.substring(i, i+2);
                }
            }
        }
        // str longer then 2 character
        for(int len = 2; len <= str.length(); len++) {
            for(int i=0; i<str.length() - len; i++) {
                int j = i+len;
                if(str.charAt(i) == str.charAt(j)) {
                    // arr[i+1][j-1] is inner string (substr(i+1, j-1)). This should also be palindrome for entire string to be palindrome
                    arr[i][j] = arr[i+1][j-1] > 0 ? arr[i+1][j-1] + 2 : 0;
                    if(arr[i][j] > max) {
                        max = arr[i][j];
                        result = str.substring(i, j+1);
                    }
                }
            }
        }

        return result;
    }

}

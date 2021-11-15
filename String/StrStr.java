package String;

public class StrStr {
    public static void main(String[] args) {
        String str = "GeeksForGeeks";
        String subStr = "For";
        int index = findSubStr(str, subStr);
        System.out.println(index);
    }

    /**
     * Find the index of substring in string
     * @param str
     * @param subStr
     * @return
     */
    // TODO : implement using KMP/Z Algo
    public static int findSubStr(String str, String subStr) {
        char c = subStr.charAt(0);
        for(int i=0; i<str.length(); i++) {
            if(str.charAt(i) == c && stringMatches(str, subStr, i)) return i;
        }
        return -1;
    }

    public static boolean stringMatches(String str, String subStr, int index) {
        if (index + subStr.length() > str.length()) return false;

        for(int i=0; i<subStr.length(); i++) {
            if(subStr.charAt(i) != str.charAt(i + index)) return false;
        }
        return true;
    }

    // using KMP alogrithm
    public int strStr(String haystack, String needle) {
        if (needle.isEmpty()) return 0;
        int[] lps = computeKMPTable(needle);
        int i = 0, j = 0, haystackLen = haystack.length(), needleLen = needle.length();
        while (i < haystackLen) {
            if (haystack.charAt(i) == needle.charAt(j)) {
                ++i;
                ++j;
                if (j == needleLen) {
                    return i - needleLen; // found solution
                }
            } else {
                if (j != 0) j = lps[j - 1]; // try match with longest prefix suffix
                else i++; // don't match -> go to next character of `haystack` string
            }
        }
        return -1;
    }
    private int[] computeKMPTable(String pattern) {
        int i = 1, j = 0, n = pattern.length();
        int[] lps = new int[n];
        while (i < n) {
            if (pattern.charAt(i) == pattern.charAt(j)) {
                lps[i++] = ++j;
            } else {
                if (j != 0) j = lps[j - 1]; // try match with longest prefix suffix
                else i++; // don't match -> go to next character
            }
        }
        return lps;
    }
}

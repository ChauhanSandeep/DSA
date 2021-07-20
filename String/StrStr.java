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
}

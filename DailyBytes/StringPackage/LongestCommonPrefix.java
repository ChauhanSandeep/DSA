package DailyBytes.StringPackage;
import java.util.*;

public class LongestCommonPrefix {
    public static void main(String args[]) {
        assert longestCommonPrefix(Arrays.asList("colorado", "color", "cold")).equals("col") : "Incorrect";
        assert longestCommonPrefix(Arrays.asList("a", "b", "c")).equals("") : "Incorrect";
        assert longestCommonPrefix(Arrays.asList("spot", "spotty", "spotted")).equals("spot") : "Incorrect";
    }

    public static String longestCommonPrefix(List<String> strList) {
        if(strList == null || strList.isEmpty()) return "";
        if(strList.size() == 1) return strList.get(0);

        Collections.sort(strList);
        return longestPrefix(strList.get(0), strList.get(strList.size() - 1));
    }

    public static String longestPrefix(String str1, String str2) {
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<str1.length() && i < str2.length(); i++) {
            if(str1.charAt(i) == str2.charAt(i)) builder.append(str1.charAt(i));
        }
        return builder.toString();
    }
}

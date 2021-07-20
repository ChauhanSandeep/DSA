package DynamicProgramming;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordBreak {
    public static void main(String[] args) {
        String s = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab";
        List<String> dict = Arrays.asList("a","aa","aaa","aaaa","aaaaa","aaaaaa","aaaaaaa","aaaaaaaa","ab");
        System.out.println(wordBreak(s, dict));

        s = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab";
        dict = Arrays.asList("a","aa","aaa","aaaa","aaaaa","aaaaaa","aaaaaaa","aaaaaaaa");
        System.out.println(wordBreak(s, dict));

        s = "leetcode";
        dict = Arrays.asList("leet", "code");
        System.out.println(wordBreak(s,dict));
    }

    /**
     * Given a string s and a dictionary of strings wordDict, return true if s can be segmented into a space-separated sequence of one or more dictionary words.
     * Note that the same word in the dictionary may be reused multiple times in the segmentation.
     * @param s
     * @param wordDict
     * @return
     */
    public static boolean wordBreak(String s, List<String> wordDict) {
        Boolean[] canBreak = new Boolean[s.length()];
        Set<String> wordSet = new HashSet<>(wordDict);
        return wordBreakRec(s, 0, wordSet, canBreak);
    }

    private static boolean wordBreakRec(String str, int startIndex, Set<String> dict, Boolean[] canBreak) {
        if (startIndex == str.length()) return true;
        if (canBreak[startIndex] != null) {
            return canBreak[startIndex];
        }

        for (int i = startIndex + 1; i <= str.length(); i++) {
            if (dict.contains(str.substring(startIndex, i))) {
                if (wordBreakRec(str, i, dict, canBreak)) {
                    canBreak[startIndex] = true;
                    return true;
                }
            }
        }
        canBreak[startIndex] = false;
        return false;
    }
}
package DynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordBreak2 {
    public static void main(String[] args) {
        String s = "catsanddog";
        List<String> wordDict = Arrays.asList("cat","cats","and","sand","dog");
        System.out.println(new WordBreak2().wordBreak(s, wordDict));
    }

    /**
     * Given a string s and a dictionary of strings wordDict, add spaces in s to construct a sentence
     * where each word is a valid dictionary word. Return all such possible sentences in any order.
     * Note that the same word in the dictionary may be reused multiple times in the segmentation.
     */
    public List<String> wordBreak(String s, List<String> wordDict) {
        Set<String> set = new HashSet<>(wordDict);
        List<String> result = new ArrayList<>();
        List<String> temp = new ArrayList<>();

        wordBreakRec(s, set, 0, result, temp);
        return result;
    }

    public void wordBreakRec(String str, Set<String> dict, int startIndex, List<String> result, List<String> currWords) {
        if(startIndex == str.length()) {
            result.add(String.join(" ", currWords));
            return;
        }

        for(int i= startIndex+1; i<=str.length(); i++) {
            if(dict.contains(str.substring(startIndex, i))) {
                currWords.add(str.substring(startIndex, i));
                wordBreakRec(str, dict, i, result, currWords);
                currWords.remove(currWords.size() - 1);
            }
        }
    }
}

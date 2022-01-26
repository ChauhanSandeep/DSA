package Bitwise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * https://leetcode.com/problems/count-words-obtained-after-adding-a-letter/
 */
public class CountWords {
    public static void main(String[] args) {
        String[] startWords = {"ant", "act", "tack"};
        String[] targetWords = {"tack", "act", "acti"};
        int res = new CountWords().wordCount(startWords, targetWords);
        System.out.println(res);
    }

    Map<String, Boolean> cache;
    public int wordCount(String[] startWords, String[] targetWords) {
        Map<Integer, Set<String>> startMap = new HashMap<>();
        Map<Integer, List<String>> targetMap = new HashMap<>();
        cache = new HashMap<>();

        for(String str: startWords) {
            char[] temp = str.toCharArray();
            Arrays.sort(temp);
            String str1 = String.valueOf(temp);
            Set<String> set = startMap.getOrDefault(str1.length(), new HashSet<>());
            set.add(str1);
            startMap.put(str1.length(), set);
        }

        for(String str: targetWords) {
            char[] temp = str.toCharArray();
            Arrays.sort(temp);
            String str1 = String.valueOf(temp);
            List<String> list = targetMap.getOrDefault(str1.length(), new ArrayList<>());
            list.add(str1);
            targetMap.put(str1.length(), list);
        }

        int result = 0;
        for(Map.Entry<Integer, List<String>> entry: targetMap.entrySet()) {
            int targetSize = entry.getKey();
            List<String> targetList = entry.getValue();
            Set<String> startSet = startMap.get(targetSize - 1);

            if(startSet != null && startSet.size() > 0) {

                for(int i=0; i<targetList.size(); i++) {
                    if(isValid(targetList.get(i), startSet)) {
                        result++;
                    }
                }

            }
        }
        return result;

    }

    public boolean isValid(String str, Set<String> startSet) {
        for(int i=0; i<str.length(); i++) {
            if(startSet.contains(str.substring(0, i) + str.substring(i+1))) return true;
        }
        return false;
    }
}

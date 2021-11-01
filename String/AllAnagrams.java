package String;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Given 2 strings text and word, find the start index of substring from text which is anagram of word
 *
 * https://leetcode.com/problems/find-all-anagrams-in-a-string/
 */
public class AllAnagrams {

    public static void main(String[] args) {
        List<Integer> result = new AllAnagrams().findAnagrams("abab", "ab");
        System.out.println(result);
    }

    public List<Integer> findAnagrams(String text, String word) {
        List<Integer> result = new ArrayList<>();
        if(text == null || word == null || text.length() < word.length()) return result;

        Map<Character, Integer> wordMap = new HashMap<>();
        Map<Character, Integer> textMap = new HashMap<>();
        for(int i=0; i<word.length(); i++) {
            wordMap.put(word.charAt(i), wordMap.getOrDefault(word.charAt(i), 0) + 1);
        }

        int first = 0;
        int last = 0;
        for(; last<word.length(); last++) {
            char curr = text.charAt(last);
            textMap.put(curr, textMap.getOrDefault(curr, 0) + 1);
        }

        if(isSame(wordMap, textMap)) {
            result.add(0);
        }
        while(last < text.length()) {
            textMap.put(text.charAt(last), textMap.getOrDefault(text.charAt(last), 0) + 1);
            textMap.put(text.charAt(first), textMap.get(text.charAt(first)) - 1);
            if(textMap.get(text.charAt(first)) == 0) {
                textMap.remove(text.charAt(first));
            }
            first++;
            last++;
            if(isSame(wordMap, textMap)) {
                result.add(first);
            }
        }
        return result;
    }

    public boolean isSame(Map<Character, Integer> first, Map<Character, Integer> second) {
        for(Map.Entry<Character, Integer> entry: first.entrySet()) {
            if(!second.containsKey(entry.getKey()) || second.get(entry.getKey()).intValue() != entry.getValue().intValue()) {
                return false;
            }
        }
        return true;
    }
}

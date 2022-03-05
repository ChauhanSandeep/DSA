package Backtracking;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * https://leetcode.com/problems/word-pattern-ii/
 */
public class WordPattern2 {
    public static void main(String[] args) {
        String pattern = "abab";
        String str = "redblueredblue";
        System.out.println("Word matches pattern ? " + new WordPattern2().wordPatternMatch(pattern, str));
    }

    int pLen;
    int sLen;
    public boolean wordPatternMatch(String pattern, String str) {
        Map<Character, String> map = new HashMap<>();
        pLen = pattern.length();
        sLen = str.length();
        /**
         * visited is used to avoid double mapping
         * pattern = aba
         * str = xyzxyzxyz
         * this should return false because mapping
         * [a -> xyz, b ->xyz] is not allowed
         */
        Set<String> visited = new HashSet<>();
        return matches(pattern, 0, str, 0, map, visited);

    }

    public boolean matches(String pattern, int pIndex, String str, int sIndex, Map<Character, String> map, Set<String> visited) {
        if(pIndex == pLen && sIndex == sLen) return true;
        if(pIndex >= pLen || sIndex >= sLen) return false;

        char pchar = pattern.charAt(pIndex);

        if(map.containsKey(pchar)) {
            if(str.substring(sIndex).startsWith(map.get(pchar))) {
                return matches(pattern, pIndex + 1, str, sIndex + map.get(pchar).length(), map, visited);
            }
            return false;
        }

        for(int i=sIndex+1; i<=sLen; i++) {
            String tentative = str.substring(sIndex, i);
            if(visited.contains(tentative)) continue;
            visited.add(tentative);
            map.put(pchar, tentative);
            if(matches(pattern, pIndex+1, str, i, map, visited)) return true;
            // backtrack
            map.remove(pchar);
            visited.remove(tentative);
        }
        return false;
    }
}

package String;

import java.util.HashMap;
import java.util.Map;

/**
 * Given a string str and an integer k, return the length of the longest substring of str such that the
 * frequency of each character in this substring is greater than or equal to k.
 *
 * https://leetcode.com/problems/longest-substring-with-at-least-k-repeating-characters/
 */
public class LongestSubstringKRepeating {
    public static void main(String[] args) {
        int result = new LongestSubstringKRepeating().longestSubstring("aaabb", 3);
        System.out.println(result);
    }

    public int longestSubstring(String str, int k) {
        int len = str.length();
        // base condition
        if(k <= 1) return len;
        if(k > len) return 0;

        Map<Character, Integer> map = new HashMap<>(); // character frequency map
        char[] strArr = str.toCharArray();
        for(Character c: strArr) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }

        // find point till where only character with frequency more than k appears
        int pivot = 0;
        while(pivot < len && map.get(strArr[pivot]) >= k) {
            pivot++;
        }
        if(pivot == len) return len;

        // validate substring from (0 - pivot)
        int left = longestSubstring(str.substring(0, pivot), k);

        // find the point till where character with frequency less than k appears
        while(pivot < len && map.get(strArr[pivot]) < k) {
            pivot++;
        }
        if(pivot == len) return left;

        // find right side of the pivot
        int right = longestSubstring(str.substring(pivot), k);
        return Math.max(left, right);
    }
}

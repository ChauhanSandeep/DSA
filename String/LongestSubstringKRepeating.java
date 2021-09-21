package String;

import java.util.HashMap;
import java.util.Map;

/**
 * Given a string str and an integer k, return the length of the longest substring of str such that the
 * frequency of each character in this substring is greater than or equal to k.
 */
public class LongestSubstringKRepeating {
    public static void main(String[] args) {
        int result = new LongestSubstringKRepeating().longestSubstring("aaabb", 3);
        System.out.println(result);
    }

    public int longestSubstring(String str, int k) {
        int len = str.length();
        if (len == 0 || len < k) return 0;
        if (k <= 1) return len;

        Map<Character, Integer> map = new HashMap<>();
        for (Character c : str.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }

        int pivot = 0;
        while (pivot < len && map.get(str.charAt(pivot)) >= k) {
            pivot++;
        }
        if (pivot == len) return len;

        int left = longestSubstring(str.substring(0, pivot), k);
        while (pivot < len && map.get(str.charAt(pivot)) < k) {
            pivot++;
        }
        int right = (pivot < len) ? longestSubstring(str.substring(pivot), k) : 0;
        return Math.max(left, right);
    }
}

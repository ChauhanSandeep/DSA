package Array;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.com/problems/minimum-window-substring/
 *
 * Min substring in source such that all target string can be created from it
 */
public class MinWindowSubstring {
    public static void main(String[] args) {
        String source = "ADOBECODEBANC";
        String target = "ABC";
        String result = new MinWindowSubstring().minWindow(source, target);
        System.out.println(result);
    }

    public String minWindow(String source, String target) {

        if (source.length() == 0 || target.length() == 0) {
            return "";
        }

        // Dictionary which keeps a count of all the unique characters in target.
        Map<Character, Integer> charFreqMap = new HashMap<>();
        for (Character c: target.toCharArray()) {
            charFreqMap.put(c, charFreqMap.getOrDefault(c, 0) + 1);
        }

        int left = 0, right = 0;
        int requiredMatching = charFreqMap.size();
        int currMatching = 0;

        // Dictionary which keeps a count of all the unique characters in the current window.
        Map<Character, Integer> windowFreqMap = new HashMap<>();

        // ans list of the form (window length, left, right)
        int[] ans = {-1, 0, 0};

        while (right < source.length()) {
            // Add one character from the right to the window
            char curr = source.charAt(right);
            windowFreqMap.put(curr, windowFreqMap.getOrDefault(curr, 0) + 1);

            if (charFreqMap.containsKey(curr) && windowFreqMap.get(curr).intValue() == charFreqMap.get(curr).intValue()) {
                currMatching++;
            }

            // Try and contract the window from left till the point where it ceases to be 'desirable'.
            while (left <= right && currMatching == requiredMatching) {
                curr = source.charAt(left);
                // Save the smallest window until now.
                if (ans[0] == -1 || right - left + 1 < ans[0]) {
                    ans[0] = right - left + 1;
                    ans[1] = left;
                    ans[2] = right;
                }

                windowFreqMap.put(curr, windowFreqMap.get(curr) - 1);
                if (charFreqMap.containsKey(curr) && windowFreqMap.get(curr) < charFreqMap.get(curr)) {
                    currMatching--;
                }
                left++;
            }

            // Keep expanding the window once we are done contracting.
            right++;
        }

        return ans[0] == -1 ? "" : source.substring(ans[1], ans[2] + 1);
    }
}

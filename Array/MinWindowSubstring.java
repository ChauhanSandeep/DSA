package Array;

public class MinWindowSubstring {
    public static void main(String[] args) {
        String source = "ADOBECODEBANC";
        String target = "ABC";
        String result = new MinWindowSubstring().minWindow(source, target);
        System.out.println("Min Window Substring: " + result);
    }

    public String minWindow(String source, String target) {
        if (source.length() == 0 || target.length() == 0) return "";

        // Frequency map for target characters
        int[] targetFreq = new int[128];
        for (char c : target.toCharArray()) targetFreq[c]++;

        int left = 0, right = 0, requiredChars = target.length();
        int minLen = Integer.MAX_VALUE, minStart = 0;

        // Sliding Window Frequency Map
        int[] windowFreq = new int[128];

        while (right < source.length()) {
            char rightChar = source.charAt(right);
            
            // If current character is part of target, decrease the required count
            if (targetFreq[rightChar] > 0) {
                windowFreq[rightChar]++;
                if (windowFreq[rightChar] <= targetFreq[rightChar]) requiredChars--;
            }

            // Try to minimize the window when all required chars are present
            while (requiredChars == 0) {
                int windowSize = right - left + 1;
                if (windowSize < minLen) {
                    minLen = windowSize;
                    minStart = left;
                }

                char leftChar = source.charAt(left);
                if (targetFreq[leftChar] > 0) {
                    windowFreq[leftChar]--;
                    if (windowFreq[leftChar] < targetFreq[leftChar]) requiredChars++;
                }
                left++; // Shrink window
            }

            right++; // Expand window
        }

        return minLen == Integer.MAX_VALUE ? "" : source.substring(minStart, minStart + minLen);
    }
}

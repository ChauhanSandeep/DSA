package Array;

/**
 * https://leetcode.com/problems/swap-adjacent-in-lr-string/
 */
public class SwapAdjLR {
    public static void main(String[] args) {
        System.out.println(new SwapAdjLR().canTransform("LXR", "LRX")); // false
        System.out.println(new SwapAdjLR().canTransform("RXXLRXRXL", "XRLXXRRLX")); // true
    }

    public boolean canTransform(String start, String end) {
        int startIndex = 0, endIndex = 0;
        int n = start.length();

        while (startIndex < n || endIndex < n) {
            // Skip 'X' in start
            while (startIndex < n && start.charAt(startIndex) == 'X') startIndex++;
            // Skip 'X' in end
            while (endIndex < n && end.charAt(endIndex) == 'X') endIndex++;

            // If one string ends before the other, transformation is impossible
            if (startIndex == n && endIndex == n) return true;
            if (startIndex == n || endIndex == n) return false;

            // The non-'X' characters should match
            if (start.charAt(startIndex) != end.charAt(endIndex)) return false;

            // 'R' can only move right
            if (start.charAt(startIndex) == 'R' && startIndex > endIndex) return false;

            // 'L' can only move left
            if (start.charAt(startIndex) == 'L' && startIndex < endIndex) return false;

            startIndex++;
            endIndex++;
        }

        return true;
    }
}

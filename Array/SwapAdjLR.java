package Array;

/**
 * https://leetcode.com/problems/swap-adjacent-in-lr-string/
 */
public class SwapAdjLR {

    public static void main(String[] args) {
        System.out.println(new SwapAdjLR().canTransform("LXR", "LRX"));
    }

    public boolean canTransform(String start, String end) {

        int startIndex = 0;
        int endIndex = 0;
        char[] startArr = start.toCharArray();
        char[] endArr = end.toCharArray();

        while (startIndex < startArr.length || endIndex < endArr.length) {
            // stop at char that is not 'X'
            while (startIndex < startArr.length && startArr[startIndex] == 'X') startIndex++;
            while (endIndex < endArr.length && endArr[endIndex] == 'X') endIndex++;

            if (startIndex >= startArr.length || endIndex >= endArr.length) break;

            // relative order for 'R' and 'L' in 2 strings should be the same
            if (startArr[startIndex] != endArr[endIndex]) return false;
            // R can only move to right
            if (startArr[startIndex] == 'R' && startIndex > endIndex) return false;
            // L can only move to left
            if (startArr[startIndex] == 'L' && startIndex < endIndex) return false;

            // check next
            startIndex++;
            endIndex++;
        }

        return startIndex == endIndex;
    }
}

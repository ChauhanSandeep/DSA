package BinarySearch;

/**
 * This code finds the lexicographically maximal string that can be created by doing up to k swaps.
 * It uses a recursive approach to explore all possible swaps and keep track of the maximum string found.
 * 
 * Algorithm: 
 * - Recursively swap characters in the string and keep track of the maximum string found.
 * - Time Complexity: O(n^k)
 * - Space Complexity: O(n)
 * 
 * LeetCode Problem Link: https://leetcode.com/problems/maximum-number-of-swaps/
 */
public class MaximalString {
    public static void main(String[] args) {
        String input = "254";
        String result = new MaximalString().findMaximalString(input, 1);
        System.out.println(result);
    }

    private String maximumString;

    public String findMaximalString(String input, int maxSwaps) {
        this.maximumString = input;
        performSwaps(input, maxSwaps);
        return maximumString;
    }

    private void performSwaps(String currentString, int remainingSwaps) {
        if (currentString.compareTo(maximumString) > 0) {
            maximumString = currentString;
        }
        if (remainingSwaps == 0) {
            return;
        }

        int length = currentString.length();
        for (int i = 0; i < length - 1; i++) {
            for (int j = i + 1; j < length; j++) {
                if (currentString.charAt(j) > currentString.charAt(i)) {
                    String swappedString = swapCharacters(currentString, i, j);
                    performSwaps(swappedString, remainingSwaps - 1);
                }
            }
        }
    }

    private String swapCharacters(String originalString, int firstIndex, int secondIndex) {
        char[] charArray = originalString.toCharArray();
        char temp = charArray[firstIndex];
        charArray[firstIndex] = charArray[secondIndex];
        charArray[secondIndex] = temp;
        return new String(charArray);
    }
}

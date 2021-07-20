package String;

public class MinInsertionPallindrome {
    public static void main(String[] args) {
        String input = "geeks";
        int res = minPalindrome(input);
        System.out.println(res);
    }

    /**
     * Find min number of characters to be inserted in string to make it palindrome
     * @param input
     * @return
     */
    public static int minPalindrome(String input) {
        int left = 0;
        int right = input.length() - 1;
        return minPalindromeRec(input, left, right);
    }

    public static int minPalindromeRec(String input, int left, int right) {
        if (left == right) return 0;
        if (left > right) return Integer.MAX_VALUE;
        if (right - 1 == left) return input.charAt(left) == input.charAt(right) ? 0 : 1;

        else return input.charAt(left) == input.charAt(right)
                ? minPalindromeRec(input, left + 1, right - 1)
                : Math.min(minPalindromeRec(input, left + 1, right), minPalindromeRec(input, left, right - 1)) + 1;
    }
}

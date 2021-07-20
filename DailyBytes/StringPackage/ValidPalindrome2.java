package DailyBytes.StringPackage;

public class ValidPalindrome2 {

    public static void main(String[] args) {
        assert isPalindrome2("foobof") : "Inoorrect";
        assert isPalindrome2("abcba") : "Inoorrect";
        assert !isPalindrome2("abccab") : "Inoorrect";
    }

    /**
     * Given a string and the ability to delete at most one character, return whether or not it can form a palindrome.
     */
    public static boolean isPalindrome2(String str) {
        if(str == null || str.length() < 2) return true;

        int first = 0, last = str.length() - 1;
        while(first < last) {
            if(str.charAt(first) != str.charAt(last)) {
                return isPalindrome(str.substring(first, last)) || isPalindrome(str.substring(first + 1, last+1));
            }
            first++;
            last--;
        }
        return true;
    }

    public static boolean isPalindrome(String str) {
        if(str == null || str.length() < 2) return true;

        int first = 0, last = str.length() - 1;
        while(first < last) {
            if(str.charAt(first++) != str.charAt(last--)) return false;
        }
        return true;
    }
}

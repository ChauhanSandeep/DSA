package DailyBytes.StringPackage;

public class ValidPallindrome {
    public static void main(String[] args) {
        assert isValidPalindrome("level") : "Incorrect";
        assert !isValidPalindrome("algorithm") : "Incorrect";
        assert isValidPalindrome("A man, a plan, a canal: Panama.") : "Incorrect";
    }

    /**
     * Check if palindrome is valid ignoring non alphabets
     * @param str
     * @return
     */
    public static boolean isValidPalindrome(String str) {
        str = str.toLowerCase();

        StringBuffer buffer = new StringBuffer();
        for(Character c: str.toCharArray()) {
            if(c >= 'a' && c<='z') buffer.append(c);
        }
        str = buffer.toString();

        int low = 0;
        int high = str.length() - 1;

        while(high> low) {
            if(str.charAt(high) != str.charAt(low)) return false;
            high--;
            low++;
        }
        return true;
    }
}

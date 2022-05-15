package String;

public class ValidNumber {
    public static void main(String[] args) {
        String[] validStrings = {"2", "0089", "-0.1", "+3.14", "4.", "-.9", "2e10", "-90E3", "3e+7", "+6e-1", "53.5e93", "-123.456e789"};
        String[] invalidStrings = {"abc", "1a", "1e", "e3", "99e2.5", "--6", "-+3", "95a54e53"};

        ValidNumber validateNumber = new ValidNumber();
        System.out.println("Checking valid numbers");
        for(String str: validStrings) {
            System.out.println(str + " " + validateNumber.isNumber(str));
        }

        System.out.println("\nChecking invalid numbers");
        for(String str: invalidStrings) {
            System.out.println(str + " " + validateNumber.isNumber(str));
        }
    }

    /**
     * https://leetcode.com/problems/valid-number/
     * @param str String to be validated
     * @return true if is valid string else false
     */
    public boolean isNumber(String str) {
        boolean seenDigit = false;
        boolean seenDecimal = false;
        boolean seenExponent = false;
        boolean seenSign = false;

        boolean validSignPosition = true;

        for(int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            validSignPosition = false;
            if(i == 0 || str.charAt(i-1) == 'e' || str.charAt(i-1) == 'E') {
                validSignPosition = true;
            }

            if(c == '+' || c == '-') {
                if(seenSign || !validSignPosition) return false;
                seenSign = true;
            }
            else if(c >= '0' && c <= '9') {
                seenDigit = true;
            }
            else if(c == 'e' || c == 'E') {
                if(!seenDigit || seenExponent) return false;
                seenExponent = true;
                seenSign = false;
                seenDigit = false;
            }
            else if (c == '.') {
                if(seenDecimal || seenExponent) return false;
                seenDecimal = true;
            }
            else{
                return false;
            }
        }
        return seenDigit;
    }
}

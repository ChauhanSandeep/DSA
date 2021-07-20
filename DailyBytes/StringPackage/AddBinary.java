package DailyBytes.StringPackage;

public class AddBinary {
    public static void main(String[] args) {

        String result = addBinary("100", "1");
        System.out.println("The sum of binary is " + result);

        result = addBinary("11", "1");
        System.out.println("The sum of binary is " + result);

        result = addBinary("1", "0");
        System.out.println("The sum of binary is " + result);

        result = addBinary("11", "11");
        System.out.println("The sum of binary is " + result);
    }

    /**
     *  1 + 0 -> 1
     *  0 + 1 -> 1
     *  0 + 0 -> 0
     *  1 + 1 -> 1 0
     *  Find sum of two binary numbers
     */
    public static String addBinary(String first, String second) {
        int firstInd = first.length() - 1;
        int secondInd = second.length() - 1;
        int carry = 0;
        StringBuilder resultStr = new StringBuilder();

        while(firstInd >= 0 && secondInd >=0) {
            int result = Integer.parseInt(first.charAt(firstInd) + "") + Integer.parseInt(second.charAt(secondInd) + "") + carry;
            resultStr.insert(0, result % 2);
            carry = result/2;
            firstInd--;
            secondInd--;

        }

        while(firstInd >= 0) {
            int result = Integer.parseInt(first.charAt(firstInd) + "") + carry;
            resultStr.insert(0, result % 2);
            carry = result/2;
            firstInd--;
        }

        while(secondInd >= 0) {
            int result = Integer.parseInt(second.charAt(secondInd) + "") + carry;
            resultStr.insert(0, result % 2);
            carry = result/2;
            secondInd--;
        }
        if(carry != 0)
            resultStr.insert(0, carry);

        return resultStr.toString();


    }
}


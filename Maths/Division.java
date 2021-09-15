package Maths;

import java.util.HashMap;

/**
 * Find division of two numbers. If the decimal part repeats then put repeating part in brackets ()
 */
public class Division {
    public static void main(String[] args) {
        System.out.println(new Division().fractionToDecimal(2, 1));
        System.out.println(new Division().fractionToDecimal(2, 3));
        System.out.println(new Division().fractionToDecimal(4, 333));
        System.out.println(new Division().fractionToDecimal(1, 5));
        System.out.println(new Division().fractionToDecimal(-1, -2147483648));
    }

    public String fractionToDecimal(int numerator, int denominator) {
        if(numerator == 0) return "0";
        StringBuilder result = new StringBuilder();
        result.append(((numerator > 0) ^ (denominator > 0)) ? "-" : "");

        long num = Math.abs((long) numerator);
        long den = Math.abs((long) denominator);

        // integral part
        result.append(num / den);
        num %= den;
        if (num == 0) {
            return result.toString();
        }

        // fractional part
        result.append(".");
        HashMap<Long, Integer> map = new HashMap<>();// <quotient,index>
        map.put(num, result.length());
        while (num != 0) {
            num *= 10;
            result.append(num / den);
            num %= den;
            if (map.containsKey(num)) {
                int index = map.get(num);
                result.insert(index, "(");
                result.append(")");
                break;
            } else {
                map.put(num, result.length());
            }
        }
        return result.toString();
    }

}

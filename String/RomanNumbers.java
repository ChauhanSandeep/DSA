package String;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class RomanNumbers {
    public static void main(String[] args) {
        String roman = "MCMIV";
        int integer = romanToInteger(roman);
        System.out.println("Integer value for " + roman + " is "+ integer);

        System.out.println("Roman value for " + 9 + " is " + intToRoman(58));
    }

    /**
     * Convert roman representation of a string to integer
     */
    public static int romanToInteger(String roman) {
        if(roman == null || roman.trim().equals("")) {
            throw new RuntimeException("Invalid roman number");
        }

        Map<Character, Integer> map = new HashMap<>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);
        int result = 0;

        for(int i=0; i<roman.length(); i++) {

            int curr = map.get(roman.charAt(i));
            if(i+1 < roman.length()) {
                int next = map.get(roman.charAt(i+1));
                if(next > curr) {
                    result += next - curr;
                    i++;
                }else{
                    result += curr;
                }
            }else{
                result += curr;
            }

        }
        return result;
    }


    private static final int[] values =    {1000, 900,  500, 400,  100,  90,  50,  40,   10,   9,   5,  4,   1};
    private static final String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX","V","IV","I"};

    /**
     * Convert number to roman
     */
    public static String intToRoman(int num) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < values.length && num > 0; i++) {
            while (values[i] <= num) {
                num -= values[i];
                builder.append(symbols[i]);
            }
        }
        return builder.toString();
    }
}

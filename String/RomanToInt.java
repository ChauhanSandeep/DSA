package String;

import java.util.HashMap;
import java.util.Map;

public class RomanToInt {
    public static void main(String[] args) {
        String roman = "MCMIV";
        int integer = romanToInteger(roman);
        System.out.println("Integer value for " + roman + " is "+ integer);
    }

    /**
     * Convert roman representation of a string to integer
     * @param roman
     * @return
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
}

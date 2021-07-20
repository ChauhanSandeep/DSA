package String;

import java.util.HashMap;
import java.util.Map;

public class LongestDistinctCharacter {
    public static void main(String[] args) {
        String input = "abababcdefababcdab";
        int result= findLongestDistinctLength(input);
        System.out.println(result);
    }

    /**
     * Find longest string with all characters unique
     * @param input
     * @return
     */
    public static int findLongestDistinctLength(String input) {
        Map<Character, Integer> map = new HashMap<>();
        map.put(input.charAt(0), 0);

        int first = 0;
        int result = 1;
        int currLen;
        for(int last=1; last<input.length(); last++) {
            Character c = input.charAt(last);
            if(map.containsKey(c) && map.get(c) >= first) {
                first = map.get(c) + 1;
            }
            currLen = last - first + 1;
            map.put(c, last);
            if(currLen>result) result = currLen;
        }
        return result;
    }
}

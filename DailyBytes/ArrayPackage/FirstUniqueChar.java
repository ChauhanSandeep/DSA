package DailyBytes.ArrayPackage;
import java.util.*;

public class FirstUniqueChar {

    public static void main(String[] args) {
        System.out.println("First unique character is " + getFirstUniqueChar("loveleetcode"));
        System.out.println("First unique character is " + getFirstUniqueChar("thedailybyte"));

    }

    public static int getFirstUniqueChar(String str) {
        Map<Character, Integer> map = new HashMap<>();

        for(int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if(map.containsKey(c)) map.remove(c);
            else map.put(c, i);
        }

        int index = Integer.MAX_VALUE;
        for(Map.Entry<Character, Integer> entry: map.entrySet()) {
            if(entry.getValue() < index) index = entry.getValue();
        }
        if(index == Integer.MAX_VALUE) return -1;

        return index;
    }
}

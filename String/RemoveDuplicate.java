package String;

import java.util.HashSet;
import java.util.Set;

public class RemoveDuplicate {
    public static void main(String[] args) {
        String input = "geeksforGeeks";
        String output = removeDuplicates(input);
        System.out.println("String after removing duplicates from " + input + "is " + output);
    }

    /**
     * Remove all the duplicates from string
     * @param input
     * @return
     */
    public static String removeDuplicates(String input) {
        input = input.toLowerCase();
        Set<Character> set = new HashSet<>();
        StringBuilder buffer = new StringBuilder();

        for(Character c: input.toCharArray()) {
            if(!set.contains(c)) {
                buffer.append(c);
                set.add(c);
            }
        }
        return buffer.toString();
    }
}

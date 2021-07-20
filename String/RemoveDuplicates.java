package String;

import java.util.*;

public class RemoveDuplicates {
    public static void main(String[] args) {
        String str = "acaaabbbacdddd";
        System.out.println(removeDuplicates(str));
    }

    private static String removeDuplicates(String str) {
        Deque<Character> queue = new ArrayDeque<>();

        for(int i=0; i<str.length(); i++) {
            if(queue.isEmpty()) {
                queue.offerLast(str.charAt(i));
            }else if (queue.peekLast() != str.charAt(i)) {
                queue.offerLast(str.charAt(i));
            }else {
                while(i < str.length() && queue.peekLast() == str.charAt(i)) {
                    i++;
                }
                queue.pollLast();
                i--;
            }
        }
        StringBuffer buffer = new StringBuffer();
        while(!queue.isEmpty()) {
            buffer.append(queue.pollFirst());
        }
        return buffer.toString();
    }
}

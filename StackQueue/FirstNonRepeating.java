package StackQueue;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class FirstNonRepeating {

    public static void main(String[] args) {
//        System.out.println(firstNonRepeatedCharacter("abcdefghija"));
//        System.out.println(firstNonRepeatedCharacter("hello"));
//        System.out.println(firstNonRepeatedCharacter("Java"));
//        System.out.println(firstNonRepeatedCharacter("simplest"));

        firstNonRepeatingStream("aabc");
    }

    /**
     * provided a string find the first non repeating character
     * @param str
     * @return
     */
    public static char firstNonRepeatedCharacter(String str) {
        Map<Character, Integer> map = new LinkedHashMap<>();

        for(Character c : str.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }

        for(Map.Entry<Character, Integer> entry : map.entrySet()) {
            if(entry.getValue() == 1) return entry.getKey();
        }
        throw new RuntimeException("First non repeating character not found");
    }

    /**
     * Provided stream of characters find the first non repeating character is realtime.
     * @param str
     */
    public static void firstNonRepeatingStream(String str) {
        Map<Character, MyNode> map = new HashMap<>();
        LinkedList<MyNode> list = new LinkedList<>();
        for(Character c : str.toCharArray()) {
            if(map.containsKey(c)) {
                list.remove(map.get(c));
                MyNode node = map.get(c);
                node.count = node.count + 1;
            }else{
                MyNode node = new MyNode(c);
                list.offer(node);
                map.put(c, node);
            }
            if(list.isEmpty()) System.out.println("#");
            else System.out.println(list.peek().c);
        }

    }
}

class MyNode {
    char c;
    int count;

    MyNode(){}

    MyNode(char c) {
        this.c = c;
        this.count = 0;
    }

    MyNode(char c, int count) {
        this.c = c;
        this.count = count;
    }

}

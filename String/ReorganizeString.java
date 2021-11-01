package String;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Given a string s, rearrange the characters of s so that any two adjacent characters are not the same.
 *
 * https://leetcode.com/problems/reorganize-string/
 */
public class ReorganizeString {

    public static void main(String[] args) {
        System.out.println(new ReorganizeString().reorganizeString("aab"));
    }

    public String reorganizeString(String str) {
        Map<Character, Integer> map = new HashMap<>();
        for(Character c: str.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<Character> maxHeap = new PriorityQueue<>((a, b) -> map.get(b) - map.get(a));
        maxHeap.addAll(map.keySet());

        StringBuilder builder = new StringBuilder();
        while(maxHeap.size() >= 2) {
            char first = maxHeap.poll();
            char second = maxHeap.poll();
            builder.append(first);
            builder.append(second);

            map.put(first, map.get(first) - 1);
            map.put(second, map.get(second) - 1);

            if(map.get(first) >= 1){
                maxHeap.offer(first);
            }
            if(map.get(second) >= 1) {
                maxHeap.offer(second);
            }
        }

        if(maxHeap.size() == 1) {
            char c = maxHeap.poll();
            if(map.get(c) > 1) {
                return "";
            }
            builder.append(c);
        }
        return builder.toString();
    }
}

package String;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * You are given a string s and an integer repeatLimit. Construct a new string repeatLimitedString
 * using the characters of s such that no letter appears more than repeatLimit times in a row.
 * You do not have to use all characters from s.
 * https://leetcode.com/problems/construct-string-with-repeat-limit/
 */
public class RepeatLimitedString {
    public static void main(String[] args) {
        System.out.println(new RepeatLimitedString().repeatLimitedString("aababab", 2));
    }

    public String repeatLimitedString(String str, int limit) {
        Map<Character, Integer> map = new HashMap<>();
        for (Character c : str.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        PriorityQueue<Node> queue = new PriorityQueue<Node>((a, b) -> b.c - a.c);
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            queue.offer(new Node(entry.getKey(), entry.getValue()));
        }

        int currLimit = 0;
        StringBuilder builder = new StringBuilder();
        Character lastChar = null;
        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            Node backup = queue.poll();

            if(lastChar != null && curr.c == lastChar && currLimit == limit) {
                if(backup == null) return builder.toString();
                builder.append(backup.c);
                backup.freq = backup.freq - 1;

                currLimit = 1;
                lastChar = backup.c;
            }else{
                builder.append(curr.c);
                curr.freq = curr.freq -1;

                if(lastChar != null && lastChar != curr.c) currLimit = 1;
                else currLimit++;
                lastChar = curr.c;
            }
            if(curr.freq != 0) {
                queue.offer(curr);
            }
            if(backup != null && backup.freq != 0) {
                queue.offer(backup);
            }
        }
        return builder.toString();
    }

}

class Node {
    char c;
    int freq;

    public Node(char c, int freq) {
        this.c = c;
        this.freq = freq;
    }
}

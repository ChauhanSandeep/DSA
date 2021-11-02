package String;

import java.util.PriorityQueue;

/**
 * A string is called happy if it does not have any of the strings 'aaa', 'bbb' or 'ccc' as a substring.
 * Create the longest happy string
 */
public class LongestHappyString {
    public static void main(String[] args) {
        String result = new LongestHappyString().longestDiverseString(1, 1, 7);
        System.out.println("Longest happy string is " + result);
    }

    public String longestDiverseString(int a, int b, int c) {
        PriorityQueue<Pair> heap = new PriorityQueue<>((x, y) -> y.count - x.count);
        if(a != 0) {
            heap.offer(new Pair('a', a));
        }
        if(b != 0) {
            heap.offer(new Pair('b', b));
        }
        if(c != 0) {
            heap.offer(new Pair('c', c));
        }

        StringBuilder builder = new StringBuilder();
        int index = 0;
        while(!heap.isEmpty()) {
            Pair currPair = heap.poll();
            char curr = currPair.c;
            if(index-2 >= 0 && builder.charAt(index-1) == curr && builder.charAt(index-2) == curr) {
                if(heap.isEmpty()) break;

                Pair nextPair = heap.poll();
                builder.append(nextPair.c);
                nextPair.count = nextPair.count-1;
                if(nextPair.count != 0) heap.offer(nextPair);
                heap.offer(currPair);
            }else{
                builder.append(curr);
                currPair.count = currPair.count - 1;
                if(currPair.count != 0) heap.offer(currPair);
            }
            index++;
        }
        return builder.toString();
    }

    /**
     * this is better approach with O(n) time complexity and O(1) space complexity
     */
    public String longestDiverseString2(int a, int b, int c) {
        StringBuilder builder = new StringBuilder();

        int total = a + b + c;
        int currA = 0;
        int currB = 0;
        int currC = 0;

        while(total > 0) {
            if((a >= b && a >= c && currA < 2) || (a > 0 && (currB == 2 || currC == 2))) {
                builder.append("a");
                a--;
                currA++;
                currB = 0;
                currC = 0;
            } else if((b >= a && b >= c && currB < 2) || (b > 0 && (currA == 2 || currC == 2))) {
                builder.append("b");
                b--;
                currB++;
                currA = 0;
                currC = 0;
            }else if((c >= a && c >= b && currC < 2) || (c > 0 && (currA == 2 || currB == 2))) {
                builder.append("c");
                c--;
                currC++;
                currA = 0;
                currB = 0;
            }

            total--;
        }
        return builder.toString();
    }
}

class Pair {
    public char c;
    public int count;

    public Pair(char c, int count) {
        this.c = c;
        this.count = count;
    }

    public Pair(char c) {
        this.c = c;
        this.count = 0;
    }
}

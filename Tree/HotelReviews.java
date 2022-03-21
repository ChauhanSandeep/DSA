package Tree;

import java.util.Arrays;

/**
 * https://www.interviewbit.com/problems/hotel-reviews/
 */
public class HotelReviews {

    public int[] solve(String dictStr, String[] arr) {
        TrieNode root = new TrieNode();
        TrieNode curr = root;

        for (int i = 0; i < dictStr.length(); i++) {
            char c = dictStr.charAt(i);
            if (c == '_') {
                curr.isWord = true;
                curr = root;
            } else {
                int index = c - 'a';
                curr.children[index] = new TrieNode();
                curr = curr.children[index];
            }
        }
        curr.isWord = true;

        Pair pairs[] = new Pair[arr.length];

        for (int i = 0; i < arr.length; i++) {
            pairs[i] = new Pair(i, 0);
            curr = root;
            boolean flag = true;
            for (int j = 0; j < arr[i].length(); j++) {
                char c = arr[i].charAt(j);
                if (c == '_') {
                    if (curr.isWord && flag)
                        pairs[i].count++;
                    curr = root;
                    flag = true;
                } else if (flag) {
                    int index = (int) c - 97;
                    if (curr.children[index] == null) {
                        flag = false;
                    } else
                        curr = curr.children[index];
                }
            }
            if (curr.isWord && flag)
                pairs[i].count++;
        }

        Arrays.sort(pairs, (p1, p2) -> {
            if (p2.count == p1.count) return p1.index - p2.index;
            return p2.count - p1.count;
        });
        int result[] = new int[pairs.length];
        for (int i = 0; i < pairs.length; i++) {
            result[i] = pairs[i].index;
        }
        return result;
    }

    class TrieNode {
        TrieNode[] children = new TrieNode[26];
        boolean isWord;

        TrieNode() {
            isWord = false;
        }
    }

    class Pair {
        int index, count;

        Pair(int index, int count) {
            this.index = index;
            this.count = count;
        }
    }
}

package Hashing;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.com/problems/partition-labels/
 *
 * You are given a string s. We want to partition the string into as many parts as possible so that each letter appears in at most one part.
 * Return a list of integers representing the size of these parts.
 */
public class PartitionLabels {
    public static void main(String[] args) {
        List<Integer> result = new PartitionLabels().partitionLabels("ababcbacadefegdehijhklij");
        System.out.println(result);
    }

    public List<Integer> partitionLabels(String str) {
        int[] position = new int[26];

        for (int i = 0; i < str.length(); i++) {
            position[str.charAt(i) - 'a'] = i;
        }

        char[] charArr = str.toCharArray();
        List<Integer> list = new ArrayList<>();
        int start = 0;
        int max = 0;

        for (int curr = 0; curr < charArr.length; curr++) {
            if (curr == start) {
                max = position[charArr[curr]];
            }
            if (max == curr) {
                list.add(curr - start + 1);
                start = curr + 1;
            } else {

                max = Math.max(max, position[charArr[curr]]);
            }
        }
        return list;
    }
}

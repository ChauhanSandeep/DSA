package Array;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A string s is called good if there are no two different characters in s that have the same frequency.
 * Given a string s, return the minimum number of characters you need to delete to make s good.
 */
public class UniqueFreqDelete {
    public static void main(String[] args) {
        String str = "aabbcc";
        int result = new UniqueFreqDelete().minDeletions(str);
        System.out.println(result);
    }

    public int minDeletions(String str) {
        int deletions = 0;
        int[] freq = new int[26];

        for (int i = 0; i < str.length(); i++) {
            freq[str.charAt(i) - 'a']++;
        }

        Set<Integer> uniqueFreq = new HashSet<>();
        for (int i = 0; i < freq.length; i++) {
            while (freq[i] > 0 && uniqueFreq.contains(freq[i])) {
                freq[i]--;
                deletions++;
            }
            uniqueFreq.add(freq[i]);
        }
        return deletions;
    }
}

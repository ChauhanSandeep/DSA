package DynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * https://leetcode.com/problems/concatenated-words/
 */
public class ConcatenatedWords {
    public static void main(String[] args) {
        String[] words = {"cat", "cats", "catsdogcats", "dog", "dogcatsdog", "hippopotamuses", "rat", "ratcatdogcat"};
        List<String> result = new ConcatenatedWords().findAllConcatenatedWordsInADict(words);
        System.out.println(result);
    }

    public List<String> findAllConcatenatedWordsInADict(String[] words) {
        List<String> result = new ArrayList<>();
        Set<String> set = new HashSet<>();
        Arrays.sort(words, (s1, s2) -> s1.length() - s2.length());

        for (String word : words) {
            if (canForm(word, set)) {
                result.add(word);
            }
            set.add(word);
        }

        return result;
    }

    private boolean canForm(String word, Set<String> set) {
        if (set.isEmpty()) return false; // handle input case [""]
        boolean[] dp = new boolean[word.length() + 1];
        dp[0] = true;

        for (int right = 1; right <= word.length(); right++) {
            for (int left = 0; left < right; left++) {
                if (!dp[left]) continue;
                if (set.contains(word.substring(left, right))) {
                    dp[right] = true;
                    break;
                }
            }
        }
        return dp[word.length()];
    }

}

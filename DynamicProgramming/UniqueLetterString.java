package DynamicProgramming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * https://leetcode.com/problems/count-unique-characters-of-all-substrings-of-a-given-string/
 */
public class UniqueLetterString {

    public static void main(String[] args) {
        System.out.println(new UniqueLetterString().uniqueLetterStringII("ABC"));
    }

    public int uniqueLetterStringII(String str) {
        int[] lastPosition = new int[26];
        Arrays.fill(lastPosition, -1);
        int[] charContributions = new int[26];
        int result = 0;

//     Basically, at each it, we count the charContributions of all the characters to all the substrings ending till that point.

        for (int i = 0; i < str.length(); i++) {

            int c = str.charAt(i) - 'A';

//       Now, we need to update the charContributions of c.
//       The total number of substrings ending at i are i+1. So if it was a unique character, it'd contribute to all of those
//       and it'str charContributions would have been i+1.
//       But if it'str repeating, it means it has already contributed previously. So remove it'str previous charContributions.
//       We can do that as we have it'str last position.
//       So these are the contributions for strings which start after this character'str last occurrence and end at i.
//       A simple example will demonstrate that the number of these strings are i+1 - lastPosition[c]
//       For characters not appeared till now, lastPosition[c] would be 0.
            int totalNumOfSubstringsEndingHere = i + 1;
            charContributions[c] = totalNumOfSubstringsEndingHere - (lastPosition[c]+1);

//       Note that, the charContributions of all the other characters will remain same.

//       count the curr answer by summing all the contributions. This loop can be avoided by the idea in original post, but I find
//       it easy to understand with this and it only iterates over 26 values.
            int curr = 0;
            for (int j = 0; j < 26; j++) {
                curr += charContributions[j];
            }

//       add the current value to final answer.
            result += curr;

//       update the last position of this char. This helps in future to count it'str charContributions if it appears again.
            lastPosition[c] = i;
        }
        return result;
    }

    /**
     * This is much easier approach
     * @param str
     * @return
     */
    public int uniqueLetterString(String str) {
        Map<Character, List<Integer>> map = new HashMap<>(); // <character, list of indexes>

        // 1. create map
        for(int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            map.computeIfAbsent(c, key -> new ArrayList<>()).add(i);
        }

        // 2. for each character, its unique count in string is
        // leftRange * rightRange, where leftRange is range from prev occurence to current occurence
        // and rightRange is range from current occurence to next occurence
        int total = 0;
        for(Map.Entry<Character, List<Integer>> entry: map.entrySet()) {
            List<Integer> indexes = entry.getValue();
            for(int i=0; i<indexes.size(); i++) {
                int lastIndex = i == 0 ? -1 : indexes.get(i-1);
                int nextIndex = i == indexes.size() - 1 ? str.length() : indexes.get(i+1);

                int leftRange = indexes.get(i) - lastIndex;
                int rightRange = nextIndex - indexes.get(i);
                total += leftRange*rightRange;
            }
        }
        return total;
    }

}

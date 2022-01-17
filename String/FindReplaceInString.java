package String;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.com/problems/find-and-replace-in-string/
 */
public class FindReplaceInString {

    public static void main(String[] args) {
        System.out.println(new FindReplaceInString().findReplaceString("abcd", new int[]{0, 2}, new String[]{"a", "cd"}, new String[]{"eee", "fff"}));
        System.out.println(new FindReplaceInString().findReplaceString("abcd", new int[]{0, 2}, new String[]{"ab", "ec"}, new String[]{"eee", "fff"}));
    }

    public String findReplaceString(String str, int[] indices, String[] sources, String[] targets) {

        List<int[]> sorted = new ArrayList<>();
        for (int i = 0; i < indices.length; i++) {
            sorted.add(new int[]{indices[i], i});
        }
        sorted.sort((a, b) -> b[0] - a[0]);

        for (int[] pairIndex : sorted) {
            int inputIndex = pairIndex[1];
            int stringIndex = pairIndex[0];

            String source = sources[inputIndex];
            String target = targets[inputIndex];

            if (str.startsWith(source, stringIndex)) {
                str = str.substring(0, stringIndex) + target + str.substring(stringIndex + source.length());
            }
        }
        return str;
    }
}

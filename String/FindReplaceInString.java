package String;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode Problem: https://leetcode.com/problems/find-and-replace-in-string/
 *
 * Problem:
 * Given a string `s`, an array of indices, and corresponding arrays of source and target strings,
 * replace occurrences of `source[i]` at `indices[i]` with `target[i]` only if the source string matches exactly.
 *
 * Approach:
 * - Sort replacement operations in **descending order of indices** to avoid index shifting issues.
 * - Iterate over sorted operations and **validate matches** before performing replacements.
 * - Use a **StringBuilder** to efficiently modify the string.
 *
 * Time Complexity: O(N log N + M), where N is the length of `indices` (for sorting) and M is the length of `s`.
 * Space Complexity: O(M) for storing the modified string.
 */
public class FindReplaceInString {
    public static void main(String[] args) {
        FindReplaceInString solver = new FindReplaceInString();
        System.out.println(solver.findReplaceString("abcd", new int[]{0, 2}, new String[]{"a", "cd"}, new String[]{"eee", "fff"})); // "eeebfff"
        System.out.println(solver.findReplaceString("abcd", new int[]{0, 2}, new String[]{"ab", "ec"}, new String[]{"eee", "fff"})); // "abcd"
    }

    public String findReplaceString(String s, int[] indices, String[] sources, String[] targets) {
        List<int[]> sortedIndices = new ArrayList<>();

        // Store index mappings along with their original order
        for (int i = 0; i < indices.length; i++) {
            sortedIndices.add(new int[]{indices[i], i});
        }

        // Sort replacements by descending index to avoid string shifting issues
        sortedIndices.sort((a, b) -> Integer.compare(b[0], a[0]));

        // Use a StringBuilder to modify the string efficiently
        StringBuilder modifiedString = new StringBuilder(s);

        for (int[] entry : sortedIndices) {
            int index = entry[0];
            int originalIndex = entry[1];

            String source = sources[originalIndex];
            String target = targets[originalIndex];

            // Check if the source substring matches at the given index
            if (s.startsWith(source, index)) {
                modifiedString.replace(index, index + source.length(), target);
            }
        }

        return modifiedString.toString();
    }
}

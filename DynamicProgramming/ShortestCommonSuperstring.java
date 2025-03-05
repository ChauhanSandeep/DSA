package DynamicProgramming;

/**
 * Problem: Shortest Common Superstring
 * 
 * Given an array of strings, find the shortest string that contains all given strings as subsequences 
 * while preserving their order.
 * 
 * Approach:
 * - Use **Greedy + Recursion** to iteratively merge the most overlapping pairs until only one string remains.
 * - In each step, find the two strings with the **maximum overlap** and merge them.
 * - The process continues recursively until we obtain a single string.
 * 
 * Time Complexity: **O(n^3)** (for each merge operation, we check all pairs, resulting in `O(n^2)`, 
 *   and we merge `O(n)` times)
 * Space Complexity: **O(n^2)** (due to recursive calls and array copies)
 *
 * This is not the optimal **NP-hard** solution, but it's a reasonable approach for small inputs.
 * 
 * LeetCode Link (Closest problem): https://leetcode.com/problems/find-the-shortest-superstring/
 */
public class ShortestCommonSuperstring {

    public static void main(String[] args) {
        String[] words = {"abcd", "cdef", "fgh", "de"};
        System.out.println("Shortest Common Superstring Length: " + new ShortestCommonSuperstring().findShortestSuperstring(words));
    }

    /**
     * Finds the length of the shortest superstring that contains all strings as subsequences.
     * @param words Array of strings
     * @return Length of the shortest superstring
     */
    public int findShortestSuperstring(String[] words) {
        if (words.length == 1) {
            return words[0].length();
        }

        while (words.length > 1) {
            int maxOverlap = -1;
            int idx1 = -1, idx2 = -1;
            String mergedString = "";

            // Find the two strings with maximum overlap
            for (int i = 0; i < words.length; i++) {
                for (int j = 0; j < words.length; j++) {
                    if (i == j) continue;
                    int overlap = findOverlap(words[i], words[j]);

                    int mergedLength = words[i].length() + words[j].length() - overlap;
                    if (overlap > maxOverlap || (overlap == maxOverlap && mergedLength < mergedString.length())) {
                        maxOverlap = overlap;
                        idx1 = i;
                        idx2 = j;
                        mergedString = mergeStrings(words[i], words[j], overlap);
                    }
                }
            }

            // If no overlap is found, return total length of all remaining words
            if (maxOverlap == -1) {
                int totalLength = 0;
                for (String word : words) {
                    totalLength += word.length();
                }
                return totalLength;
            }

            // Create a new array with merged strings
            String[] newWords = new String[words.length - 1];
            int index = 0;
            for (int i = 0; i < words.length; i++) {
                if (i != idx1 && i != idx2) {
                    newWords[index++] = words[i];
                }
            }
            newWords[index] = mergedString;

            words = newWords; // Update the array for the next iteration
        }

        return words[0].length();
    }

    /**
     * Finds the maximum overlap length between two strings.
     * @param str1 First string
     * @param str2 Second string
     * @return Overlap length
     */
    private int findOverlap(String str1, String str2) {
        for (int i = 0; i < str1.length(); i++) {
            if (str2.startsWith(str1.substring(i))) {
                return str1.length() - i;
            }
        }
        return 0;
    }

    /**
     * Merges two strings based on the given overlap length.
     * @param str1 First string
     * @param str2 Second string
     * @param overlap Overlap length
     * @return Merged string
     */
    private String mergeStrings(String str1, String str2, int overlap) {
        return str1.substring(0, str1.length() - overlap) + str2;
    }
}

package strings;

/**
 * LeetCode 2138. Divide a String Into Groups of Size k
 *
 * A string s can be partitioned into groups of size k. If the string s does not evenly divide
 * into groups of size k, then the last group will have fewer than k characters, and you should
 * fill it with fill characters to make it exactly k characters.
 *
 * Example 1:
 * Input: s = "abcdefghi", k = 3, fill = 'x'
 * Output: ["abc","def","ghi"]
 *
 * LeetCode Link: https://leetcode.com/problems/divide-a-string-into-groups-of-size-k/
 */
public class DivideAStringIntoGroupsOfSizeK {

    /**
     * Divides string into groups of size k, filling last group if necessary.
     *
     * Algorithm:
     * 1. Calculate number of complete groups and total groups needed
     * 2. Create result array of appropriate size
     * 3. Fill complete groups with substrings of size k
     * 4. Handle last group separately, adding fill characters if needed
     *
     * Time Complexity: O(n) where n is length of string s
     * Space Complexity: O(n) for the result array
     */
    public String[] divideString(String s, int k, char fill) {
        int n = s.length();
        int numGroups = (n + k - 1) / k; // Ceiling division
        String[] result = new String[numGroups];

        // Fill complete groups
        for (int i = 0; i < numGroups - 1; i++) {
            result[i] = s.substring(i * k, (i + 1) * k);
        }

        // Handle last group
        int lastGroupStart = (numGroups - 1) * k;
        StringBuilder lastGroup = new StringBuilder(s.substring(lastGroupStart));

        // Add fill characters if needed
        while (lastGroup.length() < k) {
            lastGroup.append(fill);
        }

        result[numGroups - 1] = lastGroup.toString();
        return result;
    }
}

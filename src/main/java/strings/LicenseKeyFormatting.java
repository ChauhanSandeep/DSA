package strings;

/**
 * Problem: License Key Formatting
 *
 * Remove dashes, uppercase letters, and regroup a license key so every group has
 * groupSize characters except possibly the first group.
 *
 * Leetcode: https://leetcode.com/problems/license-key-formatting/ (Easy)
 * Rating:   acceptance 46.3% (Easy) - no contest Elo (pre-contest problem)
 * Pattern:  String | Reverse scan | Fixed-size groups
 *
 * Example:
 *   Input:  key = "5F3Z-2e-9-w", groupSize = 4
 *   Output: "5F3Z-2E9W"
 *   Why:    groups are filled from the right after removing old dashes.
 *
 * Follow-ups:
 *   1. Avoid reverse? Count cleaned characters first, then build forward.
 *   2. Custom separators? Parameterize the separator to skip and insert.
 *   3. Validate input? Reject non-alphanumeric non-separator characters.
 */
public class LicenseKeyFormatting {

    public static void main(String[] args) {
        LicenseKeyFormatting solver = new LicenseKeyFormatting();
        String[] inputs = {"5F3Z-2e-9-w", "2-5g-3-J", "---"};
        int[] groupSizes = {4, 2, 3};
        String[] expected = {"5F3Z-2E9W", "2-5G-3J", ""};
        for (int i = 0; i < inputs.length; i++) {
            String got = solver.licenseKeyFormatting(inputs[i], groupSizes[i]);
            System.out.printf("key=%s k=%d -> %s  expected=%s%n", inputs[i], groupSizes[i], got, expected[i]);
        }
    }


        /**
     * Intuition: all complete groups are measured from the right, so scan from
     * the end. Skip old dashes, uppercase kept characters, insert a dash whenever
     * the current reversed group is full, then reverse the builder.
     *
     * Algorithm:
     *   1. Scan key from right to left.
     *   2. Skip dashes and uppercase kept characters.
     *   3. Insert a dash before starting a new full group.
     *   4. Reverse and return the builder.
     *
     * Time:  O(n) - each character is inspected once.
     * Space: O(n) - the formatted key is stored in the builder.
     */
    public String licenseKeyFormatting(String key, int groupSize) {
        StringBuilder result = new StringBuilder();
        int count = 0;

        // Process from right to left
        for (int i = key.length() - 1; i >= 0; i--) {
            char c = key.charAt(i);

            if (c != '-') {
                if (count == groupSize) {
                    result.append('-');
                    count = 0;
                }
                result.append(Character.toUpperCase(c));
                count++;
            }
        }

        return result.reverse().toString();
    }
}

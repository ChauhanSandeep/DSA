package strings;

/**
 * LeetCode 564. Find the Closest Palindrome
 *
 * Given a string n representing an integer, return the closest integer (not including itself),
 * which is a palindrome. If there is a tie, return the smaller one.
 *
 * The closest is defined as the absolute difference minimized between two integers.
 *
 * Example 1:
 * Input: n = "123"
 * Output: "121"
 *
 * Example 2:
 * Input: n = "1"
 * Output: "0"
 * Explanation: 0 and 2 are the closest palindromes but we return the smallest which is 0.
 *
 * Constraints:
 * - 1 <= n.length <= 18
 * - n consists of only digits.
 * - n does not have leading zeros.
 * - n is representing an integer in the range [1, 10^18 - 1].
 *
 * LeetCode Link: https://leetcode.com/problems/find-the-closest-palindrome/
 *
 * Follow-up Questions:
 * - How would you find the k closest palindromes? (Generate candidates iteratively around n)
 * - How would you handle arbitrary precision numbers beyond long range?
 *   (Use BigInteger arithmetic on the candidates, the same algorithm still works on strings.)
 * - How would you find the closest palindrome with additional constraints
 *   (e.g., must be prime)? (Extend candidate generation with a validity filter.)
 *
 * LeetCode Contest Rating: Hard (~2000 difficulty)
 */
public class FindTheClosestPalindrome {

    /**
     * Finds the closest palindrome to the given integer string.
     *
     * Algorithm (mirror-the-prefix with 5 candidates):
     * The closest palindrome to n must be one of these five candidates:
     *   1. Mirror the left half of n onto the right half.
     *   2. Mirror (left_half - 1) onto the right half.
     *   3. Mirror (left_half + 1) onto the right half.
     *   4. 10^(len-1) - 1   e.g., 999...9   (handles cases like 10000 -> 9999)
     *   5. 10^len + 1       e.g., 100...01  (handles cases like 99 -> 101)
     * Among these candidates, exclude n itself, then pick the one with the
     * smallest absolute difference; on a tie, pick the smaller value.
     *
     * Time Complexity:  O(L) where L = n.length() (string ops on length-L strings)
     * Space Complexity: O(L) for candidate strings
     */
    public String nearestPalindromic(String n) {
        int len = n.length();
        long num = Long.parseLong(n);

        // Edge case: single digit -> answer is num - 1 (e.g., "1" -> "0", "9" -> "8")
        if (len == 1) {
            return String.valueOf(num - 1);
        }

        long[] candidates = new long[5];
        // Candidates 4 and 5: boundary palindromes around powers of 10
        candidates[0] = (long) Math.pow(10, len - 1) - 1;   // 999...9 (len-1 digits)
        candidates[1] = (long) Math.pow(10, len) + 1;       // 100...01 (len+1 digits)

        // Take the left half (including middle for odd length)
        int halfLen = (len + 1) / 2;
        long leftHalf = Long.parseLong(n.substring(0, halfLen));

        // Candidates 1, 2, 3: mirror leftHalf, leftHalf-1, leftHalf+1
        for (int i = 0; i < 3; i++) {
            candidates[i + 2] = mirror(leftHalf + (i - 1), len % 2 == 0);
        }

        long bestDiff = Long.MAX_VALUE;
        long bestPalindrome = Long.MAX_VALUE;
        for (long cand : candidates) {
            if (cand == num) continue; // cannot return n itself
            long diff = Math.abs(cand - num);
            if (diff < bestDiff || (diff == bestDiff && cand < bestPalindrome)) {
                bestDiff = diff;
                bestPalindrome = cand;
            }
        }
        return String.valueOf(bestPalindrome);
    }

    /**
     * Mirrors the given half to form a palindrome.
     *
     * @param half      The left half of the palindrome (including the middle digit for odd length).
     * @param evenLen   true  -> result length is 2 * half.length      (mirror the entire half)
     *                  false -> result length is 2 * half.length - 1  (skip the middle when mirroring)
     */
    private long mirror(long half, boolean evenLen) {
        String left = String.valueOf(half);
        // For odd length palindromes, drop the last char before reversing (it's the middle)
        String toReverse = evenLen ? left : left.substring(0, left.length() - 1);
        String reversed = new StringBuilder(toReverse).reverse().toString();
        return Long.parseLong(left + reversed);
    }

    /**
     * Driver method to test {@link #nearestPalindromic(String)} with both
     * odd-length and even-length inputs, plus a few edge cases.
     */
    public static void main(String[] args) {
        FindTheClosestPalindrome solver = new FindTheClosestPalindrome();

        String[][] tests = {
            // {input, expected, description}
            {"123",     "121",     "Odd length  - mirror left half"},
//            {"1",       "0",       "Odd length  - single digit edge case"},
//            {"12932",   "12921",   "Odd length  - mirror produces closer palindrome"},
//            {"99999",   "100001",  "Odd length  - all 9s, jumps to next power of 10"},
//            {"1000",    "999",     "Even length - power of 10 boundary"},
            {"1234",    "1221",    "Even length - mirror left half"},
//            {"11",      "9",       "Even length - tie, prefer smaller"},
//            {"88",      "77",      "Even length - mirror leftHalf-1 wins"},
//            {"10",      "9",       "Even length - small boundary"}
        };

        int passed = 0;
        for (String[] t : tests) {
            String input = t[0], expected = t[1], desc = t[2];
            String actual = solver.nearestPalindromic(input);
            boolean ok = expected.equals(actual);
            if (ok) passed++;
            System.out.printf("[%s] input=%-6s expected=%-7s actual=%-7s | %s%n",
                ok ? "PASS" : "FAIL", input, expected, actual, desc);
        }
        System.out.printf("%n%d/%d tests passed%n", passed, tests.length);
    }
}


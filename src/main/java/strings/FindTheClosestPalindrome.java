package strings;

/**
 * Problem: Find the Closest Palindrome
 *
 * Given an integer string, return the nearest different palindrome. If two
 * candidates are equally close, return the smaller one.
 *
 * Leetcode: https://leetcode.com/problems/find-the-closest-palindrome/ (Hard)
 * Rating:   acceptance 32.0% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  String | Palindrome candidates | Mirror the prefix
 *
 * Example:
 *   Input:  n = "123"
 *   Output: "121"
 *   Why:    121 is closer to 123 than the next mirrored candidate 131.
 *
 * Follow-ups:
 *   1. k closest palindromes? Generate nearby prefixes and keep the best k by distance.
 *   2. Beyond long range? Compare string candidates with BigInteger or manual subtraction.
 *   3. Extra validity rules? Filter candidates and expand generation if needed.
 */
public class FindTheClosestPalindrome {

    public static void main(String[] args) {
        FindTheClosestPalindrome solver = new FindTheClosestPalindrome();
        String[] inputs = {"123", "1", "1234", "10"};
        String[] expected = {"121", "0", "1221", "9"};
        for (int i = 0; i < inputs.length; i++) {
            String got = solver.nearestPalindromic(inputs[i]);
            System.out.printf("n=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }


        /**
     * Intuition: the nearest palindrome comes from mirroring the prefix, except
     * near powers of ten where length-boundary palindromes matter. Trying the
     * prefix itself, prefix - 1, prefix + 1, and both boundaries covers the closest candidate.
     *
     * Algorithm:
     *   1. Handle single digits by returning num - 1.
     *   2. Add the lower and upper power-of-ten boundary palindromes.
     *   3. Mirror leftHalf - 1, leftHalf, and leftHalf + 1.
     *   4. Skip n itself and choose the smallest difference, breaking ties smaller.
     *
     * Time:  O(l) - a constant number of length-l candidates are built.
     * Space: O(l) - mirrored candidate strings are length proportional to n.
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

        /** Mirrors the left half into a full palindrome of the requested parity. */
    private long mirror(long half, boolean evenLen) {
        String left = String.valueOf(half);
        // For odd length palindromes, drop the last char before reversing (it's the middle)
        String toReverse = evenLen ? left : left.substring(0, left.length() - 1);
        String reversed = new StringBuilder(toReverse).reverse().toString();
        return Long.parseLong(left + reversed);
    }

    }


package strings;

/**
 * ✅ Problem: Time Needed to Rearrange a Binary String
 *
 * Each second, every occurrence of the substring "01" in `s` is
 * simultaneously replaced with "10". Return the number of seconds it
 * takes until `s` contains no "01" substring (i.e. all '1's are to the
 * left of all '0's).
 *
 * 🔗 Leetcode: https://leetcode.com/problems/time-needed-to-rearrange-a-binary-string/   (Medium)
 * 🏷️ Pattern:  String · Simulation · One-pass counting
 *
 * 🧪 Example:
 *   Input:  s = "0110101"
 *   Output: 4
 *   Trace:  0110101 → 1011010 → 1101100 → 1110100 → 1111000
 *
 * 🚧 Edge cases to remember:
 *   - already sorted ("1110000" or all same char) → 0
 *   - leading zeros only ("0001")                 → just shifts of one '1'
 *   - a '1' with no '0' to its right              → contributes nothing
 *   - empty / single char                         → 0
 *
 * 🔍 Follow-ups:
 *   1. Can you do it in O(n) without simulation? Yes — track zeros seen
 *      so far and use `finish = max(prevFinish + 1, zeros)` on each '1'.
 *   2. What if "10" → "01" instead? Mirror the scan from the right.
 *   3. What if k pairs swap per second? Divide the bottleneck count by k.
 *
 * 🔁 Related: Steps to Make Array Non-decreasing (2289), Bubble sort rounds.
 *
 *  Approach          Method                              Time     Space
 *  ----------------  ----------------------------------  -------  -----
 *  Simulation        secondsToRemoveOccurrencesBrute     O(n²)    O(n)
 *  One-pass (best)   secondsToRemoveOccurrences          O(n)     O(1)
 */
public class TimeNeededToRearrangeBinaryString {

    /**
     * 🧠 Intuition (brute): literally do what the problem says — keep
     * sweeping the string replacing every non-overlapping "01" with
     * "10" in one pass, until a pass changes nothing. Each pass is one
     * second.
     *
     * Algorithm:
     *   1. Convert to char[] for in-place edits.
     *   2. Sweep left→right; whenever chars[i]='0' and chars[i+1]='1',
     *      swap them and skip the next index (non-overlapping).
     *   3. If any swap happened in this pass, increment seconds; repeat.
     *   4. Stop when a pass makes zero swaps.
     *
     * Time:  O(n²) — up to n passes, each O(n).
     * Space: O(n) for the char buffer.
     *
     * @param str binary string
     * @return seconds until no "01" remains
     */
    public int secondsToRemoveOccurrencesBrute(String str) {
        if (str == null || str.length() < 2) return 0;

        char[] charArray = str.toCharArray();
        int seconds = 0;

        while (true) {
            boolean swapped = false;
            for (int i = 0; i < charArray.length - 1; i++) {
                if (charArray[i] == '0' && charArray[i + 1] == '1') {
                    charArray[i] = '1';
                    charArray[i + 1] = '0';
                    i++;            // non-overlapping: swaps happen simultaneously
                    swapped = true;
                }
            }
            if (!swapped) return seconds;
            seconds++;
        }
    }

    /**
     * 🧠 Intuition: imagine each '1' as a bubble that must travel left
     * past every '0' currently to its left. Per second a '1' shifts one
     * slot left — *unless* another '1' is directly in front of it (they
     * form a "11" block and only the leading one can move). So for each
     * '1' that has at least one '0' before it, its finish time is:
     *   - `zeros`           if nothing is blocking it, OR
     *   - `prevFinish + 1`  if the previous '1' would still be moving
     *                       at that time (then it has to wait one more
     *                       second behind it).
     * The answer is the finish time of the last such '1'.
     *
     * Algorithm:
     *   1. Scan left→right, maintain `zeros` = '0' count so far.
     *   2. For each '1' that has at least one '0' before it, compute
     *      its finish time as the max of "free travel" vs "waiting
     *      behind the previous '1'", and update `lastFinishTime`.
     *   3. '1's with no preceding '0' are already in place — skip.
     *   4. Return `lastFinishTime`.
     *
     * Time:  O(n)
     * Space: O(1)
     *
     * @param s binary string
     * @return seconds until no "01" remains
     */
    public int secondsToRemoveOccurrences(String s) {
        if (s == null || s.length() < 2) return 0;

        int zeros = 0;
        int lastFinishTime = 0;

        for (char c : s.toCharArray()) {
            if (c == '0') {
                zeros++;
            } else if (zeros > 0) {
                // Option 1: Pass through all zeros before this '1'
                int freeTravelFinish = zeros;

                // Option 2: Wait for the previous '1' to finish, then move
                int blockedFinish = lastFinishTime + 1;

                // The '1' finishes at whichever time is later
                lastFinishTime = Math.max(freeTravelFinish, blockedFinish);
            }
        }
        return lastFinishTime;
    }

    // ---------------------------------------------------------------------
    // Demo
    // ---------------------------------------------------------------------
    public static void main(String[] args) {
        TimeNeededToRearrangeBinaryString solver = new TimeNeededToRearrangeBinaryString();

        String[] inputs   = { "0110101", "11100",  "0001",  "1",   "01",   "0101010101" };
        int[]    expected = {        4,        0,       3,    0,     1,            9 };

        for (int i = 0; i < inputs.length; i++) {
            int brute  = solver.secondsToRemoveOccurrencesBrute(inputs[i]);
            int linear = solver.secondsToRemoveOccurrences(inputs[i]);
            System.out.printf("s=%-12s →  brute=%d  linear=%d  expected=%d%n",
                "\"" + inputs[i] + "\"", brute, linear, expected[i]);
        }
    }
}


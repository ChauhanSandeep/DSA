package strings.stack;

/**
 * 316. Remove Duplicate Letters
 *
 * Problem Statement:
 * Given a string s, remove duplicate letters so that every letter appears once and only once.
 * You must make sure your result is the smallest in lexicographical order among all possible results.
 *
 * Example:
 * Input:  s = "cbacdcbc"
 * Output: "acdb"
 *
 * Explanation:
 * - All distinct characters in "cbacdcbc" are: c, b, a, d.
 * - We want each of them to appear exactly once in the result.
 * - Among all subsequences that contain each of these characters exactly once,
 *   "acdb" is the lexicographically smallest.
 *
 * Approach (High-level):
 * - Optimal (Monotonic Stack + Last Occurrence):
 *   Iterate over the string while maintaining:
 *     - A stack to build the resulting subsequence.
 *     - A boolean array to track which characters are already in the stack.
 *     - An array of last occurrences for each character.
 *   For each character, if it is not already in the stack, we compare it with the
 *   top of the stack and pop from the stack while:
 *     - The top character is lexicographically larger than the current character, and
 *     - The top character appears later in the string (so we can still use it later).
 *   Push the current character to the stack and mark it as visited.
 *   Finally, form the result from the stack.
 *
 * - Alternative (Greedy Selection by Window + Counting):
 *   Repeatedly choose the smallest possible character that can be the next in the result while
 *   ensuring all remaining distinct characters can still appear later. Use a frequency array
 *   and a visited array to guide selection. This is also O(n) with a bit more logic complexity.
 *
 * LeetCode Link:
 * https://leetcode.com/problems/remove-duplicate-letters/
 *
 * Potential Follow-up Questions (FAANG-style) and Brief Answers:
 *
 * 1. Follow-up: What if the input can contain all ASCII characters, not just 'a' to 'z'?
 *    - Answer: Replace fixed-size arrays (length 26) with maps or arrays sized to the ASCII/Unicode range.
 *      For example, use int[128] for ASCII or a HashMap<Character, Integer> for generic Unicode.[web:7]
 *
 * 2. Follow-up: How would you handle a very large input stream that does not fit in memory?
 *    - Answer: If the stream is too large, you may need a multi-pass or chunk-based strategy.
 *      For strict lexicographical minimality, you typically need access to global last occurrences.
 *      In streaming, you would approximate or maintain partial indices via external storage.[web:2]
 *
 * 3. Follow-up: This problem is the same as "Smallest Subsequence of Distinct Characters" (LeetCode 1081).
 *    Can you generalize your solution to that problem?
 *    - Answer: The optimal monotonic stack solution directly applies. You simply use the same
 *      logic for any string of lowercase letters. The constraints and behavior are identical.[web:7]
 *
 * 4. Follow-up: Can we modify the solution to ensure stability with respect to some custom ordering
 *    (e.g., a custom comparator instead of standard lexicographical order)?
 *    - Answer: Yes. Replace direct character comparisons with calls to the custom comparator.
 *      The monotonic stack condition (top > current) becomes "top is greater than current by custom comparator".
 *
 * 5. Follow-up: How would you test this thoroughly?
 *    - Answer: Include tests for:
 *        - Single-character strings.
 *        - All characters identical.
 *        - Already strictly increasing sequences.
 *        - Strictly decreasing sequences.
 *        - Random strings with high duplication.
 *        - Edge lengths: minimum (length 1) and maximum allowed by constraints.[web:5]
 */
public class RemoveDuplicateLetters {

    /**
     * Optimal solution using a monotonic stack and last occurrence tracking.
     *
     * Algorithm:
     * 1. Edge case: if the string is null or empty, return it directly.
     * 2. Precompute the last occurrence index of each character.
     *    - Use an int array lastIndex[26] where lastIndex[c - 'a'] is the last index of character c.
     * 3. Maintain:
     *    - A stack-like structure (StringBuilder used as a stack) to store the current result.
     *    - A boolean array inResult[26] to mark which characters are already in the current result.
     * 4. Iterate over the characters of the string s by index i:
     *    - Let current = s.charAt(i).
     *    - If current is already inResult, skip it (we already used this character once).
     *    - Otherwise:
     *        a. While the "stack" is not empty AND
     *             - The last character in the stack is greater than current (lexicographically), AND
     *             - The last occurrence of that last character is after index i (meaning we can still use
     *               that character later),
     *           then pop that character from the stack and mark it as not inResult.
     *        b. Append current to the stack and mark it as inResult.
     * 5. Convert the stack to a string and return it.
     *
     * Time Complexity:
     * - O(n), where n is the length of s.
     *   Each character is pushed and popped at most once.
     *
     * Space Complexity:
     * - O(1) additional space (ignoring the output string) because:
     *   - lastIndex and inResult are fixed-size arrays of length 26.
     *   - The stack holds at most 26 distinct characters.
     *
     * This approach guarantees that:
     * - Each character appears once.
     * - The resulting sequence is lexicographically smallest among all valid subsequences.[web:5][web:2]
     *
     * @param input the input string consisting of lowercase English letters
     * @return the lexicographically smallest string with all distinct characters from s
     */
    public String removeDuplicateLetters(String input) {
        if (input == null || input.length() <= 1) {
            return input;
        }

        int length = input.length();
        int[] lastIndex = new int[26];

        // Compute last occurrence index for each character.
        for (int i = 0; i < length; i++) {
            char c = input.charAt(i);
            lastIndex[c - 'a'] = i;
        }

        StringBuilder resultStack = new StringBuilder();
        boolean[] visited = new boolean[26];

        for (int i = 0; i < length; i++) {
            char current = input.charAt(i);

            // If this character is already in the result, skip it.
            if (visited[current - 'a']) {
                continue;
            }

            // Maintain increasing lexicographical order in the stack.
            // If the top of the stack is greater than the current character and
            // the top character appears later in the string, we can safely remove it now
            // to make the string lexicographically smaller.
            while (resultStack.length() > 0) {
                char lastCharInResult = resultStack.charAt(resultStack.length() - 1);
                if (lastCharInResult <= current) {
                    break;
                }
                if (lastIndex[lastCharInResult - 'a'] <= i) {
                    // Cannot remove lastCharInResult as it will not appear later.
                    break;
                }
                // Pop from stack and mark as not in result.
                resultStack.deleteCharAt(resultStack.length() - 1);
                visited[lastCharInResult - 'a'] = false;
            }

            // Add current character to result and mark as inResult.
            resultStack.append(current);
            visited[current - 'a'] = true;
        }

        return resultStack.toString();
    }

    // Alternative solution using frequency counting and visited flags.
    // This method also uses a stack-like StringBuilder but relies on counting remaining characters
    // as we iterate, instead of precomputing last indices.
    public String removeDuplicateLettersAlternative(String input) {
        if (input == null || input.length() <= 1) {
            return input;
        }

        int[] frequency = new int[26];
        boolean[] visited = new boolean[26];

        // Count total frequency of each character.
        for (int i = 0; i < input.length(); i++) {
            frequency[input.charAt(i) - 'a']++;
        }

        StringBuilder resultStack = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char current = input.charAt(i);
            int index = current - 'a';

            // Decrease frequency since we are using/processing this character now.
            frequency[index]--;

            // If already in result, there is no need to process it again.
            if (visited[index]) {
                continue;
            }

            // Similar monotonic stack logic:
            // While last character in stack is greater than current and we still have more
            // occurrences of that last character later (frequency > 0), pop it to achieve
            // lexicographically smaller result.
            while (resultStack.length() > 0) {
                char lastCharInResult = resultStack.charAt(resultStack.length() - 1);
                int lastIndex = lastCharInResult - 'a';
                if (lastCharInResult <= current || frequency[lastIndex] == 0) {
                    break;
                }
                resultStack.deleteCharAt(resultStack.length() - 1);
                visited[lastIndex] = false;
            }

            resultStack.append(current);
            visited[index] = true;
        }

        return resultStack.toString();
    }

    // Simple main for basic sanity checks during local debugging.
    public static void main(String[] args) {
        RemoveDuplicateLetters solver = new RemoveDuplicateLetters();

        String input1 = "bcabc";
        String input2 = "cbacdcbc";
        String input3 = "aaaaa";
        String input4 = "abacb";

        System.out.println("Optimal:");
        System.out.println("Input: " + input1 + " -> Output: " + solver.removeDuplicateLetters(input1));
        System.out.println("Input: " + input2 + " -> Output: " + solver.removeDuplicateLetters(input2));
        System.out.println("Input: " + input3 + " -> Output: " + solver.removeDuplicateLetters(input3));
        System.out.println("Input: " + input4 + " -> Output: " + solver.removeDuplicateLetters(input4));

        System.out.println("Alternative:");
        System.out.println("Input: " + input1 + " -> Output: " + solver.removeDuplicateLettersAlternative(input1));
        System.out.println("Input: " + input2 + " -> Output: " + solver.removeDuplicateLettersAlternative(input2));
        System.out.println("Input: " + input3 + " -> Output: " + solver.removeDuplicateLettersAlternative(input3));
        System.out.println("Input: " + input4 + " -> Output: " + solver.removeDuplicateLettersAlternative(input4));
    }
}


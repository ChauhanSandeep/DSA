package strings.stack;

/**
 * Problem: Remove Duplicate Letters
 *
 * Remove duplicate letters so every distinct letter appears exactly once, while
 * producing the lexicographically smallest possible subsequence among all valid
 * choices.
 *
 * Leetcode: https://leetcode.com/problems/remove-duplicate-letters/ (Medium)
 * Rating:   no contest Elo (pre-contest problem)
 * Pattern:  Monotonic stack | Last occurrence | Greedy subsequence
 *
 * Example:
 *   Input:  s = "cbacdcbc"
 *   Output: "acdb"
 *   Why:    all distinct letters remain once, and larger removable prefixes are popped before smaller letters.
 *
 * Follow-ups:
 *   1. Support all ASCII characters?
 *      Use arrays of size 128 or maps instead of 26-entry lowercase arrays.
 *   2. Use a custom character order?
 *      Replace direct character comparison with the custom rank comparison.
 *   3. Process input too large for memory?
 *      You still need future occurrence knowledge, so precompute or externally store last positions.
 *
 * Related: Smallest Subsequence of Distinct Characters (1081), Create Maximum Number (321).
 */
public class RemoveDuplicateLetters {

    /**
     * Intuition: build the answer like a stack. If the current character is smaller
     * than the stack top and that top appears again later, popping it makes the
     * prefix smaller without losing the ability to include every distinct letter.
     *
     * Algorithm:
     *   1. Return input directly for null or length at most one.
     *   2. Record the last index where each character appears.
     *   3. Scan characters, skipping those already in the result stack.
     *   4. Pop larger stack characters that appear later, then append the current character.
     *
     * Time:  O(n) - each character is pushed and popped at most once.
     * Space: O(1) - fixed arrays and at most 26 stack characters are used.
     *
     * @param input lowercase string to deduplicate
     * @return lexicographically smallest subsequence containing each distinct letter once
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
        String[] inputs = {"bcabc", "cbacdcbc", "aaaaa", "abacb"};
        String[] expected = {"abc", "acdb", "a", "abc"};

        for (int i = 0; i < inputs.length; i++) {
            String got = solver.removeDuplicateLetters(inputs[i]);
            System.out.printf("s=%s -> %s  expected=%s%n", inputs[i], got, expected[i]);
        }
    }

}


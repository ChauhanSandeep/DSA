package String;

/**
 * LeetCode Problem: https://leetcode.com/problems/count-and-say/
 *
 * The "Count and Say" sequence is a series of strings where each term is generated 
 * based on the description of the previous term.
 *
 * Intuition:
 * - Start with "1" as the base case.
 * - For each iteration, read the previous sequence and construct the next sequence.
 * - Count consecutive identical digits and describe them in the new sequence.
 *
 * Algorithm:
 * 1. Initialize the sequence with "1".
 * 2. Iterate up to `n` to generate subsequent terms.
 * 3. Use a loop to read characters from the previous term and build the new term.
 * 4. Maintain a count of consecutive characters and append to the new term accordingly.
 *
 * Time Complexity: O(N * M), where N is the input number and M is the average length of sequences.
 * Space Complexity: O(M), since we store the sequence in a StringBuilder.
 */
public class CountAndSay {
    public static void main(String[] args) {
        CountAndSay solver = new CountAndSay();
        System.out.println(solver.countAndSay(4));  // Output: "1211"
    }

    public String countAndSay(int n) {
        if (n <= 0) return "";  // Edge case

        StringBuilder currentSequence = new StringBuilder("1");

        for (int i = 1; i < n; i++) {
            currentSequence = generateNextSequence(currentSequence);
        }

        return currentSequence.toString();
    }

    private StringBuilder generateNextSequence(StringBuilder previousSequence) {
        StringBuilder nextSequence = new StringBuilder();
        int count = 1;
        char digit = previousSequence.charAt(0);

        for (int i = 1; i < previousSequence.length(); i++) {
            if (previousSequence.charAt(i) == digit) {
                count++;  // Increment count if same character repeats
            } else {
                nextSequence.append(count).append(digit);
                digit = previousSequence.charAt(i);
                count = 1;
            }
        }

        // Append the last counted character group
        nextSequence.append(count).append(digit);
        return nextSequence;
    }
}

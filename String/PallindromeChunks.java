package String;

/**
 * Problem: Longest Possible Chunked Palindrome
 * https://www.geeksforgeeks.org/longest-possible-chunked-palindrome/
 *
 * Given a string, determine the maximum number of palindromic chunks it can be split into.
 *
 * Approach:
 * - Use recursion to find the longest matching prefix and suffix.
 * - When a match is found, recursively check the remaining middle part.
 * - Base case: If no match is found, the entire string is one chunk.
 *
 * Time Complexity: O(n) (Each character is processed once)
 * Space Complexity: O(n) (Recursive stack depth)
 */
public class PalindromeChunks {

    public static void main(String[] args) {
        System.out.println("Max chunks (Recursive): " + maxChunksRecursive("apple bus machine bus apple"));
        System.out.println("Max chunks (Recursive): " + maxChunksRecursive("volvo"));
    }

    /**
     * Recursive approach to find max palindromic chunks.
     */
    public static int maxChunksRecursive(String str) {
        if (str == null || str.isEmpty()) return 0;
        return findChunks(str, 0, str.length());
    }

    private static int findChunks(String str, int left, int right) {
        int n = right - left;
        if (n == 0) return 0; // Base case: empty string
        if (n == 1) return 1; // Single character is a chunk

        for (int len = 1; len <= n / 2; len++) {
            // Extract prefix and suffix
            String prefix = str.substring(left, left + len);
            String suffix = str.substring(right - len, right);

            if (prefix.equals(suffix)) {
                return 2 + findChunks(str, left + len, right - len);
            }
        }

        // If no chunks found, treat the whole string as 1 chunk

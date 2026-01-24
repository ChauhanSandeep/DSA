package dynamicprogramming.stringmatching;

/**
 * Distinct Subsequences
 *
 * Problem Statement:
 * Given two strings s and t, return the number of distinct subsequences of s which equals t.
 * A string's subsequence is a new string formed from the original string by deleting some
 * (can be none) of the characters without disturbing the relative positions of the remaining characters.
 *
 * Example:
 * Input: s = "rabbbit", t = "rabbit"
 * Output: 3
 * Explanation: There are 3 ways you can generate "rabbit" from s.
 * - rabb b it (using 1st b)
 * - rab b bit (using 2nd b)
 * - rabb b it (using 3rd b)
 *
 * LeetCode: https://leetcode.com/problems/distinct-subsequences/
 *
 * Follow-up Questions for FAANG Interviews:
 * 1. How to optimize space complexity? - Use 1D DP array instead of 2D matrix
 * 2. Handle integer overflow for large counts? - Use BigInteger or modular arithmetic
 * 3. Find actual subsequences instead of just count? - Maintain path tracking during DP
 * 4. What if strings are very large? - Consider rolling hash or suffix array optimizations
 * 5. Case-insensitive matching? - Normalize strings before processing
 * LeetCode Contest Rating: Not available (not a contest problem)
 */
public class DistinctSubsequence {

    public static void main(String[] args) {
        DistinctSubsequence solution = new DistinctSubsequence();
        String sourceString = "rabbbit";
        String targetString = "rabbit";

        System.out.println("Memoization Result: " +
            solution.numDistinctMemoization(sourceString, targetString)); // Output: 3
        System.out.println("Dynamic Programming Result: " +
            solution.numDistinctDP(sourceString, targetString)); // Output: 3
        System.out.println("Space Optimized Result: " +
            solution.numDistinctSpaceOptimized(sourceString, targetString)); // Output: 3
    }

    /**
     * Finds distinct subsequences using top-down dynamic programming with memoization
     *
     * Algorithm: Recursive Dynamic Programming with Memoization
     * Steps:
     * 1. Use recursion to explore all possible character matching combinations
     * 2. For each position, decide whether to match current character or skip it
     * 3. Use memoization to cache results for overlapping subproblems
     * 4. Base cases: target fully matched (return 1) or source exhausted (return 0)
     *
     * Time Complexity: O(m * n) where m = target length, n = source length
     * Space Complexity: O(m * n) for memoization table + O(m + n) for recursion stack
     *
     * @param sourceString the string to search in
     * @param targetString the subsequence to find
     * @return number of distinct ways to form target from source
     */
    public int numDistinctMemoization(String sourceString, String targetString) {
        // Handle edge cases
        if (targetString.length() > sourceString.length()) {
            return 0;
        }
        if (targetString.isEmpty()) {
            return 1;
        }

        // Initialize memoization table with -1 (uncomputed state)
        int[][] dp = new int[sourceString.length()][targetString.length()];
        for (int i = 0; i < sourceString.length(); i++) {
            for (int j = 0; j < targetString.length(); j++) {
                dp[i][j] = -1;
            }
        }
        
        return recHelper(sourceString, targetString, 0, 0, dp);
    }

    /**
     * Recursive helper method to count distinct subsequences with memoization
     */
    private int recHelper(String sourceString, String targetString, int sourceIndex, int targetIndex,
        int[][] dp) {
        // Base case: target string fully matched
        if (targetIndex == targetString.length()) {
            return 1;
        }

        // Base case: source string exhausted but target not fully matched
        if (sourceIndex == sourceString.length()) {
            return 0;
        }

        // Check if result already computed
        if (dp[sourceIndex][targetIndex] != -1) {
            return dp[sourceIndex][targetIndex];
        }

        int count = 0;

        // If current characters match, we have choice to use this match or skip it
        if (sourceString.charAt(sourceIndex) == targetString.charAt(targetIndex)) {
            // Option 1: Use this character match and advance both pointers
            count += recHelper(sourceString, targetString, sourceIndex + 1, targetIndex + 1, dp);
        }

        // Option 2: Skip current character in source string (always available)
        count += recHelper(sourceString, targetString, sourceIndex + 1, targetIndex, dp);

        // Cache the result for future use
        dp[sourceIndex][targetIndex] = count;
        return count;
    }

    /**
     * Finds distinct subsequences using bottom-up dynamic programming
     *
     * Algorithm: Bottom-up Dynamic Programming
     * Steps:
     * 1. Create 2D DP table where dp[i][j] = ways to form target[0..i-1] using source[0..j-1]
     * 2. Initialize base case: empty target can be formed in 1 way from any prefix
     * 3. Fill table: if characters match, add both use and skip options
     * 4. If characters don't match, only skip option is available
     *
     * Time Complexity: O(m * n) where m = target length, n = source length
     * Space Complexity: O(m * n) for the DP table
     *
     * @param sourceString the string to search in
     * @param targetString the subsequence to find
     * @return number of distinct ways to form target from source
     */
    public int numDistinctDP(String sourceString, String targetString) {
        int targetLength = targetString.length();
        int sourceLength = sourceString.length();

        // Handle edge cases
        if (targetLength > sourceLength) {
            return 0;
        }
        if (targetLength == 0) {
            return 1;
        }

        // dp[i][j] represents number of ways to form target[0..i-1] using source[0..j-1]
        int[][] dp = new int[targetLength + 1][sourceLength + 1];

        // Base case: empty target string can be formed in exactly 1 way
        // by deleting all characters from any prefix of source
        for (int sourceIndex = 0; sourceIndex <= sourceLength; sourceIndex++) {
            dp[0][sourceIndex] = 1;
        }

        // Fill the DP table using bottom-up approach
        for (int targetIndex = 1; targetIndex <= targetLength; targetIndex++) {
            for (int sourceIndex = 1; sourceIndex <= sourceLength; sourceIndex++) {
                char targetChar = targetString.charAt(targetIndex - 1);
                char sourceChar = sourceString.charAt(sourceIndex - 1);

                if (targetChar == sourceChar) {
                    // Characters match: we can either use this match or skip source character
                    dp[targetIndex][sourceIndex] = dp[targetIndex - 1][sourceIndex - 1] + // use the match
                                                   dp[targetIndex][sourceIndex - 1];     // skip source char
                } else {
                    // Characters don't match: only option is to skip source character
                    dp[targetIndex][sourceIndex] = dp[targetIndex][sourceIndex - 1];
                }
            }
        }

        return dp[targetLength][sourceLength];
    }

    /**
     * Space-optimized version using 1D array instead of 2D table
     *
     * Algorithm: Space-Optimized Dynamic Programming
     * Steps:
     * 1. Use 1D array since we only need previous row values
     * 2. Process from right to left to avoid overwriting needed values
     * 3. Apply same logic as 2D version but with optimized space usage
     *
     * Time Complexity: O(m * n)
     * Space Complexity: O(n) - only one array needed
     */
    public int numDistinctSpaceOptimized(String sourceString, String targetString) {
        int targetLength = targetString.length();
        int sourceLength = sourceString.length();

        if (targetLength > sourceLength) return 0;
        if (targetLength == 0) return 1;

        // Use 1D array where dp[j] represents ways to form target using source[0..j-1]
        int[] distinctWaysArray = new int[sourceLength + 1];

        // Initialize: empty target can be formed in 1 way
        for (int j = 0; j <= sourceLength; j++) {
            distinctWaysArray[j] = 1;
        }

        // Process each character in target
        for (int targetIndex = 1; targetIndex <= targetLength; targetIndex++) {
            int previousDiagonal = distinctWaysArray[0];
            distinctWaysArray[0] = 0; // Empty source cannot form non-empty target

            for (int sourceIndex = 1; sourceIndex <= sourceLength; sourceIndex++) {
                int currentValue = distinctWaysArray[sourceIndex];

                if (targetString.charAt(targetIndex - 1) == sourceString.charAt(sourceIndex - 1)) {
                    distinctWaysArray[sourceIndex] = previousDiagonal + distinctWaysArray[sourceIndex - 1];
                } else {
                    distinctWaysArray[sourceIndex] = distinctWaysArray[sourceIndex - 1];
                }

                previousDiagonal = currentValue;
            }
        }

        return distinctWaysArray[sourceLength];
    }
}
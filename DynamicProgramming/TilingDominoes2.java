package DynamicProgramming;

/**
 * Problem: Find the number of ways to tile a 3 x N board using 2 x 1 dominoes.
 * 
 * **Approach: Dynamic Programming with Bitmasking (State Compression)**
 * - The possible states are represented using a **3-bit mask** (0-7) where:
 *   - `1` means a cell is occupied.
 *   - `0` means a cell is empty.
 * - Recurrence relation tracks how valid states transition between columns.
 * 
 * **Time Complexity:** O(N) → Iterates once through `N`.  
 * **Space Complexity:** O(1) → Uses only two 1D arrays instead of a full DP table.  
 * 
 * **Reference:** https://www.youtube.com/watch?v=yn2jnmlepY8
 */
public class TilingDominoes2 {

    private static final int MOD = 1_000_000_007; // 10^9 + 7

    public static void main(String[] args) {
        int n = 50;
        System.out.println("Ways to tile a 3 x " + n + " board: " + countWays(n));
    }

    public static int countWays(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 0;  // 3x1 board cannot be fully covered

        int[] prev = new int[8]; // State for (i-1)
        int[] curr = new int[8]; // State for i
        prev[7] = 1; // Base case: fully filled (000 -> 111)

        for (int i = 1; i <= n; i++) {
            // Reset current state
            curr = new int[8];

            // State transitions based on bitmask representation
            curr[0] = prev[7];                          // 000 -> 111 (placing 3 horizontal dominos)
            curr[1] = prev[6];                          // 110 -> 001
            curr[2] = prev[5];                          // 101 -> 010
            curr[3] = (prev[4] + prev[7]) % MOD;        // 100 -> 011, 111 -> 011 (stacking vertically)
            curr[4] = prev[3];                          // 011 -> 100
            curr[5] = prev[2];                          // 010 -> 101
            curr[6] = (prev[1] + prev[7]) % MOD;        // 001 -> 110, 111 -> 110 (stacking vertically)
            curr[7] = ((prev[3] + prev[6]) % MOD + prev[0]) % MOD; // 011 -> 111, 110 -> 111, 000 -> 111

            // Move current state to previous for the next iteration
            prev = curr;
        }

        return prev[7]; // Fully filled state at the last column
    }
}

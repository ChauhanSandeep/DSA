package Graph;

import java.util.Arrays;
import java.util.Queue;
import java.util.ArrayDeque;

/**
 * Find the smallest multiple of a given number using only the digits 0 and 1.
 * 
 * ### **Approach:**
 * - We use **Breadth-First Search (BFS)** to explore all possible numbers formed by 0s and 1s.
 * - Each number formed is represented by its remainder when divided by `num`.
 * - We track remainders using a queue and an array (`digitMap`) to store the last digit used.
 * - When remainder `0` is found, we backtrack to construct the valid multiple.
 * 
 * ### **Complexity Analysis:**
 * - **Time Complexity:** `O(N)`, where `N` is the given number. Each remainder (0 to N-1) is processed once.
 * - **Space Complexity:** `O(N)`, as we store `N` remainders and parent tracking.
 * 
 * **LeetCode Problem Reference:** (Not available, but inspired by similar BFS number construction problems)
 * YouTube Explanation: https://www.youtube.com/watch?v=Om47LiGTy8o
 */
public class SmallestMultiple {

    public static void main(String[] args) {
        int num = 7;
        String smallestMultiple = findSmallestMultiple(num);
        System.out.println("Smallest multiple of " + num + " using only 0s and 1s: " + smallestMultiple);
    }

    /**
     * Uses BFS to find the smallest multiple of `num` that consists only of digits 0 and 1.
     *
     * @param num The given number for which we need to find the smallest multiple.
     * @return The smallest multiple as a string.
     */
    public static String findSmallestMultiple(int num) {
        if (num == 1) return "1"; // Base case, smallest multiple of 1 is 1 itself.

        Queue<Integer> queue = new ArrayDeque<>();
        char[] digitMap = new char[num]; // Stores '0' or '1' for each remainder.
        int[] parentMap = new int[num]; // Tracks parent remainder for backtracking.

        // Initialize tracking arrays
        Arrays.fill(digitMap, '2'); // Mark uninitialized states
        Arrays.fill(parentMap, -1);

        // Start BFS from remainder 1 (which represents the number "1")
        queue.offer(1);
        digitMap[1] = '1';

        while (!queue.isEmpty()) {
            int remainder = queue.poll();
            if (remainder == 0) break; // If remainder is 0, we found a valid multiple.

            // Generate new remainders by appending '0' or '1' and taking modulo `num`
            int remainderWith0 = (remainder * 10) % num;
            int remainderWith1 = (remainder * 10 + 1) % num;

            // If new remainder hasn't been visited, enqueue and track it.
            if (digitMap[remainderWith0] == '2') {
                digitMap[remainderWith0] = '0';
                parentMap[remainderWith0] = remainder;
                queue.offer(remainderWith0);
            }
            if (digitMap[remainderWith1] == '2') {
                digitMap[remainderWith1] = '1';
                parentMap[remainderWith1] = remainder;
                queue.offer(remainderWith1);
            }
        }

        // Backtrack to reconstruct the smallest multiple
        StringBuilder result = new StringBuilder();
        for (int rem = 0; rem != -1; rem = parentMap[rem]) {
            result.insert(0, digitMap[rem]);
        }

        return result.toString();
    }
}

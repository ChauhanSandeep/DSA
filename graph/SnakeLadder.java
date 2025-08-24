package graph;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * **Snake and Ladder - Minimum Dice Rolls to Reach 100**
 *
 * ### **Problem Statement:**
 * Given a **Snake and Ladder board** (1 to 100), determine the **minimum number of dice rolls**
 * required to reach **square 100** starting from square **1**.
 *
 * ### **Approach:**
 * - **Breadth-First Search (BFS)** is used to explore the shortest path.
 * - The board is represented as a `moves[]` array where:
 *   - `moves[i] = j` means there's a **ladder** (jump to `j`) or a **snake** (fall to `j`).
 *   - `moves[i] = -1` means normal progression (`i` remains `i`).
 * - We use a queue to process positions level by level.
 * - If we reach position **100**, we return the number of rolls taken.
 *
 * ### **Complexity Analysis:**
 * - **Time Complexity:** `O(100) = O(1)`, since we process at most **100** board positions.
 * - **Space Complexity:** `O(100) = O(1)`, as we store board state in an array.
 *
 * **LeetCode Reference:** (No exact match, but inspired by similar shortest path problems)
 */
public class SnakeLadder {

    public static void main(String[] args) {
        int[][] ladders = {
            {2, 15}, {5, 7}, {9, 27}, {18, 29}, {25, 35}
        };
        int[][] snakes = {
            {17, 4}, {20, 6}, {34, 12}, {24, 16}, {32, 30}
        };
        int minRolls = findMinDiceRolls(ladders, snakes);
        System.out.println("Minimum dice rolls to reach 100: " + minRolls);
    }

    /**
     * **Finds the minimum dice rolls to reach square 100 in Snake and Ladder game.**
     *
     * @param ladders 2D array where ladders[i] = {start, end} represents a ladder from `start` to `end`.
     * @param snakes  2D array where snakes[i] = {start, end} represents a snake from `start` to `end`.
     * @return Minimum dice rolls required to reach square 100, or -1 if unreachable.
     */
    public static int findMinDiceRolls(int[][] ladders, int[][] snakes) {
        int[] board = new int[101]; // Board representation
        Arrays.fill(board, -1); // Default: No ladder/snake at a position

        // Populate board with ladders
        for (int[] ladder : ladders) {
            board[ladder[0]] = ladder[1]; // Ladder from ladder[0] to ladder[1]
        }

        // Populate board with snakes
        for (int[] snake : snakes) {
            board[snake[0]] = snake[1]; // Snake from snake[0] to snake[1]
        }

        // BFS initialization
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[101];

        queue.offer(1); // Start at square 1
        visited[1] = true;

        int diceRolls = 0;

        // BFS traversal
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int currPosition = queue.poll();

                // If we reach position 100, return number of rolls taken
                if (currPosition == 100) return diceRolls;

                // Explore all dice roll outcomes (1 to 6)
                for (int dice = 1; dice <= 6; dice++) {
                    int nextPosition = currPosition + dice;

                    if (nextPosition > 100) continue; // Skip invalid positions

                    // If there's a ladder or snake, jump to the destination
                    if (board[nextPosition] != -1) {
                        nextPosition = board[nextPosition];
                    }

                    // If not visited, mark as visited and enqueue
                    if (!visited[nextPosition]) {
                        visited[nextPosition] = true;
                        queue.offer(nextPosition);
                    }
                }
            }
            diceRolls++; // Increment roll count after each BFS level
        }

        return -1; // If no path to 100
    }
}

package graphs;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Problem: Snake and Ladder Minimum Dice Rolls
 *
 * On a classic board numbered 1 through 100, ladders move you upward and snakes
 * move you downward after a dice roll. Starting at square 1, return the minimum
 * number of dice rolls needed to reach square 100.
 *
 * Pattern:  Graph | BFS on board positions | Unweighted shortest path
 *
 * Example:
 *   Input:  ladders = [[2,100]], snakes = []
 *   Output: 1
 *   Why:    rolling a 1 reaches square 2, then the ladder immediately moves the
 *           player to square 100.
 *
 * Follow-ups:
 *   1. The board size is not fixed at 100?
 *      Pass the target square as a parameter and size the arrays from it.
 *   2. The dice has a different number of sides?
 *      Replace the 1..6 transition loop with 1..diceSides.
 *   3. Return the chosen dice rolls?
 *      Store parent square and die roll for each visited square.
 *
 * Related: Snakes and Ladders (909), Minimum Jumps to Reach Home (1654).
 */
public class SnakeLadder {

    public static void main(String[] args) {
        int[][][] ladders = {{{2, 100}}, {}};
        int[][][] snakes = {{}, {}};
        int[] expected = {1, 17};
        for (int i = 0; i < ladders.length; i++) {
            int output = findMinDiceRolls(ladders[i], snakes[i]);
            System.out.printf("ladders=%s snakes=%s -> %d  expected=%d%n", Arrays.deepToString(ladders[i]), Arrays.deepToString(snakes[i]), output, expected[i]);
        }
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

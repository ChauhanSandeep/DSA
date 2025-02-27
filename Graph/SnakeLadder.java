package Graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Find the minimum dice rolls in which we can reach from 1 to 100 in the snake and ladder game.
 */
public class SnakeLadder {
  public static void main(String[] args) {
    int[][] ladders = {
        {2, 15}, {5, 7}, {9, 27}, {18, 29}, {25, 35}
    };
    int[][] snakes = {
        {17, 4}, {20, 6}, {34, 12}, {24, 16}, {32, 30}
    };
    System.out.println(new SnakeLadder().snakeLadder(ladders, snakes));
  }

  public int snakeLadder(int[][] ladders, int[][] snakes) {
    int[] moves = new int[101];
    for (int i = 0; i <= 100; i++) moves[i] = -1; // Initialize to -1 for better checking

    for (int[] ladder : ladders) {
      moves[ladder[0]] = ladder[1];
    }
    for (int[] snake : snakes) {
      moves[snake[0]] = snake[1];
    }

    boolean[] visited = new boolean[101];
    Queue<Integer> queue = new LinkedList<>();
    queue.offer(1);
    visited[1] = true; // Mark start as visited

    int level = 0;
    while (!queue.isEmpty()) {
      int size = queue.size();
      for (int i = 0; i < size; i++) {
        int curr = queue.poll();
        if (curr == 100) return level;

        for (int j = 1; j <= 6; j++) {
          int next = curr + j;
          if (next > 100) continue;

          if (moves[next] != -1) {
            next = moves[next]; // If there's a ladder/snake, jump to destination
          }

          if (!visited[next]) {
            visited[next] = true;
            queue.offer(next);
          }
        }
      }
      level++;
    }
    return -1; // If no way to reach 100
  }
}
package Graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Find the minimum dice rolls in which we can reach from 1 to 100 in snake and ladder game
 */
public class SnakeLadder {
    public static void main(String[] args) {
        int[][] ladders = {
        };
        int[][] snakes = {
        };
        System.out.println(new SnakeLadder().snakeLadder(ladders, snakes));
    }

    public int snakeLadder(int[][] ladders, int[][] snakes) {
        int[] moves = new int[101];
        for (int[] l : ladders) {
            moves[l[0]] = l[1];
        }

        for (int[] s : snakes) {
            moves[s[0]] = s[1];
        }

        boolean[] visited = new boolean[101];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(1);

        int level = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int curr = queue.poll();
                if (curr == 100) {
                    return level;
                } else if (moves[curr] != 0) {
                    int next = moves[curr];
                    if (next <= 100 && !visited[next]) {
                        visited[next] = true;
                        queue.offer(next);
                    }
                } else {
                    for (int j = 1; j <= 6; j++) {
                        int next = curr + j;
                        if (next <= 100 && !visited[next]) {
                            visited[next] = true;
                            queue.offer(next);
                        }
                    }
                }
            }
            level++;
        }
        return -1;

    }
}

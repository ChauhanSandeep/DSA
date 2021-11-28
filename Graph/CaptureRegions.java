package Graph;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Capture regions 'O' surrounded by all 'X'
 * https://www.interviewbit.com/problems/capture-regions-on-board/
 */
public class CaptureRegions {

    public static void main(String[] args) {
        /**
         'O', 'O', 'O', 'X', 'X', 'X', 'O',
         'X', 'X', 'X', 'O', 'O', 'O', 'O',
         'X', 'X', 'O', 'X', 'O', 'X', 'O',
         'O', 'X', 'O', 'X', 'O', 'X', 'O',
         'X', 'X', 'O', 'X', 'O', 'X', 'X',
         'X', 'O', 'O', 'O', 'X', 'X', 'O',
         'O', 'X', 'X', 'O', 'X', 'O', 'O',
         'O', 'X', 'O', 'O', 'X', 'O', 'X'
         */
        Character[][] matrix = {
                {'O', 'O', 'O', 'X', 'X', 'X', 'O'},
                {'X', 'X', 'X', 'O', 'O', 'O', 'O'},
                {'X', 'X', 'O', 'X', 'O', 'X', 'O'},
                {'O', 'X', 'O', 'X', 'O', 'X', 'O'},
                {'X', 'X', 'O', 'X', 'O', 'X', 'X'},
                {'X', 'O', 'O', 'O', 'X', 'X', 'O'},
                {'O', 'X', 'X', 'O', 'X', 'O', 'O'},
                {'O', 'X', 'O', 'O', 'X', 'O', 'X'}
        };
        ArrayList<ArrayList<Character>> input = new ArrayList<>();
        for (Character[] list : matrix) {
            input.add((ArrayList<Character>) Stream.of(list).collect(Collectors.toList()));
        }
        new CaptureRegions().solve(input);
        System.out.println(input);
    }

    public void solve(ArrayList<ArrayList<Character>> input) {
        if (input == null || input.size() == 0 || input.get(0).size() == 0) return;

        int rows = input.size();
        int cols = input.get(0).size();
        boolean[][] visited = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (input.get(i).get(j) == 'O' && !visited[i][j]) {
                    if (doCapture(input, i, j, visited)) {
                        capture(input, i, j);
                    }
                }
            }
        }

    }

    public boolean doCapture(ArrayList<ArrayList<Character>> input, int i, int j, boolean[][] visited) {
        if (i < 0 || j < 0 || i >= input.size() || j >= input.get(0).size()) return false;
        if (visited[i][j] || input.get(i).get(j) == 'X') return true;
        visited[i][j] = true;

        boolean down = doCapture(input, i + 1, j, visited);
        boolean up = doCapture(input, i - 1, j, visited);
        boolean right = doCapture(input, i, j + 1, visited);
        boolean left = doCapture(input, i, j - 1, visited);
        boolean capture = down && up && left && right;
        System.out.println("capture for " + i + " " + j + ": " + capture);
        return capture;
    }

    public void capture(ArrayList<ArrayList<Character>> input, int i, int j) {
        if (i < 0 || j < 0 || i >= input.size() || j >= input.get(0).size() || input.get(i).get(j) == 'X') return;
        input.get(i).set(j, 'X');

        capture(input, i + 1, j);
        capture(input, i - 1, j);
        capture(input, i, j + 1);
        capture(input, i, j - 1);
    }
}

package DynamicProgramming;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Given a matrix denoting chess board, containing few Queens. For each cell
 * A[i][j] find the number of queens that can attack cell (i, j). While calculating
 * answer for cell (i, j), assume there is no queen at that cell.
 */
public class QueenAttack {

    public static void main(String[] args) {
        ArrayList<String> lists = (ArrayList<String>) Stream.of(
                "010",
                "100",
                "001").collect(Collectors.toList());
        ArrayList<ArrayList<Integer>> result = new QueenAttack().queenAttack(lists);
        System.out.println(result);
    }

    int[] dx = {0, -1, -1, -1, 0, 1, 1, 1};
    int[] dy = {1, 1, 0, -1, -1, -1, 0, 1};
    int rows;
    int cols;

    public ArrayList<ArrayList<Integer>> queenAttack(ArrayList<String> lists) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        if (lists == null || lists.size() == 0) return result;

        this.rows = lists.size();
        this.cols = lists.get(0).length();
        for (int i = 0; i < rows; i++) {
            result.add(listOfSize(cols));
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (lists.get(i).charAt(j) == '1') {
                    for(int dir=0; dir<8; dir++) {
                        markPositions(lists, result, i + dx[dir], j + dy[dir], dir);
                    }
                }
            }
        }

        return result;
    }

    private void markPositions(ArrayList<String> lists, ArrayList<ArrayList<Integer>> result, int i, int j, int dir) {
        if (i >= rows || i < 0 || j >= cols || j < 0) return;
        result.get(i).set(j, result.get(i).get(j) + 1);
        if (lists.get(i).charAt(j) == '1') {
            return;
        }
        markPositions(lists, result, i + dx[dir], j + dy[dir], dir);
    }

    private ArrayList<Integer> listOfSize(int size) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(0);
        }
        return list;
    }
}

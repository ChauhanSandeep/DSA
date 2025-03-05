package Graph;

import java.util.*;

public class TicTacToeEndStates {

    public static void main(String[] args) {
        System.out.println("Total end states: " + new TicTacToeEndStates().countEndStates(3));
    }

    private final Set<String> result = new HashSet<>();

    public int countEndStates(int size) {
        String board = encode(size);
        Set<String> visited = new HashSet<>();

        int count = findRec(board, 0, visited, 1, size); // Start with player 'X'
        System.out.println("End states:\n" + result);
        return count;
    }

    private int findRec(String board, int marked, Set<String> visited, int player, int size) {
        if (visited.contains(board)) return 0;
        if (isEndState(board, size)) {
            result.add(board);
            return 1;
        }
        if (marked == size * size) return 0; // No more moves left

        List<String> nextStates = getNextStates(board, player);
        int sum = 0;
        for (String nextState : nextStates) {
            sum += findRec(nextState, marked + 1, visited, -player, size);
            visited.add(nextState);
        }
        visited.add(board);
        return sum;
    }

    public boolean isEndState(String board, int size) {
        int[] rowSum = new int[size];
        int[] colSum = new int[size];
        int diagSum = 0, antiDiagSum = 0;
        int totalCount = 0;

        for (int i = 0; i < size * size; i++) {
            char c = board.charAt(i);
            if (c == 'x') {
                rowSum[i / size]++;
                colSum[i % size]++;
                if (i / size == i % size) diagSum++; // Main diagonal
                if (i / size + i % size == size - 1) antiDiagSum++; // Anti-diagonal
                totalCount++;
            } else if (c == 'o') {
                rowSum[i / size]--;
                colSum[i % size]--;
                if (i / size == i % size) diagSum--;
                if (i / size + i % size == size - 1) antiDiagSum--;
                totalCount++;
            }
        }

        // Check if any row, column, or diagonal has an absolute sum equal to size (winner)
        for (int sum : rowSum) if (Math.abs(sum) == size) return true;
        for (int sum : colSum) if (Math.abs(sum) == size) return true;
        if (Math.abs(diagSum) == size || Math.abs(antiDiagSum) == size) return true;

        // If board is full, it's an end state (draw)
        return totalCount == size * size;
    }

    private List<String> getNextStates(String board, int player) {
        char marker = (player == 1) ? 'x' : 'o';
        List<String> nextStates = new ArrayList<>();
        for (int i = 0; i < board.length(); i++) {
            if (board.charAt(i) == '-') {
                nextStates.add(board.substring(0, i) + marker + board.substring(i + 1));
            }
        }
        return nextStates;
    }

    /**
     * Encode an empty board of size `size x size` as a string
     * @param size Board size
     * @return Encoded string representing an empty board
     * Example: encode(3) -> "---" (3x3 empty board)
     */
    private String encode(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size * size; i++) {
            sb.append("-");
        }
        return sb.toString();
    }
}

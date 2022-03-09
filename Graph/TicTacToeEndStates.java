package Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Find all the end states of tic-tac-toe game of size n
 * End state is state where either there is winner or draw
 */
public class TicTacToeEndStates {

    public static void main(String[] args) {
        System.out.println(new TicTacToeEndStates().countEndStates(3));

    }
    Set<String> result;

    public int countEndStates(int size) {
        String board = encode(size);
        this.result = new HashSet<>();

        int marked = 0;
        Set<String> visited = new HashSet<>();

        int count = findRec(board, marked, visited, -1, size);
        System.out.println(result);
        return count;
    }

    private int findRec(String board, int marked, Set<String> visited, int player, int size) {
        if(visited.contains(board)) return 0;
        if(isEndState(board, size)) {
            result.add(board);
            return 1;
        }
        if(marked == size*size) return 0;

        List<String> nextStates = getNextStates(board, player);
        int sum = 0;
        for(String nextState: nextStates) {
            sum += findRec(nextState, marked+1, visited, -player, size);
            visited.add(nextState);
        }
        visited.add(board);
        return sum;
    }

    public boolean isEndState(String board, int size) {
        int[] rowSum = new int[size];
        int[] colSum = new int[size];
        int diagSum = 0;
        int antiDiagSum = 0;
        int totalCount = 0;

        for(int i=0; i<size*size; i++) {
            char c = board.charAt(i);
            if(c == 'x') {
                rowSum[i/size]++;
                colSum[i%size]++;
                totalCount++;
            }
            else if(c == 'o') {
                rowSum[i/size]--;
                colSum[i%size]--;
                totalCount++;
            }
        }

        // diagonals
        for(Integer rs: rowSum) if(Math.abs(rs) == size) return true;
        for(Integer cs: colSum) if(Math.abs(cs) == size) return true;
        if(totalCount == size * size) return true;

        int index = 0;
        for(int i=0; i<size; i++) {
            char c = board.charAt(index);
            if(c == 'x') diagSum++;
            if(c == 'o') diagSum--;
            index += size+1;
        }

        index = size - 1;
        for(int i=0; i<size; i++) {
            char c = board.charAt(index);
            if(c == 'x') antiDiagSum++;
            if(c == 'o') antiDiagSum--;
            index += size-1;
        }
        if(size == Math.abs(diagSum)) return true;
        if(size == Math.abs(antiDiagSum)) return true;
        return false;
    }

    private List<String> getNextStates(String board, int player) {
        char marker = player == 1 ? 'x' : 'o';
        List<String> nextStates = new ArrayList<>();
        for(int i=0; i<board.length(); i++) {
            if(board.charAt(i) == '-') {
                nextStates.add(board.substring(0, i) + marker + board.substring(i+1));
            }
        }
        return nextStates;
    }


    private String encode(int size) {
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<size*size; i++) {
            builder.append('-');
        }
        return builder.toString();
    }
}

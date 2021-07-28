package Array;
import java.util.*;

/**
 * Any live cell with fewer than two live neighbors dies as if caused by under-population.
 * Any live cell with two or three live neighbors lives on to the next generation.
 * Any live cell with more than three live neighbors dies, as if by over-population.
 * Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
 *
 * The next state is created by applying the above rules simultaneously to every cell in the current state, where births and deaths occur simultaneously. Given the current state of the m x n grid board, return the next state.
 */
public class GameOfLife {

    public static void main(String[] args) {
        int[][] board = {
                {0,1,0},
                {0,0,1},
                {1,1,1},
                {0,0,0}};
        new GameOfLife().gameOfLife(board);
        System.out.println(Arrays.deepToString(board));
    }

    public void gameOfLife(int[][] board) {
        if(board == null || board.length == 0) return;

        for(int i=0; i<board.length; i++) {
            for(int j=0; j<board[i].length; j++) {
                int live = cellLives(board, i, j);
                board[i][j] = live;
            }
        }
        for(int i=0; i<board.length; i++) {
            for(int j=0; j<board[i].length; j++) {
                if(board[i][j] == 2) board[i][j] = 0;
                else if(board[i][j] == -1) board[i][j] = 1;
            }
        }
    }

    public int cellLives(int[][] board, int i, int j) {
        int aliveNeighbors = 0;
        int[][] directions = {
                {-1, -1},
                {-1, 0},
                {-1, 1},
                {1, -1},
                {1, 0},
                {1, 1},
                {0, -1},
                {0, 1},
        };
        for(int[] direction: directions) {
            aliveNeighbors += count(board, i+direction[0], j+direction[1]);
        }
        if(board[i][j] == 1){
            if (aliveNeighbors>= 2 && aliveNeighbors<=3) return 1;
            else return 2;
        }else{
            if(aliveNeighbors == 3) return -1;
            else return 0;
        }
    }

    public int count(int[][] board, int i, int j) {
        if(i < 0|| j<0 || i>= board.length || j>=board[i].length || board[i][j] <= 0) return 0;
        return 1;
    }
}

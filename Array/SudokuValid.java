package Array;

public class SudokuValid {

    public static void main(String[] args) {
        char[][] board =
                {
                          {'9', '3', '.', '.', '7', '.', '.', '.', '.'}
                        , {'6', '.', '.', '1', '9', '5', '.', '.', '.'}
                        , {'.', '7', '8', '.', '.', '.', '.', '6', '.'}
                        , {'8', '.', '.', '.', '6', '.', '.', '.', '3'}
                        , {'4', '.', '.', '8', '.', '3', '.', '.', '1'}
                        , {'7', '.', '.', '.', '2', '.', '.', '.', '6'}
                        , {'.', '6', '.', '.', '.', '.', '2', '8', '.'}
                        , {'.', '.', '.', '4', '1', '9', '.', '.', '5'}
                        , {'.', '.', '.', '.', '8', '.', '.', '7', '9'}};
        System.out.println(isValidSudoku(board));

    }


    /**
     * Find if the provided sudoku is valid.
     * Valid sudoku should have numbers from 1-9.
     * Number cannot be repeated in a row or a column or in 3*3 block.
     * No need to check if sudoku can be solved or now.
     * @param board
     * @return
     */
    public static boolean isValidSudoku(char[][] board) {
        boolean[][] rowDigits = new boolean[9][9];
        boolean[][] colDigits = new boolean[9][9];
        boolean[][] blockDigits = new boolean[9][9];

        for(int i=0; i<board.length; i++) {
            for(int j=0; j<board.length; j++) {
                if(board[i][j] == '.') continue;

                int digit = board[i][j] - '1';
                if(digit < 1 || digit > 9) return false;

                int blockNum = (i/3)*3 + j/3;

                if(rowDigits[i][digit]){
                    return false;
                }else{
                    rowDigits[i][digit] = true;
                }

                if(colDigits[j][digit]){
                    return false;
                }else{
                    colDigits[j][digit] = true;
                }

                if(blockDigits[blockNum][digit]){
                    return false;
                }else{
                    blockDigits[blockNum][digit] = true;
                }
            }
        }
        return true;
    }
}

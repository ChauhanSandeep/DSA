package arrays.backtracking;

/**
 * ✅ Problem: Valid Sudoku
 * https://leetcode.com/problems/valid-sudoku/
 *
 * Given a partially filled 9x9 Sudoku board, determine if it is valid.
 * Only the filled cells need to be validated according to these rules:
 *  - Each row must contain digits 1-9 without repetition.
 *  - Each column must contain digits 1-9 without repetition.
 *  - Each of the 9 3x3 sub-boxes of the grid must contain digits 1-9 without repetition.
 *
 * 🧠 Example:
 * Input:
 * [
 *   ['5','3','.','.','7','.','.','.','.'],
 *   ['6','.','.','1','9','5','.','.','.'],
 *   ['.','9','8','.','.','.','.','6','.'],
 *   ['8','.','.','.','6','.','.','.','3'],
 *   ['4','.','.','8','.','3','.','.','1'],
 *   ['7','.','.','.','2','.','.','.','6'],
 *   ['.','6','.','.','.','.','2','8','.'],
 *   ['.','.','.','4','1','9','.','.','5'],
 *   ['.','.','.','.','8','.','.','7','9']
 * ]
 * Output: true
 *
 * 🔁 Follow-up:
 * - What if the board is to be solved? (Use backtracking)
 *   Leetcode: https://leetcode.com/problems/sudoku-solver/
 * - How to validate solved board? (Same code works)
 */
public class SudokuValid {
  public static void main(String[] args) {
    char[][] board = {{'9', '3', '.', '.', '7', '.', '.', '.', '.'}, {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
        {'.', '7', '8', '.', '.', '.', '.', '6', '.'}, {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
        {'4', '.', '.', '8', '.', '3', '.', '.', '1'}, {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
        {'.', '6', '.', '.', '.', '.', '2', '8', '.'}, {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
        {'.', '.', '.', '.', '8', '.', '.', '7', '9'}};

    boolean isValid = isValidSudoku(board);
    System.out.println("Is the board valid? " + isValid);
  }

  /**
   * ✅ Validates a 9x9 Sudoku board.
   *
   * 🔍 Steps:
   * - Use 3 boolean arrays to track seen digits for rows, columns, and 3x3 blocks.
   * - Traverse each cell:
   *     - Skip '.' as it's an empty cell.
   *     - Convert digit char to index (0–8)
   *     - Calculate 3x3 block index using: (row / 3) * 3 + (col / 3)
   *     - If the digit is already seen in its row, column, or block, return false.
   *     - Otherwise, mark it as seen.
   *
   * ⏱ Time Complexity: O(1) — fixed 9x9 board
   * 🛠 Space Complexity: O(1) — fixed size boolean arrays
   */
  public static boolean isValidSudoku(char[][] board) {
    // rowSeen[i][d] → whether digit d has appeared in row i
    boolean[][] rowSeen = new boolean[9][9];
    boolean[][] colSeen = new boolean[9][9];
    boolean[][] blockSeen = new boolean[9][9];

    for (int row = 0; row < 9; row++) {
      for (int col = 0; col < 9; col++) {
        char cell = board[row][col];
        if (cell == '.') {
          continue;  // Skip empty cells
        }

        int digit = cell - '1'; // Map '1'–'9' to 0–8
        int blockIndex = (row / 3) * 3 + (col / 3);

        // If digit already seen in current row, col or block → invalid board
        if (rowSeen[row][digit] || colSeen[col][digit] || blockSeen[blockIndex][digit]) {
          return false;
        }

        // Mark digit as seen
        rowSeen[row][digit] = true;
        colSeen[col][digit] = true;
        blockSeen[blockIndex][digit] = true;
      }
    }
    return true;
  }
}
package Recursion;

public class NumberOfPaths {

    public static void main(String[] args) {
        System.out.println(numberOfPaths(3, 3));
    }

    /**
     * Find number of paths from top left to bottom right if only right or bottom travel is allowed
     * @param row
     * @param col
     * @return
     */
    static long numberOfPaths(int row, int col) {
        // Code Here
        return numberOfPaths(row, col, 1, 1);

    }

    static long numberOfPaths(int rows, int cols, int i, int j) {
        if(i<=0 || j<=0 || i>rows || j>cols) {
            return 0;
        }
        if(i == rows && j == cols) {
            return 1l;
        }

        return numberOfPaths(rows, cols, i+1, j) + numberOfPaths(rows, cols, i, j+1);
    }
}

package Array;

import java.util.HashSet;
import java.util.Set;

public class ClassRoom {

    /**
     * Provided an array describing classroom arrangement find how many students can sit in pairs(left, right, top, bottom)
     * First element contains total student count(always even and there will always be 2 rows)
     * Next elements are seats already taken.
     * @param args
     */
    public static void main(String[] args) {
/*
        [0, 1]
        [1, 1]
        [1, 1]
        [1, 0]
*/
        int[] arr = {8,1, 8};
        int result = countPairs(arr);
        System.out.println(result);
    }

    /**
     * For each unoccupied seat we have to check if there is a neighbor below or right of it.
     * Left and above scenario will be covered when second part of the pair is under consideration
     */
    public static int countPairs(int[] arr) {
        int len = (arr[0] + 1)/2;
        int[][] matrix = new int[len][2];
        Set<Integer> occupiedSeats = new HashSet<>();
        for(int i=1; i<arr.length; i++) {
            occupiedSeats.add(arr[i]);
        }

        int seatNum = 1;
        for(int i=0; i<matrix.length; i++) {
            for(int j=0; j<matrix[i].length; j++) {
                if(!occupiedSeats.contains(seatNum)) {
                    matrix[i][j] = 1;
                }
                seatNum++;
            }
        }

        int result = 0;
        for(int i=0; i<matrix.length; i++) {
            for(int j=0; j<matrix[i].length; j++) {
                if(matrix[i][j] == 1) {
                    result = result + isValidNeighbor(matrix, i+1, j) + isValidNeighbor(matrix, i, j+1);
                }
            }
        }
        return result;
    }

    public static int isValidNeighbor(int[][] matrix, int i, int j) {
        if(i < 0 || j<0 || i>=matrix.length || j>=matrix[i].length || matrix[i][j] == 0) return 0;
        return 1;
    }
}

package Array;

import java.util.HashSet;
import java.util.Set;

/**
 * Determines the number of student pairs who can sit next to each other in a classroom arrangement.
 *
 * Approach:
 * - The first element of the input array specifies the total number of students (always even, with two rows).
 * - The remaining elements indicate occupied seats.
 * - The method constructs a 2D seating matrix and checks for adjacent available seats.
 * - Runs in **O(N) time complexity**, where N is the number of seats.
 * - Space complexity is **O(N)** due to the 2D matrix representation.
 */
public class ClassRoom {
    public static void main(String[] args) {
        int[] arr = {8, 1, 8};
        int result = countPairs(arr);
        System.out.println("Number of student pairs: " + result);
    }

    /**
     * Counts the number of adjacent student pairs who can sit next to each other.
     *
     * @param arr The input array describing student count and occupied seats.
     * @return The number of adjacent student pairs.
     */
    public static int countPairs(int[] arr) {
        int totalSeats = arr[0];
        int rows = totalSeats / 2;
        int[][] seating = new int[rows][2];
        Set<Integer> occupiedSeats = new HashSet<>();

        for (int i = 1; i < arr.length; i++) {
            occupiedSeats.add(arr[i]);
        }

        int seatNumber = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 2; j++) {
                if (!occupiedSeats.contains(seatNumber)) {
                    seating[i][j] = 1;
                }
                seatNumber++;
            }
        }

        int pairCount = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 2; j++) {
                if (seating[i][j] == 1) {
                    pairCount += isValidNeighbor(seating, i + 1, j) + isValidNeighbor(seating, i, j + 1);
                }
            }
        }
        return pairCount;
    }

    /**
     * Checks if a seat is valid and unoccupied.
     *
     * @param seating The seating matrix.
     * @param row The row index.
     * @param col The column index.
     * @return 1 if the seat is valid and unoccupied; otherwise, 0.
     */
    public static int isValidNeighbor(int[][] seating, int row, int col) {
        if (row < 0 || col < 0 || row >= seating.length || col >= seating[row].length || seating[row][col] == 0) {
            return 0;
        }
        return 1;
    }
}

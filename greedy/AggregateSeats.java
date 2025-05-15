package greedy;

/**
 * Aggregate Seats (InterviewBit)
 * Problem Link: https://www.interviewbit.com/problems/seats/
 *
 * Given a row of seats represented by '.' (empty) and 'x' (occupied),
 * find the minimum moves required to aggregate all 'x' together in a contiguous block.
 *
 * Approach:
 * 1. **Find the Median 'x' Position**: The optimal position to aggregate seats
 *    is around the median of all occupied seats.
 * 2. **Move Left and Right Groups Towards the Median**:
 *    - Move all 'x' left of the median towards it.
 *    - Move all 'x' right of the median towards it.
 * 3. **Modular Arithmetic**: Since moves can be large, keep results under MODULO 10000003.
 *
 * Time Complexity: O(N) - We traverse the string at most 3 times.
 * Space Complexity: O(1) - No extra space is used.
 */
public class AggregateSeats {

    public static void main(String[] args) {
        String seatingRow = "....x..xx...x..";
        System.out.println(new AggregateSeats().minMovesToAggregateSeats(seatingRow)); // Output: Minimum moves
    }

    private static final int MODULO = 10000003;

    /**
     * Computes the minimum number of moves required to aggregate all 'x' into a contiguous block.
     *
     * @param seatingRow A string representing the row of seats with '.' as empty and 'x' as occupied.
     * @return Minimum number of moves required.
     */
    public int minMovesToAggregateSeats(String seatingRow) {
        int medianIndex = findMedianIndex(seatingRow);
        if (medianIndex == -1) return 0; // No 'x' found, no moves needed.

        long totalMoves = 0;

        // Move left side 'x' towards the median
        for (int i = medianIndex - 1, targetPosition = medianIndex - 1; i >= 0; i--) {
            if (seatingRow.charAt(i) == 'x') {
                totalMoves = (totalMoves + (targetPosition - i)) % MODULO;
                targetPosition--; // Update the next available position
            }
        }

        // Move right side 'x' towards the median
        for (int i = medianIndex + 1, targetPosition = medianIndex + 1; i < seatingRow.length(); i++) {
            if (seatingRow.charAt(i) == 'x') {
                totalMoves = (totalMoves + (i - targetPosition)) % MODULO;
                targetPosition++; // Update the next available position
            }
        }

        return (int) totalMoves;
    }

    /**
     * Finds the median index of all 'x' positions in the given seating row.
     *
     * @param seatingRow A string representing the row of seats.
     * @return Index of the median 'x', or -1 if no 'x' is found.
     */
    private int findMedianIndex(String seatingRow) {
        List<Integer> occupiedSeats = new ArrayList<>();

        // Collect all 'x' positions
        for (int i = 0; i < seatingRow.length(); i++) {
            if (seatingRow.charAt(i) == 'x') {
                occupiedSeats.add(i);
            }
        }

        if (occupiedSeats.isEmpty()) return -1; // No occupied seats

        // Median index (middle element of sorted positions)
        return occupiedSeats.get(occupiedSeats.size() / 2);
    }
}

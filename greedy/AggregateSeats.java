package greedy;

/**
 * https://www.interviewbit.com/problems/seats/
 */
public class AggregateSeats {

    public static void main(String[] args) {
        String str = "....x..xx...x..";
        System.out.println(new AggregateSeats().minMovesToAggregateSeats(str));
    }

    // Find the median index of 'x' occurrences
    private int findMedianIndex(String str) {
        int count = 0, currCount = 0, index = 0;

        for (char ch : str.toCharArray()) {
            if (ch == 'x') count++;
        }
        if (count == 0) return -1;

        int halfCount = (count + 1) / 2;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == 'x') currCount++;
            if (currCount == halfCount) return i;
        }
        return -1;
    }

    public int minMovesToAggregateSeats(String str) {
        int mid = findMedianIndex(str);
        if (mid == -1) return 0;

        final int MODULO = 10000003;
        long totalJumps = 0;

        // Move left side towards the median
        for (int i = mid - 1, availableIndex = mid - 1; i >= 0; i--) {
            if (str.charAt(i) == 'x') {
                totalJumps = (totalJumps + (availableIndex - i)) % MODULO;
                availableIndex--;
            }
        }

        // Move right side towards the median
        for (int i = mid + 1, availableIndex = mid + 1; i < str.length(); i++) {
            if (str.charAt(i) == 'x') {
                totalJumps = (totalJumps + (i - availableIndex)) % MODULO;
                availableIndex++;
            }
        }

        return (int) totalJumps;
    }
}

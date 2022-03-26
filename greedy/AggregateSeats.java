package greedy;

/**
 * https://www.interviewbit.com/problems/seats/
 */
public class AggregateSeats {

    public static void main(String[] args) {
        String str = "....x..xx...x..";
        System.out.println(new AggregateSeats().seats(str));
    }

    public int median(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == 'x') count++;
        }

        int halfCount = (count + 1) / 2;
        if (halfCount == 0) return -1;

        int currCount = 0;
        int index = 0;
        while (currCount < halfCount) {
            if (str.charAt(index) == 'x') currCount++;
            index++;
        }
        return index - 1;
    }

    public int seats(String str) {
        // 1. Find median index. Around this index all 'x' would be consolidated
        int mid = median(str);
        if (mid == -1) return 0;

        int MOD = 10000003;
        long totalJumps = 0;

        // 2. Fill left side of mid index
        int availableIndex = mid - 1;
        int currIndex = mid - 1;
        while (currIndex >= 0) {
            if (str.charAt(currIndex) == 'x') {
                totalJumps = (totalJumps + availableIndex - currIndex) % MOD;
                availableIndex--;
            }
            currIndex--;
        }

        // 3. Fill right side of mid index
        currIndex = mid + 1;
        availableIndex = mid + 1;
        while (currIndex < str.length()) {
            if (str.charAt(currIndex) == 'x') {
                totalJumps = (totalJumps + currIndex - availableIndex) % MOD;
                availableIndex++;
            }
            currIndex++;
        }

        return (int) totalJumps;
    }
}

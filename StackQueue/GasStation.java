package StackQueue;

/**
 * https://www.interviewbit.com/problems/gas-station/
 */
public class GasStation {

    public static int canCompleteCircuit(final int[] gasArr, final int[] costArr) {
        int len = gasArr.length;
        int end = 0;
        int curr = 0;
        int gasLeft = 0;
        boolean isStart = true;

        while (isStart || curr != end) {
            isStart = false;
            gasLeft += gasArr[curr] - costArr[curr];
            curr = (curr + 1) % len;

            if (gasLeft < 0) {
                // if we could not reach from 0 to N in first time. We cannot do later so return -1
                if (curr <= end) return -1;

                // reinitialize conditions
                isStart = true;
                gasLeft = 0;
                end = curr;
            }
        }
        return end;
    }

    public static void main(String[] args) {
        int[] gas = {1, 2, 3, 4, 5};
        int[] cost = {3, 4, 5, 1, 2};

        int startIndex = canCompleteCircuit(gas, cost);
        System.out.println(startIndex);
    }
}

package StackQueue;

public class GasStation {

    public static int canCompleteCircuit(int[] gas, int[] cost) {
        int start = 0;
        int end = 0;
        int gasLeft = 0;
        int len = gas.length;
        boolean isStart = true;

        while(end != start || isStart) {
            gasLeft += gas[end] - cost[end];
            if(gasLeft < 0) {
                end = (end+1)%len;
                if(end <= start) {
                    return -1; // if we could not reach from 0 to N in first time. We cannot do later
                }
                start = end;
                gasLeft = 0;
                isStart = true;
            }else {
                end = (end+1)%len;
                isStart = false;
            }
        }
        return start;

    }

    public static void main(String[] args) {
        int[] gas =  {1,2,3,4,5};
        int[] cost = {3,4,5,1,2};

        int startIndex = canCompleteCircuit(gas, cost);
        System.out.println(startIndex);
    }
}

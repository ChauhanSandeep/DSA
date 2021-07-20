package Array;

import java.util.ArrayList;

public class SumSubarray2 {


    public static void main(String[] args) {
        int arr[] = { 1,1,1 };
        int n = arr.length;
        int sum = 2;
        System.out.println(subarraySum(arr, n, sum));

    }

    public static ArrayList<Integer> subarraySum(int[] arr, int n, int sum) {
        int start = 0;
        int end = 1;
        int currSum = arr[0];
        ArrayList<Integer> resultList = new ArrayList<>();

        for(int i=end; i<arr.length; i++) {
            currSum += arr[i];
            if(currSum == sum) {
                resultList.add(start);
                resultList.add(i);
                return resultList;
            }
            if(currSum > sum){
                currSum -= arr[i];
                currSum -= arr[start];
                i--;
                start++;
            }
        }
        resultList.add(-1);
        return resultList;
    }
}

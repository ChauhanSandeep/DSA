package DynamicProgramming.StockBuySell;

/**
 * Find max profit that can be made with just one transaction
 */
public class BuySellStock0 {

    public static void main(String[] args) {
        int[] arr = {10, 22, 5, 75, 65, 80};
        int profit = new BuySellStock0().maxProfit(arr);
        System.out.println(profit);
    }

    private int maxProfit(int[] arr) {
        int maxProfit = 0;
        int currProfit = 0;
        int leastSoFar = Integer.MAX_VALUE;

        for(int i=0; i<arr.length; i++) {
            if(arr[i] < leastSoFar) {
                leastSoFar = arr[i];
            }
            currProfit = arr[i] - leastSoFar;
            maxProfit = Math.max(maxProfit, currProfit);
        }
        return maxProfit;
    }
}

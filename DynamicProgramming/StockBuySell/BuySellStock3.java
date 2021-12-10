package DynamicProgramming.StockBuySell;

/**
 * Given price of stock on each day.
 * Find max profit that can be made by having at most 2 transactions
 */
public class BuySellStock3 {

    public static void main(String[] args) {
        int[] arr = {10, 22, 5, 75, 65, 80};
        System.out.println(new BuySellStock3().maxProfit(arr));
    }

    public int maxProfit(final int[] arr) {
        int leastsf = arr[0]; // least so far
        int maxProfitIst = 0; // max profit if sold today
        int[] maxProfitIsut = new int[arr.length]; // max profit if sold upto today

        for (int i = 1; i < arr.length; i++) {
            leastsf = Math.min(arr[i], leastsf);
            maxProfitIst = arr[i] - leastsf;
            maxProfitIsut[i] = Math.max(maxProfitIsut[i-1], maxProfitIst);
        }

        int maxat = arr[arr.length - 1]; // max after that
        int maxProfitIbt = 0; // max profit if bought today
        int[] maxProfitIbut = new int[arr.length]; // max profit if bought upto today

        for (int i = arr.length - 2; i >= 0; i--) {
            maxat = Math.max(arr[i], maxat);
            maxProfitIbt = maxat - arr[i];
            maxProfitIbut[i] = Math.max(maxProfitIbt, maxProfitIbut[i+1]);
        }

        int result = 0;
        for (int i = 0; i < arr.length; i++) {
            result = Math.max(result, Math.max(maxProfitIbut[i] + maxProfitIsut[i], Math.max(maxProfitIbut[i], maxProfitIsut[i])));
        }
        return result;
    }


}

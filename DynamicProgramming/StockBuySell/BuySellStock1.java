package DynamicProgramming.StockBuySell;

/**
 * Find max profit with infinite transactions allowed with a transaction fee
 * to be paid at time of selling stock.
 */
public class BuySellStock1 {

    public static void main(String[] args) {
        int[] arr = {10, 20, 5, 40};
        int fee = 3;
        int profit = new BuySellStock1().maxProfit(arr, fee);
        System.out.println("Max profit gained with fee of " + fee + " is " + profit);

    }

    public int maxProfit(int[] arr, int fee) {
        int obsp = -arr[0]; // old bought state profit. Profit with one stock bought
        int ossp = 0;       // old sold state profit. Profit with all stock sold

        for(int i=1; i<arr.length; i++) {
            int nssp = 0; // new sold state profit
            int nbsp = 0; // new bought state profit

            if(obsp + arr[i] - fee > ossp) {
                // sell previous hold stock, book current profit, pay fee
                nssp = obsp + arr[i] - fee;
            }else{
                // continue old sold state profit
                nssp = ossp;
            }

            if(ossp - arr[i] > obsp) {
                // buy stock. pay current price
                nbsp = ossp - arr[i];
            }else{
                // continue from old bought state
                nbsp = obsp;
            }

            ossp = nssp;
            obsp = nbsp;
        }
        return ossp;
    }
}

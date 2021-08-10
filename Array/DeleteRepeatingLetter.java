package Array;

/**
 * Given a string s and an array of integers cost where cost[i] is the cost of deleting the ith character in s.
 * Return the minimum cost of deletions such that there are no two identical letters next to each other.
 */
public class DeleteRepeatingLetter {

    public static void main(String[] args) {
        int[] cost = {1, 2, 3, 4, 5};
        int minCost = new DeleteRepeatingLetter().minCost("abaac", cost);
        System.out.println(minCost);
    }

    public int minCost(String str, int[] cost) {
        int result = 0;
        int len = str.length();
        if(len == 1) return result;

        for(int i=1; i<len; i++) {

            if(str.charAt(i) == str.charAt(i-1)) {
                int max = cost[i-1];
                int sum = 0;
                char curr = str.charAt(i-1);

                for(i = i-1; i<len && str.charAt(i) == curr; i++) {
                    sum += cost[i];
                    if(cost[i] > max) {
                        max = cost[i];
                    }
                }
                result += sum - max;
                i--;
            }

        }
        return result;
    }
}

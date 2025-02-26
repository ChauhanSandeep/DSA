package Array;

/**
 * Given a string s and an array of integers cost where cost[i] is the cost of deleting the ith character in s.
 * Return the minimum cost of deletions such that there are no two identical letters next to each other.
 *
 * Approach:
 * - Iterate through the string while tracking consecutive duplicate characters.
 * - Sum up the deletion costs and retain the highest cost character in each duplicate sequence.
 * - Runs in **O(N) time complexity** since we iterate through the string once.
 * - Space complexity is **O(1)** as we use only a few variables.
 */
public class DeleteRepeatingLetter {
    public static void main(String[] args) {
        int[] cost = {1, 2, 3, 4, 5};
        System.out.println("Minimum deletion cost: " + minCost("abaac", cost));
    }

    /**
     * Calculates the minimum deletion cost to remove adjacent duplicate letters.
     *
     * @param str  Input string.
     * @param cost Deletion cost for each character.
     * @return Minimum total deletion cost.
     */
    public static int minCost(String str, int[] cost) {
        int totalCost = 0;
        int len = str.length();
        if (len == 1) return totalCost;

        for (int i = 1; i < len; i++) {
            if (str.charAt(i) == str.charAt(i - 1)) {
                int maxCost = cost[i - 1];
                int sumCost = 0;
                char currChar = str.charAt(i - 1);

                while (i < len && str.charAt(i) == currChar) {
                    sumCost += cost[i];
                    maxCost = Math.max(maxCost, cost[i]);
                    i++;
                }
                totalCost += sumCost - maxCost;
                i--; // Adjust index to continue checking properly
            }
        }
        return totalCost;
    }
}

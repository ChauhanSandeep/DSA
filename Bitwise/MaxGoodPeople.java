package Bitwise;

/**
 * https://leetcode.com/problems/maximum-good-people-based-on-statements/
 */
public class MaxGoodPeople {

    public static void main(String[] args) {
        int[][] statements = {
                {2, 1, 2},
                {1, 2, 2},
                {2, 0, 2}
        };
        System.out.println(new MaxGoodPeople().maximumGood(statements));
    }

    /**
     *
     * Create equation. ie binary string representing good and bad persons.
     * for each equation check if equation is valid for the provided statements.
     * Keep track of valid equation with max good persons.
     */
    public int maximumGood(int[][] statements) {
        int len = statements.length;
        int result = 0;

        int num = (int) (Math.pow(2, len) - 1);
        while (num > 0) {
            StringBuilder equation = new StringBuilder(Integer.toString(num, 2));
            while (equation.length() != len) {
                equation.insert(0, "0");
            }
            if (valid(statements, equation.toString())) {
                int tempRes = 0;
                for (Character c : equation.toString().toCharArray()) {
                    if (c == '1') tempRes++;
                }
                result = Math.max(tempRes, result);
            }
            num--;
        }
        return result;
    }

    public boolean valid(int[][] statements, String equation) {
        for (int i = 0; i < equation.length(); i++) {
            if (equation.charAt(i) == '1') {

                int[] statement = statements[i];
                for (int j = 0; j < statement.length; j++) {
                    if (statement[j] == 2) continue;
                    if (equation.charAt(j) - '0' != statement[j]) return false;
                }

            }
        }
        return true;
    }

}

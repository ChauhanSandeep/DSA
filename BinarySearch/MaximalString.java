package BinarySearch;

/**
 * Find the lexicographically maximal string that can be created by doing max k swaps
 */
public class MaximalString {
    public static void main(String[] args) {
        String input = "254";
        String result = new MaximalString().solve(input, 1);
        System.out.println(result);
    }

    String max;
    public String solve(String str, int k) {
        this.max = str;
        solveRec(str, k);
        return max;
    }

    public void solveRec(String str, int k) {
        if(str.compareTo(max) > 0) {
            max = str;
        }
        if(k == 0) return;

        int size = str.length();
        for(int i=0; i<size-1; i++) {
            for(int j=i+1; j<size; j++) {
                if(str.charAt(j) > str.charAt(i)) {
                    String swapped = swap(str, i, j);
                    solveRec(swapped, k-1);
                }
            }
        }
    }

    public String swap(String original, int first, int second) {
        char[] charArr = original.toCharArray();
        char temp = charArr[first];
        charArr[first] = charArr[second];
        charArr[second] = temp;
        return new String(charArr);
    }
}

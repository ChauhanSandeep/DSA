package String;

public class AtoI {
    public static void main(String[] args) {
        String input = "213";
        int output = aToI(input);
        System.out.println(output);
    }

    /**
     * Convert String representation of a integer to integer
     * @param input
     * @return
     */
    public static int aToI(String input) {
        int result = 0;
        for(Character c: input.toCharArray()) {
            if(c < '0' || c > '9') throw new RuntimeException("Incorrect character " + c);

            result = result*10 + (c - '0');
        }
        return result;
    }
}

package String;

public class ZigZagConversion {

    public static void main(String[] args) {
        String str = "PAYPALISHIRING";
        /**
         * P   A     H    N
         *  A P  L  S I  I G
         *   Y     I   R
         * @return PAHNAPLSIIGYIR
         */
        System.out.println(convert(str, 3));

        /**
         * P     I    N
         *  A   L S  I G
         *   Y A   H R
         *    P     I
         * @return PINALSIGYAHRPI
         */
        System.out.println(convert(str, 4));
    }

    /**
     * Provided a string, write it in zig zag format and return result;
     */
    public static String convert(String str, int numRows) {
        if(numRows == 1) return str;

        StringBuffer[] arr = new StringBuffer[numRows];
        for(int i=0; i<arr.length; i++) {
            arr[i] = new StringBuffer();
        }
        int index = 0;
        int dir = 1;

        for(int i=0; i<str.length(); i++) {
            arr[index].append(str.charAt(i));
            if(index == numRows - 1 || (index == 0 && i != 0)) {
                dir = dir * -1;
            }
            if(dir == 1) index++;
            else index--;
        }
        StringBuilder result = new StringBuilder();
        for(StringBuffer buffer: arr) {
            result.append(buffer.toString());
        }
        return result.toString();
    }
}

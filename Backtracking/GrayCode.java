package Backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The gray code is a binary numeral system where two successive values differ in only one bit.
 *
 * Given a non-negative integer n representing the total number of bits in the code,
 * print the sequence of gray code. A gray code sequence must begin with 0.
 */
public class GrayCode {

    public static void main(String[] args) {
        ArrayList<Integer> list = new GrayCode().grayCode(4);
        for(Integer num: list) {
            System.out.println(Integer.toString(num, 2));
        }
    }

    public ArrayList<Integer> grayCode(int n) {
        List<String> strList = grayCodeRec(n);

        ArrayList<Integer> resultList = new ArrayList<>();
        for(int i=0; i<strList.size(); i++) {
            resultList.add(Integer.parseInt(strList.get(i), 2));
        }
        return resultList;
    }

    public ArrayList<String> grayCodeRec(int n) {
        if(n == 1) {
            ArrayList<String> result = new ArrayList<>();
            result.add("0");
            result.add("1");
            return result;
        }else{
            ArrayList<String> prev = grayCodeRec(n-1);
            ArrayList<String> result = new ArrayList<String>();

            for(int i=0; i<prev.size(); i++) {
                result.add("0" + prev.get(i));
            }
            for(int i=prev.size()-1; i>=0; i--) {
                result.add("1"+prev.get(i));
            }
            return result;
        }
    }

}

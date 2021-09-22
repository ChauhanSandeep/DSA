package Recursion;

import java.util.ArrayList;
import java.util.List;

/**
 * Given a string find all the valid IPs that can be created
 */
public class RestoreIpAddress {
    public static void main(String[] args) {
        List<String> list = new RestoreIpAddress().restoreIpAddresses("25525511135");
        System.out.println(list);
    }

    public List<String> restoreIpAddresses(String s) {
        List<String> result = new ArrayList<>();

        String curr = "";
        restoreIpAddresses(s, 3, result, curr);
        return result;
    }

    public void restoreIpAddresses(String str, int count, List<String> result, String curr){
        if(count < 0) return;
        if(count == 0 && isValidString(str)) {
            result.add(curr + "." + str);
            return;
        }

        for(int i=1; i<str.length(); i++){
            if(isValidString(str.substring(0, i))){
                restoreIpAddresses(str.substring(i), count-1, result, "".equals(curr) ? str.substring(0, i) : curr + "." + str.substring(0, i));
            }else{
                break;
            }
        }
    }

    public boolean isValidString(String str) {
        try{
            int n = Integer.parseInt(str);
            if(n == 0 && str.length() > 1) return false;
            if(n > 0 && str.startsWith("0")) return false;
            if(n<0 || n > 255) return false;
        }catch(Exception e) {
            return false;
        }
        return true;
    }
}

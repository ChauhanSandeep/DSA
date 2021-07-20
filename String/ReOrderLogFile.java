package String;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ReOrderLogFile {

    public static void main(String[] args) {
        String[] str = {"a1 9 2 3 1","g1 act car","zo4 4 7","ab1 off key dog","a8 act zoo","a2 act car"};
        String[] result = new ReOrderLogFile().reorderLogFiles(str);
        System.out.println(Arrays.asList(result));
    }

    public String[] reorderLogFiles(String[] logs) {
        List<String> letterLogs = new ArrayList<>();
        List<String> digitLogs = new ArrayList<>();

        for(int i=0; i<logs.length; i++) {
            if(isDigitLog(logs[i])) {
                digitLogs.add(logs[i]);
            }else{
                letterLogs.add(logs[i]);
            }
        }
        System.out.println(digitLogs.toString());
        System.out.println(letterLogs.toString());

        letterLogs.sort((a, b) -> {
            int index =  a.substring(a.indexOf(" ")).compareTo(b.substring(b.indexOf(" ")));
            if(index == 0) return a.substring(0, a.indexOf(" ")).compareTo(b.substring(0, b.indexOf(" ")));
            else return index;
        });
        String[] result = new String[logs.length];
        int i=0;
        for(; i<letterLogs.size(); i++) {
            result[i] = letterLogs.get(i);
        }
        int j=0;
        for(; j<digitLogs.size(); j++) {
            result[i] = digitLogs.get(j);
            i++;
        }
        return result;
    }

    public boolean isDigitLog(String log) {
        String[] split = log.split("[ ]+");
        for(int i=1; i<split.length; i++) {
            String temp = split[i];
            for(int j=0; j<temp.length(); j++) {
                if(temp.charAt(j) >= 'a' && temp.charAt(j) <= 'z'){
                    return false;
                }
            }
        }
        return true;
    }
}

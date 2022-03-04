package String;

import java.util.ArrayList;
import java.util.List;

public class CsvParser {

    public static void main(String[] args) {
        String[] input = {
                "\"Alexandra \"\"Alex\"\"\",Menendez,alex.menendez@gmail.com,Miami,1",
                "John,Smith,john.smith@gmail.com,Los Angeles,10",
                "Jane,Roberts,janer@msn.com,\"San Francisco, CA\",0",
                "1,2,,4,\"5\""
        };
        List<List<String>> output = parseCSV(input);
        for(List<String> out: output)
            System.out.println(out);
    }

    private static List<List<String>> parseCSV(String[] input) {
        List<List<String>> result = new ArrayList<>();
        for(int i=0; i<input.length; i++) {
            result.add(parseLine(input[i]));
        }
        return result;
    }

    private static List<String> parseLine(String line) {
        List<String> result = new ArrayList<>();
        //String[] words = line.split(",", 0);
        List<String> words = betterParse(line);
        for(String word: words) {
            String w  = removeFirstDoubleQuote(word);
            w = addSingleQuote(w);
            result.add(w);
        }
        return result;
    }

    private static String addSingleQuote(String s) {
        return "'" + s + "'";
    }

    private static String removeFirstDoubleQuote(String s) {
        char[] chars = s.toCharArray();
        if(chars.length > 0 && chars[0] == '"' && chars[s.length()-1] == '"') {
            return s.substring(1,s.length()-1);
        }
        return s;
    }

    private static List<String> betterParse(String s) {

        char[] chars = s.toCharArray();
        List<String> result = new ArrayList<>();
        int i=0;
        StringBuilder sb = new StringBuilder();
        boolean doubleQuoteFound = false;

        while(i < chars.length) {
            if(chars[i] == ',' && !doubleQuoteFound) {
                String si = removeFirstDoubleQuote(sb.toString());
                result.add(removeDupQuotes(si));
                sb = new StringBuilder();
            } else if(chars[i] == '"') {
                doubleQuoteFound = !doubleQuoteFound;
                sb.append(chars[i]);
            } else {
                sb.append(chars[i]);
            }
            i++;
        }
        if(sb.length() != 0)
            result.add(sb.toString());

        return result;
    }

    private static String removeDupQuotes(String s) {
        char[] chars = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        int i=0;
        boolean quoteFlag = false;
        while(i < chars.length) {
            if(!quoteFlag && chars[i] == '\"') {
                sb.append(chars[i]);
                quoteFlag = !quoteFlag;
            } else if(quoteFlag && chars[i] != '\"') {
                sb.append(chars[i]);
                quoteFlag = !quoteFlag;
            } else if(!quoteFlag && chars[i] != '\"') {
                sb.append(chars[i]);
            }
            i++;
        }
        return sb.toString();
    }
}

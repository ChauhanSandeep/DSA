package AmazonOa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoinbaseRound1 {

    public static void main(String[] argv) {
        String[][] logs1 = new String[][] {
                { "58523", "user_1", "resource_1" },
                { "62314", "user_2", "resource_2" },
                { "54001", "user_1", "resource_3" },
                { "200", "user_6", "resource_5" },
                { "215", "user_6", "resource_4" },
                { "54060", "user_2", "resource_3" },
                { "53760", "user_3", "resource_3" },
                { "58522", "user_22", "resource_1" },
                { "53651", "user_5", "resource_3" },
                { "2", "user_6", "resource_1" },
                { "100", "user_6", "resource_6" },
                { "400", "user_7", "resource_2" },
                { "100", "user_8", "resource_6" },
                { "54359", "user_1", "resource_3"},
        };

        String[][] logs2 = new String[][] {
                {"300", "user_1", "resource_3"},
                {"599", "user_1", "resource_3"},
                {"900", "user_1", "resource_3"},
                {"1199", "user_1", "resource_3"},
                {"1200", "user_1", "resource_3"},
                {"1201", "user_1", "resource_3"},
                {"1202", "user_1", "resource_3"}
        };

        String[][] logs3 = new String[][] {
                {"300", "user_10", "resource_5"}
        };

        // Map<String, List<String>> res2 = getLogTime(logs2);
        // System.out.println(res2);
        // Map<String, List<String>> res3 = getLogTime(logs3);
        // System.out.println(res3);

        String[] res1 = mostAccessedResource(logs1);
        System.out.println(Arrays.toString(res1));

    }


    public static String[] mostAccessedResource(String[][] logs) {
        if(logs == null || logs.length == 0 || logs[0].length == 0) return null;
        Map<String, List<Integer>> map = new HashMap<>();


        for(String[] log: logs) {
            Integer timestamp = Integer.parseInt(log[0]);
            String user = log[1];
            String resource = log[2];

            List<Integer> timestampList = map.getOrDefault(resource, new ArrayList<>());
            timestampList.add(timestamp);
            map.put(resource, timestampList);
        }

        String maxResource = "";
        int maxCount = 0;

        for(Map.Entry<String, List<Integer>> entry: map.entrySet()) {
            String resource = entry.getKey();
            System.out.println(resource);
            List<Integer> timestamps = entry.getValue();
            int count = getCount(timestamps);
            if(count > maxCount) {
                maxResource = resource;
                maxCount = count;
            }
        }

        return new String[]{maxResource, String.valueOf(maxCount)};

    }

    private static int getCount(List<Integer> timestamps) {
        Collections.sort(timestamps);
        System.out.println(timestamps);
        int maxLen = 0;

        for(int i=0; i<timestamps.size(); i++) {
            int currLen = 1;
            for(int j=i+1; j<timestamps.size(); j++) {
                if(timestamps.get(j) - timestamps.get(i) <= 300) {
                    currLen++;
                }else{
                    break;
                }
            }
            if(currLen > maxLen) {
                maxLen = currLen;
            }
        }
        return maxLen;
    }
}

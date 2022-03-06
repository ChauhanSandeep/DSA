package Graph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * https://leetcode.com/problems/evaluate-division/
 */
public class EvaluateDivision {

    public static void main(String[] args) {
        List<List<String>> equations = Stream.of(
                        Stream.of("a", "b").collect(Collectors.toList()),
                        Stream.of("b", "c").collect(Collectors.toList())
            ).collect(Collectors.toList());

        double[] values = {2.0, 3.0};
        List<List<String>> queries =Stream.of(
                        Stream.of("a","c").collect(Collectors.toList()),
                        Stream.of("b","a").collect(Collectors.toList()),
                        Stream.of("a","e").collect(Collectors.toList()),
                        Stream.of("a","a").collect(Collectors.toList()),
                        Stream.of("x","x").collect(Collectors.toList())
            ).collect(Collectors.toList());
        double[] doubles = new EvaluateDivision().calcEquation(equations, values, queries);
        System.out.println(Arrays.toString(doubles));
    }

    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        Map<String, HashMap<String, Double>> map = new HashMap<>();
        int len = equations.size();
        for(int i=0; i<len; i++){
            String dividend = equations.get(i).get(0);
            String divisor = equations.get(i).get(1);
            double quotient = values[i];

            map.putIfAbsent(dividend, new HashMap<>());
            map.get(dividend).put(divisor, quotient);

            map.putIfAbsent(divisor, new HashMap<>());
            map.get(divisor).put(dividend, 1/quotient);
        }

        double[] result = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            List<String> query = queries.get(i);
            String dividend = query.get(0);
            String divisor = query.get(1);

            if(!map.containsKey(dividend) || !map.containsKey(divisor)) {
                result[i] = -1;
                continue;
            }
            if(dividend.equals(divisor)) {
                result[i] = 1;
                continue;
            }
            Set<String> visited = new HashSet<>();
            result[i] = dfs(dividend, divisor, 1d, map, visited);
        }
        return result;
    }

    private double dfs(String currNode, String destNode, double resTillNow, Map<String, HashMap<String, Double>> map, Set<String> visited) {

        if(currNode.equals(destNode)) return resTillNow;
        if(!map.containsKey(currNode) || visited.contains(currNode)) return -1d;
        visited.add(currNode);

        for(Map.Entry<String, Double> child: map.get(currNode).entrySet()) {
            String nNode = child.getKey();
            Double jumpVal = child.getValue();
            double temp = dfs(nNode, destNode, resTillNow * jumpVal, map, visited);
            if(temp != -1d) return temp;
        }
        return -1d;
    }
}

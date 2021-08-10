package Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Merge accounts which contains common email Ids
 */
class MergeAccounts {

    public static void main(String[] args) {
        List<List<String>> accounts = new ArrayList<>();
        List<String> a1 = Stream.of("John","johnsmith@mail.com","john_newyork@mail.com").collect(Collectors.toList());
        List<String> a2 = Stream.of("John","johnsmith@mail.com","john00@mail.com").collect(Collectors.toList());
        List<String> a3 = Stream.of("Mary","mary@mail.com").collect(Collectors.toList());
        List<String> a4 = Stream.of("John","johnnybravo@mail.com").collect(Collectors.toList());
        accounts.add(a1);
        accounts.add(a2);
        accounts.add(a3);
        accounts.add(a4);
        List<List<String>> lists = new MergeAccounts().accountsMerge(accounts);
        System.out.println(lists);
    }

    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        Map<String, Set<String>> graph = new HashMap<>();  //<email node, neighbor nodes>
        Map<String, String> owner = new HashMap<>();        //<email, username>

        // Build the graph;
        for (List<String> account : accounts) {
            String userName = account.get(0);
            Set<String> neighbors = new HashSet<>(account);
            neighbors.remove(userName);

            for (int i = 1; i < account.size(); i++) {
                String email = account.get(i);
                if (!graph.containsKey(email)) {
                    graph.put(email, new HashSet<>());
                }
                graph.get(email).addAll(neighbors);
                owner.put(email, userName);
            }
        }

        Set<String> visited = new HashSet<>();
        List<List<String>> results = new ArrayList<>();
        // DFS search the graph;
        for (String email : owner.keySet()) {
            if (!visited.contains(email)) {
                List<String> result = new ArrayList<>();
                dfs(graph, email, visited, result);
                Collections.sort(result);
                result.add(0, owner.get(email));
                results.add(result);
            }
        }


        return results;
    }

    public void dfs(Map<String, Set<String>> graph, String email, Set<String> visited, List<String> list) {
        list.add(email);
        visited.add(email);
        for (String neighbor : graph.get(email)) {
            if (!visited.contains(neighbor)) {
                dfs(graph, neighbor, visited, list);
            }
        }
    }
}
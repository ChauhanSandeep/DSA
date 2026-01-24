package dynamicprogramming.MiscellaneousDP;

import java.util.*;

/**
 * 1125. Smallest Sufficient Team
 *
 * Problem: Given an array of required skills and people with their skills,
 * return any sufficient team of the smallest possible size such that the team
 * collectively has all the required skills.
 *
 * Example:
 * Input: req_skills = ["java","nodejs","reactjs"], people = [["java"],["nodejs"],["nodejs","reactjs"]]
 * Output: [0,2]
 * Explanation: Person 0 has "java", person 2 has "nodejs" and "reactjs"
 *
 * LeetCode: https://leetcode.com/problems/smallest-sufficient-team
 *
 * Follow-up questions:
 * Q: What if skills have different priorities or weights?
 * A: Extend DP state to include weighted costs and modify transitions.
 *
 * Q: How to handle very large skill sets efficiently?
 * A: Use bit manipulation optimizations and pruning strategies.
 *
 * Q: Can we find multiple optimal teams or teams within size constraints?
 * A: Modify DP to track all optimal solutions or add size constraints.
 */
public class SmallestSufficientTeam {

    /**
     * Bitmask DP approach - optimal solution for reasonable skill counts.
     *
     * Algorithm: Dynamic Programming with Bitmasks
     * - Use bitmask to represent skill coverage (bit i = 1 if skill i is covered)
     * - dp[mask] = minimum team size to cover skills represented by mask
     * - For each person, try adding them to existing teams
     * - Track parent information to reconstruct actual team
     *
     * Time Complexity: O(2^m * n) where m is skills count, n is people count
     * Space Complexity: O(2^m) for DP array
     */
    public int[] smallestSufficientTeam(String[] req_skills, List<List<String>> people) {
        int m = req_skills.length;
        int n = people.size();

        // Map skills to indices
        Map<String, Integer> skillToIndex = new HashMap<>();
        for (int i = 0; i < m; i++) {
            skillToIndex.put(req_skills[i], i);
        }

        // Convert people skills to bitmasks
        int[] peopleSkills = new int[n];
        for (int i = 0; i < n; i++) {
            for (String skill : people.get(i)) {
                if (skillToIndex.containsKey(skill)) {
                    peopleSkills[i] |= (1 << skillToIndex.get(skill));
                }
            }
        }

        int targetMask = (1 << m) - 1; // All skills covered

        // DP with path reconstruction
        int[] dp = new int[1 << m];
        int[] parent = new int[1 << m];
        int[] usedPerson = new int[1 << m];

        Arrays.fill(dp, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dp[0] = 0;

        // Fill DP table
        for (int mask = 0; mask < (1 << m); mask++) {
            if (dp[mask] == Integer.MAX_VALUE) continue;

            for (int person = 0; person < n; person++) {
                int newMask = mask | peopleSkills[person];
                if (dp[newMask] > dp[mask] + 1) {
                    dp[newMask] = dp[mask] + 1;
                    parent[newMask] = mask;
                    usedPerson[newMask] = person;
                }
            }
        }

        // Reconstruct team
        List<Integer> team = new ArrayList<>();
        int currentMask = targetMask;

        while (parent[currentMask] != -1) {
            team.add(usedPerson[currentMask]);
            currentMask = parent[currentMask];
        }

        return team.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Optimized bitmask DP with pruning.
     * Includes various optimizations for better performance.
     */
    public int[] smallestSufficientTeamOptimized(String[] req_skills, List<List<String>> people) {
        int m = req_skills.length;
        int n = people.size();

        if (m > 20) {
            // Fall back to approximation for large skill sets
            return approximateSolution(req_skills, people);
        }

        Map<String, Integer> skillToIndex = new HashMap<>();
        for (int i = 0; i < m; i++) {
            skillToIndex.put(req_skills[i], i);
        }

        // Convert and filter people
        List<PersonInfo> validPeople = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int skillMask = 0;
            for (String skill : people.get(i)) {
                if (skillToIndex.containsKey(skill)) {
                    skillMask |= (1 << skillToIndex.get(skill));
                }
            }

            if (skillMask != 0) {
                validPeople.add(new PersonInfo(i, skillMask, Integer.bitCount(skillMask)));
            }
        }

        // Sort people by skill count (greedy hint)
        validPeople.sort((a, b) -> Integer.compare(b.skillCount, a.skillCount));

        int targetMask = (1 << m) - 1;
        Map<Integer, TeamInfo> dp = new HashMap<>();
        dp.put(0, new TeamInfo(0, new ArrayList<>()));

        for (PersonInfo person : validPeople) {
            Map<Integer, TeamInfo> newDp = new HashMap<>(dp);

            for (Map.Entry<Integer, TeamInfo> entry : dp.entrySet()) {
                int mask = entry.getKey();
                TeamInfo team = entry.getValue();

                int newMask = mask | person.skillMask;

                if (!newDp.containsKey(newMask) ||
                    newDp.get(newMask).size > team.size + 1) {

                    List<Integer> newTeam = new ArrayList<>(team.members);
                    newTeam.add(person.originalIndex);
                    newDp.put(newMask, new TeamInfo(team.size + 1, newTeam));
                }
            }

            dp = newDp;

            // Early termination if target reached
            if (dp.containsKey(targetMask)) {
                break;
            }
        }

        return dp.get(targetMask).members.stream().mapToInt(Integer::intValue).toArray();
    }

    private static class PersonInfo {
        int originalIndex;
        int skillMask;
        int skillCount;

        PersonInfo(int originalIndex, int skillMask, int skillCount) {
            this.originalIndex = originalIndex;
            this.skillMask = skillMask;
            this.skillCount = skillCount;
        }
    }

    private static class TeamInfo {
        int size;
        List<Integer> members;

        TeamInfo(int size, List<Integer> members) {
            this.size = size;
            this.members = members;
        }
    }

    /**
     * Approximation algorithm for large skill sets.
     * Uses greedy approach when exact DP becomes impractical.
     */
    private int[] approximateSolution(String[] req_skills, List<List<String>> people) {
        Set<String> remainingSkills = new HashSet<>(Arrays.asList(req_skills));
        List<Integer> team = new ArrayList<>();
        boolean[] used = new boolean[people.size()];

        while (!remainingSkills.isEmpty()) {
            int bestPerson = -1;
            int maxNewSkills = 0;

            // Find person who covers most remaining skills
            for (int i = 0; i < people.size(); i++) {
                if (used[i]) continue;

                int newSkills = 0;
                for (String skill : people.get(i)) {
                    if (remainingSkills.contains(skill)) {
                        newSkills++;
                    }
                }

                if (newSkills > maxNewSkills) {
                    maxNewSkills = newSkills;
                    bestPerson = i;
                }
            }

            if (bestPerson == -1) break; // No more useful people

            // Add best person to team
            team.add(bestPerson);
            used[bestPerson] = true;

            // Remove covered skills
            for (String skill : people.get(bestPerson)) {
                remainingSkills.remove(skill);
            }
        }

        return team.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * Branch and bound approach for exact solution with pruning.
     * More memory efficient than DP for sparse problems.
     */
    public int[] smallestSufficientTeamBranchBound(String[] req_skills, List<List<String>> people) {
        int m = req_skills.length;
        Map<String, Integer> skillToIndex = new HashMap<>();
        for (int i = 0; i < m; i++) {
            skillToIndex.put(req_skills[i], i);
        }

        int[] peopleSkills = new int[people.size()];
        for (int i = 0; i < people.size(); i++) {
            for (String skill : people.get(i)) {
                if (skillToIndex.containsKey(skill)) {
                    peopleSkills[i] |= (1 << skillToIndex.get(skill));
                }
            }
        }

        BranchBoundState initialState = new BranchBoundState(
            0, new ArrayList<>(), 0, Integer.MAX_VALUE
        );

        return branchAndBound(peopleSkills, (1 << m) - 1, 0, initialState).team
            .stream().mapToInt(Integer::intValue).toArray();
    }

    private BranchBoundState branchAndBound(int[] peopleSkills, int targetMask,
                                          int personIndex, BranchBoundState currentBest) {
        if (currentBest.skillMask == targetMask) {
            return currentBest;
        }

        if (personIndex >= peopleSkills.length) {
            return new BranchBoundState(-1, new ArrayList<>(), 0, Integer.MAX_VALUE);
        }

        // Pruning: if current team size >= best known size, skip
        if (currentBest.teamSize >= currentBest.bestSize) {
            return new BranchBoundState(-1, new ArrayList<>(), 0, Integer.MAX_VALUE);
        }

        // Try not including current person
        BranchBoundState withoutPerson = branchAndBound(peopleSkills, targetMask,
                                                       personIndex + 1, currentBest);

        // Try including current person
        int newMask = currentBest.skillMask | peopleSkills[personIndex];
        List<Integer> newTeam = new ArrayList<>(currentBest.team);
        newTeam.add(personIndex);

        BranchBoundState withPerson = new BranchBoundState(
            newMask, newTeam, currentBest.teamSize + 1,
            Math.min(currentBest.bestSize, withoutPerson.bestSize)
        );

        BranchBoundState withPersonResult = branchAndBound(peopleSkills, targetMask,
                                                         personIndex + 1, withPerson);

        // Return better solution
        if (withoutPerson.skillMask == targetMask && withPersonResult.skillMask == targetMask) {
            return withoutPerson.teamSize <= withPersonResult.teamSize ?
                   withoutPerson : withPersonResult;
        } else if (withoutPerson.skillMask == targetMask) {
            return withoutPerson;
        } else if (withPersonResult.skillMask == targetMask) {
            return withPersonResult;
        } else {
            return new BranchBoundState(-1, new ArrayList<>(), 0, Integer.MAX_VALUE);
        }
    }

    private static class BranchBoundState {
        int skillMask;
        List<Integer> team;
        int teamSize;
        int bestSize;

        BranchBoundState(int skillMask, List<Integer> team, int teamSize, int bestSize) {
            this.skillMask = skillMask;
            this.team = team;
            this.teamSize = teamSize;
            this.bestSize = bestSize;
        }
    }

    /**
     * ILP (Integer Linear Programming) formulation approach.
     * Formulates as set cover problem for optimal solutions.
     */
    public int[] smallestSufficientTeamILP(String[] req_skills, List<List<String>> people) {
        // This would typically use an ILP solver library
        // For demonstration, we'll use a simplified constraint-based approach

        int m = req_skills.length;
        int n = people.size();

        Map<String, Integer> skillToIndex = new HashMap<>();
        for (int i = 0; i < m; i++) {
            skillToIndex.put(req_skills[i], i);
        }

        // Create constraint matrix
        boolean[][] constraints = new boolean[m][n];
        for (int j = 0; j < n; j++) {
            for (String skill : people.get(j)) {
                if (skillToIndex.containsKey(skill)) {
                    constraints[skillToIndex.get(skill)][j] = true;
                }
            }
        }

        // Use constraint satisfaction approach
        return solveSetCover(constraints);
    }

    // Simplified set cover solver
    private int[] solveSetCover(boolean[][] constraints) {
        int m = constraints.length; // skills
        int n = constraints[0].length; // people

        boolean[] covered = new boolean[m];
        List<Integer> solution = new ArrayList<>();

        while (!allCovered(covered)) {
            int bestPerson = -1;
            int maxNewCoverage = 0;

            for (int person = 0; person < n; person++) {
                int newCoverage = 0;
                for (int skill = 0; skill < m; skill++) {
                    if (!covered[skill] && constraints[skill][person]) {
                        newCoverage++;
                    }
                }

                if (newCoverage > maxNewCoverage) {
                    maxNewCoverage = newCoverage;
                    bestPerson = person;
                }
            }

            if (bestPerson == -1) break;

            solution.add(bestPerson);
            for (int skill = 0; skill < m; skill++) {
                if (constraints[skill][bestPerson]) {
                    covered[skill] = true;
                }
            }
        }

        return solution.stream().mapToInt(Integer::intValue).toArray();
    }

    private boolean allCovered(boolean[] covered) {
        for (boolean c : covered) {
            if (!c) return false;
        }
        return true;
    }

    /**
     * Multi-objective optimization approach.
     * Considers multiple criteria like team size, skill coverage, etc.
     */
    public List<TeamSolution> findMultiObjectiveTeams(String[] req_skills,
                                                     List<List<String>> people,
                                                     TeamCriteria criteria) {
        int m = req_skills.length;
        Map<String, Integer> skillToIndex = new HashMap<>();
        for (int i = 0; i < m; i++) {
            skillToIndex.put(req_skills[i], i);
        }

        List<TeamSolution> solutions = new ArrayList<>();

        // Generate multiple solutions with different approaches
        int[] greedyTeam = approximateSolution(req_skills, people);
        solutions.add(evaluateTeam(greedyTeam, req_skills, people, criteria));

        if (m <= 16) {
            int[] optimalTeam = smallestSufficientTeam(req_skills, people);
            solutions.add(evaluateTeam(optimalTeam, req_skills, people, criteria));
        }

        // Sort by multi-objective score
        solutions.sort((a, b) -> Double.compare(b.score, a.score));

        return solutions;
    }

    private TeamSolution evaluateTeam(int[] team, String[] req_skills,
                                    List<List<String>> people, TeamCriteria criteria) {
        double sizeScore = 1.0 / (team.length + 1); // Smaller teams are better

        // Calculate skill overlap (redundancy)
        Map<String, Integer> skillCount = new HashMap<>();
        for (int person : team) {
            for (String skill : people.get(person)) {
                skillCount.put(skill, skillCount.getOrDefault(skill, 0) + 1);
            }
        }

        double redundancyPenalty = 0;
        for (int count : skillCount.values()) {
            if (count > 1) {
                redundancyPenalty += (count - 1) * 0.1;
            }
        }

        double score = criteria.sizeWeight * sizeScore -
                      criteria.redundancyWeight * redundancyPenalty;

        return new TeamSolution(team, score, team.length, redundancyPenalty);
    }

    public static class TeamCriteria {
        public final double sizeWeight;
        public final double redundancyWeight;

        public TeamCriteria(double sizeWeight, double redundancyWeight) {
            this.sizeWeight = sizeWeight;
            this.redundancyWeight = redundancyWeight;
        }
    }

    public static class TeamSolution {
        public final int[] team;
        public final double score;
        public final int size;
        public final double redundancy;

        public TeamSolution(int[] team, double score, int size, double redundancy) {
            this.team = team;
            this.score = score;
            this.size = size;
            this.redundancy = redundancy;
        }

        @Override
        public String toString() {
            return String.format("Team: %s, Size: %d, Score: %.3f, Redundancy: %.3f",
                               Arrays.toString(team), size, score, redundancy);
        }
    }

    /**
     * Dynamic team building with constraints.
     * Handles additional constraints like team size limits, person availability, etc.
     */
    public int[] buildConstrainedTeam(String[] req_skills, List<List<String>> people,
                                    TeamConstraints constraints) {
        int m = req_skills.length;
        Map<String, Integer> skillToIndex = new HashMap<>();
        for (int i = 0; i < m; i++) {
            skillToIndex.put(req_skills[i], i);
        }

        // Filter people based on availability
        List<Integer> availablePeople = new ArrayList<>();
        for (int i = 0; i < people.size(); i++) {
            if (constraints.isAvailable(i)) {
                availablePeople.add(i);
            }
        }

        // Constraint-aware DP
        if (m <= 20 && availablePeople.size() <= 50) {
            return constrainedDP(req_skills, people, availablePeople, constraints, skillToIndex);
        } else {
            return constrainedGreedy(req_skills, people, availablePeople, constraints);
        }
    }

    private int[] constrainedDP(String[] req_skills, List<List<String>> people,
                              List<Integer> availablePeople, TeamConstraints constraints,
                              Map<String, Integer> skillToIndex) {
        int m = req_skills.length;
        int targetMask = (1 << m) - 1;

        Map<Integer, ConstrainedTeamState> dp = new HashMap<>();
        dp.put(0, new ConstrainedTeamState(0, new ArrayList<>()));

        for (int person : availablePeople) {
            if (!constraints.canAddPerson(person)) continue;

            int personSkillMask = 0;
            for (String skill : people.get(person)) {
                if (skillToIndex.containsKey(skill)) {
                    personSkillMask |= (1 << skillToIndex.get(skill));
                }
            }

            Map<Integer, ConstrainedTeamState> newDp = new HashMap<>(dp);

            for (Map.Entry<Integer, ConstrainedTeamState> entry : dp.entrySet()) {
                int mask = entry.getKey();
                ConstrainedTeamState state = entry.getValue();

                if (state.team.size() >= constraints.maxTeamSize) continue;

                int newMask = mask | personSkillMask;

                List<Integer> newTeam = new ArrayList<>(state.team);
                newTeam.add(person);
                ConstrainedTeamState newState = new ConstrainedTeamState(state.team.size() + 1, newTeam);

                if (!newDp.containsKey(newMask) || newDp.get(newMask).size > newState.size) {
                    newDp.put(newMask, newState);
                }
            }

            dp = newDp;
        }

        ConstrainedTeamState solution = dp.get(targetMask);
        return solution != null ?
               solution.team.stream().mapToInt(Integer::intValue).toArray() :
               new int[0];
    }

    private int[] constrainedGreedy(String[] req_skills, List<List<String>> people,
                                  List<Integer> availablePeople, TeamConstraints constraints) {
        Set<String> remainingSkills = new HashSet<>(Arrays.asList(req_skills));
        List<Integer> team = new ArrayList<>();

        while (!remainingSkills.isEmpty() && team.size() < constraints.maxTeamSize) {
            int bestPerson = -1;
            int maxNewSkills = 0;

            for (int person : availablePeople) {
                if (team.contains(person) || !constraints.canAddPerson(person)) continue;

                int newSkills = 0;
                for (String skill : people.get(person)) {
                    if (remainingSkills.contains(skill)) {
                        newSkills++;
                    }
                }

                if (newSkills > maxNewSkills) {
                    maxNewSkills = newSkills;
                    bestPerson = person;
                }
            }

            if (bestPerson == -1) break;

            team.add(bestPerson);
            for (String skill : people.get(bestPerson)) {
                remainingSkills.remove(skill);
            }
        }

        return team.stream().mapToInt(Integer::intValue).toArray();
    }

    private static class ConstrainedTeamState {
        int size;
        List<Integer> team;

        ConstrainedTeamState(int size, List<Integer> team) {
            this.size = size;
            this.team = team;
        }
    }

    public static class TeamConstraints {
        public final int maxTeamSize;
        private final Set<Integer> unavailablePeople;
        private final Set<Integer> mandatoryPeople;

        public TeamConstraints(int maxTeamSize, Set<Integer> unavailablePeople, Set<Integer> mandatoryPeople) {
            this.maxTeamSize = maxTeamSize;
            this.unavailablePeople = unavailablePeople != null ? unavailablePeople : new HashSet<>();
            this.mandatoryPeople = mandatoryPeople != null ? mandatoryPeople : new HashSet<>();
        }

        public boolean isAvailable(int person) {
            return !unavailablePeople.contains(person);
        }

        public boolean canAddPerson(int person) {
            return isAvailable(person);
        }

        public boolean isMandatory(int person) {
            return mandatoryPeople.contains(person);
        }
    }
}
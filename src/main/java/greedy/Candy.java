package greedy;

import java.util.*;

/**
 * Problem: Candy
 *
 * Children stand in a line, each with a rating. Give every child at least one
 * candy, and give any child with a higher rating than an adjacent child more
 * candies than that neighbor. Return the minimum total candies needed.
 *
 * Leetcode: https://leetcode.com/problems/candy/ (Hard)
 * Rating:   acceptance 48.8% (Hard) - no contest Elo (pre-contest problem)
 * Pattern:  Greedy | Local constraints | Two passes and slope counting
 *
 * Example:
 *   Input:  ratings = [1,3,4,5,2]
 *   Output: 11
 *   Why:    candies [1,2,3,4,1] satisfy every neighbor rule, and the peak rating
 *           5 must beat both sides, so no smaller total can work.
 *
 * Follow-ups:
 *   1. Can this be solved in O(1) extra space?
 *      Count increasing and decreasing slopes, adding one extra candy when a down slope outgrows its peak.
 *   2. What if children are arranged in a circle?
 *      Break at a global minimum rating, then run the line solution from that valley.
 *   3. What if equal ratings must receive equal candies?
 *      Compress equal-rating runs and apply the neighbor rule between groups.
 *   4. What if each child has a maximum candy cap?
 *      Run the greedy assignment, then validate caps; some inputs become impossible.
 *
 * Related: Gas Station (134), Minimum Number of Arrows to Burst Balloons (452).
 */
public class Candy {

    public static void main(String[] args) {
        Candy solver = new Candy();
        int[][] inputs = { {}, {1, 0, 2}, {1, 2, 2}, {1, 3, 4, 5, 2} };
        int[] expected = {0, 5, 4, 11};

        for (int i = 0; i < inputs.length; i++) {
            int got = solver.candy(inputs[i]);
            System.out.printf("ratings=%s -> %d  expected=%d%n",
                Arrays.toString(inputs[i]), got, expected[i]);
        }
    }

    /**
     * Intuition: the naive "fix both neighbors at once" gets tangled because a
     * child can be constrained from both sides. Split it into two one-sided
     * facts. A rising edge left-to-right forces exactly one more candy than the
     * left neighbor; anything extra is waste. A falling edge is the same fact
     * seen right-to-left. The exchange argument is local: once a child has enough
     * for one side, raising it to the minimum the other side needs never breaks a
     * neighbor rule, so taking the max of the two one-sided requirements is
     * optimal.
     *
     * Algorithm:
     *   1. Give every child one candy.
     *   2. Left-to-right: if ratings[i] > ratings[i-1], candies[i] = candies[i-1] + 1.
     *   3. Right-to-left: if ratings[i] > ratings[i+1], candies[i] = max(candies[i], candies[i+1] + 1).
     *   4. Sum the candies; each child now satisfies both neighbor rules.
     *
     * Time:  O(n) - two linear passes plus one linear sum, each child touched a constant number of times.
     * Space: O(n) - one candy count stored per child.
     */
    public int candy(int[] ratings) {
        int n = ratings.length;
        if (n <= 1) return n;
        
        int[] candies = new int[n];
        Arrays.fill(candies, 1);
        
        // Left to right pass
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }
        
        // Right to left pass
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
        }
        
        return Arrays.stream(candies).sum();
    }
    
    /**
     * Intuition (O(1) space): the candy array only ever stores mountain shapes.
     * In a strictly rising run the cheapest candies are 1, 2, 3, ..., and in a
     * strictly falling run they are the same staircase reversed. So we can add
     * slope lengths directly instead of remembering every child. The one shared
     * child is the peak between an up slope and a down slope: it must be tall
     * enough for the longer side, so when the old peak already covers the down
     * slope we subtract the candy the down count would otherwise double-add. Flat
     * edges impose no order, so they reset the mountain to a single candy.
     *
     * Algorithm:
     *   1. Track running up-slope, down-slope, and peak lengths; start the total at 1.
     *   2. On an ascending step, extend the up slope and add (up + 1) candies.
     *   3. On a descending step, extend the down slope and add (down + 1); if the
     *      peak already covers the down slope, subtract one to avoid double paying.
     *   4. On an equal step, reset the slopes and add a single candy.
     *
     * Time:  O(n) - one scan processes each adjacent pair once.
     * Space: O(1) - only slope counters and the running total are kept.
     */
    public int candyOptimal(int[] ratings) {
        if (ratings.length <= 1) return ratings.length;
        
        int totalCandies = 1;
        int up = 0;     // Length of current ascending sequence
        int down = 0;   // Length of current descending sequence
        int peak = 0;   // Height of current peak
        
        for (int i = 1; i < ratings.length; i++) {
            if (ratings[i] > ratings[i - 1]) {
                // Ascending
                peak = ++up;
                down = 0;
                totalCandies += 1 + up;
            } else if (ratings[i] < ratings[i - 1]) {
                // Descending
                up = 0;
                down++;
                totalCandies += 1 + down;
                
                // If peak is not tall enough, add extra candy
                if (peak >= down) {
                    totalCandies--;
                }
            } else {
                // Equal ratings
                peak = up = down = 0;
                totalCandies += 1;
            }
        }
        
        return totalCandies;
    }
    
    /**
     * Segment-based approach for better understanding.
     * Divides array into monotonic segments and processes each.
     */
    public int candySegmented(int[] ratings) {
        if (ratings.length <= 1) return ratings.length;
        
        List<Segment> segments = identifySegments(ratings);
        return calculateCandiesFromSegments(segments);
    }
    
    private List<Segment> identifySegments(int[] ratings) {
        List<Segment> segments = new ArrayList<>();
        int start = 0;
        
        while (start < ratings.length) {
            int end = start;
            SegmentType type = SegmentType.FLAT;
            
            // Identify segment type and end
            if (end + 1 < ratings.length) {
                if (ratings[end + 1] > ratings[end]) {
                    type = SegmentType.ASCENDING;
                    while (end + 1 < ratings.length && ratings[end + 1] > ratings[end]) {
                        end++;
                    }
                } else if (ratings[end + 1] < ratings[end]) {
                    type = SegmentType.DESCENDING;
                    while (end + 1 < ratings.length && ratings[end + 1] < ratings[end]) {
                        end++;
                    }
                } else {
                    type = SegmentType.FLAT;
                    while (end + 1 < ratings.length && ratings[end + 1] == ratings[end]) {
                        end++;
                    }
                }
            }
            
            segments.add(new Segment(start, end, type));
            start = end + 1;
        }
        
        return segments;
    }
    
    private int calculateCandiesFromSegments(List<Segment> segments) {
        int totalCandies = 0;
        int[] candies = new int[getCombinedLength(segments)];
        Arrays.fill(candies, 1);
        
        for (Segment segment : segments) {
            switch (segment.type) {
                case ASCENDING:
                    for (int i = segment.start + 1; i <= segment.end; i++) {
                        candies[i] = candies[i - 1] + 1;
                    }
                    break;
                case DESCENDING:
                    for (int i = segment.end - 1; i >= segment.start; i--) {
                        candies[i] = Math.max(candies[i], candies[i + 1] + 1);
                    }
                    break;
                case FLAT:
                    // All remain 1, no additional processing needed
                    break;
            }
        }
        
        return Arrays.stream(candies).sum();
    }
    
    private int getCombinedLength(List<Segment> segments) {
        return segments.isEmpty() ? 0 : segments.get(segments.size() - 1).end + 1;
    }
    
    private enum SegmentType {
        ASCENDING, DESCENDING, FLAT
    }
    
    private static class Segment {
        int start, end;
        SegmentType type;
        
        Segment(int start, int end, SegmentType type) {
            this.start = start;
            this.end = end;
            this.type = type;
        }
        
        @Override
        public String toString() {
            return String.format("[%d-%d: %s]", start, end, type);
        }
    }
    
    /**
     * Circular arrangement variation.
     * Handles case where children are arranged in a circle.
     */
    public int candyCircular(int[] ratings) {
        int n = ratings.length;
        if (n <= 1) return n;
        
        // Handle circular constraint by considering array as extended
        int[] extendedRatings = new int[2 * n];
        System.arraycopy(ratings, 0, extendedRatings, 0, n);
        System.arraycopy(ratings, 0, extendedRatings, n, n);
        
        int[] candies = new int[2 * n];
        Arrays.fill(candies, 1);
        
        // Apply two-pass algorithm on extended array
        // Left to right
        for (int i = 1; i < 2 * n; i++) {
            if (extendedRatings[i] > extendedRatings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }
        
        // Right to left
        for (int i = 2 * n - 2; i >= 0; i--) {
            if (extendedRatings[i] > extendedRatings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
        }
        
        // Take minimum of corresponding positions in both halves
        int totalCandies = 0;
        for (int i = 0; i < n; i++) {
            totalCandies += Math.min(candies[i], candies[i + n]);
        }
        
        return totalCandies;
    }
    
    /**
     * Optimized approach for arrays with many equal consecutive ratings.
     * Groups consecutive equal ratings for efficiency.
     */
    public int candyWithGroups(int[] ratings) {
        if (ratings.length <= 1) return ratings.length;
        
        List<Group> groups = compressToGroups(ratings);
        return calculateCandiesFromGroups(groups);
    }
    
    private List<Group> compressToGroups(int[] ratings) {
        List<Group> groups = new ArrayList<>();
        int start = 0;
        
        while (start < ratings.length) {
            int end = start;
            while (end + 1 < ratings.length && ratings[end + 1] == ratings[end]) {
                end++;
            }
            
            groups.add(new Group(ratings[start], end - start + 1));
            start = end + 1;
        }
        
        return groups;
    }
    
    private int calculateCandiesFromGroups(List<Group> groups) {
        if (groups.isEmpty()) return 0;
        if (groups.size() == 1) return groups.get(0).count;
        
        int[] candiesPerGroup = new int[groups.size()];
        Arrays.fill(candiesPerGroup, 1);
        
        // Left to right pass
        for (int i = 1; i < groups.size(); i++) {
            if (groups.get(i).rating > groups.get(i - 1).rating) {
                candiesPerGroup[i] = candiesPerGroup[i - 1] + 1;
            }
        }
        
        // Right to left pass
        for (int i = groups.size() - 2; i >= 0; i--) {
            if (groups.get(i).rating > groups.get(i + 1).rating) {
                candiesPerGroup[i] = Math.max(candiesPerGroup[i], candiesPerGroup[i + 1] + 1);
            }
        }
        
        // Calculate total candies
        int total = 0;
        for (int i = 0; i < groups.size(); i++) {
            total += candiesPerGroup[i] * groups.get(i).count;
        }
        
        return total;
    }
    
    private static class Group {
        int rating;
        int count;
        
        Group(int rating, int count) {
            this.rating = rating;
            this.count = count;
        }
        
        @Override
        public String toString() {
            return String.format("(%d×%d)", rating, count);
        }
    }
    
    /**
     * Multi-constraint candy distribution.
     * Handles additional constraints like maximum candies per child.
     */
    public int candyWithConstraints(int[] ratings, int maxCandiesPerChild) {
        int n = ratings.length;
        if (n <= 1) return n;
        
        int[] candies = new int[n];
        Arrays.fill(candies, 1);
        
        // Modified two-pass with constraint checking
        // Left to right
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = Math.min(maxCandiesPerChild, candies[i - 1] + 1);
            }
        }
        
        // Right to left
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                int requiredCandies = Math.min(maxCandiesPerChild, candies[i + 1] + 1);
                candies[i] = Math.max(candies[i], requiredCandies);
            }
        }
        
        // Verify constraints are satisfiable
        if (!validateConstraints(ratings, candies, maxCandiesPerChild)) {
            return -1; // Impossible to satisfy constraints
        }
        
        return Arrays.stream(candies).sum();
    }
    
    private boolean validateConstraints(int[] ratings, int[] candies, int maxCandiesPerChild) {
        for (int i = 0; i < ratings.length; i++) {
            if (candies[i] > maxCandiesPerChild) {
                return false;
            }
            
            // Check left neighbor constraint
            if (i > 0 && ratings[i] > ratings[i - 1] && candies[i] <= candies[i - 1]) {
                return false;
            }
            
            // Check right neighbor constraint
            if (i < ratings.length - 1 && ratings[i] > ratings[i + 1] && candies[i] <= candies[i + 1]) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Dynamic programming approach for comparison.
     * More intuitive but less efficient than greedy approaches.
     */
    public int candyDP(int[] ratings) {
        int n = ratings.length;
        if (n <= 1) return n;
        
        // dp[i] = minimum candies for child i
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        
        boolean changed = true;
        while (changed) {
            changed = false;
            
            // Check all constraints
            for (int i = 0; i < n; i++) {
                // Check left constraint
                if (i > 0 && ratings[i] > ratings[i - 1] && dp[i] <= dp[i - 1]) {
                    dp[i] = dp[i - 1] + 1;
                    changed = true;
                }
                
                // Check right constraint
                if (i < n - 1 && ratings[i] > ratings[i + 1] && dp[i] <= dp[i + 1]) {
                    dp[i] = dp[i + 1] + 1;
                    changed = true;
                }
            }
        }
        
        return Arrays.stream(dp).sum();
    }
    
    /**
     * Analysis and debugging utilities.
     * Tools for validating and analyzing candy distributions.
     */
    public static class CandyAnalyzer {
        
        public static AnalysisResult analyze(int[] ratings) {
            Candy solver = new Candy();
            
            long startTime = System.nanoTime();
            int twoPassResult = solver.candy(ratings);
            long twoPassTime = System.nanoTime() - startTime;
            
            startTime = System.nanoTime();
            int optimalResult = solver.candyOptimal(ratings);
            long optimalTime = System.nanoTime() - startTime;
            
            int[] optimalDistribution = calculateOptimalDistribution(ratings);
            
            return new AnalysisResult(
                twoPassResult,
                optimalResult,
                twoPassTime / 1_000_000.0,
                optimalTime / 1_000_000.0,
                optimalDistribution,
                validateDistribution(ratings, optimalDistribution)
            );
        }
        
        private static int[] calculateOptimalDistribution(int[] ratings) {
            int n = ratings.length;
            int[] candies = new int[n];
            Arrays.fill(candies, 1);
            
            // Standard two-pass algorithm
            for (int i = 1; i < n; i++) {
                if (ratings[i] > ratings[i - 1]) {
                    candies[i] = candies[i - 1] + 1;
                }
            }
            
            for (int i = n - 2; i >= 0; i--) {
                if (ratings[i] > ratings[i + 1]) {
                    candies[i] = Math.max(candies[i], candies[i + 1] + 1);
                }
            }
            
            return candies;
        }
        
        private static boolean validateDistribution(int[] ratings, int[] candies) {
            if (ratings.length != candies.length) return false;
            
            for (int i = 0; i < ratings.length; i++) {
                if (candies[i] < 1) return false;
                
                if (i > 0 && ratings[i] > ratings[i - 1] && candies[i] <= candies[i - 1]) {
                    return false;
                }
                
                if (i < ratings.length - 1 && ratings[i] > ratings[i + 1] && candies[i] <= candies[i + 1]) {
                    return false;
                }
            }
            
            return true;
        }
        
        public static class AnalysisResult {
            public final int twoPassResult;
            public final int optimalResult;
            public final double twoPassTimeMs;
            public final double optimalTimeMs;
            public final int[] distribution;
            public final boolean isValid;
            
            AnalysisResult(int twoPassResult, int optimalResult, double twoPassTimeMs,
                         double optimalTimeMs, int[] distribution, boolean isValid) {
                this.twoPassResult = twoPassResult;
                this.optimalResult = optimalResult;
                this.twoPassTimeMs = twoPassTimeMs;
                this.optimalTimeMs = optimalTimeMs;
                this.distribution = distribution;
                this.isValid = isValid;
            }
            
            @Override
            public String toString() {
                return String.format(
                    "Two-pass: %d (%.3fms), Optimal: %d (%.3fms), Match: %s, Valid: %s\nDistribution: %s",
                    twoPassResult, twoPassTimeMs, optimalResult, optimalTimeMs,
                    twoPassResult == optimalResult, isValid,
                    Arrays.toString(distribution)
                );
            }
        }
    }
    
    /**
     * Advanced variations and extensions.
     */
    public static class CandyVariations {
        
        /**
         * Candy distribution with budget constraint.
         * Find maximum number of children that can be satisfied within budget.
         */
        public int maxChildrenWithBudget(int[] ratings, int budget) {
            // Calculate minimum required candies
            Candy solver = new Candy();
            int[] optimalDistribution = CandyAnalyzer.calculateOptimalDistribution(ratings);
            
            // Create priority queue of (child_index, required_candies)
            PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
            
            for (int i = 0; i < optimalDistribution.length; i++) {
                pq.offer(new int[]{i, optimalDistribution[i]});
            }
            
            int totalCost = 0;
            int children = 0;
            Set<Integer> satisfied = new HashSet<>();
            
            while (!pq.isEmpty() && totalCost + pq.peek()[1] <= budget) {
                int[] child = pq.poll();
                int index = child[0];
                int cost = child[1];
                
                // Check if we can satisfy this child without violating constraints
                if (canSatisfyChild(ratings, satisfied, index)) {
                    totalCost += cost;
                    children++;
                    satisfied.add(index);
                }
            }
            
            return children;
        }
        
        private boolean canSatisfyChild(int[] ratings, Set<Integer> satisfied, int index) {
            // Check if satisfying this child violates any neighbor constraints
            
            // Left neighbor
            if (index > 0 && satisfied.contains(index - 1)) {
                if (ratings[index] > ratings[index - 1] || ratings[index - 1] > ratings[index]) {
                    // Would require coordination with neighbor's candy count
                    return false;
                }
            }
            
            // Right neighbor  
            if (index < ratings.length - 1 && satisfied.contains(index + 1)) {
                if (ratings[index] > ratings[index + 1] || ratings[index + 1] > ratings[index]) {
                    return false;
                }
            }
            
            return true;
        }
        
        /**
         * Candy distribution with fairness constraint.
         * Minimize the maximum number of candies any child receives.
         */
        public int minimizeMaxCandies(int[] ratings) {
            int left = 1, right = ratings.length;
            int result = right;
            
            while (left <= right) {
                int mid = left + (right - left) / 2;
                
                if (canDistributeWithMaxCandies(ratings, mid)) {
                    result = mid;
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            
            return result;
        }
        
        private boolean canDistributeWithMaxCandies(int[] ratings, int maxCandies) {
            int n = ratings.length;
            int[] candies = new int[n];
            Arrays.fill(candies, 1);
            
            // Modified two-pass with max constraint
            for (int i = 1; i < n; i++) {
                if (ratings[i] > ratings[i - 1]) {
                    candies[i] = Math.min(maxCandies, candies[i - 1] + 1);
                }
            }
            
            for (int i = n - 2; i >= 0; i--) {
                if (ratings[i] > ratings[i + 1]) {
                    int required = Math.min(maxCandies, candies[i + 1] + 1);
                    candies[i] = Math.max(candies[i], required);
                }
            }
            
            // Validate all constraints are satisfied
            return CandyAnalyzer.validateDistribution(ratings, candies);
        }
    }
}
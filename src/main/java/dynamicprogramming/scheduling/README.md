# Scheduling Pattern

## Pattern Overview

The **Scheduling** pattern involves selecting and scheduling tasks, jobs, or activities to optimize some objective (maximize profit, minimize time) while respecting constraints (deadlines, conflicts, dependencies).

## Core Characteristics

- **Tasks/Jobs**: Set of activities with properties (start time, end time, profit, deadline)
- **Constraints**: Time conflicts, dependencies, limited resources
- **Optimization**: Maximize profit, minimize time, or satisfy constraints
- **Sorting**: Often requires sorting by time, deadline, or other criteria

## When to Recognize Scheduling Pattern

Look for these indicators:
1. Problem involves **scheduling tasks** or **selecting jobs**
2. Tasks have **time constraints** (start, end, deadline)
3. Need to **optimize** profit, time, or number of tasks
4. **Conflicts** or **dependencies** between tasks

Example phrases: "job scheduling", "course schedule", "meeting rooms", "task deadline", "interval scheduling"

## Generic Steps to Solve Scheduling Problems

### Step 1: Understand Task Properties
- **Time properties**: start time, end time, duration, deadline
- **Value**: profit, weight, priority
- **Constraints**: conflicts, prerequisites, capacity limits

### Step 2: Choose Sorting Strategy
Common sorting approaches:
- **By end time**: For interval scheduling maximization
- **By deadline**: For deadline-based scheduling
- **By profit/weight**: For weighted job scheduling
- **By start time**: For interval overlap problems

### Step 3: Define DP State
```
dp[i] = optimal solution considering first i tasks

Or:
dp[time] = optimal solution up to given time

Or:
dp[i][constraint] = solution for i tasks with constraint value
```

### Step 4: Derive Recurrence Relation

**General Pattern**:
```java
for (int i = 0; i < n; i++) {
    // Option 1: Skip current task
    int skip = dp[previous];
    
    // Option 2: Include current task
    int include = task[i].profit + dp[last_non_conflicting];
    
    dp[i] = max(skip, include);
}
```

## Common Problem Templates

### Template 1: Weighted Job Scheduling (Non-Overlapping)

**Problem**: Select non-overlapping jobs to maximize profit.

```java
class Solution {
    public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
        int n = startTime.length;
        
        // Create job objects and sort by end time
        int[][] jobs = new int[n][3];
        for (int i = 0; i < n; i++) {
            jobs[i] = new int[]{startTime[i], endTime[i], profit[i]};
        }
        Arrays.sort(jobs, (a, b) -> a[1] - b[1]);  // Sort by end time
        
        // dp[i] = max profit using jobs 0..i
        int[] dp = new int[n];
        dp[0] = jobs[0][2];  // First job's profit
        
        for (int i = 1; i < n; i++) {
            // Option 1: Skip current job
            int skip = dp[i-1];
            
            // Option 2: Include current job
            int include = jobs[i][2];  // Current profit
            
            // Find last non-conflicting job
            int lastNonConflict = findLastNonConflicting(jobs, i);
            if (lastNonConflict != -1) {
                include += dp[lastNonConflict];
            }
            
            dp[i] = Math.max(skip, include);
        }
        
        return dp[n-1];
    }
    
    // Binary search for last job that doesn't conflict
    private int findLastNonConflicting(int[][] jobs, int current) {
        int left = 0, right = current - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (jobs[mid][1] <= jobs[current][0]) {  // end <= start
                result = mid;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
}
```

**Key Insight**: Sort by end time, use binary search to find last non-conflicting job.

### Template 2: Course Schedule III (Greedy + Heap)

**Problem**: Maximum courses you can take given duration and deadline.

```java
class Solution {
    public int scheduleCourse(int[][] courses) {
        // Sort by deadline
        Arrays.sort(courses, (a, b) -> a[1] - b[1]);
        
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        int currentTime = 0;
        
        for (int[] course : courses) {
            int duration = course[0];
            int deadline = course[1];
            
            // Try to add current course
            currentTime += duration;
            maxHeap.offer(duration);
            
            // If we exceed deadline, remove longest course
            if (currentTime > deadline) {
                currentTime -= maxHeap.poll();
            }
        }
        
        return maxHeap.size();
    }
}
```

**Key Insight**: Greedily take courses, remove longest if deadline exceeded.

### Template 3: Minimum Difficulty of Job Schedule

**Problem**: Distribute jobs over d days to minimize difficulty.

```java
class Solution {
    public int minDifficulty(int[] jobDifficulty, int d) {
        int n = jobDifficulty.length;
        if (n < d) return -1;  // Can't schedule
        
        // dp[day][job] = min difficulty to complete jobs 0..job in day days
        int[][] dp = new int[d + 1][n];
        
        // Initialize with large values
        for (int[] row : dp) {
            Arrays.fill(row, Integer.MAX_VALUE / 2);
        }
        
        // Base case: schedule jobs 0..i in 1 day
        dp[1][0] = jobDifficulty[0];
        for (int i = 1; i < n; i++) {
            dp[1][i] = Math.max(dp[1][i-1], jobDifficulty[i]);
        }
        
        // Fill DP table
        for (int day = 2; day <= d; day++) {
            for (int job = day - 1; job < n; job++) {  // At least 'day' jobs needed
                int maxDifficulty = 0;
                
                // Try ending day 'day' at job 'job'
                // Previous day ends at 'prev', current day is [prev+1, job]
                for (int prev = job; prev >= day - 1; prev--) {
                    maxDifficulty = Math.max(maxDifficulty, jobDifficulty[prev]);
                    dp[day][job] = Math.min(dp[day][job], 
                                           dp[day-1][prev-1] + maxDifficulty);
                }
            }
        }
        
        return dp[d][n-1];
    }
}
```

**Key Insight**: Try all ways to partition jobs into d days, track max difficulty per day.

### Template 4: Video Stitching (Interval Covering)

**Problem**: Minimum clips to cover [0, time] interval.

```java
class Solution {
    public int videoStitching(int[][] clips, int time) {
        // dp[t] = minimum clips needed to cover [0, t]
        int[] dp = new int[time + 1];
        Arrays.fill(dp, Integer.MAX_VALUE / 2);
        dp[0] = 0;
        
        for (int t = 1; t <= time; t++) {
            for (int[] clip : clips) {
                int start = clip[0];
                int end = clip[1];
                
                // If clip covers current time point
                if (start <= t && t <= end) {
                    dp[t] = Math.min(dp[t], dp[start] + 1);
                }
            }
        }
        
        return dp[time] == Integer.MAX_VALUE / 2 ? -1 : dp[time];
    }
}
```

**Alternative Greedy Approach** - O(n):
```java
public int videoStitching(int[][] clips, int time) {
    // Sort by start time
    Arrays.sort(clips, (a, b) -> a[0] - b[0]);
    
    int count = 0;
    int currentEnd = 0;
    int nextEnd = 0;
    int i = 0;
    
    while (currentEnd < time) {
        // Find clip that extends furthest from current position
        while (i < clips.length && clips[i][0] <= currentEnd) {
            nextEnd = Math.max(nextEnd, clips[i][1]);
            i++;
        }
        
        // No clip can extend current coverage
        if (nextEnd == currentEnd) return -1;
        
        count++;
        currentEnd = nextEnd;
    }
    
    return count;
}
```

## Common Patterns and Strategies

### 1. **Interval Scheduling Maximization**
- **Sort by end time**
- Greedily select non-overlapping intervals
- Or use DP with binary search for last non-conflicting

### 2. **Deadline-Based Scheduling**
- **Sort by deadline**
- Use priority queue/heap to manage selections
- Remove low-value items if deadline exceeded

### 3. **Partitioning Tasks into Days/Groups**
- **Use 2D DP**: dp[day][task]
- Try all partition points
- Track constraints per partition

### 4. **Interval Covering**
- **Sort by start time**
- Greedy: extend coverage as far as possible
- Or DP: minimum intervals to cover each point

## Sorting Strategies

| Problem Type | Sort By | Why |
|-------------|---------|-----|
| Weighted job scheduling | End time | Easy to find non-conflicting jobs |
| Deadline-based | Deadline | Process in deadline order |
| Interval covering | Start time | Extend coverage greedily |
| Meeting rooms | Start time | Check overlaps sequentially |

## Time and Space Complexity

**Typical Complexities**:
- **Sorting**: O(n log n)
- **DP**: O(n²) or O(n × d) for multi-dimensional
- **Greedy with heap**: O(n log n)
- **Space**: O(n) or O(n × d) for DP table

## Common Mistakes to Avoid

1. **Wrong sorting criteria**: Choose sorting based on problem needs
2. **Not handling conflicts**: Check for overlaps/conflicts correctly
3. **Greedy without proof**: Ensure greedy approach is optimal
4. **Off-by-one in intervals**: Be careful with inclusive/exclusive bounds
5. **Missing edge cases**: Empty input, impossible schedules

## Problem-Solving Checklist

- [ ] What are we scheduling? (jobs, courses, intervals)
- [ ] What properties do tasks have? (time, profit, deadline)
- [ ] What constraints exist? (conflicts, dependencies)
- [ ] What's the objective? (max profit, min time, max tasks)
- [ ] Choose sorting strategy (end time, deadline, start time)
- [ ] Greedy or DP? (Greedy if optimal substructure is clear)
- [ ] For DP: define state and recurrence
- [ ] Handle edge cases (impossible schedule, empty input)

## Related Patterns

- **Interval Problems**: Overlapping intervals, merging
- **Knapsack**: Some scheduling problems reduce to knapsack
- **Greedy**: Many scheduling problems have greedy solutions

## Practice Problems in this Package

1. **Maximum Profit Job Scheduling** - Select non-overlapping jobs for max profit
2. **Course Schedule III** - Max courses within deadlines
3. **Minimum Difficulty of Job Schedule** - Distribute jobs over d days
4. **Video Stitching** - Minimum clips to cover time interval

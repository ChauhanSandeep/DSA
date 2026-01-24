# Grid Path Pattern

## Pattern Overview

The **Grid Path** pattern involves navigating through a 2D grid (matrix) to find optimal paths, count ways, or minimize/maximize some value. Movement is typically restricted to certain directions (right, down, diagonal).

## Core Characteristics

- **2D Grid**: Problems involve a matrix or grid structure
- **Path Finding**: Navigate from start to end (usually top-left to bottom-right)
- **Movement Constraints**: Limited directions (e.g., only right and down)
- **Optimization**: Minimize cost, maximize value, or count paths

## When to Recognize Grid Path Pattern

Look for these indicators:
1. Problem involves a **2D matrix/grid**
2. Need to find **path** from source to destination
3. **Movement restricted** to specific directions
4. **Optimize** path sum, count paths, or satisfy constraints

Example phrases: "minimum path sum", "unique paths", "dungeon game", "robot paths", "grid traversal"

## Generic Steps to Solve Grid Path Problems

### Step 1: Understand the Grid and Movement Rules
- **Grid dimensions**: m × n
- **Starting point**: Usually (0, 0) - top-left
- **Ending point**: Usually (m-1, n-1) - bottom-right
- **Allowed moves**: Right, down, diagonal? Any direction?
- **Constraints**: Obstacles? Costs? Special cells?

### Step 2: Define DP State
```
dp[i][j] = optimal solution to reach cell (i, j)

Examples:
- dp[i][j] = minimum path sum to reach (i, j)
- dp[i][j] = number of unique paths to reach (i, j)
- dp[i][j] = maximum health/gold at (i, j)
```

### Step 3: Establish Base Cases
```java
// Starting cell
dp[0][0] = grid[0][0];  // or 1 for counting paths

// First row (can only come from left)
for (int j = 1; j < n; j++) {
    dp[0][j] = dp[0][j-1] + grid[0][j];
}

// First column (can only come from above)
for (int i = 1; i < m; i++) {
    dp[i][0] = dp[i-1][0] + grid[i][0];
}
```

### Step 4: Derive Recurrence Relation

**For moves Right and Down only**:
```java
dp[i][j] = grid[i][j] + optimize(
    dp[i-1][j],  // came from above
    dp[i][j-1]   // came from left
);

// optimize = min for minimum path sum
// optimize = max for maximum path sum
// optimize = + for counting paths
```

**For moves in 4 directions** (requires different approach):
```java
// Use BFS, DFS with memoization, or Dijkstra's algorithm
// Standard DP doesn't work well with unrestricted movement
```

### Step 5: Implementation Templates

**Template 1: Minimum/Maximum Path Sum**
```java
public int pathSum(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[][] dp = new int[m][n];
    
    // Base case: starting cell
    dp[0][0] = grid[0][0];
    
    // Fill first row
    for (int j = 1; j < n; j++) {
        dp[0][j] = dp[0][j-1] + grid[0][j];
    }
    
    // Fill first column
    for (int i = 1; i < m; i++) {
        dp[i][0] = dp[i-1][0] + grid[i][0];
    }
    
    // Fill rest of grid
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            // Min for minimum path, max for maximum path
            dp[i][j] = grid[i][j] + Math.min(dp[i-1][j], dp[i][j-1]);
        }
    }
    
    return dp[m-1][n-1];
}
```

**Template 2: Count Unique Paths**
```java
public int uniquePaths(int m, int n) {
    int[][] dp = new int[m][n];
    
    // Base case: one way to reach any cell in first row/column
    for (int j = 0; j < n; j++) dp[0][j] = 1;
    for (int i = 0; i < m; i++) dp[i][0] = 1;
    
    // Fill rest of grid
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            // Ways from above + ways from left
            dp[i][j] = dp[i-1][j] + dp[i][j-1];
        }
    }
    
    return dp[m-1][n-1];
}
```

**Template 3: With Obstacles**
```java
public int uniquePathsWithObstacles(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[][] dp = new int[m][n];
    
    // If start or end is obstacle, return 0
    if (grid[0][0] == 1 || grid[m-1][n-1] == 1) return 0;
    
    dp[0][0] = 1;
    
    // Fill first row (stop at first obstacle)
    for (int j = 1; j < n; j++) {
        dp[0][j] = (grid[0][j] == 1) ? 0 : dp[0][j-1];
    }
    
    // Fill first column (stop at first obstacle)
    for (int i = 1; i < m; i++) {
        dp[i][0] = (grid[i][0] == 1) ? 0 : dp[i-1][0];
    }
    
    // Fill rest of grid
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            if (grid[i][j] == 1) {
                dp[i][j] = 0;  // Obstacle
            } else {
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
            }
        }
    }
    
    return dp[m-1][n-1];
}
```

**Template 4: Space-Optimized (1D DP)**
```java
public int pathSumOptimized(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[] dp = new int[n];
    
    // Initialize first row
    dp[0] = grid[0][0];
    for (int j = 1; j < n; j++) {
        dp[j] = dp[j-1] + grid[0][j];
    }
    
    // Process row by row
    for (int i = 1; i < m; i++) {
        dp[0] += grid[i][0];  // First column
        
        for (int j = 1; j < n; j++) {
            // dp[j] has value from above (previous row)
            // dp[j-1] has value from left (current row)
            dp[j] = grid[i][j] + Math.min(dp[j], dp[j-1]);
        }
    }
    
    return dp[n-1];
}
```

## Common Problem Variations

### 1. **Minimum Path Sum**
```java
// Find path with minimum sum from top-left to bottom-right
dp[i][j] = grid[i][j] + Math.min(dp[i-1][j], dp[i][j-1]);
```

### 2. **Unique Paths**
```java
// Count number of paths from top-left to bottom-right
dp[i][j] = dp[i-1][j] + dp[i][j-1];
```

### 3. **Dungeon Game** (Minimum health needed)
```java
// Work backwards from end to start
// dp[i][j] = minimum health needed at (i, j) to reach end
dp[i][j] = Math.max(1, 
    Math.min(dp[i+1][j], dp[i][j+1]) - dungeon[i][j]
);
```

### 4. **Triangle Minimum Path**
```java
// Triangle: each row has i+1 elements
// Can move to [i+1][j] or [i+1][j+1]
dp[i][j] = triangle[i][j] + Math.min(dp[i+1][j], dp[i+1][j+1]);
```

### 5. **Longest Increasing Path in Matrix**
```java
// Can move in 4 directions if next cell is greater
// Use DFS with memoization
int dfs(int i, int j, int[][] matrix, int[][] memo) {
    if (memo[i][j] != 0) return memo[i][j];
    
    int maxLen = 1;
    for (direction : directions) {
        int ni = i + direction[0], nj = j + direction[1];
        if (valid(ni, nj) && matrix[ni][nj] > matrix[i][j]) {
            maxLen = Math.max(maxLen, 1 + dfs(ni, nj, matrix, memo));
        }
    }
    
    memo[i][j] = maxLen;
    return maxLen;
}
```

## Direction Handling

**2 Directions (Right, Down)**:
```java
int[][] directions = {{0, 1}, {1, 0}};
```

**4 Directions (Up, Down, Left, Right)**:
```java
int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
```

**8 Directions (Including Diagonals)**:
```java
int[][] directions = {
    {-1, -1}, {-1, 0}, {-1, 1},
    {0, -1},           {0, 1},
    {1, -1},  {1, 0},  {1, 1}
};
```

**Boundary Check**:
```java
boolean isValid(int i, int j, int m, int n) {
    return i >= 0 && i < m && j >= 0 && j < n;
}
```

## Space Optimization Techniques

### 1. **1D DP Array** - O(n) space
Only keep one row instead of full 2D array.

### 2. **In-Place Modification** - O(1) space
Modify input grid if allowed:
```java
// Use grid itself as DP table
for (int i = 1; i < m; i++) {
    for (int j = 1; j < n; j++) {
        grid[i][j] += Math.min(grid[i-1][j], grid[i][j-1]);
    }
}
return grid[m-1][n-1];
```

### 3. **Two Variables** - O(1) space
For simple paths, sometimes just track current and previous value.

## Time and Space Complexity

**Typical Complexities**:
- **Time**: O(m × n) - visit each cell once
- **Space**: 
  - Full DP: O(m × n)
  - Space-optimized: O(n) or O(min(m, n))
  - In-place: O(1)

## Common Mistakes to Avoid

1. **Not handling first row/column separately**: They have only one way to reach
2. **Wrong direction dependencies**: Make sure dp[i][j] depends on already-computed cells
3. **Forgetting obstacles**: Check for obstacles before computing dp value
4. **Integer overflow**: For counting problems with large grids
5. **Off-by-one errors**: Be careful with array indices and boundaries

## Problem-Solving Checklist

- [ ] Understand grid dimensions and starting/ending points
- [ ] Identify allowed movement directions
- [ ] Define what dp[i][j] represents
- [ ] Initialize first row and first column correctly
- [ ] Handle obstacles or special cells
- [ ] Derive recurrence based on movement directions
- [ ] Consider space optimization (1D DP)
- [ ] Check for edge cases (1×1 grid, all obstacles)
- [ ] Verify boundary conditions

## Related Patterns

- **Graph DP**: Generalization to arbitrary graphs
- **Matrix Chain Multiplication**: Also involves 2D DP but different structure
- **Sequence DP**: 1D version of grid DP

## Practice Problems in this Package

1. **Minimum Path Sum** - Find path with minimum sum
2. **Minimum Path Sum Triangle** - Triangle-shaped grid
3. **Dungeon Game** - Minimum health needed to reach end
4. **Longest Increasing Path in a Matrix** - Longest increasing path

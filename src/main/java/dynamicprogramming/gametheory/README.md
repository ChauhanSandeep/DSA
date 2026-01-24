# Game Theory Pattern

## Pattern Overview

The **Game Theory** pattern involves two-player games where both players play optimally. The goal is to determine the winner or optimal strategy when both players make the best possible moves.

## Core Characteristics

- **Two Players**: Usually Alice and Bob, or Player 1 and Player 2
- **Optimal Play**: Both players play optimally to maximize their own score
- **Turn-Based**: Players alternate turns
- **Zero-Sum**: One player's gain is the other's loss (usually)
- **Minimax**: Maximize your score while minimizing opponent's score

## When to Recognize Game Theory Pattern

Look for these indicators:
1. Problem involves **two players** competing
2. Players **alternate turns**
3. Both players play **optimally**
4. Need to determine **winner** or **optimal score**

Example phrases: "two players", "play optimally", "Alice and Bob", "stone game", "predict the winner"

## Core Concept: Minimax Strategy

**Minimax**: On your turn, you choose the move that maximizes your advantage, assuming opponent will minimize your advantage on their turn.

```
Your Score = max(
    move1 - Opponent's Best Response to move1,
    move2 - Opponent's Best Response to move2,
    ...
)
```

## Generic Steps to Solve Game Theory Problems

### Step 1: Understand the Game Rules
- What moves are allowed?
- How do players score?
- What's the winning condition?

### Step 2: Define DP State
```
dp[i][j] = maximum advantage (your score - opponent's score) 
           for subgame from index i to j when it's your turn

Or:
dp[i][j][turn] = best score for current player in range [i, j]
```

### Step 3: Establish Base Cases
```java
// Single element game
dp[i][i] = value[i];  // Take the only element

// No elements
dp[i][j] where i > j = 0;  // No advantage
```

### Step 4: Derive Recurrence Relation

**General Minimax Pattern**:
```java
for (int len = 2; len <= n; len++) {
    for (int i = 0; i + len <= n; i++) {
        int j = i + len - 1;
        
        // Try all possible moves
        // Your move gives you points, opponent plays optimally on remaining
        dp[i][j] = max(
            value[i] - dp[i+1][j],    // Take left, opponent plays [i+1, j]
            value[j] - dp[i][j-1],    // Take right, opponent plays [i, j-1]
            ...                       // Other possible moves
        );
    }
}
```

**Key Insight**: Your advantage = points_you_get - opponent's_advantage_on_remaining_game

## Common Problem Templates

### Template 1: Stone Game (Pick from Ends)

**Problem**: Pick stones from either end, both play optimally. Does first player win?

```java
class Solution {
    public boolean stoneGame(int[] piles) {
        int n = piles.length;
        
        // dp[i][j] = max advantage (player1 - player2) for piles[i..j]
        int[][] dp = new int[n][n];
        
        // Base case: single pile, take it
        for (int i = 0; i < n; i++) {
            dp[i][i] = piles[i];
        }
        
        // Fill DP table for increasing lengths
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len <= n; i++) {
                int j = i + len - 1;
                
                // Take left: get piles[i], opponent plays optimally on [i+1, j]
                int takeLeft = piles[i] - dp[i+1][j];
                
                // Take right: get piles[j], opponent plays optimally on [i, j-1]
                int takeRight = piles[j] - dp[i][j-1];
                
                dp[i][j] = Math.max(takeLeft, takeRight);
            }
        }
        
        return dp[0][n-1] > 0;  // Player 1 wins if advantage > 0
    }
}
```

**Space-Optimized (1D)**:
```java
public boolean stoneGame(int[] piles) {
    int n = piles.length;
    int[] dp = piles.clone();
    
    for (int len = 2; len <= n; len++) {
        for (int i = 0; i + len <= n; i++) {
            int j = i + len - 1;
            dp[i] = Math.max(piles[i] - dp[i+1], piles[j] - dp[i]);
        }
    }
    
    return dp[0] > 0;
}
```

**Math Observation for Original Stone Game**:
```java
// For even number of piles, first player always wins
// They can choose to take all even-indexed or all odd-indexed piles
public boolean stoneGame(int[] piles) {
    return true;  // Always true for original problem
}
```

### Template 2: Predict the Winner

**Problem**: Array of scores, pick from ends. Can first player win?

```java
class Solution {
    public boolean predictTheWinner(int[] nums) {
        int n = nums.length;
        int[][] dp = new int[n][n];
        
        // Base case
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }
        
        // Fill DP
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len <= n; i++) {
                int j = i + len - 1;
                dp[i][j] = Math.max(
                    nums[i] - dp[i+1][j],
                    nums[j] - dp[i][j-1]
                );
            }
        }
        
        return dp[0][n-1] >= 0;  // First player wins or ties
    }
}
```

### Template 3: Stone Game II (Variable Take)

**Problem**: Can take 1 to 2M piles from left, M starts at 1.

```java
class Solution {
    public int stoneGameII(int[] piles) {
        int n = piles.length;
        
        // Suffix sum: sum of piles from i to end
        int[] suffixSum = new int[n];
        suffixSum[n-1] = piles[n-1];
        for (int i = n-2; i >= 0; i--) {
            suffixSum[i] = suffixSum[i+1] + piles[i];
        }
        
        // dp[i][m] = max stones current player can get from index i with M=m
        int[][] dp = new int[n][n+1];
        
        // Base case: when i >= n, no stones left
        // When remaining piles <= 2M, take all
        for (int m = 1; m <= n; m++) {
            for (int i = n-1; i >= 0; i--) {
                if (i + 2*m >= n) {
                    dp[i][m] = suffixSum[i];  // Take all remaining
                }
            }
        }
        
        // Fill DP table
        for (int i = n-1; i >= 0; i--) {
            for (int m = 1; m <= n; m++) {
                if (i + 2*m < n) {
                    // Try taking x piles (1 <= x <= 2M)
                    for (int x = 1; x <= 2*m && i+x <= n; x++) {
                        // Current player gets piles[i..i+x-1]
                        // Opponent plays optimally on remaining with M' = max(M, x)
                        int remaining = suffixSum[i] - suffixSum[i+x];
                        int opponentGets = dp[i+x][Math.max(m, x)];
                        int currentPlayerGets = remaining - opponentGets + suffixSum[i+x];
                        
                        dp[i][m] = Math.max(dp[i][m], currentPlayerGets);
                    }
                }
            }
        }
        
        return dp[0][1];
    }
}
```

### Template 4: Nim Game / Divisor Game

**Problem**: Take 1,2,3... from N. Last to move wins. Who wins?

**Nim Game (take 1, 2, or 3)**:
```java
public boolean canWinNim(int n) {
    // Losing positions: n = 4, 8, 12, ... (multiples of 4)
    // From any non-multiple of 4, you can force opponent to multiple of 4
    return n % 4 != 0;
}
```

**Divisor Game (take divisor of N)**:
```java
public boolean divisorGame(int n) {
    // Alice starts with n
    // Pattern: even numbers win, odd numbers lose
    return n % 2 == 0;
}

// DP approach for general understanding:
public boolean divisorGameDP(int n) {
    boolean[] dp = new boolean[n+1];
    // dp[i] = true if current player wins with number i
    
    dp[0] = false;  // No moves, lose
    dp[1] = false;  // Can't move, lose
    
    for (int i = 2; i <= n; i++) {
        for (int x = 1; x < i; x++) {
            if (i % x == 0) {  // x is divisor
                // If opponent loses from i-x, we win
                if (!dp[i-x]) {
                    dp[i] = true;
                    break;
                }
            }
        }
    }
    
    return dp[n];
}
```

## Winning and Losing Positions

**Losing Position**: Position where current player will lose with optimal play
**Winning Position**: Position where current player can win with optimal play

**Key Principle**:
- If you can move to a losing position, you're in a winning position
- If all moves lead to winning positions, you're in a losing position

```java
boolean isWinning(int state) {
    for (Move move : getPossibleMoves(state)) {
        int nextState = applyMove(state, move);
        if (isLosing(nextState)) {
            return true;  // Found a move to losing position
        }
    }
    return false;  // All moves lead to winning positions
}
```

## Time and Space Complexity

**Typical Complexities**:
- **Time**: O(n²) for interval DP, O(n × M) with parameters
- **Space**: O(n²) or O(n × M)
- **Can often optimize to O(n) or O(1)** with pattern recognition

## Common Mistakes to Avoid

1. **Not considering advantage**: Track difference, not individual scores
2. **Wrong sign**: Your_advantage = your_points - opponent's_advantage
3. **Base case errors**: Single element games need careful initialization
4. **Not trying all moves**: Consider all legal moves in recurrence
5. **Pattern recognition**: Some games have simple mathematical patterns

## Problem-Solving Checklist

- [ ] What moves are allowed?
- [ ] How is scoring determined?
- [ ] Define dp[i][j] as advantage or winning boolean
- [ ] Establish base cases (single element, no elements)
- [ ] For each position, try all legal moves
- [ ] Subtract opponent's best response from your gain
- [ ] Check if there's a mathematical pattern
- [ ] Consider space optimization

## Related Patterns

- **Minimax Algorithm**: Core concept in game theory
- **Dynamic Programming on Intervals**: Many games use interval DP
- **Combinatorial Game Theory**: Advanced topic beyond DP

## Practice Problems in this Package

1. **Stone Game** - Pick stones from ends, determine winner
2. **Predict the Winner** - Pick from ends, can player 1 win?
3. **Stone Game II** - Variable number of piles to take
4. **Nim Game** - Take 1, 2, or 3 stones
5. **Divisor Game** - Take divisors of N

# State Machine Pattern

## Pattern Overview

The **State Machine** pattern models dynamic programming problems where at each position, you can be in one of several **states**, and transitions between states have specific costs or constraints. This pattern is powerful for problems with discrete state transitions.

## Core Characteristics

- **Multiple States**: At each position, multiple states are possible (bought, sold, holding, cooldown, etc.)
- **State Transitions**: Clear rules for moving from one state to another
- **Position-Based**: States change as you move through positions (days, houses, etc.)
- **Optimization**: Usually maximize/minimize value across all state transitions

## When to Recognize State Machine Pattern

Look for these indicators:
1. Problem has **distinct states** or phases (holding stock, cooldown, painted red/blue)
2. **Transitions between states** have specific rules or costs
3. Need to track **multiple possibilities** at each position
4. Decision at position i depends on **which state** you were in at position i-1

Example phrases: "buy/sell stock", "cooldown period", "different colors", "alternating choices", "can't use adjacent"

## Generic Steps to Solve State Machine Problems

### Step 1: Identify States
**Ask yourself**: "What distinct situations can I be in at each position?"

Examples:
- Stock problems: `{holding stock, not holding stock, cooldown}`
- House painting: `{painted red, painted blue, painted green}`
- House robber: `{robbed previous house, didn't rob previous house}`

### Step 2: Define State Transitions
**Map out**: "From each state, which states can I transition to, and what's the cost?"

Example (Stock with Cooldown):
```
State: HOLD (holding stock)
  → SELL (sell stock, get price)
  → HOLD (keep holding, no change)

State: SELL (just sold)
  → COOLDOWN (must cooldown)

State: COOLDOWN (in cooldown)
  → BUY (can buy again)
  → COOLDOWN (stay in cooldown)
```

### Step 3: Define DP State

**Common State Definitions**:
```
dp[i][state] = optimal value at position i in given state

Examples:
- dp[day][HOLD] = max profit at day i while holding stock
- dp[house][RED] = min cost to paint houses 0..i with house i being red
- dp[i][ROBBED] = max money after house i when house i was robbed
```

### Step 4: Establish Base Cases
```java
// Initialize day 0 or position 0 for each state
dp[0][STATE1] = initial_value_1;
dp[0][STATE2] = initial_value_2;

// Often some states are impossible initially
dp[0][IMPOSSIBLE_STATE] = Integer.MIN_VALUE/2; // or MAX_VALUE/2
```

### Step 5: Derive Recurrence Relations

**General Pattern**:
```java
for (int i = 1; i < n; i++) {
    for (each state S) {
        // Consider all possible transitions TO state S
        for (each previous state P that can transition to S) {
            dp[i][S] = optimize(dp[i][S], 
                               dp[i-1][P] + transition_cost(P → S));
        }
    }
}
```

### Step 6: Implementation Templates

**Template 1: Explicit States (2D DP)**
```java
public int solveStat Machine(int[] values) {
    int n = values.length;
    int[][] dp = new int[n][NUM_STATES];
    
    // Initialize base case
    dp[0][STATE_0] = initial_value_0;
    dp[0][STATE_1] = initial_value_1;
    // ... initialize other states
    
    // Fill DP table
    for (int i = 1; i < n; i++) {
        // State 0 transitions
        dp[i][STATE_0] = Math.max(
            dp[i-1][STATE_X] + cost_X_to_0,
            dp[i-1][STATE_Y] + cost_Y_to_0
        );
        
        // State 1 transitions
        dp[i][STATE_1] = Math.max(
            dp[i-1][STATE_A] + cost_A_to_1,
            dp[i-1][STATE_B] + cost_B_to_1
        );
        
        // ... handle other state transitions
    }
    
    // Return best final state
    return Math.max(dp[n-1][STATE_0], dp[n-1][STATE_1], ...);
}
```

**Template 2: Space-Optimized (Variables for States)**
```java
public int solveStateMachineOptimized(int[] values) {
    // Current states
    int state0 = initial_value_0;
    int state1 = initial_value_1;
    int state2 = initial_value_2;
    
    for (int i = 0; i < values.length; i++) {
        // Calculate new states (use temp variables to avoid conflicts)
        int newState0 = Math.max(state1 + transition1_to_0, 
                                 state2 + transition2_to_0);
        int newState1 = Math.max(state0 + transition0_to_1, 
                                 state2 + transition2_to_1);
        int newState2 = Math.max(state0 + transition0_to_2, 
                                 state1 + transition1_to_2);
        
        // Update states
        state0 = newState0;
        state1 = newState1;
        state2 = newState2;
    }
    
    return Math.max(state0, state1, state2);
}
```

## Common Problem Variations

### 1. **House Robber** (Can't rob adjacent houses)
**States**: `{robbed, not_robbed}`
```java
dp[i][ROBBED] = dp[i-1][NOT_ROBBED] + houses[i];
dp[i][NOT_ROBBED] = Math.max(dp[i-1][ROBBED], dp[i-1][NOT_ROBBED]);
```

### 2. **Stock Buy/Sell with Cooldown**
**States**: `{hold, sold, cooldown}`
```java
dp[i][HOLD] = Math.max(dp[i-1][HOLD], dp[i-1][COOLDOWN] - prices[i]);
dp[i][SOLD] = dp[i-1][HOLD] + prices[i];
dp[i][COOLDOWN] = Math.max(dp[i-1][SOLD], dp[i-1][COOLDOWN]);
```

### 3. **Paint House** (Adjacent houses different colors)
**States**: `{red, blue, green}`
```java
dp[i][RED] = costs[i][RED] + Math.min(dp[i-1][BLUE], dp[i-1][GREEN]);
dp[i][BLUE] = costs[i][BLUE] + Math.min(dp[i-1][RED], dp[i-1][GREEN]);
dp[i][GREEN] = costs[i][GREEN] + Math.min(dp[i-1][RED], dp[i-1][BLUE]);
```

### 4. **Stock Buy/Sell with Transaction Fee**
**States**: `{hold, not_hold}`
```java
dp[i][HOLD] = Math.max(dp[i-1][HOLD], dp[i-1][NOT_HOLD] - prices[i]);
dp[i][NOT_HOLD] = Math.max(dp[i-1][NOT_HOLD], 
                           dp[i-1][HOLD] + prices[i] - fee);
```

## State Machine Diagram Technique

**Draw it out!** Visual representation helps:

```
Example: Stock with Cooldown

   +-------+
   | HOLD  |<----+
   +-------+     |
      |          |
      | sell     | buy
      v          |
   +-------+     |
   | SELL  |     |
   +-------+     |
      |          |
      v          |
   +-------+     |
   |COOLDOWN|----+
   +-------+
```

## Time and Space Complexity

**Typical Complexities**:
- **Time**: O(n × k) where n = positions, k = number of states
- **Space**: 
  - 2D approach: O(n × k)
  - Space-optimized: O(k) - only track current state values

## Common Mistakes to Avoid

1. **Circular dependencies**: Use temporary variables when updating multiple states
2. **Wrong initial states**: Some states may be impossible initially (use -∞ or ∞)
3. **Missing transitions**: Ensure all valid state transitions are considered
4. **Forgetting to stay in same state**: Often "do nothing" is a valid transition
5. **Not considering all final states**: Answer might not be in a specific state

## Problem-Solving Checklist

- [ ] Identify all possible states at each position
- [ ] Draw state transition diagram
- [ ] Define what each state represents clearly
- [ ] Initialize base case for each state
- [ ] For each state, identify all incoming transitions
- [ ] Use temp variables to avoid circular dependency
- [ ] Consider "stay in same state" transitions
- [ ] Check all final states for answer
- [ ] Can we optimize space by keeping only current states?

## Advanced Techniques

### 1. **Finite State Automaton (FSA)**
Some problems can be modeled as FSA:
- Define states formally
- Define transition function
- Implement using state machine DP

### 2. **Multiple Dimensions**
Sometimes states have multiple aspects:
```java
dp[i][transactions][holding] = profit
// 3 dimensions: position, transaction count, holding state
```

### 3. **Implicit States**
Some problems have implicit states (parity, modulo):
```java
dp[i][sum % k] = count of ways
// State is the remainder when dividing by k
```

## Related Patterns

- **Linear Partition**: Similar structure but focuses on partitioning, not states
- **Grid Path**: Can model as state machine with position states
- **Sequence**: Some sequence problems have implicit states

## Practice Problems in this Package

1. **House Robber** - Rob/skip states for adjacent houses
2. **Delete and Earn** - Similar to house robber with value accumulation
3. **Paint House** - Choose colors with different costs
4. **Domino and Tromino Tiling** - Tile states for covering boards
5. **Max Product Subarray** - Track positive/negative product states

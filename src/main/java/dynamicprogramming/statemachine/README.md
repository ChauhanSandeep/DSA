# State Machine Pattern

## Core Idea
At each position, you're in one of several **states**. Transitions between states follow specific rules. Track the best value for each state.

## The Pattern (First Principles)

Instead of `dp[i] = one value`, you have:
```
dp[i][state0] = best value at position i in state 0
dp[i][state1] = best value at position i in state 1
dp[i][state2] = best value at position i in state 2
```

Each state transitions to specific other states.

## Universal Template

```java
public int statemachine(int[] arr) {
    // Define states (example: 2 states)
    int state0 = init_0;
    int state1 = init_1;
    
    for (int val : arr) {
        // Use temp variables to avoid overwriting
        int new0 = max(transition_from_0, transition_from_1);
        int new1 = max(transition_from_0, transition_from_1);
        
        state0 = new0;
        state1 = new1;
    }
    
    return max(state0, state1);
}
```

## Common Patterns

### House Robber (2 states)
```java
rob = notRob_prev + house[i]      // Rob current, must not have robbed prev
notRob = max(rob_prev, notRob_prev) // Skip current
```

### Stock (2-3 states)
```java
hold = max(hold_prev, notHold_prev - price)    // Buy or keep holding
notHold = max(notHold_prev, hold_prev + price) // Sell or stay empty
```

### Paint House (k colors)
```java
dp[i][color] = cost[color] + min(all other colors from prev house)
```

## Key Insight
Draw the state diagram! Each node is a state, edges show transitions.

**Time**: O(n × k) where k = number of states  
**Space**: O(k) with optimization

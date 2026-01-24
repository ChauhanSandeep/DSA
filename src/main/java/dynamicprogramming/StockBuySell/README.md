# Stock Buy/Sell Pattern

## Pattern Overview

The **Stock Buy/Sell** pattern is a specialized state machine pattern focused on maximizing profit from stock transactions. It involves making optimal buy and sell decisions under various constraints (transaction limits, cooldown periods, fees).

## Core Characteristics

- **State-Based**: Track different states (holding stock, not holding, cooldown)
- **Transaction Constraints**: Limited number of transactions, cooldown, or fees
- **Sequential Decisions**: Each day, decide whether to buy, sell, or hold
- **Profit Maximization**: Goal is to maximize total profit

## When to Recognize Stock Buy/Sell Pattern

Look for these indicators:
1. Problem involves **buying and selling** items over time
2. **Constraints** on transactions (max k transactions, cooldown, fees)
3. Need to **maximize profit** from buy-sell operations
4. **Sequential** decision-making (day by day)

Example phrases: "buy and sell stock", "maximize profit", "at most k transactions", "cooldown period", "transaction fee"

## Problem Variations Overview

| Problem | Constraint | Difficulty |
|---------|-----------|------------|
| Best Time to Buy and Sell Stock I | 1 transaction | Easy |
| Best Time to Buy and Sell Stock II | Unlimited transactions | Easy |
| Best Time to Buy and Sell Stock III | At most 2 transactions | Hard |
| Best Time to Buy and Sell Stock IV | At most k transactions | Hard |
| With Cooldown | Unlimited + 1 day cooldown | Medium |
| With Transaction Fee | Unlimited + fee per transaction | Medium |

## Generic Approach for All Stock Problems

### Step 1: Identify States
**Common States**:
- `hold`: Currently holding a stock
- `notHold` or `sold`: Not holding any stock (available to buy)
- `cooldown`: In cooldown period (for cooldown variation)

### Step 2: Define State Transitions
```
BUY: notHold → hold (pay price)
SELL: hold → notHold (receive price)
HOLD: stay in current state (do nothing)
COOLDOWN: sold → cooldown → notHold (for cooldown variation)
```

### Step 3: Define DP State

**For unlimited transactions**:
```
dp[i][0] = max profit on day i not holding stock
dp[i][1] = max profit on day i holding stock
```

**For k transactions**:
```
dp[i][j][0] = max profit on day i after at most j transactions, not holding
dp[i][j][1] = max profit on day i after at most j transactions, holding
```

### Step 4: Derive Recurrence Relations

**General Pattern**:
```java
// Not holding stock on day i
dp[i][0] = max(
    dp[i-1][0],              // didn't have stock yesterday, do nothing
    dp[i-1][1] + prices[i]   // had stock yesterday, sell today
);

// Holding stock on day i
dp[i][1] = max(
    dp[i-1][1],              // had stock yesterday, keep holding
    dp[i-1][0] - prices[i]   // didn't have stock yesterday, buy today
);
```

## Problem-Specific Solutions

### 1. **Stock I - At Most 1 Transaction**

**Simple Greedy Approach** - O(n) time, O(1) space:
```java
public int maxProfit(int[] prices) {
    int minPrice = Integer.MAX_VALUE;
    int maxProfit = 0;
    
    for (int price : prices) {
        minPrice = Math.min(minPrice, price);
        maxProfit = Math.max(maxProfit, price - minPrice);
    }
    
    return maxProfit;
}
```

**Key Insight**: Track minimum price seen so far, calculate profit if selling today.

### 2. **Stock II - Unlimited Transactions**

**Greedy Approach** - O(n) time, O(1) space:
```java
public int maxProfit(int[] prices) {
    int profit = 0;
    
    for (int i = 1; i < prices.length; i++) {
        // Add profit whenever price increases
        if (prices[i] > prices[i-1]) {
            profit += prices[i] - prices[i-1];
        }
    }
    
    return profit;
}
```

**State Machine Approach**:
```java
public int maxProfit(int[] prices) {
    int notHold = 0;    // Max profit not holding stock
    int hold = -prices[0];  // Max profit holding stock
    
    for (int i = 1; i < prices.length; i++) {
        int newNotHold = Math.max(notHold, hold + prices[i]);
        int newHold = Math.max(hold, notHold - prices[i]);
        
        notHold = newNotHold;
        hold = newHold;
    }
    
    return notHold;
}
```

**Key Insight**: Capture every price increase.

### 3. **Stock III - At Most 2 Transactions**

**4-State Approach**:
```java
public int maxProfit(int[] prices) {
    // buy1: after buying first stock
    // sell1: after selling first stock
    // buy2: after buying second stock
    // sell2: after selling second stock
    
    int buy1 = Integer.MIN_VALUE / 2;
    int sell1 = 0;
    int buy2 = Integer.MIN_VALUE / 2;
    int sell2 = 0;
    
    for (int price : prices) {
        buy1 = Math.max(buy1, -price);
        sell1 = Math.max(sell1, buy1 + price);
        buy2 = Math.max(buy2, sell1 - price);
        sell2 = Math.max(sell2, buy2 + price);
    }
    
    return sell2;
}
```

**Key Insight**: Track profit after each of 4 states (buy1, sell1, buy2, sell2).

### 4. **Stock IV - At Most k Transactions**

**General k Transactions**:
```java
public int maxProfit(int k, int[] prices) {
    int n = prices.length;
    if (n <= 1 || k == 0) return 0;
    
    // If k >= n/2, it's equivalent to unlimited transactions
    if (k >= n / 2) {
        return maxProfitUnlimited(prices);
    }
    
    // dp[t][0] = max profit after at most t transactions, not holding
    // dp[t][1] = max profit after at most t transactions, holding
    int[][] dp = new int[k + 1][2];
    
    for (int t = 0; t <= k; t++) {
        dp[t][1] = Integer.MIN_VALUE / 2;  // Can't hold without buying
    }
    
    for (int price : prices) {
        for (int t = k; t >= 1; t--) {
            dp[t][0] = Math.max(dp[t][0], dp[t][1] + price);
            dp[t][1] = Math.max(dp[t][1], dp[t-1][0] - price);
        }
    }
    
    return dp[k][0];
}

private int maxProfitUnlimited(int[] prices) {
    int profit = 0;
    for (int i = 1; i < prices.length; i++) {
        if (prices[i] > prices[i-1]) {
            profit += prices[i] - prices[i-1];
        }
    }
    return profit;
}
```

**Key Insight**: One transaction = buy + sell. Track transactions completed.

### 5. **With Cooldown**

**3-State Approach**:
```java
public int maxProfit(int[] prices) {
    // hold: holding stock
    // sold: just sold stock (must cooldown next)
    // cooldown: in cooldown or available to buy
    
    int hold = Integer.MIN_VALUE / 2;
    int sold = 0;
    int cooldown = 0;
    
    for (int price : prices) {
        int newHold = Math.max(hold, cooldown - price);
        int newSold = hold + price;
        int newCooldown = Math.max(cooldown, sold);
        
        hold = newHold;
        sold = newSold;
        cooldown = newCooldown;
    }
    
    return Math.max(sold, cooldown);
}
```

**Key Insight**: After selling, must cooldown before buying again.

### 6. **With Transaction Fee**

```java
public int maxProfit(int[] prices, int fee) {
    int notHold = 0;
    int hold = -prices[0];
    
    for (int i = 1; i < prices.length; i++) {
        int newNotHold = Math.max(notHold, hold + prices[i] - fee);
        int newHold = Math.max(hold, notHold - prices[i]);
        
        notHold = newNotHold;
        hold = newHold;
    }
    
    return notHold;
}
```

**Key Insight**: Subtract fee when selling (or when buying - same effect).

## State Machine Diagram

```
For Cooldown Problem:

    +----------+
    |   HOLD   |<--------+
    +----------+         |
         |               |
         | sell          | buy
         v               |
    +----------+         |
    |   SOLD   |         |
    +----------+         |
         |               |
         | (automatic)   |
         v               |
    +----------+         |
    | COOLDOWN |---------+
    +----------+
```

## Universal Template

```java
public int maxProfitTemplate(int[] prices, int k, int cooldown, int fee) {
    // States: [transaction][holding][cooldown_days]
    // Adjust based on specific constraints
    
    int n = prices.length;
    int[][][] dp = new int[k+1][2][cooldown+1];
    
    // Initialize base cases
    // ...
    
    for (int day = 0; day < n; day++) {
        for (int trans = k; trans >= 1; trans--) {
            for (int cd = 0; cd <= cooldown; cd++) {
                // Update states based on: buy, sell, hold
                // Apply fee if applicable
            }
        }
    }
    
    return maxOfAllFinalStates(dp);
}
```

## Time and Space Complexity

**Typical Complexities**:
- **Stock I**: O(n) time, O(1) space
- **Stock II**: O(n) time, O(1) space
- **Stock III**: O(n) time, O(1) space
- **Stock IV**: O(n × k) time, O(k) space
- **With Cooldown**: O(n) time, O(1) space
- **With Fee**: O(n) time, O(1) space

## Common Mistakes to Avoid

1. **Forgetting to use temporary variables**: Update all states simultaneously
2. **Incorrect initialization**: States representing impossible situations need MIN/MAX_VALUE
3. **Off-by-one in transactions**: Clarify if transaction = buy+sell or just one action
4. **Not handling k >= n/2 case**: For large k, treat as unlimited transactions
5. **Wrong final state**: Return the "not holding" state, not "holding"

## Problem-Solving Checklist

- [ ] How many transactions allowed? (1, 2, k, unlimited)
- [ ] Any special constraints? (cooldown, fee)
- [ ] Define all states clearly
- [ ] Draw state transition diagram
- [ ] Initialize impossible states to MIN/MAX_VALUE
- [ ] Use temp variables to avoid race conditions
- [ ] Return correct final state (usually "not holding")
- [ ] Consider space optimization (usually can reduce to O(1))

## Related Patterns

- **State Machine**: Stock problems are a special case of state machine DP
- **Sequence**: Similar sequential decision-making

## Practice Problems in this Package

1. **Best Time to Buy and Sell Stock** - Single transaction
2. **Best Time to Buy and Sell Stock III** - At most 2 transactions
3. **Best Time to Buy and Sell Stock IV** - At most k transactions
4. **With Cooldown** - Unlimited transactions with cooldown
5. **With Transaction Fee** - Unlimited transactions with fee

# Stock Buy/Sell Pattern

## Core Idea
State machine for buying/selling stocks with constraints (transaction limits, cooldown, fees).

## The Pattern (First Principles)

Track states: **holding stock** vs **not holding stock**
```
hold[i] = max profit on day i while holding stock
notHold[i] = max profit on day i without stock

Transitions:
  BUY:  notHold → hold (pay price)
  SELL: hold → notHold (gain price)
  WAIT: stay in current state
```

## Universal Template

```java
public int maxProfit(int[] prices) {
    int hold = -prices[0];  // Bought first stock
    int notHold = 0;        // Have no stock
    
    for (int i = 1; i < prices.length; i++) {
        int newHold = max(hold, notHold - prices[i]);    // Keep or buy
        int newNotHold = max(notHold, hold + prices[i]); // Keep or sell
        
        hold = newHold;
        notHold = newNotHold;
    }
    
    return notHold;
}
```

## Variants

| Constraint | Modification |
|-----------|-------------|
| **1 transaction** | Greedy: track min price |
| **Unlimited** | Add all positive differences |
| **k transactions** | Add transaction counter dimension |
| **Cooldown** | Add cooldown state |
| **Fee** | Subtract fee on buy or sell |

### With Cooldown
```java
hold = max(hold, cooldown - price);    // Can only buy after cooldown
sold = hold + price;                    // Just sold
cooldown = max(cooldown, sold);         // Cooldown or stay
```

### With k Transactions
```java
for (int t = k; t >= 1; t--) {
    dp[t][hold] = max(dp[t][hold], dp[t-1][notHold] - price);
    dp[t][notHold] = max(dp[t][notHold], dp[t][hold] + price);
}
```

**Time**: O(n) or O(n×k)  
**Space**: O(1) or O(k)

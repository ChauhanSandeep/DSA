
# Aditya Verma Stack Playlist - Revision Notes

This document serves as a revision guide for the problems covered in Aditya Verma's Stack playlist on YouTube.  The goal is to reinforce the problem-solving approach, intuition, and key concepts behind each problem. Each section includes Problem Description, Intuition, Approach (step-by-step), and Common Mistakes.

---

## 1. Balanced Parenthesis (Valid Parenthesis)

**Problem Description:**

Given a string `s` containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid. A string is valid if:
1. Open brackets must be closed by the same type of brackets.
2. Open brackets must be closed in the correct order.
3. Every close bracket has a corresponding open bracket of the same type.

**Intuition:**

The core idea is that for every closing parenthesis, there *must* be a matching opening parenthesis of the same type that appeared *most recently*.  A stack perfectly embodies this "last-in, first-out" (LIFO) behavior.  Think of it like stacking plates; the last plate you put on is the first one you take off.

**Approach (Step-by-Step):**

1.  **Initialization:** Create an empty stack to store opening parentheses.
2.  **Iteration:** Iterate through the input string, character by character.
3.  **Opening Parenthesis:** If the current character is an opening parenthesis ('(', '{', '['), push it onto the stack.
4.  **Closing Parenthesis:** If the current character is a closing parenthesis (')', '}', ']'):
    *   **Empty Stack Check:**  First, check if the stack is empty.  If it is, it means we encountered a closing parenthesis without a corresponding opening parenthesis, so the string is invalid.  Return `false`.
    *   **Matching Check:** If the stack is not empty, pop the top element from the stack.  This top element should be the corresponding opening parenthesis.
    *   **Mismatch:** If the popped element doesn't match the closing parenthesis (e.g., popped '{' but the current character is ')'), the string is invalid. Return `false`.
5.  **Final Check:** After iterating through the entire string, check if the stack is empty.
    *   **Empty Stack:** If the stack is empty, it means all opening parentheses have been matched with corresponding closing parentheses, so the string is valid.  Return `true`.
    *   **Non-Empty Stack:** If the stack is not empty, it means there are unmatched opening parentheses, so the string is invalid.  Return `false`.

**Example:**

String: "{[()]}"

1.  '{': Push to stack. Stack: `{`
2.  '[': Push to stack. Stack: `{[`
3.  '(': Push to stack. Stack: `{[(`
4.  ')': Pop from stack. Stack: `{[`   Match '(', ')'
5.  ']': Pop from stack. Stack: `{`   Match '[', ']'
6.  '}': Pop from stack. Stack: ``   Match '{', '}'
7.  Stack is empty. Return `true`.

String: "{[()]"

1.  '{': Push to stack. Stack: `{`
2.  '[': Push to stack. Stack: `{[`
3.  '(': Push to stack. Stack: `{[(`
4.  ')': Pop from stack. Stack: `{[`   Match '(', ')'
5.  ']': Pop from stack. Stack: `{`   Match '[', ']'
6.  '}': Pop from stack. Stack: ``   Match '{', '}'
7.  Stack is not empty. Return `false`.
**Common Mistakes:**

*   **Forgetting the Empty Stack Check:**  Failing to check if the stack is empty before attempting to pop when you encounter a closing parenthesis. This leads to a `StackUnderflowException` (or similar).
*   **Incorrect Matching Logic:**  Using an `if/else` ladder instead of a `switch` statement or a lookup table (e.g., a `HashMap`) for matching parenthesis pairs, making the code less readable and maintainable.
*   **Ignoring the Final Stack Check:** Not checking if the stack is empty at the end of the iteration.  This can lead to incorrect results for strings like "(((".

---

## 2. Next Greater Element

**Problem Description:**

Given an array `arr`, for each element, find the next greater element (NGE). The next greater element for an element `x` is the first element to the right of `x` in the array that is greater than `x`. If no such element exists, the NGE is -1.

**Intuition:**

The stack helps us keep track of potential candidates for the NGE of elements we've seen so far. The key is that the stack will store elements in *decreasing* order.  When we encounter a larger element, we know that it's the NGE for all the smaller elements on the stack.

**Approach (Step-by-Step):**

1.  **Initialization:**
    *   Create an empty stack to store array elements (or indices).
    *   Create a result array `nge` of the same size as the input array, initialized with -1 (default NGE).
2.  **Iteration (Right to Left):** Iterate through the array from right to left (from `n-1` to `0`).
3.  **Stack Comparison:** For each element `arr[i]`:
    *   **Pop Smaller Elements:** While the stack is not empty *and* `arr[i]` is greater than or equal to the element at the top of the stack (`stack.peek()`), pop elements from the stack. This is because these popped elements can never be the NGE of `arr[i]` or any element to the left of `arr[i]`.
    *   **NGE Found:** After popping, if the stack is not empty, the element at the top of the stack is the NGE of `arr[i]`.  Set `nge[i] = stack.peek()`.
    *   **Push Current Element:** Push `arr[i]` onto the stack.
4.  **Return Result:** Return the `nge` array.

**Example:**

Array: `[1, 3, 2, 4]`

1.  `4`: Stack is empty. Push 4.  Stack: `4`.  `nge[3] = -1`
2.  `2`:  `2 < 4`. Push 2.  Stack: `4, 2`. `nge[2] = 4`
3.  `3`: `3 < 4`. Push 3. Stack: `4, 3`. `nge[1] = 4`
4.  `1`: `1 < 3`. Push 1. Stack: `4, 3, 1`. `nge[0] = 3`

Result: `[3, 4, 4, -1]`

**Code Snippet (illustrative):**

```java
//Not Complete Code
for (int i = n - 1; i >= 0; i--) {
  while (!stack.isEmpty() && arr[i] >= stack.peek()) {
    stack.pop();
  }
  nge[i] = stack.isEmpty() ? -1 : stack.peek();
  stack.push(arr[i]);
}
```

**Common Mistakes:**

*   **Iterating in the Wrong Direction:** Iterating from left to right won't work.  You need to iterate from right to left to find the *next* greater element.
*   **Incorrect Comparison:** Using `>` instead of `>=` in the `while` loop can lead to incorrect results when there are duplicate elements in the array.
*   **Pushing Indices vs. Values:** Make sure you're pushing the actual values from the array onto the stack and not their indices (unless the problem specifically requires indices).
*   **Not handling Empty Stack Correctly:** When the stack is empty after the popping operations, correctly assign -1 to `nge[i]`.

---

## 3. Next Smaller Element

**Problem Description:**

Given an array `arr`, for each element, find the next smaller element (NSE). The next smaller element for an element `x` is the first element to the right of `x` in the array that is smaller than `x`. If no such element exists, the NSE is -1.

**Intuition:**

The logic is very similar to the Next Greater Element, but with a slight modification in the comparison. Instead of finding the next *greater* element, we want to find the next *smaller* element.  Therefore, the stack will now store elements in *increasing* order.

**Approach (Step-by-Step):**

1.  **Initialization:**
    *   Create an empty stack to store array elements (or indices).
    *   Create a result array `nse` of the same size as the input array, initialized with -1.
2.  **Iteration (Right to Left):** Iterate through the array from right to left.
3.  **Stack Comparison:** For each element `arr[i]`:
    *   **Pop Larger Elements:** While the stack is not empty *and* `arr[i]` is smaller than or equal to the element at the top of the stack (`stack.peek()`), pop elements from the stack.
    *   **NSE Found:** After popping, if the stack is not empty, the element at the top of the stack is the NSE of `arr[i]`.  Set `nse[i] = stack.peek()`.
    *   **Push Current Element:** Push `arr[i]` onto the stack.
4.  **Return Result:** Return the `nse` array.

**Example:**

Array: `[4, 5, 2, 10, 8]`

1.  `8`: Stack is empty. Push 8. `nse[4] = -1`
2.  `10`: Stack is empty. Push 10. `nse[3] = -1`
3.  `2`: pop 10, pop 8. Stack is empty. Push 2. `nse[2] = -1`
4.  `5`: Push 5. `nse[1] = 2`
5.  `4`: Push 4. `nse[0] = 2`
    Output: `[2, 2, -1, 8, -1]`

**Common Mistakes:**

Same as Next Greater Element, but with the comparison reversed.

*   **Incorrect Comparison:** Use `<` instead of `<=` in the `while` loop.
*   **Confusing with Next Greater Element:** Make sure you understand the difference in comparison (greater vs. smaller) and that you're popping the *larger* elements when looking for the NSE.

---

## 4. Previous Greater Element

**Problem Description:**

Given an array `arr`, for each element, find the previous greater element (PGE). The previous greater element for an element `x` is the first element to the left of `x` in the array that is greater than `x`. If no such element exists, the PGE is -1.

**Intuition:**

Since we're looking for elements to the *left*, we iterate from left to right. The stack will store elements in decreasing order (similar to NGE).  Whenever we encounter an element bigger than something on the stack, we pop until we find the previous greater.

**Approach (Step-by-Step):**

1.  **Initialization:**
    *   Create an empty stack.
    *   Create a result array `pge` of the same size as the input array, initialized with -1.
2.  **Iteration (Left to Right):** Iterate through the array from left to right (from `0` to `n-1`).
3.  **Stack Comparison:** For each element `arr[i]`:
    *   **Pop Smaller Elements:** While the stack is not empty *and* `arr[i]` is greater than or equal to the element at the top of the stack (`stack.peek()`), pop elements from the stack.  This is because the popped elements are *before* `arr[i]` and *smaller* than `arr[i]`, so they cannot be the PGE for `arr[i]` or any element to the right of `arr[i]`.
    *   **PGE Found:** After popping, if the stack is not empty, the element at the top of the stack is the PGE of `arr[i]`. Set `pge[i] = stack.peek()`.
    *   **Push Current Element:** Push `arr[i]` onto the stack.
4.  **Return Result:** Return the `pge` array.

**Example:**

Array: `[1, 3, 2, 4]`

1.  `1`: Stack is empty. Push 1.  Stack: `1`. `pge[0] = -1`
2.  `3`: `3 > 1`.  Pop 1. Stack is empty. Push 3.  Stack: `3`. `pge[1] = -1`
3.  `2`: `2 < 3`. Push 2. Stack: `3, 2`. `pge[2] = 3`
4.  `4`: `4 > 2`. Pop 2. `4 > 3`. Pop 3. Stack is empty. Push 4. Stack: `4`. `pge[3] = -1`

Result: `[-1, -1, 3, -1]`

**Common Mistakes:**

*   **Iterating in the Wrong Direction:** Iterating from right to left is wrong. Iterate from left to right.
*   **Incorrect Comparison:** Using `>` instead of `>=`.
*   **Pushing Indices vs. Values:** Push the values of the array.

---

## 5. Previous Smaller Element

**Problem Description:**

Given an array `arr`, for each element, find the previous smaller element (PSE). The previous smaller element for an element `x` is the first element to the left of `x` in the array that is smaller than `x`. If no such element exists, the PSE is -1.

**Intuition:**

The stack will store elements in increasing order (like NSE). We iterate from left to right.

**Approach (Step-by-Step):**

1.  **Initialization:**
    *   Create an empty stack.
    *   Create a result array `pse` of the same size as the input array, initialized with -1.
2.  **Iteration (Left to Right):** Iterate through the array from left to right.
3.  **Stack Comparison:** For each element `arr[i]`:
    *   **Pop Larger Elements:** While the stack is not empty *and* `arr[i]` is smaller than or equal to the element at the top of the stack (`stack.peek()`), pop elements from the stack.
    *   **PSE Found:** After popping, if the stack is not empty, the element at the top of the stack is the PSE of `arr[i]`.  Set `pse[i] = stack.peek()`.
    *   **Push Current Element:** Push `arr[i]` onto the stack.
4.  **Return Result:** Return the `pse` array.

**Example:**

Array: `[4, 5, 2, 10, 8]`

1.  `4`: Stack is empty. Push 4. `pse[0] = -1`
2.  `5`: Push 5. `pse[1] = 4`
3.  `2`: pop 5, pop 4. Stack is empty. Push 2. `pse[2] = -1`
4.  `10`: Push 10. `pse[3] = 2`
5.  `8`: Pop 10. Push 8. `pse[4] = 2`

Result: `[-1, 4, -1, 2, 2]`

**Common Mistakes:**

*   **Iterating in the Wrong Direction:** Iterate from left to right.
*   **Incorrect Comparison:**  Use `<` instead of `<=`.

---

## 6. Stock Span Problem

**Problem Description:**

The stock span problem is a financial problem where we have a series of n daily price quotes for a stock and we need to calculate the span of stock's price for all n days. The span `Si` of the stock's price on a given day `i` is defined as the maximum number of consecutive days just before the given day, for which the price of the stock on the current day is less than or equal to its price on the given day.

**Intuition:**

For each day `i`, we need to find the number of consecutive days (including the current day) where the stock price was less than or equal to the current day's price.  This is related to finding the *previous greater element*. The span is essentially the distance between the current day and the index of the previous greater element *plus 1*.

**Approach (Step-by-Step):**

1.  **Initialization:**
    *   Create an empty stack to store *indices* of the array elements.
    *   Create a result array `span` of the same size as the input array.
2.  **Iteration (Left to Right):** Iterate through the array from left to right (from `0` to `n-1`).
3.  **Stack Comparison:** For each day `i`:
    *   **Pop Smaller or Equal Prices:** While the stack is not empty *and* the price on day `i` (`prices[i]`) is greater than or equal to the price on the day at the top of the stack (`prices[stack.peek()]`), pop the top element (index) from the stack.
    *   **Calculate Span:**
        *   **Empty Stack:** If the stack is empty after popping, it means there is no previous greater element to the left of the current day. The span is `i + 1`.
        *   **Non-Empty Stack:** If the stack is not empty, the top element of the stack is the index of the previous greater element.  The span is `i - stack.peek()`.
    *   **Push Current Index:** Push the *index* `i` onto the stack.
4.  **Return Result:** Return the `span` array.

**Example:**

Prices: `[100, 80, 60, 70, 60, 75, 85]`

1. `100`: stack is empty. Span = 0 + 1 = 1. Push index 0. stack: `0`
2. `80`: 80 < 100. Span = 1 - 0 = 1. Push index 1. stack: `0, 1`
3. `60`: 60 < 80. Span = 2 - 1 = 1. Push index 2. stack: `0, 1, 2`
4. `70`: 70 > 60. Span = 3 - 1 = 2. Push index 3. stack: `0, 1, 3`
5. `60`: 60 < 70. Span = 4 - 3 = 1. Push index 4. stack: `0, 1, 3, 4`
6. `75`: 75 > 60; 75 > 70. Span = 5 - 1 = 4. Push index 5. stack: `0, 1, 5`
7. `85`: 85 > 75; 85 > 80; 85 < 100 Span = 6 - 0 = 6; Push index 6. stack: `0, 6`

Result: `[1, 1, 1, 2, 1, 4, 6]`

**Code Snippet (Illustrative):**

```java
//Not Complete Code
Stack<Integer> stack = new Stack<>();
int[] span = new int[prices.length];

for (int i = 0; i < prices.length; i++) {
  while (!stack.isEmpty() && prices[i] >= prices[stack.peek()]) {
    stack.pop();
  }

  span[i] = stack.isEmpty() ? (i + 1) : (i - stack.peek());
  stack.push(i);
}
```

**Common Mistakes:**

*   **Storing Values vs. Indices:**  Crucially, store the *indices* on the stack, not the prices themselves.
*   **Incorrect Span Calculation:** The span is the distance (difference in indices) between the current index and the index of the previous greater element.
*   **Incorrect Comparison:** The prices should be greater than or equal to (`>=`) .
---

## 7. Largest Rectangular Area in a Histogram

**Problem Description:**

Given an array of integers `heights` representing the histogram's bar height where the width of each bar is 1, find the area of largest rectangle in the histogram.

**Intuition:**

The largest rectangle for a bar `heights[i]` extends to the left until it encounters a bar smaller than `heights[i]` (the *previous smaller element*) and to the right until it encounters a bar smaller than `heights[i]` (the *next smaller element*).  The width of the rectangle is the distance between these two smaller elements, and the height is `heights[i]`.

**Approach (Step-by-Step):**

1.  **Find Next Smaller Element (NSE):**  Calculate the `nse` array as described in problem 3.
2.  **Find Previous Smaller Element (PSE):** Calculate the `pse` array as described in problem 5.
3.  **Calculate Areas:**
    *   Iterate through the `heights` array.
    *   For each `heights[i]`:
        *   `width = nse[i] - pse[i] - 1`. Need to handle edge cases. If the next or previous smaller element doesn't exist, we use the boundaries as the limits.
        * `area = heights[i] * width`.
    *   Keep track of the maximum area seen so far.
4.  **Return Maximum Area:** Return the maximum area.

**Handling Edge Cases for PSE and NSE**

- If `pse[i]` is -1, it means there's no smaller element to the left, so the starting point of the rectangle is 0.
- If `nse[i]` is -1, it means there's no smaller element to the right, so the ending point of the rectangle is `n - 1`.

**Example:**

Heights: `[2, 1, 5, 6, 2, 3]`

1. NSE: `[1, -1, 4, 4, 5, -1]`
2. PSE: `[-1, -1, 1, 2, 1, 4]`
3. Area calculation
    - `2`: width = 1 - (-1) - 1 = 1; area = 2
    - `1`: width = -1 - (-1) -1 = -1, but nse and pse need to handle the case when nse = -1, nse = n, similarly when pse = -1, pse=0 . Then width = 6-0-1 = 5; area = 5
    - `5`: width = 4 - 1 - 1 = 2; area = 10
    - `6`: width = 4 - 2 - 1 = 1; area = 6
    - `2`: width = 5 - 1 - 1 = 3; area = 6
    - `3`: width = -1 - 4 - 1 = -6, so width = 6-4-1 = 1; area = 3

Result: `10`

**Code Snippet (Illustrative):**

```java
//Not Complete Code
public int largestRectangleArea(int[] heights) {
    int n = heights.length;
    int[] nse = nextSmallerElement(heights); // Implement this
    int[] pse = previousSmallerElement(heights); // Implement this
    int maxArea = 0;

    for (int i = 0; i < n; i++) {
        int width = (nse[i] == -1 ? n : nse[i]) - (pse[i] == -1 ? -1 : pse[i]) - 1;
        int area = heights[i] * width;
        maxArea = Math.max(maxArea, area);
    }

    return maxArea;
}
```

**Common Mistakes:**

*   **Not Finding NSE and PSE Correctly:**  Errors in implementing the NSE and PSE calculations will propagate to the final result.
*   **Incorrect Width Calculation:** Remember to subtract 1 from the difference between NSE and PSE indices. `width = nse[i] - pse[i] - 1`
*   **Edge Case Handling:**  Carefully handle cases where there is no next/previous smaller element (NSE/PSE is -1). You need to consider the array boundaries.
*   **Forgetting to Handle zero's in array:** if the array contains the element zero, the stack might not work as expected. Handle that edge case.

---

## 8. Rain Water Trapping

**Problem Description:**

Given `n` non-negative integers representing an elevation map where the width of each bar is `1`, compute how much water it can trap after raining.

**Intuition:**

For each bar at index `i`, the amount of water it can trap is determined by the *minimum* of the maximum height to its left and the maximum height to its right, minus the height of the bar itself.  If this difference is negative, it means no water can be trapped at that bar.

**Approach (Step-by-Step):**

1. **Calculate Maximum Height to the Left (LeftMax):** Create an array `leftMax` of the same size as the input array.  `leftMax[i]` will store the maximum height of any bar to the left of and including index `i`. Iterate from left to right.
2. **Calculate Maximum Height to the Right (RightMax):** Create an array `rightMax` of the same size as the input array.  `rightMax[i]` will store the maximum height of any bar to the right of and including index `i`. Iterate from right to left.
3. **Calculate Trapped Water:**
    * Iterate through the array.
    * For each bar at index `i`:
        *  `waterTrapped = Math.min(leftMax[i], rightMax[i]) - heights[i]`
        *  If `waterTrapped > 0`, add it to the total trapped water.
4. **Return Total Trapped Water:** Return the total accumulated `waterTrapped`.

**Example:**

Heights: `[4, 2, 0, 3, 2, 5]`

1. `leftMax`: `[4, 4, 4, 4, 4, 5]`
2. `rightMax`: `[5, 5, 5, 5, 5, 5]`
3. Water Calculation:
    *   `4`: min(4, 5) - 4 = 0
    *   `2`: min(4, 5) - 2 = 2
    *   `0`: min(4, 5) - 0 = 4
    *   `3`: min(4, 5) - 3 = 1
    *   `2`: min(4, 5) - 2 = 2
    *   `5`: min(5, 5) - 5 = 0

Total Trapped Water: `0 + 2 + 4 + 1 + 2 + 0 = 9`

**Code Snippet (Illustrative):**

```java
//Not Complete Code
public int trap(int[] height) {
    int n = height.length;
    int[] leftMax = new int[n];
    int[] rightMax = new int[n];

    // Calculate leftMax
    leftMax[0] = height[0];
    for (int i = 1; i < n; i++) {
        leftMax[i] = Math.max(height[i], leftMax[i - 1]);
    }

    // Calculate rightMax
    rightMax[n - 1] = height[n - 1];
    for (int i = n - 2; i >= 0; i--) {
        rightMax[i] = Math.max(height[i], rightMax[i + 1]);
    }

    // Calculate trapped water
    int trappedWater = 0;
    for (int i = 0; i < n; i++) {
        int waterLevel = Math.min(leftMax[i], rightMax[i]);
        if (waterLevel > height[i]) {
            trappedWater += (waterLevel - height[i]);
        }
    }

    return trappedWater;
}
```

## 9. Minimum Element in Stack (with O(1) Extra Space)

**Problem Description:**

Design a stack that supports push, pop, top, and retrieving the minimum element in *constant* time (O(1)).

**Intuition:**

The challenge here is to track the minimum element without having to iterate through the entire stack every time `getMin()` is called.  We can't just store the minimum value as a separate variable because popping an element might change the minimum, and we'd have no way to efficiently find the *new* minimum.

The clever approach uses a stack to store modified values. The key idea is to store the difference between the current element and the current minimum.  When pushing an element smaller than the current minimum, we update the minimum and store a "flag" value that allows us to recover the previous minimum upon popping.

**Approach (Step-by-Step):**

We'll use a single stack to store modified values and a separate variable to keep track of the current minimum.

1. **Initialization:**
   * Create a stack (e.g., `Stack<Long> stack = new Stack<>();`)
   * Create a variable to store the current minimum (e.g., `long minEle;`)

2. **`push(int x)`:**
   * **First Element:** If the stack is empty, push `x` onto the stack and set `minEle = x`.
   * **Subsequent Elements:** If the stack is not empty:
     * **`x >= minEle`:** Push `x` onto the stack.
     * **`x < minEle`:**  This is the tricky part.
       * Push `2L * x - minEle` onto the stack.  (Note: `2L * x - minEle` is a `long` to prevent overflow.) This value acts as a "flag" to indicate that `x` is the new minimum. Furthermore, its value will always be less than current minimum `minEle`, so it can be used as a flag.
       * Update `minEle = x`.

3. **`pop()`:**
   * **Empty Stack:** If the stack is empty, return -1 or throw an exception (depending on problem constraints).
   * **`stack.peek() >= minEle`:** The top element is just a regular value. Pop it and return it.
   * **`stack.peek() < minEle`:** The top element is our "flag" value indicating a new minimum was pushed.
     * The previous minimum can be calculated: `oldMin = 2L * minEle - stack.peek()`. Now update the value of `minEle` to `oldMin`.
     * Pop the top element.
     * Return the previous minimum

4. **`top()`:**
    * If stack is empty return -1;
    * If `stack.peek() >= minEle`, return `stack.peek()`.
    * Else return `minEle`.

5. **`getMin()`:**
   * If the stack is empty, return -1 or throw an exception.
   * Return `minEle`.

**Example:**

```
push(2)
minEle = 2, stack: [2]

push(3)
minEle = 2, stack: [2, 3]

getMin() -> 2

push(1)
minEle = 1, stack: [2, 3, -1]   (2*1 - 2 = -1)

getMin() -> 1

pop()
minEle = 2, stack: [2, 3]

getMin() -> 2
```

**Code Snippet (Illustrative):**

```java
class MinStack {
    Stack<Long> stack = new Stack<>();
    long minEle;

    public void push(int x) {
        if (stack.isEmpty()) {
            stack.push((long)x);
            minEle = x;
        } else {
            if (x >= minEle) {
                stack.push((long)x);
            } else {
                stack.push(2L * x - minEle);
                minEle = x;
            }
        }
    }

    public void pop() {
        if (stack.isEmpty()) return; //Or throw exception.

        if (stack.peek() >= minEle) {
            stack.pop();
        } else {
            minEle = 2 * minEle - stack.peek();
            stack.pop();
        }
    }

    public int top() {
        if(stack.isEmpty()) return -1;
        if(stack.peek() >= minEle) return stack.peek().intValue();
        else return (int) minEle;

    }

    public int getMin() {
        if (stack.isEmpty()) return -1;
        return (int)minEle;
    }
}

```


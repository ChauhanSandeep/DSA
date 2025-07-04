# 📚 Permutations, Combinations & Math

## 🔍 Why You Need These

These concepts appear in:
- Recursion & backtracking problems
- Counting problems with constraints
- Combinatorics-based Dynamic Programming
- Probability-based questions
- Optimization problems (like choosing k items from n)

Even if you studied these in school, interview problems add layers of logic that make brushing up essential.

---

## 🔢 Factorial (n!)

### What It Is
The **factorial** of a number is the product of all positive integers ≤ n.

**n! = n × (n-1) × (n-2) × ... × 1**

### Where You Need It
- To calculate permutations and combinations
- For combinatorics-based questions
- To build precomputed arrays for fast `nCr`

### Code Example
```
static long factorial(int n) {
    long res = 1;
    for (int i = 2; i <= n; i++) res *= i;
    return res;
}
```

---

## 🔁 Permutations

### What It Is
Permutations refer to **arrangements** of objects.  
If the **order matters**, it's a permutation.

### Formula
**nPr = n! / (n - r)!**

### When to Use
- Arranging people/items/digits in all possible ways
- Generating all strings or paths with different orders
- Questions like “K-th permutation” or “How many ways to arrange X out of Y items?”

---

## 🔀 Combinations

### What It Is
Combinations refer to **selections** of objects.  
If the **order doesn't matter**, it's a combination.

### Formula
**nCr = n! / (r! × (n - r)!)**

### When to Use
- Selecting teams, subsets, or groups
- Problems like choosing k items from n
- Grid path problems and probability distributions

---

## 🌌 Power Set / Subset Generation

### What It Is
All subsets (including the empty set) of a given set.  
There are **2ⁿ subsets** for a set with `n` elements.

### When to Use
- Subset sum or partition problems
- Exhaustive search problems (like knapsack variants)
- Backtracking problems with "pick or skip" logic

---

## 🧁 Combinations With Replacement

### 🧠 What it is
You want to choose `r` elements from `n` options **and you're allowed to pick the same element multiple times**.

For example, You're selecting 3 scoops of ice cream from 5 flavors. You can pick:
- Vanilla, Vanilla, Chocolate
- Strawberry, Strawberry, Strawberry

Here, the order doesn't matter, and repeats are allowed.

### ✅ Formula
**(n + r - 1)Cr**

### 💻 Code example
```
// Print all combinations of size r from array with replacement allowed
public static void combinationWithRepetition(int[] arr, int r, int index, int start, List<Integer> current) {
    if (index == r) {
        System.out.println(current);
        return;
    }
    for (int i = start; i < arr.length; i++) {
        current.add(arr[i]);
        combinationWithRepetition(arr, r, index + 1, i, current); // not i+1
        current.remove(current.size() - 1);
    }
}
```

### 💡 DSA Relevance
Useful in problems involving:
- Distributing items with repetition
- Generating non-decreasing sequences
- Problems where selection can repeat, like vowel string combinations

---

## 🔁 Permutations of Strings with Duplicates

### What it is
When characters repeat, some permutations become redundant. You need a way to generate **only unique arrangements**.

For example, In string `"aab"` — valid unique permutations:
- `"aab"`, `"aba"`, `"baa"`

Without deduplication, `"aab"` might appear multiple times.

### 💻 Code Example
```
public static void permuteUnique(char[] chars, boolean[] used, StringBuilder current, Set<String> results) {
    if (current.length() == chars.length) {
        results.add(current.toString());
        return;
    }

    for (int i = 0; i < chars.length; i++) {
        if (used[i]) continue;
        if (i > 0 && chars[i] == chars[i - 1] && !used[i - 1]) continue;

        used[i] = true;
        current.append(chars[i]);
        permuteUnique(chars, used, current, results);
        used[i] = false;
        current.deleteCharAt(current.length() - 1);
    }
}
```

### 💡 DSA Relevance
- Generate unique permutations from a multiset
- Backtracking with de-duplication
- Efficient recursion pruning

---

## Pascal’s Triangle

### What it is
A triangular array that helps compute `nCr` values **without factorials**, using just addition.

### 📌 Structure
Each value is the sum of the two values above it:
```
Row 0:          1
Row 1:        1   1
Row 2:      1   2   1
Row 3:    1   3   3   1
Row 4:  1  4   6   4   1
```
Want 4C2? Just lookup row 4, column 2 → value is 6.
### 💻 Java Code
```
int[][] pascal = new int[n+1][n+1];
for (int i = 0; i <= n; i++) {
    pascal[i][0] = pascal[i][i] = 1;
    for (int j = 1; j < i; j++) {
        pascal[i][j] = pascal[i-1][j-1] + pascal[i-1][j];
    }
}
```

### 💡 DSA Relevance
- Efficient precomputation of nCr
- Used in DP/grid traversal problems
- Avoids factorial overflows

---

## Multinomial Coefficient

### What it is
Used when arranging items where some elements are **repeated**.

For example, "BANANA" has:
- 3 A’s
- 2 N’s
- 1 B

We can’t use simple `6!` because of duplicates.

### ✅ Formula
**n! / (k1! × k2! × ... × kr!)**
where k1, k2 etc are number of repeation of each character

### 💡 DSA Relevance
- Word permutation problems with repetition
- Anagram counting
- String arrangement with repeated characters

---

## ➕ Inclusion-Exclusion Principle

### What it is
Used to avoid **overcounting** when elements belong to multiple sets.

For a set of A and B, the total unique counts are:
```
|A ∪ B| = |A| + |B| - |A ∩ B|
```
For set of A, B and C:
```
|A ∪ B ∪ C| = |A| + |B| + |C| 
            - |A ∩ B| - |A ∩ C| - |B ∩ C| 
            + |A ∩ B ∩ C|
```

For example, How many numbers between 1 to 1000 are divisible by 2 or 5 or 7?

Naively:
- Div by 2 → ⌊1000 / 2⌋ = 500
- Div by 5 → ⌊1000 / 5⌋ = 200
- Div by 7 → ⌊1000 / 7⌋ = 142
  → Total = 500 + 200 + 142 = 842 (❌ wrong)

You need to **subtract** overlap:
- Div by both 2 & 5 → ⌊1000 / LCM(2,5)⌋ = 1000 / 10 = 100
- Div by both 2 & 7 → ⌊1000 / 14⌋ = 71
- Div by both 5 & 7 → ⌊1000 / 35⌋ = 28
- Div by 2, 5 & 7 → ⌊1000 / LCM(2,5,7)⌋ = 1000 / 70 = 14
  → Correct count = 500 + 200 + 142 - (100 + 71 + 28) + 14
  = 657

```
public int countMultiples(int N) { // N = 1000 in our example
    int a = N / 3;
    int b = N / 5;
    int c = N / 7;

    int ab = N / lcm(3, 5);
    int bc = N / lcm(5, 7);
    int ac = N / lcm(3, 7);

    int abc = N / lcm(3, lcm(5, 7));

    int result = a + b + c - ab - bc - ac + abc;
    System.out.println(result);
}
```

### 💡 DSA Relevance
- Count numbers satisfying multiple divisibility conditions
- Handle set unions with overlap
- Solve combinatorics with constraints

---

## 🧠 Bitmasking for Subsets

### 🧠 Intuition
Every subset of a set of size `n` can be represented as an `n`-bit binary number.

- `0` means exclude element
- `1` means include element

### 📌 Example
For `[1, 2, 3]`:
- `000` → `[]`
- `001` → `[3]`
- `101` → `[1, 3]`
- ...

Total: `2^n` subsets

### 💻 Java Code
```
int[] nums = {1, 2, 3};
int n = nums.length;
for (int mask = 0; mask < (1 << n); mask++) {
    List<Integer> subset = new ArrayList<>();
    for (int i = 0; i < n; i++) {
        if ((mask & (1 << i)) != 0) {
            subset.add(nums[i]);
        }
    }
    System.out.println(subset);
}
```

### 💡 DSA Relevance
- Subset sum, partition problems
- Efficient enumeration of all subsets
- State representation in bitmask DP
---

## 🧠 Optimization Tips for Interviews

- Precompute **factorials and inverse factorials** for modular nCr
- Use **modular inverse** for large constraints with modulo (1e9+7)
- Use **DP** when overlapping subproblems are detected (e.g. nCr with large n)

---

## 📘 Example Leetcode Problems

| Problem | Topic |
|--------|---------|
| [46. Permutations](https://leetcode.com/problems/permutations) | Basic Permutation |
| [47. Permutations II](https://leetcode.com/problems/permutations-ii) | Duplicates Handling |
| [77. Combinations](https://leetcode.com/problems/combinations) | Simple Combination |
| [39. Combination Sum](https://leetcode.com/problems/combination-sum) | Subset + Backtracking |
| [60. K-th Permutation Sequence](https://leetcode.com/problems/permutation-sequence) | Math + Factorials |
| [1641. Count Sorted Vowel Strings](https://leetcode.com/problems/count-sorted-vowel-strings) | Combinations with Replacement |
| [90. Subsets II](https://leetcode.com/problems/subsets-ii) | Duplicate Subsets |
| [1239. Max Length of Concatenated String with Unique Characters](https://leetcode.com/problems/maximum-length-of-a-concatenated-string-with-unique-characters) | Bitmask DP |


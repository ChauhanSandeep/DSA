## 🔩 Common Bitwise Operators in Java

| Operator | Symbol | Meaning                                | Example (`a = 5`, `b = 3`) | Binary View            |
|----------|--------|----------------------------------------|----------------------------|-------------------------|
| AND      | `&`    | 1 if both bits are 1                   | `5 & 3 = 1`                | `0101 & 0011 = 0001`    |
| OR       | `|`    | 1 if at least one bit is 1             | `5 | 3 = 7`                | `0101 | 0011 = 0111`    |
| XOR      | `^`    | 1 if bits are different                 | `5 ^ 3 = 6`                | `0101 ^ 0011 = 0110`    |
| NOT      | `~`    | Inverts all bits (2's complement)      | `~5 = -6`                  | `~00000101 = 11111010`  |
| Left Shift | `<<` | Shifts bits left (multiply by 2)       | `3 << 1 = 6`               | `0011 << 1 = 0110`      |
| Right Shift| `>>` | Shifts bits right (divide by 2)        | `6 >> 1 = 3`               | `0110 >> 1 = 0011`      |

---

## 🔧 Practical Use Cases in DSA

### 1. Check if a number is even or odd

```
boolean isEven = (num & 1) == 0;
```

**Why it works:**  
In binary, even numbers have the last bit as `0`, and odd numbers have it as `1`. The expression `num & 1` isolates the last bit.

---

### 2. Swap two numbers without a temp variable

```
x = x ^ y;
y = x ^ y;
x = x ^ y;
```

**Why it works:**  
XOR flips bits. When applied carefully, it cancels out the bits and helps us reconstruct the original values without using extra space.

---

### 3. Count set bits (number of 1s)

```
int count = 0;
while (n != 0) {
    n = n & (n - 1);
    count++;
}
```

**Why it works:**  
Each time you do `n & (n - 1)`, the lowest set bit is cleared. So it runs only as many times as there are 1s in the binary representation.

---

### 4. Check if a number is a power of 2

```
boolean isPowerOfTwo = (n > 0) && ((n & (n - 1)) == 0);
```

**Why it works:**  
Power of 2 numbers have only one bit set. For example, `8 = 1000`. Subtracting 1 flips all the bits to the right of the set bit, and ANDing with original gives 0.

---

### 5. Get the ith bit (0-based indexing)

```
int bit = (num >> i) & 1;
```

**Why it works:**  
Right shifting by `i` moves the ith bit to the least significant position, and `& 1` isolates it.

---

### 6. Set the ith bit

```
num |= (1 << i);
```

**Why it works:**  
`1 << i` creates a mask where only the ith bit is 1. OR-ing sets that bit in `num`.

---

### 7. Clear the ith bit

```
num &= ~(1 << i);
```

**Why it works:**  
`1 << i` sets the ith bit. Negating it (`~`) creates a mask where the ith bit is 0 and rest are 1. AND-ing clears that bit.

---

### 8. Toggle the ith bit

```
num ^= (1 << i);
```

**Why it works:**  
XOR with 1 flips the bit, XOR with 0 keeps it unchanged. So this flips the ith bit.

---

### 9. Check if ith bit is set

```
boolean isSet = (num & (1 << i)) != 0;
```

**Why it works:**  
AND-ing with a mask where only ith bit is set gives non-zero only if ith bit is actually set.

---

### 10. Clear all bits from LSB to ith bit (inclusive)

```
num &= ~((1 << (i + 1)) - 1);
```

**Why it works:**  
`(1 << (i + 1)) - 1` creates a mask with 1s from 0 to i. Negating it flips those bits to 0 and clears them in `num`.

---

### 11. Clear all bits from MSB to ith bit (exclusive)

```
num &= ((1 << i) - 1);
```

**Why it works:**  
This creates a mask with 1s in the lower i bits. AND-ing removes higher bits (MSB to i).

---

## 🧲 Bonus Tricks

### Check if a number is a multiple of 4

```
boolean isMultipleOf4 = (num & 3) == 0;
```

Because 4 in binary is `100`, multiples of 4 have the last 2 bits as 0.

---

### Turn off rightmost set bit

```
num = num & (num - 1);
```

Very useful in optimization problems where you want to process only the set bits.

---

### Isolate rightmost set bit

```
int rightmostSetBit = num & -num;
```

**Why it works:**  
Negative of a number (in 2's complement) flips the bits and adds 1. AND-ing it isolates the lowest set bit.

---

### Check if two integers have opposite signs

```
boolean oppositeSigns = (x ^ y) < 0;
```

**Why it works:**  
Sign bit differs → XOR will have MSB = 1 → Negative number.

---

### Count number of bits to convert `a` to `b`

```
int count = 0;
int xor = a ^ b;
while (xor != 0) {
    xor &= (xor - 1);
    count++;
}
```

Each bit in the XOR result shows a difference. Count set bits to find how many bits differ.

---

## 🏁 Summary

Bit manipulation helps you:
- Optimize space (e.g., using bits to store flags)
- Speed up calculations (powers of 2, toggling, etc.)
- Solve tricky logic problems cleanly and cleverly

✅ **Practice binary patterns, visualize examples, and reimplement common tricks by hand.**


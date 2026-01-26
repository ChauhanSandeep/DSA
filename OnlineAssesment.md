These are the patterns used to read input in competitive programming using Java's `Scanner` class. Understanding when to use each pattern can help quickly and correctly parse input data.

# Input Reading Patterns in Competitive Programming (Java Scanner)

## Pattern 1: The "Line-Based" Scanner

**Use case:** When you need to read an entire line as a single unit, or when the number of elements on a line is unknown/variable.

For example, if the test case input format is:

```bash
3
1 2 3 4 5
10 20 30
100 200
```

```java
int t = Integer.parseInt(sc.nextLine()); // Safely consume the first line without newline issues
while (t-- > 0) {
    String line = sc.nextLine();         // Grab the whole line
    String[] parts = line.trim().split("\\s+"); // Split by any whitespace
    // Convert strings to ints manually...
}

```

### Why use this?

* **Variable Input:** Great for problems where a line might have 5 numbers or 50, and they don't tell you the count beforehand.
* **Safety:** By using `Integer.parseInt(sc.nextLine())` instead of `sc.nextInt()`, you avoid the "Newline Bug" (where a leftover `\n` character ruins your next input read).
* **Regex Power:** `split("\\s+")` handles cases where there might be multiple spaces or tabs between numbers.

---

## Pattern 2: The "Token-Based" Scanner

**Use case:** When the input format is strictly defined (e.g., "every line will always have two strings and two integers").
For example, if the test case input format is:

```bash
2
hello 5 world 10
foo 15 bar 20
```

```java
int t = sc.nextInt(); 
while (t-- > 0) {
    String str1 = sc.next();      // Read first string
    int num1 = sc.nextInt();      // Read first integer
    String str2 = sc.next();      // Read second string
    int num2 = sc.nextInt();      // Read second integer
    // Process the inputs...
}

```

### Why use this?

* **Simplicity:** It’s much cleaner and requires less boilerplate code (no manual splitting or parsing).
* **Automatic Skipping:** `sc.next()` and `sc.nextInt()` automatically skip over whitespace and newlines to find the next relevant "token."
* **Predictability:** Best used when the problem statement says: *"Each test case contains four space-separated values..."*

---

## ⚖️ At a Glance: Which one to pick?

| Feature | **Pattern 1 (Line-Based)** | **Pattern 2 (Token-Based)** |
| --- | --- | --- |
| **Best for...** | Unknown number of items per line | Fixed, predictable input format |
| **Methods** | `nextLine()` + `split()` | `next()`, `nextInt()`, `nextDouble()` |
| **Handling Spaces** | Manual via Regex (`\\s+`) | Handled automatically by Scanner |
| **Risk Factor** | More code, but very robust | High risk of "Newline Bug" if mixing types |

### ⚠️ The "Golden Rule" for Revision

**Never mix `sc.nextInt()` and `sc.nextLine()` blindly.** If you use `nextInt()` and then try to use `nextLine()`, the `nextLine()` will capture the "Enter" key press (\n) left behind by the integer, resulting in an empty string.

**The Fix:** Either use Pattern 1 for *everything* or call an extra `sc.nextLine()` to "flush" the buffer after reading numbers.
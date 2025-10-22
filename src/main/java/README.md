# 📘 Java Syntax Cheatsheet for DSA Interviews

> A comprehensive quick-reference guide to essential Java syntaxes, shortcuts, patterns, and techniques that are **crucial for technical interviews**. Master these to write cleaner, faster, and more expressive code.

---

## 📚 Table of Contents
1. [Collections & Maps](#-collections--maps)
2. [String Operations](#-string-operations)
3. [Arrays & Primitives](#-arrays--primitives)
4. [Comparators & Sorting](#-comparators--sorting)
5. [Streams & Lambdas](#-streams--lambdas)
6. [Data Structures](#-data-structures)
7. [Bit Manipulation](#-bit-manipulation)
8. [Math Utilities](#-math-utilities)
9. [Common Patterns](#-common-patterns)
10. [Interview Pitfalls](#-interview-pitfalls)
11. [Time Complexity Reference](#-time-complexity-quick-reference)

---

## 📚 Collections & Maps

### Map Operations
``` java
// Safe initialization & updates
map.putIfAbsent(key, new ArrayList<>());
map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
map.computeIfPresent(key, (k, v) -> v + 1);
map.merge(key, 1, Integer::sum);  // Perfect for frequency counting
map.getOrDefault(key, 0);

// Iteration
map.forEach((k, v) -> System.out.println(k + ": " + v));
for (Map.Entry<K, V> entry : map.entrySet()) { }

// Quick checks
map.containsKey(key);
map.containsValue(value);
```

### List Operations
``` java
// Creation
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
List<String> list = List.of("a", "b", "c");  // Immutable (Java 9+)

// Sorting
list.sort(Comparator.naturalOrder());
list.sort(Comparator.reverseOrder());
Collections.sort(list);
Collections.reverse(list);

// Utilities
Collections.frequency(list, element);
Collections.swap(list, i, j);
Collections.fill(list, value);
list.subList(fromIndex, toIndex);  // O(1) view, not copy
```

### Set Operations
``` java
Set<Integer> set = new HashSet<>(list);
Set<Integer> set = new LinkedHashSet<>();  // Maintains insertion order
Set<Integer> set = new TreeSet<>();  // Sorted, O(log n) operations

// Set operations
set1.addAll(set2);      // Union
set1.retainAll(set2);   // Intersection
set1.removeAll(set2);   // Difference
```

### Queue & Deque
``` java
Queue<Integer> queue = new LinkedList<>();
queue.offer(x);         // Add (better than add())
queue.poll();           // Remove and return (returns null if empty)
queue.peek();           // View front

Deque<Integer> deque = new ArrayDeque<>();
deque.offerFirst(x);    // Add to front
deque.offerLast(x);     // Add to back
deque.pollFirst();      // Remove from front
deque.pollLast();       // Remove from back
deque.peekFirst();      // View front
deque.peekLast();       // View back
```

---

## 🔤 String Operations

### String Manipulation
``` java
// Character checks
Character.isDigit(ch);
Character.isLetter(ch);
Character.isLetterOrDigit(ch);
Character.isWhitespace(ch);
Character.isUpperCase(ch);
Character.isLowerCase(ch);

// Case conversion
s.toLowerCase();
s.toUpperCase();
Character.toLowerCase(ch);
Character.toUpperCase(ch);

// Common operations
s.trim();                           // Remove leading/trailing whitespace
s.substring(start, end);            // [start, end)
s.charAt(i);
s.indexOf(substring);
s.lastIndexOf(substring);
s.replace(oldChar, newChar);
s.replaceAll(regex, replacement);
s.split("\\s+");                    // Split by whitespace
String.join(", ", list);            // Join with delimiter

// StringBuilder (mutable, faster for multiple modifications)
StringBuilder sb = new StringBuilder();
sb.append(str);
sb.insert(index, str);
sb.deleteCharAt(index);
sb.reverse();
sb.toString();

// Conversion
char[] chars = s.toCharArray();
String s = new String(chars);
String s = String.valueOf(value);   // Any type to String
```

### String Patterns
``` java
// Palindrome check
boolean isPalindrome = new StringBuilder(s).reverse().toString().equals(s);

// Character frequency
Map<Character, Integer> freq = new HashMap<>();
for (char c : s.toCharArray()) {
    freq.merge(c, 1, Integer::sum);
}

// Anagram check (same frequency)
char[] a1 = s1.toCharArray(), a2 = s2.toCharArray();
Arrays.sort(a1); Arrays.sort(a2);
boolean isAnagram = Arrays.equals(a1, a2);
```

---

## 🧮 Arrays & Primitives

### Array Basics
``` java
// Declaration & initialization
int[] arr = new int[n];
int[] arr = {1, 2, 3, 4, 5};
int[] arr = new int[]{1, 2, 3};
int[][] matrix = new int[m][n];

// Copying
int[] copy = arr.clone();
int[] copy = Arrays.copyOf(arr, length);
int[] copy = Arrays.copyOfRange(arr, from, to);
System.arraycopy(src, srcPos, dest, destPos, length);

// Filling
Arrays.fill(arr, value);
Arrays.fill(arr, fromIndex, toIndex, value);

// Sorting
Arrays.sort(arr);                   // O(n log n)
Arrays.sort(arr, fromIndex, toIndex);

// Searching (in sorted array)
int index = Arrays.binarySearch(arr, key);  // Returns negative if not found

// Conversion
List<Integer> list = Arrays.stream(arr).boxed().collect(Collectors.toList());
int[] arr = list.stream().mapToInt(i -> i).toArray();

// 2D array to List
int[][] arr = {{1,2}, {3,4}};
List<int[]> list = Arrays.asList(arr);  // Fixed-size
List<int[]> list = new ArrayList<>(Arrays.asList(arr));  // Mutable
```

### Array Utilities
``` java
// Stream operations on arrays
Arrays.stream(arr).sum();
Arrays.stream(arr).max().getAsInt();
Arrays.stream(arr).min().getAsInt();
Arrays.stream(arr).average().getAsDouble();
Arrays.stream(arr).count();

// Comparison
Arrays.equals(arr1, arr2);          // 1D
Arrays.deepEquals(arr1, arr2);      // Multi-dimensional

// String representation
Arrays.toString(arr);               // 1D: [1, 2, 3]
Arrays.deepToString(arr);           // Multi-dimensional
```

### Primitive Type Conversions
``` java
// String to number
int x = Integer.parseInt(str);
long x = Long.parseLong(str);
double x = Double.parseDouble(str);

// Number to String
String s = String.valueOf(num);
String s = Integer.toString(num);

// Character to int
int digit = ch - '0';               // '5' -> 5
char ch = (char)('0' + digit);      // 5 -> '5'

// Int to char
char ch = (char)(num + 'a');        // 0 -> 'a'
```

---

## ⚖️ Comparators & Sorting

### Safe Comparators
``` java
// ❌ NEVER use subtraction (integer overflow risk)
Arrays.sort(arr, (a, b) -> a - b);  // WRONG!

// ✅ ALWAYS use Integer.compare()
Arrays.sort(arr, (a, b) -> Integer.compare(a, b));
Arrays.sort(arr, Integer::compare);

// Multi-dimensional arrays
Arrays.sort(arr, (a, b) -> Integer.compare(a[0], b[0]));

// Multiple criteria
Arrays.sort(arr, (a, b) -> {
    if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
    return Integer.compare(a[1], b[1]);
});
```

### Advanced Comparators
``` java
// Natural order
list.sort(Comparator.naturalOrder());
list.sort(Comparator.reverseOrder());

// Comparing by field
list.sort(Comparator.comparing(Person::getAge));
list.sort(Comparator.comparing(Person::getAge).reversed());

// Multiple fields
list.sort(Comparator.comparing(Person::getLastName)
                    .thenComparing(Person::getFirstName));

// Null-safe
list.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
list.sort(Comparator.nullsLast(Comparator.naturalOrder()));

// Custom comparator
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> 
    Integer.compare(a[0], b[0])
);
```

---

## 🌀 Streams & Lambdas

### Basic Stream Operations
``` java
// Creation
Stream<Integer> stream = list.stream();
Stream<Integer> stream = Arrays.stream(arr);
Stream<Integer> stream = Stream.of(1, 2, 3);
IntStream stream = IntStream.range(0, 10);      // [0, 10)
IntStream stream = IntStream.rangeClosed(0, 10); // [0, 10]

// Filtering & Mapping
list.stream()
    .filter(x -> x > 0)
    .map(x -> x * 2)
    .collect(Collectors.toList());

// FlatMap (flatten nested collections)
list.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList());

// Distinct
list.stream().distinct().collect(Collectors.toList());

// Sorting
list.stream()
    .sorted()
    .sorted(Comparator.reverseOrder())
    .sorted(Comparator.comparing(Person::getAge))
    .collect(Collectors.toList());
```

### Terminal Operations
``` java
// Collect
list.stream().collect(Collectors.toList());
list.stream().collect(Collectors.toSet());
list.stream().collect(Collectors.joining(", "));

// Reduce
int sum = list.stream().reduce(0, Integer::sum);
int product = list.stream().reduce(1, (a, b) -> a * b);
Optional<Integer> max = list.stream().reduce(Integer::max);

// Match
boolean anyPositive = list.stream().anyMatch(x -> x > 0);
boolean allPositive = list.stream().allMatch(x -> x > 0);
boolean noneNegative = list.stream().noneMatch(x -> x < 0);

// Find
Optional<Integer> first = list.stream().findFirst();
Optional<Integer> any = list.stream().findAny();

// Count
long count = list.stream().filter(x -> x > 0).count();

// Min/Max
Optional<Integer> min = list.stream().min(Integer::compare);
Optional<Integer> max = list.stream().max(Integer::compare);
```

### Advanced Collectors
``` java
// Group by
Map<Type, List<Item>> grouped = list.stream()
    .collect(Collectors.groupingBy(Item::getType));

// Group by with counting
Map<Type, Long> counts = list.stream()
    .collect(Collectors.groupingBy(Item::getType, Collectors.counting()));

// Partition (split into two groups)
Map<Boolean, List<Integer>> partitioned = list.stream()
    .collect(Collectors.partitioningBy(x -> x > 0));

// To Map
Map<Integer, String> map = list.stream()
    .collect(Collectors.toMap(Item::getId, Item::getName));

// To Map with merge function (handle duplicates)
Map<Integer, String> map = list.stream()
    .collect(Collectors.toMap(
        Item::getId, 
        Item::getName, 
        (existing, replacement) -> existing
    ));

// Joining
String result = list.stream()
    .map(String::valueOf)
    .collect(Collectors.joining(", ", "[", "]"));  // [1, 2, 3]
```

---

## 🏗️ Data Structures

### Stack
``` java
Stack<Integer> stack = new Stack<>();
stack.push(x);
stack.pop();
stack.peek();
stack.isEmpty();
stack.size();

// Prefer Deque over Stack (Stack is legacy)
Deque<Integer> stack = new ArrayDeque<>();
stack.push(x);      // or addFirst(x)
stack.pop();        // or removeFirst()
stack.peek();       // or peekFirst()
```

### PriorityQueue (Heap)
``` java
// Min-heap (default)
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

// Max-heap
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a); // Avoid!
PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b, a));

// Operations: O(log n)
pq.offer(x);        // Add
pq.poll();          // Remove min/max
pq.peek();          // View min/max
pq.remove(x);       // Remove specific element (O(n))

// Custom comparator for complex objects
PriorityQueue<int[]> pq = new PriorityQueue<>(
    Comparator.comparingInt(a -> a[0])
);
```

### TreeSet / TreeMap (Sorted)
``` java
TreeSet<Integer> set = new TreeSet<>();
set.add(x);
set.first();        // Minimum
set.last();         // Maximum
set.lower(x);       // Largest element < x
set.higher(x);      // Smallest element > x
set.floor(x);       // Largest element <= x
set.ceiling(x);     // Smallest element >= x

TreeMap<Integer, String> map = new TreeMap<>();
map.firstKey();
map.lastKey();
map.lowerKey(x);
map.higherKey(x);
map.floorKey(x);
map.ceilingKey(x);
```

---

## 🔢 Bit Manipulation

### Common Bit Operations
``` java
// Check if ith bit is set
boolean isSet = (num & (1 << i)) != 0;
boolean isSet = ((num >> i) & 1) == 1;

// Set ith bit
num |= (1 << i);

// Clear ith bit
num &= ~(1 << i);

// Toggle ith bit
num ^= (1 << i);

// Check if number is power of 2
boolean isPowerOf2 = (n > 0) && ((n & (n - 1)) == 0);

// Count set bits (Brian Kernighan's algorithm)
int countBits = 0;
while (n > 0) {
    n &= (n - 1);
    countBits++;
}
// Or use built-in
int countBits = Integer.bitCount(n);

// Get rightmost set bit
int rightmost = n & (-n);

// Remove rightmost set bit
n = n & (n - 1);

// Check even/odd
boolean isEven = (num & 1) == 0;

// XOR properties
a ^ a = 0           // Self-cancellation
a ^ 0 = a           // Identity
a ^ b ^ b = a       // Find unique in duplicates
```

### Bit Manipulation Patterns
``` java
// Swap without temp variable
a ^= b;
b ^= a;
a ^= b;

// Get all subsets (2^n subsets)
for (int mask = 0; mask < (1 << n); mask++) {
    List<Integer> subset = new ArrayList<>();
    for (int i = 0; i < n; i++) {
        if ((mask & (1 << i)) != 0) {
            subset.add(nums[i]);
        }
    }
}

// Range XOR [L, R]
int xorRange = xor(R) ^ xor(L - 1);
```

---

## 🧮 Math Utilities

### Common Math Operations
``` java
// Min/Max
Math.min(a, b);
Math.max(a, b);
Math.abs(x);

// Power & Root
Math.pow(base, exp);
Math.sqrt(x);
Math.cbrt(x);           // Cube root

// Ceiling & Floor
Math.ceil(x);           // Round up
Math.floor(x);          // Round down
Math.round(x);          // Round to nearest

// Random
int rand = (int)(Math.random() * n);  // [0, n)
```

### GCD & LCM
``` java
// Greatest Common Divisor (Euclidean algorithm)
int gcd(int a, int b) {
    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }
    return a;
}

// Or use built-in (Java 9+)
int gcd = Math.gcd(a, b);  // Not available in Java 8

// Least Common Multiple
int lcm(int a, int b) {
    return (a * b) / gcd(a, b);
}
```

### Prime Numbers
``` java
// Check if prime
boolean isPrime(int n) {
    if (n <= 1) return false;
    if (n <= 3) return true;
    if (n % 2 == 0 || n % 3 == 0) return false;
    for (int i = 5; i * i <= n; i += 6) {
        if (n % i == 0 || n % (i + 2) == 0)
            return false;
    }
    return true;
}

// Sieve of Eratosthenes
boolean[] sieve(int n) {
    boolean[] isPrime = new boolean[n + 1];
    Arrays.fill(isPrime, true);
    isPrime[0] = isPrime[1] = false;
    
    for (int i = 2; i * i <= n; i++) {
        if (isPrime[i]) {
            for (int j = i * i; j <= n; j += i) {
                isPrime[j] = false;
            }
        }
    }
    return isPrime;
}
```

---

## 🎯 Common Patterns

### Two Pointers
``` java
// Opposite direction (e.g., Two Sum in sorted array)
int left = 0, right = arr.length - 1;
while (left < right) {
    int sum = arr[left] + arr[right];
    if (sum == target) return new int[]{left, right};
    else if (sum < target) left++;
    else right--;
}

// Same direction (e.g., Remove duplicates)
int slow = 0;
for (int fast = 1; fast < arr.length; fast++) {
    if (arr[fast] != arr[slow]) {
        arr[++slow] = arr[fast];
    }
}
```

### Sliding Window
``` java
// Fixed size window
int windowSum = 0;
for (int i = 0; i < k; i++) windowSum += arr[i];
int maxSum = windowSum;

for (int i = k; i < arr.length; i++) {
    windowSum += arr[i] - arr[i - k];
    maxSum = Math.max(maxSum, windowSum);
}

// Variable size window
int left = 0, maxLen = 0;
for (int right = 0; right < arr.length; right++) {
    // Add arr[right] to window
    while (/* window invalid */) {
        // Remove arr[left] from window
        left++;
    }
    maxLen = Math.max(maxLen, right - left + 1);
}
```

### Binary Search
``` java
// Standard binary search
int left = 0, right = arr.length - 1;
while (left <= right) {
    int mid = left + (right - left) / 2;  // Avoid overflow
    if (arr[mid] == target) return mid;
    else if (arr[mid] < target) left = mid + 1;
    else right = mid - 1;
}
return -1;  // Not found

// Find leftmost (lower_bound)
int left = 0, right = arr.length;
while (left < right) {
    int mid = left + (right - left) / 2;
    if (arr[mid] < target) left = mid + 1;
    else right = mid;
}
return left;

// Find rightmost (upper_bound)
int left = 0, right = arr.length;
while (left < right) {
    int mid = left + (right - left) / 2;
    if (arr[mid] <= target) left = mid + 1;
    else right = mid;
}
return left - 1;
```

### Prefix Sum
``` java
// 1D prefix sum
int[] prefix = new int[arr.length + 1];
for (int i = 0; i < arr.length; i++) {
    prefix[i + 1] = prefix[i] + arr[i];
}
// Sum of range [i, j] = prefix[j + 1] - prefix[i]

// 2D prefix sum
int[][] prefix = new int[m + 1][n + 1];
for (int i = 1; i <= m; i++) {
    for (int j = 1; j <= n; j++) {
        prefix[i][j] = matrix[i-1][j-1] 
                     + prefix[i-1][j] 
                     + prefix[i][j-1] 
                     - prefix[i-1][j-1];
    }
}
```

### Union-Find (Disjoint Set)
``` java
class UnionFind {
    private int[] parent, rank;
    
    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }
    
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);  // Path compression
        }
        return parent[x];
    }
    
    public boolean union(int x, int y) {
        int rootX = find(x), rootY = find(y);
        if (rootX == rootY) return false;
        
        // Union by rank
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        return true;
    }
}
```

---

## ⚠️ Interview Pitfalls

### Common Mistakes to Avoid
``` java
// ❌ Integer overflow in comparator
Arrays.sort(arr, (a, b) -> a[0] - b[0]);  // WRONG!
// ✅ Use Integer.compare()
Arrays.sort(arr, (a, b) -> Integer.compare(a[0], b[0]));

// ❌ Using list.toArray() without type
Object[] arr = list.toArray();  // Returns Object[]
// ✅ Specify type
String[] arr = list.toArray(new String[0]);
int[][] arr = list.toArray(new int[0][]);

// ❌ Modifying list during iteration
for (int x : list) {
    if (x > 5) list.remove(x);  // ConcurrentModificationException
}
// ✅ Use iterator or collect indices
Iterator<Integer> it = list.iterator();
while (it.hasNext()) {
    if (it.next() > 5) it.remove();
}

// ❌ Arrays.asList() returns fixed-size list
List<String> list = Arrays.asList("a", "b");
list.add("c");  // UnsupportedOperationException
// ✅ Wrap in ArrayList
List<String> list = new ArrayList<>(Arrays.asList("a", "b"));

// ❌ Comparing arrays with ==
if (arr1 == arr2)  // Compares references
// ✅ Use Arrays.equals()
if (Arrays.equals(arr1, arr2))

// ❌ Integer cache (-128 to 127)
Integer a = 128, b = 128;
if (a == b)  // false (different objects)
// ✅ Use .equals()
if (a.equals(b))

// ❌ String concatenation in loops
String s = "";
for (int i = 0; i < n; i++) s += i;  // O(n²)
// ✅ Use StringBuilder
StringBuilder sb = new StringBuilder();
for (int i = 0; i < n; i++) sb.append(i);
```

### Edge Cases to Remember
``` java
// Empty inputs
if (arr == null || arr.length == 0) return;

// Single element
if (arr.length == 1) return arr[0];

// Integer overflow
int mid = left + (right - left) / 2;  // Not (left + right) / 2

// Off-by-one errors
for (int i = 0; i < arr.length; i++)     // Correct
for (int i = 0; i <= arr.length; i++)    // ArrayIndexOutOfBoundsException

// Negative modulo
int mod = ((x % n) + n) % n;  // Handles negative x
```

---

## ⏱️ Time Complexity Quick Reference

| Operation | ArrayList | LinkedList | HashSet | TreeSet | HashMap | TreeMap |
|-----------|-----------|------------|---------|---------|---------|---------|
| Access    | O(1)      | O(n)       | -       | -       | O(1)    | -       |
| Search    | O(n)      | O(n)       | O(1)    | O(log n)| O(1)    | O(log n)|
| Insert    | O(1)*     | O(1)       | O(1)    | O(log n)| O(1)    | O(log n)|
| Delete    | O(n)      | O(1)**     | O(1)    | O(log n)| O(1)    | O(log n)|

*Amortized for ArrayList add at end; O(n) for insertion at arbitrary position  
**O(1) if you have reference to the node; O(n) to find it

### Algorithm Time Complexities
- **Sorting**: O(n log n) - Arrays.sort(), Collections.sort()
- **Binary Search**: O(log n)
- **Linear Search**: O(n)
- **BFS/DFS**: O(V + E)
- **Dijkstra**: O((V + E) log V)
- **Union-Find**: O(α(n)) ≈ O(1) amortized
- **Kadane's Algorithm**: O(n)
- **Two Pointers**: O(n)
- **Sliding Window**: O(n)

---

## ✨ Pro Tips

### Performance
- Use `StringBuilder` for string concatenation in loops
- Prefer `ArrayList` over `LinkedList` in most cases
- Use primitive arrays (`int[]`) over `List<Integer>` for better performance
- Avoid autoboxing/unboxing in tight loops
- Use `Arrays.sort()` instead of `Collections.sort()` for primitive arrays

### Readability vs Performance
- Prefer **streams** for readability in simple transformations
- Use **loops** for complex logic or performance-critical sections
- Use **method references** when possible: `String::length` vs `s -> s.length()`

### Interview Favorites
- `computeIfAbsent()` - Building graphs, grouping
- `merge()` - Frequency counting
- `Collectors.groupingBy()` - Grouping and aggregation
- Monotonic stack - Next greater/smaller element
- Sliding window - Subarray problems
- Two pointers - Sorted array problems
- Union-Find - Graph connectivity

### Memory Optimization
- Use space-optimized DP when possible (O(1) instead of O(n))
- Reuse variables instead of creating new ones
- Be mindful of recursion depth (stack space)

---

**Made with ☕ and 💻 by Sandeep**  
**Repository**: [ChauhanSandeep/DSA](https://github.com/ChauhanSandeep/DSA)
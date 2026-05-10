# 📘 Java Syntax Cheatsheet for DSA Interviews

> A comprehensive quick-reference guide to essential Java syntaxes, shortcuts, patterns, and techniques that are **crucial for technical interviews**. Master these to write cleaner, faster, and more expressive code.

---

## 🧮 Arrays & Primitives

### Array Basics

#### Declaration & Initialization

*Create arrays with different syntaxes - O(n)*

```java
swaint[] arr = new int[n];            // Array of size n, initialized to 0
int[] arr = {1, 2, 3, 4, 5};       // Array literal
int[][] matrix = new int[m][n];    // 2D array
```

#### Copying Arrays

*Create copies of arrays efficiently*

```java
int[] copy = arr.clone();          // Shallow copy - O(n)
int[] copy = Arrays.copyOf(arr, length);  // Copy with new length - O(n)
int[] copy = Arrays.copyOfRange(arr, from, to);  // Copy range [from, to) - O(n)
System.arraycopy(src, srcPos, dest, destPos, length);  // Native copy - O(n)
```

#### Filling Arrays

*Set array values - O(n)*

```java
Arrays.fill(arr, value);           // Fill entire array
Arrays.fill(arr, fromIndex, toIndex, value);  // Fill range
```

#### Sorting & Searching

*Sort in arrays*

```java
Arrays.sort(arr);                  // Sort entire array - O(n log n)
Arrays.sort(arr, (a, b) -> Integer.compare(a, b));  // Custom comparator
Arrays.sort(arr, fromIndex, toIndex);  // Sort range - O(n log n)
```

*Search in arrays*

```java
int index = Arrays.binarySearch(arr, key);  // Binary search (sorted array) - O(log n)
```

#### Array-List Conversions

*Convert between arrays and lists*

```java
List<Integer> list = Arrays.stream(arr).boxed().collect(Collectors.toList());  // Array to List - O(n)
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));  // Array literal to List - O(n)
int[] arr = list.stream()
            .mapToInt(i -> i) // Unbox Integer to int
            .toArray();  // List to array - O(n)
```

#### 2D Array to List

*Convert 2D arrays to lists*

```java
int[][] arr = {{1,2}, {3,4}};
List<int[]> list = Arrays.asList(arr);  // Fixed-size list - O(1)
List<int[]> list = new ArrayList<>(Arrays.asList(arr));  // Mutable list - O(n)

List<List<Integer>> list = Arrays.stream(matrix)
            .map(row -> Arrays.stream(row)          // stream each row
                              .boxed()             // autobox int -> Integer
                              .collect(Collectors.toList()))
            .collect(Collectors.toList());

```

### Array Utilities

#### Stream Operations

*Process arrays using streams - all O(n)*

```java
Arrays.stream(arr).sum();          // Sum all elements
Arrays.stream(arr).max().getAsInt();  // Find maximum
Arrays.stream(arr).min().getAsInt();  // Find minimum
Arrays.stream(arr).average().getAsDouble();  // Calculate average
```

#### Array Comparison

*Compare arrays for equality*

```java
Arrays.equals(arr1, arr2);         // Compare 1D arrays - O(n)
Arrays.deepEquals(arr1, arr2);     // Compare multi-dimensional - O(n)
```

#### String Representation

*Convert arrays to readable strings*

```java
Arrays.toString(arr);              // 1D: [1, 2, 3] - O(n)
Arrays.deepToString(arr);          // Multi-dimensional - O(n)
```

### Primitive Type Conversions

#### String to Number

*Parse strings to numeric types - O(n)*

```java
int x = Integer.parseInt(str);     // String to int
long x = Long.parseLong(str);      // String to long
double x = Double.parseDouble(str); // String to double
```

#### Number to String

*Convert numbers to strings - O(1) to O(log n)*

```java
String s = String.valueOf(num);    // Any number to String
```

#### Character-Number Conversions

*Convert between chars and digits - O(1)*

```java
int digit = ch - '0';              // '5' -> 5 (char to int)
char ch = (char)('0' + digit);     // 5 -> '5' (int to char)
char ch = (char)(num + 'a');       // 0 -> 'a' (int to letter)
```

---

### List Operations

#### Creation

*Create lists with different characteristics*

```java
// Mutable list created from a fixed set of integers
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));

 
List<Integer> list = Arrays.stream(nums1)
                           .boxed()             // Converts each primitive int to Integer
                           .collect(Collectors.toList());   // Collects into a mutable List<Integer>

List<String> list = List.of("a", "b", "c");  // Immutable (Java 9+)
```

#### Sorting

*Sort lists in various orders - O(n log n)*

```java
list.sort((a, b) -> a.getAge() - b.getAge()); // Sort in ascending age

Collections.sort(list);  // Sort using natural ordering
Collections.sort(list, (a, b) -> Integer.compare(a, b));  // Custom comparator, sort ascending

Collections.reverse(list);  // Reverse list order - O(n)
```

#### Utilities

*Common list utility operations*

```java
int freq = Collections.frequency(list, element);  // Count occurrences - O(n). Returns int
Collections.swap(list, i, j);  // Swap two elements - O(1). Mutates same list
Collections.fill(list, value);  // Fill with value - O(n). Mutates same list
List<Integer> sub = list.subList(fromIndex, toIndex);  // Get view [fromIndex, toIndex) - O(1). Changes reflect in original list
```

---

### Set Operations

#### Creation

*Create sets with different ordering guarantees*

```java
Set<Integer> hashset = new HashSet<>(list);  // Unordered, O(1) operations
Set<Integer> linkedHashSet = new LinkedHashSet<>();  // Maintains insertion order
Set<Integer> treeSet = new TreeSet<>();  // Sorted, O(log n) operations
Set<Integer> descendingTreeSet = new TreeSet<>((a, b) -> b - a); // custom descending order
```

#### Set Operations

*Perform mathematical set operations - O(n)*

```java
set1.addAll(set2);      // Union: combine both sets
set1.retainAll(set2);   // Intersection: keep common elements
set1.removeAll(set2);   // Difference: remove elements in set2
```

### Map Operations

#### Safe Initialization & Updates

*Initialize map values safely and update entries efficiently - O(1) average*

```java
List<Integer> prevValue = map.putIfAbsent(key, new ArrayList<>());  // Add key only if absent
map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);  // Compute and add in one step
Integer updated = map.computeIfPresent(key, (k, v) -> v + 1);  // Update only if key exists
Integer count = map.getOrDefault(key, 0);  // Get value or return default - O(1)
```

#### Iteration

*Iterate through map entries efficiently - O(n)*

```java
map.forEach((k, v) -> System.out.println(k + ": " + v));  // Lambda-based iteration
for (Map.Entry<K, V> entry : map.entrySet()) { }  // Traditional iteration
```

#### Quick Checks

*Check map contents - O(1) average*

```java
map.containsKey(key);  // Check if key exists
map.containsValue(value);  // Check if value exists - O(n)
```

### TreeSet / TreeMap (Sorted)

#### TreeSet Operations

*Sorted set with navigational methods - O(log n) for all operations*

```java
TreeSet<Integer> set = new TreeSet<>();
set.add(x);         // Add element
set.first();        // Get minimum element
set.last();         // Get maximum element
set.lower(x);       // Largest element < x
set.higher(x);      // Smallest element > x
set.floor(x);       // Largest element <= x
set.ceiling(x);     // Smallest element >= x
```

#### TreeMap Operations

*Sorted map with navigational methods - O(log n) for all operations*

```java
TreeMap<Integer, String> map = new TreeMap<>();
map.firstKey();     // Get minimum key
map.lastKey();      // Get maximum key
map.lowerKey(x);    // Largest key < x
map.higherKey(x);   // Smallest key > x
map.floorKey(x);    // Largest key <= x
map.ceilingKey(x);  // Smallest key >= x
```

### Queue & Deque

#### Queue Operations

*FIFO data structure - all operations O(1)*

Different implementations: LinkedList, ArrayDeque, PriorityQueue:

- Use LinkedList when you need a simple queue.
- Use ArrayDeque for better performance as a queue or stack.
- Use PriorityQueue for priority-based ordering.

```java
Queue<Integer> queue = new LinkedList<>();
queue.offer(x);         // Add to rear (better than add())
queue.poll();           // Remove and return front (null if empty)
queue.peek();           // View front without removing
```

#### Deque Operations

*Double-ended queue - all operations O(1)*

Different implementations: LinkedList, ArrayDeque:

- Use LinkedList for a simple deque.
- Use ArrayDeque for better performance.

```java
Deque<Integer> deque = new ArrayDeque<>();

// Add elements
deque.offer(x);         // Add to back (same as offerLast)
deque.offerFirst(x);    // Add to front
deque.offerLast(x);     // Add to back

// Remove elements
deque.poll();           // Remove from front (same as pollFirst)
deque.pollFirst();      // Remove from front
deque.pollLast();       // Remove from back

// View elements (no removal)
deque.peek();           // View front (same as peekFirst)
deque.peekFirst();      // View front
deque.peekLast();       // View back
```

### Stack

#### Legacy Stack (Avoid)

```java
Stack<Integer> stack = new Stack<>();
stack.push(x);      // Push element - O(1)
stack.pop();        // Pop element - O(1)
stack.peek();       // View top - O(1)
stack.isEmpty();    // Check if empty - O(1)
stack.size();       // Get size - O(1)
```

### PriorityQueue (Heap)

#### Min-Heap Creation

*Default priority queue orders elements naturally*

```java
PriorityQueue<Integer> minHeap = new PriorityQueue<>();  // Smallest element at top
```

#### Max-Heap Creation

*Reverse ordering for maximum element at top*

```java
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());  // Safe
PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b, a));  // Safe
```

#### Basic Operations

*All operations except remove specific element*

```java
pq.offer(x);        // Add element - O(log n)
pq.poll();          // Remove and return min/max - O(log n)
pq.peek();          // View min/max without removing - O(1)
pq.remove(x);       // Remove specific element - O(n)
```

#### Custom Comparators

*Priority queue for complex objects - O(log n) per operation*

```java
PriorityQueue<int[]> pq = new PriorityQueue<>(
    Comparator.comparingInt(a -> a[0])  // Compare by first element
);
```

---

## 🔤 String Operations

### String Manipulation

#### Character Checks

*Validate character types - O(1)*

```java
Character.isDigit(ch);         // Check if character is digit
Character.isLetter(ch);        // Check if character is letter
Character.isLetterOrDigit(ch); // Check if alphanumeric
Character.isWhitespace(ch);    // Check if whitespace
Character.isUpperCase(ch);     // Check if uppercase
Character.isLowerCase(ch);     // Check if lowercase
```

#### Case Conversion

*Convert case of characters and strings - O(n) for strings*

```java
String lowercase = s.toLowerCase();               // Convert string to lowercase - O(n)
String uppercase = s.toUpperCase();               // Convert string to uppercase - O(n)
char lowercaseChar = Character.toLowerCase(ch);   // Convert char to lowercase - O(1)
char uppercaseChar = Character.toUpperCase(ch);   // Convert char to uppercase - O(1)
```

#### Common String Operations

*Essential string methods with their complexities*

```java
String t = s.trim();                      
// returns String (new), removes leading/trailing whitespace – O(n)

String sub = s.substring(start, end);    
// returns String, range [start, end) – O(n) 

char c = s.charAt(i);                     
// returns char – O(1)

int idx = s.indexOf(substring);           
// returns int (first index or -1) – O(n * m)

int idx = s.indexOf(substring, fromIndex);
// returns int (first index or -1) – O(n * m)

int lastIdx = s.lastIndexOf(substring);   
// returns int (last index or -1) – O(n * m)

String r = s.replaceFirst(oldCharSequence, newCharSequence);
// returns String (new), replaces first occurrence of oldCharSequence with newCharSequence – O(n)

String r = s.replace(oldCharSequence, newCharSequence);   
// returns String (new), replaces all occurrences of oldCharSequence with newCharSequence – O(n)

String r2 = s.replaceAll(regex, replacement); 
// returns String (new), regex-based replace – O(n)*

String[] parts = s.split("\\s+");         
// returns String[] – O(n)*

String joined = String.join(", ", list);  
// returns comma-separated String – O(n)
```

#### StringBuilder

*Mutable strings for efficient modifications - O(1) amortized per operation*

```java
StringBuilder sb = new StringBuilder();
sb.append(str);                // Append string - O(1) amortized
sb.insert(index, str);         // Insert at position - O(n) due to shifting
sb.deleteCharAt(index);        // Delete character - O(n) due to shifting
sb.reverse();                  // Reverse string - O(n)
sb.toString();                 // Convert to String - O(n)
```

#### String Conversions

*Convert between String and other types*

```java
char[] chars = s.toCharArray();    // String to char array - O(n)
String s = new String(chars);      // Char array to String - O(n)
String s = String.valueOf(value);  // Any type to String - O(1) to O(n)
```

### String Patterns

#### Palindrome Check

*Check if string reads same forwards and backwards - O(n)*

```java
boolean isPalindrome = new StringBuilder(s).reverse().toString().equals(s);
```

#### Character Frequency

*Count occurrences of each character - O(n)*

```java
Map<Character, Integer> freq = new HashMap<>();
for (char c : s.toCharArray()) {
    freq.merge(c, 1, Integer::sum);  // Increment count for each character
}
```

#### Anagram Check

*Verify if two strings have same character frequencies - O(n log n)*

```java
char[] a1 = s1.toCharArray(), a2 = s2.toCharArray();
Arrays.sort(a1); Arrays.sort(a2);  // Sort both arrays
boolean isAnagram = Arrays.equals(a1, a2);  // Compare sorted arrays
```

---

## ⚖️ Comparators & Sorting

### Safe Comparators

#### Avoiding Integer Overflow

*Always use safe comparison methods - O(n log n) for sorting*

```java
// ❌ NEVER use subtraction (integer overflow risk)
Arrays.sort(arr, (a, b) -> a - b);  // WRONG!

// ✅ ALWAYS use Integer.compare()
Arrays.sort(arr, (a, b) -> Integer.compare(a, b));  // Safe comparison
Arrays.sort(arr, Integer::compare);  // Method reference

// Multi-dimensional arrays
Arrays.sort(arr, (a, b) -> Integer.compare(a[0], b[0]));  // Compare by first element

// Multiple criteria
Arrays.sort(arr, (a, b) -> {
    if (a[0] != b[0]) return Integer.compare(a[0], b[0]);  // Primary criteria
    return Integer.compare(a[1], b[1]);  // Secondary criteria
});
```

### Advanced Comparators

#### Natural Order Sorting

*Sort using natural/reverse order - O(n log n)*

```java
Collections.sort(list, Comparator.naturalOrder());   // Ascending order
Collections.sort(list, Comparator.reverseOrder());   // Descending order
Collections.sort(list, (a, b) -> a.compareTo(b));  // Ascending order

list.sort(Comparator.naturalOrder());   // Ascending order
list.sort(Comparator.reverseOrder());   // Descending order
```

#### Field-Based Sorting

*Sort objects by specific fields - O(n log n)*

```java
list.sort(Comparator.comparing(Person::getAge));  // Sort by age
list.sort(Comparator.comparing(Person::getAge).reversed());  // Sort by age descending
```

#### Multi-Field Sorting

*Sort by multiple criteria - O(n log n)*

```java
list.sort(Comparator.comparing(Person::getLastName)
                    .thenComparing(Person::getFirstName));  // Sort by last name, then first name
```

#### Null-Safe Comparators

*Handle null values safely during sorting - O(n log n)*

```java
list.sort(Comparator.nullsFirst(Comparator.naturalOrder()));  // Nulls at start
list.sort(Comparator.nullsLast(Comparator.naturalOrder()));   // Nulls at end
```

#### Custom Comparators for Data Structures

*Use custom comparators in priority queues - O(log n) per operation*

```java
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> 
    Integer.compare(a[0], b[0])  // Compare by first element
);
```

---

## 🌀 Streams & Lambdas

### Basic Stream Operations

#### Stream Creation

*Create streams from various sources - O(1) for creation*

```java
Stream<Integer> stream = list.stream();         // From collection
Stream<Integer> stream = Arrays.stream(arr);    // From array
Stream<Integer> stream = Stream.of(1, 2, 3);   // From values
IntStream stream = IntStream.range(0, 10);      // Range [0, 10)
IntStream stream = IntStream.rangeClosed(0, 10); // Range [0, 10]
```

#### Filtering & Mapping

*Transform stream elements - O(n)*

```java
list.stream()
    .filter(x -> x > 0)      // Keep only positive numbers
    .map(x -> x * 2)         // Double each element
    .collect(Collectors.toList());
```

#### FlatMap

*Flatten nested collections - O(n*m) where m is avg nested size*

```java
list.stream()
    .flatMap(List::stream)   // Flatten list of lists into single stream
    .collect(Collectors.toList());
```

#### Distinct Elements

*Remove duplicates - O(n)*

```java
list.stream().distinct().collect(Collectors.toList());  // Keep unique elements
```

#### Stream Sorting

*Sort stream elements - O(n log n)*

```java
list.stream()
    .sorted()                                    // Natural order
    .sorted(Comparator.reverseOrder())          // Reverse order
    .sorted(Comparator.comparing(Person::getAge))  // By field
    .collect(Collectors.toList());
```

### Terminal Operations

#### Collect

*Gather stream elements into collections - O(n)*

```java
list.stream().collect(Collectors.toList());     // Collect to List
list.stream().collect(Collectors.toSet());      // Collect to Set
list.stream().collect(Collectors.joining(", ")); // Join to String
```

#### Reduce

*Combine stream elements into single result - O(n)*

```java
int sum = list.stream().reduce(0, Integer::sum);  // Sum all elements
int product = list.stream().reduce(1, (a, b) -> a * b);  // Product of elements
Optional<Integer> max = list.stream().reduce(Integer::max);  // Find maximum
```

#### Match Operations

*Test stream elements against predicate - O(n) worst case*

```java
boolean anyPositive = list.stream().anyMatch(x -> x > 0);   // Any element matches
boolean allPositive = list.stream().allMatch(x -> x > 0);   // All elements match
boolean noneNegative = list.stream().noneMatch(x -> x < 0); // No element matches
```

#### Find Operations

*Find elements in stream - O(n) worst case*

```java
Optional<Integer> first = list.stream().findFirst();  // First element
Optional<Integer> any = list.stream().findAny();      // Any element (parallel)
```

#### Count

*Count stream elements - O(n)*

```java
long count = list.stream().filter(x -> x > 0).count();  // Count matching elements
```

#### Min/Max

*Find minimum or maximum - O(n)*

```java
Optional<Integer> min = list.stream().min((a, b) -> a.compareTo(b)); // Find minimum number
Optional<Integer> max = list.stream().max((a, b) -> a.compareTo(b)); // Find maximum number
```

### Advanced Collectors

#### Group By

*Group elements by classifier - O(n)*

```java
Map<Type, List<Item>> grouped = list.stream()
    .collect(Collectors.groupingBy(Item::getType));  // Group by type field
```

#### Group By with Counting

*Group and count elements - O(n)*

```java
// Count per group
Map<Type, Long> counts = list.stream()
    .collect(Collectors.groupingBy(Item::getType, Collectors.counting()));

// Count frequency of each number
Map<Integer, Integer> frequency = Arrays.stream(nums)
    .boxed()
    .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(e -> 1)));
```

#### Partition

*Split into two groups based on predicate - O(n)*

```java
Map<Boolean, List<Integer>> partitioned = list.stream()
    .collect(Collectors.partitioningBy(x -> x > 0));  // Split positive/negative
```

#### Collect to Map

*Create map from stream elements - O(n)*

```java
Map<Integer, String> map = list.stream()
    .collect(Collectors.toMap(Item::getId, Item::getName));  // Key-value pairs
```

#### Collect to Map with Duplicate Handling

*Handle duplicate keys when creating map - O(n)*

```java
Map<Integer, String> map = list.stream()
    .collect(Collectors.toMap(
        Item::getId,           // Key mapper
        Item::getName,         // Value mapper
        (existing, replacement) -> existing  // Keep existing on duplicate
    ));
```

#### Joining Strings

*Concatenate stream elements - O(n)*

```java
String result = list.stream()
    .map(String::valueOf)
    .collect(Collectors.joining(", ", "[", "]"));  // Format: [1, 2, 3]
```

---

## 🔢 Bit Manipulation

### Common Bit Operations

#### Check Bit Status

*Check if specific bit is set - O(1)*

```java
boolean isSet = (num & (1 << i)) != 0;  // Check ith bit using mask
boolean isSet = ((num >> i) & 1) == 1;  // Check ith bit using shift
```

#### Modify Bits

*Set, clear, or toggle specific bits - O(1)*

```java
num |= (1 << i);    // Set ith bit to 1
num &= ~(1 << i);   // Clear ith bit to 0
num ^= (1 << i);    // Toggle ith bit
```

#### Power of 2 Check

*Verify if number is power of 2 - O(1)*

```java
boolean isPowerOf2 = (n > 0) && ((n & (n - 1)) == 0);  // True if power of 2
```

#### Count Set Bits

*Count number of 1s in binary representation*

```java
// Brian Kernighan's algorithm - O(k) where k is number of set bits
int countBits = 0;
while (n > 0) {
    n &= (n - 1);   // Remove rightmost set bit
    countBits++;
}

// Built-in method - O(1)
int countBits = Integer.bitCount(n);
```

#### Rightmost Set Bit

*Isolate or remove rightmost 1 bit - O(1)*

```java
int rightmost = n & (-n);   // Get rightmost set bit
n = n & (n - 1);            // Remove rightmost set bit
```

#### Parity Check

*Check if number is even or odd - O(1)*

```java
boolean isEven = (num & 1) == 0;  // True if even, false if odd
```

#### XOR Properties

*Essential XOR identities - O(1)*

```java
a ^ a = 0           // Self-cancellation property
a ^ 0 = a           // Identity property
a ^ b ^ b = a       // Find unique element in duplicates
```

### Bit Manipulation Patterns

#### Swap Without Temp Variable

*Swap two numbers using XOR - O(1)*

```java
a ^= b;  // Step 1
b ^= a;  // Step 2
a ^= b;  // Step 3: values swapped
```

#### Generate All Subsets

*Generate all 2^n subsets using bitmasks - O(n * 2^n)*

```java
for (int mask = 0; mask < (1 << n); mask++) {  // Iterate through all masks
    List<Integer> subset = new ArrayList<>();
    for (int i = 0; i < n; i++) {
        if ((mask & (1 << i)) != 0) {  // Check if ith element is in subset
            subset.add(nums[i]);
        }
    }
}
```

#### Range XOR

*Calculate XOR of range [L, R] - O(1) with preprocessing*

```java
int xorRange = xor(R) ^ xor(L - 1);  // XOR from 0 to R, cancel 0 to L-1
```

---

## 🧮 Math Utilities

### Common Math Operations

#### Min/Max and Absolute

*Basic comparison operations - O(1)*

```java
Math.min(a, b);     // Return smaller of two values
Math.max(a, b);     // Return larger of two values
Math.abs(x);        // Return absolute value
```

#### Power & Root

*Exponential and root calculations*

```java
Math.pow(base, exp);  // Calculate base^exp - O(log exp)
Math.sqrt(x);         // Square root - O(1)
Math.cbrt(x);         // Cube root - O(1)
```

#### Rounding Operations

*Round floating point numbers - O(1)*

```java
Math.ceil(x);       // Round up to nearest integer
Math.floor(x);      // Round down to nearest integer
Math.round(x);      // Round to nearest integer
```

#### Random Numbers

*Generate random values - O(1)*

```java
// Random int in [0, n)
Random random = new Random();
int rand = random.nextInt(n);
```

### GCD & LCM

#### Greatest Common Divisor

*Find GCD using Euclidean algorithm - O(log min(a,b))*

```java
int gcd(int a, int b) {
    while (b != 0) {
        int temp = b;
        b = a % b;  // Remainder becomes new divisor
        a = temp;
    }
    return a;  // GCD when remainder is 0
}
```

#### Least Common Multiple

*Calculate LCM using GCD - O(log min(a,b))*

```java
int lcm(int a, int b) {
    return (a * b) / gcd(a, b);  // LCM formula using GCD
}
```

### Prime Numbers

#### Prime Check

*Check if number is prime - O(√n)*

```java
boolean isPrime(int n) {
    if (n <= 1) return false;      // 0, 1 not prime
    if (n <= 3) return true;       // 2, 3 are prime
    if (n % 2 == 0 || n % 3 == 0) return false;  // Divisible by 2 or 3
    for (int i = 5; i * i <= n; i += 6) {  // Check 6k±1 numbers
        if (n % i == 0 || n % (i + 2) == 0)
            return false;
    }
    return true;
}
```

#### Sieve of Eratosthenes

*Find all primes up to n - O(n log log n)*

```java
boolean[] sieve(int n) {
    boolean[] isPrime = new boolean[n + 1];
    Arrays.fill(isPrime, true);
    isPrime[0] = isPrime[1] = false;  // 0 and 1 not prime
    
    for (int i = 2; i * i <= n; i++) {
        if (isPrime[i]) {
            for (int j = i * i; j <= n; j += i) {  // Mark multiples as not prime
                isPrime[j] = false;
            }
        }
    }
    return isPrime;  // Array where isPrime[i] = true if i is prime
}
```

---

**Made with ☕ and 💻 by Sandeep**  
**Repository**: [ChauhanSandeep/DSA](https://github.com/ChauhanSandeep/DSA)
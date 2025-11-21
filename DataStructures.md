# 📘 Java Syntax Cheatsheet for DSA Interviews

> A comprehensive quick-reference guide to essential Java syntaxes, shortcuts, patterns, and techniques that are **crucial for technical interviews**. Master these to write cleaner, faster, and more expressive code.

---

## 🧮 Arrays & Primitives

### Array Basics

#### Declaration & Initialization
*Create arrays with different syntaxes - O(n)*

``` java
int[] arr = new int[n];            // Array of size n, initialized to 0
int[] arr = {1, 2, 3, 4, 5};       // Array literal
int[] arr = new int[]{1, 2, 3};    // Array initialization
int[][] matrix = new int[m][n];    // 2D array
```

#### Copying Arrays
*Create copies of arrays efficiently*

``` java
int[] copy = arr.clone();          // Shallow copy - O(n)
int[] copy = Arrays.copyOf(arr, length);  // Copy with new length - O(n)
int[] copy = Arrays.copyOfRange(arr, from, to);  // Copy range [from, to) - O(n)
System.arraycopy(src, srcPos, dest, destPos, length);  // Native copy - O(n)
```

#### Filling Arrays
*Set array values - O(n)*

``` java
Arrays.fill(arr, value);           // Fill entire array
Arrays.fill(arr, fromIndex, toIndex, value);  // Fill range
```

#### Sorting & Searching
*Sort in arrays*

``` java
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

``` java
List<Integer> list = Arrays.stream(arr).boxed().collect(Collectors.toList());  // Array to List - O(n)
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));  // Array literal to List - O(n)
int[] arr = list.stream().mapToInt(i -> i).toArray();  // List to array - O(n)
```

#### 2D Array to List
*Convert 2D arrays to lists*

``` java
int[][] arr = {{1,2}, {3,4}};
List<int[]> list = Arrays.asList(arr);  // Fixed-size list - O(1)
List<int[]> list = new ArrayList<>(Arrays.asList(arr));  // Mutable list - O(n)

List<List<Integer>> list = Arrays.stream(matrix)
            .map(row -> Arrays.stream(row)          // stream each row
                              .boxed()             // convert int -> Integer
                              .collect(Collectors.toList()))
            .collect(Collectors.toList());

```

### Array Utilities

#### Stream Operations
*Process arrays using streams - all O(n)*

``` java
Arrays.stream(arr).sum();          // Sum all elements
Arrays.stream(arr).max().getAsInt();  // Find maximum
Arrays.stream(arr).min().getAsInt();  // Find minimum
Arrays.stream(arr).average().getAsDouble();  // Calculate average
Arrays.stream(arr).count();        // Count elements
```

#### Array Comparison
*Compare arrays for equality*

``` java
Arrays.equals(arr1, arr2);         // Compare 1D arrays - O(n)
Arrays.deepEquals(arr1, arr2);     // Compare multi-dimensional - O(n)
```

#### String Representation
*Convert arrays to readable strings*

``` java
Arrays.toString(arr);              // 1D: [1, 2, 3] - O(n)
Arrays.deepToString(arr);          // Multi-dimensional - O(n)
```

### Primitive Type Conversions

#### String to Number
*Parse strings to numeric types - O(n)*

``` java
int x = Integer.parseInt(str);     // String to int
long x = Long.parseLong(str);      // String to long
double x = Double.parseDouble(str); // String to double
```

#### Number to String
*Convert numbers to strings - O(1) to O(log n)*

``` java
String s = String.valueOf(num);    // Any number to String
String s = Integer.toString(num);  // Int to String
```

#### Character-Number Conversions
*Convert between chars and digits - O(1)*

``` java
int digit = ch - '0';              // '5' -> 5 (char to int)
char ch = (char)('0' + digit);     // 5 -> '5' (int to char)
char ch = (char)(num + 'a');       // 0 -> 'a' (int to letter)
```

---

### List Operations

#### Creation
*Create lists with different characteristics*

``` java
// Mutable list created from a fixed set of integers
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));

 
List<Integer> list = Arrays.stream(nums1)
                           .boxed()             // Converts each primitive int to Integer
                           .collect(Collectors.toList());   // Collects into a mutable List<Integer>

List<String> list = List.of("a", "b", "c");  // Immutable (Java 9+)
```

#### Sorting
*Sort lists in various orders - O(n log n)*

``` java
list.sort(Comparator.naturalOrder());  // Sort in ascending order
list.sort(Comparator.reverseOrder());  // Sort in descending order
list.sort((a, b) -> a.getAge() - b.getAge()); // Sort in ascending age

Collections.sort(list);  // Sort using natural ordering
Collections.reverse(list);  // Reverse list order - O(n)

Collections.sort(list, (a, b) -> Integer.compare(a, b));  // Custom comparator
```

#### Utilities
*Common list utility operations*

``` java
Collections.frequency(list, element);  // Count occurrences - O(n)
Collections.swap(list, i, j);  // Swap two elements - O(1)
Collections.fill(list, value);  // Fill with value - O(n)
list.subList(fromIndex, toIndex);  // Get view (not copy) - O(1)
```

---

### Set Operations

#### Creation
*Create sets with different ordering guarantees*

``` java
Set<Integer> set = new HashSet<>(list);  // Unordered, O(1) operations
Set<Integer> set = new LinkedHashSet<>();  // Maintains insertion order
Set<Integer> set = new TreeSet<>();  // Sorted, O(log n) operations
```

#### Set Operations
*Perform mathematical set operations - O(n)*

``` java
set1.addAll(set2);      // Union: combine both sets
set1.retainAll(set2);   // Intersection: keep common elements
set1.removeAll(set2);   // Difference: remove elements in set2
```


### Map Operations

#### Safe Initialization & Updates
*Initialize map values safely and update entries efficiently - O(1) average*

``` java
map.putIfAbsent(key, new ArrayList<>());  // Add key only if absent
map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);  // Compute and add in one step
map.computeIfPresent(key, (k, v) -> v + 1);  // Update only if key exists
map.merge(key, 1, Integer::sum);  // Merge values, perfect for frequency counting - O(1)
map.getOrDefault(key, 0);  // Get value or return default - O(1)
```

#### Iteration
*Iterate through map entries efficiently - O(n)*

``` java
map.forEach((k, v) -> System.out.println(k + ": " + v));  // Lambda-based iteration
for (Map.Entry<K, V> entry : map.entrySet()) { }  // Traditional iteration
```

#### Quick Checks
*Check map contents - O(1) average*

``` java
map.containsKey(key);  // Check if key exists
map.containsValue(value);  // Check if value exists - O(n)
````

### TreeSet / TreeMap (Sorted)

#### TreeSet Operations
*Sorted set with navigational methods - O(log n) for all operations*

``` java
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

``` java
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

``` java
Queue<Integer> queue = new LinkedList<>();
queue.offer(x);         // Add to rear (better than add())
queue.poll();           // Remove and return front (null if empty)
queue.peek();           // View front without removing
```

#### Deque Operations
*Double-ended queue - all operations O(1)*

``` java
Deque<Integer> deque = new ArrayDeque<>();
deque.offerFirst(x);    // Add to front
deque.offerLast(x);     // Add to back
deque.pollFirst();      // Remove from front
deque.pollLast();       // Remove from back
deque.peekFirst();      // View front
deque.peekLast();       // View back
```
### Stack

#### Legacy Stack (Avoid)
*Old Stack class - synchronized overhead*

``` java
Stack<Integer> stack = new Stack<>();
stack.push(x);      // Push element - O(1)
stack.pop();        // Pop element - O(1)
stack.peek();       // View top - O(1)
stack.isEmpty();    // Check if empty - O(1)
stack.size();       // Get size - O(1)
```

#### Modern Stack (Recommended)
*Use Deque for better performance - all operations O(1)*

``` java
Deque<Integer> stack = new ArrayDeque<>();
stack.push(x);      // Push element (or addFirst)
stack.pop();        // Pop element (or removeFirst)
stack.peek();       // View top (or peekFirst)
```

### PriorityQueue (Heap)

#### Min-Heap Creation
*Default priority queue orders elements naturally*

``` java
PriorityQueue<Integer> minHeap = new PriorityQueue<>();  // Smallest element at top
```

#### Max-Heap Creation
*Reverse ordering for maximum element at top*

``` java
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());  // Safe
PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b, a));  // Safe
```

#### Basic Operations
*All operations except remove specific element*

``` java
pq.offer(x);        // Add element - O(log n)
pq.poll();          // Remove and return min/max - O(log n)
pq.peek();          // View min/max without removing - O(1)
pq.remove(x);       // Remove specific element - O(n)
```

#### Custom Comparators
*Priority queue for complex objects - O(log n) per operation*

``` java
PriorityQueue<int[]> pq = new PriorityQueue<>(
    Comparator.comparingInt(a -> a[0])  // Compare by first element
);
```

---

## 🔤 String Operations

### String Manipulation

#### Character Checks
*Validate character types - O(1)*

``` java
Character.isDigit(ch);         // Check if character is digit
Character.isLetter(ch);        // Check if character is letter
Character.isLetterOrDigit(ch); // Check if alphanumeric
Character.isWhitespace(ch);    // Check if whitespace
Character.isUpperCase(ch);     // Check if uppercase
Character.isLowerCase(ch);     // Check if lowercase
```

#### Case Conversion
*Convert case of characters and strings - O(n) for strings*

``` java
s.toLowerCase();               // Convert string to lowercase
s.toUpperCase();               // Convert string to uppercase
Character.toLowerCase(ch);     // Convert char to lowercase - O(1)
Character.toUpperCase(ch);     // Convert char to uppercase - O(1)
```

#### Common String Operations
*Essential string methods with their complexities*

``` java
s.trim();                      // Remove leading/trailing whitespace - O(n)
s.substring(start, end);       // Extract substring [start, end) - O(n)
s.charAt(i);                   // Get character at index - O(1)
s.indexOf(substring);          // Find first occurrence - O(n*m)
s.lastIndexOf(substring);      // Find last occurrence - O(n*m)
s.replace(oldChar, newChar);   // Replace all occurrences - O(n)
s.replaceAll(regex, replacement); // Replace with regex - O(n)
s.split("\\s+");               // Split by whitespace - O(n)
String.join(", ", list);       // Join with delimiter - O(n)
```

#### StringBuilder
*Mutable strings for efficient modifications - O(1) amortized per operation*

``` java
StringBuilder sb = new StringBuilder();
sb.append(str);                // Append string
sb.insert(index, str);         // Insert at position
sb.deleteCharAt(index);        // Delete character
sb.reverse();                  // Reverse string - O(n)
sb.toString();                 // Convert to String - O(n)
```

#### String Conversions
*Convert between String and other types*

``` java
char[] chars = s.toCharArray();    // String to char array - O(n)
String s = new String(chars);      // Char array to String - O(n)
String s = String.valueOf(value);  // Any type to String - O(1) to O(n)
```

### String Patterns

#### Palindrome Check
*Check if string reads same forwards and backwards - O(n)*

``` java
boolean isPalindrome = new StringBuilder(s).reverse().toString().equals(s);
```

#### Character Frequency
*Count occurrences of each character - O(n)*

``` java
Map<Character, Integer> freq = new HashMap<>();
for (char c : s.toCharArray()) {
    freq.merge(c, 1, Integer::sum);  // Increment count for each character
}
```

#### Anagram Check
*Verify if two strings have same character frequencies - O(n log n)*

``` java
char[] a1 = s1.toCharArray(), a2 = s2.toCharArray();
Arrays.sort(a1); Arrays.sort(a2);  // Sort both arrays
boolean isAnagram = Arrays.equals(a1, a2);  // Compare sorted arrays
```

---

## ⚖️ Comparators & Sorting

### Safe Comparators

#### Avoiding Integer Overflow
*Always use safe comparison methods - O(n log n) for sorting*

``` java
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

``` java
Collections.sort(list, Comparator.naturalOrder());   // Ascending order
Collections.sort(list, Comparator.reverseOrder());   // Descending order
Collections.sort(list, (a, b) -> a.compareTo(b));  // Ascending order

list.sort(Comparator.naturalOrder());   // Ascending order
list.sort(Comparator.reverseOrder());   // Descending order
```

#### Field-Based Sorting
*Sort objects by specific fields - O(n log n)*

``` java
list.sort(Comparator.comparing(Person::getAge));  // Sort by age
list.sort(Comparator.comparing(Person::getAge).reversed());  // Sort by age descending
```

#### Multi-Field Sorting
*Sort by multiple criteria - O(n log n)*

``` java
list.sort(Comparator.comparing(Person::getLastName)
                    .thenComparing(Person::getFirstName));  // Sort by last name, then first name
```

#### Null-Safe Comparators
*Handle null values safely during sorting - O(n log n)*

``` java
list.sort(Comparator.nullsFirst(Comparator.naturalOrder()));  // Nulls at start
list.sort(Comparator.nullsLast(Comparator.naturalOrder()));   // Nulls at end
```

#### Custom Comparators for Data Structures
*Use custom comparators in priority queues - O(log n) per operation*

``` java
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> 
    Integer.compare(a[0], b[0])  // Compare by first element
);
```

---

## 🌀 Streams & Lambdas

### Basic Stream Operations

#### Stream Creation
*Create streams from various sources - O(1) for creation*

``` java
Stream<Integer> stream = list.stream();         // From collection
Stream<Integer> stream = Arrays.stream(arr);    // From array
Stream<Integer> stream = Stream.of(1, 2, 3);   // From values
IntStream stream = IntStream.range(0, 10);      // Range [0, 10)
IntStream stream = IntStream.rangeClosed(0, 10); // Range [0, 10]
```

#### Filtering & Mapping
*Transform stream elements - O(n)*

``` java
list.stream()
    .filter(x -> x > 0)      // Keep only positive numbers
    .map(x -> x * 2)         // Double each element
    .collect(Collectors.toList());
```

#### FlatMap
*Flatten nested collections - O(n*m) where m is avg nested size*

``` java
list.stream()
    .flatMap(List::stream)   // Flatten list of lists into single stream
    .collect(Collectors.toList());
```

#### Distinct Elements
*Remove duplicates - O(n)*

``` java
list.stream().distinct().collect(Collectors.toList());  // Keep unique elements
```

#### Stream Sorting
*Sort stream elements - O(n log n)*

``` java
list.stream()
    .sorted()                                    // Natural order
    .sorted(Comparator.reverseOrder())          // Reverse order
    .sorted(Comparator.comparing(Person::getAge))  // By field
    .collect(Collectors.toList());
```

### Terminal Operations

#### Collect
*Gather stream elements into collections - O(n)*

``` java
list.stream().collect(Collectors.toList());     // Collect to List
list.stream().collect(Collectors.toSet());      // Collect to Set
list.stream().collect(Collectors.joining(", ")); // Join to String
```

#### Reduce
*Combine stream elements into single result - O(n)*

``` java
int sum = list.stream().reduce(0, Integer::sum);  // Sum all elements
int product = list.stream().reduce(1, (a, b) -> a * b);  // Product of elements
Optional<Integer> max = list.stream().reduce(Integer::max);  // Find maximum
```

#### Match Operations
*Test stream elements against predicate - O(n) worst case*

``` java
boolean anyPositive = list.stream().anyMatch(x -> x > 0);   // Any element matches
boolean allPositive = list.stream().allMatch(x -> x > 0);   // All elements match
boolean noneNegative = list.stream().noneMatch(x -> x < 0); // No element matches
```

#### Find Operations
*Find elements in stream - O(n) worst case*

``` java
Optional<Integer> first = list.stream().findFirst();  // First element
Optional<Integer> any = list.stream().findAny();      // Any element (parallel)
```

#### Count
*Count stream elements - O(n)*

``` java
long count = list.stream().filter(x -> x > 0).count();  // Count matching elements
```

#### Min/Max
*Find minimum or maximum - O(n)*

``` java
Optional<Integer> min = list.stream().min(Integer::compare);  // Minimum element
Optional<Integer> max = list.stream().max(Integer::compare);  // Maximum element
```

### Advanced Collectors

#### Group By
*Group elements by classifier - O(n)*

``` java
Map<Type, List<Item>> grouped = list.stream()
    .collect(Collectors.groupingBy(Item::getType));  // Group by type field
```

#### Group By with Counting
*Group and count elements - O(n)*

``` java
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

``` java
Map<Boolean, List<Integer>> partitioned = list.stream()
    .collect(Collectors.partitioningBy(x -> x > 0));  // Split positive/negative
```

#### Collect to Map
*Create map from stream elements - O(n)*

``` java
Map<Integer, String> map = list.stream()
    .collect(Collectors.toMap(Item::getId, Item::getName));  // Key-value pairs
```

#### Collect to Map with Duplicate Handling
*Handle duplicate keys when creating map - O(n)*

``` java
Map<Integer, String> map = list.stream()
    .collect(Collectors.toMap(
        Item::getId,           // Key mapper
        Item::getName,         // Value mapper
        (existing, replacement) -> existing  // Keep existing on duplicate
    ));
```

#### Joining Strings
*Concatenate stream elements - O(n)*

``` java
String result = list.stream()
    .map(String::valueOf)
    .collect(Collectors.joining(", ", "[", "]"));  // Format: [1, 2, 3]
```

---

## 🔢 Bit Manipulation

### Common Bit Operations

#### Check Bit Status
*Check if specific bit is set - O(1)*

``` java
boolean isSet = (num & (1 << i)) != 0;  // Check ith bit using mask
boolean isSet = ((num >> i) & 1) == 1;  // Check ith bit using shift
```

#### Modify Bits
*Set, clear, or toggle specific bits - O(1)*

``` java
num |= (1 << i);    // Set ith bit to 1
num &= ~(1 << i);   // Clear ith bit to 0
num ^= (1 << i);    // Toggle ith bit
```

#### Power of 2 Check
*Verify if number is power of 2 - O(1)*

``` java
boolean isPowerOf2 = (n > 0) && ((n & (n - 1)) == 0);  // True if power of 2
```

#### Count Set Bits
*Count number of 1s in binary representation*

``` java
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

``` java
int rightmost = n & (-n);   // Get rightmost set bit
n = n & (n - 1);            // Remove rightmost set bit
```

#### Parity Check
*Check if number is even or odd - O(1)*

``` java
boolean isEven = (num & 1) == 0;  // True if even, false if odd
```

#### XOR Properties
*Essential XOR identities - O(1)*

``` java
a ^ a = 0           // Self-cancellation property
a ^ 0 = a           // Identity property
a ^ b ^ b = a       // Find unique element in duplicates
```

### Bit Manipulation Patterns

#### Swap Without Temp Variable
*Swap two numbers using XOR - O(1)*

``` java
a ^= b;  // Step 1
b ^= a;  // Step 2
a ^= b;  // Step 3: values swapped
```

#### Generate All Subsets
*Generate all 2^n subsets using bitmasks - O(n * 2^n)*

``` java
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

``` java
int xorRange = xor(R) ^ xor(L - 1);  // XOR from 0 to R, cancel 0 to L-1
```

---

## 🧮 Math Utilities

### Common Math Operations

#### Min/Max and Absolute
*Basic comparison operations - O(1)*

``` java
Math.min(a, b);     // Return smaller of two values
Math.max(a, b);     // Return larger of two values
Math.abs(x);        // Return absolute value
```

#### Power & Root
*Exponential and root calculations*

``` java
Math.pow(base, exp);  // Calculate base^exp - O(log exp)
Math.sqrt(x);         // Square root - O(1)
Math.cbrt(x);         // Cube root - O(1)
```

#### Rounding Operations
*Round floating point numbers - O(1)*

``` java
Math.ceil(x);       // Round up to nearest integer
Math.floor(x);      // Round down to nearest integer
Math.round(x);      // Round to nearest integer
```

#### Random Numbers
*Generate random values - O(1)*

``` java
// Random int in [0, n)
Random random = new Random();
int rand = random.nextInt(n);
```

### GCD & LCM

#### Greatest Common Divisor
*Find GCD using Euclidean algorithm - O(log min(a,b))*

``` java
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

``` java
int lcm(int a, int b) {
    return (a * b) / gcd(a, b);  // LCM formula using GCD
}
```

### Prime Numbers

#### Prime Check
*Check if number is prime - O(√n)*

``` java
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

``` java
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

## 🎯 Common Patterns

### Two Pointers

#### Opposite Direction Pattern
*Two pointers moving towards each other - O(n)*

``` java
// Use case: Two Sum in sorted array, Container With Most Water
int left = 0, right = arr.length - 1;
while (left < right) {
    int sum = arr[left] + arr[right];
    if (sum == target) return new int[]{left, right};  // Found pair
    else if (sum < target) left++;   // Need larger sum
    else right--;                    // Need smaller sum
}
```

#### Same Direction Pattern
*Fast and slow pointers - O(n)*

``` java
// Use case: Remove duplicates, partition array
int slow = 0;
for (int fast = 1; fast < arr.length; fast++) {
    if (arr[fast] != arr[slow]) {      // Found new unique element
        arr[++slow] = arr[fast];        // Move it to slow pointer position
    }
}
```

### Sliding Window

#### Fixed Size Window
*Window of constant size k - O(n)*

``` java
// Use case: Maximum sum subarray of size k
int windowSum = 0;
for (int i = 0; i < k; i++) windowSum += arr[i];  // Initial window
int maxSum = windowSum;

for (int i = k; i < arr.length; i++) {
    windowSum += arr[i] - arr[i - k];  // Slide window: add new, remove old
    maxSum = Math.max(maxSum, windowSum);
}
```

#### Variable Size Window
*Window size changes based on condition - O(n)*

``` java
// Use case: Longest substring without repeating characters
int left = 0, maxLen = 0;
for (int right = 0; right < arr.length; right++) {
    // Add arr[right] to window
    while (/* window invalid condition */) {
        // Remove arr[left] from window
        left++;  // Shrink window from left
    }
    maxLen = Math.max(maxLen, right - left + 1);  // Update result
}
```

### Binary Search

#### Standard Binary Search
*Find exact element in sorted array - O(log n)*

``` java
int left = 0, right = arr.length - 1;
while (left <= right) {
    int mid = left + (right - left) / 2;  // Avoid overflow
    if (arr[mid] == target) return mid;   // Found target
    else if (arr[mid] < target) left = mid + 1;  // Search right half
    else right = mid - 1;                 // Search left half
}
return -1;  // Target not found
```

#### Find Leftmost (Lower Bound)
*Find first position where element >= target - O(log n)*

``` java
int left = 0, right = arr.length;
while (left < right) {
    int mid = left + (right - left) / 2;
    if (arr[mid] < target) left = mid + 1;  // Target is on right
    else right = mid;                        // Could be answer, search left
}
return left;  // Leftmost position
```

#### Find Rightmost (Upper Bound)
*Find last position where element <= target - O(log n)*

``` java
int left = 0, right = arr.length;
while (left < right) {
    int mid = left + (right - left) / 2;
    if (arr[mid] <= target) left = mid + 1;  // Can go further right
    else right = mid;                         // Too far, search left
}
return left - 1;  // Rightmost position
```

### Prefix Sum

#### 1D Prefix Sum
*Precompute cumulative sums for range queries - O(n) build, O(1) query*

``` java
int[] prefix = new int[arr.length + 1];
for (int i = 0; i < arr.length; i++) {
    prefix[i + 1] = prefix[i] + arr[i];  // Cumulative sum
}
// Query: Sum of range [i, j] = prefix[j + 1] - prefix[i]
```

#### 2D Prefix Sum
*Precompute 2D cumulative sums for submatrix queries - O(m*n) build, O(1) query*

``` java
int[][] prefix = new int[m + 1][n + 1];
for (int i = 1; i <= m; i++) {
    for (int j = 1; j <= n; j++) {
        prefix[i][j] = matrix[i-1][j-1]   // Current cell
                     + prefix[i-1][j]      // Top sum
                     + prefix[i][j-1]      // Left sum
                     - prefix[i-1][j-1];   // Remove overlap
    }
}
// Query: Sum of rectangle from (r1,c1) to (r2,c2) using inclusion-exclusion
```

### Union-Find (Disjoint Set)

*Efficiently manage disjoint sets with near-constant time operations*

``` java
class UnionFind {
    private int[] parent, rank;
    
    // Initialize n elements, each in its own set - O(n)
    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;  // Each element is its own parent
    }
    
    // Find root with path compression - O(α(n)) amortized
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);  // Path compression: flatten tree
        }
        return parent[x];
    }
    
    // Union two sets by rank - O(α(n)) amortized
    public boolean union(int x, int y) {
        int rootX = find(x), rootY = find(y);
        if (rootX == rootY) return false;  // Already in same set
        
        // Union by rank: attach smaller tree under larger tree
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;  // Increase rank when trees are equal
        }
        return true;
    }
}
```

---

## ⚠️ Interview Pitfalls

### Common Mistakes to Avoid

#### Integer Overflow in Comparators
*Always use safe comparison methods*

``` java
// ❌ WRONG: Subtraction can overflow
Arrays.sort(arr, (a, b) -> a[0] - b[0]);

// ✅ CORRECT: Safe comparison
Arrays.sort(arr, (a, b) -> Integer.compare(a[0], b[0]));
```

#### List to Array Conversion
*Always specify array type to avoid ClassCastException*

``` java
// ❌ WRONG: Returns Object[], causes ClassCastException
Object[] arr = list.toArray();

// ✅ CORRECT: Specify type
String[] arr = list.toArray(new String[0]);  // For String array
int[][] arr = list.toArray(new int[0][]);    // For 2D array
```

#### Modifying Collections During Iteration
*Don't modify collection while iterating with for-each*

``` java
// ❌ WRONG: Throws ConcurrentModificationException
for (int x : list) {
    if (x > 5) list.remove(x);
}

// ✅ CORRECT: Use iterator
Iterator<Integer> it = list.iterator();
while (it.hasNext()) {
    if (it.next() > 5) it.remove();  // Iterator's remove is safe
}
```

#### Arrays.asList() Limitations
*Returns fixed-size list, can't add/remove elements*

``` java
// ❌ WRONG: Throws UnsupportedOperationException
List<String> list = Arrays.asList("a", "b");
list.add("c");

// ✅ CORRECT: Wrap in mutable ArrayList
List<String> list = new ArrayList<>(Arrays.asList("a", "b"));
```

#### Array Equality Comparison
*Use Arrays.equals() instead of ==*

``` java
// ❌ WRONG: Compares references, not contents
if (arr1 == arr2)

// ✅ CORRECT: Compares contents
if (Arrays.equals(arr1, arr2))
```

#### Integer Object Comparison
*Integer cache only works for -128 to 127*

``` java
// ❌ WRONG: == compares references
Integer a = 128, b = 128;
if (a == b)  // false (different objects outside cache range)

// ✅ CORRECT: Use .equals()
if (a.equals(b))  // true (compares values)
```

#### String Concatenation in Loops
*Use StringBuilder to avoid O(n²) complexity*

``` java
// ❌ WRONG: Creates new string each iteration - O(n²)
String s = "";
for (int i = 0; i < n; i++) s += i;

// ✅ CORRECT: StringBuilder is mutable - O(n)
StringBuilder sb = new StringBuilder();
for (int i = 0; i < n; i++) sb.append(i);
```

### Edge Cases to Remember

#### Null and Empty Inputs
*Always validate inputs before processing*

``` java
if (arr == null || arr.length == 0) return;  // Handle null/empty arrays
```

#### Single Element
*Single element often requires special handling*

``` java
if (arr.length == 1) return arr[0];  // Base case for many algorithms
```

#### Integer Overflow Prevention
*Avoid overflow when calculating midpoint*

``` java
int mid = left + (right - left) / 2;  // Safe - prevents overflow
// NOT: int mid = (left + right) / 2;  // Can overflow if sum exceeds MAX_VALUE
```

#### Array Bounds
*Watch for off-by-one errors in loops*

``` java
for (int i = 0; i < arr.length; i++)   // ✅ Correct: 0 to length-1
for (int i = 0; i <= arr.length; i++)  // ❌ Wrong: ArrayIndexOutOfBoundsException
```

#### Negative Modulo
*Handle negative numbers correctly in modulo*

``` java
int mod = ((x % n) + n) % n;  // Handles negative x correctly
// Direct x % n gives negative result for negative x in Java
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
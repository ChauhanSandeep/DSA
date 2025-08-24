# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Repository Overview

**tesseract-core** is a Java-based algorithmic problem-solving repository focused on technical interview preparation. It contains implementations of common algorithms and data structures organized by topic, with comprehensive documentation and multiple solution approaches.

## Common Development Commands

### Basic Compilation and Execution

```bash
# Compile all Java files in the repository
javac $(find . -name "*.java")

# Run the main entry point
java Main

# Compile and run a specific algorithm class
javac Array/BinarySearch.java && java Array.BinarySearch

# Compile and run with package structure
javac tree/Node.java Tree/TreeNode.java && java Tree.TreeNode
```

### Running Individual Algorithm Examples

Most algorithm files contain `main` methods that demonstrate usage:

```bash
# Example: Run binary search demonstration
javac Array/BinarySearch.java && java array.BinarySearch

# Example: Run tree algorithm
javac Tree/MaximumDepthOfBinaryTree.java && java Tree.MaximumDepthOfBinaryTree

# Example: Run dynamic programming solution
javac DynamicProgramming/ClimbingStairs.java && java DynamicProgramming.ClimbingStairs
```

### Package Management Script

The repository includes utility scripts for managing package declarations:

```bash
# Update package declarations in frazsheet directory (legacy)
./update_packages.sh

# Reorganization script (already executed)
./reorganize_frazsheet.sh
```

## Architecture & Code Organization

### Directory Structure

The codebase is organized by algorithmic topics:

- **`Array/`** - Array manipulation, searching, sorting problems
- **`Bitwise/`** - Bit manipulation operations and techniques  
- **`Tree/`** & **`tree/`** - Binary tree implementations and algorithms (note: dual package structure)
- **`Graph/`** - Graph algorithms (BFS, DFS, shortest path, etc.)
- **`DynamicProgramming/`** - DP solutions for optimization problems
- **`String/`** - String processing and pattern matching
- **`LinkedList/`** - Linked list operations and algorithms
- **`StackQueue/`** - Stack and queue implementations
- **`Heap/`** - Priority queue and heap-based algorithms
- **`Backtracking/`** - Recursive backtracking solutions
- **`BinarySearch/`** - Binary search variations
- **`greedy/`** - Greedy algorithm implementations
- **`Hashing/`** - Hash table and hashing techniques
- **`Maths/`** - Mathematical algorithms and number theory
- **`Recursion/`** - Recursive problem solutions
- **`Multithreading/`** - Concurrency and threading examples
- **`DesignPatterns/`** - Design pattern implementations
- **`Design/`** - System design and data structure implementations
- **`Test/`** - Utility and testing files
- **`utils/`** - Helper utilities

### Key Files

- **`Main.java`** - Repository entry point with welcome message
- **`README.md`** - Java 8 syntax cheatsheet for interviews
- **`Errors.md`** - Common coding mistakes and fixes
- **`Bitwise/README.md`** - Comprehensive bitwise operations guide
- **`update_packages.sh`** - Script to fix package declarations

## Key Conventions & Patterns

### Code Documentation Pattern

Each algorithm file follows a consistent documentation structure:

```java
/**
 * ✅ Problem: [Problem Name]
 *
 * [Problem Description]
 *
 * 🔗 Leetcode: [URL if applicable]
 *
 * 🧠 Example:
 * Input: [example input]
 * Output: [example output]
 *
 * 🔍 Follow-up Questions:
 * 1. [Follow-up question]
 * 2. [Additional variations]
 */
```

### Method Documentation

Methods include comprehensive comments with complexity analysis:

```java
/**
 * [Method description and algorithm steps]
 *
 * Time Complexity: O(...)
 * Space Complexity: O(...)
 *
 * @param [parameter] [description]
 * @return [return value description]
 */
```

### Naming Conventions

- **Iterative solutions**: `methodName()`
- **Recursive solutions**: `methodNameRecursive()`
- **Helper methods**: `methodHelper()` or `helperName()`
- **Alternative approaches**: `methodNameOptimized()`, `methodNameDP()`

### Java 8 Best Practices

The codebase leverages modern Java features extensively:

```java
// Safe collection operations
map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
map.merge(key, 1, Integer::sum);

// Stream operations
list.stream()
    .filter(x -> x > 0)
    .sorted(Comparator.reverseOrder())
    .collect(Collectors.toList());

// Safe comparators (avoid overflow)
Arrays.sort(arr, (a, b) -> Integer.compare(a[0], b[0]));
```

## Critical Coding Guidelines

### Comparator Safety
**❌ Never use subtraction in comparators:**
```java
// WRONG - can cause integer overflow
Arrays.sort(arr, (a, b) -> a[0] - b[0]);

// CORRECT - use built-in comparison
Arrays.sort(arr, (a, b) -> Integer.compare(a[0], b[0]));
```

### Array Conversion Safety
**❌ Avoid generic `toArray()`:**
```java
// WRONG - returns Object[]
Object[] arr = list.toArray();

// CORRECT - specify type
int[][] arr = list.toArray(new int[0][]);
String[] arr = list.toArray(new String[0]);
```

### List Creation from Arrays
```java
// For mutable lists
List<String> mutableList = new ArrayList<>(Arrays.asList("a", "b", "c"));

// For fixed-size lists (read-only)
List<String> fixedList = Arrays.asList("a", "b", "c");
```

## Package Structure Guide

| Directory | Package Name | Sample Classes | Focus Area |
|-----------|--------------|----------------|-------------|
| `Array/` | `array` | `BinarySearch`, `TwoSum`, `ThreeSum` | Array algorithms |
| `Tree/` | `Tree` | `TreeNode`, `BinaryTreePaths`, `SerializeAndDeserializeBinaryTree` | Tree structures |
| `tree/` | `tree` | `Node` | Tree utilities (legacy) |
| `Graph/` | `Graph` | `BFS`, `DFS`, `AllPathsFromSourceToTarget` | Graph algorithms |
| `String/` | `String` | `LongestSubstring`, `LongestRepeatingCharacterReplacement` | String processing |
| `DynamicProgramming/` | `DynamicProgramming` | `ClimbingStairs`, `BurstBalloons` | DP solutions |
| `Bitwise/` | `Bitwise` | `MaximumXor`, `SumOfTwoIntegers` | Bit manipulation |
| `Design/` | `Design` | `LRUCache`, `DesignHashmap` | System design |

**Note**: The repository has mixed package naming conventions (`Tree` vs `tree`). When adding new files, use the existing package name for the directory.

## Quick Reference

<details>
<summary>Java 8 Collections Cheatsheet</summary>

```java
// Map operations
map.putIfAbsent(key, new ArrayList<>());
map.computeIfAbsent(key, k -> new ArrayList<>());
map.computeIfPresent(key, (k, v) -> v + 1);
map.merge(key, 1, Integer::sum);

// Stream operations
list.stream().filter(x -> x > 0).collect(Collectors.toList());
list.stream().map(String::toUpperCase).collect(Collectors.toList());
list.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());

// Collectors
Collectors.toMap(Item::getKey, Item::getValue, Integer::sum);
Collectors.groupingBy(Foo::getType);
Collectors.joining(", ");
```
</details>

<details>
<summary>Bitwise Operations Quick Reference</summary>

| Operation | Code | Use Case |
|-----------|------|----------|
| Check even/odd | `(num & 1) == 0` | Parity check |
| Power of 2 | `(n > 0) && ((n & (n - 1)) == 0)` | Validation |
| Count set bits | `n & (n - 1)` in loop | Bit counting |
| Get ith bit | `(num >> i) & 1` | Bit extraction |
| Set ith bit | `num \|= (1 << i)` | Bit setting |
| Clear ith bit | `num &= ~(1 << i)` | Bit clearing |
| Toggle ith bit | `num ^= (1 << i)` | Bit toggling |

</details>

## Development Workflow

1. **Adding New Algorithms**: Create in appropriate topic directory with package declaration
2. **Testing**: Each file typically includes a `main` method for demonstration
3. **Documentation**: Follow the established comment patterns with complexity analysis
4. **Package Consistency**: Use existing package names for each directory

## Common Algorithmic Patterns

- **Two Pointers**: Used extensively in `Array/` problems
- **Sliding Window**: Found in string and array problems  
- **DFS/BFS**: Core patterns in `Graph/` and `Tree/` directories
- **Dynamic Programming**: Memoization and tabulation in `DynamicProgramming/`
- **Monotonic Stack**: Referenced in `Errors.md` for next/previous element problems
- **Binary Search**: Variations in `BinarySearch/` directory with template patterns

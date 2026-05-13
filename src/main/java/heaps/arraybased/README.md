# Array-Based Heap (Max-Heap) Notes

This guide explains heaps from first principles and focuses on interview-ready understanding for array-based binary heaps.

## 1) What is a Heap?

A heap is a **complete binary tree** that satisfies a heap-order property.

- **Complete binary tree**: all levels are full except possibly the last, and the last level is filled left to right.
- **Max-heap property**: every parent is greater than or equal to its children.
- Because of this property, the root always stores the maximum element.

## 2) Why Array Representation Works

For a complete binary tree, nodes can be stored level-by-level in an array with no gaps.

For index `i` (0-based indexing):

- `parent(i) = (i - 1) / 2`
- `left(i) = 2 * i + 1`
- `right(i) = 2 * i + 2`

This is one of the most common interview formulas.

### Leaf and Internal Nodes

- Last non-leaf index is `(n / 2) - 1`
- Leaf nodes are from index `n / 2` to `n - 1`

This is why heap build starts from `(n / 2) - 1` and moves backward.

## 3) Core Heap Invariants

At all times:

- The active heap occupies indices `[0, size - 1]`
- Max-heap order holds for each node in that active range
- Elements beyond `size - 1` are ignored (or free capacity)

## 4) Operations and Intuition

### `peek()`

- Return root (`arr[0]`)
- Time: `O(1)`

### `extractMax()`

1. Save root (max).
2. Move last element to root.
3. Decrease size.
4. Sift down from root to restore order.

- Time: `O(log n)`

### `insert(value)`

1. Place value at end.
2. Sift up while parent is smaller.

- Time: `O(log n)`

### `increaseKey(index, newValue)`

1. Update value at index to a larger value.
2. Sift up because value may violate order with parent.

- Time: `O(log n)`

### `updateValue(index, newValue)`

- If value increases: sift up.
- If value decreases: sift down.

- Time: `O(log n)`

### `deleteAtIndex(index)`

Common practical flow:

1. Swap target with last element.
2. Decrease size.
3. Restore heap by choosing sift up or sift down based on new value position.

- Time: `O(log n)`

### `buildHeap(array)`

Bottom-up build:

1. Start at last non-leaf `(n / 2) - 1`
2. Sift down each node down to `0`

- Time: `O(n)` (important interview point)

## 5) Why Build-Heap is `O(n)`, not `O(n log n)`

Although sift-down can be `O(log n)`, most nodes are near leaves and move only a small distance. Summed work over all nodes becomes linear: `O(n)`.

## 6) Typical Interview Traps

- Mixing 0-based and 1-based formulas.
- Forgetting to shrink heap size before heapifying in extract/delete.
- Wrong last non-leaf computation.
- Using full array length instead of active `size`.
- Not handling empty heap / invalid index.

## 7) Edge Cases

- Empty heap operations (`peek`, `extractMax`) should throw or clearly return sentinel.
- Single element heap.
- Duplicates (still valid).
- Negative values (heap property still works).

## 8) Relation to Heap Sort and Priority Queue

- **Heap sort** repeatedly extracts max and places it at end of array.
- **Priority queue** is typically a heap under the hood and offers `insert` + `extract` operations efficiently.

## 9) Quick Revision Checklist

- Know parent/left/right formulas.
- Know last non-leaf index.
- Be able to code `siftUp` and `siftDown` without looking up notes.
- Explain why build-heap is `O(n)`.
- Explain extraction/update/delete with correct heap size handling.

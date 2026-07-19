package heaps.arraybased;

import java.util.Arrays;

/**
 * Problem: Array-Based Max Heap
 *
 * Implement a max heap backed by an array, supporting insertion, maximum lookup,
 * maximum extraction, key updates, deletion by index, and heap construction from
 * an existing array.
 *
 * Pattern:  Heap | Array representation | Sift up and heapify down
 *
 * Example:
 *   Input:  insert 10, insert 4, insert 15, then extractMax
 *   Output: 15
 *   Why:    a max heap always keeps the largest value at index 0.
 *
 * Follow-ups:
 *   1. How would you implement a min heap?
 *      Reverse the parent-child comparison in siftUp and heapifyDown.
 *   2. How do you support arbitrary remove by value?
 *      Maintain a value-to-indices map and then reuse deleteAtIndex.
 *   3. How do you make it generic?
 *      Store T[] with a Comparator<T> and replace integer comparisons.
 *   4. How do you make updates stable for equal priorities?
 *      Store insertion sequence numbers and compare them as a tie-breaker.
 */

public class ArrayMaxHeap {

    private int[] heap;
    private int size;

    public ArrayMaxHeap(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }
        this.heap = new int[capacity];
        this.size = 0;
    }

    public ArrayMaxHeap(int[] input) {
        if (input == null) {
            throw new IllegalArgumentException("Input array cannot be null.");
        }
        this.heap = Arrays.copyOf(input, Math.max(1, input.length));
        this.size = input.length;
        buildHeap();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int peek() {
        ensureNotEmpty("peek");
        return heap[0];
    }

    public void insert(int value) {
        ensureCapacity(size + 1);
        heap[size] = value;
        siftUp(size);
        size++;
    }

    public int extractMax() {
        ensureNotEmpty("extractMax");
        int maxValue = heap[0];
        swap(0, size - 1);
        size--;
        if (!isEmpty()) {
            heapifyDown(0);
        }
        return maxValue;
    }

    public void increaseKey(int index, int newValue) {
        validateIndex(index);
        if (newValue < heap[index]) {
            throw new IllegalArgumentException("newValue must be >= current value for increaseKey.");
        }
        heap[index] = newValue;
        siftUp(index);
    }

    public void updateValue(int index, int newValue) {
        validateIndex(index);
        int oldValue = heap[index];
        heap[index] = newValue;

        if (newValue > oldValue) {
            siftUp(index);
        } else if (newValue < oldValue) {
            heapifyDown(index);
        }
    }

    public int deleteAtIndex(int index) {
        validateIndex(index);

        int removedValue = heap[index];
        swap(index, size - 1);
        size--;

        if (index < size) {
            if (index > 0 && heap[index] > heap[parent(index)]) {
                siftUp(index);
            } else {
                heapifyDown(index);
            }
        }

        return removedValue;
    }

    public int[] toArray() {
        return Arrays.copyOf(heap, size);
    }

    /** Restores heap order for the current array contents. */
    private void buildHeap() {
        for (int i = (size / 2) - 1; i >= 0; i--) {
            heapifyDown(i);
        }
    }

    /** Moves a value upward until the max-heap parent rule is restored. */
    private void siftUp(int index) {
        int current = index;
        while (current > 0) {
            int parent = parent(current);
            if (heap[parent] >= heap[current]) {
                break;
            }
            swap(parent, current);
            current = parent;
        }
    }

    /** Moves a value downward until both children are no larger. */
    private void heapifyDown(int index) {
        int current = index;
        while (true) {
            int left = left(current);
            int right = right(current);
            int largest = current;

            if (left < size && heap[left] > heap[largest]) {
                largest = left;
            }
            if (right < size && heap[right] > heap[largest]) {
                largest = right;
            }

            if (largest == current) {
                break;
            }

            swap(current, largest);
            current = largest;
        }
    }

    /** Returns the parent index in the array heap. */
    private int parent(int index) {
        return (index - 1) / 2;
    }

    /** Returns the left child index in the array heap. */
    private int left(int index) {
        return (2 * index) + 1;
    }

    /** Returns the right child index in the array heap. */
    private int right(int index) {
        return (2 * index) + 2;
    }

    /** Throws if index is outside the active heap range. */
    private void validateIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of heap range: " + index);
        }
    }

    /** Throws if a heap operation requires at least one value. */
    private void ensureNotEmpty(String operation) {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty. Cannot perform: " + operation);
        }
    }

    /** Grows the backing array when minimumCapacity exceeds current capacity. */
    private void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity <= heap.length) {
            return;
        }
        int newCapacity = Math.max(minimumCapacity, heap.length * 2);
        heap = Arrays.copyOf(heap, newCapacity);
    }

    /** Swaps two positions in the backing array. */
    private void swap(int firstIndex, int secondIndex) {
        int temp = heap[firstIndex];
        heap[firstIndex] = heap[secondIndex];
        heap[secondIndex] = temp;
    }

    public static void main(String[] args) {
        int[] input = {12, 7, 25, 15, 8, 30, 3};
        ArrayMaxHeap maxHeap = new ArrayMaxHeap(input);
        int firstGot = maxHeap.extractMax();
        System.out.printf("input=%s extractMax -> %d  expected=%d%n",
                Arrays.toString(input), firstGot, 30);

        ArrayMaxHeap single = new ArrayMaxHeap(1);
        single.insert(42);
        int secondGot = single.peek();
        System.out.printf("input=%s peek -> %d  expected=%d%n",
                Arrays.toString(new int[]{42}), secondGot, 42);
    }
}

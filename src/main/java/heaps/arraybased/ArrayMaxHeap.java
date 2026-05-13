package heaps.arraybased;

import java.util.Arrays;

/**
 * Array-backed max-heap with interview-focused core operations.
 *
 * Operations:
 * - peek: O(1)
 * - insert: O(log n)
 * - extractMax: O(log n)
 * - increaseKey: O(log n)
 * - updateValue: O(log n)
 * - deleteAtIndex: O(log n)
 * - buildHeap (constructor from array): O(n)
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

    private void buildHeap() {
        for (int i = (size / 2) - 1; i >= 0; i--) {
            heapifyDown(i);
        }
    }

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

    private int parent(int index) {
        return (index - 1) / 2;
    }

    private int left(int index) {
        return (2 * index) + 1;
    }

    private int right(int index) {
        return (2 * index) + 2;
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of heap range: " + index);
        }
    }

    private void ensureNotEmpty(String operation) {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty. Cannot perform: " + operation);
        }
    }

    private void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity <= heap.length) {
            return;
        }
        int newCapacity = Math.max(minimumCapacity, heap.length * 2);
        heap = Arrays.copyOf(heap, newCapacity);
    }

    private void swap(int firstIndex, int secondIndex) {
        int temp = heap[firstIndex];
        heap[firstIndex] = heap[secondIndex];
        heap[secondIndex] = temp;
    }

    public static void main(String[] args) {
        int[] input = {12, 7, 25, 15, 8, 30, 3};
        ArrayMaxHeap maxHeap = new ArrayMaxHeap(input);

        System.out.println("Initial heap array: " + Arrays.toString(maxHeap.toArray()));
        System.out.println("Peek max: " + maxHeap.peek());

        int extracted = maxHeap.extractMax();
        System.out.println("Extracted max: " + extracted);
        System.out.println("After extractMax: " + Arrays.toString(maxHeap.toArray()));

        maxHeap.updateValue(2, 40);
        System.out.println("After updateValue(index=2, value=40): " + Arrays.toString(maxHeap.toArray()));

        maxHeap.insert(22);
        System.out.println("After insert(22): " + Arrays.toString(maxHeap.toArray()));

        int removed = maxHeap.deleteAtIndex(1);
        System.out.println("Removed at index 1: " + removed);
        System.out.println("After deleteAtIndex(1): " + Arrays.toString(maxHeap.toArray()));
    }
}

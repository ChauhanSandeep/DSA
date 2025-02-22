package three;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * A data structure that supports add, remove and removeRandomElement operations in O(1) time.
 * Uses a combination of HashMap and ArrayList to achieve constant time operations.
 * @param <T> Type of elements stored in the data structure
 */
public class O1DataStructure<T> {

  // Map stores element to index mapping for O(1) access
  private Map<T, Integer> elementToIndex;
  // List stores actual elements for O(1) random access
  private List<T> elements;
  // Random number generator for selecting random elements
  private Random random;

  public O1DataStructure() {
    elementToIndex = new HashMap<>();
    elements = new ArrayList<>();
    random = new Random();
  }

  /**
   * Adds an element to the data structure
   * @param val Element to be added
   * @return true if element was added, false if already exists
   */
  public boolean add(T val) {
    if (elementToIndex.containsKey(val)) {
      return false;
    }

    // Add element to list and store its index in map
    elementToIndex.put(val, elements.size());
    elements.add(val);
    return true;
  }

  /**
   * Removes specified element from the data structure
   * @param val Element to be removed
   * @return true if element was removed, false if not found
   */
  public boolean remove(T val) {
    if (!elementToIndex.containsKey(val)) {
      return false;
    }

    // Get index of element to remove
    int indexToRemove = elementToIndex.get(val);
    T lastElement = elements.get(elements.size() - 1);

    // Move last element to the position of element being removed
    elements.set(indexToRemove, lastElement);
    elementToIndex.put(lastElement, indexToRemove);

    // Remove the element
    elements.remove(elements.size() - 1);
    elementToIndex.remove(val);

    return true;
  }

  /**
   * Removes and returns a random element from the data structure
   * @return Random element from the data structure
   * @throws IllegalStateException if data structure is empty
   */
  public T removeRandomElement() {
    if (elements.isEmpty()) {
      throw new IllegalStateException("Data structure is empty");
    }

    // Get random index and corresponding element
    int randomIndex = random.nextInt(elements.size());
    T randomElement = elements.get(randomIndex);

    // Remove the element using existing remove method
    remove(randomElement);

    return randomElement;
  }

  /**
   * Returns the current size of the data structure
   * @return Number of elements in the data structure
   */
  public int size() {
    return elements.size();
  }
}

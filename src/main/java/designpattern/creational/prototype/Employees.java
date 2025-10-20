package designpattern.creational.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * Prototype Pattern - Employees Implementation
 *
 * The Prototype Pattern is a creational design pattern that creates new objects by
 * cloning existing objects (prototypes) rather than creating them from scratch.
 * This is useful when object creation is expensive or complex.
 *
 * Problem Solved:
 * - Expensive object initialization (e.g., loading data from database)
 * - Need for multiple similar objects with slight variations
 * - Want to avoid subclassing for different object configurations
 * - Complex object construction that should be reused
 *
 * Key Concepts:
 * 1. **Prototype**: An object that supports cloning (implements Cloneable)
 * 2. **Clone**: Creates a copy of the prototype
 * 3. **Deep Copy**: Clone creates independent copy of internal objects (used here)
 * 4. **Shallow Copy**: Clone shares references with original (not used here)
 *
 * This Implementation:
 * - Uses deep cloning for the employee list
 * - Each clone has its own independent list
 * - Changes to one clone don't affect others or the original
 * - Expensive loadData() operation is done only once
 *
 * Use Cases:
 * - Caching expensive objects and cloning for variations
 * - Creating multiple objects with similar initial state
 * - Avoiding repeated database/network calls for similar data
 * - Object pools where objects are cloned instead of created
 *
 * Real-World Examples:
 * - Object.clone() in Java
 * - Photoshop: Duplicate layer (creates prototype copy)
 * - Game development: Cloning game entities with same properties
 * - Database connection pools: Clone configuration objects
 *
 * Deep vs Shallow Copy:
 * - Shallow: Copies primitive values, shares object references
 * - Deep: Copies primitive values AND creates new copies of objects
 * - This class uses deep copy to ensure list independence
 *
 * Benefits:
 * - Performance: Avoids expensive initialization
 * - Flexibility: Can modify clones independently
 * - Reduced complexity: Don't need many subclasses
 *
 * Related Patterns:
 * - Abstract Factory: Creates families of related objects
 * - Builder: Constructs complex objects step by step
 * - Singleton: Ensures only one instance exists
 *
 * @author Sandeep Chauhan
 */
public class Employees implements Cloneable{

	private List<String> empList; // List of employee names

	/**
	 * Default constructor - creates empty employee list.
	 */
	public Employees(){
		empList = new ArrayList<String>();
	}

	/**
	 * Constructor with initial list - used by clone() method.
	 *
	 * @param list Initial list of employee names
	 */
	public Employees(List<String> list){
		this.empList=list;
	}

	/**
	 * Loads employee data from a data source (simulated).
	 *
	 * In a real application, this might:
	 * - Query a database
	 * - Make network API calls
	 * - Read from files
	 * - Perform expensive computations
	 *
	 * The Prototype Pattern allows this expensive operation to be performed
	 * once, then cloned multiple times without repeating the cost.
	 */
	public void loadData(){
		// Simulate expensive database operation
		// In reality, this might query: SELECT * FROM employees
		empList.add("Pankaj");
		empList.add("Raj");
		empList.add("David");
		empList.add("Lisa");
	}

	/**
	 * Gets the list of employees.
	 *
	 * @return List of employee names
	 */
	public List<String> getEmpList() {
		return empList;
	}

	/**
	 * Creates and returns a deep copy of this Employees object.
	 *
	 * Deep Copy Implementation:
	 * - Creates new ArrayList (not shared with original)
	 * - Copies all employee names to new list
	 * - Returns new Employees object with the copied list
	 *
	 * Why Deep Copy?
	 * - Ensures clone is independent from original
	 * - Changes to clone's list don't affect original
	 * - Changes to original don't affect clone
	 *
	 * Shallow Copy Alternative (not used):
	 * ```java
	 * return new Employees(this.empList); // Would share same list!
	 * ```
	 *
	 * @return Deep copy of this Employees object
	 * @throws CloneNotSupportedException if cloning is not supported
	 */
	@Override
	public Object clone() throws CloneNotSupportedException{
		// Create new list for deep copy
		List<String> temp = new ArrayList<String>();

		// Copy all elements from original list to new list
		for(String s : this.getEmpList()){
			temp.add(s);
		}

		// Return new Employees object with copied list
		return new Employees(temp);
	}

}
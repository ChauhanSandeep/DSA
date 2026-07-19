package designpattern.creational.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * Prototype pattern example backed by an employee list.
 *
 * Intent: create new objects by cloning a prepared prototype instead of repeating
 * expensive setup. Use it when several objects start with the same state but then
 * need independent changes.
 *
 * Participants: Employees is the prototype, clone() creates a deep copy, the
 * internal list is the copied state, and Driver is the client that customizes the
 * clones after one loadData() call.
 */
public class Employees implements Cloneable{

	private List<String> empList; // List of employee names

	/** Creates an empty employee prototype. */
	public Employees(){
		empList = new ArrayList<String>();
	}

	/** Creates an employee object backed by the supplied list. */
	public Employees(List<String> list){
		this.empList=list;
	}

	/** Loads the seed data that later clones copy. */
	public void loadData(){
		// Simulate expensive database operation
		// In reality, this might query: SELECT * FROM employees
		empList.add("Pankaj");
		empList.add("Raj");
		empList.add("David");
		empList.add("Lisa");
	}

	/** Returns the mutable employee list for this object. */
	public List<String> getEmpList() {
		return empList;
	}

	/** Creates a deep copy with a separate employee list. */
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

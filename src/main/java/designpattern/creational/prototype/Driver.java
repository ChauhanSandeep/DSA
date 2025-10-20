package designpattern.creational.prototype;

import java.util.List;

/**
 * Driver - Client Class for Prototype Pattern Demo
 *
 * This class demonstrates the Prototype Pattern by showing how to:
 * 1. Create an expensive object once (loading from "database")
 * 2. Clone the object multiple times instead of recreating it
 * 3. Modify clones independently without affecting the original or other clones
 *
 * Key Benefits Demonstrated:
 * - Performance: loadData() called only once, not for each object
 * - Independence: Each clone can be modified without affecting others
 * - Simplicity: Cloning is simpler than recreating complex objects
 *
 * Real-World Scenario:
 * Imagine a company has a list of employees loaded from a database.
 * Multiple departments need this list, but each wants to modify it
 * (add department-specific employees, remove irrelevant ones, etc.).
 * Instead of querying the database multiple times, we:
 * 1. Load once into a prototype object
 * 2. Clone the prototype for each department
 * 3. Each department modifies their own clone
 *
 * @author Sandeep Chauhan
 */
public class Driver {

    /**
     * Main method demonstrating Prototype Pattern usage.
     *
     * Demonstrates:
     * 1. Creating and initializing a prototype (expensive operation)
     * 2. Cloning the prototype multiple times (cheap operation)
     * 3. Modifying clones independently (deep copy ensures independence)
     *
     * @param args Command line arguments (not used)
     * @throws CloneNotSupportedException if cloning fails
     */
    public static void main(String[] args) throws CloneNotSupportedException {
        // Step 1: Create prototype and load expensive data (only done once!)
        System.out.println("Loading employee data from database (expensive operation)...");
        Employees emps = new Employees();
        emps.loadData();  // Expensive operation - database query, network call, etc.

        // Step 2: Clone the prototype (cheap operation, no database access)
        System.out.println("\nCloning employee objects (cheap operation)...");
        Employees empsNew = (Employees) emps.clone();   // Clone 1
        Employees empsNew1 = (Employees) emps.clone();  // Clone 2

        // Step 3: Modify clones independently
        System.out.println("\nModifying clones independently...");

        // Department A adds a new employee
        List<String> list = empsNew.getEmpList();
        list.add("John");

        // Department B removes an employee
        List<String> list1 = empsNew1.getEmpList();
        list1.remove("Pankaj");

        // Step 4: Verify that modifications are independent
        System.out.println("\nResults showing deep copy independence:");
        System.out.println("Original (emps):     " + emps.getEmpList());      // [Pankaj, Raj, David, Lisa]
        System.out.println("Clone 1 (empsNew):   " + list);                   // [Pankaj, Raj, David, Lisa, John]
        System.out.println("Clone 2 (empsNew1):  " + list1);                  // [Raj, David, Lisa]

        System.out.println("\nNotice:");
        System.out.println("- Original unchanged (still has all 4 employees)");
        System.out.println("- Clone 1 has John added");
        System.out.println("- Clone 2 has Pankaj removed");
        System.out.println("- Each clone is independent (deep copy)");
        System.out.println("- Database loaded only ONCE, but we have 3 independent objects!");
    }

}

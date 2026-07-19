package designpattern.creational.prototype;

import java.util.List;

/**
 * Demo client for the Prototype pattern.
 *
 * Intent: show that expensive seed data can be loaded once, cloned cheaply, and
 * then customized independently. Use this pattern when constructing similar
 * objects repeatedly would be slower or more complex than copying a prototype.
 *
 * Participants: Driver is the client, Employees is the prototype, loadData()
 * prepares the source object, and clone() creates independent copies.
 */
public class Driver {

    /** Clones one loaded Employees prototype and mutates each copy independently. */
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

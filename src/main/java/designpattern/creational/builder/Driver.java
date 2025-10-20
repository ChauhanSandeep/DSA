package designpattern.creational.builder;

/**
 * Driver - Client Class for Builder Pattern Demo
 *
 * This class demonstrates how to use the Builder Pattern to create BankAccount objects
 * in a readable, flexible, and maintainable way.
 *
 * Key Demonstration:
 * - Fluent interface makes code read like natural language
 * - Method chaining creates clean, readable object construction
 * - No need to remember parameter order (unlike constructors)
 * - Easy to see what each value represents
 * - Optional parameters can be omitted without creating multiple constructors
 *
 * Builder Pattern Benefits Shown:
 * 1. **Readability**: Clear what each parameter means (.owner("Sandeep"))
 * 2. **Flexibility**: Can set parameters in any order
 * 3. **Maintainability**: Adding new optional fields doesn't break existing code
 * 4. **Type Safety**: Compile-time checking of method names and types
 *
 * Compare with Constructor Approach (problematic):
 * ```java
 * // Confusing - what does each parameter mean?
 * BankAccount account = new BankAccount(1234L, "Sandeep", "Bangalore", 100.0, 4.25);
 * ```
 *
 * @author Sandeep Chauhan
 */
public class Driver {
    /**
     * Main method demonstrating Builder Pattern usage.
     *
     * Notice how the builder creates a fluent, readable construction process:
     * 1. Start with mandatory parameter (account number)
     * 2. Chain optional parameters in any order
     * 3. Call build() to get the final object
     */
    public static void main(String[] args) {
        // Create BankAccount using Builder Pattern
        // Each method call is self-documenting - no need to remember parameter order
        BankAccount bankAccount = new BankAccount.Builder(1234L)  // Mandatory: account number
                .branch("Bangalore")      // Optional: set branch
                .owner("Sandeep")         // Optional: set owner
                .balance(100L)            // Optional: set balance
                .rate(4.25)               // Optional: set interest rate
                .build();                 // Build the final object

        // Print the account details
        System.out.println(bankAccount);

        // Can also create accounts with different combinations of parameters
        BankAccount minimalAccount = new BankAccount.Builder(5678L)
                .owner("John Doe")
                .build();  // Only account number and owner set

        System.out.println(minimalAccount);
    }
}

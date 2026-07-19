package designpattern.creational.builder;

/**
 * Demo client for the Builder pattern.
 *
 * Intent: show fluent, named construction of BankAccount objects without exposing
 * a long constructor. Use this pattern when callers need readable setup for an
 * object with required and optional values.
 *
 * Participants: Driver is the client, BankAccount.Builder is the builder, and
 * BankAccount is the product returned by build().
 */
public class Driver {
    /** Builds accounts with different optional fields and prints them. */
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

package designpattern.creational.builder;

/**
 * Builder Pattern - BankAccount Implementation
 *
 * The Builder Pattern is a creational design pattern that provides a flexible solution
 * to construct complex objects step by step. It separates the construction of an object
 * from its representation, allowing the same construction process to create different
 * representations.
 *
 * Problem Solved:
 * - Telescoping constructor anti-pattern (many constructor overloads)
 * - Unclear parameter meaning when many parameters have same type
 * - Objects that need to be immutable after creation
 * - Objects with many optional parameters
 *
 * Builder Pattern Structure:
 * 1. **Product** (BankAccount): The complex object being built
 * 2. **Builder** (BankAccount.Builder): Constructs the product step by step
 * 3. **Private Constructor**: Prevents direct instantiation, forces use of builder
 * 4. **Fluent Interface**: Builder methods return 'this' for method chaining
 * 5. **build()**: Finalizes construction and returns the product
 *
 * Key Advantages:
 * - Readability: Clear what each parameter represents (e.g., .owner("John"))
 * - Flexibility: Can set parameters in any order
 * - Optional Parameters: Easy to handle (just don't call the setter)
 * - Immutability: Product object can be made immutable
 * - Validation: Can validate in build() before creating object
 * - Named Parameters: Java doesn't have named parameters, builder simulates this
 *
 * Example Usage:
 * ```java
 * BankAccount account = new BankAccount.Builder(1234L)
 *     .owner("John Doe")
 *     .branch("Main Street")
 *     .balance(10000.0)
 *     .rate(4.5)
 *     .build();
 * ```
 *
 * Comparison with Alternatives:
 * - Telescoping Constructors: Becomes unwieldy with many parameters
 * - JavaBeans Pattern: Mutable objects, can be in inconsistent state
 * - Builder Pattern: Best of both worlds - immutability + readability
 *
 * Real-World Examples:
 * - StringBuilder/StringBuffer in Java
 * - OkHttpClient.Builder for HTTP client configuration
 * - Retrofit.Builder for REST client setup
 * - AlertDialog.Builder in Android
 *
 * Related Patterns:
 * - Abstract Factory: Creates families of objects
 * - Prototype: Creates objects by cloning
 * - Factory Method: Simpler, for single-step creation
 *
 * @author Sandeep Chauhan
 */
public class BankAccount {

    // Product properties - final fields make the object immutable after construction
    private long accountNumber;    // Unique account identifier (mandatory)
    private String owner;          // Account owner name (optional)
    private String branch;         // Bank branch location (optional)
    private double balance;        // Current account balance (optional)
    private double interestRate;   // Interest rate percentage (optional)

    /**
     * Private constructor prevents direct instantiation.
     * Forces clients to use the Builder to create BankAccount objects.
     * This ensures objects are always fully constructed before use.
     */
    private BankAccount() {
        // Constructor is private - only Builder can create instances
    }

    /**
     * Returns string representation of the bank account.
     * Useful for debugging and logging.
     *
     * @return String containing all account details
     */
    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNumber=" + accountNumber +
                ", owner='" + owner + '\'' +
                ", branch='" + branch + '\'' +
                ", balance=" + balance +
                ", interestRate=" + interestRate +
                '}';
    }

    /**
     * Builder - Inner Static Class for Constructing BankAccount Objects
     *
     * The Builder provides a fluent interface for constructing BankAccount objects.
     * It holds the same fields as BankAccount and provides setter methods that
     * return the builder itself, enabling method chaining.
     *
     * Design Decision: Inner Static Class
     * - Static: Can be instantiated without BankAccount instance
     * - Inner: Has access to BankAccount's private constructor
     * - Enables: new BankAccount.Builder(...) syntax
     */
    public static class Builder {

        // Builder holds same fields as product
        private long accountNumber;
        private String owner;
        private String branch;
        private double balance;
        private double interestRate;

        /**
         * Constructor for Builder with mandatory parameters.
         *
         * Design Decision: Only mandatory fields in constructor
         * - accountNumber is required for all bank accounts
         * - Other fields are optional and set via builder methods
         *
         * @param accountNumber Unique account number (mandatory)
         */
        public Builder(long accountNumber) {
            this.accountNumber = accountNumber;
        }

        /**
         * Sets the account owner name.
         *
         * Fluent Interface: Returns 'this' to enable method chaining.
         * Example: builder.owner("John").branch("NYC").build()
         *
         * @param owner Name of the account owner
         * @return This builder instance for chaining
         */
        public Builder owner(String owner){
            this.owner = owner;
            return this; // Enables fluent interface / method chaining
        }

        /**
         * Sets the bank branch location.
         *
         * @param branch Bank branch name or location
         * @return This builder instance for chaining
         */
        public Builder branch(String branch){
            this.branch = branch;
            return this;
        }

        /**
         * Sets the account balance.
         *
         * @param balance Initial or current account balance
         * @return This builder instance for chaining
         */
        public Builder balance(double balance){
            this.balance = balance;
            return this;
        }

        /**
         * Sets the interest rate.
         *
         * @param interestRate Annual interest rate percentage
         * @return This builder instance for chaining
         */
        public Builder rate(double interestRate){
            this.interestRate = interestRate;
            return this;
        }

        /**
         * Builds and returns the final BankAccount object.
         *
         * This method:
         * 1. Creates a new BankAccount instance (using private constructor)
         * 2. Transfers all values from builder to the account
         * 3. Returns the fully initialized, immutable account object
         *
         * Enhancement Opportunities:
         * - Add validation (e.g., balance >= 0, interestRate >= 0)
         * - Throw exception if mandatory fields are missing
         * - Set default values for optional fields
         *
         * @return Fully constructed BankAccount object
         */
        public BankAccount build(){
            // Create the actual product using private constructor
            BankAccount account = new BankAccount();

            // Transfer values from builder to product
            account.accountNumber = this.accountNumber;
            account.owner = this.owner;
            account.branch = this.branch;
            account.balance = this.balance;
            account.interestRate = this.interestRate;

            // Could add validation here:
            // if (account.balance < 0) throw new IllegalStateException("Balance cannot be negative");

            return account; // Return fully initialized object
        }
    }

}
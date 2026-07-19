package designpattern.creational.builder;

/**
 * Builder pattern example for constructing a bank account.
 *
 * Intent: separate object construction from the final object so callers can set
 * optional values with readable, named steps instead of long constructor lists.
 * Use it when an object has mandatory and optional fields, many same-typed
 * parameters, or construction rules that should live in one place.
 *
 * Participants: BankAccount is the product, BankAccount.Builder is the builder,
 * the builder setter methods collect construction state, and build() creates the
 * finished product through the private constructor.
 */
public class BankAccount {

    // Product properties - final fields make the object immutable after construction
    private long accountNumber;    // Unique account identifier (mandatory)
    private String owner;          // Account owner name (optional)
    private String branch;         // Bank branch location (optional)
    private double balance;        // Current account balance (optional)
    private double interestRate;   // Interest rate percentage (optional)

    /** Prevents direct construction so callers use the builder. */
    private BankAccount() {
        // Constructor is private - only Builder can create instances
    }

    /** Formats all account fields for the demo output. */
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

    /** Builder that gathers BankAccount fields through a fluent API. */
    public static class Builder {

        // Builder holds same fields as product
        private long accountNumber;
        private String owner;
        private String branch;
        private double balance;
        private double interestRate;

        /** Starts a builder with the mandatory account number. */
        public Builder(long accountNumber) {
            this.accountNumber = accountNumber;
        }

        /** Sets the optional account owner and returns this builder. */
        public Builder owner(String owner){
            this.owner = owner;
            return this; // Enables fluent interface / method chaining
        }

        /** Sets the optional branch and returns this builder. */
        public Builder branch(String branch){
            this.branch = branch;
            return this;
        }

        /** Sets the optional balance and returns this builder. */
        public Builder balance(double balance){
            this.balance = balance;
            return this;
        }

        /** Sets the optional interest rate and returns this builder. */
        public Builder rate(double interestRate){
            this.interestRate = interestRate;
            return this;
        }

        /** Creates a BankAccount from the values collected by this builder. */
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

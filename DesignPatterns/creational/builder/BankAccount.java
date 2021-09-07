package DesignPatterns.creational.builder;

/**
 * Make constructor of Parent class private
 * Add static Builder class with same instance variables as Parent class
 * Make constructor with mandatory parameters
 * add setters for remaining instance variables with return type as Builder
 * add build method which creates Parent class object and return instance of it.
 */
public class BankAccount {

    private long accountNumber;
    private String owner;
    private String branch;
    private double balance;
    private double interestRate;

    //Fields omitted for brevity.
    private BankAccount() {
        //Constructor is now private.
    }

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

    public static class Builder {

        private long accountNumber;
        private String owner;
        private String branch;
        private double balance;
        private double interestRate;

//        only mandatory fields will be in constructor
        public Builder(long accountNumber) {
            this.accountNumber = accountNumber;
        }

        public Builder owner(String owner){
            this.owner = owner;
//          By returning the builder each time, we can create a fluent interface.
            return this;
        }

        public Builder branch(String branch){
            this.branch = branch;

            return this;
        }

        public Builder balance(double balance){
            this.balance = balance;

            return this;
        }

        public Builder rate(double interestRate){
            this.interestRate = interestRate;

            return this;
        }

        public BankAccount build(){
            //Here we create the actual bank account object, which is always in a fully initialised state when it's returned.
            BankAccount account = new BankAccount();  //Since the builder is in the BankAccount class, we can invoke its private constructor.
            account.accountNumber = this.accountNumber;
            account.owner = this.owner;
            account.branch = this.branch;
            account.balance = this.balance;
            account.interestRate = this.interestRate;

            return account;
        }
    }

}
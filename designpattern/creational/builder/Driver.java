package designpattern.creational.builder;

public class Driver {
    public static void main(String[] args) {
        BankAccount bankAccount = new BankAccount.Builder(1234L)
                .branch("Bangalore")
                .owner("Sandeep")
                .balance(100L)
                .rate(4.25)
                .build();
        System.out.println(bankAccount);
    }
}

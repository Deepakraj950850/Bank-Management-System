import java.io.Serializable;

/**
 * Represents a bank account with basic details and a PIN for security.
 * Implements Serializable for future flexibility (though we use text files now).
 */
public class Account implements Serializable {
    private String fullName;
    private String accountNumber;
    private String pin;         // Encapsulated PIN
    private double balance;

    // Constructor
    public Account(String fullName, String accountNumber, String pin, double initialBalance) {
        this.fullName = fullName;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = initialBalance;
    }

    // --- Getters and Setters (Encapsulation) ---
    public String getFullName() {
        return fullName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    // Business Logic Methods
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    // Utility method to format for file storage: name,accNo,pin,balance
    public String toFileString() {
        return fullName + "," + accountNumber + "," + pin + "," + balance;
    }

    // Static method to create Account object from file string
    public static Account fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 4) {
            String name = parts[0];
            String accNo = parts[1];
            String pin = parts[2];
            double bal = Double.parseDouble(parts[3]);
            return new Account(name, accNo, pin, bal);
        }
        return null;
    }
}
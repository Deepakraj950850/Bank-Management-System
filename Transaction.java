import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single financial transaction (Deposit or Withdraw).
 */
public class Transaction {
    private String accountNumber;
    private String type; // "DEPOSIT" or "WITHDRAW"
    private double amount;
    private String timestamp;

    public Transaction(String accountNumber, String type, double amount) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Overloaded constructor for reading from file
    public Transaction(String accountNumber, String type, double amount, String timestamp) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    // --- Getters ---
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    // Format for file storage
    public String toFileString() {
        return accountNumber + "," + type + "," + amount + "," + timestamp;
    }

    // Static method to parse from file
    public static Transaction fromFileString(String line) {
        String[] parts = line.split(",");
        if (parts.length == 4) {
            String acc = parts[0];
            String type = parts[1];
            double amt = Double.parseDouble(parts[2]);
            String ts = parts[3];
            return new Transaction(acc, type, amt, ts);
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("%-20s %-10s $%-10.2f %s", timestamp, type, amount, "Balance Updated");
    }
}
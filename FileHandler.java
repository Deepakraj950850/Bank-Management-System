import java.io.*;
import java.util.*;

/**
 * Handles all file read/write operations for accounts.txt and transactions.txt.
 * Uses exception handling to manage I/O errors.
 */
public class FileHandler {
    private static final String ACCOUNTS_FILE = "accounts.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    // ------------------- Account Methods -------------------
    public static List<Account> readAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        File file = new File(ACCOUNTS_FILE);
        if (!file.exists()) {
            return accounts; // Return empty list if file doesn't exist yet
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Account acc = Account.fromFileString(line);
                if (acc != null) {
                    accounts.add(acc);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading accounts file: " + e.getMessage());
        }
        return accounts;
    }

    public static void writeAllAccounts(List<Account> accounts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNTS_FILE))) {
            for (Account acc : accounts) {
                writer.write(acc.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to accounts file: " + e.getMessage());
        }
    }

    public static Account findAccount(String accountNumber) {
        List<Account> accounts = readAllAccounts();
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                return acc;
            }
        }
        return null;
    }

    public static boolean updateAccount(Account updatedAccount) {
        List<Account> accounts = readAllAccounts();
        boolean found = false;
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getAccountNumber().equals(updatedAccount.getAccountNumber())) {
                accounts.set(i, updatedAccount);
                found = true;
                break;
            }
        }
        if (found) {
            writeAllAccounts(accounts);
            return true;
        }
        return false;
    }

    public static boolean addAccount(Account newAccount) {
        List<Account> accounts = readAllAccounts();
        // Check if account number already exists
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(newAccount.getAccountNumber())) {
                return false; // Account number already exists
            }
        }
        accounts.add(newAccount);
        writeAllAccounts(accounts);
        return true;
    }

    // ------------------- Transaction Methods -------------------
    public static void saveTransaction(Transaction transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            writer.write(transaction.toFileString());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving transaction: " + e.getMessage());
        }
    }

    public static List<Transaction> getTransactionsForAccount(String accountNumber) {
        List<Transaction> allTransactions = new ArrayList<>();
        File file = new File(TRANSACTIONS_FILE);
        if (!file.exists()) {
            return allTransactions;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Transaction t = Transaction.fromFileString(line);
                if (t != null && t.getAccountNumber().equals(accountNumber)) {
                    allTransactions.add(t);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading transactions: " + e.getMessage());
        }
        return allTransactions;
    }
}
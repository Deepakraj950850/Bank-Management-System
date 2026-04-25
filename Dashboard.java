import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Dashboard Screen: Shows balance and allows Deposit, Withdraw, and View History.
 */
public class Dashboard extends JFrame {
    private Account currentAccount;
    private JLabel welcomeLabel;
    private JLabel balanceLabel;
    private JTextField amountField;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton historyButton;
    private JButton logoutButton;

    public Dashboard(Account account) {
        this.currentAccount = account;
        setTitle("Bank Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
        updateBalanceDisplay();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Welcome Label
        welcomeLabel = new JLabel("Welcome, " + currentAccount.getFullName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(welcomeLabel, gbc);

        // Account Number Display
        JLabel accLabel = new JLabel("Account: " + currentAccount.getAccountNumber());
        accLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridy = 1;
        mainPanel.add(accLabel, gbc);

        // Balance Display
        balanceLabel = new JLabel();
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        balanceLabel.setForeground(new Color(0, 102, 0));
        gbc.gridy = 2;
        mainPanel.add(balanceLabel, gbc);

        // Separator
        gbc.gridy = 3;
        mainPanel.add(new JSeparator(), gbc);

        // Amount Input
        gbc.gridwidth = 1;
        gbc.gridy = 4;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Enter Amount:"), gbc);

        amountField = new JTextField(10);
        gbc.gridx = 1;
        mainPanel.add(amountField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        depositButton = new JButton("Deposit");
        withdrawButton = new JButton("Withdraw");
        historyButton = new JButton("View History");
        logoutButton = new JButton("Logout");

        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(historyButton);
        buttonPanel.add(logoutButton);

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        // Action Listeners
        depositButton.addActionListener(new TransactionAction("DEPOSIT"));
        withdrawButton.addActionListener(new TransactionAction("WITHDRAW"));
        historyButton.addActionListener(e -> showTransactionHistory());
        logoutButton.addActionListener(e -> logout());

        add(mainPanel);
    }

    private void updateBalanceDisplay() {
        balanceLabel.setText(String.format("Current Balance: $%.2f", currentAccount.getBalance()));
    }

    private void refreshAccountData() {
        // Reload the latest account data from file
        Account latest = FileHandler.findAccount(currentAccount.getAccountNumber());
        if (latest != null) {
            this.currentAccount = latest;
            updateBalanceDisplay();
        }
    }

    private class TransactionAction implements ActionListener {
        private String type;

        public TransactionAction(String type) {
            this.type = type;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String amountText = amountField.getText().trim();
            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(Dashboard.this, "Please enter an amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    throw new NumberFormatException("Amount must be positive.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(Dashboard.this, "Invalid amount. Enter a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = false;
            String message = "";

            if (type.equals("DEPOSIT")) {
                currentAccount.deposit(amount);
                success = true;
                message = String.format("Successfully deposited $%.2f", amount);
            } else if (type.equals("WITHDRAW")) {
                if (currentAccount.withdraw(amount)) {
                    success = true;
                    message = String.format("Successfully withdrew $%.2f", amount);
                } else {
                    message = "Insufficient balance!";
                }
            }

            if (success) {
                // Save updated account to file
                FileHandler.updateAccount(currentAccount);
                // Record transaction
                Transaction transaction = new Transaction(currentAccount.getAccountNumber(), type, amount);
                FileHandler.saveTransaction(transaction);
                // Refresh UI
                refreshAccountData();
                amountField.setText("");
                JOptionPane.showMessageDialog(Dashboard.this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(Dashboard.this, message, "Transaction Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showTransactionHistory() {
        List<Transaction> transactions = FileHandler.getTransactionsForAccount(currentAccount.getAccountNumber());

        JFrame historyFrame = new JFrame("Transaction History");
        historyFrame.setSize(600, 400);
        historyFrame.setLocationRelativeTo(this);

        // Create table model
        String[] columnNames = {"Date & Time", "Type", "Amount", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        for (Transaction t : transactions) {
            Object[] row = {
                    t.getTimestamp(),
                    t.getType(),
                    String.format("$%.2f", t.getAmount()),
                    "Completed"
            };
            model.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        historyFrame.add(scrollPane);

        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transactions found.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            historyFrame.setVisible(true);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new Login().setVisible(true);
        }
    }
}
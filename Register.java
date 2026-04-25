import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Registration Screen for new bank accounts.
 */
public class Register extends JFrame {
    private JTextField fullNameField;
    private JTextField accNumberField;
    private JPasswordField pinField;
    private JTextField initialDepositField;
    private JButton registerButton;
    private JButton backButton;

    public Register() {
        setTitle("Bank Management System - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("NEW ACCOUNT REGISTRATION");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Full Name
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Full Name:"), gbc);
        fullNameField = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(fullNameField, gbc);

        // Account Number
        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Account Number:"), gbc);
        accNumberField = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(accNumberField, gbc);

        // PIN
        gbc.gridy = 3;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("PIN (4-6 digits):"), gbc);
        pinField = new JPasswordField(15);
        gbc.gridx = 1;
        mainPanel.add(pinField, gbc);

        // Initial Deposit
        gbc.gridy = 4;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Initial Deposit:"), gbc);
        initialDepositField = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(initialDepositField, gbc);

        // Register Button
        registerButton = new JButton("Create Account");
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(registerButton, gbc);

        // Back Button
        backButton = new JButton("Back to Login");
        gbc.gridy = 6;
        mainPanel.add(backButton, gbc);

        // Actions
        registerButton.addActionListener(new RegisterAction());
        backButton.addActionListener(e -> {
            dispose();
            new Login().setVisible(true);
        });

        add(mainPanel);
    }

    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = fullNameField.getText().trim();
            String accNum = accNumberField.getText().trim();
            String pin = new String(pinField.getPassword());
            String depositStr = initialDepositField.getText().trim();

            // Validation
            if (name.isEmpty() || accNum.isEmpty() || pin.isEmpty() || depositStr.isEmpty()) {
                JOptionPane.showMessageDialog(Register.this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (pin.length() < 4 || pin.length() > 6) {
                JOptionPane.showMessageDialog(Register.this, "PIN must be 4-6 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double initialDeposit;
            try {
                initialDeposit = Double.parseDouble(depositStr);
                if (initialDeposit < 0) {
                    throw new NumberFormatException("Negative amount");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(Register.this, "Invalid deposit amount. Enter a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create Account
            Account newAccount = new Account(name, accNum, pin, initialDeposit);
            boolean success = FileHandler.addAccount(newAccount);

            if (success) {
                JOptionPane.showMessageDialog(Register.this,
                        "Account Created Successfully!\nPlease Login.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new Login().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(Register.this,
                        "Account Number already exists. Please choose a different number.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
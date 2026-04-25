import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Login Screen: Asks for Account Number and PIN.
 * Validates credentials using FileHandler.
 */
public class Login extends JFrame {
    private JTextField accNumberField;
    private JPasswordField pinField;
    private JButton loginButton;
    private JButton registerButton;

    public Login() {
        setTitle("Bank Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center on screen
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("BANK LOGIN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Account Number
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Account Number:"), gbc);

        accNumberField = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(accNumberField, gbc);

        // PIN
        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("PIN:"), gbc);

        pinField = new JPasswordField(15);
        gbc.gridx = 1;
        mainPanel.add(pinField, gbc);

        // Login Button
        loginButton = new JButton("Login");
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(loginButton, gbc);

        // Register Button
        registerButton = new JButton("Register New Account");
        gbc.gridy = 4;
        mainPanel.add(registerButton, gbc);

        // Add Listeners
        loginButton.addActionListener(new LoginAction());
        registerButton.addActionListener(e -> {
            dispose(); // Close Login window
            new Register().setVisible(true); // Open Register window
        });

        add(mainPanel);
    }

    // Inner class for Login Action
    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String accNumber = accNumberField.getText().trim();
            String pin = new String(pinField.getPassword());

            if (accNumber.isEmpty() || pin.isEmpty()) {
                JOptionPane.showMessageDialog(Login.this,
                        "Please enter both Account Number and PIN.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate using FileHandler
            Account account = FileHandler.findAccount(accNumber);
            if (account != null && account.getPin().equals(pin)) {
                JOptionPane.showMessageDialog(Login.this,
                        "Login Successful! Welcome, " + account.getFullName(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close Login
                new Dashboard(account).setVisible(true); // Open Dashboard
            } else {
                JOptionPane.showMessageDialog(Login.this,
                        "Invalid Account Number or PIN.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
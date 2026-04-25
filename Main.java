import javax.swing.*;

/**
 * Main entry point for the Bank Management System.
 * It sets the Look and Feel to Nimbus (if available) and launches the Login screen.
 */
public class Main {
    public static void main(String[] args) {
        // Set Look and Feel for a better GUI appearance
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, default look and feel is used.
            System.out.println("Nimbus Look and Feel not available, using default.");
        }

        // Launch the Login screen on the Event Dispatch Thread (thread-safe for Swing)
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
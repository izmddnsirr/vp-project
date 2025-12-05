import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            // Use the native look and feel (Windows on Windows machines)
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Using default look and feel: " + ex.getMessage());
        }

        // Launch GUI dalam Event Dispatch Thread (standard Swing)
        SwingUtilities.invokeLater(() -> new WelcomeFrame().setVisible(true));
    }
}
